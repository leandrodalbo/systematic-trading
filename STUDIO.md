# Research Studio — the plan

Status: draft, 2026-11-20. Trading is now the only project (user, 2026-11-20). No streaming.
ClaudioXP is on hold, used as a tool when needed.

## The idea in one line

**I test popular trading ideas on real data, and show what really works, with the scripts.**

Every finding is: one question, one chart, one honest verdict, one link to the script on GitHub.
Wins and failures both get published. That honesty is the reason people come back: most trading
accounts on X sell signals; very few show the test and the script.

## Fundamentals vs systematic/quant (short)

- **Fundamental:** study one company (profits, debt, cash, price vs value) and decide if it's
  worth more than its price. Deep, few names, judgement.
- **Systematic:** fixed rules decide every trade, the same way every time. No gut call.
- **Quant:** maths and data to find a pattern and test it isn't luck.
- **This project:** quant to find and test the edge, systematic to run it. Fundamentals come in as
  *data* we test ("do cheap companies beat expensive ones?"). That mix is called
  "quantamental". More in [knowledge/quant-vs-systematic-notes.md](knowledge/quant-vs-systematic-notes.md).

## What we can show (content series)

1. **"Does it work?"** (weekly, the main series). Take a famous rule, test it, give a verdict.
   Ideas, in order (they follow the curriculum, so study and content are the same work):
   - RSI(2) below 10, buy SPY (Connors, Phase 03)
   - Price 2 standard deviations below the 20-day mean (z-score, Bandy)
   - "Buy the dip" after a 3-day fall
   - Golden cross (50/200 day), as the momentum contrast
   - Does mean reversion work on Bitcoin like on stocks?
   - Monday / turn-of-month / "sell in May" calendar effects
   - Same rule with and without trading costs (how costs kill a good backtest)
2. **Daily market check** (every day, small). One number or chart made by our own scripts, e.g.
   "how many of our watch-list stocks are oversold today", "SPY z-score today", "BTC vs SPY this
   week". This is the "check the markets every day" habit, and a reason to look every day.
3. **Trap of the month.** Show a mistake on purpose: look-ahead bias, overfitting, survivorship
   bias. Same data, wrong method, fake great result. People love these and learn from them.
4. **Fundamentals corner** (every two weeks). One company number explained (P/E, free cash flow,
   debt), then tested as a rule. Free data: SEC EDGAR company facts. Reading: Storelli,
   *Advanced Stocks Analysis* (already in [00-foundations.md](00-foundations.md), manual
   investing track).
5. **Build log.** The system itself: trade-plumber fetches data, data-analysis tests it.
   One real design detail per post.
6. **Paper-trading journal** (from Phase 07). The system's live paper trades, in public, with
   the reason for each one. A public track record is the strongest reason to follow.

## The website

A static site, the place where findings live (X posts link to it).

- **Home:** latest finding + today's market check.
- **Notes:** small daily discoveries (from the daily routine): a question, a chart, a few lines,
  a link to the script.
- **Findings:** one page per "Does it work?" test: question, chart, verdict, what it means,
  link to the script.
- **Concepts:** short plain cards (z-score, drawdown, look-ahead bias…), linked from findings.
- **Build log:** the system, step by step.
- **Footer on every page:** "Research only. Not financial advice." (user, 2026-11-21; UK: don't
  tell people what to buy.)

Decided (user, 2026-11-21):
- Folder: `green-pastures/from-the-hills/` (Psalm 121:1).
- Site name: **Quant Research Studio** (matches quantresearch.studio).
- Tool: Eleventy 3 (npm), standard configuration: pages (markdown) in the site folder itself,
  `_includes/` for layouts, `_data/` for data, output in `_site/` (git-ignored in
  `green-pastures/.gitignore`, like `node_modules/`). No README in the site folder: every `.md`
  there becomes a page; this section explains the site. `npm test` = build, then Node's built-in tests (`tests/*.test.js`) on the built
  HTML. `package.json` is private and uses ES modules.
