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
    .map((path) => ({ path, page: loadBuiltPage(path) }));
}

export function titleOf(page) {
  return page("title").text();
}

export function descriptionOf(page) {
  return page('meta[name="description"]').attr("content");
}

export function repeatedValues(values) {
  return values.filter((value, index) => values.indexOf(value) !== index);
}

export function linksIn(page, nav) {
  return page(nav)
    .find("a")
    .map((_, link) => ({ text: page(link).text().trim(), href: page(link).attr("href") }))
    .get();
}
