// 3622. Check Divisibility by Digit Sum and Product
// Solved
// Easy
// Topics
// premium lock icon
// Companies
// Hint
// You are given a positive integer n. Determine whether n is divisible by the sum of the following two values:

// The digit sum of n (the sum of its digits).

// The digit product of n (the product of its digits).

// Return true if n is divisible by this sum; otherwise, return false.

 

// Example 1:

// Input: n = 99

// Output: true

// Explanation:

// Since 99 is divisible by the sum (9 + 9 = 18) plus product (9 * 9 = 81) of its digits (total 99), the output is true.

// Example 2:

// Input: n = 23

// Output: false

// Explanation:

// Since 23 is not divisible by the sum (2 + 3 = 5) plus product (2 * 3 = 6) of its digits (total 11), the output is false.

 

// Constraints:
// 1 <= n <= 10^6

class Solution {
    public boolean checkDivisibility(int n) {

        // Keep a copy of n because we will modify num
        // while extracting its digits.
        int num = n;

        // Stores the sum of all digits.
        int sum = 0;

        // Stores the product of all digits.
        // Start with 1 because 1 is the multiplicative identity.
        int product = 1;

        // Process every digit of n from right to left.
        while (num > 0) {

            // Extract the last digit.
            // Example: 123 % 10 = 3
            int digit = num % 10;

            // Add the digit to the digit sum.
            sum += digit;

            // Multiply the digit into the digit product.
            product *= digit;

            // Remove the last digit.
            // Example: 123 / 10 = 12
            num /= 10;
        }

        // The required divisor is:
        // digit sum + digit product.
        //
        // n is divisible by this value if the remainder is 0.
        return n % (sum + product) == 0;
    }
}