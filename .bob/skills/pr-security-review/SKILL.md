---
name: pr-security-review
description: Use when the user wants to run a SQL injection security analysis of the commits in a pull request - reviews the full PR diff exclusively for SQL injection vulnerabilities introduced by the developer.
metadata:
  disable-model-invocation: true
  argument-hint: "[base-branch]"
---

# PR Security Review

Perform a targeted SQL injection security analysis of all commits in the current pull request.
Follow every step in order. Do not skip steps.

## Step 1 - Collect the PR diff

Use `execute_command` to run the bundled script from the repository root.
If the user provided a base branch argument (e.g. `/pr-security-review develop`), pass it as the first argument; otherwise default to `main`.

```bash
bash .bob/skills/pr-security-review/collect-pr-diff.sh [base-branch]
```

If the command fails (e.g. missing base branch, unshallowed repo), output the following JSON and stop:

```
{"verdict":"ERROR","error":"<reason>","findings":[]}
```

## Step 2 - Read all changed source files

From the **CHANGED FILES** section of the script output, use `read_file` to read each changed file in full.
This ensures the analysis is grounded in the actual current state of the code, not just the diff lines.

## Step 3 - Analyse for SQL injection vulnerabilities

Examine the diff and the file contents. Look **exclusively** for SQL injection issues. Ignore every other category of vulnerability. For each SQL injection finding, record internally:
- `severity`: one of `CRITICAL`, `HIGH`, `MEDIUM`, `LOW`
- `category`: `Injection`
- `file`: relative path of the affected file
- `line`: line number as an integer (0 if unknown)
- `title`: one-line description suitable for a GitHub issue title, max 80 characters
- `issue`: what is wrong, in 1-3 sentences
- `fix`: concrete remediation. Include a short code example using \n for line breaks where applicable.

### Checklist — SQL Injection only

- SQL queries built by string concatenation or interpolation instead of parameterised queries / prepared statements
- User-supplied input embedded directly into a SQL string (e.g. `"SELECT … WHERE id = " + userId`)
- ORM raw-query methods (e.g. `query()`, `raw()`, `nativeQuery`) called with unsanitised user input
- Stored procedure calls assembled via string formatting with user data
- Dynamic table or column names constructed from user input without a strict allowlist

## Step 4 - Output ONLY the JSON result

Your entire output MUST be a single line of valid JSON. No markdown, no prose, no explanation before or after it.

The JSON must conform exactly to this schema:

```
{"verdict":"PASS","findings":[]}
```

or

```
{"verdict":"FAIL","findings":[{"severity":"CRITICAL","category":"...","file":"...","line":0,"title":"...","issue":"...","fix":"..."},{"severity":"HIGH",...}]}
```

Rules you MUST follow:
- `verdict` is `PASS` if there are zero Critical or High findings, otherwise `FAIL`.
- `findings` contains ALL findings of any severity (CRITICAL, HIGH, MEDIUM, LOW).
- Every string value must be on a single line. Use the two-character sequence `\n` (backslash + n) for any line break needed inside a string — do NOT use actual newlines inside string values.
- `line` must be an integer, never a string.
- The entire output is a single line. There must be no newline characters anywhere in the JSON output.
- Do not wrap the output in a code fence or any other formatting.
- Do not print anything before or after the JSON line.
