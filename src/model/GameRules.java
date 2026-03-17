package model;

import java.util.ArrayList;

public class GameRules {

    // Check if player has cards of the required suit
    public static boolean hasRequiredSuit(ArrayList<Card> hand, String requiredSuit) {
        for (Card c : hand) {
            if (c.getSuit().equals(requiredSuit)) return true;
        }
        return false;
    }

    // Get valid cards the player can play
    public static ArrayList<Integer> getPlayableIndices(ArrayList<Card> hand, String requiredSuit) {
        ArrayList<Integer> indices = new ArrayList<>();
        boolean mustFollow = hasRequiredSuit(hand, requiredSuit);

        for (int i = 0; i < hand.size(); i++) {
            Card c = hand.get(i);
            if (mustFollow) {
                if (c.getSuit().equals(requiredSuit)) indices.add(i);
            } else {
                indices.add(i); // can play any card
            }
        }
        return indices;
    }
}