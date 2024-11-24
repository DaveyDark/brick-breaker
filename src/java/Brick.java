import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Brick {

  private int x, y, width, height;
  private int health;
  private Image image;

  public Brick(int x, int y, int health) {
    this.x = x;
    this.y = y;
    this.width = 40;
    this.height = 20;
    this.health = health;

    loadImage();
  }

  private void loadImage() {
    String imageName = "";
    switch (health) {
      case 1:
        imageName = "/brick1.png";
        break;
      case 2:
        imageName = "/brick2.png";
        break;
      case 3:
        imageName = "/brick3.png";
        break;
      case 4:
        imageName = "/brick4.png";
        break;
      case 5:
        imageName = "/brick5.png";
        break;
      default:
        imageName = "/brick1.png";
        break;
    }
    try {
      image = ImageIO.read(getClass().getResourceAsStream(imageName));
    } catch (IOException e) {
      e.printStackTrace();
    } catch (IllegalArgumentException e) {
      System.err.println("Image not found: " + imageName);
    }
  }

  public void draw(Graphics g) {
    if (image != null) {
      g.drawImage(image, x, y, width, height, null);
    } else {
      g.fillRect(x, y, width, height);
    }
  }

  public Rectangle getBounds() {
    return new Rectangle(x, y, width, height);
  }

  public void hit() {
    health--;
    if (health > 0) {
      loadImage();
    }
  }

  public boolean isDestroyed() {
    return (health <= 0);
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
