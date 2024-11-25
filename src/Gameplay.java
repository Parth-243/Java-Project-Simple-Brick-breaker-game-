import javax.swing.JPanel;
import javax.swing.Timer;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Gameplay extends JPanel implements KeyListener, ActionListener {
  public static final int FRAME_WIDTH = 700;
  public static final int FRAME_HEIGHT = 700;
  public static final int BALL_SIZE = 20;
  public static final int PADDLE_WIDTH = 100;
  public static final int PADDLE_HEIGHT = 8;
  public static final int PADDLE_Y_POSITION = 630;
  public static final int INITIAL_BRICKS = 21;
  public static final int DELAY = 5;

  private boolean play = false;
  private int score = 0;
  private int totalBricks = INITIAL_BRICKS;

  private Timer timer;
  private int playerX = 310;
  private int ballPosX = 120;
  private int ballPosY = 320;
  private int ballXDir = -1;
  private int ballYDir = -2;

  private MapGenerator map;

  public Gameplay() {
    map = new MapGenerator(3, 7);
    addKeyListener(this);
    setFocusable(true);
    setFocusTraversalKeysEnabled(false);
    timer = new Timer(DELAY, this);
    timer.start();
  }

  public void paint(Graphics g) {
    // Background
    g.setColor(Color.BLACK);
    g.fillRect(0, 0, FRAME_WIDTH, FRAME_HEIGHT);

    // Draw Map
    map.draw((Graphics2D) g);

    // Borders
    g.setColor(Color.GREEN);
    g.fillRect(0, 0, 3, FRAME_HEIGHT); // Left border
    g.fillRect(0, 0, FRAME_WIDTH, 3); // Top border
    g.fillRect(FRAME_WIDTH - 3, 0, 3, FRAME_HEIGHT); // Right border

    // Paddle
    g.setColor(Color.RED);
    g.fillRect(playerX, PADDLE_Y_POSITION, PADDLE_WIDTH, PADDLE_HEIGHT);

    // Ball
    g.setColor(Color.WHITE);
    g.fillOval(ballPosX, ballPosY, BALL_SIZE, BALL_SIZE);

    // Score
    g.setColor(Color.WHITE);
    g.setFont(new Font("serif", Font.BOLD, 25));
    g.drawString("Score: " + score, FRAME_WIDTH - 150, 30);

    // Game Over
    if (ballPosY > FRAME_HEIGHT - 50) {
      play = false;
      ballXDir = 0;
      ballYDir = 0;
      g.setColor(Color.RED);
      g.setFont(new Font("serif", Font.BOLD, 30));
      g.drawString("Game Over, Scores: " + score, 190, 300);
      g.drawString("Press Enter to Restart", 230, 350);
    }

    // Game Won
    if (totalBricks <= 0) {
      play = false;
      ballXDir = 0;
      ballYDir = 0;
      g.setColor(Color.GREEN);
      g.setFont(new Font("serif", Font.BOLD, 30));
      g.drawString("You Won! Scores: " + score, 190, 300);
      g.drawString("Press Enter to Restart", 230, 350);
    }

    g.dispose();
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    timer.start();
    if (play) {
      // Ball-Paddle Collision
      if (new Rectangle(ballPosX, ballPosY, BALL_SIZE, BALL_SIZE)
          .intersects(new Rectangle(playerX, PADDLE_Y_POSITION, PADDLE_WIDTH, PADDLE_HEIGHT))) {
        ballYDir = -ballYDir;
      }

      // Ball-Brick Collision
      A: for (int i = 0; i < map.map.length; i++) {
        for (int j = 0; j < map.map[0].length; j++) {
          if (map.map[i][j] > 0) {
            int brickX = j * map.brickWidth + 80;
            int brickY = i * map.brickHeight + 50;
            Rectangle brickRect = new Rectangle(brickX, brickY, map.brickWidth, map.brickHeight);
            Rectangle ballRect = new Rectangle(ballPosX, ballPosY, BALL_SIZE, BALL_SIZE);

            if (ballRect.intersects(brickRect)) {
              map.setBrickValue(0, i, j);
              totalBricks--;
              score += 5;

              if (ballPosX + BALL_SIZE - 1 <= brickRect.x || ballPosX + 1 >= brickRect.x + brickRect.width) {
                ballXDir = -ballXDir;
              } else {
                ballYDir = -ballYDir;
              }
              break A;
            }
          }
        }
      }

      // Ball Movement
      ballPosX += ballXDir;
      ballPosY += ballYDir;

      // Border Collision
      if (ballPosX < 0 || ballPosX > FRAME_WIDTH - BALL_SIZE)
        ballXDir = -ballXDir;
      if (ballPosY < 0)
        ballYDir = -ballYDir;
    }

    repaint();
  }

  @Override
  public void keyPressed(KeyEvent e) {
    if (e.getKeyCode() == KeyEvent.VK_RIGHT && playerX < FRAME_WIDTH - PADDLE_WIDTH - 20)
      moveRight();
    if (e.getKeyCode() == KeyEvent.VK_LEFT && playerX > 10)
      moveLeft();
    if (e.getKeyCode() == KeyEvent.VK_ENTER && !play)
      resetGame();
  }

  private void moveRight() {
    play = true;
    playerX += 20;
  }

  private void moveLeft() {
    play = true;
    playerX -= 20;
  }

  private void resetGame() {
    play = true;
    ballPosX = 120;
    ballPosY = 350;
    ballXDir = -1;
    ballYDir = -2;
    playerX = 310;
    score = 0;
    totalBricks = INITIAL_BRICKS;
    map = new MapGenerator(3, 7);
    repaint();
  }

  @Override
  public void keyTyped(KeyEvent e) {
  }

  @Override
  public void keyReleased(KeyEvent e) {
  }
}
