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
      double sideXleft = -dy / distance * 10, sideYleft = dx / distance * 10;
      double sideXright = dy / distance * 10, sideYright = -dx / distance * 10;
      dx = (dx / distance);
      dy = (dy / distance);
      double tempX = x, tempY = y;
      int tempDistance = 0;
      while ((Math.abs(tempX - otherX) > 5 || Math.abs(tempY - otherY) > 5) && tempDistance < 1000) {
         if (layout[((int) tempY) / tileSize][((int) tempX) / tileSize] == 1 || layout[((int) tempY) / tileSize][((int) tempX) / tileSize] == 3 ||
            ((tempY + sideYleft) < 1000 && (tempY + sideYleft >= 0) && (tempX + sideXleft) < 1000 && (tempX + sideXleft >= 0) &&
            layout[(int) ((tempY + sideYleft) / tileSize)][(int) ((tempX + sideXleft) / tileSize)] == 1) || 
            ((tempY + sideYleft) < 1000 && (tempY + sideYleft >= 0) && (tempX + sideXleft) < 1000 && (tempX + sideXleft >= 0) &&
            layout[(int) ((tempY + sideYleft) / tileSize)][(int) ((tempX + sideXleft) / tileSize)] == 3) ||
            ((tempY + sideYright) < 1000 && (tempY + sideYright >= 0) && (tempX + sideXright) < 1000 && (tempX + sideXright >= 0) &&
            layout[(int) ((tempY + sideYright) / tileSize)][(int) ((tempX + sideXright) / tileSize)] == 1) ||
            ((tempY + sideYright) < 1000 && (tempY + sideYright >= 0) && (tempX + sideXright) < 1000 && (tempX + sideXright >= 0) &&
            layout[(int) ((tempY + sideYright) / tileSize)][(int) ((tempX + sideXright) / tileSize)] == 3)) {
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