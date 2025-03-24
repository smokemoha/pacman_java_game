
// Import the JFrame class from the javax.swing package, which is used to create a window for the application
import javax.swing.JFrame;

// Define the main class for the application
public class App {
    // The main method serves as the entry point for the program
    public static void main(String[] args) throws Exception {
        // Define the number of rows in the game board grid (21 rows)
        int rowCount = 21;
        // Define the number of columns in the game board grid (19 columns)
        int columnCount = 19;
        // Set the size of each tile in the game board to 32 pixels (both width and
        // height)
        int tileSize = 32;
        // Calculate the total width of the game board in pixels by multiplying the
        // number of columns by the tile size
        int boardWidth = columnCount * tileSize; // 19 * 32 = 608 pixels
        // Calculate the total height of the game board in pixels by multiplying the
        // number of rows by the tile size
        int boardHeight = rowCount * tileSize; // 21 * 32 = 672 pixels

        // Create a new JFrame object (a window) with the title "Pac Man" displayed in
        // the title bar
        JFrame frame = new JFrame("Pac Man");
        // This line is commented out; if enabled, it would make the frame visible
        // immediately
        // frame.setVisible(true);

        // Set the size of the window to match the calculated dimensions of the game
        // board (608x672 pixels)
        frame.setSize(boardWidth, boardHeight);
        // Center the window on the user's screen by setting its location relative to
        // null
        frame.setLocationRelativeTo(null);
        // Prevent the user from resizing the window, ensuring the game board dimensions
        // remain fixed
        frame.setResizable(false);
        // Configure the window to terminate the application when the user clicks the
        // close button
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create an instance of the PacMan class, which contain the game logic and
        // rendering for Pac-Man
        PacMan pacmanGame = new PacMan();
        // Add the PacMan game component to the frame, making it the content displayed
        // within the window
        frame.add(pacmanGame);
        // Adjust the frame's size to fit the preferred size of its components (e.g.,
        // the PacMan component),
        // accounting for window borders and title bar; this may override the earlier
        // setSize call
        frame.pack();
        // Request focus for the PacMan component so it can receive keyboard input
        // (e.g., to move Pac-Man)
        pacmanGame.requestFocus();
        // Make the frame visible on the screen, displaying the Pac-Man game to the user
        frame.setVisible(true);
    }
}