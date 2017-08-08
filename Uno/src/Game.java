import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Game {

    public List<UnoCard> cardDeck;
    public List<Player> players;
    public UnoCard actualCard;
    public List<UnoCard> cardStack;
    public Player actualPlayer;

    private static Controller controller;


    public Game(Controller controller) {
        cardDeck = new ArrayList<UnoCard>();
        players = new ArrayList<Player>();
        cardStack = new ArrayList<UnoCard>();
        this.controller = controller;
    }

    public void addPlayer(int position, Player player) {
        players.add(position, player);
    }

    public void Shuffle() {
        Collections.shuffle(cardDeck);
    }
}

