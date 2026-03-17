import model.Bidding;
import model.Deck;
import model.Player;

import java.util.Scanner;

public class Lorris {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println("Enter number of human players (1-6):");
        int humanCount = sc.nextInt();
        if (humanCount < 1 || humanCount > 6) humanCount = 1;

        // Ask for AI difficulty once for all AI players
        boolean allExpertAI = false;
        if (humanCount < 6) {
            System.out.println("AI difficulty for all AI players (1=basic, 2=expert):");
            int diff = sc.nextInt();
            allExpertAI = (diff == 2);
        }

        // Initialize players
        Player[] players = new Player[6];
        boolean[] isAI = new boolean[6];
        boolean[] isExpertAI = new boolean[6];

        for (int i = 0; i < 6; i++) {
            if (i < humanCount) {
                players[i] = new Player("Human " + (i + 1));
                isAI[i] = false;
                isExpertAI[i] = false;
            } else {
                players[i] = new Player("AI " + (i + 1));
                isAI[i] = true;
                isExpertAI[i] = allExpertAI; // All AIs have the same difficulty
            }
        }

        // Initialize deck and shuffle
        Deck deck = new Deck();
        deck.shuffle();

        // Deal cards to players FIRST
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 8; j++) {
                players[i].addCard(deck.dealCard());
            }
        }

        // Show hands to human players
        for (int i = 0; i < humanCount; i++) {
            System.out.println(players[i].getName() + "'s hand: " + players[i].getHand());
        }

        // THEN do bidding phase (players need to see their hands to bid)
        Bidding bidding = new Bidding(players, isAI, isExpertAI);
        bidding.startBidding(sc);
        Player declarer = bidding.getWinner();
        String trumpSuit = bidding.getTrump();

        if (declarer == null) {
            System.out.println("No bid was made. Game over.");
            sc.close();
            return;
        }

        System.out.println("Declarer: " + declarer.getName() + ", Trump: " + trumpSuit);

        // Show AI types
        System.out.println("All AI players are: " + (allExpertAI ? "EXPERT" : "BASIC"));

        // Play tricks
        PlayTricks.playAllTricksWithDifficulty(players, declarer, trumpSuit, isAI, isExpertAI);

        sc.close();
    }
}