// 3702. Longest Subsequence With Non-Zero Bitwise XOR
// Solved
// Medium
// Topics
// premium lock icon
// Companies
// Hint
// You are given an integer array nums.

// Return the length of the longest subsequence in nums whose bitwise XOR is non-zero. If no such subsequence exists, return 0.

 

// Example 1:

// Input: nums = [1,2,3]

// Output: 2

// Explanation:

// One longest subsequence is [2, 3]. The bitwise XOR is computed as 2 XOR 3 = 1, which is non-zero.

// Example 2:

// Input: nums = [2,3,4]

// Output: 3

// Explanation:

// The longest subsequence is [2, 3, 4]. The bitwise XOR is computed as 2 XOR 3 XOR 4 = 5, which is non-zero.

 

// Constraints:

// 1 <= nums.length <= 105
// 0 <= nums[i] <= 109



class Solution {
    public int longestSubsequence(int[] nums) {
        // XOR of the entire array.
        int xor = 0;

        for (int x : nums) {
            xor ^= x;
        }

        // If total XOR is already non-zero, taking all elements is optimal.
        if (xor != 0) return nums.length;

        // If total XOR is zero but there exists a non-zero element,
        // we can drop one suitable element to make XOR non-zero.
        for (int x : nums) {
            if (x != 0) return nums.length - 1;
        }

        // All elements are zero, so every subsequence XOR is zero.
        return 0;
    }
}