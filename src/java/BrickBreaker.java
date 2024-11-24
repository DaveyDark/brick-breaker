
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

public class BrickBreaker extends JPanel implements KeyListener, Ball.BallListener {

  private Level level;
  private Player player;
  private Ball ball;

  private int score;
  private boolean gameOver;

  public BrickBreaker(String levelFile) {
    level = new Level(levelFile);
    player = new Player(640);
    ball = new Ball(player);
    ball.setListener(this);
    score = 0;
    gameOver = false;

    addKeyListener(this);
    setFocusable(true);
    setFocusTraversalKeysEnabled(false);

    Timer timer = new Timer(16, e -> {
      gameUpdate();
      repaint();
    });
    timer.start();
  }

  @Override
  public void paint(Graphics g) {
    super.paint(g);
    drawLevel(g);
    player.draw(g);
    ball.draw(g);
    if (!gameOver) {
      drawLevel(g);
      player.draw(g);
      ball.draw(g);

      g.setColor(Color.BLACK);
      g.setFont(new Font("Arial", Font.BOLD, 14));
      g.drawString("Score: " + score, 10, 470);
    } else {
      g.setColor(Color.BLACK);
      g.setFont(new Font("Arial", Font.BOLD, 36));
      FontMetrics fm = g.getFontMetrics();
      String gameOverText = "GAME OVER";
      int x = (getWidth() - fm.stringWidth(gameOverText)) / 2;
      int y = getHeight() / 2 - fm.getHeight();
      g.drawString(gameOverText, x, y);

      g.setFont(new Font("Arial", Font.PLAIN, 24));
      String scoreText = "Final Score: " + score;
      fm = g.getFontMetrics();
      x = (getWidth() - fm.stringWidth(scoreText)) / 2;
      y += fm.getHeight() + 20;
      g.drawString(scoreText, x, y);
    }
  }

  @Override
  public void ballLost() {
    score -= 30;
  }

  private void drawLevel(Graphics g) {
    Brick[][] bricks = level.getBricks();
    for (int i = 0; i < 12; i++) {
      for (int j = 0; j < 24; j++) {
        Brick brick = bricks[i][j];
        if (brick != null) {
          brick.draw(g);
        }
      }
    }
  }

  private void gameUpdate() {
    if (!gameOver) {
      player.update();
      ball.update();
      checkCollisions();
    }
  }

  private void checkCollisions() {
    if (ball.getBounds().intersects(player.getBounds())) {
      ball.adjustDirection(player.getX());
    }

    Brick[][] bricks = level.getBricks();
    Rectangle ballBounds = ball.getBounds();

    boolean collisionDetected = false;

    outerLoop: for (int i = 0; i < 12; i++) {
      for (int j = 0; j < 24; j++) {
        Brick brick = bricks[i][j];
        if (brick != null && ballBounds.intersects(brick.getBounds())) {
          if (!collisionDetected) {
            collisionDetected = true;

            if (ball.getY() + ball.getDiameter() - ball.getDy() <= brick.getY()
                || ball.getY() - ball.getDy() >= brick.getY() + brick.getHeight()) {
              ball.reverseDirectionY();
            } else {
              ball.reverseDirectionX();
            }
          }

          brick.hit();
          if (brick.isDestroyed()) {
            bricks[i][j] = null;
            score += 5;
          } else {
            score += 1;
          }

          break outerLoop;
        }
      }
    }

    if (isAllBricksDestroyed()) {
      gameOver = true;
      repaint();
    }
  }

  private boolean isAllBricksDestroyed() {
    Brick[][] bricks = level.getBricks();
    for (int i = 0; i < 12; i++) {
      for (int j = 0; j < 24; j++) {
        if (bricks[i][j] != null) {
          return false;
        }
      }
    }
    return true;
  }

  @Override
  public void keyPressed(KeyEvent e) {
    int key = e.getKeyCode();

    if (gameOver) {
      System.exit(0);
    } else {
      if (!ball.isMoving()) {
        ball.start();
      }

      if (key == KeyEvent.VK_LEFT) {
        player.moveLeft(true);
      }

      if (key == KeyEvent.VK_RIGHT) {
        player.moveRight(true);
      }
    }
  }

  @Override
  public void keyReleased(KeyEvent e) {
    int key = e.getKeyCode();

    if (key == KeyEvent.VK_LEFT) {
      player.moveLeft(false);
    }

    if (key == KeyEvent.VK_RIGHT) {
      player.moveRight(false);
    }
  }

  @Override
  public void keyTyped(KeyEvent e) {
    // Not used
  }

  public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> {
      String levelFile = "level1.txt";

      if (args.length > 0) {
        levelFile = args[0];
      }

      JFrame frame = new JFrame("Brick Breaker");
      BrickBreaker game = new BrickBreaker(levelFile);
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.setResizable(false);
      frame.add(game);
      frame.pack();
      frame.setLocationRelativeTo(null);
      frame.setVisible(true);
      frame.setTitle("Brick Breaker");
      frame.setSize(640, 480);
      frame.setResizable(false);
    });
  }

}
