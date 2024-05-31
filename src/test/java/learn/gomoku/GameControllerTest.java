package learn.gomoku;

import learn.gomoku.game.Gomoku;
import learn.gomoku.game.Result;
import learn.gomoku.players.HumanPlayer;
import learn.gomoku.players.Player;
import learn.gomoku.players.RandomPlayer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Scanner;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/*
* Requirements
Can set up two players.
For a human player, collect their name. (A random player's name is randomly generated.)
For each stone placement, use the player's name to ask questions.
Since the random player doesn't require input, the UI should display stone placement and the results of placement.
(Random player placement may fail since they don't know what they're doing.)
Re-prompt on failed placement. The game must not proceed until a player has made a valid move.
Display the final result of the game.
Give the option to play again.
* */

class GameControllerTest {
    private InputStream originalSystemIn;
    private Scanner scanner;
    GameController gameController = new GameController();
    private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    private final PrintStream originalSystemOut = System.out;
    char[][] board;

    @BeforeEach
    public void setUp() {
        // Save the original System.in to restore it later
        originalSystemIn = System.in;
        System.setOut(new PrintStream(outputStream));

        // set up a new game
        Player player1 = new HumanPlayer();
        player1.setName("George");
        Player player2 = new HumanPlayer();
        player2.setName("Jim");
        gameController.game = new Gomoku(player1, player2);
        gameController.board = new char[Gomoku.WIDTH][Gomoku.WIDTH];
    }

    @AfterEach
    public void tearDown() {
        // Restore the original System.in
        System.setIn(originalSystemIn);
        System.setOut(originalSystemOut);
    }

    @Test
    public void shouldSelectHumanPlayer(){
        String input = "1\n";  // Input to simulate user typing "42" and pressing Enter
        InputStream inputStream = new ByteArrayInputStream(input.getBytes());
        System.setIn(inputStream);  // Set the simulated input stream

        scanner = new Scanner(System.in);  // Create a scanner to read from the simulated input

        Player result = gameController.getPlayer(1, scanner);

        assertEquals(HumanPlayer.class, result.getClass());
    }

    @Test
    public void shouldSelectRandomPlayer(){
        String input = "2\n";  // Input to simulate user typing "42" and pressing Enter
        InputStream inputStream = new ByteArrayInputStream(input.getBytes());
        System.setIn(inputStream);  // Set the simulated input stream

        scanner = new Scanner(System.in);  // Create a scanner to read from the simulated input

        Player result = gameController.getPlayer(1, scanner);

        assertEquals(RandomPlayer.class, result.getClass());
    }


    //Play
    //should get off board message
    @Test
    public void shouldDisplayErrorIfOffBoard(){
        Player currentPlayer = gameController.game.getCurrent();
        String input = "16\n16\n";   // Input to simulate user typing "16 and 16" and pressing Enter
        InputStream inputStream = new ByteArrayInputStream(input.getBytes());
        System.setIn(inputStream);  // Set the simulated input stream

        scanner = new Scanner(System.in);  // Create a scanner to read from the simulated input

        // call the method
        Result result = gameController.play(scanner);

        String capturedOutput = outputStream.toString().trim();
        String expectedOutput = "Stone is off the board."; // Replace with your expected output
        assertTrue(capturedOutput.contains(expectedOutput));
        assertFalse(result.isSuccess());
        assertTrue(gameController.game.getCurrent().equals(currentPlayer));

    }
    // should get duplicate move message
    @Test
    public void shouldGetDuplicateMoveError(){

        String input = "1\n1\n";   // Input to simulate user typing "16 and 16" and pressing Enter
        InputStream inputStream = new ByteArrayInputStream(input.getBytes());
        System.setIn(inputStream);  // Set the simulated input stream

        scanner = new Scanner(System.in);  // Create a scanner to read from the simulated input

        // call the method
        gameController.play(scanner);


        Player currentPlayer = gameController.game.getCurrent();
        input = "1\n1\n";   // Input to simulate user typing "16 and 16" and pressing Enter
        inputStream = new ByteArrayInputStream(input.getBytes());
        System.setIn(inputStream);  // Set the simulated input stream

        scanner = new Scanner(System.in);  // Create a scanner to read from the simulated input

        // call the method
        Result result = gameController.play(scanner);

        String capturedOutput = outputStream.toString().trim();
        String expectedOutput = "Duplicate move."; // Replace with your expected output
        assertTrue(capturedOutput.contains(expectedOutput));
        assertFalse(result.isSuccess());
        assertTrue(gameController.game.getCurrent().equals(currentPlayer));

    }
    // should place valid stone
    @Test
    public void shouldPlaceValidStone(){
        Player currentPlayer = gameController.game.getCurrent();
        String input = "3\n5\n";   // Input to simulate user typing "16 and 16" and pressing Enter
        InputStream inputStream = new ByteArrayInputStream(input.getBytes());
        System.setIn(inputStream);  // Set the simulated input stream

        scanner = new Scanner(System.in);  // Create a scanner to read from the simulated input
        // call the method
        Result result = gameController.play(scanner);
        assertTrue(result.isSuccess());
        assertFalse(gameController.game.getCurrent().equals(currentPlayer));
        String capturedOutput = outputStream.toString().trim();
        String expectedOutput = currentPlayer.getName(); // Replace with your expected output
        assertTrue(capturedOutput.contains(expectedOutput));
    }
    //should display move from random player
    @Test
    public void shouldDisplayNameFromRandomPlayer(){
        // set up a new game
        Player player1 = new RandomPlayer();
        Player player2 = new RandomPlayer();
        gameController.game = new Gomoku(player1, player2);
        gameController.board = new char[Gomoku.WIDTH][Gomoku.WIDTH];
        String name = gameController.game.getCurrent().getName();

        gameController.play(scanner);
        String capturedOutput = outputStream.toString().trim();
        String expectedOutput = name; // Replace with your expected output
        assertTrue(capturedOutput.contains(expectedOutput));
    }


