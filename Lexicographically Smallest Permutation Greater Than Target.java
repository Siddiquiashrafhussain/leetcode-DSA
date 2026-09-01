//3720. Lexicographically Smallest Permutation Greater Than Target

class LexicographicallySmallestPermutationGreaterThanTarget {

    public String lexGreaterPermutation(String s, String target) {

        int n = s.length();

        // freq[i] = number of times character ('a' + i)
        // appears in string s.
        //
        // Example:
        // s = "aabc"
        // freq['a'] = 2, freq['b'] = 1, freq['c'] = 1
        int[] freq = new int[26];

        // Build the frequency table for s.
        for (int i = 0; i < n; i++) {
            freq[s.charAt(i) - 'a']++;
        }

        /*
         * Step 1:
         * Try to match target exactly from left to right.
         *
         * matched = number of characters we successfully matched
         * with target.
         *
         * We consume each matched character from freq because
         * that character has already been used.
         */
        int matched = 0;

        while (matched < n) {

            // Convert target character into an index [0..25].
            int c = target.charAt(matched) - 'a';

            // We cannot continue matching target because this
            // character is not available in s.
            if (freq[c] == 0) {
                break;
            }

            // Use this character.
            freq[c]--;

            // Move to the next position.
            matched++;
        }

        /*
         * Step 2:
         *
         * We now need to make the permutation STRICTLY GREATER
         * than target.
         *
         * The best place to make the change is as far right as possible.
         *
         * Why?
         * Because the longer our prefix matches target,
         * the smaller the resulting permutation will be.
         *
         * Example:
         *
         * target = "bba"
         *
         * Changing at index 0:
         * "cab"
         *
         * Changing at index 1:
         * "bca"
         *
         * "bca" is smaller than "cab".
         *
         * Therefore, we backtrack from right to left.
         */
        int pos = (matched == n) ? n - 1 : matched;

        while (pos >= 0) {

            /*
             * If this position was part of the successfully matched
             * prefix, restore its character before trying something
             * larger at this position.
             *
             * Example:
             * target = "bba"
             *
             * If we backtrack to index 1, the 'b' used there must
             * be returned to freq.
             */
            if (pos < matched) {
                freq[target.charAt(pos) - 'a']++;
            }

            // Current target character.
            int current = target.charAt(pos) - 'a';

            /*
             * Step 3:
             * Find the SMALLEST character available that is greater
             * than target[pos].
             *
             * We start from current + 1.
             *
             * Choosing the smallest possible greater character
             * keeps the answer lexicographically smallest.
             */
            int greater = current + 1;

            while (greater < 26 && freq[greater] == 0) {
                greater++;
            }

            /*
             * If we found a greater character, this position can
             * become the first position where our answer differs
             * from target.
             */
            if (greater < 26) {

                // Start with target so its prefix is preserved.
                char[] ans = target.toCharArray();

                // Put the smallest greater character at this position.
                ans[pos] = (char) ('a' + greater);

                // Consume the character we just used.
                freq[greater]--;

                /*
                 * Step 4:
                 *
                 * Once the answer is already greater than target,
                 * we want the remaining suffix to be as small as
                 * possible.
                 *
                 * Therefore, put all remaining characters in
                 * alphabetical order.
                 */
                int index = pos + 1;

                for (int c = 0; c < 26; c++) {

                    // Put every remaining occurrence of character c.
                    while (freq[c] > 0) {
                        ans[index++] = (char) ('a' + c);
                        freq[c]--;
                    }
                }

                // We found the lexicographically smallest valid answer.
                return new String(ans);
            }

            /*
             * No character greater than target[pos] is available.
             *
             * So we must move further left and try changing an
             * earlier position.
             */
            pos--;
        }

        /*
         * We could not make any position greater than target.
         *
         * Therefore, no permutation of s is strictly greater
         * than target.
         */
        return "";
    }
}