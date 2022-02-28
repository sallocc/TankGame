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
                  if (blockIdx == 0) nodeList.add(new Node((col + 1.8) * tileSize, (row + 1.8) * tileSize));
                  if (blockIdx == 1) nodeList.add(new Node((col + 0.8) * tileSize, (row + 1.8) * tileSize));
                  if (blockIdx == 2) nodeList.add(new Node((col + 1.8) * tileSize, (row + 0.8) * tileSize));
                  if (blockIdx == 3) nodeList.add(new Node((col + 0.8) * tileSize, (row + 0.8) * tileSize));
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
            if (exchangePoint.canWalk(otherPoint.getX(), otherPoint.getY(), layout, tileSize) && (exchangePoint.getX() != otherPoint.getX()
               || exchangePoint.getY() != otherPoint.getY())) {
                  exchangePoint.addConnection(otherPoint);
            }
         }  
      }
   }
   
   public static ArrayList<Node> chooseShortestWalkablePath(ArrayList<ArrayList<Node>> pathList, Tank enemy, int[][] layout, int tileSize) {
      ArrayList<Node> shortestPath = new ArrayList<>();
      double shortestPathLength = 1000000;
      for (ArrayList<Node> path: pathList) {
         //Only calculate path lengths for those that are walkable by the enemy
         if (path.get(path.size() - 1).canWalk(enemy.getX(), enemy.getY(), layout, tileSize)) {
            double pathLength = 0;
            for (int i = 0; i < path.size() - 1; i++) {
               Node firstPoint = path.get(i);
               Node secondPoint = path.get(i + 1);
               pathLength += Math.sqrt((firstPoint.getX() - secondPoint.getX()) * (firstPoint.getX() - secondPoint.getX())
                               + (firstPoint.getY() - secondPoint.getY()) * (firstPoint.getY() - secondPoint.getY()));
            }
            if (pathLength < shortestPathLength) {
               shortestPath = path;
               shortestPathLength = pathLength;
            }
         }
      }
      return shortestPath;
   }
   
   public static ArrayList<ArrayList<Node>> generatePaths(ArrayList<Node> nodeGraph, Tank player, Tank enemy, int[][] layout, int tileSize) {
      //Find all exchange points that can walk to the player
      ArrayList<Node> startPoints = new ArrayList<>();
      for (Node point: nodeGraph) {
         if (point.canWalk(player.getX(), player.getY(), layout, tileSize)) {
            startPoints.add(point);
         }
      }
      
      ArrayList<ArrayList<Node>> paths = new ArrayList<ArrayList<Node>>();
      //From each of these, generate the paths to every other reachable exchange point 
      //and put all of these paths into an ArrayList
      for (Node startPoint: startPoints) {
         for (ArrayList<Node> path: extendPaths(enemy, nodeGraph, startPoint, layout, tileSize)) {
            paths.add(path);
         }
      }
      return paths;
   } 
   
   public static ArrayList<ArrayList<Node>> extendPaths(Tank enemy, ArrayList<Node> nodeGraph, Node startPoint, int[][] layout, int tileSize) {
      ArrayList<Node> visited = new ArrayList<>();
      visited.add(startPoint);
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
                     for (int i = 0; i < path.size() - 1; i++) {
                        newPath.add(path.get(i));
                     }
                     newPath.add(connection);
                     pathsToAdd.add(newPath);
                  }
                  visited.add(connection);
               }  
            }
         }
         for (ArrayList<Node> newPath: pathsToAdd) {
            paths.add(newPath);
         }
         pathsToAdd.clear();
         
      }
      return paths;
   }
   
   public static Node getPathFoundNode(Tank enemy, Tank player, int[][] layout, int tileSize) {
      ArrayList<Node> nodeList = generateExchangePoints(layout, tileSize);
      addNodeConnections(nodeList, layout, tileSize);
      ArrayList<ArrayList<Node>> paths = generatePaths(nodeList, player, enemy, layout, tileSize);
      ArrayList<Node> shortestPath = chooseShortestWalkablePath(paths, enemy, layout, tileSize);
      if (!shortestPath.isEmpty()) {
         return shortestPath.get(shortestPath.size() - 1);
      } else {
         return new Node(enemy.getX(), enemy.getY());
      }
   }
}