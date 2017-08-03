import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Game {

    static List<UnoCard> cardDeck;
    static List<Player> players;
    static UnoCard actualCard;
    static List<UnoCard> cardStack;
    public static Player actualPlayer;

    public Game() {
        cardDeck = new ArrayList<UnoCard>();
        players = new ArrayList<Player>();
        cardStack = new ArrayList<UnoCard>();
    }

    public void addPlayer(Player player) {
        players.add(player);
    }

    public void StartGame() {
//        CreatePlayers();
        CreateCards();
        Shuffle(cardDeck);
        DealCards(players);
    }

    public void CreateCards() {
        // zwei Sätze Karten werden erstellt mit je 4 Farben und Zahlen 0 - 9
        for (int h = 0; h < 2; h++) {
            for (int i = 1; i < 10; i++) {
                cardDeck.add(new UnoCard(i, "blue"));
            }
            for (int i = 1; i < 10; i++) {
                cardDeck.add(new UnoCard(i, "red"));
            }
            for (int i = 1; i < 10; i++) {
                cardDeck.add(new UnoCard(i, "green"));
            }
            for (int i = 1; i < 10; i++) {
                cardDeck.add(new UnoCard(i, "yellow"));
            }
        }
        cardDeck.add(new UnoCard(0, "blue"));
        cardDeck.add(new UnoCard(0, "red"));
        cardDeck.add(new UnoCard(0, "green"));
        cardDeck.add(new UnoCard(0, "yellow"));
    }

    public void Shuffle(List<UnoCard> unoCards) {
        Collections.shuffle(unoCards);
    }

    public void DealCards(List<Player> players) {
        for (Player p : players) {
            for (int i = 0; i < 7; i++) {
                p.cardsOnHand.add(cardDeck.get(0));
                cardDeck.remove(0);
            }
        }
    }

    public void uncoverFirstCard(){
        actualCard = cardDeck.get(0);
        cardStack.add(cardDeck.get(0));
        cardDeck.remove(0);
        MainFrame.refreshStack(actualCard);
    }

    public void playRound(List<Player> players){
        // Spiele eine Runde
        for(int round = 0; round < players.size(); round++) {
            actualPlayer = players.get(round);
            System.out.print("Liegende Karte: ");
            System.out.println(Game.actualCard.get_color() + ", " + Game.actualCard.get_number());
            System.out.print("Spieler: ");
            System.out.println(actualPlayer.get_name());
            if(actualPlayer.cardsOnHand.size() < 2 && actualPlayer.cardsOnHand.size() > 0)
            {
                System.out.println("UNO!!!!");
            }
            if(actualPlayer.cardsOnHand.size() < 1)
            {
                System.out.println("GEWONNEN!!!!!!!!!");
                return;
            }
            System.out.println("Vor Kartenlegen: " + actualPlayer.cardsOnHand.size() + " Karten");
            for (UnoCard c : actualPlayer.cardsOnHand) {
                System.out.println(c.get_color() + ", " + c.get_number());
            }
            // Prüfe, ob Karte passt
            actualPlayer.play();
            System.out.println("Nach Kartenlegen: " + actualPlayer.cardsOnHand.size() + " Karten");
            for (UnoCard c : actualPlayer.cardsOnHand) {
                System.out.println(c.get_color() + ", " + c.get_number());
            }
            System.out.println("Noch " + cardDeck.size() + " Karten auf Deck");
            System.out.println("Bereits " + cardStack.size() + " Karten auf Haufen");
            System.out.println();
        }
    }
}

