import java.util.ArrayList;

/**
 * Class that represents a Player
 */
public class Player {

    private String _name;
    public ArrayList<UnoCard> cardsOnHand;

    public String get_name() {
        return _name;
    }

    public void set_name(String _name) {
        this._name = _name;
    }

    /**
     * Standard Konstruktor
     * @param name Name of the Player
     */
    public Player(String name) {
        this._name = name;
        cardsOnHand = new ArrayList<>();
    }
}
