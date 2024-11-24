import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Level {
  private Brick[][] bricks;

  public Level(String filePath) {
    bricks = new Brick[12][24];
    loadLevel(filePath);
  }

  private void loadLevel(String filePath) {
    InputStream is = getClass().getResourceAsStream("/" + filePath);
    if (is == null) {
      System.err.println("Level file not found: " + filePath);
      System.exit(1);
    }
    try (BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
      String line;
      int y = 0;
      while ((line = reader.readLine()) != null && y < 12) {
        for (int x = 0; x < line.length() && x < 24; x++) {
          char c = line.charAt(x);
          int health = 0;
          if (c >= '1' && c <= '5') {
            health = c - '0';
          }
          if (health > 0) {
            bricks[y][x] = new Brick(80 + (x * 40), y * 20, health);
          }
        }
        y++;
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public Brick[][] getBricks() {
    return bricks;
  }
}
