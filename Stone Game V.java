/*1563. Stone Game V
Solved
Hard
Topics
premium lock icon
Companies
Hint
There are several stones arranged in a row, and each stone has an associated value which is an integer given in the array stoneValue.

In each round of the game, Alice divides the row into two non-empty rows (i.e. left row and right row), then Bob calculates the value of each row which is the sum of the values of all the stones in this row. Bob throws away the row which has the maximum value, and Alice's score increases by the value of the remaining row. If the value of the two rows are equal, Bob lets Alice decide which row will be thrown away. The next round starts with the remaining row.

The game ends when there is only one stone remaining. Alice's score is initially zero.

Return the maximum score that Alice can obtain.

 

Example 1:

Input: stoneValue = [6,2,3,4,5,5]
Output: 18
Explanation: In the first round, Alice divides the row to [6,2,3], [4,5,5]. The left row has the value 11 and the right row has value 14. Bob throws away the right row and Alice's score is now 11.
In the second round Alice divides the row to [6], [2,3]. This time Bob throws away the left row and Alice's score becomes 16 (11 + 5).
The last round Alice has only one choice to divide the row which is [2], [3]. Bob throws away the right row and Alice's score is now 18 (16 + 2). The game ends because only one stone is remaining in the row.
Example 2:

Input: stoneValue = [7,7,7,7,7,7,7]
Output: 28
Example 3:

Input: stoneValue = [4]
Output: 0
 

Constraints:

1 <= stoneValue.length <= 500
1 <= stoneValue[i] <= 10^6 */



