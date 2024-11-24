
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Player {

  private int x, y;
  private int width, height;
  private double dx;
  private double acceleration;
  private double maxSpeed;
  private double deceleration;
  private int windowWidth;

  private boolean movingLeft;
  private boolean movingRight;

  private Image image;

  public Player(int windowWidth) {
    this.windowWidth = windowWidth;
    this.width = 100;
    this.height = 20;
    this.x = (windowWidth - width) / 2;
    this.y = 400;

    this.dx = 0;
    this.acceleration = 0.5;
    this.maxSpeed = 8.0;
    this.deceleration = 0.5;

    this.movingLeft = false;
    this.movingRight = false;

    loadImage();
  }

  private void loadImage() {
    try {
      image = ImageIO.read(getClass().getResourceAsStream("/paddle.png"));
    } catch (IOException e) {
      e.printStackTrace();
    } catch (IllegalArgumentException e) {
      System.err.println("Paddle image not found.");
    }
  }

  public void update() {
    if (movingLeft) {
      dx -= acceleration;
      if (dx < -maxSpeed) {
        dx = -maxSpeed;
      }
    } else if (movingRight) {
      dx += acceleration;
      if (dx > maxSpeed) {
        dx = maxSpeed;
      }
    } else {
      if (dx > 0) {
        dx -= deceleration;
        if (dx < 0) {
          dx = 0;
        }
      } else if (dx < 0) {
        dx += deceleration;
        if (dx > 0) {
          dx = 0;
        }
      }
    }

    x += dx;

    if (x < 0) {
      x = 0;
      dx = 0;
    } else if (x + width > windowWidth) {
      x = windowWidth - width;
      dx = 0;
    }
  }

  public void draw(Graphics g) {
    if (image != null) {
      g.drawImage(image, x, y, width, height, null);
    } else {
      g.setColor(Color.BLACK);
      g.fillRect(x, y, width, height);
    }
  }

  public void moveLeft(boolean isPressed) {
    movingLeft = isPressed;
  }

  public void moveRight(boolean isPressed) {
    movingRight = isPressed;
  }

  public Rectangle getBounds() {
    return new Rectangle(x, y, width, height);
  }

  public int getX() {
    return x;
  }

  public int getY() {
    return y;
  }

  public int getWidth() {
    return width;
  }

  public int getHeight() {
    return height;
  }
}
