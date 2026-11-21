#!/usr/bin/env bash
# Builds and tests the site, then copies _site/ to Hostinger over SSH.
# Settings come only from the environment (~/.zshrc); nothing here is secret.
set -euo pipefail

SETTINGS=(TRADEWEB_SERVER TRADEWEB_SSHUSER TRADEWEB_PORT TRADEWEB_FOLDER TRADEWEB_KEY)

for setting in "${SETTINGS[@]}"; do
  if [[ -z "${!setting:-}" ]]; then
    echo "Missing ${setting}. Set it in ~/.zshrc, then open a new terminal." >&2
    exit 1
  fi
done

cd "$(dirname "$0")"

# Eleventy never empties _site/, so start clean: only the fresh build may go live.
rm -rf _site
npm test --silent

# --delete: the server matches the build (old pages removed).
# .well-known holds Hostinger's SSL files, so it is never touched.
rsync -az --delete --exclude .well-known \
  -e "ssh -p ${TRADEWEB_PORT} -i ${TRADEWEB_KEY}" \
  _site/ "${TRADEWEB_SSHUSER}@${TRADEWEB_SERVER}:${TRADEWEB_FOLDER}/"
