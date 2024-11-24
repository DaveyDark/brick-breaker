
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;

public class Ball {

  private double x, y;
  private double dx, dy;
  private int diameter;
  private boolean moving;

  private Image image;

  private Player player;

  private double maxSpeed;

  public Ball(Player player) {
    this.player = player;
    this.diameter = 20;
    this.moving = false;
    this.maxSpeed = 5.0;

    resetPosition();

    loadImage();
  }

  private void loadImage() {
    try {
      image = ImageIO.read(getClass().getResourceAsStream("/ball.png"));
    } catch (IOException e) {
      e.printStackTrace();
    } catch (IllegalArgumentException e) {
      System.err.println("Ball image not found.");
    }
  }

  public void update() {
    if (!moving) {
      x = player.getX() + (player.getWidth() - diameter) / 2;
    } else {
      x += dx;
      y += dy;

      checkWallCollisions();
    }
  }

  private void checkWallCollisions() {
    if (x <= 0) {
      x = 0;
      dx = -dx;
    } else if (x + diameter >= 640) {
      x = 640 - diameter;
      dx = -dx;
    }

    if (y <= 0) {
      y = 0;
      dy = -dy;
    }

    if (y + diameter >= 480) {
      if (listener != null) {
        listener.ballLost();
      }
      resetPosition();
    }
  }

  public void draw(Graphics g) {
    if (image != null) {
      g.drawImage(image, (int) x, (int) y, diameter, diameter, null);
    } else {
      g.setColor(Color.RED);
      g.fillOval((int) x, (int) y, diameter, diameter);
    }
  }

  public Rectangle getBounds() {
    return new Rectangle((int) x, (int) y, diameter, diameter);
  }

  public void reverseDirectionY() {
    dy = -dy;
  }

  public void reverseDirectionX() {
    dx = -dx;
  }

  private void limitSpeed() {
    // Clamp dx
    if (dx > maxSpeed) {
      dx = maxSpeed;
    } else if (dx < -maxSpeed) {
      dx = -maxSpeed;
    }
    // Clamp dy
    if (dy > maxSpeed) {
      dy = maxSpeed;
    } else if (dy < -maxSpeed) {
      dy = -maxSpeed;
    }
  }

  public void adjustDirection(double paddleX) {
    double paddleCenter = paddleX + player.getWidth() / 2;
    double ballCenter = x + diameter / 2;
    double hitPosition = (ballCenter - paddleCenter) / (player.getWidth() / 2); // Normalize between -1 and 1

    hitPosition = Math.max(-1, Math.min(hitPosition, 1));

    dx = hitPosition * maxSpeed;

    dy = -Math.abs(dy);

    limitSpeed();
  }

  private void resetPosition() {
    x = player.getX() + (player.getWidth() - diameter) / 2;
    y = player.getY() - diameter;
    dx = 0;
    dy = 0;
    moving = false;
  }

  public void start() {
    if (!moving) {
      moving = true;
      dy = -3;
      Random rand = new Random();
      dx = rand.nextDouble() * 2 - 1;
    }
  }

  public interface BallListener {

    void ballLost();
  }

  private BallListener listener;

  public void setListener(BallListener listener) {
    this.listener = listener;
  }

  public int getDiameter() {
    return diameter;
  }

  public double getDx() {
    return dx;
  }

  public double getDy() {
    return dy;
  }

  public double getX() {
    return x;
  }

  public double getY() {
    return y;
  }

  public boolean isMoving() {
    return moving;
  }
}
