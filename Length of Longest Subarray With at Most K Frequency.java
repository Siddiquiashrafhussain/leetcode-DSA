// 2958. Length of Longest Subarray With at Most K Frequency
// Solved
// Medium
// Topics
// premium lock icon
// Companies
// Hint
// You are given an integer array nums and an integer k.

// The frequency of an element x is the number of times it occurs in an array.

// An array is called good if the frequency of each element in this array is less than or equal to k.

// Return the length of the longest good subarray of nums.

// A subarray is a contiguous non-empty sequence of elements within an array.

 

// Example 1:

// Input: nums = [1,2,3,1,2,3,1,2], k = 2
// Output: 6
// Explanation: The longest possible good subarray is [1,2,3,1,2,3] since the values 1, 2, and 3 occur at most twice in this subarray. Note that the subarrays [2,3,1,2,3,1] and [3,1,2,3,1,2] are also good.
// It can be shown that there are no good subarrays with length more than 6.
// Example 2:

// Input: nums = [1,2,1,2,1,2,1,2], k = 1
// Output: 2
// Explanation: The longest possible good subarray is [1,2] since the values 1 and 2 occur at most once in this subarray. Note that the subarray [2,1] is also good.
// It can be shown that there are no good subarrays with length more than 2.
// Example 3:

// Input: nums = [5,5,5,5,5,5,5], k = 4
// Output: 4
// Explanation: The longest possible good subarray is [5,5,5,5] since the value 5 occurs 4 times in this subarray.
// It can be shown that there are no good subarrays with length more than 4.
 

// Constraints:

// 1 <= nums.length <= 105
// 1 <= nums[i] <= 109
// 1 <= k <= nums.length


class LengthOfLongestSubarrayWithAtMostKFrequency {
    /**
     * Sliding-window solution that maintains counts of elements inside the window
     * using a custom open-addressing hash table implemented with parallel arrays:
     * - keys: stored element values
     * - vals: counts (frequencies) of the corresponding keys
     * - used: whether a slot has been occupied
     *
     * The window is expanded by moving `right` forward and incrementing the
     * frequency of nums[right]. If that element's frequency exceeds `k`, we
     * advance `left` (shrinking the window) and decrement frequencies until
     * the violating element's count is <= k again. We track the maximum
     * window length seen.
     *
     * Time complexity: O(n) expected, since each element enters and leaves
     * the window at most once and hash lookups are O(1) average.
     * Space complexity: O(m) where m is a power-of-two sized table (~4n).
     */
    public int maxSubarrayLength(int[] nums, int k) {
        int n = nums.length;

        // Choose table size as power of two >= 4 * n to reduce collisions.
        int size = 1;
        while (size < n * 4) size <<= 1;

        // Parallel arrays implementing a simple open-addressing hash map:
        // keys[i] stores the key (value), vals[i] stores its frequency,
        // used[i] marks whether slot i has ever been initialized.
        int[] keys = new int[size];
        int[] vals = new int[size];
        boolean[] used = new boolean[size];
        int mask = size - 1; // for fast modulo when table size is power of two

        int left = 0; // left index of the sliding window
        int ans = 0;   // best (maximum) window length found

        for (int right = 0; right < n; right++) {
            int x = nums[right];

            // Find slot for x using linear probing
            int idx = hash(x) & mask;
            while (used[idx] && keys[idx] != x) {
                idx = (idx + 1) & mask;
            }

            // If empty slot, initialize with the key
            if (!used[idx]) {
                used[idx] = true;
                keys[idx] = x;
            }

            // Increment frequency of x (inside current window)
            vals[idx]++;

            // If frequency of x now exceeds k, move left forward until
            // the frequency of that same value is <= k again. Each time
            // we move left we decrement the count of the outgoing value.
            while (vals[idx] > k) {
                int y = nums[left++];
                int j = hash(y) & mask;

                // Locate slot for the outgoing value y (must exist)
                while (keys[j] != y) {
                    j = (j + 1) & mask;
                }

                vals[j]--;
            }

            // Update answer with current window length
            int len = right - left + 1;
            if (len > ans) ans = len;
        }

        return ans;
    }

    private int hash(int x) {
        // A small integer mixing/hash function to distribute 32-bit keys
        // across the table. It uses xor shifts and multiplications by
        // large odd constants to produce well-distributed bits for the
        // open-addressing hash table above.
        x ^= x >>> 16;
        x *= 0x7feb352d;
        x ^= x >>> 15;
        x *= 0x846ca68b;
        x ^= x >>> 16;
        return x;
    }
}