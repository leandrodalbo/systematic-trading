import { join } from "node:path";

export const SITE_FOLDER = join(import.meta.dirname, "..", "..");
export const BUILT_HOME_PAGE = join(SITE_FOLDER, "_site", "index.html");

export const SITE_NAME = "Quant Research Studio";
export const ONE_LINE_IDEA =
  "I test popular trading ideas on real data, and show what really works, with the scripts.";
