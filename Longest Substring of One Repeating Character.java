// 2213. Longest Substring of One Repeating Character
// Solved
// Hard
// Topics
// premium lock icon
// Companies
// Hint
// You are given a 0-indexed string s. You are also given a 0-indexed string queryCharacters of length k and a 0-indexed array of integer indices queryIndices of length k, both of which are used to describe k queries.

// The ith query updates the character in s at index queryIndices[i] to the character queryCharacters[i].

// Return an array lengths of length k where lengths[i] is the length of the longest substring of s consisting of only one repeating character after the ith query is performed.

 

// Example 1:

// Input: s = "babacc", queryCharacters = "bcb", queryIndices = [1,3,3]
// Output: [3,3,4]
// Explanation: 
// - 1st query updates s = "bbbacc". The longest substring consisting of one repeating character is "bbb" with length 3.
// - 2nd query updates s = "bbbccc". 
//   The longest substring consisting of one repeating character can be "bbb" or "ccc" with length 3.
// - 3rd query updates s = "bbbbcc". The longest substring consisting of one repeating character is "bbbb" with length 4.
// Thus, we return [3,3,4].
// Example 2:

// Input: s = "abyzz", queryCharacters = "aa", queryIndices = [2,1]
// Output: [2,3]
// Explanation:
// - 1st query updates s = "abazz". The longest substring consisting of one repeating character is "zz" with length 2.
// - 2nd query updates s = "aaazz". The longest substring consisting of one repeating character is "aaa" with length 3.
// Thus, we return [2,3].
 

// Constraints:

// 1 <= s.length <= 105
// s consists of lowercase English letters.
// k == queryCharacters.length == queryIndices.length
// 1 <= k <= 105
// queryCharacters consists of lowercase English letters.
// 0 <= queryIndices[i] < s.length


class Solution {

    /**
     * Segment Tree data structure to efficiently track the longest substring of repeating characters.
     * Each node maintains:
     * - pre: length of repeating characters from the left boundary
     * - suf: length of repeating characters to the right boundary
     * - best: longest repeating substring within this segment
     * - cs: the character array representation of the string
     */
    private static class SegmentTree {
        private final int n; // Length of the string
        private final int[] pre; // Prefix: consecutive same chars from left
        private final int[] suf; // Suffix: consecutive same chars from right
        private final int[] best; // Best: longest repeating substring in this segment
        private final char[] cs; // Character array

        /**
         * Constructs a segment tree from the input string.
         * Time Complexity: O(n)
         */
        SegmentTree(String s) {
            n = s.length();

            // Allocate 4*n space for the segment tree (standard size)
            int size = n << 2;
            pre = new int[size];
            suf = new int[size];
            best = new int[size];

            cs = s.toCharArray();

            // Build the tree recursively
            build(1, 0, n - 1);
        }

        /**
         * Recursively builds the segment tree.
         * For leaf nodes: pre=1, suf=1, best=1 (single character is a repeating substring of length 1)
         * For internal nodes: combine left and right children
         * 
         * @param node Current node index (1-based)
         * @param l Left boundary of current segment
         * @param r Right boundary of current segment
         */
        private void build(int node, int l, int r) {
            if (l == r) {
                // Base case: leaf node represents a single character
                pre[node] = 1;
                suf[node] = 1;
                best[node] = 1;
                return;
            }

            int mid = (l + r) >>> 1; // Find midpoint (right shift for division by 2)
            int left = node << 1; // Left child index (left shift for multiplication by 2)
            int right = left | 1; // Right child index

            // Recursively build left and right subtrees
            build(left, l, mid);
            build(right, mid + 1, r);

            // Merge information from children
            pushUp(node, l, r);
        }

