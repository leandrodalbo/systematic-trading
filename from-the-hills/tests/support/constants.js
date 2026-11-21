import { join } from "node:path";

export const SITE_FOLDER = join(import.meta.dirname, "..", "..");
export const BUILT_SITE = join(SITE_FOLDER, "_site");
export const HOME_PAGE_PATH = "index.html";
export const HOME_PAGE_ADDRESS = "/";
export const BUILT_HOME_PAGE = join(BUILT_SITE, HOME_PAGE_PATH);

export const SITE_NAME = "Quant Research Studio";
export const ONE_LINE_IDEA =
  "Checking the markets every day, testing trading ideas on real data, and building the system that trades them.";

export const TITLE_ENDING = ` · ${SITE_NAME}`;
export const HOME_HEADING = "Trading Enquiries & Market Research";
export const HOME_TITLE = `${HOME_HEADING}${TITLE_ENDING}`;
export const HOME_DESCRIPTION =
  "Daily market numbers and trading ideas tested on real data. Research only, not financial advice.";
export const MAX_TITLE_LENGTH = 60;
export const MAX_DESCRIPTION_LENGTH = 160;

export const DISCLAIMER = "Research only. Not financial advice.";
export const FOOTER_LINKS = Object.freeze([
  Object.freeze({ text: "X", href: "https://x.com/QResearchStudio" }),
  Object.freeze({ text: "GitHub", href: "https://github.com/leandrodalbo/systematic-trading" }),
]);
export const MENU_BUTTON_LABEL = "Open menu";
export const NAV_LINKS = Object.freeze([
  Object.freeze({ text: "Home", href: "/" }),
  Object.freeze({ text: "Market", href: "/market/" }),
  Object.freeze({ text: "Notes", href: "/notes/" }),
  Object.freeze({ text: "System", href: "/system/" }),
  Object.freeze({ text: "Concepts", href: "/concepts/" }),
]);

export const NOTES_PAGE_PATH = "notes/index.html";
export const NOTES_PAGE_ADDRESS = "/notes/";
export const NOTES_PAGE_TITLE = `Notes${TITLE_ENDING}`;
export const NOTES_PAGE_DESCRIPTION = "Short research notes: one question, one chart, what the data said.";
export const CHART_FILE = "chart.png";

// Newest first.
export const NOTES = Object.freeze([
  Object.freeze({
    section: "notes",
    folder: "apple-pe",
    picture: CHART_FILE,
    title: "Apple P/E, 2007–2026",
    description:
      "Apple, 2007–2026: after a high-P/E year, P/E fell the next year 6 times out of 9. After a low year, 3 out of 10.",
    day: "2026-11-21",
    readableDay: "21 November 2026",
  }),
  Object.freeze({
    section: "notes",
    folder: "spy-drops-vs-rises",
    picture: CHART_FILE,
    title: "Drops vs rises in SPY, 1993–2026",
    description:
      "SPY above its 200-day average, 1993–2026: drops lasted 17 days, rises 56. Drops moved about twice as fast.",
    day: "2026-11-21",
    readableDay: "21 November 2026",
  }),
]);

export const SYSTEM_PAGE_PATH = "system/index.html";
export const SYSTEM_PAGE_ADDRESS = "/system/";
export const SYSTEM_PAGE_TITLE = `System: Kotlin + Python for trading${TITLE_ENDING}`;
export const SYSTEM_PAGE_DESCRIPTION =
  "A backend view of a trading system: Spring Boot for data and orders, Python for research and signals, one set of files between them.";

// Newest first.
export const SYSTEM_ENTRIES = Object.freeze([
  Object.freeze({
    section: "system",
    folder: "one-system-two-languages",
    title: "One system, two languages",
    description:
      "Kotlin for the plumbing, Python for the research. Each language picked as the right tool for its job.",
    day: "2026-11-21",
    readableDay: "21 November 2026",
  }),
]);

