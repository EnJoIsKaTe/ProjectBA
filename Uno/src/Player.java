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
     * Spieler zieht Karte
     */

}
