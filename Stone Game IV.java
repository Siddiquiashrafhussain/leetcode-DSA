// 1510. Stone Game IV
// Solved
// Hard
// Topics
// premium lock icon
// Companies
// Hint
// Alice and Bob take turns playing a game, with Alice starting first.

// Initially, there are n stones in a pile. On each player's turn, that player makes a move consisting of removing any non-zero square number of stones in the pile.

// Also, if a player cannot make a move, he/she loses the game.

// Given a positive integer n, return true if and only if Alice wins the game otherwise return false, assuming both players play optimally.

 

// Example 1:

// Input: n = 1
// Output: true
// Explanation: Alice can remove 1 stone winning the game because Bob doesn't have any moves.
// Example 2:

// Input: n = 2
// Output: false
// Explanation: Alice can only remove 1 stone, after that Bob removes the last one winning the game (2 -> 1 -> 0).
// Example 3:

// Input: n = 4
// Output: true
// Explanation: n is already a perfect square, Alice can win with one move, removing 4 stones (4 -> 0).
 

// Constraints:

// 1 <= n <= 105


class Solution {
    public boolean winnerSquareGame(int n) {
        // dp[i] means whether the current player can force a win when there are i stones left.
        boolean[] dp = new boolean[n + 1];

        for (int i = 1; i <= n; i++) {
            // Try every possible square move size j*j.
            // If after taking j*j stones, the opponent cannot win from the remaining stones,
            // then the current player can win this state.
            for (int j = 1; j * j <= i; j++) {
                if (!dp[i - j * j]) {
                    dp[i] = true;
                    break;
                }
            }
        }

        // The answer for the original state is whether Alice can win with n stones.
        return dp[n];
    }
}