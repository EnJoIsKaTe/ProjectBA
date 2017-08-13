import javax.swing.*;


public class PlayRoundWorker extends SwingWorker<Void, Object> {

    private Controller _controller;

    public PlayRoundWorker(Controller controller) {

        _controller = controller;
    }

    /**
     * Führt die Methode playRound aus
     *
     * @return
     */
    @Override
    protected Void doInBackground() {

        _controller.playRound();

        return null;
    }
}
