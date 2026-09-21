#!/usr/bin/env bash
# collect-pr-diff.sh
# Usage: ./collect-pr-diff.sh [base-branch]
#
# Prints to stdout:
#   1. The list of files changed in this PR (relative paths)
#   2. The full unified diff of those changes
#
# The base branch defaults to 'main'. Override via the first argument or
# by setting the BASE_BRANCH environment variable.
#
# Designed to be run from the root of the repository.

set -euo pipefail

BASE="${BASE_BRANCH:-${1:-main}}"

# Resolve the merge-base so we diff only the PR commits, not unrelated history
MERGE_BASE=$(git merge-base HEAD "origin/${BASE}" 2>/dev/null \
             || git merge-base HEAD "${BASE}" 2>/dev/null \
             || echo "")

if [ -z "$MERGE_BASE" ]; then
  echo "ERROR: cannot find merge-base against '${BASE}'. Make sure the base branch is fetched." >&2
  exit 1
fi

echo "=== PR DIFF SUMMARY ==="
echo "Base branch  : ${BASE}"
echo "Merge-base   : ${MERGE_BASE}"
echo "HEAD         : $(git rev-parse HEAD)"
echo ""

echo "=== CHANGED FILES ==="
git diff --name-only "${MERGE_BASE}" HEAD
echo ""

echo "=== COMMIT LOG ==="
git log --oneline "${MERGE_BASE}..HEAD"
echo ""

echo "=== FULL DIFF ==="
# Exclude binary files; limit each file to 500 lines to keep output manageable
git diff "${MERGE_BASE}" HEAD \
  --unified=3 \
  --diff-filter=ACMRT \
  -- '*.java' '*.properties' '*.yaml' '*.yml' \
     '*.xml' '*.json' '*.env' '*.sh' '*.py' '*.ts' '*.js' \
  | head -n 2000

echo ""
echo "=== END OF DIFF ==="
