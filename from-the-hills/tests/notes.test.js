import { test } from "node:test";
import assert from "node:assert/strict";

import { addressOf, loadBuiltPage, pictureAddress } from "./support/built-pages.js";
import { NOTES, NOTES_PAGE_ADDRESS, NOTES_PAGE_PATH } from "./support/constants.js";

test("each card shows the note's chart, day and description", () => {
  const page = loadBuiltPage(NOTES_PAGE_PATH);
  page("main article").each((index, card) => {
    const note = NOTES[index];
    const chart = page(card).find("img").attr("src");
    assert.equal(addressOf(chart, NOTES_PAGE_ADDRESS), pictureAddress(note), `${note.folder}: wrong chart`);
    assert.equal(page(card).find(`time[datetime="${note.day}"]`).text().trim(), note.readableDay);
    assert.ok(page(card).text().includes(note.description), `${note.folder}: no description`);
  });
});
