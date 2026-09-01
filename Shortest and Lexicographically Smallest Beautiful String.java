/// 2904. Shortest and Lexicographically Smallest Beautiful String


class ShortestAndLexicographicallySmallestBeautifulString {
    public String shortestBeautifulSubstring(String s, int k) {

        int n = s.length();
        int left = 0;
        int ones = 0;

        String ans = "";

        for (int right = 0; right < n; right++) {

            // Add the current character to the sliding window.
            if (s.charAt(right) == '1') {
                ones++;
            }

            /*
             * Maintain exactly k ones.
             *
             * 1. If we have more than k ones,
             *    move left until we have k.
             *
             * 2. If we have exactly k ones and the leftmost
             *    character is '0', remove leading zeros because
             *    they only make the substring longer.
             */
            while (ones > k ||
                   (ones == k && s.charAt(left) == '0')) {

                if (s.charAt(left) == '1') {
                    ones--;
                }

                left++;
            }

            // Current window contains exactly k ones.
            if (ones == k) {

                String current = s.substring(left, right + 1);

                /*
                 * Choose the better answer:
                 *
                 * 1. No answer yet.
                 * 2. Shorter substring.
                 * 3. Same length but lexicographically smaller.
                 */
                if (ans.isEmpty()
                        || current.length() < ans.length()
                        || (current.length() == ans.length()
                            && current.compareTo(ans) < 0)) {

                    ans = current;
                }
            }
        }

        return ans;
    }
}