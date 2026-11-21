import { test } from "node:test";
import assert from "node:assert/strict";

import { loadBuiltPage } from "./support/built-pages.js";
import { SYSTEM_PAGE_PATH } from "./support/constants.js";

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
