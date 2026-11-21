import { test } from "node:test";
import assert from "node:assert/strict";
import { existsSync } from "node:fs";

import { BUILT_HOME_PAGE } from "./support/constants.js";

test("the build writes the home page", () => {
  assert.ok(existsSync(BUILT_HOME_PAGE), `No home page at ${BUILT_HOME_PAGE}`);
});
