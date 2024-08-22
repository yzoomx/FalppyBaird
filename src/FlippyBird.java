import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

public class FlippyBird extends JPanel implements ActionListener, KeyListener {
    int boardWidth = 360;
    int boardHeight = 640;

    Image backgroundImage;
    Image birdImg;
    Image topPipeImg;
    Image bottomPipeImg;

    int birdX = boardWidth / 8;
    int birdY = boardHeight / 2;
    int biedwith = 34;
    int birdhith = 24;

    class Bird {
        int x = birdX;
        int y = birdY;
        int with = biedwith;
        int hith = birdhith;
        Image img;

        Bird(Image img) {
            this.img = img;
        }
    }

    // Pipes
    int pipeX = boardWidth;
    int pipeY = 0;
    int pipewith = 64; // scaled by 1/6
    int piphith = 512;

    class Pipe {
        int x = pipeX;
        int y = pipeY;
        int with = pipewith;
        int hith = piphith;
        Image img;
        boolean passed = false;

        Pipe(Image img) {
            this.img = img;
        }
    }

    // Game Logic
    Bird bird;
    int velocityX = -4;
    int velocityY = 0;
    int gravityY = 1;

    ArrayList<Pipe> pipes;
    Random random = new Random();

    // Game loop
    Timer gameLoop;
    Timer placePipesTimer;
    boolean gameOver = false;
    double score = 0;

    FlippyBird() {
        setPreferredSize(new Dimension(boardWidth, boardHeight));

        setFocusable(true);
        addKeyListener(this);

        backgroundImage = new ImageIcon(getClass().getResource("./flappybirdbg.png")).getImage();
        birdImg = new ImageIcon(getClass().getResource("./flappybird.png")).getImage();
        topPipeImg = new ImageIcon(getClass().getResource("./toppipe.png")).getImage();
        bottomPipeImg = new ImageIcon(getClass().getResource("./bottompipe.png")).getImage();

        bird = new Bird(birdImg);
        pipes = new ArrayList<Pipe>();

        placePipesTimer = new Timer(1500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                placePipes();
            }
        });

        placePipesTimer.start();
        // Game timer
        gameLoop = new Timer(1000 / 60, this); // 1000/60 = 16.6
        gameLoop.start();
    }

    public void placePipes() {
        int randomPipeY = (int) (pipeY - piphith / 4 - Math.random() * (piphith / 2));
        int opeingSpace = boardHeight / 4;
        Pipe topPipe = new Pipe(topPipeImg);
        topPipe.y = randomPipeY;
        pipes.add(topPipe);

        Pipe bottPipe = new Pipe(bottomPipeImg);
        bottPipe.y = topPipe.y + piphith + opeingSpace;
        pipes.add(bottPipe);
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
        g.drawImage(backgroundImage, 0, 0, boardWidth, boardHeight, null);
        g.drawImage(bird.img, bird.x, bird.y, bird.with, bird.hith, null);

        for (int i = 0; i < pipes.size(); i++) {
            Pipe pipe = pipes.get(i);
            g.drawImage(pipe.img, pipe.x, pipe.y, pipe.with, pipe.hith, null);
        }
        g.setColor(Color.white);
        g.setFont(new Font("Arial", Font.PLAIN, 32));
        if (gameOver) {
            g.drawString("Game Over: " + String.valueOf((int) score), 10, 35);
        } else {
            g.drawString(String.valueOf((int) score), 10, 35);
        }
    }

    public void move() {
        velocityY += gravityY;
        bird.y += velocityY;
        bird.y = Math.max(bird.y, 0);

        // pipes

        for (int i = 0; i < pipes.size(); i++) {
            Pipe pipe = pipes.get(i);
            pipe.x += velocityX;

            if (!pipe.passed && bird.x > pipe.x + pipe.with) {
                pipe.passed = true;
                score += 0.5;
            }

            if (collision(bird, pipe)) {
                gameOver = true;
            }
        }

        if (bird.y > boardHeight) {
            gameOver = true;
        }
    }

    public boolean collision(Bird a, Pipe b) {
        return a.x < b.x + b.with && // a's top left corner doesn't reach b •s top right corner
                a.x + a.with > b.x && // a's top right corner passes b •s top left corner
                a.y < b.y + b.hith && // a's top left corner doesn't reach b •s bottom left corner
                a.y + a.hith > b.y; // a's bottom left corner passes b's top left corner

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();
        if (gameOver) {
            placePipesTimer.stop();
            gameLoop.stop();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE)
            ;
        velocityY = -9;
        if (gameOver) {
            // restart the game by resetting the conditons
            bird.y = birdY;
            velocityY = 0;
            pipes.clear();
            gameOver = false;
            gameLoop.start();
            placePipesTimer.start();
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
}
