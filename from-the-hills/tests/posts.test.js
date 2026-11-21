import { test } from "node:test";
import assert from "node:assert/strict";

import {
  allBuiltEntries,
  builtEntries,
  cardsIn,
  dayShownIn,
  descriptionOf,
  entryPictureAddress,
  headingOf,
  loadBuiltPage,
  titleOf,
} from "./support/built-pages.js";
import { ISO_DAY, KNOWN_ENTRIES, READABLE_DAY, SECTIONS, TITLE_ENDING } from "./support/constants.js";

test("the known entries are built with their title, description, day and picture", () => {
  const entries = allBuiltEntries();
  for (const known of KNOWN_ENTRIES) {
    const entry = entries.find(({ address }) => address === known.address);
    assert.ok(entry, `No page for ${known.address}`);
    assert.equal(headingOf(entry.page), known.title);
    assert.equal(descriptionOf(entry.page), known.description);
    assert.deepEqual(dayShownIn(entry.page), { day: known.day, readableDay: known.readableDay });
    assert.equal(entryPictureAddress(entry), known.picture, `${known.address}: wrong picture`);
  }
});

test("every entry page shows its title as the one main heading", () => {
  for (const { address, page } of allBuiltEntries()) {
    assert.equal(page("h1").length, 1, `${address}: expected one main heading`);
    assert.equal(titleOf(page), `${headingOf(page)}${TITLE_ENDING}`, `${address}: the heading and title differ`);
  }
});

test("every entry page shows the day it was written", () => {
  for (const { address, page } of allBuiltEntries()) {
    const { day, readableDay } = dayShownIn(page);
    assert.match(day ?? "", ISO_DAY, `${address}: no day in the datetime`);
    assert.match(readableDay, READABLE_DAY, `${address}: the day is not shown like "20 November 2026"`);
  }
});

test("each list page has its own title and description", () => {
  for (const section of SECTIONS) {
    const page = loadBuiltPage(section.path);
    assert.equal(titleOf(page), section.title);
    assert.equal(descriptionOf(page), section.description);
  }
});

test("each list page shows every entry of its section, newest first", () => {
  for (const section of SECTIONS) {
    const cards = cardsIn(loadBuiltPage(section.path), section.address);
    const listed = cards.map((card) => card.address).sort();
    const built = builtEntries(section).map((entry) => entry.address).sort();
    assert.deepEqual(listed, built, `${section.address}: not every entry is listed`);
    const days = cards.map((card) => card.day);
    assert.deepEqual(days, days.toSorted().reverse(), `${section.address}: not newest first`);
  }
});

test("each card shows its entry's day, description and picture", () => {
  const entries = allBuiltEntries();
  for (const section of SECTIONS) {
    for (const card of cardsIn(loadBuiltPage(section.path), section.address)) {
      const entry = entries.find(({ address }) => address === card.address);
      assert.ok(entry, `${section.address}: a card links to ${card.address}, which is not an entry`);
      assert.equal(card.day, dayShownIn(entry.page).day, `${card.address}: wrong day on the card`);
      assert.ok(card.text.includes(descriptionOf(entry.page)), `${card.address}: no description on the card`);
      assert.equal(card.picture, entryPictureAddress(entry), `${card.address}: wrong picture on the card`);
    }
  }
});

