import javax.imageio.ImageIO;
import java.io.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import javafx.scene.media.*;
import javafx.embed.swing.JFXPanel;

public class Tank {

   public enum Cardinal {
      NORTH, SOUTH, WEST, EAST, NORTHEAST, NORTHWEST, SOUTHWEST, SOUTHEAST   
   }
   
   private boolean is_enemy;
   public boolean travelling_ai = false;
   public boolean rocket_ai = false;
   public boolean tripleshot_ai = false;
   public boolean homing_ai = false;
   public boolean pathfinding_ai = false;
   public int x, y;
   private BufferedImage sprite1, sprite2, sprite3;
   private BufferedImage sprite1_n, sprite1_w, sprite1_e, sprite1_ne, sprite1_nw, sprite1_se, sprite1_sw, sprite1_s;
   private BufferedImage sprite2_n, sprite2_w, sprite2_e, sprite2_ne, sprite2_nw, sprite2_se, sprite2_sw, sprite2_s;
   private BufferedImage sprite3_n, sprite3_w, sprite3_e, sprite3_ne, sprite3_nw, sprite3_se, sprite3_sw, sprite3_s;
   
   private BufferedImage barrelSprite1, barrelSprite2, barrelSprite3;
   private int frameCount = 0;
   private int orientation = 0;
   private static int size = 50;
   public Cardinal direction = Cardinal.WEST;
   public Cardinal barrelDirection = Cardinal.EAST;
   private Media explosionSound;   
   private MediaPlayer explosionSoundPlayer;   
   public long lastFiredTime = 0;
   
   
   public Tank(boolean is_enemy, int x, int y) {
      JFXPanel fxPanel = new JFXPanel();
      explosionSound = new Media(new File("Explosion+1.mp3").toURI().toString());
      explosionSoundPlayer = new MediaPlayer(explosionSound);

      
      this.x = x;
      this.y = y;
      this.is_enemy = is_enemy;
      if (is_enemy) {
         try {
            sprite1 = ImageIO.read(new File("tank_1_enemy_simple.png"));
            sprite2 = ImageIO.read(new File("tank_2_enemy_simple.png"));
            sprite3 = ImageIO.read(new File("tank_3_enemy_simply.png"));
            barrelSprite1 = ImageIO.read(new File("tank_top_1_enemy.png"));
            barrelSprite2 = ImageIO.read(new File("tank_top_2_enemy.png"));
            barrelSprite3 = ImageIO.read(new File("tank_top_3_enemy.png"));
            sprite1_n = sprite1;
            sprite1_ne = sprite1;
            sprite1_s = sprite1;
            sprite1_se = sprite1;
            sprite1_w = sprite1;
            sprite1_nw = sprite1;
            sprite1_e = sprite1;
            sprite1_sw = sprite1;
            sprite2_n = sprite2;
            sprite2_ne = sprite2;
            sprite2_s = sprite2;
            sprite2_se = sprite2;
            sprite2_w = sprite2;
            sprite2_nw = sprite2;
            sprite2_sw = sprite2;
            sprite2_e = sprite2;
            sprite3_n = sprite3;
            sprite3_ne = sprite3;
            sprite3_s = sprite3;
            sprite3_se = sprite3;
            sprite3_w = sprite3;
            sprite3_nw = sprite3;
            sprite3_e = sprite3;
            sprite3_sw = sprite3;
            
         } catch (IOException e) {
         
         }
      } else {
         try {
            sprite1 = ImageIO.read(new File("tank_1_simple.png"));
            sprite2 = ImageIO.read(new File("tank_2_simple.png"));
            sprite3 = ImageIO.read(new File("tank_template.png"));
            barrelSprite1 = ImageIO.read(new File("tank_top_1_e.png"));
            barrelSprite2 = ImageIO.read(new File("tank_top_2.png"));
            barrelSprite3 = ImageIO.read(new File("tank_top_3.png"));
            sprite1_n = ImageIO.read(new File("tank_1_s_simple.png"));
            sprite1_ne = ImageIO.read(new File("tank_1_sw_simple.png"));
            sprite1_nw = ImageIO.read(new File("tank_1_se_simple.png"));
            sprite1_w = ImageIO.read(new File("tank_1_simple.png"));
            sprite1_e = ImageIO.read(new File("tank_1_simple.png"));
            sprite1_s = ImageIO.read(new File("tank_1_s_simple.png"));
            sprite1_se = ImageIO.read(new File("tank_1_se_simple.png"));
            sprite1_sw = ImageIO.read(new File("tank_1_sw_simple.png"));
            sprite2_n = ImageIO.read(new File("tank_2_n_simple.png"));
            sprite2_ne = ImageIO.read(new File("tank_2_sw_simple.png"));
            sprite2_nw = ImageIO.read(new File("tank_2_se_simple.png"));
            sprite2_w = ImageIO.read(new File("tank_2_simple.png"));
            sprite2_e = ImageIO.read(new File("tank_2_simple.png"));
            sprite2_s = ImageIO.read(new File("tank_2_n_simple.png"));
            sprite2_se = ImageIO.read(new File("tank_2_se_simple.png"));
            sprite2_sw = ImageIO.read(new File("tank_2_sw_simple.png"));
            sprite3_n = ImageIO.read(new File("tank_3_n_simple.png"));
            sprite3_ne = ImageIO.read(new File("tank_3_sw_simple.png"));
            sprite3_nw = ImageIO.read(new File("tank_3_se_simple.png"));
            sprite3_w = ImageIO.read(new File("tank_3_simple.png"));
            sprite3_e = ImageIO.read(new File("tank_3_simple.png"));
            sprite3_s = ImageIO.read(new File("tank_3_n_simple.png"));
            sprite3_se = ImageIO.read(new File("tank_3_se_simple.png"));
            sprite3_sw = ImageIO.read(new File("tank_3_sw_simple.png"));
         } catch (IOException e) {
            System.out.println("Failed reading image: " + e.getMessage());
         }
      }
   }
   
