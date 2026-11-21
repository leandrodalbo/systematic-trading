import { test } from "node:test";
import assert from "node:assert/strict";

import {
  addressOf,
  builtFileExists,
  descriptionOf,
  loadBuiltPage,
  noteAddress,
  noteChartAddress,
  notePagePath,
  titleOf,
} from "./support/built-pages.js";
import {
  NOTES,
  NOTES_PAGE_ADDRESS,
  NOTES_PAGE_DESCRIPTION,
  NOTES_PAGE_PATH,
  NOTES_PAGE_TITLE,
  TITLE_ENDING,
} from "./support/constants.js";

test("each note folder becomes a page", () => {
  for (const note of NOTES) {
    assert.ok(builtFileExists(notePagePath(note)), `No page for the note "${note.folder}"`);
  }
});

test("each note page has the note's title and description", () => {
  for (const note of NOTES) {
    const page = loadBuiltPage(notePagePath(note));
    assert.equal(titleOf(page), `${note.title}${TITLE_ENDING}`);
    assert.equal(descriptionOf(page), note.description);
  }
});

test("each note page shows its title as the one main heading", () => {
  for (const note of NOTES) {
    const headings = loadBuiltPage(notePagePath(note))("h1");
    assert.equal(headings.length, 1, `${note.folder}: expected one main heading`);
    assert.equal(headings.text().trim(), note.title);
  }
});

test("each note page shows the day it was written", () => {
  for (const note of NOTES) {
    const day = loadBuiltPage(notePagePath(note))(`time[datetime="${note.day}"]`);
    assert.equal(day.text().trim(), note.readableDay, `${note.folder}: the day is not shown`);
  }
});

test("each note page shows its chart", () => {
  for (const note of NOTES) {
    const page = loadBuiltPage(notePagePath(note));
    const charts = page("main img")
      .map((_, image) => addressOf(page(image).attr("src"), noteAddress(note)))
      .get();
    assert.ok(charts.includes(noteChartAddress(note)), `${note.folder}: the chart is not shown`);
  }
});

test("each note's chart is copied next to its page", () => {
  for (const note of NOTES) {
    assert.ok(builtFileExists(noteChartAddress(note)), `${note.folder}: the chart file is missing`);
  }
});

test("the notes page has its own title and description", () => {
  const page = loadBuiltPage(NOTES_PAGE_PATH);
  assert.equal(titleOf(page), NOTES_PAGE_TITLE);
  assert.equal(descriptionOf(page), NOTES_PAGE_DESCRIPTION);
});

test("the notes page lists every note, newest first", () => {
  const page = loadBuiltPage(NOTES_PAGE_PATH);
  const listed = page("main article h2")
    .map((_, heading) => page(heading).text().trim())
    .get();
  assert.deepEqual(listed, NOTES.map((note) => note.title));
});

test("each card links to its note", () => {
  const page = loadBuiltPage(NOTES_PAGE_PATH);
  page("main article").each((index, card) => {
    const link = page(card).find("a").attr("href");
    assert.equal(addressOf(link, NOTES_PAGE_ADDRESS), noteAddress(NOTES[index]));
  });
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
