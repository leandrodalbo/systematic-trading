import { test } from "node:test";
import assert from "node:assert/strict";
import { existsSync } from "node:fs";

import { addressOf, loadBuiltPage, postAddress } from "./support/built-pages.js";
import {
  BUILT_HOME_PAGE,
  HOME_HEADING,
  HOME_PAGE_ADDRESS,
  HOME_PAGE_PATH,
  LATEST_ON_HOME,
} from "./support/constants.js";

test("the build writes the home page", () => {
  assert.ok(existsSync(BUILT_HOME_PAGE), `No home page at ${BUILT_HOME_PAGE}`);
});

test("the home page shows its title as the one main heading", () => {
  const headings = loadBuiltPage(HOME_PAGE_PATH)("h1");
  assert.equal(headings.length, 1, "The home page should have one main heading");
  assert.equal(headings.text().trim(), HOME_HEADING);
});

test("the home page shows the latest entry of each section, with the section's name", () => {
  const page = loadBuiltPage(HOME_PAGE_PATH);
  const cards = page("main article").toArray();
  assert.equal(cards.length, LATEST_ON_HOME.length, "The home page should show one card per section");
  LATEST_ON_HOME.forEach(({ label, post }, index) => {
    const card = page(cards[index]);
    assert.ok(card.text().includes(label), `Card ${index + 1}: no "${label}" label`);
    assert.equal(card.find("h2").text().trim(), post.title, `Card ${index + 1}: wrong entry`);
    assert.equal(addressOf(card.find("h2 a").attr("href"), HOME_PAGE_ADDRESS), postAddress(post));
  });
});
