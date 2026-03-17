package model;

public class Card implements Comparable<Card> {
    private String suit;
    private String rank;

    public Card(String suit, String rank) {
        this.suit = suit;
        this.rank = rank;
    }

    public String getSuit() {
        return suit;
    }

    public void setSuit(String suit) {
        this.suit = suit;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    @Override
    public int compareTo(Card other) {
        // Joker is always the highest card
        if (this.suit.equals("Joker") && other.suit.equals("Joker")) return 0;
        if (this.suit.equals("Joker")) return -1; // Joker is higher than any other card
        if (other.suit.equals("Joker")) return 1;  // Any card is lower than Joker

        // Compare suits: Spades > Clubs > Diamonds > Hearts
        int suitCompare = getSuitValue(other.suit) - getSuitValue(this.suit);
        if (suitCompare != 0) return suitCompare;

        // Compare ranks: A > K > Q > J > 10 > 9 > 8 > 7 > 6 > 5 > 4 > 3
        return getRankValue(other.rank) - getRankValue(this.rank);
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

    @Override
    public String toString() {
        return suit + "-" + rank;
    }
}