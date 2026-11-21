import { test } from "node:test";
import assert from "node:assert/strict";

import { loadBuiltPages } from "./support/built-pages.js";

test("every image on every page has alt text", () => {
  for (const { path, page } of loadBuiltPages()) {
    page("img").each((_, image) => {
      const alt = page(image).attr("alt");
      assert.ok(alt?.trim(), `${path}: an image has no alt text (${page(image).attr("src")})`);
    });
  }
});
