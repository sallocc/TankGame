import java.awt.image.BufferedImage;
import java.awt.*;
import java.io.*;
import javax.imageio.ImageIO;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.RasterFormatException;

public class Missile {

   public static int size = 50;
   public static int smokeSize = 20;
   public boolean homing = false;
   public double x, y;
   public double dx, dy;
   public boolean is_enemy;
   public double[][] smoke = new double[3][2];
   public BufferedImage sprite1, sprite2, sprite3;
   public static BufferedImage smoke1, smoke2, smoke3;
   private int frameCount = 0;
   public int bouncesLeft = 0;
   
   public Missile(double x, double y, double dx, double dy, boolean is_enemy, BufferedImage sprite1, BufferedImage sprite2, BufferedImage sprite3) {
      this.x = x;
      this.y = y;
      this.dx = dx;
      this.dy = dy;
      this.is_enemy = is_enemy;
      this.sprite1 = sprite1;
      this.sprite2 = sprite2;
      this.sprite3 = sprite3;
      if (smoke1 == null) {
         try {
            smoke1 = ImageIO.read(new File("smoke1.png"));
            smoke2 = ImageIO.read(new File("smoke2.png"));
            smoke2 = ImageIO.read(new File("smoke3.png"));
         } catch (IOException e) { }
      }
   }
   
   public void updateSmoke() {
      smoke[2][0] = smoke[1][0];
      smoke[2][1] = smoke[1][1];
      smoke[1][0] = smoke[0][0];
      smoke[1][1] = smoke[0][1];
      smoke[0][0] = (x - 3*dx);
      smoke[0][1] = (y - 3*dy);
   }
   
   public void setSprites(BufferedImage sprite1, BufferedImage sprite2, BufferedImage sprite3) {
      this.sprite1 = sprite1;
      this.sprite2 = sprite2;
      this.sprite3 = sprite3;
   }
   
   public int getBounces() { return bouncesLeft; }
   
   public void setBounces(int bounces) { bouncesLeft = bounces; }
   
   public double getX() { return x; }
   
   public double getY() { return y; }
   
   public double getDX() { return dx; }
   
   public void setDX(double newDX) { dx = newDX; }
   
   public void setDY(double newDY) { dy = newDY; }
   
   public double getDY() { return dy; }
   
   public void draw(Graphics g, Tank player, BufferedImage sprite1Orig, BufferedImage sprite2Orig, BufferedImage sprite3Orig, int missileSpeed) {
      frameCount++;
      if (homing) {
         double angle = Math.atan(dy / dx);
         if (dx < 0) {
            angle += Math.PI;
         } else if (angle >= -Math.PI / 2 && angle < 0) {
            angle += 2 * Math.PI;
         }
         double angleToPlayer = Math.atan((player.y - y) / (player.x - x));
         if ((player.x - x) < 0) {
            angleToPlayer += Math.PI;
         } else if (angleToPlayer >= -Math.PI / 2 && angleToPlayer < 0) {
            angleToPlayer += 2 * Math.PI;
         }
         
         //Only try to rotate when the angle is great enough
         if (Math.abs(angleToPlayer - angle) > Math.toRadians(5) || Math.abs(angle - angleToPlayer) > Math.toRadians(5)) {
            int rotationDirection = 0;
            if (angleToPlayer > angle) {
               if (Math.abs(angleToPlayer - angle) < 2 * Math.PI - (Math.abs(angleToPlayer - angle))) {
                  rotationDirection = 1;
               } else {
                  rotationDirection = -1;
               }
            } else {
               if (Math.abs(angleToPlayer - angle) < 2 * Math.PI - (Math.abs(angleToPlayer - angle)) && angleToPlayer < angle) {
                  rotationDirection = -1;  
               } else {
                  rotationDirection = 1;
               }
            }
            double rotationRequired = angle + (3 * (Math.PI / 180) * rotationDirection);
            dx = missileSpeed / 2 * Math.cos(rotationRequired);
            dy = missileSpeed / 2 * Math.sin(rotationRequired);
            double locationX = sprite1Orig.getWidth() / 2;
            double locationY = sprite1Orig.getHeight() / 2;
            try {
               AffineTransform tx = AffineTransform.getRotateInstance(rotationRequired, locationX, locationY);
               AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
               BufferedImage missileSprite1rotate = op.filter(sprite1Orig, null);
               BufferedImage missileSprite2rotate = op.filter(sprite2Orig, null);
               BufferedImage missileSprite3rotate = op.filter(sprite3Orig, null);
               this.sprite1 = missileSprite1rotate;
               this.sprite2 = missileSprite2rotate;
               this.sprite3 = missileSprite3rotate;
            } catch (RasterFormatException e) {
               System.out.println("Error with playerAngle = " + angleToPlayer + ", missileAngle = " + angle + ", and rotationAngle = " + rotationRequired);
            }
         }
      }
      if (frameCount > 11) frameCount = 0;
      switch (frameCount % 3) {
         case 0:
            g.drawImage(sprite1, (int) (x - size / 2.0), (int)  (y - size / 2.0), size, size, null);
            break;
            
         case 1:
            g.drawImage(sprite2, (int) (x - size / 2.0), (int)  (y - size / 2.0), size, size, null);
            break;
         
         default:
            g.drawImage(sprite3, (int) (x - size / 2.0), (int)  (y - size / 2.0), size, size, null);
      }
      if (smoke[0][0] != 0 && smoke[0][1] != 0) {
         g.drawImage(smoke1, (int) (smoke[0][0] - smokeSize / 2.0), (int) (smoke[0][1] - smokeSize / 2.0), smokeSize, smokeSize, null);
      }
      if (smoke[1][0] != 0 && smoke[1][1] != 0) {
         g.drawImage(smoke2, (int) (smoke[1][0] - smokeSize / 2.0), (int) (smoke[1][1] - smokeSize / 2.0), smokeSize, smokeSize, null);
      }
      if (smoke[2][0] != 0 && smoke[2][1] != 0) {
         g.drawImage(smoke3, (int) (smoke[2][0] - smokeSize / 2.0), (int) (smoke[2][1] - smokeSize / 2.0), smokeSize, smokeSize, null);
      }
      x += dx;
      y += dy;
      if (frameCount % 2 == 0) updateSmoke();
   }
}