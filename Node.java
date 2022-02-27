import java.util.ArrayList;

public class Node {
   private ArrayList<Node> connections = new ArrayList<>();
   private double x, y;
   
   public Node(double x, double y) {
      this.x = x;
      this.y = y;
   }  
   
   public void addConnection(Node connection) {
      connections.add(connection);
   }
   
   public void addConnections(ArrayList<Node> nodeList) {
      for (Node node: nodeList) {
         connections.add(node);
      } 
   }
   
   public boolean canWalk(double destX, double destY, int[][] layout, int tileSize) {
      double otherX = destX, otherY = destY;
      double dx = otherX - x, dy = otherY - y;
      double distance = Math.sqrt(dx * dx + dy * dy);
      dx = (dx / distance);
      dy = (dy / distance);
      double tempX = x, tempY = y;
      int tempDistance = 0;
      while ((Math.abs(tempX - otherX) > 5 || Math.abs(tempY - otherY) > 5) && tempDistance < 1000) {
         if (layout[((int) tempY) / tileSize][((int) tempX) / tileSize] == 1 || layout[((int) tempY) / tileSize][((int) tempX) / tileSize] == 3) {
            return false;
         }
         tempX += 5 * dx;
         tempY += 5 * dy;
         tempDistance += 5;
      }
      return true;
   }
   
   public ArrayList<Node> getConnections() { return connections; }
   
   public double getX() { return x; }
   
   public double getY() { return y; }
   
}