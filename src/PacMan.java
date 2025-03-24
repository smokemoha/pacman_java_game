import java.awt.*;
import java.awt.event.*;
import java.util.HashSet;
import java.util.Random;
import javax.swing.*;

// Define the main class for the Pac-Man game, extending JPanel for rendering and implementing ActionListener and KeyListener for event handling
public class PacMan extends JPanel implements ActionListener, KeyListener {
    // Inner class to represent game objects such as Pac-Man, ghosts, walls, and
    // food
    class Block {
        int x; // X-coordinate of the block
        int y; // Y-coordinate of the block
        int width; // Width of the block
        int height; // Height of the block
        Image image; // Image representing the block (e.g., Pac-Man, ghost, wall)

        int startX; // Initial X-coordinate for resetting
        int startY; // Initial Y-coordinate for resetting
        char direction = 'U'; // Current direction: U (Up), D (Down), L (Left), R (Right)
        int velocityX = 0; // Velocity in the X direction
        int velocityY = 0; // Velocity in the Y direction

        // Constructor to initialize a block with an image and position
        Block(Image image, int x, int y, int width, int height) {
            this.image = image;
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.startX = x; // Store starting position for resets
            this.startY = y;
        }

        // Method to update the block's direction and handle movement and collisions
        void updateDirection(char direction) {
            char prevDirection = this.direction; // Store previous direction in case of collision
            this.direction = direction; // Set new direction
            updateVelocity(); // Adjust velocity based on direction
            this.x += this.velocityX; // Move in X direction
            this.y += this.velocityY; // Move in Y direction
            // Check for collisions with walls and revert if necessary
            for (Block wall : walls) {
                if (collision(this, wall)) {
                    this.x -= this.velocityX; // Undo X movement
                    this.y -= this.velocityY; // Undo Y movement
                    this.direction = prevDirection; // Revert to previous direction
                    updateVelocity(); // Reset velocity to previous state
                }
            }
        }

        // Update velocity based on the current direction
        void updateVelocity() {
            if (this.direction == 'U') { // Up
                this.velocityX = 0;
                this.velocityY = -tileSize / 4; // Move up at 1/4 tile size per frame
            } else if (this.direction == 'D') { // Down
                this.velocityX = 0;
                this.velocityY = tileSize / 4; // Move down
            } else if (this.direction == 'L') { // Left
                this.velocityX = -tileSize / 4; // Move left
                this.velocityY = 0;
            } else if (this.direction == 'R') { // Right
                this.velocityX = tileSize / 4; // Move right
                this.velocityY = 0;
            }
        }

        // Reset the block to its initial position
        void reset() {
            this.x = this.startX;
            this.y = this.startY;
        }
    }

    // Game board dimensions and tile size
    private int rowCount = 21; // Number of rows in the game grid
    private int columnCount = 19; // Number of columns in the game grid
    private int tileSize = 32; // Size of each tile in pixels (32x32)
    private int boardWidth = columnCount * tileSize; // Total width of the board
    private int boardHeight = rowCount * tileSize; // Total height of the board

    // Images for game elements
    private Image wallImage; // Image for walls
    private Image blueGhostImage; // Image for blue ghost
    private Image orangeGhostImage; // Image for orange ghost
    private Image pinkGhostImage; // Image for pink ghost
    private Image redGhostImage; // Image for red ghost
    private Image pacmanUpImage; // Image for Pac-Man facing up
    private Image pacmanDownImage; // Image for Pac-Man facing down
    private Image pacmanLeftImage; // Image for Pac-Man facing left
    private Image pacmanRightImage; // Image for Pac-Man facing right
    private Image cherryImage; // Image for the cherry bonus item

