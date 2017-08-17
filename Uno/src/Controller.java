import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.lang.reflect.InvocationTargetException;
import java.util.Collections;

public class Controller implements MouseListener {

    private Game _game;
    private MainFrame _mainFrame;
    private boolean _isHumanPlayersTurn;


    public void initController(Game game, MainFrame mainFrame) {

        _game = game;
        _mainFrame = mainFrame;
    }

    /**
     * Event das die Ereignisse zum Karten legen und ziehen des menschlichen Spielers verarbeitet
     * @param mouseEvent
     */
    @Override
    public void mouseClicked(MouseEvent mouseEvent) {

        System.out.println("Ist EDT?: " + SwingUtilities.isEventDispatchThread());

        boolean repaint = false;
        // Unterscheiden ob Karte Ziehen oder Karte legen
        // Karte ziehen
        if (mouseEvent.getSource() instanceof DrawButton) {

            if (_isHumanPlayersTurn) {

                drawCard(_game.actualPlayer);

                _isHumanPlayersTurn = false;

                repaint = true;
                // spielt eine Runde mittels SwingWorker
                new PlayRoundWorker(this).execute();

            } else {

                for (Player humanPlayer : _game.players) {

                    if (humanPlayer instanceof RealPlayer) {

                        drawCard(humanPlayer);

                        _mainFrame.repaintPlayerCards(humanPlayer.cardsOnHand);
                        _mainFrame.setVisible(true);
                        repaint = false;
                    }
                }
            }
        }

        // Karte legen
        else if (mouseEvent.getSource() instanceof PlayerCardButton) {

            PlayerCardButton playerCardButton = (PlayerCardButton) mouseEvent.getSource();
            UnoCard unoCard = playerCardButton.get_unoCard();

//            if (_game.actualPlayer instanceof RealPlayer) {
                if (realPlay(unoCard, (RealPlayer) _game.actualPlayer)) {

                    _isHumanPlayersTurn = false;
                    repaint = true;

                    // Eine Runde mittels SwingWorker spielen
                    new PlayRoundWorker(this).execute();
                }
//            }
            // Zwischenwerfen einer Karte, wenn man nicht dran ist
//            else {
//
//                for (Player humanPlayer : _game.players) {
//
//                    if (humanPlayer instanceof RealPlayer) {
//
//                        if (realPlay(unoCard, (RealPlayer) humanPlayer)) {
//
//                            _mainFrame.repaintPlayerCards(humanPlayer.cardsOnHand);
//                            _mainFrame.setVisible(true);
//                            repaint = false;
//                        }
//                    }
//                }
//            }
        }
            if (repaint) {

                _mainFrame.repaintPlayerCards(_game.actualPlayer.cardsOnHand);
                _mainFrame.setVisible(true);
            }
        }

        /**
         * Auspielen einer Karte durch einen realen Spieler
         *
         * @param cardToPlay Karte die ausgespielt werden soll
         * @param rPlayer    Spieler Objekt des menschlichen Spielers
         * @return Gibt an ob der Zug gelunge ist. Also ob die Karte auf der Hand ist und auf die liegende passt
         */

    private boolean realPlay(UnoCard cardToPlay, RealPlayer rPlayer) {

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
            if (_game.actualCard.fits(cardToPlay)) {
                System.out.println("Karte passt");
                _game.actualCard = cardToPlay;
                _mainFrame.refreshStack(_game.actualCard);
                _game.cardStack.add(_game.actualCard);
                rPlayer.cardsOnHand.remove(cardToPlay);

                System.out.println("Nach Kartenlegen: " + _game.actualPlayer.cardsOnHand.size() + " Karten");
                for (UnoCard c : _game.actualPlayer.cardsOnHand) {
                    System.out.println(c.get_color() + ", " + c.get_number());
                }
                System.out.println("Noch " + _game.cardDeck.size() + " Karten auf Deck");
                System.out.println("Bereits " + _game.cardStack.size() + " Karten auf Haufen");
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
    private void virtualPlay(final VirtualPlayer vPlayer) {

        boolean cardFits = false;
        for (final UnoCard c : vPlayer.cardsOnHand) {
            if (_game.actualCard.fits(c)) {
                _game.actualCard = c;

                final String protocol = vPlayer.get_name() + " hat " + vPlayer.cardsOnHand.size() + " Karten.\n"
                        + " spielt: " + c.get_color() + ", " + c.get_number();

                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {

                        _mainFrame.refreshStack(_game.actualCard);

                        _mainFrame.writeToProtocol(protocol);
                    }
                };

                SwingUtilities.invokeLater(runnable);

                _game.cardStack.add(_game.actualCard);
                vPlayer.cardsOnHand.remove(_game.actualCard);
                cardFits = true;
                System.out.println(vPlayer.get_name() + " spielt: " + c.get_color() + ", " + c.get_number());
                break;
            }
        }
        if (cardFits == false) {
            drawCard(vPlayer);
        }
    }

    /**
     * Karte ziehen
     */
    private void drawCard(Player drawingPlayer) {
        if (_game.cardDeck.size() < 1) {
            for (int i = 0; i < _game.cardStack.size(); i++) {
                _game.cardDeck.add(_game.cardStack.get(i));
            }
            _game.cardStack.clear();
            Collections.shuffle(_game.cardDeck);
        }

        final String protocol = drawingPlayer.get_name() + " hat gezogen";

        // hier eine coole Syntax für das schreiben eines runnable, die intelliJ vorgeschlagen hat
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                _mainFrame.writeToProtocol(protocol);
            }
        };

