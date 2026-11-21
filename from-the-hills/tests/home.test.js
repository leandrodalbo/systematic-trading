import { test } from "node:test";
import assert from "node:assert/strict";
import { existsSync } from "node:fs";

import { builtEntries, cardsIn, loadBuiltPage } from "./support/built-pages.js";
import {
  BUILT_HOME_PAGE,
  HOME_HEADING,
  HOME_PAGE_ADDRESS,
  HOME_PAGE_PATH,
  SECTIONS,
} from "./support/constants.js";

test("the build writes the home page", () => {
  assert.ok(existsSync(BUILT_HOME_PAGE), `No home page at ${BUILT_HOME_PAGE}`);
});

test("the home page shows its title as the one main heading", () => {
  const headings = loadBuiltPage(HOME_PAGE_PATH)("h1");
  assert.equal(headings.length, 1, "The home page should have one main heading");
  assert.equal(headings.text().trim(), HOME_HEADING);
});

test("the home page shows the newest entry of each section, with the section's name", () => {
  const sectionsWithEntries = SECTIONS.filter((section) => builtEntries(section).length > 0);
  const cards = cardsIn(loadBuiltPage(HOME_PAGE_PATH), HOME_PAGE_ADDRESS);
  assert.equal(cards.length, sectionsWithEntries.length, "The home page should show one card per section");
  sectionsWithEntries.forEach((section, index) => {
    const [newest] = cardsIn(loadBuiltPage(section.path), section.address);
    assert.ok(cards[index].text.includes(section.label), `Card ${index + 1}: no "${section.label}" label`);
    assert.equal(cards[index].address, newest.address, `Card ${index + 1}: not the newest ${section.label} entry`);
  });
});
