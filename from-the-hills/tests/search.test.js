import { test } from "node:test";
import assert from "node:assert/strict";

import {
  allBuiltEntries,
  builtFileExists,
  descriptionOf,
  isInternal,
  loadBuiltPage,
  loadBuiltPages,
  sitemapEntries,
  titleOf,
} from "./support/built-pages.js";
import {
  INDEXED_PART,
  SEARCH_ADDRESS,
  SEARCH_BOX,
  SEARCH_DESCRIPTION,
  SEARCH_HEADING,
  SEARCH_INDEX_PATH,
  SEARCH_NO_SCRIPT_TEXT,
  SEARCH_PAGE_PATH,
  SEARCH_PLACEHOLDER,
  SEARCH_SCRIPT_ADDRESS,
  SEARCH_STYLESHEET_ADDRESS,
  SEARCH_TITLE,
  SITE_URL,
} from "./support/constants.js";

test("search index is built", () => {
  assert.ok(builtFileExists(SEARCH_INDEX_PATH), `${SEARCH_INDEX_PATH} is not built`);
});

test("every entry is indexed", () => {
  for (const { path, page } of allBuiltEntries()) {
    assert.equal(page(INDEXED_PART).length, 1, `${path}: not marked for the search index`);
  }
});

test("only entries are indexed", () => {
  const entryPaths = new Set(allBuiltEntries().map((entry) => entry.path));
  for (const { path, page } of loadBuiltPages()) {
    if (!entryPaths.has(path)) {
      assert.equal(page(INDEXED_PART).length, 0, `${path}: is not an entry but is marked for the index`);
    }
  }
});

test("search page has its title, description and heading", () => {
  const page = loadBuiltPage(SEARCH_PAGE_PATH);
  assert.equal(titleOf(page), SEARCH_TITLE);
  assert.equal(descriptionOf(page), SEARCH_DESCRIPTION);
  assert.equal(page("main h1").text().trim(), SEARCH_HEADING);
});

test("search page has the search box", () => {
  const page = loadBuiltPage(SEARCH_PAGE_PATH);
  assert.equal(page(`main ${SEARCH_BOX}`).length, 1, `no ${SEARCH_BOX} box in the main part`);
  assert.ok(page("main script").text().includes(SEARCH_PLACEHOLDER), `the box doesn't say "${SEARCH_PLACEHOLDER}"`);
});

test("search page explains itself without JavaScript", () => {
  const page = loadBuiltPage(SEARCH_PAGE_PATH);
  assert.equal(page("main noscript").text().trim(), SEARCH_NO_SCRIPT_TEXT);
});

test("search page is not in the sitemap", () => {
  const addresses = sitemapEntries().map((entry) => entry.address);
  assert.ok(!addresses.includes(`${SITE_URL}${SEARCH_ADDRESS}`), "the sitemap lists the search page");
});

test("search loads only from the site itself", () => {
  const page = loadBuiltPage(SEARCH_PAGE_PATH);
  const scripts = page("script[src]").map((_, script) => page(script).attr("src")).get();
  const stylesheets = page('link[rel="stylesheet"]').map((_, link) => page(link).attr("href")).get();
  assert.ok(scripts.includes(SEARCH_SCRIPT_ADDRESS), `doesn't load ${SEARCH_SCRIPT_ADDRESS}`);
  assert.ok(stylesheets.includes(SEARCH_STYLESHEET_ADDRESS), `doesn't load ${SEARCH_STYLESHEET_ADDRESS}`);
  assert.ok(scripts.every(isInternal), "loads a script from another site");
});
