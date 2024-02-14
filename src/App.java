import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class App implements KeyListener {

    public static void main(String[] args) throws Exception {

        int Widthoftheboard = 600;
        int HeigthofBoard = 600;

        JFrame frame = new JFrame("Snake");
        frame.setVisible(true);
        frame.setSize(Widthoftheboard, HeigthofBoard);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        SnakeGame Gameofsnake = new SnakeGame(Widthoftheboard, HeigthofBoard);
        frame.add(Gameofsnake);
        Gameofsnake.requestFocus();
        frame.pack();


    }
    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
