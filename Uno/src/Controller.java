import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class Controller implements MouseListener {

    Game game;
    MainFrame mainFrame;


    public void initController() {

        game = new Game(this);
        mainFrame = new MainFrame(this);
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

            if (game.isHumanPlayersTurn) {

                game.drawCard(game.getActualPlayer());

                game.isHumanPlayersTurn = false;

                repaint = true;
                // spielt eine Runde mittels SwingWorker
                new PlayRoundWorker(this).execute();

            } else {

                for (Player humanPlayer : game.getPlayers()) {

                    if (humanPlayer instanceof RealPlayer) {

                        game.drawCard(humanPlayer);

                        mainFrame.repaintPlayerCards(humanPlayer.cardsOnHand);
                        mainFrame.setVisible(true);
                        repaint = false;
                    }
                }
            }
        }

        // Karte legen
        else if (mouseEvent.getSource() instanceof PlayerCardButton) {

            PlayerCardButton playerCardButton = (PlayerCardButton) mouseEvent.getSource();
            UnoCard unoCard = playerCardButton.get_unoCard();

//            if (game.actualPlayer instanceof RealPlayer) {
                if (game.realPlay(unoCard, (RealPlayer) game.getActualPlayer())) {

                    game.isHumanPlayersTurn = false;
                    repaint = true;

                    // Eine Runde mittels SwingWorker spielen
                    new PlayRoundWorker(this).execute();
                }
//            }
            // Zwischenwerfen einer Karte, wenn man nicht dran ist
//            else {
//
//                for (Player humanPlayer : game.players) {
//
//                    if (humanPlayer instanceof RealPlayer) {
//
//                        if (realPlay(unoCard, (RealPlayer) humanPlayer)) {
//
//                            mainFrame.repaintPlayerCards(humanPlayer.cardsOnHand);
//                            mainFrame.setVisible(true);
//                            repaint = false;
//                        }
//                    }
//                }
//            }
        }
            if (repaint) {

                mainFrame.repaintPlayerCards(game.getActualPlayer().cardsOnHand);
                mainFrame.setVisible(true);
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
