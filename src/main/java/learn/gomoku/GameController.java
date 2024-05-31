package learn.gomoku;

import learn.gomoku.game.Gomoku;
import learn.gomoku.game.Result;
import learn.gomoku.game.Stone;
import learn.gomoku.players.HumanPlayer;
import learn.gomoku.players.Player;
import learn.gomoku.players.RandomPlayer;

import java.util.ArrayList;
import java.util.Scanner;

import static learn.gomoku.game.Gomoku.WIDTH;

public class GameController {

    // Fields
    private Scanner console;

    public Gomoku game;
    public char[][] board;
    public Result result;


    // Methods
    /**
     * Method Name: run
     * Method Output: none
     * Method Input: none
     * Explanation: This method orchestrates the execution of the game. It follows these steps:
     *  1. Calls the setup method to initialize the game.
     *  2. Prints the initial state of the game board using the printBoard method.
     *  3. Enters a loop where the game is played until it is over:
     *      a. Calls the play method, allowing the player to make moves or the computer to generate moves.
     *      b. Checks if the game is over using the isOver method.
     *  4. Prints a newline and the game result message.
     *  5. Asks the player if they want to play again using the playAgain method and stores the answer.
     *  6. If the player wants to play again, recursively calls the run method to start a new game.
     *  7. If the player decides not to play again, prints a goodbye message.
     */

    public void run() {
        // Initializing the game
        setup();
        // Printing the board
        printBoard();

        // Making the moves until it is over
        while (!game.isOver()) {
            result = play(console);
            if (game.isOver()) {
                break;
            }
        }

        // Printing the message who wins
        System.out.println("\n" + result);
        System.out.println(result.getMessage());

        // Asking the user if they want to play again
        if (playAgain()) {
           run();
        } else {
            System.out.println("Goodbye!");
        }
    }


    /**
     * Method Name: setup
     * Method Output: none
     * Method Input: none
     * Explanation: This method initializes the Gomoku game by performing the following steps:
     *  1. Creates a new char array to represent the game board with dimensions WIDTH x WIDTH.
     *  2. Prints a welcome message to the console.
     *  3. Asks for the name of Player 1 and sets it if Player 1 is a human player.
     *  4. Asks for the name of Player 2 and sets it if Player 2 is a human player.
     *  5. Creates a new Gomoku game instance with the initialized players.
     *  6. Prints a message indicating the randomization of the starting player.
     *  7. Prints a message announcing which player goes first.
     */

    public void setup() {

        // Initializing the board
        board = new char[WIDTH][WIDTH];

        System.out.println("Welcome to Gomoku");
        System.out.println("=================");

        console = new Scanner(System.in);

        // Creating the players
        Player player1 = getPlayer(1, console);
        Player player2 = getPlayer(2, console);

        // Creating the game
        game = new Gomoku(player1, player2);

        System.out.println("\n(Randomizing)");

        // Randomly printing the player name who goes first
        System.out.println("\n" + game.getCurrent().getName() + " goes first.");

    }

    /**
     * Method Name: getPlayer
     * Method Output: Player
     * Method Input: int playerNumber, Scanner console
     * Explanation: This method prompts the user to choose the type of player for a given player number and returns
     * the corresponding player instance. It follows these steps:
     *  1. Initializes a Player variable to null.
     *  2. Enters a do-while loop to repeatedly prompt the user until a valid choice is made.
     *  3. Displays the player number and options to the user:
     *      - 1 for Human Player
     *      - 2 for Random Player
     *  4. Reads the user's choice and creates the corresponding player instance.
     *  5. Marks the choice as valid, and exits the loop.
     *  6. Returns the selected player instance.
     */

    public Player getPlayer(int playerNumber, Scanner console) {
        Player player = null;

        // The loop continues until the user gives a valid input
        do {
            System.out.println("Player " + playerNumber + " is:");
            System.out.println("1. Human");
            System.out.println("2. Random Player");
            System.out.print("Select [1-2]: ");

            int choice = Integer.parseInt(console.nextLine());

            String playerName;
            switch (choice) {
                case 1:
                    // Asking for name
                    System.out.print("Player 1, enter your name: ");

                    // If the string is empty then create the human player with name
                    if (console.hasNextLine()) {
                        playerName = console.nextLine();
                        player = new HumanPlayer(playerName);
                    }
                    // Otherwise create a human player without the name
                    player = new HumanPlayer();
                    break;
                case 2:
                    // Creating random player
                    player = new RandomPlayer();
                    break;
                default:
                    System.out.println("Invalid choice.");
            }
        } while (player == null);

        //System.out.println("human player2");
        return player;
    }


