import javax.swing.*;

/**
 * Button that is used to represent and play a card
 */
public class PlayerCardButton extends JButton {

    private UnoCard _unoCard;

    public UnoCard get_unoCard() {
        return _unoCard;
    }

    public PlayerCardButton(UnoCard unoCard){

        _unoCard = unoCard;
    }
}
