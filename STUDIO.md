# Quant Research Studio — the plan

The only active project (user, 2026-11-20). Site: https://quantresearch.studio · X:
[@QResearchStudio](https://x.com/QResearchStudio) · Scripts: GitHub
`leandrodalbo/systematic-trading` (public).

## The idea in one line

**A weekly look at the markets, known trading methods tested on real data, and a real trading
system built from them.**

Every finding: one question, one chart, one honest verdict, the script on GitHub. Wins and
failures both get published. Most trading accounts sell signals; we show the test.

## Quant, systematic, fundamental

- **Quant:** maths and data to find a pattern and check it isn't luck.
- **Systematic:** fixed rules decide every trade, the same way every time.
- **Fundamental:** study one company and decide if it's worth more than its price.
- **Here:** quant to find the edge, systematic to run it. Fundamentals come in as data we test
  ("do cheap companies beat expensive ones?"). More in
  [knowledge/quant-vs-systematic-notes.md](knowledge/quant-vs-systematic-notes.md).

## What we show (content series)

1. **Weekly market check** (Saturdays, on Friday's close; user, 2026-11-24). No day trading, so
   a daily check doesn't make sense. SPY, QQQ, IWM, BTC and the watch list. One entry on the site,
   one post on X. A really big day (a crash) can get its own entry that day.
2. **"Does it work?"** (when ready, the main series). A famous rule, tested, with a verdict.
   They follow the curriculum, so study and content are the same work:
   - RSI(2) below 10, buy SPY (Connors)
   - Price 2 standard deviations below the 20-day mean (z-score, Bandy)
   - "Buy the dip" after a 3-day fall
   - Golden cross (50/200 day), as the momentum contrast
   - Mean reversion on Bitcoin vs stocks
   - Calendar effects: Monday, turn of month, "sell in May"
   - The same rule with and without trading costs
3. **Trap of the month.** A mistake on purpose (look-ahead bias, overfitting, survivorship bias):
   same data, wrong method, fake great result.
4. **Fundamentals corner.** One company number explained (P/E, free cash flow, debt), then tested
   as a rule. Free data: SEC EDGAR. Reading: Storelli (Phase 00, manual investing track).
5. **Build log.** The system itself: trade-plumber fetches data, data-analysis tests it. One real
   design detail each time.
6. **Paper-trading journal** (from Phase 07). Live paper trades in public, with the reason for
   each one.

## The website

Static site in `from-the-hills/` (Eleventy 3, Tailwind 4, "terminal dark" look). Footer on every
page: "Research only. Not financial advice." No email, no cookies, no tracking.

- **Sections:** Market, Notes, System, Concepts. Home shows the latest entry from each.
- **Search** (`/search/`, Pagefind): every entry is indexed on each `npm test` and deploy. Built
  in the site's own files, no outside service.
- **Market:** one entry per week (`market/<YYYY-MM-DD>/`, the day of the closing prices, so
  usually a Friday), plus one for a really big day. Every entry is kept, so each X post links to
  its own. Titles short, with numbers.
- **Notes:** one question, one chart, what the data said.
- **System:** how Kotlin and Python work together, plus build entries.
- **Concepts:** one plain page per word we use (what it is, how to read it, a real example). Only
  what we understand well enough to explain.
- **Charts:** dark version on the site, white in `knowledge/`.

**Add an entry:** make a folder `<section>/<name>/` with `index.md` (front matter: `title`,
`description`, `date` with the time) and any `chart.png` next to it. Optional picture on the card:
`picture`, `pictureAlt`, `pictureWidth`, `pictureHeight`. No test changes needed.

**Check and publish:** `npm test` (build + tests), `npm start` (local preview), `npm run deploy`
(clean build, tests, then rsync over SSH to Hostinger). Connection details only in `~/.zshrc`
(`TRADEWEB_SERVER`, `TRADEWEB_SSHUSER`, `TRADEWEB_PORT`, `TRADEWEB_FOLDER`, `TRADEWEB_KEY`).

**Later:**
- The two note charts (SPY swings, Apple P/E) as scripts in
  `implementations/data-analysis/scripts/`, then script links on notes.

## What "research" means here

Read, look at the market, ask a question, check it with data, keep what you learn. Reading and
doing run side by side: we never wait to finish a book or a phase before we build, test or post
(user, 2026-11-20).

## A day (user, 2026-11-20)

One journal file per day: `journal/YYYY-MM-DD.md`, from [journal/TEMPLATE.md](journal/TEMPLATE.md).

**Every day:**

1. **Read (at least 1 hour).** The next chapter in the reading plan (Bandy now; Storelli for
   fundamentals). Notes in `knowledge/`.
2. **Then one practical thing, whatever the day gives** (list below).

**Saturdays: the weekly market check** (user, 2026-11-24), on Friday's close. The same
checklist, so weeks can be compared:

- The flow: fetch by hand (`./update_watchlist.sh`) → the report (numbers to read) → scan and
  analysis → only then AI makes a **card** (an image) for the one interesting thing found, to
  catch attention on X. No fixed card.
- The report: `uv run scripts/weekly_report.py` in `implementations/data-analysis/`, after the
  fetch. One row per watch list ticker, saved as `journal/reports/YYYY-MM-DD.csv`. It only
  calculates; finding what's unusual is the work on that file.
- SPY, QQQ, IWM, BTC: the week's move, short and long trend, crosses, z20. Then the watch list.
- The calendar for the next week: earnings, Fed, big data.
- 3–5 lines in the journal, numbers only, no guesses. Then the market entry and the X post.
- A really big day (a crash) can get its own check and entry that day.

**The practical things:**

- The weekly market entry on the site and the post on X (Saturdays).
- A note on the website, if it's worth keeping.
- A script in `implementations/data-analysis/scripts/` to answer a question.
- An indicator in data-analysis, using an existing library (TDCG cycle).
- A trade-plumber feature, e.g. an alert (TDCG cycle).
- An idea for later, in [journal/ideas.md](journal/ideas.md).
- More reading, if nothing else comes up.

X posts: the Saturday check (`../fruit-of-hands/POSTS-GUIDE.md`, Template 4); other days, whatever
the work finds. Bigger tests happen when they're ready, not on a set day.

## Next practical things (any order)

- RSI(2) in `indicators/`, then RSI(2) and z-score on SPY in a script.
- First "Does it work?": RSI(2) on SPY vs buy-and-hold.
- First trap: look-ahead bias.

Later, with the curriculum: Phase 04 build, Phase 05 validation ("my backtest lied, here's how I
caught it"), Phase 07 public paper trading.
