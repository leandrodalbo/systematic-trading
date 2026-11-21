import { test } from "node:test";
import assert from "node:assert/strict";

import {
  descriptionOf,
  loadBuiltPage,
  loadBuiltPages,
  repeatedValues,
  titleOf,
} from "./support/built-pages.js";
import {
  HOME_DESCRIPTION,
  HOME_PAGE_PATH,
  HOME_TITLE,
  MAX_DESCRIPTION_LENGTH,
  MAX_TITLE_LENGTH,
  TITLE_ENDING,
} from "./support/constants.js";

test("the home page has its own title", () => {
  assert.equal(titleOf(loadBuiltPage(HOME_PAGE_PATH)), HOME_TITLE);
});

test("the home page has its own description", () => {
  assert.equal(descriptionOf(loadBuiltPage(HOME_PAGE_PATH)), HOME_DESCRIPTION);
});

test("every page has a title of its own before the site name", () => {
  for (const { path, page } of loadBuiltPages()) {
    const title = titleOf(page);
    assert.ok(title.endsWith(TITLE_ENDING) && title.length > TITLE_ENDING.length, `${path}: no title`);
  }
});

test("every page has a description", () => {
  for (const { path, page } of loadBuiltPages()) {
    assert.ok(descriptionOf(page), `${path}: no description`);
  }
});

test("no two pages share a title", () => {
  const titles = loadBuiltPages().map(({ page }) => titleOf(page));
  assert.deepEqual(repeatedValues(titles), [], "Some pages share a title");
});

test("no two pages share a description", () => {
  const descriptions = loadBuiltPages().map(({ page }) => descriptionOf(page));
  assert.deepEqual(repeatedValues(descriptions), [], "Some pages share a description");
});

test("no title is longer than Google shows", () => {
  for (const { path, page } of loadBuiltPages()) {
    const length = titleOf(page).length;
    assert.ok(length <= MAX_TITLE_LENGTH, `${path}: the title has ${length} characters, max ${MAX_TITLE_LENGTH}`);
  }
});

test("no description is longer than Google shows", () => {
  for (const { path, page } of loadBuiltPages()) {
    const description = descriptionOf(page);
    assert.ok(description, `${path}: no description`);
    assert.ok(
      description.length <= MAX_DESCRIPTION_LENGTH,
      `${path}: the description has ${description.length} characters, max ${MAX_DESCRIPTION_LENGTH}`,
    );
  }
});
