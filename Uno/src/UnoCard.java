import java.util.Collections;
import java.util.List;

public class UnoCard {

    private int _number;
    private String _color;

    public int get_number() {
        return _number;
    }

    public void set_number(int _number) {
        this._number = _number;
    }

    public String get_color() {
        return _color;
    }

    public void set_color(String _color) {
        this._color = _color;
    }

    public UnoCard() {
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

    public boolean fits(UnoCard card){
        if (this.get_number() == card.get_number() || this.get_color() == card.get_color()) {
            return true;
        } else {
            return false;
        }
    }
}
