const READABLE_DAY = new Intl.DateTimeFormat("en-GB", { dateStyle: "long", timeZone: "UTC" });
const ISO_DAY_LENGTH = "YYYY-MM-DD".length;

export default function (eleventyConfig) {
  eleventyConfig.addFilter("readableDay", (date) => READABLE_DAY.format(date));
  eleventyConfig.addFilter("isoDay", (date) => date.toISOString().slice(0, ISO_DAY_LENGTH));
  eleventyConfig.addPassthroughCopy("notes/**/*.png");
}
