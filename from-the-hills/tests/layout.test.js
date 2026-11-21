import { test } from "node:test";
import assert from "node:assert/strict";

import { linksIn, loadBuiltPage, loadBuiltPages } from "./support/built-pages.js";
import {
  DISCLAIMER,
  FOOTER_LINKS,
  HOME_PAGE_PATH,
  MENU_BUTTON_LABEL,
  NAV_LINKS,
  ONE_LINE_IDEA,
  SITE_NAME,
} from "./support/constants.js";

test("every page's footer shows the disclaimer", () => {
  for (const { path, page } of loadBuiltPages()) {
    assert.ok(page("footer").text().includes(DISCLAIMER), `${path}: the footer doesn't show "${DISCLAIMER}"`);
  }
});

test("every page's footer links to X and GitHub, marked as the site's own accounts", () => {
  for (const { path, page } of loadBuiltPages()) {
    assert.deepEqual(linksIn(page, "footer"), FOOTER_LINKS, `${path}: the footer links are not right`);
    page("footer a").each((_, link) => {
      assert.equal(page(link).attr("rel"), "me", `${path}: the ${page(link).text().trim()} link is not marked rel="me"`);
    });
  }
});

test("every page's header shows the site name", () => {
  for (const { path, page } of loadBuiltPages()) {
    assert.ok(page("header").text().includes(SITE_NAME), `${path}: the header doesn't show "${SITE_NAME}"`);
  }
});

test("every page has the desktop and mobile nav, each with every section link", () => {
  for (const { path, page } of loadBuiltPages()) {
    const navs = page("header nav").toArray();
    assert.equal(navs.length, 2, `${path}: expected a desktop and a mobile nav in the header`);
    for (const nav of navs) {
      assert.deepEqual(linksIn(page, nav), NAV_LINKS, `${path}: the nav links are not right`);
    }
  }
});

test("every page has the mobile menu button", () => {
  for (const { path, page } of loadBuiltPages()) {
    const button = page(`header button[aria-label="${MENU_BUTTON_LABEL}"]`);
    assert.equal(button.length, 1, `${path}: no "${MENU_BUTTON_LABEL}" button in the header`);
  }
});

test("the home page shows its own content inside the layout", () => {
  const page = loadBuiltPage(HOME_PAGE_PATH);
  assert.ok(page("main").text().includes(ONE_LINE_IDEA), "The home page's main part doesn't show the one-line idea");
});
