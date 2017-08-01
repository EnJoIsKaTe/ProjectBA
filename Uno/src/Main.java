import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {
    public static void main(String[] args) throws IOException {

        // Spiel wird gestartet
//        Game.CreatePlayers();

        // Fenster wird erzeugt
        JFrame main = new MainFrame();

        Player paul = new VirtualPlayer("Paul");
        Player karl = new VirtualPlayer("Egon");
        Player ich = new RealPlayer("Ich");

        Game g = new Game();
        g.addPlayer(paul);
        g.addPlayer(karl);
        g.addPlayer(ich);

        g.CreateCards();
        g.Shuffle(Game.cardDeck);
        g.DealCards(Game.players);
        MainFrame.initialPlayerCards(ich.cardsOnHand);

        // Erste Karte aufdecken
        g.uncoverFirstCard();

        InputStreamReader isr = new InputStreamReader(System.in);
        BufferedReader br = new BufferedReader(isr);
        String eingabe = "";
        int count = 1;
        while(!eingabe.equals("n")){
            System.out.println("RUNDE: " + count);
            count++;
            g.playRound(g.players);
            System.out.println("Weiter? (ja = Enter; nein = n)");
            eingabe = br.readLine();
        }
    }
}
