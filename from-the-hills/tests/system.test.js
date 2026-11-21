import { test } from "node:test";
import assert from "node:assert/strict";

import { descriptionOf, loadBuiltPage, titleOf } from "./support/built-pages.js";
import {
  SYSTEM_ENTRIES,
  SYSTEM_PAGE_DESCRIPTION,
  SYSTEM_PAGE_PATH,
  SYSTEM_PAGE_TITLE,
} from "./support/constants.js";

test("the system page has its own title and description", () => {
  const page = loadBuiltPage(SYSTEM_PAGE_PATH);
  assert.equal(titleOf(page), SYSTEM_PAGE_TITLE);
  assert.equal(descriptionOf(page), SYSTEM_PAGE_DESCRIPTION);
});

test("the system page shows the data-flow diagram with a text label", () => {
  const diagram = loadBuiltPage(SYSTEM_PAGE_PATH)('main svg[role="img"]');
  assert.equal(diagram.length, 1, "No data-flow diagram on the system page");
  assert.ok(diagram.attr("aria-label")?.trim(), "The diagram has no text label");
});

test("the system page shows the overview before the entries", () => {
  const page = loadBuiltPage(SYSTEM_PAGE_PATH);
  const first = page("main svg, main article").first();
  assert.equal(first.prop("tagName").toLowerCase(), "svg", "The entries come before the overview");
});

test("an entry card shows no picture when the entry has none", () => {
  const page = loadBuiltPage(SYSTEM_PAGE_PATH);
  page("main article").each((index, card) => {
    const pictures = page(card).find("img");
    assert.equal(pictures.length, 0, `${SYSTEM_ENTRIES[index].folder}: the card shows a picture it doesn't have`);
  });
});
