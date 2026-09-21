# Claude Code Instructions: Matchinsights

## Development method: TDCG

Every feature or behavior change follows this loop. Do not skip steps. Ask before moving to the next one.

```
1. SPECIFY  — agree on what to build, in plain language
2. RED      — generate failing tests; wait for human approval before continuing
3. PROMPT   — generate the implementation using the approved tests as the spec
4. GREEN    — tests pass; do not weaken assertions to force it
5. REVIEW   — check naming, single responsibility, duplication, dead code
6. REFACTOR — apply what review found; tests stay green throughout
7. COMMIT   — one small focused commit with a clear why
```

### Rules

- Always ask before moving to the next step. Never chain steps.
- Generate tests first. Wait for approval. Then generate the implementation.
- If a test fails, fix the implementation or the spec. Never change the test to make it pass.
- One behavior per cycle. If the prompt grows, split the cycle.
- Style changes (layout, colors) do not need a full cycle. Confirm with the user and apply.
- Always check an existing similar component before generating anything.

## Project

Multi-repo platform for football data.

- `match-insights-mobile` — React Native app
- `match-insights-ui` — Web frontend
- `match-insights-ui-core` — Shared SVG builders and core logic
- `match-insights` — API (Spring Boot / Kotlin)
- `match-insights-sse` — Live events service
- `match-insights-infra` — Terraform

Active plan: `roadmap/plans/repo_audit_cleanup/repo_audit_cleanup.md` — human-implemented, one-task-at-a-time codebase cleanup across all repos. Supersedes the now-closed `football_project_ready` set. `roadmap/plans/fast-track/knockout_bracket_tree.md` completed 2026-07-20.
Roadmap: `roadmap/README.md`
Method reference: `TDCG.md`
