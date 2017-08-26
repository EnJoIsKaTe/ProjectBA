import javax.swing.*;


public class PlayRoundWorker extends SwingWorker<Void, Object> {

    private Controller controller;

    public PlayRoundWorker(Controller controller) {

        this.controller = controller;
    }

    /**
     * Führt die Methode playRound aus
     *
     * @return
     */
    @Override
    protected Void doInBackground() {

        controller.game.playRound();

        return null;
    }
}
