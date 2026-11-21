import { test } from "node:test";
import assert from "node:assert/strict";
import { existsSync, readFileSync } from "node:fs";

import { BUILT_HOME_PAGE, ONE_LINE_IDEA, SITE_NAME } from "./support/constants.js";

test("the build writes the home page", () => {
  assert.ok(existsSync(BUILT_HOME_PAGE), `No home page at ${BUILT_HOME_PAGE}`);
});

test("the home page shows the site name", () => {
  const html = readFileSync(BUILT_HOME_PAGE, "utf8");
  assert.ok(html.includes(SITE_NAME), `The home page doesn't show "${SITE_NAME}"`);
});

test("the home page shows the one-line idea", () => {
  const html = readFileSync(BUILT_HOME_PAGE, "utf8");
  assert.ok(html.includes(ONE_LINE_IDEA), `The home page doesn't show "${ONE_LINE_IDEA}"`);
});
