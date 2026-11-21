import { readdirSync, readFileSync } from "node:fs";
import { join } from "node:path";
import { load } from "cheerio";

import { BUILT_SITE } from "./constants.js";

export function loadBuiltPage(path) {
  return load(readFileSync(join(BUILT_SITE, path), "utf8"));
}

export function loadBuiltPages() {
  return readdirSync(BUILT_SITE, { recursive: true })
    .filter((path) => path.endsWith(".html"))
    .map((path) => ({ path, $: loadBuiltPage(path) }));
}

export function linksIn($, nav) {
  return $(nav)
    .find("a")
    .map((_, link) => ({ text: $(link).text().trim(), href: $(link).attr("href") }))
    .get();
}
