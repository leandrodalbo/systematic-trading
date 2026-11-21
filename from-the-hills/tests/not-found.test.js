import { test } from "node:test";
import assert from "node:assert/strict";

import { builtFileLines, linksIn, loadBuiltPage, titleOf } from "./support/built-pages.js";
import {
  HTACCESS_NO_STORE_RULE,
  HTACCESS_NOT_FOUND_RULE,
  HTACCESS_PATH,
  NOT_FOUND_HEADING,
  NOT_FOUND_HOME_LINK,
  NOT_FOUND_PAGE_PATH,
  NOT_FOUND_TEXT,
  NOT_FOUND_TITLE,
} from "./support/constants.js";

test("the 404 page has its title, heading and text", () => {
  const page = loadBuiltPage(NOT_FOUND_PAGE_PATH);
  assert.equal(titleOf(page), NOT_FOUND_TITLE);
  assert.equal(page("main h1").text().trim(), NOT_FOUND_HEADING);
  assert.ok(page("main").text().includes(NOT_FOUND_TEXT), "The 404 page doesn't say what happened");
});

test("the 404 page links back to the home page", () => {
  const page = loadBuiltPage(NOT_FOUND_PAGE_PATH);
  assert.deepEqual(linksIn(page, "main"), [NOT_FOUND_HOME_LINK]);
});

test(".htaccess sends unknown addresses to the 404 page", () => {
  const lines = builtFileLines(HTACCESS_PATH);
  assert.ok(lines.includes(HTACCESS_NOT_FOUND_RULE), `.htaccess has no "${HTACCESS_NOT_FOUND_RULE}"`);
});

test(".htaccess keeps pages and scripts fresh", () => {
  const lines = builtFileLines(HTACCESS_PATH);
  assert.ok(lines.includes(HTACCESS_NO_STORE_RULE), `.htaccess has no "${HTACCESS_NO_STORE_RULE}"`);
});
