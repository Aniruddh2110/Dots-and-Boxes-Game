import java.util.Scanner;

public class CLI_DotsAndBoxes {
    private static final int SIZE = 5;
    private boolean[][] horizontalLines = new boolean[SIZE][SIZE - 1];
    private boolean[][] verticalLines = new boolean[SIZE - 1][SIZE];
    private int[][] boxes = new int[SIZE - 1][SIZE - 1];
    private int currentPlayer = 1;
    private int[] scores = new int[2];
    private boolean gameOver = false;
    private Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        CLI_DotsAndBoxes game = new CLI_DotsAndBoxes();
        game.startGame();
    }

    private void startGame() {
        while (!gameOver) {
            printBoard();
            System.out.println("Player " + currentPlayer + "'s turn.");
            System.out.print("Enter your move (row col direction [h/v]): ");
            String input = scanner.nextLine();

            if (processMove(input)) {
                // Only switch player if no box was claimed
                if (!checkAndClaimBox(lastRow, lastCol, lastDirection == 'h')) {
                    currentPlayer = 3 - currentPlayer; // Switch player if no box claimed
                }
                checkGameOver();
            }
        }
        printBoard();
        System.out.println("Game Over!");
        System.out.println("Player 1 Score: " + scores[0]);
        System.out.println("Player 2 Score: " + scores[1]);
    }

    private boolean lastMoveProcessed = false;
    private int lastRow, lastCol;
    private char lastDirection;

    private boolean processMove(String input) {
        String[] parts = input.split(" ");
        if (parts.length != 3) {
            System.out.println("Invalid input format. Try again.");
            return false;
        }

        try {
            int row = Integer.parseInt(parts[0]);
            int col = Integer.parseInt(parts[1]);
            char direction = parts[2].charAt(0);

            if (row < 0 || row >= SIZE || col < 0 || col >= SIZE || (direction != 'h' && direction != 'v')) {
                System.out.println("Invalid move. Try again.");
                return false;
            }

            if (direction == 'h' && col < SIZE - 1 && !horizontalLines[row][col]) {
                horizontalLines[row][col] = true;
                lastRow = row;
                lastCol = col;
                lastDirection = direction;
                lastMoveProcessed = true;
            } else if (direction == 'v' && row < SIZE - 1 && !verticalLines[row][col]) {
                verticalLines[row][col] = true;
                lastRow = row;
                lastCol = col;
                lastDirection = direction;
                lastMoveProcessed = true;
            } else {
                System.out.println("Invalid move. Try again.");
                lastMoveProcessed = false;
            }

            return lastMoveProcessed;

        } catch (NumberFormatException e) {
            System.out.println("Invalid input format. Try again.");
            return false;
        }
    }

    private boolean checkAndClaimBox(int row, int col, boolean isHorizontal) {
        boolean claimed = false;

        if (isHorizontal) {
            if (row > 0 && horizontalLines[row][col] && horizontalLines[row - 1][col]
                    && verticalLines[row - 1][col] && verticalLines[row - 1][col + 1]) {
                boxes[row - 1][col] = currentPlayer;
                scores[currentPlayer - 1]++;
                claimed = true;
            }
            if (row < SIZE - 1 && horizontalLines[row][col] && horizontalLines[row + 1][col]
                    && verticalLines[row][col] && verticalLines[row][col + 1]) {
                boxes[row][col] = currentPlayer;
                scores[currentPlayer - 1]++;
                claimed = true;
            }
        } else {
            if (col > 0 && verticalLines[row][col] && verticalLines[row][col - 1]
                    && horizontalLines[row][col - 1] && horizontalLines[row + 1][col - 1]) {
                boxes[row][col - 1] = currentPlayer;
                scores[currentPlayer - 1]++;
                claimed = true;
            }
            if (col < SIZE - 1 && verticalLines[row][col] && verticalLines[row][col + 1]
                    && horizontalLines[row][col] && horizontalLines[row + 1][col]) {
                boxes[row][col] = currentPlayer;
                scores[currentPlayer - 1]++;
                claimed = true;
            }
        }

        return claimed;
    }

    private void checkGameOver() {
        boolean allBoxesClaimed = true;
        for (int i = 0; i < SIZE - 1; i++) {
            for (int j = 0; j < SIZE - 1; j++) {
                if (boxes[i][j] == 0) {
                    allBoxesClaimed = false;
                    break;
                }
            }
        }
        gameOver = allBoxesClaimed;
    }

    private void printBoard() {
        System.out.println();

        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                System.out.print("+");
                if (j < SIZE - 1 && horizontalLines[i][j]) {
                    System.out.print("--");
                } else {
                    System.out.print("  ");
                }
            }
            System.out.println("+");

            for (int j = 0; j < SIZE; j++) {
                if (i < SIZE - 1 && verticalLines[i][j]) {
                    System.out.print("|");
                } else {
                    System.out.print(" ");
                }
                System.out.print("  ");
            }
            System.out.println("|");
        }

        System.out.print("+");
        for (int j = 0; j < SIZE - 1; j++) {
            System.out.print("--+");
        }
        System.out.println();
    }
}
