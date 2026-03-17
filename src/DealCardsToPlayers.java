import model.Deck;
import model.Player;

public class DealCardsToPlayers {
    public static void main(String[] args){
        Deck deck = new Deck();
        deck.shuffle();
        Player[] players = new Player[6];
        for (int i = 0; i <6 ; i++){
            players[i]= new Player("Player "+ (i + 1));
        }
        deal(deck, players);
        for (Player p:players){
            System.out.println(p.getName()+" 's hand: "+p.getHand());
        }
    }

    public static void deal(Deck deck, Player[] players) {
        for(int i =0; i<8;i++){
            for(Player p: players){
                p.addCard(deck.dealCard());
            }
        }
    }
}