   public void playExplosion() { 
      explosionSoundPlayer.stop();
      explosionSoundPlayer.play(); 
   }
   
   public BufferedImage getTankImage1() { return sprite1_w; }
   
   public BufferedImage getTankImage2() { return sprite2_w; }
   
   public BufferedImage getTankImage3() { return sprite3_w; }
   
   public void setTankImages(BufferedImage sprite1, BufferedImage sprite2, BufferedImage sprite3) {
      this.sprite1_w = sprite1;
      this.sprite2_w = sprite2;
      this.sprite3_w = sprite3;
   }
   
   public int getSize() { return size; }
   
   public int getX() { return x; }
   
   public int getY() { return y; }
   
   public void setBarrelImage(BufferedImage barrelSprite1, BufferedImage barrelSprite2, BufferedImage barrelSprite3) {
      this.barrelSprite1 = barrelSprite1;
      this.barrelSprite2 = barrelSprite2;
      this.barrelSprite3 = barrelSprite3;
   }
   
   public BufferedImage getBarrelImage1() { return barrelSprite1; }
   
   public BufferedImage getBarrelImage2() { return barrelSprite2; }
   
   public BufferedImage getBarrelImage3() { return barrelSprite3; }
   
   public void draw(Graphics g) {
      frameCount++;
      if (frameCount > 2) frameCount = 0;
      x -= size/2;
      y -= size/2;
      switch (frameCount) {
         case 0:
            switch (direction) {
               case NORTH:
                  g.drawImage(sprite1_n, x, y, size, size, null);
                  break;
               case NORTHWEST:
                  g.drawImage(sprite1_nw, x, y, size, size, null);
                  break;
               case NORTHEAST:
                  g.drawImage(sprite1_ne, x, y, size, size, null);
                  break;
               case WEST:
                  g.drawImage(sprite1_w, x, y, size, size, null);
                  break;
               case EAST:
                   g.drawImage(sprite1_e, x, y, size, size, null);
                   break;
               case SOUTH:
                   g.drawImage(sprite1_s, x, y, size, size, null);
                   break;
               case SOUTHEAST:
                   g.drawImage(sprite1_se, x, y, size, size, null);
                   break;
               case SOUTHWEST:
                   g.drawImage(sprite1_sw, x, y, size, size, null);
                   break;
               default:
                  g.drawImage(sprite1, x, y, size, size, null);
            }
            break;
         case 1:
            switch (direction) {
               case NORTH:
                  g.drawImage(sprite2_n, x, y, size, size, null);
                  break;
               case NORTHWEST:
                  g.drawImage(sprite2_nw, x, y, size, size, null);
                  break;
               case NORTHEAST:
                  g.drawImage(sprite2_ne, x, y, size, size, null);
                  break;
               case WEST:
                  g.drawImage(sprite2_w, x, y, size, size, null);
                  break;
               case EAST:
                   g.drawImage(sprite2_e, x, y, size, size, null);
                   break;
               case SOUTH:
                   g.drawImage(sprite2_s, x, y, size, size, null);
                   break;
               case SOUTHEAST:
                   g.drawImage(sprite2_se, x, y, size, size, null);
                   break;
               case SOUTHWEST:
                   g.drawImage(sprite2_sw, x, y, size, size, null);
                   break;
               default:
                  g.drawImage(sprite2, x, y, size, size, null);
                  
            }
            break;
         case 2:
            switch (direction) {
               case NORTH:
                  g.drawImage(sprite3_n, x, y, size, size, null);
                  break;
               case NORTHWEST:
                  g.drawImage(sprite3_nw, x, y, size, size, null);
                  break;
               case NORTHEAST:
                  g.drawImage(sprite3_ne, x, y, size, size, null);
                  break;
               case WEST:
                  g.drawImage(sprite3_w, x, y, size, size, null);
                  break;
               case EAST:
                   g.drawImage(sprite3_e, x, y, size, size, null);
                   break;
               case SOUTH:
                   g.drawImage(sprite3_s, x, y, size, size, null);
                   break;
               case SOUTHEAST:
                   g.drawImage(sprite3_se, x, y, size, size, null);
                   break;
               case SOUTHWEST:
                   g.drawImage(sprite3_sw, x, y, size, size, null);
                   break;
               default:
                  g.drawImage(sprite3, x, y, size, size, null);
                  
            }
            break;
         default:
            g.drawImage(sprite3, x, y, size, size, null);
            break;
            
      }
      switch (frameCount) {
         case 0:
            g.drawImage(barrelSprite1, x, y, size, size, null);
            break;
         case 1:
            g.drawImage(barrelSprite2, x, y, size, size, null);
            break;
         default:
            g.drawImage(barrelSprite3, x, y, size, size, null);
      }
      x += size/2;
      y += size/2; 
   }
   
   public boolean canSee(Tank other, int[][] layout, int tileSize) {
      int otherX = other.x, otherY = other.y;
      double dx = otherX - x, dy = otherY - y;
      double distance = Math.sqrt(dx * dx + dy * dy);
      dx = (dx / distance);
      dy = (dy / distance);
      double tempX = x, tempY = y;
      int tempDistance = 0;
      while ((Math.abs(tempX - otherX) > 5 || Math.abs(tempY - otherY) > 5) && tempDistance < 1000) {
         if (layout[((int) tempY) / tileSize][((int) tempX) / tileSize] == 1) {
            return false;
         }
         tempX += 5 * dx;
         tempY += 5 * dy;
         tempDistance += 5;
      }
      return true;
   }

}