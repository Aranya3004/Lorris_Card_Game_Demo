package model;

import model.Player;
import model.Card;
import java.util.ArrayList;
import java.util.Random;

public class BiddingUtils {

    public static int evaluateHandStrength(Player player, boolean isExpert) {
        ArrayList<Card> hand = player.getHand();
        int strength = 0;

        // Count high cards (J, Q, K, A, Joker)
        for (Card card : hand) {
            strength += getCardValue(card);
        }

        // Expert AI considers suit distribution
        if (isExpert) {
            strength += evaluateSuitDistribution(hand);
        }

        return strength;
    }

    private static int getCardValue(Card card) {
        switch(card.getRank()) {
            case "J": return 1;
            case "Q": return 2;
            case "K": return 3;
            case "A": return 4;
            case "Joker": return 8; // Joker is very valuable
            default: return 0;
        }
    }

    private static int evaluateSuitDistribution(ArrayList<Card> hand) {
        int[] suitCount = new int[4]; // Spades, Clubs, Diamonds, Hearts
        String[] suits = {"Spades", "Clubs", "Diamonds", "Hearts"};

        for (Card card : hand) {
            for (int i = 0; i < suits.length; i++) {
                if (card.getSuit().equals(suits[i])) {
                    suitCount[i]++;
                    break;
                }
            }
        }

        // Bonus for long suits (5+ cards)
        int bonus = 0;
        for (int count : suitCount) {
            if (count >= 5) bonus += 3;
            else if (count >= 4) bonus += 1;
        }

        return bonus;
    }

    public static int calculateAIBid(Player player, int currentHighestBid, boolean isExpert) {
        int handStrength = evaluateHandStrength(player, isExpert);
        Random rand = new Random();

        // Base bid calculation based on hand strength
        int baseBid;
        if (handStrength < 8) baseBid = 0; // Pass
        else if (handStrength < 12) baseBid = 1;
        else if (handStrength < 16) baseBid = 2;
        else if (handStrength < 20) baseBid = 3;
        else if (handStrength < 24) baseBid = 4;
        else if (handStrength < 28) baseBid = 5;
        else if (handStrength < 32) baseBid = 6;
        else if (handStrength < 36) baseBid = 7;
        else baseBid = 8;

        // Adjust based on current highest bid
        if (currentHighestBid > 0) {
            if (baseBid <= currentHighestBid) {
                // If we can't beat the current bid, pass
                if (rand.nextInt(100) < 80 || baseBid == 0) {
                    return 0; // Pass
                } else {
                    // Occasionally bid one higher if we have a decent hand
                    return Math.min(8, currentHighestBid + 1);
                }
            }
        }

        // Add some randomness to AI bidding (expert AI is more accurate)
        int randomFactor = isExpert ? rand.nextInt(2) - 0 : rand.nextInt(3) - 1;
        return Math.max(0, Math.min(8, baseBid + randomFactor));
    }

    public static String chooseTrumpSuit(Player player, boolean isExpert) {
        ArrayList<Card> hand = player.getHand();
        String[] suits = {"Spades", "Clubs", "Diamonds", "Hearts"};
        int[] suitStrength = new int[4];

        // Calculate strength for each suit
        for (Card card : hand) {
            for (int i = 0; i < suits.length; i++) {
                if (card.getSuit().equals(suits[i])) {
                    suitStrength[i] += getCardValue(card);
                    // Bonus for having more cards in this suit
                    suitStrength[i] += 1;
                    break;
                }
            }
        }

        // Find the strongest suit
        int maxStrength = -1;
        String chosenSuit = suits[0];

        for (int i = 0; i < suitStrength.length; i++) {
            if (suitStrength[i] > maxStrength) {
                maxStrength = suitStrength[i];
                chosenSuit = suits[i];
            }
        }

        return chosenSuit;
    }
}