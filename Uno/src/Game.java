import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Class the represents a Cardgame containing of the Cards and Players
 */
public class Game {

    public List<UnoCard> cardDeck;
    public List<Player> players;
    public UnoCard actualCard;
    public List<UnoCard> cardStack;
    public Player actualPlayer;

    private static Controller controller;

    /**
     * Standard Konstruktor
     * @param controller
     */
    public Game(Controller controller) {
        cardDeck = new ArrayList<UnoCard>();
        players = new ArrayList<Player>();
        cardStack = new ArrayList<UnoCard>();
        this.controller = controller;
    }

    /**
     * Hinzufügen eines neuen Spielers zum Spiel
     * @param position Position an der der Spieler eingefügt wird
     * @param player Spieler- Objekt
     */
    public void addPlayer(int position, Player player) {
        players.add(position, player);
    }

    /**
     * Mischen des Kartendecks des Spiels
     */
    public void Shuffle() {
        Collections.shuffle(cardDeck);
    }
}

