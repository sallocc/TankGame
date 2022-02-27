import java.util.ArrayList;
import java.util.ArrayDeque;

public class PathFind {
   public static ArrayList<Node> generateExchangePoints(int[][] layout, int tileSize) {
      int[][] pointPlaceLog = new int[layout.length][layout.length];
      ArrayList<Node> nodeList = new ArrayList<>();
      for (int row = 0; row < layout.length - 1; row++) {
         for (int col = 0; col < layout.length - 1; col++) {
            int topLeft = layout[row][col];
            int topRight = layout[row][col + 1];
            int bottomLeft = layout[row + 1][col];
            int bottomRight = layout[row + 1][col + 1];
            int[] square = new int[]{topLeft, topRight, bottomLeft, bottomRight};
            int blocks = 0;
            int blockIdx = -1;
            int pointRow = 0;
            int pointCol = 0;
            for (int i = 0; i < 4; i++) {
               if (square[i] == 1 || square[i] == 3) {
                  blocks++;
                  blockIdx = i;
               }
            }
            switch (blockIdx) {
                  case 0:
                     pointRow = row + 1;
                     pointCol = col + 1;
                     break;
                  case 1:
                     pointRow = row + 1;
                     pointCol = col;
                     break;
                  case 2:
                     pointRow = row;
                     pointCol = col + 1;
                     break;
                  case 3:
                     pointRow = row;
                     pointCol = col;
                     break;
                  default:
               }

            //If there is only 1 block in the square there is an exchange point
            if (blocks == 1 ) {
               if (pointPlaceLog[pointRow][pointCol] == 0) {
                  if (blockIdx == 0) nodeList.add(new Node((col + 1.5) * tileSize, (row + 1.5) * tileSize));
                  if (blockIdx == 1) nodeList.add(new Node((col + 0.5) * tileSize, (row + 1.5) * tileSize));
                  if (blockIdx == 2) nodeList.add(new Node((col + 1.5) * tileSize, (row + 0.5) * tileSize));
                  if (blockIdx == 3) nodeList.add(new Node((col + 0.5) * tileSize, (row + 0.5) * tileSize));
                  pointPlaceLog[pointRow][pointCol] = 1;
               }
            }
            
         }
      }
      return nodeList;
   }
   
   public static void addNodeConnections(ArrayList<Node> nodeList, int[][] layout, int tileSize) {
      for (Node exchangePoint: nodeList) {
         for (Node otherPoint: nodeList) {
            if (exchangePoint.canWalk(otherPoint.getX(), otherPoint.getY(), layout, tileSize) && exchangePoint.getX() != otherPoint.getX()
               && exchangePoint.getY() != otherPoint.getY()) {
                  exchangePoint.addConnection(otherPoint);
            }
         }  
      }
   }
   
   public static ArrayList<Node> chooseShortestWalkablePath(ArrayList<ArrayList<Node>> pathList) {
      return null;
   }
   
   public static ArrayList<ArrayList<Node>> generatePaths(ArrayList<Node> nodeGraph) {
      //Find all exchange points that can walk to the player
      
      
      //From each of these, generate the paths to every other reachable exchange point 
      //and put all of these paths into an ArrayList
      return null;
   } 
   
   public static ArrayList<ArrayList<Node>> extendPaths(Tank enemy, ArrayList<Node> nodeGraph, Node startPoint, int[][] layout, int tileSize) {
      ArrayList<Node> visited = new ArrayList<>();
      ArrayList<ArrayList<Node>> paths = new ArrayList<ArrayList<Node>>();
      ArrayList<Node> firstPath = new ArrayList<>();
      firstPath.add(startPoint);
      paths.add(firstPath);
      boolean moreConnections = true;
      while (moreConnections) {
         moreConnections = false;
         ArrayList<ArrayList<Node>> pathsToAdd = new ArrayList<ArrayList<Node>>();
         //Extend each path by one for each non-visited connection if it can't walk to the enemy
         for (ArrayList<Node> path: paths) {
            //Extend the last node in the path
            Node toExtend = path.get(path.size() - 1);
            //Stop all paths that can walk to the enemy
            if (toExtend.canWalk(enemy.getX(), enemy.getY(), layout, tileSize)) {
               continue;
            }
            boolean firstConnection = true;
            for (Node connection: toExtend.getConnections()) {
               //Add each non-visited connection
               if (!visited.contains(connection)) {
                  moreConnections = true;
                  if (firstConnection) {
                     path.add(connection);
                     firstConnection = false;
                  } else {
                     ArrayList<Node> newPath = new ArrayList<>();
                     for (Node point: path) {
                        newPath.add(point);
                     }
                     newPath.add(connection);
                     pathsToAdd.add(newPath);
                  }
                  visited.add(connection);
               }  
            }
            for (ArrayList<Node> newPath: pathsToAdd) {
               paths.add(newPath);
            }
            pathsToAdd.clear();
         }
         
      }
      return paths;
   }
}