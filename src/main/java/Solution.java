package main.java;

import java.util.*;

/**
 * Solution to <a href="https://www.hackerrank.com/challenges/matrix-rotation-algo">challenge</a>.
 */
public class Solution {

    public static void main(String[] args) {
        /* Enter your code here. Read input from STDIN. Print output to STDOUT. Your class should be named Solution. */
        Scanner scanner = new Scanner(System.in);
        String line = scanner.nextLine();
        String[] tokens = line.split("\\s+");
        int numRows = Integer.parseInt(tokens[0]);
        int numColumns = Integer.parseInt(tokens[1]);
        int rotationAmount = Integer.parseInt(tokens[2]);
        ArrayList<Integer>[] shells;
        shells = new ArrayList[Math.min(numColumns, numRows)/2];
        //Populate the shells
        for (int j = 0; j < shells.length; j++) {
            shells[j] = new ArrayList<>();
        }
        /* shells are populated as follows:
         _______>
        ^ ___>  |
        | ^   | |
        | |   # |
        | <---* #
        <-------*

        starting at the * and finishing at the #

        The actual pattern starts from the top left corner and prepends numbers
        on the left and bottom faces and appends all others as they are seen.
         */
        for (int rowNumber = 0; rowNumber < numRows; rowNumber++) {
            line = scanner.nextLine();
            tokens = line.split("\\s+");
            for (int columnNumber = 0; columnNumber < numColumns; columnNumber++) {
                int edgeDist = distFromNearestEdge(rowNumber, columnNumber, numRows, numColumns);
                if (columnNumber == edgeDist || (numRows - rowNumber - edgeDist - 1) == 0) {
                    shells[edgeDist].add(0, Integer.parseInt(tokens[columnNumber]));
                } else {
                    shells[edgeDist].add(Integer.parseInt(tokens[columnNumber]));
                }
            }
        }
        /*for (ArrayList shell : shells) {
            System.out.println(shell.toString());
        }*/

        /* Now the lists will be rotated */
        for (ArrayList shell : shells) {
            /* effective rotation denotes how far it would be rotated without counting full rotations*/
            int effectiveRotation = rotationAmount % shell.size();
            List movedPortion = shell.subList(0, effectiveRotation);
            shell.addAll(movedPortion);
            for (int i = 0; i < effectiveRotation; i++) {
                shell.remove(0);
            }
        }
        /*for (ArrayList shell : shells) {
            System.out.println(shell.toString());
        }*/

        /* Now the lists are reassembled in their original format */
        int[][] outputArray = new int[numRows][];
        for (int i = 0; i < numRows; i++) {
            outputArray[i] = new int[numColumns];
        }
        for (int shellNum = 0; shellNum < shells.length; shellNum++) {
            for (int shellIndex = 0; shellIndex < shells[shellNum].size(); shellIndex++) {
                int[] coordinates = calculateCoordinates(shellNum, shellIndex, numColumns, numRows);
                outputArray[coordinates[0]][coordinates[1]] = shells[shellNum].get(shellIndex);
            }
        }
        /*for (int[] row : outputArray) {
            System.out.println(Arrays.toString(row));
        }*/


//        System.out.println("And we're done.");
        for (int[] row : outputArray) {
            for (int val : row) {
                System.out.print(Integer.toString(val) + " ");
            }
            System.out.print("\n");
        }
    }

    /**
     * Gives the zero based distance from the nearest edge of the matrix.
     * @param row the row of this entry base zero
     * @param column the column of this entry base zero
     * @param numRows the number of rows in the matrix base one
     * @param numColumns the number of columns in the matrix base one
     * @return the integer value of the distance to the nearest edge
     */
    private static int distFromNearestEdge(int row, int column, int numRows, int numColumns) {
        return Math.min(Math.min(row, (numRows - row - 1)), Math.min(column, (numColumns - column - 1)));
    }

    /**
     * Returns a row major array containing two numbers, the first being the row number and the second the column number
     * of the position where a value would have originated based on its shell position.
     * @param shellNum the shells distance from the edge base zero
     * @param shellIndex the index of the value within the shell base zero
     * @param numColumns the number of columns in the matrix base one
     * @param numRows the number of rows in the matrix base one
     * @return an array of two ints where the first value represents the row and
     * the second value represents the column
     */
    private static int[] calculateCoordinates(int shellNum, int shellIndex, int numColumns, int numRows) {
        //the first element position will be the bottom right corner of the shell
        int[] coordinates = new int[2];
        int bottomDistance = numColumns - 2 * shellNum - 1;
        int leftDistance = numRows - 2 * shellNum - 1;
        int topDistance = bottomDistance;
        int rightDistance = leftDistance - 1;

        if (shellIndex <= bottomDistance) {
            //cases from the bottom side
            coordinates[0] = numRows - 1 - shellNum;
            coordinates[1] = numColumns - 1 - shellNum - shellIndex;
        } else if (shellIndex <= (bottomDistance + leftDistance)) {
            //cases from the left side
            int leftIndex =  shellIndex - bottomDistance;
            coordinates[0] = numRows - 1 - shellNum - leftIndex;
            coordinates[1] = shellNum;
        } else if (shellIndex <= (bottomDistance + leftDistance + topDistance)) {
            //cases from the top
            int topIndex = shellIndex - leftDistance - bottomDistance;
            coordinates[0] = shellNum;
            coordinates[1] = shellNum + topIndex;
        } else {
            //cases from the right
            int rightIndex = shellIndex - leftDistance - bottomDistance - topDistance;
            coordinates[0] = rightIndex + shellNum;
            coordinates[1] = numColumns - shellNum - 1;
        }
        return coordinates;
    }
}

/*
4 4 1
1 2 3 4
1 2 3 4
1 2 3 4
1 2 3 4


4 4 5
1 1 1 1
2 2 2 2
3 3 3 3
4 4 4 4
 */