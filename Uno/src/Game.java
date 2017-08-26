import javax.swing.*;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Class the represents a Cardgame containing of the Cards and Players
 */
public class Game {

    private List<UnoCard> cardDeck;
    private List<Player> players;
    private UnoCard actualCard;
    private List<UnoCard> cardStack;
    private Player actualPlayer;
    private Controller controller;
    
    boolean isHumanPlayersTurn;

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public Player getActualPlayer() {
        return actualPlayer;
    }

    public void setActualPlayer(Player actualPlayer) {
        this.actualPlayer = actualPlayer;
    }

    /**
     * Standard Konstruktor
     * @param controller
     */
    public Game(Controller controller) {
        cardDeck = new ArrayList<UnoCard>();
        players = new ArrayList<Player>();
        cardStack = new ArrayList<UnoCard>();
        this.controller = controller;
    }

    /**
     * Hinzufügen eines neuen Spielers zum Spiel
     * @param position Position an der der Spieler eingefügt wird
     * @param player Spieler- Objekt
     */
    public void addPlayer(int position, Player player) {
        players.add(position, player);
    }

    /**
     * Mischen des Kartendecks des Spiels
     */
    public void Shuffle() {
        Collections.shuffle(cardDeck);
    }

    /**
     * Auspielen einer Karte durch einen realen Spieler
     *
     * @param cardToPlay Karte die ausgespielt werden soll
     * @param rPlayer    Spieler Objekt des menschlichen Spielers
     * @return Gibt an ob der Zug gelunge ist. Also ob die Karte auf der Hand ist und auf die liegende passt
     */

    public boolean realPlay(UnoCard cardToPlay, RealPlayer rPlayer) {

        // wird true, wenn die Karte passt
        boolean validPlay = false;

        System.out.println("Zu spielende Karte: " + cardToPlay.get_color() + ", " + cardToPlay.get_number());

        boolean isOnHand = false;
        for (UnoCard c : rPlayer.cardsOnHand) {
            if (cardToPlay.equals(c)) {
                cardToPlay = c;
                isOnHand = true;
            }
        }

        if (isOnHand) {
            System.out.println("Auf Hand");
            if (actualCard.fits(cardToPlay)) {
                System.out.println("Karte passt");
                actualCard = cardToPlay;
                controller.mainFrame.refreshStack(actualCard);
                cardStack.add(actualCard);
                rPlayer.cardsOnHand.remove(cardToPlay);

                System.out.println("Nach Kartenlegen: " + actualPlayer.cardsOnHand.size() + " Karten");
                for (UnoCard c : actualPlayer.cardsOnHand) {
                    System.out.println(c.get_color() + ", " + c.get_number());
                }
                System.out.println("Noch " + cardDeck.size() + " Karten auf Deck");
                System.out.println("Bereits " + cardStack.size() + " Karten auf Haufen");
                System.out.println();
                validPlay = true;
            } else {
                validPlay = false;
                System.out.println("Karte passt nicht. Bitte wählen Sie erneut oder Ziehen Sie eine Karte");
            }
        }
        return validPlay;
    }

    /**
     * Auspielen einer Karte durch einen virtuellen Spieler
     *
     * @param vPlayer
     */
    public void virtualPlay(final VirtualPlayer vPlayer) {

        boolean cardFits = false;
        for (final UnoCard c : vPlayer.cardsOnHand) {
            if (actualCard.fits(c)) {
                actualCard = c;

                final String protocol = vPlayer.get_name() + " spielt: " + c.get_color() + ", " + c.get_number();

                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {

                        controller.mainFrame.refreshStack(actualCard);

                        controller.mainFrame.writeToProtocol(protocol);
                    }
                };

                SwingUtilities.invokeLater(runnable);

                cardStack.add(actualCard);
                vPlayer.cardsOnHand.remove(actualCard);
                cardFits = true;
                System.out.println(vPlayer.get_name() + " spielt: " + c.get_color() + ", " + c.get_number());
                break;
            }
        }
        if (cardFits == false) {
            drawCard(vPlayer);
        }
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

    public void uncoverFirstCard() {
        actualCard = cardDeck.get(0);
        cardStack.add(cardDeck.get(0));
        cardDeck.remove(0);
        controller.mainFrame.refreshStack(actualCard);
    }