    // Tile map defining the game board layout
    // X = wall, O = skip (no object), P = Pac-Man, ' ' = food, b/o/p/r = ghosts
    private String[] tileMap = {
            "XXXXXXXXXXXXXXXXXXX",
            "X        X        X",
            "X XX XXX X XXX XX X",
            "X                 X",
            "X XX X XXXXX X XX X",
            "X    X       X    X",
            "XXXX XXXX XXXX XXXX",
            "OOOX X       X XOOO",
            "XXXX X XXrXX X XXXX",
            "O       bpo       O",
            "XXXX X XXXXX X XXXX",
            "OOOX X       X XOOO",
            "XXXX X XXXXX X XXXX",
            "X        X        X",
            "X XX XXX X XXX XX X",
            "X  X     P     X  X",
            "XX X X XXXXX X X XX",
            "X    X   X   X    X",
            "X XXXXXX X XXXXXX X",
            "X                 X",
            "XXXXXXXXXXXXXXXXXXX"
    };

    // Collections to store game objects
    HashSet<Block> walls; // Set of wall blocks
    HashSet<Block> foods; // Set of food blocks
    HashSet<Block> ghosts; // Set of ghost blocks
    Block pacman; // Single Pac-Man block

    // Variables for cherry functionality
    Block cherry = null; // Cherry block (null if not spawned)
    long cherrySpawnTime = 0; // Timestamp of when the cherry was spawned

    Timer gameLoop; // Timer for the game loop
    char[] directions = { 'U', 'D', 'L', 'R' }; // Possible movement directions
    Random random = new Random(); // Random number generator for ghost movement and cherry spawn
    int score = 0; // Player's score
    int lives = 3; // Player's remaining lives
    boolean gameOver = false; // Flag indicating if the game is over

    // Constructor to initialize the Pac-Man game
    PacMan() {
        setPreferredSize(new Dimension(boardWidth, boardHeight)); // Set the size of the game panel
        setBackground(Color.BLACK); // Set background color to black
        addKeyListener(this); // Add this class as a key listener for input
        setFocusable(true); // Allow the panel to receive keyboard focus

        // Load images for game elements from resources
        wallImage = new ImageIcon(getClass().getResource("./wall.png")).getImage();
        blueGhostImage = new ImageIcon(getClass().getResource("./blueGhost.png")).getImage();
        orangeGhostImage = new ImageIcon(getClass().getResource("./orangeGhost.png")).getImage();
        pinkGhostImage = new ImageIcon(getClass().getResource("./pinkGhost.png")).getImage();
        redGhostImage = new ImageIcon(getClass().getResource("./redGhost.png")).getImage();
        pacmanUpImage = new ImageIcon(getClass().getResource("./pacmanUp.png")).getImage();
        pacmanDownImage = new ImageIcon(getClass().getResource("./pacmanDown.png")).getImage();
        pacmanLeftImage = new ImageIcon(getClass().getResource("./pacmanLeft.png")).getImage();
        pacmanRightImage = new ImageIcon(getClass().getResource("./pacmanRight.png")).getImage();
        cherryImage = new ImageIcon(getClass().getResource("./cherry.png")).getImage();

        loadMap(); // Initialize the game board from the tile map
        // Assign random initial directions to ghosts
        for (Block ghost : ghosts) {
            char newDirection = directions[random.nextInt(4)];
            ghost.updateDirection(newDirection);
        }
        // Start the game loop with a 50ms delay (approximately 20 frames per second)
        gameLoop = new Timer(50, this);
        gameLoop.start();
    }

