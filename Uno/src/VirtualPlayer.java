

public class VirtualPlayer extends Player {


    public VirtualPlayer(String _name) {
        super(_name);
    }

    @Override
    public void play() {
        boolean cardFits = false;
        for (UnoCard c : cardsOnHand) {
            if (Game.actualCard.fits(c)) {
                Game.actualCard = c;
                MainFrame.refreshStack(Game.actualCard);
                Game.cardStack.add(Game.actualCard);
                cardsOnHand.remove(Game.actualCard);
                cardFits = true;
                break;
            }
        }
        if (cardFits == false) {
            drawCard();
        }
    }
}
