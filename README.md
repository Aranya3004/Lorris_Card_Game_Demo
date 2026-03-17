LorrisDemo 🃏
A modular, object-oriented trick-taking card game simulation built with Java 21.

LorrisDemo is a robust Java application that simulates the core mechanics of a strategic trick-taking card game. Designed with strict adherence to Object-Oriented Design (OOD) principles, the project breaks down complex game phases—dealing, bidding, and trick-playing—into discrete, testable components. It serves as an excellent foundation for building out full card game clients or exploring modern Java development practices.

📖 Game Overview
At its core, LorrisDemo replicates a traditional trick-taking game lifecycle:

Dealing: A standard deck of cards is shuffled and distributed equally among the players.

Bidding Phase: Players evaluate their hands and bid on the number of tricks they expect to win (or the trump suit, depending on the specific rule set in play).

Trick-Taking Phase: Players take turns playing cards into the center. The highest card of the leading suit (or the highest trump card) wins the trick. The simulation handles the logic for legal plays, trick evaluation, and scorekeeping.

🛠️ Technologies Used
Java (JDK 21): Leverages modern Java features for clean, efficient, and maintainable code.

Object-Oriented Design: Heavily utilizes encapsulation, polymorphism, and modularity.

Git: Version control for tracking development and managing feature branches.

🏗️ Architecture & Key Classes
The project is structured around clear, real-world noun-based classes to maintain a highly readable codebase:

Lorris: The main game controller. Orchestrates the state machine, transitioning the game from dealing to bidding, to playing, and finally to scoring.

Bidding: Encapsulates the logic and rules for the bidding phase, managing player declarations and determining the winning bid.

Player: Represents an individual participant (human or bot), managing their current hand of cards, their bid, and tricks won.

Deck & Card: Fundamental data structures representing the deck and individual playing cards, including shuffling and drawing mechanics.

Trick: Manages the lifecycle of a single trick, enforcing suit-following rules and calculating the winner of the round.

🚀 How to Run
LorrisDemo is designed with modularity in mind, allowing developers to run and test specific phases of the game in isolation. The project includes several pre-defined run configurations:

Clone the repository:

Bash
git clone https://github.com/Aranya3004/LorrisDemo.git
Open the project in your preferred IDE (IntelliJ IDEA, Eclipse, etc.).

Execute one of the following run configurations:

Lorris or Main: Runs the complete, end-to-end game simulation.

Bidding: Runs an isolated simulation of the bidding phase for testing edge cases in bid logic.

DealCardsToPlayers: Tests the deck shuffling and distribution algorithms.

PlayTricks: Bypasses dealing and bidding to exclusively test the trick-taking evaluation logic.

🔮 Future Enhancements
While the core simulation is fully functional, this project is built to be expanded. Planned future enhancements include:

Graphical User Interface (GUI): Transitioning from console-based outputs to a rich visual experience using JavaFX.

Network Multiplayer: Implementing WebSockets or Java Sockets to allow players to compete over a local network or the internet.

AI Opponents: Introducing basic to advanced heuristic-based bots for single-player functionality.

Rule Variants: Abstracting the core rules engine to support multiple popular trick-taking variants (e.g., Spades, Hearts, Bridge).
