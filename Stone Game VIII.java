////================= LeetCode 1872 — Stone Game VIII ==================////
class Solution {
    public int stoneGameVIII(int[] stones) {

        int n = stones.length;

        // prefix[i] = sum of stones[0 ... i]
        long[] prefix = new long[n];

        prefix[0] = stones[0];

        for (int i = 1; i < n; i++) {
            prefix[i] = prefix[i - 1] + stones[i];
        }

        /*
         * If Alice takes all n stones, her score difference is
         * simply the total sum.
         *
         * This is our initial best answer.
         */
        long best = prefix[n - 1];

        /*
         * Try every possible prefix that can be removed.
         *
         * If we remove stones[0 ... i], Alice gets prefix[i].
         * Then it becomes Bob's turn, so Bob's optimal result is
         * subtracted from Alice's gain.
         *
         * Recurrence:
         *
         * best = max(best, prefix[i] - best)
         *
         * We process from right to left so that 'best' represents
         * the result for the next state.
         */
        for (int i = n - 2; i >= 1; i--) {
            best = Math.max(best, prefix[i] - best);
        }

        return (int) best;
    }
}