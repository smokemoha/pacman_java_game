Pac-Man Clone in Java
Overview
This project is a Pac-Man clone built using Java and the Swing framework. The game features classic maze navigation with Pac-Man, ghosts, walls, food dots, and a bonus cherry that appears randomly to award extra points. It demonstrates essential game programming techniques including animation, collision detection, user input handling, and simple ghost AI.

Features
Classic Gameplay:
Navigate a maze, collect food dots, and avoid ghosts.

Bonus Cherry:
A randomly spawned cherry appears on the board for a limited time. Collect it to earn extra points.

Multiple Ghosts:
Ghosts with distinct colors and randomized movement add a challenging twist.

Collision Detection:
Efficient collision detection for walls, ghosts, food dots, and the bonus cherry.

Dynamic Map Loading:
The level layout is defined using a tile map, making modifications and level design straightforward.

Keyboard Controls:
Use arrow keys to control Pac-Man’s direction.

Score and Lives Tracking:
The game tracks your score and remaining lives, restarting upon game over.

Installation and Setup
Prerequisites
Java Development Kit (JDK) 8 or higher

An IDE or text editor (e.g., IntelliJ IDEA, Eclipse, Visual Studio Code)

(Optional) A build tool like Maven or Gradle

How to Build and Run
Clone the Repository:

bash
Copy
Edit
git clone https://github.com/yourusername/pacman-java.git
cd pacman-java
Compile the Code:

Compile the project using your IDE or via the command line:

bash
Copy
Edit
javac -cp . *.java
Run the Game:

Execute the game by running the main class:

bash
Copy
Edit
java App
Resource Files:

Ensure that the following image files are in the proper resources directory (or in the same folder as your compiled classes):

wall.png

blueGhost.png

orangeGhost.png

pinkGhost.png

redGhost.png

pacmanUp.png

pacmanDown.png

pacmanLeft.png

pacmanRight.png

cherry.png

Gameplay Instructions
Objective:
Guide Pac-Man through the maze, eating food dots while avoiding ghosts. Collect the bonus cherry when it appears for extra points.

Controls:

Up Arrow: Move up

Down Arrow: Move down

Left Arrow: Move left

Right Arrow: Move right

Game Over:
Colliding with a ghost reduces your lives. The game ends when all lives are lost. Press any key to restart after a game over.