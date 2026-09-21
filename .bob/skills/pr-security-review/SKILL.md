---
name: pr-security-review
description: Use when the user wants to run a deep security analysis of the commits in a pull request - reviews the full PR diff for vulnerabilities, hardcoded secrets, injection flaws, and other security issues introduced by the developer.
metadata:
  disable-model-invocation: true
  argument-hint: "[base-branch]"
---

# PR Security Review

Perform a thorough security analysis of all commits in the current pull request.
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

## Step 3 - Analyse for security vulnerabilities

Examine the diff and the file contents. For each vulnerability found, record internally:
- `severity`: one of `CRITICAL`, `HIGH`, `MEDIUM`, `LOW`
- `category`: one of `Secrets & Credentials`, `Injection`, `Authentication & Authorisation`, `Cryptography`, `Network & Binding`, `Input Validation`, `Logging & Error Handling`, `Dependency & Container`
- `file`: relative path of the affected file
- `line`: line number as an integer (0 if unknown)
- `title`: one-line description suitable for a GitHub issue title, max 80 characters
- `issue`: what is wrong, in 1-3 sentences
- `fix`: concrete remediation. Include a short code example using \n for line breaks where applicable.

### Checklist

**Secrets & Credentials**
- Hardcoded passwords, API keys, tokens, or connection strings
- Secrets committed to `.env`, `application.properties`, or config files
- Credentials passed as plain environment variables in CI/CD files

**Injection**
- SQL queries built by string concatenation instead of parameterised queries
- Use of `eval()`, `exec()`, `Runtime.getRuntime().exec()`, or equivalent
- Template injection, JNDI injection, or expression language injection

**Authentication & Authorisation**
- Passwords stored or compared in plaintext
- Missing or bypassable authentication checks on endpoints
- JWT or session tokens without proper validation
- Overly permissive CORS (`origins=*`, `methods=*`, `headers=*`)

**Cryptography**
- Use of weak algorithms: MD5, SHA-1, DES, RC4, ECB mode
- Custom encryption logic
- Insecure random number generation (`java.util.Random` for security purposes)
- Missing TLS or certificate validation disabled

**Network & Binding**
- Services binding to `0.0.0.0` or all interfaces
- HTTP used where HTTPS is required
- Timeout values missing or set to 0/infinite

**Input Validation**
- Missing server-side validation of user-supplied data
- File upload paths or names accepted without sanitisation
- Regex patterns vulnerable to ReDoS

**Logging & Error Handling**
- Sensitive data (passwords, tokens, PII) written to logs
- Stack traces or system details exposed to clients
- Overly verbose error messages

**Dependency & Container**
- New dependencies added that are not well-maintained or carry known CVEs
- Container images not sourced from `registry.redhat.io`
- Containers running as root

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
