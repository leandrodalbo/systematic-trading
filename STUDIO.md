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
- **Footer on every page:** "Education and research only. Not financial advice." (UK: don't
  tell people what to buy.)

Needs:
- The scripts must be public on GitHub (today `systematic-trading` is the repo; to check: is it
  public?).
- A folder for the site, named from the Psalms (user picks).

## Domain

Checked 2026-11-20 (whois/RDAP), looked free:

- **backtestlog.com** (recommended: plain, says what it is, .com is easy to trust)
- backtestlog.dev
- edgenotebook.dev
- edgejournal.dev
- quantnotebook.dev

Taken: edgelab.dev, marketlogbook.com, quantnotebook.com, edgeledger.com, nulledge.com.
Check again just before buying; free can change.

## The daily routine (user, 2026-11-20)

Every day is the same loop: **read → check the market → find one interesting thing → script it
if needed → publish it if it's worth it.** About 2–3 hours. One journal file per day:
`journal/YYYY-MM-DD.md`, copied from [journal/TEMPLATE.md](journal/TEMPLATE.md).

1. **Read (at least 1 hour, first).** The current book (now: Bandy; Storelli for fundamentals).
   Notes go in `knowledge/`. Write any idea worth testing in [journal/ideas.md](journal/ideas.md).
2. **Check the market (20–30 min).** The same checklist every day, so days can be compared:
   - SPY, QQQ, BTC: today's move, and where price sits vs its 20-day mean.
   - The watch list: anything unusual (big move, very oversold, very overbought).
   - The calendar: earnings, Fed, big data releases today or tomorrow.
   - Write 3–5 lines in the journal: what happened, numbers only, no guesses.
3. **Find one interesting thing (15 min).** One question from today's check or reading. Worth it if
   it's **surprising, testable, and useful to someone else**. Examples: "SPY fell 3 days in a row,
   what usually happens next?", "Is BTC more oversold than SPY this week?". If nothing today,
   write "nothing" and pick from `journal/ideas.md` instead.
4. **Script it, if needed (0–60 min).** Only when the question needs data to answer, or the check
   will repeat.
   - One-off question → a script in `implementations/data-analysis/scripts/`.
   - Something we'll reuse (an indicator, the daily check) → into the package, with a TDCG cycle.
   - The script's output (number, chart) goes in the journal.
5. **Publish, if it's worth it (15 min).** Not every day.
   - Small finding → a short "note" on the website, with the chart and a link to the script on
     GitHub.
   - Big finding (a full rule test) → a "finding" page (the weekly "Does it work?").
   - Post on X when something's published (max one post a day).

## The weekly rhythm

On top of the daily routine. Fits the two posts a week in `../fruit-of-hands/POSTS-GUIDE.md`.

- **Monday:** pick the week's question (from `journal/ideas.md`).
- **Tuesday post:** "This week I'm testing…" (the question, why it matters).
- **Wednesday:** run the test.
- **Thursday post:** the verdict + chart + link to the finding.
- **Friday:** write the finding page, publish the site.

## First four weeks

- **Week 1:** pick domain and folder name. Finish Bandy notes for Phase 03. First test by hand
  in a notebook: RSI(2) on SPY. First Tuesday/Thursday posts.
- **Week 2:** daily check script v1 (watch list oversold count), from the CSVs trade-plumber
  already saves. TDCG cycles.
- **Week 3:** site live with: home, first finding, 3 concept cards.
- **Week 4:** second finding (z-score rule), first "trap" post (look-ahead bias).

Then keep going with the curriculum: Phase 04 build, Phase 05 validation (great content: "my
backtest lied, here's how I caught it"), Phase 07 public paper trading.

## Open questions (user decides)

- Domain: backtestlog.com or another?
- Folder name for the site (Psalms).
- Is `leandrodalbo/systematic-trading` public, or a separate public repo for findings scripts?
- X account: the current one, or a new one for the studio?