    public void DealCards() {
        for (Player p : players) {
            for (int i = 0; i < 7; i++) {
                p.cardsOnHand.add(cardDeck.get(0));
                cardDeck.remove(0);
            }
        }
    }

    /**
     * Karte ziehen
     */
    public void drawCard(Player drawingPlayer) {
        if (cardDeck.size() < 1) {
            for (int i = 0; i < cardStack.size(); i++) {
                cardDeck.add(cardStack.get(i));
            }
            cardStack.clear();
            Collections.shuffle(cardDeck);
        }

        final String protocol = drawingPlayer.get_name() + " hat gezogen";

        // hier eine coole Syntax für das schreiben eines runnable, die intelliJ vorgeschlagen hat
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                controller.mainFrame.writeToProtocol(protocol);
            }
        };

        SwingUtilities.invokeLater(runnable);

        UnoCard drawedCard = cardDeck.get(0);
        drawingPlayer.cardsOnHand.add(drawedCard);
        cardDeck.remove(drawedCard);
    }

    /**
     * Spielen einer Runde, wenn der menschliche Spieler dran ist wird aus der Methode gesprungen
     */
    public void playRound() {
        // Spiele eine Runde

        for (int position = 0; position < players.size(); position++) {

            actualPlayer = players.get(position);
            System.out.print("Liegende Karte: ");
            System.out.println(actualCard.get_color() + ", " + actualCard.get_number());
            System.out.print("Spieler: ");
            System.out.println(actualPlayer.get_name());

            final String protocol;
            if(actualPlayer instanceof VirtualPlayer){
                protocol = actualPlayer.get_name() + " ist am Zug.\n"
                        + actualPlayer.get_name() + " hat " + actualPlayer.cardsOnHand.size() + " Karten.";
            }
            else{
                protocol = "Du bist am Zug.\n";
            }



            try {
                SwingUtilities.invokeAndWait(new Runnable() {
                    @Override
                    public void run() {
                        controller.mainFrame.writeToProtocol(protocol);
                    }
                });
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }

            // Pause, wenn bei den nicht-menschlichen Spielern
            if (actualPlayer instanceof VirtualPlayer){
                insertDelay(800);
            }

            if (actualPlayer.cardsOnHand.size() < 2 && actualPlayer.cardsOnHand.size() > 0) {
                try {
                    SwingUtilities.invokeAndWait(new Runnable() {
                        @Override
                        public void run() {
                            controller.mainFrame.writeToProtocol("UNO!!!");
                        }
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }

                System.out.println("UNO!!!!");
            }
            if (actualPlayer.cardsOnHand.size() < 1) {
                System.out.println("GEWONNEN!!!!!!!!!");
                String message = actualPlayer.get_name();
                message += " hat gewonnen!";
                controller.mainFrame.Message(message);
                return;
            }
            System.out.println("Vor Kartenlegen: " + actualPlayer.cardsOnHand.size() + " Karten");
            for (UnoCard c : actualPlayer.cardsOnHand) {
                System.out.println(c.get_color() + ", " + c.get_number());
            }

            if (actualPlayer instanceof VirtualPlayer) {

                this.virtualPlay((VirtualPlayer) actualPlayer);
            }

            // bricht hier ab wenn es ein echter Spieler ist und wartet auf das Event
            else if (actualPlayer instanceof RealPlayer) {

                isHumanPlayersTurn = true;
                return;
            }

            System.out.println("Nach Kartenlegen: " + actualPlayer.cardsOnHand.size() + " Karten");
            for (UnoCard c : actualPlayer.cardsOnHand) {
                System.out.println(c.get_color() + ", " + c.get_number());
            }
            System.out.println("Noch " + cardDeck.size() + " Karten auf Deck");
            System.out.println("Bereits " + cardStack.size() + " Karten auf Haufen");
            System.out.println();
        }
    }

    /**
     * Fügt eine Verzögerung in den Spielverlauf ein, die einen Zug eines virtuellen Spielers simuliert
     *
     * @param milliseconds Zeit in Millisekunden, die die Pause dauern soll
     */
    private void insertDelay(long milliseconds) {

        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