class Solution {
    /**
     * Stone Game V - Dynamic Programming Solution
     * 
     * Problem: Alice divides stones into two non-empty rows. Bob throws away the row with maximum sum.
     * Alice gets points equal to the remaining row's sum. Goal: maximize Alice's total score.
     * 
     * Approach: Dynamic Programming with optimization
     * - dp[j]: Maximum score Alice can get from stones [0, j)
     * - Process intervals backwards to allow reuse of precomputed values
     * - Use suffix maximum array to optimize case 2 computation
     * 
     * Time Complexity: O(n²) - Two nested loops, no repeated work
     * Space Complexity: O(n²) - For prefix sums and suffix maximum array
     */
    public int stoneGameV(int[] stoneValue) {
        int n = stoneValue.length;

        // ========== PREFIX SUM ARRAY ==========
        // prefix[i] = sum of stones from index 0 to i-1
        // Used to efficiently calculate range sums: sum[i, j) = prefix[j] - prefix[i]
        int[] prefix = new int[n + 1];
        for (int i = 0; i < n; i++) {
            prefix[i + 1] = prefix[i] + stoneValue[i];
        }

        // ========== DP ARRAY ==========
        // dp[j] = Maximum score Alice can obtain from the interval [0, j)
        // dp[0] = 0 (no stones, no score)
        // dp[n] = answer (score for all stones)
        int[] dp = new int[n + 1];

        // ========== SUFFIX MAXIMUM ARRAY ==========
        // suf[i][j] = Maximum value of (dp[k] - prefix[i]) for all k in range [i, j)
        // Used to optimize computation when right sum is optimal
        // Key insight: We need max(dp[k] + right_sum) where right_sum = prefix[j] - prefix[k]
        //            = max(dp[k] - prefix[k] + prefix[j]) = max(dp[k] - prefix[k]) + prefix[j]
        //            = max(dp[k] - prefix[i] + prefix[i] - prefix[k]) 
        //            = max(dp[k] - prefix[i]) - (prefix[k] - prefix[i]) + prefix[j]
        int[][] suf = new int[n + 1][n + 1];

        // Process intervals in reverse order (from small to large)
        // This ensures when computing dp[j], we have already computed all smaller dp values
        for (int i = n - 1; i >= 0; i--) {

            // ========== BASE CASE: Empty interval ==========
            // suf[i+1][i+1] represents an empty interval - mark as invalid
            suf[i + 1][i + 1] = Integer.MIN_VALUE;

            // ========== BASE CASE: Single stone interval [i, i] ==========
            // When there's only 1 stone, Alice can't divide it, so she gets 0 points
            // suf[i][i+1] will be used in suffix max calculations
            // Represents max(dp[k] - prefix[i]) for k in [i, i+1) which is just k=i
            // dp[i] = 0, so suf[i][i+1] = 0 - prefix[i] = -prefix[i]
            suf[i][i + 1] = -prefix[i];

            // ========== PROCESS ALL INTERVALS STARTING AT i ==========
            int bestLeft = 0;   // Tracks the best (dp[k] + prefix[k]) for case 1
            int k = i + 1;      // Pointer that moves right; maintains left_sum <= right_sum

            // Iterate through all possible ending positions j > i+1 (need at least 2 stones)
            for (int j = i + 2; j <= n; j++) {

                // ========== ADVANCE POINTER k FOR CASE 1 ==========
                // CASE 1: Bob throws away the LEFT side (left_sum < right_sum)
                // Alice gets: left_sum = prefix[k] - prefix[i]
                // Total score: dp[k] + left_sum (dp[k] is score from previous rounds on interval [i, k))
                // 
                // Move k forward while: left_sum <= right_sum
                // left_sum  = prefix[k] - prefix[i]
                // right_sum = prefix[j] - prefix[k]
                // Condition: prefix[k] - prefix[i] <= prefix[j] - prefix[k]
                //           2*prefix[k] <= prefix[i] + prefix[j]
                while (k < j &&
                       prefix[k] - prefix[i] <= prefix[j] - prefix[k]) {

                    // Update the best value we've seen for case 1
                    // dp[k] = score from interval [0, k), but we need score from [i, k)
                    // However, dp[k] already accounts for the split optimization
                    // So we use dp[k] + prefix[k] in the formula
                    bestLeft = Math.max(bestLeft, dp[k] + prefix[k]);
                    k++;
                }

                int q = k;  // q will be the split position for case 2 calculation

                // ========== HANDLE EQUAL SUMS CASE ==========
                // If left_sum == right_sum at position (k-1), Bob lets Alice choose which to throw
                // Alice chooses to throw the right side (to get left_sum which is larger)
                // If k > i+1, it means we've moved k at least once
                // Check if the previous position k-1 has equal sums
                if (k > i + 1 &&
                    prefix[k - 1] - prefix[i] == prefix[j] - prefix[k - 1]) {

                    // When sums are equal, use k-1 for case 2 calculation
                    // This allows us to get the maximum benefit from the suffix array
                    q = k - 1;
                }

                // ========== CALCULATE CASE 1: LEFT SIDE IS THROWN AWAY ==========
                // Alice gets the left side sum when: left_sum <= right_sum (at position k)
                // Score = dp[k] + left_sum where left_sum = prefix[k] - prefix[i]
                // But bestLeft already contains dp[k] + prefix[k], so we subtract prefix[i]
                // Score = bestLeft - prefix[i]
                int leftBest = bestLeft - prefix[i];

                // ========== CALCULATE CASE 2: RIGHT SIDE IS THROWN AWAY ==========
                // Alice gets the right side sum when: right_sum <= left_sum (at position q)
                // Score = dp[q] + right_sum where right_sum = prefix[j] - prefix[q]
                // Using suffix maximum: max(dp[k] - prefix[i]) for k in [q, j)
                // rightBest = suf[q][j] + prefix[j]
                // This gives us: max(dp[k] - prefix[i]) + prefix[j] = max(dp[k] + prefix[j] - prefix[i])
                // But we need dp[k] + (prefix[j] - prefix[q]), not with prefix[i]
                // Actually, suf[q][j] gives max(dp[k] - prefix[i]) for k >= q
                // We need max(dp[k]) for k >= q with right_sum
                // rightBest represents the maximum score when we choose to take the right side
                int rightBest = suf[q][j] + prefix[j];

                // ========== CHOOSE THE MAXIMUM ==========
                // dp[j] = maximum score Alice can get from interval [0, j)
                // She chooses the split that maximizes her score
                dp[j] = Math.max(leftBest, rightBest);

                // ========== UPDATE SUFFIX MAXIMUM ==========
                // Maintain suf[i][j] = max(dp[k] - prefix[i]) for k in [i, j)
                // suf[i][j] = max of:
                //   - suf[i+1][j]: previously computed suffix max for [i+1, j)
                //   - dp[j] - prefix[i]: the current position j
                // This allows O(1) lookup of the best split for case 2 in future iterations
                suf[i][j] = Math.max(suf[i + 1][j], dp[j] - prefix[i]);
            }
        }

        // ========== RETURN ANSWER ==========
        // dp[n] represents the maximum score from interval [0, n), which includes all stones
        return dp[n];
    }
}

/*
Complexity:

Time: O(n²) - Two nested loops with amortized O(1) pointer operations
Space: O(n²) - Suffix maximum array */