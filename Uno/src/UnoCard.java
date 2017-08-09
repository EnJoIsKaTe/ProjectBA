/**
 * Card to play
 */
public class UnoCard {

    private int _number;
    private String _color;

    public int get_number() {
        return _number;
    }


    public String get_color() {
        return _color;
    }


    public UnoCard(int _number, String _color) {
        this._number = _number;
        this._color = _color;
    }

    public boolean equals(UnoCard card) {
        if (card.get_number() == this.get_number() && card.get_color().equals(this.get_color())) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Gibt an ob die Karte passt
     * @param card
     * @return
     */
    public boolean fits(UnoCard card){
        if (this.get_number() == card.get_number() || this.get_color() == card.get_color()) {
            return true;
        } else {
            return false;
        }
    }
}
