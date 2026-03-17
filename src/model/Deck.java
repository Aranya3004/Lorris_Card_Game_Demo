package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Deck {
    private ArrayList<Card> cards;

    public Deck() {
        cards = new ArrayList<>();
        String[] suits = {"Spades", "Clubs", "Diamonds", "Hearts"};
        String[] ranks = {"A", "K", "Q", "J", "10", "9", "8", "7", "6", "5", "4", "3"};

        // Create cards in proper order: Spades, Clubs, Diamonds, Hearts
        for (String suit : suits) {
            for (String rank : ranks) {
                cards.add(new Card(suit, rank));
            }
        }

        // Remove Spades 3 (as per the original requirement)
        for (int i = 0; i < cards.size(); i++) {
            Card c = cards.get(i);
            if (c.getSuit().equals("Spades") && c.getRank().equals("3")) {
                cards.remove(i);
                break;
            }
        }

        // Add Joker (should be included as per the original requirement)
        cards.add(new Card("Joker", "Joker"));
    }

    public ArrayList<Card> getCards() {
        return cards;
    }

    public void shuffle() {
        Collections.shuffle(cards);
    }

    public Card dealCard() {
        if (cards.isEmpty()) {
            return null;
        }
        return cards.remove(0);
    }

    // Method to sort the deck in proper order
    public void sort() {
        Collections.sort(cards, new Comparator<Card>() {
            @Override
            public int compare(Card c1, Card c2) {
                // Joker is always the highest card
                if (c1.getSuit().equals("Joker") && c2.getSuit().equals("Joker")) return 0;
                if (c1.getSuit().equals("Joker")) return -1; // Joker is higher than any other card
                if (c2.getSuit().equals("Joker")) return 1;  // Any card is lower than Joker

                // Compare suits: Spades > Clubs > Diamonds > Hearts
                int suitCompare = getSuitValue(c2.getSuit()) - getSuitValue(c1.getSuit());
                if (suitCompare != 0) return suitCompare;

                // Compare ranks: A > K > Q > J > 10 > 9 > 8 > 7 > 6 > 5 > 4 > 3
                return getRankValue(c2.getRank()) - getRankValue(c1.getRank());
            }

            private int getSuitValue(String suit) {
                switch(suit) {
                    case "Spades": return 4;
                    case "Clubs": return 3;
                    case "Diamonds": return 2;
                    case "Hearts": return 1;
                    default: return 0;
                }
            }

            private int getRankValue(String rank) {
                switch(rank) {
                    case "A": return 14;
                    case "K": return 13;
                    case "Q": return 12;
                    case "J": return 11;
                    case "10": return 10;
                    case "9": return 9;
                    case "8": return 8;
                    case "7": return 7;
                    case "6": return 6;
                    case "5": return 5;
                    case "4": return 4;
                    case "3": return 3;
                    default: return 0;
                }
            }
        });
    }

    @Override
    public String toString() {
        return "Deck{" +
                "cards=" + cards +
                '}';
    }
}