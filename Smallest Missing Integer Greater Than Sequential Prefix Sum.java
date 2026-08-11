// 2996. Smallest Missing Integer Greater Than Sequential Prefix Sum
// Easy
// Topics
// premium lock icon
// Companies
// Hint
// You are given a 0-indexed array of integers nums.

// A prefix nums[0..i] is sequential if, for all 1 <= j <= i, nums[j] = nums[j - 1] + 1. In particular, the prefix consisting only of nums[0] is sequential.

// Return the smallest integer x missing from nums such that x is greater than or equal to the sum of the longest sequential prefix.

 

// Example 1:

// Input: nums = [1,2,3,2,5]
// Output: 6
// Explanation: The longest sequential prefix of nums is [1,2,3] with a sum of 6. 6 is not in the array, therefore 6 is the smallest missing integer greater than or equal to the sum of the longest sequential prefix.
// Example 2:

// Input: nums = [3,4,5,1,12,14,13]
// Output: 15
// Explanation: The longest sequential prefix of nums is [3,4,5] with a sum of 12. 12, 13, and 14 belong to the array while 15 does not. Therefore 15 is the smallest missing integer greater than or equal to the sum of the longest sequential prefix.
 

// Constraints:

// 1 <= nums.length <= 50
// 1 <= nums[i] <= 50
class Solution {
    /**
     * Return the smallest integer x missing from nums such that
     * x is greater than or equal to the sum of the longest sequential prefix.
     *
     * A sequential prefix is one where each next element increments by 1.
     */
    public int missingInteger(int[] nums) {
        // Initialize sum with the first element: the prefix contains at least nums[0]
        int sum = nums[0];

        // Extend the prefix while numbers are consecutive (nums[i] = nums[i-1] + 1)
        for (int i = 1; i < nums.length; i++) {
            if (nums[i] == nums[i - 1] + 1) {
                // include this number in the sequential prefix sum
                sum += nums[i];
            } else {
                // sequence breaks here; stop extending the prefix
                break;
            }
        }

        // Use a presence array to mark which values appear in nums.
        // Constraints guarantee nums[i] <= 50, but allocate up to 100 to
        // safely search for missing values >= sum.
        boolean[] seen = new boolean[101];
        for (int x : nums) {
            if (x >= 0 && x < seen.length) {
                seen[x] = true;
            }
        }

        // Starting from sum, find the smallest integer not present in nums.
        while (sum <= 100 && seen[sum]) {
            sum++;
        }
        return sum;
    }
}