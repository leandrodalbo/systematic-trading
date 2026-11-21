import { test } from "node:test";
import assert from "node:assert/strict";

import {
  addressOf,
  builtFileExists,
  loadBuiltPage,
  noteChartAddress,
  postAddress,
  postPagePath,
} from "./support/built-pages.js";
import { NOTES, NOTES_PAGE_ADDRESS, NOTES_PAGE_PATH } from "./support/constants.js";

test("each note page shows its chart", () => {
  for (const note of NOTES) {
    const page = loadBuiltPage(postPagePath(note));
    const charts = page("main img")
      .map((_, image) => addressOf(page(image).attr("src"), postAddress(note)))
      .get();
    assert.ok(charts.includes(noteChartAddress(note)), `${note.folder}: the chart is not shown`);
  }
});

test("each note's chart is copied next to its page", () => {
  for (const note of NOTES) {
    assert.ok(builtFileExists(noteChartAddress(note)), `${note.folder}: the chart file is missing`);
  }
});

test("each card shows the note's chart, day and description", () => {
  const page = loadBuiltPage(NOTES_PAGE_PATH);
  page("main article").each((index, card) => {
    const note = NOTES[index];
    const chart = page(card).find("img").attr("src");
    assert.equal(addressOf(chart, NOTES_PAGE_ADDRESS), noteChartAddress(note), `${note.folder}: wrong chart`);
    assert.equal(page(card).find(`time[datetime="${note.day}"]`).text().trim(), note.readableDay);
    assert.ok(page(card).text().includes(note.description), `${note.folder}: no description`);
  });
});
