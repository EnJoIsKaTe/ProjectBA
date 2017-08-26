import java.io.IOException;

public class Main {

    private static MainFrame _mainFrame;

    public static void main(String[] args) throws IOException {

        Player paul = new VirtualPlayer("Paul");
        Player karl = new VirtualPlayer("Egon");
        Player ich = new RealPlayer("Ich");

        Controller controller = new Controller();
        controller.initController();

        controller.game.addPlayer(0, paul);
        controller.game.addPlayer(1, karl);
        controller.game.addPlayer(2, ich);

        controller.game.CreateCards();
        controller.game.Shuffle();
        controller.game.DealCards();

        controller.mainFrame.repaintPlayerCards(ich.cardsOnHand);

        controller.game.uncoverFirstCard();

        controller.game.playRound();
    }
}
