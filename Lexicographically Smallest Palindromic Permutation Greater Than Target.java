//3734. Lexicographically Smallest Palindromic Permutation Greater Than Target
class Solution {

    public String lexPalindromicPermutation(String s, String target) {

        final int n = s.length();
        final int half = n / 2;

        // ------------------------------------------------------------
        // 1. Count characters in s.
        // ------------------------------------------------------------
        int[] count = new int[26];

        for (int i = 0; i < n; i++) {
            count[s.charAt(i) - 'a']++;
        }

        // ------------------------------------------------------------
        // 2. A palindrome can have at most one character
        //    with an odd frequency.
        // ------------------------------------------------------------
        int middle = -1;

        for (int c = 0; c < 26; c++) {
            if ((count[c] & 1) != 0) {
                if (middle != -1) {
                    return "";
                }
                middle = c;
            }
        }

        // ------------------------------------------------------------
        // 3. Only half of the palindrome is independent.
        //
        // Example:
        // left  = "abc"
        // middle = 'd'
        // right = "cba"
        //
        // Therefore, we only need half of each frequency.
        // ------------------------------------------------------------
        int[] freq = new int[26];

        for (int c = 0; c < 26; c++) {
            freq[c] = count[c] >> 1;
        }

        char[] left = new char[half];

        // ------------------------------------------------------------
        // 4. Match target's left half as long as possible.
        //
        // We consume characters from freq as we match them.
        // ------------------------------------------------------------
        int matched = 0;

        while (matched < half) {

            int c = target.charAt(matched) - 'a';

            if (freq[c] == 0) {
                break;
            }

            left[matched] = target.charAt(matched);
            freq[c]--;
            matched++;
        }

        // ------------------------------------------------------------
        // 5. If the complete left half matches target,
        //    build the palindrome and check the COMPLETE string.
        //
        // Equal left halves do NOT automatically mean equality
        // because target itself may not be a palindrome.
        // ------------------------------------------------------------
        if (matched == half) {

            String candidate = buildPalindrome(left, middle);

            if (candidate.compareTo(target) > 0) {
                return candidate;
            }

            /*
             * target's left half was completely matched,
             * but the resulting palindrome is not greater.
             *
             * We now backtrack from the last position.
             */
            matched = half;
        }

        // ------------------------------------------------------------
        // 6. Backtrack from right to left.
        //
        // We want the RIGHTMOST possible position to become larger
        // because that keeps the longest possible prefix equal to target.
        // ------------------------------------------------------------
        int pos = matched == half ? half - 1 : matched;

        while (pos >= 0) {

            /*
             * If this position was previously consumed while matching
             * target, restore it before trying a larger character.
             */
            if (pos < matched) {
                freq[target.charAt(pos) - 'a']++;
            }

            int current = target.charAt(pos) - 'a';

            // --------------------------------------------------------
            // Find the smallest available character greater than
            // target[pos].
            // --------------------------------------------------------
            int greater = current + 1;

            while (greater < 26 && freq[greater] == 0) {
                greater++;
            }

            if (greater < 26) {

                // Put the smallest greater character here.
                left[pos] = (char) ('a' + greater);
                freq[greater]--;

                // ----------------------------------------------------
                // Fill the remaining half with the smallest
                // possible characters.
                // ----------------------------------------------------
                int index = pos + 1;

                for (int c = 0; c < 26; c++) {
                    while (freq[c] > 0) {
                        left[index++] = (char) ('a' + c);
                        freq[c]--;
                    }
                }

                // Build and return the palindrome.
                return buildPalindrome(left, middle);
            }

            // No greater character at this position.
            // Try changing an earlier position.
            pos--;
        }

        // No palindromic permutation is strictly greater than target.
        return "";
    }

    // ------------------------------------------------------------
    // Construct the complete palindrome:
    //
    // left + middle + reverse(left)
    // ------------------------------------------------------------
    private String buildPalindrome(char[] left, int middle) {

        int half = left.length;
        char[] result = new char[half * 2 + (middle == -1 ? 0 : 1)];

        // Copy left half.
        for (int i = 0; i < half; i++) {
            result[i] = left[i];
        }

        // Put middle character for odd-length palindrome.
        int index = half;

        if (middle != -1) {
            result[index++] = (char) ('a' + middle);
        }

        // Copy reversed left half.
        for (int i = half - 1; i >= 0; i--) {
            result[index++] = left[i];
        }

        return new String(result);
    }
}