    // Method to load the game map from the tileMap array
    public void loadMap() {
        walls = new HashSet<Block>(); // Initialize walls collection
        foods = new HashSet<Block>(); // Initialize food collection
        ghosts = new HashSet<Block>(); // Initialize ghosts collection

        // Iterate through each row and column of the tile map
        for (int r = 0; r < rowCount; r++) {
            for (int c = 0; c < columnCount; c++) {
                String row = tileMap[r];
                char tileMapChar = row.charAt(c);

                int x = c * tileSize; // Calculate X position
                int y = r * tileSize; // Calculate Y position

                // Create blocks based on the tile map character
                if (tileMapChar == 'X') { // Wall
                    Block wall = new Block(wallImage, x, y, tileSize, tileSize);
                    walls.add(wall);
                } else if (tileMapChar == 'b') { // Blue ghost
                    Block ghost = new Block(blueGhostImage, x, y, tileSize, tileSize);
                    ghosts.add(ghost);
                } else if (tileMapChar == 'o') { // Orange ghost
                    Block ghost = new Block(orangeGhostImage, x, y, tileSize, tileSize);
                    ghosts.add(ghost);
                } else if (tileMapChar == 'p') { // Pink ghost
                    Block ghost = new Block(pinkGhostImage, x, y, tileSize, tileSize);
                    ghosts.add(ghost);
                } else if (tileMapChar == 'r') { // Red ghost
                    Block ghost = new Block(redGhostImage, x, y, tileSize, tileSize);
                    ghosts.add(ghost);
                } else if (tileMapChar == 'P') { // Pac-Man
                    pacman = new Block(pacmanRightImage, x, y, tileSize, tileSize);
                } else if (tileMapChar == ' ') { // Food (small dot)
                    Block food = new Block(null, x + 14, y + 14, 4, 4); // Centered 4x4 pixel dot
                    foods.add(food);
                }
            }
        }
    }

    // Override paintComponent to handle custom rendering
    public void paintComponent(Graphics g) {
        super.paintComponent(g); // Clear the panel
        draw(g); // Draw game elements
    }

    // Method to draw all game elements on the screen
    public void draw(Graphics g) {
        // Draw Pac-Man with its current image
        g.drawImage(pacman.image, pacman.x, pacman.y, pacman.width, pacman.height, null);

        // Draw all ghosts
        for (Block ghost : ghosts) {
            g.drawImage(ghost.image, ghost.x, ghost.y, ghost.width, ghost.height, null);
        }

        // Draw all walls
        for (Block wall : walls) {
            g.drawImage(wall.image, wall.x, wall.y, wall.width, wall.height, null);
        }

        // Draw food dots as small white rectangles
        g.setColor(Color.WHITE);
        for (Block food : foods) {
            g.fillRect(food.x, food.y, food.width, food.height);
        }

        // Draw cherry if it exists
        if (cherry != null) {
            g.drawImage(cherry.image, cherry.x, cherry.y, cherry.width, cherry.height, null);
        }

        // Draw score and lives (or game over message)
        g.setFont(new Font("Arial", Font.PLAIN, 18));
        if (gameOver) {
            g.drawString("Game Over: " + score, tileSize / 2, tileSize / 2);
        } else {
            g.drawString("x" + lives + " Score: " + score, tileSize / 2, tileSize / 2);
        }
    }

    // Method to update the game state (movement, collisions, scoring)
    public void move() {
        // Update Pac-Man's position based on velocity
        pacman.x += pacman.velocityX;
        pacman.y += pacman.velocityY;

        // Check for wall collisions with Pac-Man
        for (Block wall : walls) {
            if (collision(pacman, wall)) {
                pacman.x -= pacman.velocityX; // Revert X movement
                pacman.y -= pacman.velocityY; // Revert Y movement
                break;
            }
        }

        // Update ghosts and check for collisions with Pac-Man
        for (Block ghost : ghosts) {
            if (collision(ghost, pacman)) { // Pac-Man hits a ghost
                lives -= 1; // Lose a life
                if (lives == 0) { // Check if game is over
                    gameOver = true;
                    return;
                }
                resetPositions(); // Reset positions after losing a life
            }

            // Special ghost behavior: force upward movement at row 9 if moving horizontally
            if (ghost.y == tileSize * 9 && ghost.direction != 'U' && ghost.direction != 'D') {
                ghost.updateDirection('U');
            }
            ghost.x += ghost.velocityX; // Move ghost in X direction
            ghost.y += ghost.velocityY; // Move ghost in Y direction
            // Check for wall collisions or board edge for ghosts
            for (Block wall : walls) {
                if (collision(ghost, wall) || ghost.x <= 0 || ghost.x + ghost.width >= boardWidth) {
                    ghost.x -= ghost.velocityX; // Revert movement
                    ghost.y -= ghost.velocityY;
                    char newDirection = directions[random.nextInt(4)]; // Pick a new random direction
                    ghost.updateDirection(newDirection);
                }
            }
        }

        // Check for food collisions and update score
        Block foodEaten = null;
        for (Block food : foods) {
            if (collision(pacman, food)) {
                foodEaten = food; // Mark food as eaten
                score += 10; // Add 10 points to score
            }
        }
        foods.remove(foodEaten); // Remove eaten food

        // Handle cherry collision and timeout
        if (cherry != null) {
            if (collision(pacman, cherry)) { // Pac-Man collects cherry
                score += 100; // Add bonus points
                cherry = null; // Remove cherry
            } else if (System.currentTimeMillis() - cherrySpawnTime > 5000) { // Cherry times out after 5 seconds
                cherry = null;
            }
        }

        // Randomly spawn a cherry if none exists (1 in 200 chance per frame)
        if (cherry == null && random.nextInt(200) == 0) {
            spawnCherry();
        }

        // If all food is eaten, reset the map and positions
        if (foods.isEmpty()) {
            loadMap();
            resetPositions();
        }
    }

