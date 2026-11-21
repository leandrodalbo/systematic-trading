import { join } from "node:path";

export const SITE_FOLDER = join(import.meta.dirname, "..", "..");
export const BUILT_SITE = join(SITE_FOLDER, "_site");
export const HOME_PAGE_PATH = "index.html";
export const HOME_PAGE_ADDRESS = "/";
export const BUILT_HOME_PAGE = join(BUILT_SITE, HOME_PAGE_PATH);

export const SITE_NAME = "Quant Research Studio";
export const ONE_LINE_IDEA =
  "Checking the markets every day, studying known trading methods, and using them to build a real trading system.";

export const TITLE_ENDING = ` · ${SITE_NAME}`;
export const HOME_HEADING = "Trading Enquiries & Market Research";
export const HOME_TITLE = `${HOME_HEADING}${TITLE_ENDING}`;
export const HOME_DESCRIPTION =
  "Daily market numbers and known trading methods studied on real data. Research only, not financial advice.";
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

// Each section and its list page. Entries are found in the built site, so a new entry needs no
// test change: the rules below check every entry that is built.
export const SECTIONS = Object.freeze([
  Object.freeze({
    section: "market",
    label: "Market",
    path: "market/index.html",
    address: "/market/",
    title: `Market${TITLE_ENDING}`,
    description: "One short check per trading day: SPY, QQQ, IWM and BTC against their highs and averages.",
  }),
  Object.freeze({
    section: "notes",
    label: "Notes",
    path: "notes/index.html",
    address: "/notes/",
    title: `Notes${TITLE_ENDING}`,
    description: "Short research notes: one question, one chart, what the data said.",
  }),
  Object.freeze({
    section: "system",
    label: "System",
    path: "system/index.html",
    address: "/system/",
    title: `System: Kotlin + Python for trading${TITLE_ENDING}`,
    description:
      "A backend view of a trading system: Spring Boot for data and orders, Python for research and signals, one set of files between them.",
  }),
  Object.freeze({
    section: "concepts",
    label: "Concepts",
    path: "concepts/index.html",
    address: "/concepts/",
    title: `Concepts${TITLE_ENDING}`,
    description: "The words used in the notes and the market check, each in a few plain lines.",
  }),
]);
export const SYSTEM_PAGE_PATH = "system/index.html";

// A day on an entry: "2026-11-20" in the datetime, "20 November 2026" on the page.
export const ISO_DAY = /^\d{4}-\d{2}-\d{2}$/;
export const READABLE_DAY = /^\d{1,2} [A-Z][a-z]+ \d{4}$/;

// Fixed examples: past entries, one per section, that must stay as they are.
export const KNOWN_ENTRIES = Object.freeze([
  Object.freeze({
    address: "/market/2026-11-20/",
    title: "SPY −11.5%, BTC under 200-week avg",
    description:
      "20 Nov 2026: SPY −11.5% from its 6 Oct high and below its 200-day average. BTC closed the week under its 200-week average, −53% from its 2025 high.",
    day: "2026-11-20",
    readableDay: "20 November 2026",
    picture: "/market/2026-11-20/chart.png",
  }),
  Object.freeze({
    address: "/notes/apple-pe/",
    title: "Apple P/E, 2007–2026",
    description:
      "Apple, 2007–2026: after a high-P/E year, P/E fell the next year 6 times out of 9. After a low year, 3 out of 10.",
    day: "2026-11-21",
    readableDay: "21 November 2026",
    picture: "/notes/apple-pe/chart.png",
  }),
  Object.freeze({
    address: "/system/one-system-two-languages/",
    title: "One system, two languages",
    description:
      "Kotlin for the plumbing, Python for the research. Each language picked as the right tool for its job.",
    day: "2026-11-21",
    readableDay: "21 November 2026",
  }),
  Object.freeze({
    address: "/concepts/pe-ratio/",
    title: "What is the P/E ratio?",
    description: "Price ÷ earnings per share: how much you pay for each $1 a company earns in a year.",
    day: "2026-11-21",
    readableDay: "21 November 2026",
  }),
]);

export const SITE_URL = "https://quantresearch.studio";

export const NOT_FOUND_PAGE_PATH = "404.html";
export const NOT_FOUND_TITLE = `Page not found${TITLE_ENDING}`;
export const NOT_FOUND_HEADING = "Page not found";
export const NOT_FOUND_TEXT = "This page doesn't exist or has moved.";
export const NOT_FOUND_HOME_LINK = Object.freeze({ text: "Back to the home page", href: "/" });

export const HTACCESS_PATH = ".htaccess";
export const HTACCESS_NOT_FOUND_RULE = "ErrorDocument 404 /404.html";
export const HTACCESS_NO_STORE_RULE = 'Header set Cache-Control "no-store"';
// www.quantresearch.studio → quantresearch.studio, so Google sees one copy of each page.
export const HTACCESS_WWW_RULES = Object.freeze([
  "RewriteEngine On",
  "RewriteCond %{HTTP_HOST} ^www\\.(.+)$ [NC]",
  "RewriteRule ^ https://%1%{REQUEST_URI} [R=301,L]",
]);

export const SITEMAP_PATH = "sitemap.xml";
export const ROBOTS_PATH = "robots.txt";
export const ROBOTS_LINES = Object.freeze([
  "User-agent: *",
  "Allow: /",
  `Sitemap: ${SITE_URL}/sitemap.xml`,
]);

export const STYLESHEET_ADDRESS = "/styles/site.css";
export const PLAY_CDN_HOST = "cdn.tailwindcss.com";
