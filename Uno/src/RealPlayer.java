import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class RealPlayer extends Player {

    private String _colorInput = "";

    public RealPlayer(String name) {
        super(name);
    }

    @Override
    public void play() {
        InputStreamReader isr = new InputStreamReader(System.in);
        BufferedReader br = new BufferedReader(isr);
        UnoCard cardToPlay = new UnoCard();

        System.out.println("Welche Karte spielen? (Enter zum Ziehen)");
        System.out.print("Farbe: ");
        try {
            _colorInput = br.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(_colorInput.equals("")){
            drawCard();
            return;
        }
        else {
            cardToPlay.set_color(_colorInput);
        }

        System.out.print("Nummer: ");
        try {
            cardToPlay.set_number(Integer.parseInt(br.readLine()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Zu spielende Karte: " + cardToPlay.get_color() + ", " + cardToPlay.get_number());

        boolean isOnHand = false;
        for(UnoCard c : cardsOnHand){
            if(cardToPlay.equals(c)){
                cardToPlay = c;
                isOnHand = true;
            }
        }

        if(isOnHand){
            System.out.println("Auf Hand");
            if(Game.actualCard.fits(cardToPlay)){
                System.out.println("Karte passt");
                Game.actualCard = cardToPlay;
                MainFrame.refreshStack(Game.actualCard);
                Game.cardStack.add(Game.actualCard);
                cardsOnHand.remove(cardToPlay);
            }
            else{
                drawCard();
            }
        }
        else{
            System.out.println("Karte nicht auf der Hand!");
            play();
        }
    }
}