- Structure from the Dal Bo consulting site
  (`../still-waters/consulting/static-websites/dalboconsultancy/`): sticky header with logo box,
  nav, mobile menu, dark footer. Its own look (user, 2026-11-21): **"Terminal dark"**: near-black
  background `#0b0f14`, white text `#e6edf3`, green accent `#22c55e`, red for falls `#f87171`;
  Inter for text, JetBrains Mono for numbers. Tailwind via CDN, like the Dal Bo site.
  Layout `_includes/base.njk` (each page: `layout: base.njk`); site name and nav links in
  `_data/site.json`; logo = small line-chart icon; footer = site name, disclaimer, "© 2026 Quant
  Research Studio · This site uses no cookies or tracking." Tests read HTML with cheerio.
- **The software side is as visible as the markets side** (user, 2026-11-21: "in the end I am a
  backend guy exploring this"). Nav: Home · Market · Notes · System · Concepts. **System** =
  one overview page (the parts, how data flows, Kotlin vs Python, "the code we backtest is the
  code that trades") + build entries, newest first, one folder each like notes. Home shows the
  latest note and the latest system entry.
  System page (user, 2026-11-21): title "System: Kotlin + Python for trading", description "A
  backend view of a trading system: Spring Boot for data and orders, Python for research and
  signals, one set of files between them." Overview with a simple data-flow diagram (SVG, site
  colours, text alternative). Entries: one folder each (`system/<name>/index.md`), picture
  optional. First entry: "One system, two languages", description "Kotlin for the plumbing,
  Python for the research. Each language picked as the right tool for its job."
- Charts on the site use a dark version (site background and colours); knowledge notes keep the
  white ones (user, 2026-11-21).
- One folder per note (user, 2026-11-21): `notes/<name>/index.md` with its charts next to it
  (`chart.png`…); the page is `/notes/<name>/`. Moving or deleting a note = one folder.
- Notes page `/notes/` (user, 2026-11-21): cards with the note's chart, date, title and
  description, newest first. Each note's front matter has the date **and time** it was written;
  pages show only the day, the time sets the order. Page title "Notes", description "Short
  research notes: one question, one chart, what the data said."
- Notes have no script link until the scripts are public in `implementations/data-analysis/scripts/`
  (user, 2026-11-21).
- First note (user, 2026-11-21): "Drops vs rises in SPY, 1993–2026", description "SPY above its
  200-day average, 1993–2026: drops lasted 17 days, rises 56. Drops moved about twice as fast."
  Second note: "Apple P/E, 2007–2026", description "Apple, 2007–2026: after a high-P/E year, P/E
  fell the next year 6 times out of 9. After a low year, 3 out of 10." Both built in cycle 3.
- Page titles: each page writes its own part, the layout adds " · Quant Research Studio"
  (max 60 characters in total); each page has its own description (max 160). Titles and
  descriptions are never repeated across pages. Home (user, 2026-11-21): title "Trading
  Enquiries & Market Research"; description "Daily market numbers and trading ideas tested on
  real data. Research only, not financial advice."
- Plain static HTML, no server; nothing that hides content from Google (no React app).
- Built with the Claudio plugin (TDCG): open a Claude Code session **in
  `green-pastures/from-the-hills/`** and run `/claudio:init` there. Claudio guards the folder the
  session starts in, so never init from `firm-heart/` or `green-pastures/`.
- Posts: X only.
- **Every section is a list of entries** (user, 2026-11-21, replaces "Market: today only"):
  Market, Notes, System and Concepts all use one folder per entry, the same post layout and card.
  The folder decides the section (one section per entry; link between entries when needed). Each
  section page lists its entries newest first; home shows the latest from all sections.
- **Market** (`/market/<YYYY-MM-DD>/`): one entry on a day with a check, none on other days. Every
  day is kept: each X post links to its own day's page, so old links stay right, and each day is a
  real page for Google (about 20 MB a year, fine for free hosts). An entry holds anything the day
  needs: numbers, text, one or more images, links. Each trading day: one X post, by hand.
- **Concepts** (`/concepts/<name>/`): one entry per concept, each its own page (e.g. "What is a
  z-score?" can be found on Google). First three (user, 2026-11-21): the 200-day average, mean vs
  median (from *Naked Statistics*, `knowledge/naked-stats-notes.md`; the notes use medians), and
  the P/E ratio. Only what is understood well enough to explain plainly; more as learning goes on.
  Titles: "What is the 200-day average?", "Mean vs median", "What is the P/E ratio?". Each entry:
  What it is, How to read it, Example (a real number from our data), Where it's used (link to the
  note). List page: title "Concepts", description "The words used in the notes and the market
  check, each in a few plain lines." The three list pages (Notes, System, Concepts) share one
  list layout; each page says its section with `section: notes` (user, 2026-11-21).
- Later, once we know what we look for: a script that fills the Market file, and its link on the page.
- Later, first in the next website plan (v2): search in the navbar with Pagefind (static index
  built after Eleventy, no server), once there are about 30+ pages (user asked, 2026-11-21).
- Later: the two note charts (SPY swings, Apple P/E) as scripts in
  `implementations/data-analysis/scripts/`, so anyone can redraw them; today they were drawn by
  one-off steps (2026-11-21).

Needs:
- The scripts must be public on GitHub (today `systematic-trading` is the repo; to check: is it
  public?).

## Domain

**Picked: quantresearch.studio** (user, 2026-11-20). "Quant research" says what we do, ".studio"
matches the research studio idea. Looked free on 2026-11-20 (whois/RDAP). Not bought yet; check
the yearly renewal price before buying (new endings like .studio often cost more after year one).

Other names checked the same day, looked free:

- tradingresearch.dev
- tradingresearch.co.uk (UK)
- tradingresearch.blog
- tradingresearchlog.com
- tradingresearchnotes.com
- quantresearch.dev
- backtestlog.com, edgenotebook.dev (no "research")

Taken: tradingresearch.com, tradingresearch.io, tradingresearchlab.com, edgelab.dev,
quantnotebook.com.
Check again just before buying; free can change.

## What "research" means here

Research = **read, look at the market, ask a question, check it with data, keep what you learn.**
Reading and doing run side by side. Reading never blocks the practical work: we don't wait to
finish a book (or a phase) before we build, test or post (user, 2026-11-20).

## A day (user, 2026-11-20)

Two fixed parts, then one practical thing. One journal file per day: `journal/YYYY-MM-DD.md`,
from [journal/TEMPLATE.md](journal/TEMPLATE.md).

**Every day:**

1. **Read (at least 1 hour).** The next chapter (or part of one) in the reading plan, in order
   (Bandy now; Storelli for fundamentals). Notes in `knowledge/`. Some days, read more; that's fine.
2. **Check the market (20–30 min).** Same checklist every day, so days can be compared:
   - SPY, QQQ, BTC: today's move, and where price sits vs its 20-day mean.
   - The watch list: anything unusual (big move, very oversold, very overbought).
   - The calendar: earnings, Fed, big data releases.
   - 3–5 lines in the journal, numbers only, no guesses.

**Then: one practical thing, whatever the day gives.** Pick from the menu. No need to publish or
implement every day.

- **A post on X**, if something's interesting.
- **A note on the website**, if it's worth keeping (chart + few lines + GitHub link).
- **A script** in `implementations/data-analysis/scripts/`, to answer a question with data.
- **An indicator or check in data-analysis**, using an existing Python library (pandas, a TA
  library) instead of writing the maths by hand. TDCG cycle.
- **A feature in trade-plumber** (more tickers, a scheduled fetch, an alert). TDCG cycle.
- **An idea** for later, in [journal/ideas.md](journal/ideas.md).
- **More reading**, if nothing practical comes up.

## The week

One fixed thing: the daily market check post on X (`../fruit-of-hands/POSTS-GUIDE.md`,
Template 4) on every trading day. A finding can be the day's post instead. Bigger tests ("Does it work?") happen when they're ready, not on a set day.

## Practical things to start with (any order, no dates)

- Buy quantresearch.studio; pick the site folder name.
- RSI(2) and z-score on SPY in a script, on the CSVs trade-plumber already saves.
- The daily check as a script (so step 2 takes 5 minutes, not 30).
- trade-plumber: fetch the watch list every day by itself.
- Site v1: home, first note, 3 concept cards.
- First "Does it work?": RSI(2) on SPY vs buy-and-hold.
- First "trap" post: look-ahead bias.

Later, as the curriculum goes on: Phase 04 build, Phase 05 validation (great content: "my backtest
lied, here's how I caught it"), Phase 07 public paper trading.

## Open questions (user decides)

- Site tool: Eleventy or Astro.
- Is `leandrodalbo/systematic-trading` public, or a separate public repo for findings scripts?
- X account: the current one, or a new one for the studio?
