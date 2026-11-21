import { existsSync, readdirSync, readFileSync } from "node:fs";
import { join } from "node:path";
import { load } from "cheerio";

import { BUILT_SITE, SECTIONS, SITEMAP_PATH } from "./constants.js";

const ANY_SITE = "https://site.example";
const FOLDER_PAGE = "index.html";

export function builtFileExists(path) {
  return existsSync(join(BUILT_SITE, path));
}

// Every built entry of a section: one folder with an index.html under the section's folder.
export function builtEntries(section) {
  return readdirSync(join(BUILT_SITE, section.section), { withFileTypes: true })
    .filter((item) => item.isDirectory())
    .map((folder) => `${section.section}/${folder.name}/${FOLDER_PAGE}`)
    .filter(builtFileExists)
    .map((path) => ({ path, address: pageAddress(path), page: loadBuiltPage(path) }));
}

export function allBuiltEntries() {
  return SECTIONS.flatMap(builtEntries);
}

export function headingOf(page) {
  return page("main h1").text().trim();
}

// The day an entry or card shows: { day: "2026-11-20", readableDay: "20 November 2026" }.
export function dayShownIn(page, element = "main") {
  const time = page(element).find("time[datetime]").first();
  return { day: time.attr("datetime"), readableDay: time.text().trim() };
}

// The entry's own picture (set by the post layout), as an address; undefined when it has none.
export function entryPictureAddress(entry) {
  const src = entry.page("main article > img").attr("src");
  return src && addressOf(src, entry.address);
}

// The cards on a page: where each links to, its day, its text and its picture address.
export function cardsIn(page, fromAddress) {
  return page("main article")
    .map((_, card) => {
      const src = page(card).find("img").attr("src");
      return {
        address: addressOf(page(card).find("h2 a").attr("href"), fromAddress),
        day: dayShownIn(page, card).day,
        text: page(card).text(),
        picture: src && addressOf(src, fromAddress),
      };
    })
    .get();
}

export function addressOf(link, fromAddress) {
  return new URL(link, `${ANY_SITE}${fromAddress}`).pathname;
}

// The address a built page is served at: "notes/x/index.html" → "/notes/x/", "404.html" → "/404.html".
export function pageAddress(path) {
  const folder = path.endsWith(FOLDER_PAGE) ? path.slice(0, -FOLDER_PAGE.length) : path;
  return `/${folder}`;
}

// The built file an address is served from: "/notes/x/" → "notes/x/index.html".
export function builtFileFor(address) {
  const path = address.slice(1);
  return path === "" || path.endsWith("/") ? `${path}${FOLDER_PAGE}` : path;
}

export function isInternal(link) {
  return !link.startsWith("#") && !URL.canParse(link);
}

export function readBuiltFile(path) {
  return readFileSync(join(BUILT_SITE, path), "utf8");
}

export function builtFileLines(path) {
  return readBuiltFile(path)
    .split("\n")
    .map((line) => line.trim());
}

export function sitemapEntries() {
  const sitemap = load(readBuiltFile(SITEMAP_PATH), { xml: true });
  return sitemap("url")
    .map((_, url) => ({
      address: sitemap(url).find("loc").text().trim(),
      lastmod: sitemap(url).find("lastmod").text().trim(),
    }))
    .get();
}

export function loadBuiltPage(path) {
  return load(readBuiltFile(path));
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
