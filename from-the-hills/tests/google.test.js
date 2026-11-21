import { test } from "node:test";
import assert from "node:assert/strict";

import {
  allBuiltEntries,
  builtFileLines,
  dayShownIn,
  loadBuiltPages,
  pageAddress,
  sitemapEntries,
} from "./support/built-pages.js";
import {
  HTACCESS_PATH,
  HTACCESS_WWW_RULES,
  NOT_FOUND_PAGE_PATH,
  ROBOTS_LINES,
  ROBOTS_PATH,
  SITE_URL,
} from "./support/constants.js";

test("the sitemap lists every page by its full address, except the 404 page", () => {
  const expected = loadBuiltPages()
    .map(({ path }) => path)
    .filter((path) => path !== NOT_FOUND_PAGE_PATH)
    .map((path) => `${SITE_URL}${pageAddress(path)}`)
    .sort();
  const listed = sitemapEntries().map((entry) => entry.address).sort();
  assert.deepEqual(listed, expected);
});

test("each entry in the sitemap carries its day; other pages carry none", () => {
  const days = new Map(allBuiltEntries().map(({ address, page }) => [`${SITE_URL}${address}`, dayShownIn(page).day]));
  for (const entry of sitemapEntries()) {
    assert.equal(entry.lastmod, days.get(entry.address) ?? "", `${entry.address}: wrong lastmod`);
  }
});

test("robots.txt allows everything and points to the sitemap", () => {
  const lines = builtFileLines(ROBOTS_PATH);
  for (const line of ROBOTS_LINES) {
    assert.ok(lines.includes(line), `robots.txt has no "${line}"`);
  }
});

test("every page except the 404 page names its full address as the one true copy", () => {
  for (const { path, page } of loadBuiltPages()) {
    const canonical = page('link[rel="canonical"]').attr("href");
    const expected = path === NOT_FOUND_PAGE_PATH ? undefined : `${SITE_URL}${pageAddress(path)}`;
    assert.equal(canonical, expected, `${path}: wrong canonical link`);
  }
});

test(".htaccess sends www addresses to the plain address", () => {
  const lines = builtFileLines(HTACCESS_PATH);
  for (const rule of HTACCESS_WWW_RULES) {
    assert.ok(lines.includes(rule), `.htaccess has no "${rule}"`);
  }
});