        /**
         * Merges information from left and right child nodes to update the current node.
         * This is the core logic of the segment tree.
         * 
         * Key insight: When merging two segments:
         * 1. pre[node] = length of repeating chars from left
         *    - If the entire left segment is same char AND matches right's first char,
         *      add right's prefix length
         * 2. suf[node] = length of repeating chars to the right
         *    - If the entire right segment is same char AND matches left's last char,
         *      add left's suffix length
         * 3. best[node] = best of (left_best, right_best, left_suffix + right_prefix)
         *    - The best substring could be entirely in left, entirely in right,
         *      or span across the boundary
         */
        private void pushUp(int node, int l, int r) {
            int left = node << 1;
            int right = left | 1;

            int mid = (l + r) >>> 1;

            // Calculate the lengths of left and right segments
            int leftLen = mid - l + 1;
            int rightLen = r - mid;

            // Initialize pre and suf from children
            pre[node] = pre[left];
            suf[node] = suf[right];

            // The best in current node is at least the best from children
            best[node] = Math.max(best[left], best[right]);

            // If the boundary characters match, we can merge segments
            if (cs[mid] == cs[mid + 1]) {

                // If entire left segment is the same character, 
                // we can extend it into the right segment
                if (pre[left] == leftLen) {
                    pre[node] = leftLen + pre[right];
                }

                // If entire right segment is the same character,
                // we can extend it backward into the left segment
                if (suf[right] == rightLen) {
                    suf[node] = rightLen + suf[left];
                }

                // The best substring might span across the boundary:
                // left's suffix + right's prefix (both are same character)
                best[node] = Math.max(
                    best[node],
                    suf[left] + pre[right]
                );
            }
        }

        /**
         * Updates the character at the given index in the string.
         * Note: This only updates the character array, doesn't update the tree yet.
         */
        void updateChar(char c, int index) {
            cs[index] = c;
        }

        /**
         * Updates the segment tree after a character change.
         * Triggers the cascading update from the leaf up to the root.
         * Time Complexity: O(log n)
         */
        void update(int index) {
            update(1, 0, n - 1, index);
        }

        /**
         * Recursively updates the segment tree node that contains the changed index.
         * Pushes the update information up to the root.
         * 
         * @param node Current node index (1-based)
         * @param l Left boundary of current segment
         * @param r Right boundary of current segment
         * @param index The index of the character that was updated
         */
        private void update(
                int node,
                int l,
                int r,
                int index) {

            if (l == r) {
                // Leaf node: nothing to update (already updated via updateChar)
                return;
            }

            int mid = (l + r) >>> 1;

            // Navigate to the child containing the updated index
            if (index <= mid) {
                update(node << 1, l, mid, index);
            } else {
                update(node << 1 | 1, mid + 1, r, index);
            }

            // Merge updated information from children
            pushUp(node, l, r);
        }

        /**
         * Returns the length of the longest repeating substring in the entire string.
         */
        int getBest() {
            return best[1];
        }
    }

    /**
     * Main solution method for finding the longest substring of repeating character after each query.
     * 
     * Algorithm Overview:
     * 1. Build a segment tree from the initial string
     * 2. For each query:
     *    a. Update the character at the specified index
     *    b. Update the segment tree to reflect the change (O(log n))
     *    c. Query the tree for the longest repeating substring (O(1))
     * 3. Store the result after each query
     * 
     * Time Complexity: O(n + k*log(n)) where n is string length, k is number of queries
     * Space Complexity: O(n) for the segment tree
     * 
     * @param s The initial string
     * @param queryCharacters String containing the characters to update (one per query)
     * @param queryIndices Array of indices where updates should occur
     * @return Array of longest repeating substring lengths after each query
     */
    public int[] longestRepeating(
            String s,
            String queryCharacters,
            int[] queryIndices) {

        int q = queryIndices.length; // Number of queries

        // Build the segment tree from the initial string
        SegmentTree tree = new SegmentTree(s);

        // Array to store the answer after each query
        int[] ans = new int[q];

        // Process each query
        for (int i = 0; i < q; i++) {

            int index = queryIndices[i];
            char c = queryCharacters.charAt(i);

            // Only update if the character actually changes (optimization)
            if (tree.cs[index] != c) {
                tree.updateChar(c, index); // Update the character
                tree.update(index); // Update the segment tree
            }

            // Record the best answer after this update
            ans[i] = tree.getBest();
        }

        return ans;
    }
}