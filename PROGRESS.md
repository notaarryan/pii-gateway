# PII Redaction Gateway — Progress

## Status
- **Current session:** 2 (Java essentials: records, interfaces, generics, enums)
- **Completed sessions:** 1
- **Open branches / unfinished work:** none
- **Latest release tag:** none yet (v0.1.0 planned at end of Phase 1, after Session 5)
- **Repo:** https://github.com/notaarryan/pii-gateway

## Completed sessions
### Session 1 — Tooling and repo setup (2026-11-16)
- Installed Temurin JDK 21; set JAVA_HOME + PATH in ~/.zshrc so `java` and `mvn` both use 21
- Created Maven project in IntelliJ (groupId `com.aryan`, artifactId `pii-gateway`)
- Set `maven.compiler.release` to 21 in pom.xml (replaced source/target)
- Wrote .gitignore (target/, .env, .idea/, *.iml, .DS_Store, logs) and README
- `git init -b main`, initial commit, created public GitHub repo, pushed
- Branch protection on `main`: require PR, no required approvals (solo), no bypass
- First full cycle: issue #1 → branch `docs/progress-tracking` → commit → PR #2 → squash merge → cleanup

## Current directory tree
    pii-gateway/
    ├── .gitignore
    ├── PROGRESS.md
    ├── README.md
    ├── pom.xml
    └── src/
    ├── main/
    │ ├── java/ (empty — packages start in Session 2)
    │ └── resources/ (empty)
    └── test/
    └── java/ (empty)


## Design patterns learned
- None yet (Strategy is first, in Session 6)

## Git skills learned
- `git init -b main`, `git status`, `git add`, `git commit -m`, `git log --oneline`
- `git remote add origin`, `git push -u` (upstream tracking)
- `git switch`, `git switch -c` (create branch), `git pull` before branching
- `git commit --amend` (only on unpushed commits)
- `git branch -D` after a squash merge (new hash, so `-d` refuses), `git fetch --prune`
- Conventional Commits: `type(scope): imperative summary`; types feat, fix, test, docs, refactor, chore, ci
- Branch naming: feature/, fix/, chore/, docs/, refactor/, test/
- GitHub Issues + `Closes #N`; PR template (What / Why / How / How to test)
- Merge strategies: merge commit vs squash vs rebase (squash is the project default)
- Branch protection rules vs rulesets

## Concepts learned
- Maven: pom.xml, standard directory layout, plugins downloaded from Central into ~/.m2
- `modelVersion` (POM format) vs Java version
- `maven.compiler.release` vs `source`/`target`: release also restricts APIs to that Java version
- `java` resolves via PATH; Homebrew `mvn` resolves via JAVA_HOME
- IntelliJ: syncing Maven after editing pom.xml; project SDK and language level; preview vs final language features
- Why `target/` is never committed (generated output: commit inputs, not outputs)
- Test code in src/test/java is never packaged; test-scoped deps stay out of production
- Git tracks files, not folders (empty dirs don't appear; `.gitkeep` convention)
- A green build that compiled nothing proves nothing

## Shaky (revisit in warm-ups)
- Explaining the **"because"** in answers, not just the fact
- `release` vs `source`/`target`: why release is safer
- PATH vs JAVA_HOME: which tool reads which
- What happens on a direct push to a protected branch (GitHub rejects it)
- Picking the commit type (docs vs chore): match it to what actually changed

## Current metrics
- None yet (the evaluator arrives in Session 9)

## Parked ideas
- Add the Maven Wrapper (`mvnw`) so the project builds without a local Maven install
- Restrict the repo's merge options to squash-only (Settings → General → Pull Requests)