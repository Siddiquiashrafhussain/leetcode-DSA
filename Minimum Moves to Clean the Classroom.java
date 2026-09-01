// 3568. Minimum Moves to Clean the Classroom

class Solution {

    public int minMoves(String[] classroom, int energy) {

        final int rows = classroom.length;
        final int cols = classroom[0].length();
        final int cells = rows * cols;

        int start = -1;
        int litterCount = 0;

        // litterBit[cell] = bit representing litter at this cell.
        int[] litterBit = new int[cells];

        // ---------------------------------------------------------
        // Find start position and assign one bit to every litter.
        // ---------------------------------------------------------
        for (int r = 0; r < rows; r++) {
            String row = classroom[r];

            for (int c = 0; c < cols; c++) {

                char ch = row.charAt(c);
                int cell = r * cols + c;

                if (ch == 'S') {
                    start = cell;
                } else if (ch == 'L') {
                    litterBit[cell] = 1 << litterCount++;
                }
            }
        }

        // No litter means no moves are needed.
        if (litterCount == 0) {
            return 0;
        }

        final int masks = 1 << litterCount;
        final int fullMask = masks - 1;

        /*
         * bestEnergy[index]
         *
         * For the same:
         *      (mask, cell)
         *
         * store the maximum energy seen so far.
         *
         * Store energy + 1 because:
         *      0 = never visited
         *      1 = visited with energy 0
         */
        final byte[] bestEnergy = new byte[masks * cells];

        /*
         * Encode a state into one int:
         *
         * state =
         * ((mask * cells + cell) * (energy + 1)) + power
         */
        final int energyStates = energy + 1;

        /*
         * Maximum number of possible encoded states.
         *
         * Using an int[] avoids State object creation and boxing.
         */
        final int[] queue =
                new int[masks * cells * energyStates];

        int head = 0;
        int tail = 0;

        // Initial state: start, no litter, full energy.
        final int startIndex = start;

        bestEnergy[startIndex] = (byte) (energy + 1);

        queue[tail++] = start * energyStates + energy;

        int moves = 0;

        // ---------------------------------------------------------
        // BFS
        // ---------------------------------------------------------
        while (head < tail) {

            final int levelEnd = tail;

            while (head < levelEnd) {

                int state = queue[head++];

                // Decode energy.
                int power = state % energyStates;

                // Remove energy part.
                int base = state / energyStates;

                // Decode cell and litter mask.
                int cell = base % cells;
                int mask = base / cells;

                // All litter collected.
                if (mask == fullMask) {
                    return moves;
                }

                // Cannot move with zero energy.
                if (power == 0) {
                    continue;
                }

                int r = cell / cols;
                int c = cell - r * cols;

                // -------------------------------------------------
                // UP
                // -------------------------------------------------
                if (r > 0) {

                    int nr = r - 1;
                    int nc = c;

                    char ch = classroom[nr].charAt(nc);

                    if (ch != 'X') {

                        int nextCell = cell - cols;
                        int nextPower = power - 1;
                        int nextMask = mask | litterBit[nextCell];

                        if (ch == 'R') {
                            nextPower = energy;
                        }

                        if (tryAdd(
                                nextCell,
                                nextMask,
                                nextPower,
                                cells,
                                energyStates,
                                bestEnergy,
                                queue,
                                tail
                        )) {
                            tail++;
                        }
                    }
                }

                // -------------------------------------------------
                // DOWN
                // -------------------------------------------------
                if (r + 1 < rows) {

                    int nr = r + 1;
                    int nc = c;

                    char ch = classroom[nr].charAt(nc);

                    if (ch != 'X') {

                        int nextCell = cell + cols;
                        int nextPower = power - 1;
                        int nextMask = mask | litterBit[nextCell];

                        if (ch == 'R') {
                            nextPower = energy;
                        }

                        if (tryAdd(
                                nextCell,
                                nextMask,
                                nextPower,
                                cells,
                                energyStates,
                                bestEnergy,
                                queue,
                                tail
                        )) {
                            tail++;
                        }
                    }
                }

                // -------------------------------------------------
                // LEFT
                // -------------------------------------------------
                if (c > 0) {

                    int nr = r;
                    int nc = c - 1;

                    char ch = classroom[nr].charAt(nc);

                    if (ch != 'X') {

                        int nextCell = cell - 1;
                        int nextPower = power - 1;
                        int nextMask = mask | litterBit[nextCell];

                        if (ch == 'R') {
                            nextPower = energy;
                        }

                        if (tryAdd(
                                nextCell,
                                nextMask,
                                nextPower,
                                cells,
                                energyStates,
                                bestEnergy,
                                queue,
                                tail
                        )) {
                            tail++;
                        }
                    }
                }

                // -------------------------------------------------
                // RIGHT
                // -------------------------------------------------
                if (c + 1 < cols) {

                    int nr = r;
                    int nc = c + 1;

                    char ch = classroom[nr].charAt(nc);

                    if (ch != 'X') {

                        int nextCell = cell + 1;
                        int nextPower = power - 1;
                        int nextMask = mask | litterBit[nextCell];

                        if (ch == 'R') {
                            nextPower = energy;
                        }

                        if (tryAdd(
                                nextCell,
                                nextMask,
                                nextPower,
                                cells,
                                energyStates,
                                bestEnergy,
                                queue,
                                tail
                        )) {
                            tail++;
                        }
                    }
                }
            }

            // One BFS layer = one move.
            moves++;
        }

        return -1;
    }

    /*
     * Add a state only when it improves the maximum energy
     * for the same (cell, mask).
     */
    private boolean tryAdd(
            int cell,
            int mask,
            int power,
            int cells,
            int energyStates,
            byte[] bestEnergy,
            int[] queue,
            int tail) {

        int index = mask * cells + cell;

        // Already reached with equal or greater energy.
        if (power <= bestEnergy[index] - 1) {
            return false;
        }

        bestEnergy[index] = (byte) (power + 1);

        queue[tail] = index * energyStates + power;

        return true;
    }
}