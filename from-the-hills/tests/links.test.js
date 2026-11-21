import { test } from "node:test";
import assert from "node:assert/strict";

import {
  addressOf,
  builtFileExists,
  builtFileFor,
  isInternal,
  loadBuiltPages,
  pageAddress,
} from "./support/built-pages.js";

test("every internal link, image, stylesheet and script points to a built file", () => {
  for (const { path, page } of loadBuiltPages()) {
    const links = page("a[href], img[src], link[href], script[src]")
      .map((_, element) => page(element).attr("href") ?? page(element).attr("src"))
      .get()
      .filter(isInternal);
    for (const link of links) {
      const file = builtFileFor(addressOf(link, pageAddress(path)));
      assert.ok(builtFileExists(file), `${path}: "${link}" points to a missing file`);
    }
  }
});
