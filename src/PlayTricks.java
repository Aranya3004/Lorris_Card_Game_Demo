import model.Card;
import model.GameRules;
import model.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class PlayTricks {

    public static void playAllTricksWithDifficulty(Player[] players, Player declarer, String trump,
                                                   boolean[] isAI, boolean[] isExpertAI) {
        Scanner sc = new Scanner(System.in);

        int teamAScore = 0;
        int teamBScore = 0;

        // Find the declarer's index to start with them as leader
        int leaderIndex = 0;
        for (int i = 0; i < players.length; i++) {
            if (players[i] == declarer) {
                leaderIndex = i;
                break;
            }
        }

        int[] trumpCount = {countTrumps(players, trump)};
        HashMap<String, Integer> playedCards = new HashMap<>();

        for (int t = 0; t < 8; t++) {
            ArrayList<Card> trick = new ArrayList<>();
            Player leader = players[leaderIndex];
            boolean jokerLed = false;
            boolean isLastTrick = (t == 7); // 8th trick (0-indexed)

            System.out.println("\nTrick " + (t + 1) + ": " + leader.getName() + " leads.");
            Card leadCard;

            if (isAI[leaderIndex]) {
                leadCard = isExpertAI[leaderIndex]
                        ? expertAIPlay(leader, null, trump, trumpCount, playedCards, isLastTrick)
                        : basicAIPlay(leader, null, trump, isLastTrick);
                System.out.println(leader.getName() + " played: " + leadCard);
            } else {
                System.out.println(leader.getName() + "'s hand: " + leader.getHand());
                System.out.println("Enter card index to play:");
                int idx = sc.nextInt();
                leadCard = leader.getHand().remove(idx);
                System.out.println(leader.getName() + " played: " + leadCard);
            }

            // Special rule: On last trick, Joker becomes 3 of Spades
            if (isLastTrick && leadCard.getRank().equals("Joker")) {
                System.out.println("Special rule: Joker becomes 3 of Spades on the last trick!");
                leadCard = new Card("Spades", "3"); // Transform Joker to 3 of Spades
            }

            // Check if Joker was led (before potential transformation)
            jokerLed = leadCard.getRank().equals("Joker") && !isLastTrick;

            trackCard(leadCard, playedCards);
            if (leadCard.getSuit().equals(trump)) trumpCount[0]--;
            trick.add(leadCard);

            String leadSuit = leadCard.getSuit();
            int currentIndex = (leaderIndex + 1) % 6;

            for (int i = 1; i < 6; i++) {
                Player p = players[currentIndex];
                Card played;

                // Special rule: If Joker was led, players must play Spades if they have them
                String requiredSuit = jokerLed ? "Spades" : leadSuit;

                if (isAI[currentIndex]) {
                    played = isExpertAI[currentIndex]
                            ? expertAIPlay(p, requiredSuit, trump, trumpCount, playedCards, isLastTrick)
                            : basicAIPlay(p, requiredSuit, trump, isLastTrick);
                    System.out.println(p.getName() + " played: " + played);
                } else {
                    ArrayList<Integer> playable = GameRules.getPlayableIndices(p.getHand(), requiredSuit);
                    System.out.println(p.getName() + "'s hand: " + p.getHand());
                    System.out.println("Playable card indices: " + playable);

                    int playIdx;
                    while (true) {
                        playIdx = sc.nextInt();
                        if (playable.contains(playIdx)) break;
                        System.out.println("Invalid card. Choose from playable indices: " + playable);
                    }
                    played = p.getHand().remove(playIdx);
                }

                // Special rule: On last trick, Joker becomes 3 of Spades
                if (isLastTrick && played.getRank().equals("Joker")) {
                    System.out.println("Special rule: Joker becomes 3 of Spades on the last trick!");
                    played = new Card("Spades", "3"); // Transform Joker to 3 of Spades
                }

                trackCard(played, playedCards);
                if (played.getSuit().equals(trump)) trumpCount[0]--;

                System.out.println(p.getName() + " played: " + played);
                trick.add(played);
                currentIndex = (currentIndex + 1) % 6;
            }

            int winnerIndex = determineTrickWinner(trick, leaderIndex, trump);
            Card winningCard = trick.get((winnerIndex - leaderIndex + 6) % 6);

            System.out.println("Winner of trick: " + players[winnerIndex].getName() + " with " + winningCard);

            if (winnerIndex % 2 == 0) teamAScore++;
            else teamBScore++;

            leaderIndex = winnerIndex;
        }

        System.out.println("\nTeam A score: " + teamAScore);
        System.out.println("Team B score: " + teamBScore);

        if (teamAScore > teamBScore) System.out.println("Team A wins!");
        else if (teamBScore > teamAScore) System.out.println("Team B wins!");
        else System.out.println("It's a tie!");

        sc.close();
    }

    // -------- AI Implementations --------
    private static Card basicAIPlay(Player p, String leadSuit, String trump, boolean isLastTrick) {
        ArrayList<Integer> playable;

        if (leadSuit == null) playable = nonTrumpLowestCards(p, trump);
        else playable = GameRules.getPlayableIndices(p.getHand(), leadSuit);

        if (playable.isEmpty()) playable = allCardsIndices(p);

        // Special consideration for last trick - don't play Joker if it will become weak
        if (isLastTrick) {
            ArrayList<Integer> nonJokerCards = new ArrayList<>();
            for (int idx : playable) {
                if (!p.getHand().get(idx).getRank().equals("Joker")) {
                    nonJokerCards.add(idx);
                }
            }
            if (!nonJokerCards.isEmpty()) {
                playable = nonJokerCards;
            }
        }

        int chosenIdx = lowestCardIndex(p, playable);
        return p.getHand().remove(chosenIdx);
    }

    private static Card expertAIPlay(Player p, String leadSuit, String trump, int[] trumpCount,
                                     HashMap<String, Integer> playedCards, boolean isLastTrick) {
        ArrayList<Integer> playable;

        if (leadSuit == null) {
            playable = nonTrumpLowestCards(p, trump);
            if (playable.isEmpty()) playable = allCardsIndices(p);
        } else playable = GameRules.getPlayableIndices(p.getHand(), leadSuit);

        // Special consideration for last trick - don't play Joker if it will become weak
        if (isLastTrick) {
            ArrayList<Integer> nonJokerCards = new ArrayList<>();
            for (int idx : playable) {
                if (!p.getHand().get(idx).getRank().equals("Joker")) {
                    nonJokerCards.add(idx);
                }
            }
            if (!nonJokerCards.isEmpty()) {
                playable = nonJokerCards;
            }
        }

        int chosenIdx;
        if (leadSuit != null) {
            ArrayList<Integer> leadSuitCards = new ArrayList<>();
            for (int idx : playable)
                if (p.getHand().get(idx).getSuit().equals(leadSuit)) leadSuitCards.add(idx);

            if (!leadSuitCards.isEmpty()) {
                chosenIdx = lowestSafeCard(p, leadSuitCards, leadSuit, playedCards);
            } else {
                ArrayList<Integer> trumpCards = new ArrayList<>();
                for (int idx : playable)
                    if (p.getHand().get(idx).getSuit().equals(trump)) trumpCards.add(idx);

                if (!trumpCards.isEmpty()) {
                    if (trumpCount[0] <= 2) chosenIdx = highestCardIndex(p, trumpCards);
                    else chosenIdx = lowestCardIndex(p, trumpCards);
                    trumpCount[0]--;
                } else chosenIdx = lowestCardIndex(p, playable);
            }
        } else {
            chosenIdx = lowestCardIndex(p, playable);
        }

        return p.getHand().remove(chosenIdx);
    }

    // -------- Utility Methods --------
    private static ArrayList<Integer> nonTrumpLowestCards(Player p, String trump) {
        ArrayList<Integer> list = new ArrayList<>();
        for (int i = 0; i < p.getHand().size(); i++)
            if (!p.getHand().get(i).getSuit().equals(trump)) list.add(i);
        return list;
    }

    private static ArrayList<Integer> allCardsIndices(Player p) {
        ArrayList<Integer> list = new ArrayList<>();
        for (int i = 0; i < p.getHand().size(); i++) list.add(i);
        return list;
    }

    private static int lowestSafeCard(Player p, ArrayList<Integer> leadSuitCards, String leadSuit, HashMap<String, Integer> playedCards) {
        int chosenIdx = leadSuitCards.get(0);
        for (int idx : leadSuitCards) {
            Card c = p.getHand().get(idx);
            if (rankValue(c) < rankValue(p.getHand().get(chosenIdx)) && !likelyToWin(c, leadSuit, playedCards))
                chosenIdx = idx;
        }
        return chosenIdx;
    }

    private static boolean likelyToWin(Card c, String leadSuit, HashMap<String, Integer> playedCards) {
        int highRankThreshold = 11; // J or higher
        String key = leadSuit + "-" + c.getRank();
        return rankValue(c) >= highRankThreshold && !playedCards.containsKey(key);
    }

    private static int lowestCardIndex(Player p, ArrayList<Integer> indices) {
        int chosenIdx = indices.get(0);
        for (int idx : indices)
            if (rankValue(p.getHand().get(idx)) < rankValue(p.getHand().get(chosenIdx)))
                chosenIdx = idx;
        return chosenIdx;
    }

    private static int highestCardIndex(Player p, ArrayList<Integer> indices) {
        int chosenIdx = indices.get(0);
        for (int idx : indices)
            if (rankValue(p.getHand().get(idx)) > rankValue(p.getHand().get(chosenIdx)))
                chosenIdx = idx;
        return chosenIdx;
    }

    private static void trackCard(Card c, HashMap<String, Integer> playedCards) {
        playedCards.put(c.getSuit() + "-" + c.getRank(), 1);
    }

    private static int determineTrickWinner(ArrayList<Card> trick, int leaderIndex, String trump) {
        Card winningCard = trick.get(0);
        int winnerIndex = leaderIndex;
        for (int i = 1; i < trick.size(); i++) {
            Card c = trick.get(i);
            if (c.getSuit().equals(trump) && !winningCard.getSuit().equals(trump)) {
                winningCard = c;
                winnerIndex = (leaderIndex + i) % 6;
            } else if (c.getSuit().equals(winningCard.getSuit()) && rankValue(c) > rankValue(winningCard)) {
                winningCard = c;
                winnerIndex = (leaderIndex + i) % 6;
            } else if (c.getSuit().equals(trump) && winningCard.getSuit().equals(trump) &&
                    rankValue(c) > rankValue(winningCard)) {
                winningCard = c;
                winnerIndex = (leaderIndex + i) % 6;
            }
        }
        return winnerIndex;
    }

    private static int rankValue(Card c) {
        if (c.getRank().equals("Joker")) return 15; // Joker is the highest

        switch(c.getRank()) {
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

    private static int countTrumps(Player[] players, String trump) {
        int count = 0;
        for (Player p : players)
            for (Card c : p.getHand())
                if (c.getSuit().equals(trump)) count++;
        return count;
    }
}