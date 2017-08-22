import java.io.IOException;

public class Main {

    // Zum Testen von Push / Pull

    private static MainFrame _mainFrame;

    public static void main(String[] args) throws IOException {

        Player paul = new VirtualPlayer("Paul");
        Player karl = new VirtualPlayer("Egon");
        Player ich = new RealPlayer("Ich");

        Controller controller = new Controller();
        _mainFrame = new MainFrame(controller);

        Game game  = new Game(controller);
        controller.initController(game, _mainFrame);

        game.addPlayer(0, paul);
        game.addPlayer(1, karl);
        game.addPlayer(2, ich);

        controller.CreateCards();
        game.Shuffle();
        controller.DealCards();

        _mainFrame.repaintPlayerCards(ich.cardsOnHand);

        // Erste Karte aufdecken
        controller.uncoverFirstCard();

        controller.playRound();
    }
}
