import javax.swing.*;

public class TankGame {
   public static void main(String[] args) {
      JFrame frame = new JFrame("Tank Game");
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.setSize(515, 535);
      DrawGame gameDrawer = new DrawGame();
      gameDrawer.setFrame(frame);
      frame.add(gameDrawer);
      gameDrawer.initializeWindow();
      gameDrawer.flipAnimation();
      frame.setVisible(true);
   }
}