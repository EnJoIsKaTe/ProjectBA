import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * MainFrame class of the GUI
 */

public class MainFrame extends JFrame {
    Dimension mainSize = new Dimension(400, 400);
    ImageIcon deckIcon;
    static JLabel stackLabel;
    static JPanel bottomPanel;
    static JPanel topPanel;
    static JPanel mainPanel;

    public JPanel outputPanel;
    static Controller controller;

    /**
     * Standard Konstruktor des MainFrame
     * Baut die GUI auf und verbindet sie mit dem Controller
     *
     * @param controller Controller Objekt, der zur Steuerung verwendet werden soll
     */
    public MainFrame(Controller controller) {

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
//        setLayout(new GridLayout(2,1,10,10));
        setLayout(new BorderLayout(10, 10));
        mainPanel = new JPanel(new GridLayout(2, 1, 10, 10));

        add(mainPanel, BorderLayout.CENTER);

        setTitle("Uno");
        setPreferredSize(mainSize);
        topPanel = new JPanel();

        topPanel.setLayout(new GridLayout(1, 3, 10, 10));
        bottomPanel = new JPanel();
//        bottomPanel.setBackground(Color.DARK_GRAY);
        mainPanel.add(topPanel);
        mainPanel.add(bottomPanel);
        outputPanel = new JPanel();
        outputPanel.setBackground(Color.BLUE);
//        JPanel stackPanel = new JPanel();
        stackLabel = new JLabel("leer");

        this.controller = controller;

        /**
         * Wird deckButton gedrückt, wird eine Karte gezogen
         */
        //final JButton deckButton = new JButton();
        DrawButton deckButton = new DrawButton();

        deckIcon = new ImageIcon(getClass().getResource("images/Back1.png"));
        Image img = deckIcon.getImage();
        Image newimg = img.getScaledInstance(50, 100,
                java.awt.Image.SCALE_SMOOTH);
        deckIcon = new ImageIcon(newimg);
        deckButton.setIcon(deckIcon);

        deckButton.addMouseListener(controller);

//        stackPanel.add(stackButton);
//        stackPanel.setBackground(Color.GREEN);
//        JPanel deckPanel = new JPanel();
//        deckPanel.setBackground(Color.YELLOW);
        topPanel.add(outputPanel);
//        topPanel.add(stackPanel);
        topPanel.add(stackLabel);
        topPanel.add(deckButton);
//        pack();


        // get the screen size as a java dimension
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

//      get 2/3 of the height, and 2/3 of the width
        int height = screenSize.height * 2 / 3;
        int width = screenSize.width * 1 / 2;

//      set the jframe height and width
        this.setMinimumSize(new Dimension(width, height));

        setVisible(true);
    }

    /**
     * Updated den Kartenstack und gibt an welche Karte oben liegt
     *
     * @param topCard Liegende oberste Karte
     */
    public void refreshStack(UnoCard topCard) {

        stackLabel.setText(topCard.get_color() + ", " + topCard.get_number());
        setColorOfUpperCard(topCard);
    }

    /**
     * Repaints the GUI area where that represents the players hand / cards
     *
     * @param playerCards ArrayList of Cards the player is holding on his hand
     */
    public void repaintPlayerCards(ArrayList<UnoCard> playerCards) {
        bottomPanel.removeAll();
        bottomPanel.setLayout(new GridLayout(1, playerCards.size()));

        for (UnoCard c : playerCards) {
            PlayerCardButton playerCard = new PlayerCardButton(c);
            playerCard.addMouseListener(controller);
            playerCard.setText(c.get_color() + ", " + c.get_number());
            bottomPanel.add(playerCard);
        }
        bottomPanel.repaint();
    }

    public void Message(String message) {
        this.stackLabel.setText(message);
    }

    /**
     * Passt die Farbe des Feldes in der Oberfläche der Farbe der liegenden Karte an.
     *
     * @param unoCard Karte an deren Farbe das Feld in der Oberfläche angepasst werden soll
     */
    private void setColorOfUpperCard(UnoCard unoCard) {

        Color color;
        try {
            Field field = Class.forName("java.awt.Color").getField(unoCard.get_color());
            color = (Color) field.get(null);
        } catch (Exception e) {
            color = null; // Not defined
            e.printStackTrace();
        }

        outputPanel.setBackground(color);
        outputPanel.repaint();
        outputPanel.setVisible(true);
    }
}
