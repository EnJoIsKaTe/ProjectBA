import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.*;

public class MainFrame extends JFrame {
    Dimension mainSize = new Dimension(400, 400);
    ImageIcon deckIcon;
    static JLabel stackLabel;
    static JPanel bottomPanel;

    public MainFrame() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
//        setLayout(new GridLayout(2,1,10,10));
        setLayout(new BorderLayout(10, 10));
        JPanel mainPanel = new JPanel(new GridLayout(2, 1, 10, 10));
        add(mainPanel, BorderLayout.CENTER);

        setTitle("Uno");
        setPreferredSize(mainSize);
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new GridLayout(1, 3, 10, 10));
        bottomPanel = new JPanel();
//        bottomPanel.setBackground(Color.DARK_GRAY);
        mainPanel.add(topPanel);
        mainPanel.add(bottomPanel);
        JPanel ausgabePanel = new JPanel();
        ausgabePanel.setBackground(Color.BLUE);
//        JPanel stackPanel = new JPanel();
        stackLabel = new JLabel("leer");

        /**
         * Wird deckButton gedrückt, wird eine Karte gezogen
         */
        JButton deckButton = new JButton();
        deckIcon = new ImageIcon(getClass().getResource("images/Back1.png"));
        Image img = deckIcon.getImage();
        Image newimg = img.getScaledInstance(50, 100,
                java.awt.Image.SCALE_SMOOTH);
        deckIcon = new ImageIcon(newimg);
        deckButton.setIcon(deckIcon);
        deckButton.addComponentListener(new ComponentListener() {
            @Override
            public void componentResized(ComponentEvent e) {
                Image img = deckIcon.getImage();
                Image newimg = img.getScaledInstance(deckButton.getWidth(), deckButton.getHeight(),
                        java.awt.Image.SCALE_SMOOTH);
                deckIcon = new ImageIcon(newimg);
                deckButton.setIcon(deckIcon);
            }

            @Override
            public void componentMoved(ComponentEvent e) {

            }

            @Override
            public void componentShown(ComponentEvent e) {

            }

            @Override
            public void componentHidden(ComponentEvent e) {

            }
        });
        deckButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Game.actualPlayer.drawCard();
            }
        });

//        stackPanel.add(stackButton);
//        stackPanel.setBackground(Color.GREEN);
//        JPanel deckPanel = new JPanel();
//        deckPanel.setBackground(Color.YELLOW);
        topPanel.add(ausgabePanel);
//        topPanel.add(stackPanel);
        topPanel.add(stackLabel);
        topPanel.add(deckButton);
        pack();
        setVisible(true);
    }

    public static void refreshStack(UnoCard topCard){
        stackLabel.setText(topCard.get_color() + ", " + topCard.get_number());
    }

    public static void initialPlayerCards(ArrayList<UnoCard> playerCards){
        bottomPanel.setLayout(new GridLayout(1,playerCards.size()));
        for(UnoCard c : playerCards){
            JButton playerCard = new JButton();
            playerCard.setText(c.get_color() + ", " + c.get_number());
            bottomPanel.add(playerCard);
        }
    }
}
