import { test } from "node:test";
import assert from "node:assert/strict";

import { linksIn, loadBuiltPage, loadBuiltPages } from "./support/built-pages.js";
import {
  DISCLAIMER,
  HOME_PAGE_PATH,
  MENU_BUTTON_LABEL,
  NAV_LINKS,
  ONE_LINE_IDEA,
  SITE_NAME,
} from "./support/constants.js";

test("every page's footer shows the disclaimer", () => {
  for (const { path, $ } of loadBuiltPages()) {
    assert.ok($("footer").text().includes(DISCLAIMER), `${path}: the footer doesn't show "${DISCLAIMER}"`);
  }
});

test("every page's header shows the site name", () => {
  for (const { path, $ } of loadBuiltPages()) {
    assert.ok($("header").text().includes(SITE_NAME), `${path}: the header doesn't show "${SITE_NAME}"`);
  }
});

test("every page has the desktop and mobile nav, each with the four links", () => {
  for (const { path, $ } of loadBuiltPages()) {
    const navs = $("header nav").toArray();
    assert.equal(navs.length, 2, `${path}: expected a desktop and a mobile nav in the header`);
    for (const nav of navs) {
      assert.deepEqual(linksIn($, nav), NAV_LINKS, `${path}: the nav links are not right`);
    }
  }
});

test("every page has the mobile menu button", () => {
  for (const { path, $ } of loadBuiltPages()) {
    const button = $(`header button[aria-label="${MENU_BUTTON_LABEL}"]`);
    assert.equal(button.length, 1, `${path}: no "${MENU_BUTTON_LABEL}" button in the header`);
  }
});

test("the home page shows its own content inside the layout", () => {
  const $ = loadBuiltPage(HOME_PAGE_PATH);
  assert.ok($("main").text().includes(ONE_LINE_IDEA), "The home page's main part doesn't show the one-line idea");
});