    @Test
    public void shouldPrintCleanBoard(){
        gameController.printBoard();
        String capturedOutput = outputStream.toString().trim();
        String expectedOutput ="\n   01 02 03 04 05 06 07 08 09 10 11 12 13 14 15 \n" +
                "01  _  _  _  _  _  _  _  _  _  _  _  _  _  _  _\n" +
                "02  _  _  _  _  _  _  _  _  _  _  _  _  _  _  _\n" +
                "03  _  _  _  _  _  _  _  _  _  _  _  _  _  _  _\n" +
                "04  _  _  _  _  _  _  _  _  _  _  _  _  _  _  _\n" +
                "05  _  _  _  _  _  _  _  _  _  _  _  _  _  _  _\n" +
                "06  _  _  _  _  _  _  _  _  _  _  _  _  _  _  _\n" +
                "07  _  _  _  _  _  _  _  _  _  _  _  _  _  _  _\n" +
                "08  _  _  _  _  _  _  _  _  _  _  _  _  _  _  _\n" +
                "09  _  _  _  _  _  _  _  _  _  _  _  _  _  _  _\n" +
                "10  _  _  _  _  _  _  _  _  _  _  _  _  _  _  _\n" +
                "11  _  _  _  _  _  _  _  _  _  _  _  _  _  _  _\n" +
                "12  _  _  _  _  _  _  _  _  _  _  _  _  _  _  _\n" +
                "13  _  _  _  _  _  _  _  _  _  _  _  _  _  _  _\n" +
                "14  _  _  _  _  _  _  _  _  _  _  _  _  _  _  _\n" +
                "15  _  _  _  _  _  _  _  _  _  _  _  _  _  _  _"; // Replace with your expected output

        // Check if each line of expectedOutput is contained in capturedOutput
        for (String line : expectedOutput.split("\n")) {
            assertTrue(capturedOutput.contains(line.trim()));
        }

    }

    //should print board with valid move
    @Test
    void testPlayMethodWithValidUserInput() {
        // Arrange
        String input = "2" + System.lineSeparator() + "2" + System.lineSeparator();
        InputStream inputStream = new ByteArrayInputStream(input.getBytes());
        Scanner scanner = new Scanner(inputStream);

        // Assuming your game instance is created and initialized somewhere in the test class
//        YourGameClass gameInstance = new YourGameClass();

        // Act
        Result result = gameController.play(scanner);

        // Assert
        assertTrue(result.isSuccess());
        // Add more assertions based on the expected behavior of the play method

        // You might want to check the output as well if it's relevant
        // For example, you can redirect System.out and check what's printed to the console
        // (similar to the approach used in the previous examples)
    }
    // should not place invalid moves
    @Test
    void testPlayMethodWithInvalidUserInputOffBoard() {
        // Arrange
        String input = "16" + System.lineSeparator() + "16" + System.lineSeparator();
        InputStream inputStream = new ByteArrayInputStream(input.getBytes());
        Scanner scanner = new Scanner(inputStream);

        // Assuming your game instance is created and initialized somewhere in the test class
//        YourGameClass gameInstance = new YourGameClass();
        // Act
        Result result = gameController.play(scanner);
        // Assert
        assertFalse(result.isSuccess());
        String capturedOutput = outputStream.toString().trim();
        assertTrue(capturedOutput.contains("Stone is off the board."));
    }
    @Test
    void testPlayMethodWithInvalidUserInputDuplicate() {
        // Arrange
        String input = "2" + System.lineSeparator() + "2" + System.lineSeparator();
        InputStream inputStream = new ByteArrayInputStream(input.getBytes());
        Scanner scanner = new Scanner(inputStream);

        // Assuming your game instance is created and initialized somewhere in the test class
//        YourGameClass gameInstance = new YourGameClass();
        // Act
        Result result = gameController.play(scanner);
        // Assert
        assertTrue(result.isSuccess());

        // make duplicate move
        input = "2" + System.lineSeparator() + "2" + System.lineSeparator();
        inputStream = new ByteArrayInputStream(input.getBytes());
        scanner = new Scanner(inputStream);

        result = gameController.play(scanner);

        assertFalse(result.isSuccess());
        String capturedOutput = outputStream.toString().trim();
        assertTrue(capturedOutput.contains("Duplicate move."));
    }


}