        SwingUtilities.invokeLater(runnable);

        UnoCard drawedCard = _game.cardDeck.get(0);
        drawingPlayer.cardsOnHand.add(drawedCard);
        _game.cardDeck.remove(drawedCard);
    }

    /**
     * Spielen einer Runde, wenn der menschliche Spieler dran ist wird aus der Methode gesprungen
     */
    public void playRound() {
        // Spiele eine Runde

        for (int position = 0; position < _game.players.size(); position++) {

            _game.actualPlayer = _game.players.get(position);
            System.out.print("Liegende Karte: ");
            System.out.println(_game.actualCard.get_color() + ", " + _game.actualCard.get_number());
            System.out.print("Spieler: ");
            System.out.println(_game.actualPlayer.get_name());

            final String protocol = _game.actualPlayer.get_name() + " ist am Zug";

            try {
                SwingUtilities.invokeAndWait(new Runnable() {
                    @Override
                    public void run() {
                        _mainFrame.writeToProtocol(protocol);
                    }
                });
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }

            // Pause, wenn bei den nicht-menschlichen Spielern
            if (_game.actualPlayer instanceof VirtualPlayer){

                insertDelay(1500);
            }

            if (_game.actualPlayer.cardsOnHand.size() < 2 && _game.actualPlayer.cardsOnHand.size() > 0) {
                System.out.println("UNO!!!!");
            }
            if (_game.actualPlayer.cardsOnHand.size() < 1) {
                System.out.println("GEWONNEN!!!!!!!!!");
                String message = _game.actualPlayer.get_name();
                message += " hat gewonnen!";
                _mainFrame.Message(message);
                return;
            }
            System.out.println("Vor Kartenlegen: " + _game.actualPlayer.cardsOnHand.size() + " Karten");
            for (UnoCard c : _game.actualPlayer.cardsOnHand) {
                System.out.println(c.get_color() + ", " + c.get_number());
            }

            if (_game.actualPlayer instanceof VirtualPlayer) {

                virtualPlay((VirtualPlayer) _game.actualPlayer);
            }

            // bricht hier ab wenn es ein echter Spieler ist und wartet auf das Event
            else if (_game.actualPlayer instanceof RealPlayer) {

                _isHumanPlayersTurn = true;
                return;
            }

            System.out.println("Nach Kartenlegen: " + _game.actualPlayer.cardsOnHand.size() + " Karten");
            for (UnoCard c : _game.actualPlayer.cardsOnHand) {
                System.out.println(c.get_color() + ", " + c.get_number());
            }
            System.out.println("Noch " + _game.cardDeck.size() + " Karten auf Deck");
            System.out.println("Bereits " + _game.cardStack.size() + " Karten auf Haufen");
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

    public void CreateCards() {
        // zwei Sätze Karten werden erstellt mit je 4 Farben und Zahlen 0 - 9
        for (int h = 0; h < 2; h++) {
            for (int i = 1; i < 10; i++) {
                _game.cardDeck.add(new UnoCard(i, "blue"));
            }
            for (int i = 1; i < 10; i++) {
                _game.cardDeck.add(new UnoCard(i, "red"));
            }
            for (int i = 1; i < 10; i++) {
                _game.cardDeck.add(new UnoCard(i, "green"));
            }
            for (int i = 1; i < 10; i++) {
                _game.cardDeck.add(new UnoCard(i, "yellow"));
            }
        }
        _game.cardDeck.add(new UnoCard(0, "blue"));
        _game.cardDeck.add(new UnoCard(0, "red"));
        _game.cardDeck.add(new UnoCard(0, "green"));
        _game.cardDeck.add(new UnoCard(0, "yellow"));
    }

    public void uncoverFirstCard() {
        _game.actualCard = _game.cardDeck.get(0);
        _game.cardStack.add(_game.cardDeck.get(0));
        _game.cardDeck.remove(0);
        _mainFrame.refreshStack(_game.actualCard);
    }

    public void DealCards() {
        for (Player p : _game.players) {
            for (int i = 0; i < 7; i++) {
                p.cardsOnHand.add(_game.cardDeck.get(0));
                _game.cardDeck.remove(0);
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseReleased(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseEntered(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseExited(MouseEvent mouseEvent) {

    }

}
