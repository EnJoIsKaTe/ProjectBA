import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.lang.reflect.InvocationTargetException;
import java.util.Collections;

public class Controller implements MouseListener {

    private Game _game;
    private MainFrame _mainFrame;

    public void initController(Game game, MainFrame mainFrame){

        _game = game;
        _mainFrame = mainFrame;

    }

    @Override
    public void mouseClicked(MouseEvent mouseEvent) {

        // Unterscheiden ob Karte Ziehen oder Karte legen
        // Karte ziehen
        if (mouseEvent.getSource() instanceof DrawButton){
            drawCard();

            playRound();
        }

        // Karte legen
        if (mouseEvent.getSource() instanceof PlayerCardButton){

            PlayerCardButton playerCardButton = (PlayerCardButton)mouseEvent.getSource();
            UnoCard unoCard = playerCardButton.get_unoCard();

            if (_game.actualPlayer instanceof RealPlayer) {
                if (realPlay(unoCard, (RealPlayer)_game.actualPlayer)){
                    playRound();
                }
            }
        }
        _mainFrame.repaintPlayerCards(_game.actualPlayer.cardsOnHand);
        _mainFrame.setVisible(true);
    }

    /**
     * Auspielen einer Karte durch einen realen Spieler
     * @param cardToPlay Karte die ausgespielt werden soll
     * @param rPlayer Spieler Objekt des menschlichen Spielers
     * @return
     */
    public boolean realPlay(UnoCard cardToPlay, RealPlayer rPlayer){

        // wird true, wenn die Karte passt
        boolean validPlay = false;

        System.out.println("Zu spielende Karte: " + cardToPlay.get_color() + ", " + cardToPlay.get_number());

        boolean isOnHand = false;
        for(UnoCard c : rPlayer.cardsOnHand){
            if(cardToPlay.equals(c)){
                cardToPlay = c;
                isOnHand = true;
            }
        }

        if(isOnHand){
            System.out.println("Auf Hand");
            if(_game.actualCard.fits(cardToPlay)){
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
            }
            else{
                validPlay = false;
                System.out.println("Karte passt nicht. Bitte wählen Sie erneut oder Ziehen Sie eine Karte");
            }
        }
        return validPlay;
    }

    /**
     * Auspielen einer Karte durch einen virtuellen Spieler
     * @param vPlayer
     */
    public void virtualPlay(VirtualPlayer vPlayer) {

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                _mainFrame.refreshStack(_game.actualCard);

            }
        };

        boolean cardFits = false;
        for (UnoCard c : vPlayer.cardsOnHand) {
            if (_game.actualCard.fits(c)) {
                _game.actualCard = c;
                //_mainFrame.refreshStack(_game.actualCard);

                SwingUtilities.invokeLater(runnable);

                _game.cardStack.add(_game.actualCard);
                vPlayer.cardsOnHand.remove(_game.actualCard);
                cardFits = true;
                System.out.println(vPlayer.get_name() + " spielt: " + c.get_color() + ", " + c.get_number());
                break;
            }
        }
        if (cardFits == false) {
            drawCard();
        }
    }

    /**
     * Karte ziehen
     */
    public void drawCard(){
        if(_game.cardDeck.size() < 1)
        {
            for(int i = 0; i < _game.cardStack.size(); i++)
            {
                _game.cardDeck.add(_game.cardStack.get(i));
            }
            _game.cardStack.clear();
            Collections.shuffle(_game.cardDeck);
        }
        UnoCard drawedCard = _game.cardDeck.get(0);
        _game.actualPlayer.cardsOnHand.add(drawedCard);
        _game.cardDeck.remove(drawedCard);
    }

    /**
     * Spielen einer Runde, wenn der menschliche Spieler dran ist wird aus der Methode gesprungen
     */
    public void playRound(){
        // Spiele eine Runde
        for(int position = 0; position < _game.players.size(); position++) {
            _game.actualPlayer = _game.players.get(position);
            System.out.print("Liegende Karte: ");
            System.out.println(_game.actualCard.get_color() + ", " + _game.actualCard.get_number());
            System.out.print("Spieler: ");
            System.out.println(_game.actualPlayer.get_name());
            if(_game.actualPlayer.cardsOnHand.size() < 2 && _game.actualPlayer.cardsOnHand.size() > 0)
            {
                System.out.println("UNO!!!!");
            }
            if(_game.actualPlayer.cardsOnHand.size() < 1)
            {
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

            // Prüfe, ob Karte passt

            if (_game.actualPlayer instanceof VirtualPlayer)
            {
                virtualPlay((VirtualPlayer)_game.actualPlayer);
            }

            // bricht hier ab wenn es ein echter Spieler ist und wartet auf das Event
            else if (_game.actualPlayer instanceof RealPlayer){
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

    public void uncoverFirstCard(){
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
