import javax.swing.*;

/**
 * Created by cs15bv1 on 03.08.17.
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