    // Helper method to spawn a cherry in a valid position (not inside walls)
    private void spawnCherry() {
        int x, y;
        boolean valid;
        do {
            x = random.nextInt(columnCount) * tileSize; // Random X position
            y = random.nextInt(rowCount) * tileSize; // Random Y position
            valid = true;
            // Check if the position overlaps with any wall
            for (Block wall : walls) {
                if (x < wall.x + wall.width &&
                        x + tileSize > wall.x &&
                        y < wall.y + wall.height &&
                        y + tileSize > wall.y) {
                    valid = false; // Position is invalid if it overlaps a wall
                    break;
                }
            }
        } while (!valid); // Repeat until a valid position is found
        cherry = new Block(cherryImage, x, y, tileSize, tileSize); // Spawn cherry
        cherrySpawnTime = System.currentTimeMillis(); // Record spawn time
    }

    // Method to check for collision between two blocks
    public boolean collision(Block a, Block b) {
        return a.x < b.x + b.width && // Check X overlap
                a.x + a.width > b.x &&
                a.y < b.y + b.height && // Check Y overlap
                a.y + a.height > b.y;
    }

    // Reset Pac-Man and ghosts to their starting positions
    public void resetPositions() {
        pacman.reset(); // Reset Pac-Man position
        pacman.velocityX = 0; // Stop Pac-Man movement
        pacman.velocityY = 0;
        for (Block ghost : ghosts) {
            ghost.reset(); // Reset ghost position
            char newDirection = directions[random.nextInt(4)]; // Assign random direction
            ghost.updateDirection(newDirection);
        }
    }

    // ActionListener method called by the game loop timer to update and redraw the
    // game
    @Override
    public void actionPerformed(ActionEvent e) {
        move(); // Update game state
        repaint(); // Redraw the screen
        if (gameOver) { // Stop the game loop if game is over
            gameLoop.stop();
        }
    }

    // KeyListener methods (only keyReleased is used)
    @Override
    public void keyTyped(KeyEvent e) {
    } // Not used

    @Override
    public void keyPressed(KeyEvent e) {
    } // Not used

    @Override
    public void keyReleased(KeyEvent e) {
        if (gameOver) { // Reset the game if over and a key is pressed
            loadMap();
            resetPositions();
            lives = 3; // Restore lives
            score = 0; // Reset score
            gameOver = false; // Clear game over flag
            gameLoop.start(); // Restart game loop
        }
        // Update Pac-Man's direction based on arrow key input
        if (e.getKeyCode() == KeyEvent.VK_UP) {
            pacman.updateDirection('U');
        } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            pacman.updateDirection('D');
        } else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            pacman.updateDirection('L');
        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            pacman.updateDirection('R');
        }

        // Update Pac-Man's image to match its direction
        if (pacman.direction == 'U') {
            pacman.image = pacmanUpImage;
        } else if (pacman.direction == 'D') {
            pacman.image = pacmanDownImage;
        } else if (pacman.direction == 'L') {
            pacman.image = pacmanLeftImage;
        } else if (pacman.direction == 'R') {
            pacman.image = pacmanRightImage;
        }
    }
}