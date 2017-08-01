import java.util.ArrayList;
import java.util.Collections;

public class Player {

    private String _name;
    public ArrayList<UnoCard> cardsOnHand;

    public String get_name() {
        return _name;
    }

    public void set_name(String _name) {
        this._name = _name;
    }

    //Konstruktor
    public Player(String name) {
        this._name = name;
        cardsOnHand = new ArrayList<>();
    }

    /**
     * Prüft, ob Karte gelegt werden kann, wenn ja, wird sie gelegt
     */
    public void play(){
    }

    /**
     * Spieler zieht Karte
     */
    public void drawCard(){
        if(Game.cardDeck.size() < 1)
        {
            for(int i = 0; i < Game.cardStack.size(); i++)
            {
                Game.cardDeck.add(Game.cardStack.get(i));
            }
            Game.cardStack.clear();
            Collections.shuffle(Game.cardDeck);
        }
        UnoCard drawedCard = Game.cardDeck.get(0);
        cardsOnHand.add(drawedCard);
        Game.cardDeck.remove(drawedCard);
    }
}
