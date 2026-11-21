import { join } from "node:path";

export const SITE_FOLDER = join(import.meta.dirname, "..", "..");
export const BUILT_SITE = join(SITE_FOLDER, "_site");
export const HOME_PAGE_PATH = "index.html";
export const BUILT_HOME_PAGE = join(BUILT_SITE, HOME_PAGE_PATH);

export const SITE_NAME = "Quant Research Studio";
export const ONE_LINE_IDEA =
  "I test popular trading ideas on real data, and show what really works, with the scripts.";

export const DISCLAIMER = "Research only. Not financial advice.";
export const MENU_BUTTON_LABEL = "Open menu";
export const NAV_LINKS = Object.freeze([
  Object.freeze({ text: "Home", href: "/" }),
  Object.freeze({ text: "Market", href: "/market/" }),
  Object.freeze({ text: "Notes", href: "/notes/" }),
  Object.freeze({ text: "Concepts", href: "/concepts/" }),
]);
