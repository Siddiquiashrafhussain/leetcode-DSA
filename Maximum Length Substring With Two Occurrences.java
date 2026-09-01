// 3090. Maximum Length Substring With Two Occurrences
// Solved
// Easy
// Topics
// premium lock icon
// Companies
// Hint
// Given a string s, return the maximum length of a substring such that it contains at most two occurrences of each character.
 

// Example 1:

// Input: s = "bcbbbcba"

// Output: 4

// Explanation:

// The following substring has a length of 4 and contains at most two occurrences of each character: "bcbbbcba".
// Example 2:

// Input: s = "aaaa"

// Output: 2

// Explanation:

// The following substring has a length of 2 and contains at most two occurrences of each character: "aaaa".
 

// Constraints:

// 2 <= s.length <= 100
// s consists only of lowercase English letters.



class MaximumLengthSubstringWithTwoOccurrences {
    public int maximumLengthSubstring(String s) {
        // freq[i] stores how many times character i appears in the current window
        int[] freq = new int[26];
        int left = 0;   // left boundary of the sliding window
        int ans = 0;    // maximum valid window length found so far

        // right expands the window one character at a time
        for (int right = 0; right < s.length(); right++) {
            int r = s.charAt(right) - 'a';
            freq[r]++;

            // If this character appears more than 2 times, shrink the window
            // from the left until the count becomes valid again.
            while (freq[r] > 2) {
                freq[s.charAt(left++) - 'a']--;
            }

            // Update the best answer with the current window length.
            ans = Math.max(ans, right - left + 1);
        }

        return ans;
    }
}