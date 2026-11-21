import { join } from "node:path";

export const SITE_FOLDER = join(import.meta.dirname, "..", "..");
export const BUILT_SITE = join(SITE_FOLDER, "_site");
export const HOME_PAGE_PATH = "index.html";
export const BUILT_HOME_PAGE = join(BUILT_SITE, HOME_PAGE_PATH);

export const SITE_NAME = "Quant Research Studio";
export const ONE_LINE_IDEA =
  "I test popular trading ideas on real data, and show what really works, with the scripts.";

export const TITLE_ENDING = ` · ${SITE_NAME}`;
export const HOME_TITLE = `Trading Enquiries & Market Research${TITLE_ENDING}`;
export const HOME_DESCRIPTION =
  "Daily market numbers and trading ideas tested on real data. Research only, not financial advice.";
export const MAX_TITLE_LENGTH = 60;
export const MAX_DESCRIPTION_LENGTH = 160;

export const DISCLAIMER = "Research only. Not financial advice.";
export const MENU_BUTTON_LABEL = "Open menu";
export const NAV_LINKS = Object.freeze([
  Object.freeze({ text: "Home", href: "/" }),
  Object.freeze({ text: "Market", href: "/market/" }),
  Object.freeze({ text: "Notes", href: "/notes/" }),
  Object.freeze({ text: "Concepts", href: "/concepts/" }),
]);

export const NOTES_PAGE_PATH = "notes/index.html";
export const NOTES_PAGE_ADDRESS = "/notes/";
export const NOTES_PAGE_TITLE = `Notes${TITLE_ENDING}`;
export const NOTES_PAGE_DESCRIPTION = "Short research notes: one question, one chart, what the data said.";
export const NOTE_CHART_FILE = "chart.png";

// Newest first.
export const NOTES = Object.freeze([
  Object.freeze({
    folder: "apple-pe",
    title: "Apple P/E, 2007–2026",
    description:
      "Apple, 2007–2026: after a high-P/E year, P/E fell the next year 6 times out of 9. After a low year, 3 out of 10.",
    day: "2026-11-21",
    readableDay: "21 November 2026",
  }),
  Object.freeze({
    folder: "spy-drops-vs-rises",
    title: "Drops vs rises in SPY, 1993–2026",
    description:
      "SPY above its 200-day average, 1993–2026: drops lasted 17 days, rises 56. Drops moved about twice as fast.",
    day: "2026-11-21",
    readableDay: "21 November 2026",
  }),
]);