export const CONCEPTS_PAGE_PATH = "concepts/index.html";
export const CONCEPTS_PAGE_ADDRESS = "/concepts/";
export const CONCEPTS_PAGE_TITLE = `Concepts${TITLE_ENDING}`;
export const CONCEPTS_PAGE_DESCRIPTION =
  "The words used in the notes and the market check, each in a few plain lines.";

// Newest first.
export const CONCEPTS = Object.freeze([
  Object.freeze({
    section: "concepts",
    folder: "pe-ratio",
    title: "What is the P/E ratio?",
    description: "Price ÷ earnings per share: how much you pay for each $1 a company earns in a year.",
    day: "2026-11-21",
    readableDay: "21 November 2026",
  }),
  Object.freeze({
    section: "concepts",
    folder: "mean-vs-median",
    title: "Mean vs median",
    description:
      "Mean = the average, median = the middle value. A few extreme values pull the mean, so the notes use the median.",
    day: "2026-11-21",
    readableDay: "21 November 2026",
  }),
  Object.freeze({
    section: "concepts",
    folder: "200-day-average",
    title: "What is the 200-day average?",
    description:
      "The average closing price of the last 200 trading days. Above it = rising market, below it = falling market.",
    day: "2026-11-21",
    readableDay: "21 November 2026",
  }),
]);

export const MARKET_PAGE_PATH = "market/index.html";
export const MARKET_PAGE_ADDRESS = "/market/";
export const MARKET_PAGE_TITLE = `Market${TITLE_ENDING}`;
export const MARKET_PAGE_DESCRIPTION =
  "One short check per trading day: SPY, QQQ, IWM and BTC against their highs and averages.";

// Newest first. Folder = the day of the closing prices.
export const MARKET_ENTRIES = Object.freeze([
  Object.freeze({
    section: "market",
    folder: "2026-11-20",
    picture: CHART_FILE,
    title: "SPY −11.5%, BTC under 200-week avg",
    description:
      "20 Nov 2026: SPY −11.5% from its 6 Oct high and below its 200-day average. BTC closed the week under its 200-week average, −53% from its 2025 high.",
    day: "2026-11-20",
    readableDay: "20 November 2026",
  }),
]);

// All four sections share one post layout, so one set of tests covers them all.
export const POSTS = Object.freeze([...MARKET_ENTRIES, ...NOTES, ...SYSTEM_ENTRIES, ...CONCEPTS]);

// The home page shows the latest entry of each section, in this order, with the section's name.
export const LATEST_ON_HOME = Object.freeze([
  Object.freeze({ label: "Market", post: MARKET_ENTRIES[0] }),
  Object.freeze({ label: "Notes", post: NOTES[0] }),
  Object.freeze({ label: "System", post: SYSTEM_ENTRIES[0] }),
  Object.freeze({ label: "Concepts", post: CONCEPTS[0] }),
]);

// Each page that lists posts as cards, with its posts newest first.
export const POST_LISTS = Object.freeze([
  Object.freeze({
    path: MARKET_PAGE_PATH,
    address: MARKET_PAGE_ADDRESS,
    title: MARKET_PAGE_TITLE,
    description: MARKET_PAGE_DESCRIPTION,
    posts: MARKET_ENTRIES,
  }),
  Object.freeze({
    path: NOTES_PAGE_PATH,
    address: NOTES_PAGE_ADDRESS,
    title: NOTES_PAGE_TITLE,
    description: NOTES_PAGE_DESCRIPTION,
    posts: NOTES,
  }),
  Object.freeze({
    path: SYSTEM_PAGE_PATH,
    address: SYSTEM_PAGE_ADDRESS,
    title: SYSTEM_PAGE_TITLE,
    description: SYSTEM_PAGE_DESCRIPTION,
    posts: SYSTEM_ENTRIES,
  }),
  Object.freeze({
    path: CONCEPTS_PAGE_PATH,
    address: CONCEPTS_PAGE_ADDRESS,
    title: CONCEPTS_PAGE_TITLE,
    description: CONCEPTS_PAGE_DESCRIPTION,
    posts: CONCEPTS,
  }),
]);
