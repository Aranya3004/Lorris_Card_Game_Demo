package model;

import java.util.Scanner;

public class Bidding {
    private Player[] players;
    private Player declarer;
    private String trump;
    private int highestBid;
    private boolean[] isAI;
    private boolean[] isExpertAI;

    public Bidding(Player[] players, boolean[] isAI, boolean[] isExpertAI) {
        this.players = players;
        this.isAI = isAI;
        this.isExpertAI = isExpertAI;
        this.highestBid = 0;
        this.declarer = null;
        this.trump = "";
    }

    public void startBidding(Scanner sc) {
        int passes = 0;
        boolean biddingEnded = false;

        String[] suits = {"Spades", "Clubs", "Diamonds", "Hearts"};

        System.out.println("\n=== BIDDING PHASE ===");
        System.out.println("Each player gets one chance to bid in order.");

        // Each player bids exactly once in order
        for (int currentPlayer = 0; currentPlayer < 6 && !biddingEnded; currentPlayer++) {
            Player p = players[currentPlayer];
            int bid;

            if (isAI[currentPlayer]) {
                // AI player bids automatically
                bid = BiddingUtils.calculateAIBid(p, highestBid, isExpertAI[currentPlayer]);
                System.out.println(p.getName() + " is thinking...");
                try { Thread.sleep(1000); } catch (InterruptedException e) {} // Pause for realism

                if (bid == 0) {
                    System.out.println(p.getName() + " passes.");
                    passes++;
                } else {
                    System.out.println(p.getName() + " bids " + bid + " tricks.");
                    highestBid = bid;
                    declarer = p;
                    passes = 0; // Reset passes when someone bids

                    if (bid == 8) {
                        System.out.println("model.Bidding ended immediately because " + p.getName() + " bid 8!");
                        biddingEnded = true;
                        break; // End bidding immediately
                    }
                }
            } else {
                // Human player bids
                System.out.println(p.getName() + "'s turn to bid. Current highest bid: " +
                        (highestBid > 0 ? highestBid : "None"));
                System.out.println("Enter your bid (0 to pass, 1-8):");
                bid = sc.nextInt();

                // Validate bid input
                while (bid < 0 || bid > 8 || (bid > 0 && bid <= highestBid)) {
                    if (bid < 0 || bid > 8) {
                        System.out.println("Invalid bid! Please enter a value between 0 and 8.");
                    } else {
                        System.out.println("Bid must be higher than current highest bid (" + highestBid + ") or 0 to pass.");
                    }
                    bid = sc.nextInt();
                }

                if (bid == 0) {
                    passes++;
                    System.out.println(p.getName() + " passes.");
                } else {
                    highestBid = bid;
                    declarer = p;
                    passes = 0; // Reset passes when someone bids
                    System.out.println(p.getName() + " bids " + bid + " tricks.");

                    if (bid == 8) {
                        System.out.println("model.Bidding ended immediately because " + p.getName() + " bid 8!");
                        biddingEnded = true;
                        break; // End bidding immediately
                    }
                }
            }
        }

        // If all players passed
        if (passes == 6) {
            System.out.println("All players passed. No bid made.");
            return;
        }

        // If bidding ended with no bid
        if (highestBid == 0) {
            System.out.println("No bid was made. Game cannot continue.");
            return;
        }

        System.out.println(declarer.getName() + " wins the bid with " + highestBid + " tricks");

        // Find the declarer's index for trump selection
        int declarerIndex = -1;
        for (int i = 0; i < players.length; i++) {
            if (players[i] == declarer) {
                declarerIndex = i;
                break;
            }
        }

        // Trump selection
        if (isAI[declarerIndex]) {
            // AI chooses trump automatically
            trump = BiddingUtils.chooseTrumpSuit(declarer, isExpertAI[declarerIndex]);
            System.out.println(declarer.getName() + " chooses " + trump + " as trump.");
        } else {
            // Human chooses trump
            System.out.println(declarer.getName() + ", choose the trump suit:");
            for (int i = 0; i < suits.length; i++) {
                System.out.println((i + 1) + ". " + suits[i]);
            }

            int choice = sc.nextInt();
            while (choice < 1 || choice > suits.length) {
                System.out.println("Invalid choice! Please enter a number between 1 and " + suits.length);
                choice = sc.nextInt();
            }

            trump = suits[choice - 1];
            System.out.println("Trump suit is: " + trump);
        }
    }

    public Player getDeclarer() {
        return declarer;
    }

    public String getTrump() {
        return trump;
    }

    public int getHighestBid() {
        return highestBid;
    }

    public Player getWinner() {
        return declarer;
    }
}