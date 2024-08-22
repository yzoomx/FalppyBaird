import javax.swing.*;

public class App {
    public static void main(String[] args) {
        int boardwith = 360;
        int boardhith = 640;

        JFrame frame = new JFrame("Flappy Bird");
        // frame.setVisible(true);
        frame.setSize(boardwith, boardhith);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        FlippyBird flippyBird = new FlippyBird();
        frame.add(flippyBird);
        frame.pack();
        flippyBird.requestFocus();
        frame.setVisible(true);
    }
}