    /**
 * Method Name: printBoard
 * Method Output: none
 * Method Input: none
 * Explanation: This method prints the current state of the game board to the console. It follows these steps:
 *  1. Retrieves the list of stones from the game using game.getStones().
 *  2. Iterates through each stone in the list, updating the board array based on stone positions and colors.
 *  3. Prints column numbers at the top of the board for easy reference.
 *  4. Prints each row of the board, indicating 'B' for black stones, 'W' for white stones, and '_' for empty spots.
 *  5. Ensures proper formatting and spacing for a clear and readable board display.
 *  6. This method does not return any value.
 */


    public void printBoard() {
        // Initializing the list of moves
        ArrayList<Stone> stones = (ArrayList<Stone>) game.getStones();

        // Iterating through the board and filling with available moves
        for (char[] row : board) {
            java.util.Arrays.fill(row, '_');
        }

        // Placing stones on the board
        for (Stone stone : stones) {
            // Selecting the stone symbol
            char st = stone.isBlack() ? 'B' : 'W';
            board[stone.getRow()][stone.getColumn()] = st;
        }

        // Print column numbers
        System.out.print("   ");
        for (int col = 0; col < Gomoku.WIDTH; col ++) {
            // Print columns with leading zero if necessary
            System.out.printf("%02d ", col + 1);
        }
        System.out.println();

        // Print rows with row numbers and board contents
        for (int row = 0; row < Gomoku.WIDTH; row ++) {
            // Print row number with leading zero if necessary
            System.out.printf("%02d ", row + 1);
            for (int col = 0; col < Gomoku.WIDTH; col++) {
                // Print board contents
                System.out.print(" " + board[row][col] + " ");
            }
            System.out.println();
        }
    }



    /**
     * Method Name: play
     * Method Output: Result
     * Method Input: Scanner console
     * Explanation: This method represents a player's turn in the Gomoku game. It performs the following steps:
     *  1. Prints a newline and a message indicating whose turn it is.
     *  2. Declares variables for row, column, and a Stone instance.
     *  3. If the current player is a HumanPlayer, prompts the user to enter a row and column and creates a Stone.
     *  4. If the current player is not a HumanPlayer, generates a move using generateMove and creates a Stone.
     *  5. Places the Stone on the game board and updates the game result.
     *  6. If the placement is unsuccessful, prints an error message.
     *  7. Prints the current state of the game board.
     *  8. Returns the result of the player's move.
     */

    public Result play(Scanner console){
        System.out.println("");
        System.out.println(game.getCurrent().getName() + "'s turn.");

        int row;
        int column;
        Stone stone;

        // Checking if the current player is a human player
        if (game.getCurrent() instanceof HumanPlayer) {
            System.out.print("Enter a row: ");
            row = Integer.parseInt(console.nextLine());

            System.out.print("Enter a column: ");
            column = Integer.parseInt(console.nextLine());

            // Creating the stone
            stone = new Stone(row, column, game.isBlacksTurn());
        } else {

            // Gernerating the move if the current player is a random player
            stone = game.getCurrent().generateMove(game.getStones());
        }

        // Placing the stone
        result = game.place(stone);

        // Printing the message
        if (!result.isSuccess()) {
            System.out.println(result.getMessage());
        }

        printBoard();

        return result;
    }

    /**
     * Method Name: readRequiredString
     * Method Output: String
     * Method Input: String message
     * Explanation: This method prompts the user with a message until a non-empty, non-whitespace string is entered.
     * It performs the following steps:
     *  1. Declares variables for the result string and a version without whitespaces.
     *  2. Enters a do-while loop to repeatedly prompt the user until a non-empty string is entered.
     *  3. Prints the provided message to the console and reads the user's input.
     *  4. Iterates through the characters of the input, excluding whitespaces, to build a version without whitespaces.
     *  5. Checks if the resulting string without whitespaces is empty.
     *  6. If the string is empty, repeats the loop. Otherwise, exits the loop and returns the original result.
     */

    public String readRequiredString(String message) {

        String result;
        String resultWithoutWhitespace;

        // The loop continues until the user gives a non empty message
        do {
            System.out.print(message);
            result = console.nextLine();

            // Removing all the whitespace
            resultWithoutWhitespace = result.replaceAll(" ", "");

        } while (resultWithoutWhitespace.isEmpty());

        return result;
    }

    /**
     * Method Name: playAgain
     * Method Output: boolean
     * Method Input: None
     * Explanation: This method prompts the user to play again and returns true if the answer is 'y' (case-insensitive).
     * It performs the following steps:
     *  1. Prints a newline.
     *  2. Uses the readRequiredString method to prompt the user with the message "Play Again? [y/n]: ".
     *  3. Prints an additional newline.
     *  4. Returns true if the answer (ignoring case) is 'y', indicating the user wants to play again; otherwise,
     *     returns false.
     */

    public boolean playAgain() {
        System.out.println("");
        String input = readRequiredString("Play Again? [y/n]: ");
        System.out.println("");

        if (input.equalsIgnoreCase("y")) {
            return true;
        }
        return false;
    }

}
