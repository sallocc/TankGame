import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import javax.swing.Timer;
import javax.imageio.ImageIO;
import java.io.*;
import javax.swing.event.*;
import java.util.Random;
import java.util.ArrayList;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import javafx.scene.media.*;
import javafx.embed.swing.JFXPanel;
import java.util.Scanner;

public class DrawGame extends JPanel {

   private JFrame frame;
   private JPanel panel;
   private Timer timer;
   private int tickLength = 100;
   private boolean playing = false;
   private boolean accuracyStarsOn = true;
   private boolean levelSummaryScreen = false;
   private boolean mainMenuActive = true;
   private boolean levelSelectActive = false;
   private boolean modeSelectActive = false;
   private boolean endlessModeActive = false;
   private int levelHovered = 0;
   private int levelModeHovered = 0;
   private boolean gamePlayActive = false;
   private boolean gameOverScreen = false;
   private boolean optionsMenuActive = false;
   private boolean fadeToWhite = false;
   private boolean drawExplosion = false;
   private boolean soundOn = true;
   private boolean musicOn = true;
   private int difficulty = 1;
   private int frameCount = 0;
   private int frameRepeat = 3;
   private boolean drawLevel1 = false, drawLevel2 = false, drawLevel3 = false;
   private BufferedImage level1Image1, level1Image2, level1Image3;
   private BufferedImage level2Image1, level2Image2, level2Image3;
   private BufferedImage level3Image1, level3Image2, level3Image3;
   private BufferedImage mainMenu1Normal, mainMenu2Normal, mainMenu3Normal, mainMenu1Play, mainMenu2Play, mainMenu3Play;
   private BufferedImage mainMenuWhite1, mainMenuWhite2, mainMenuWhite3, mainMenuWhite4, mainMenuWhite5, mainMenuWhite6;
   private BufferedImage mainMenu1, mainMenu2, mainMenu3;
   private BufferedImage gameOverScreen1, gameOverScreen2, gameOverScreen3;
   private BufferedImage missileSprite1, missileSprite2, missileSprite3;
   private BufferedImage explosionImage1, explosionImage2, explosionImage3, explosionImage4, 
               explosionImage5, explosionImage6, explosionImage7, explosionImage8;
   private BufferedImage optionsSoundOnMusicOnEasy, optionsSoundOnMusicOffEasy, optionsSoundOffMusicOnEasy, optionsSoundOffMusicOffEasy, 
      optionsSoundOnMusicOnMedium, optionsSoundOnMusicOffMedium, optionsSoundOffMusicOnMedium, optionsSoundOffMusicOffMedium, 
      optionsSoundOnMusicOnHard, optionsSoundOnMusicOffHard, optionsSoundOffMusicOnHard, optionsSoundOffMusicOffHard;
   private BufferedImage levelSelect;
   private BufferedImage levelSummary, levelSummaryNextLevel, levelSummaryLevelSelect;
   private BufferedImage mudTile, holeTile;
   private boolean nextLevelHovered = false;
   private boolean levelSelectHovered = false;
   private int missileSpeed = 5;
   
   private Tank player;
   private int playerLives = 3;
   private int playerSpeed = 8;
   private int[] accuracyStars;
   private int[] timerStars;
   private int currStar = 0;
   private int enemyCount = 0;
   private long levelTimeStart = 0;
   private long levelTimeEnd = 0;
   private long lastEnemySpawnTime = 0;
   private int missilesFired = 0;
   
   private int level = 1;
   
   private ArrayList<Tank> enemies = new ArrayList<>();
   private ArrayList<int[][]> levelLayouts = new ArrayList<>();
   private boolean down_pressed, left_pressed, up_pressed, right_pressed;
   private BufferedImage[] barrelSpritesRotated1 = new BufferedImage[360];
   private BufferedImage[] barrelSpritesRotated2 = new BufferedImage[360];
   private BufferedImage[] barrelSpritesRotated3 = new BufferedImage[360];
   private BufferedImage[][] playerBarrelImages = new BufferedImage[][]{barrelSpritesRotated1, barrelSpritesRotated2, barrelSpritesRotated3};
   
   private BufferedImage[] barrelSpritesRotated1Enemy = new BufferedImage[360];
   private BufferedImage[] barrelSpritesRotated2Enemy = new BufferedImage[360];
   private BufferedImage[] barrelSpritesRotated3Enemy = new BufferedImage[360];
   private BufferedImage[][] enemyBarrelImages = new BufferedImage[][]{barrelSpritesRotated1Enemy, barrelSpritesRotated2Enemy, barrelSpritesRotated3Enemy};
   private BufferedImage[] enemySpritesRotated1 = new BufferedImage[360];
   private BufferedImage[] enemySpritesRotated2 = new BufferedImage[360];
   private BufferedImage[] enemySpritesRotated3 = new BufferedImage[360];
   private BufferedImage[][] enemyTankImages = new BufferedImage[][]{enemySpritesRotated1, enemySpritesRotated2, enemySpritesRotated3};
   private BufferedImage starSymbol, accuracySymbol, timerSymbol;
   private BufferedImage redAccuracySymbol, redTimerSymbol, blackStarSymbol;
   
   private ArrayList<Missile> missiles = new ArrayList<>();
   private ArrayList<Explosion> explosions = new ArrayList<>();
   public class Explosion {
      int x, y;
      int frame = 0;
      BufferedImage barrelImage;
      BufferedImage enemyImage;
      
      public Explosion(int x, int y, BufferedImage barrelImage, BufferedImage enemyImage) {
         this.x = x;
         this.y = y;
         this.barrelImage = barrelImage;
         this.enemyImage = enemyImage;
      }
      
      public void draw(Graphics g) {
         g.drawImage(enemyImage, x, y, player.getSize(), player.getSize(), null);
         g.drawImage(barrelImage, x, y, player.getSize(), player.getSize(), null);
         try {
            if (explosionImage1 == null) {
               explosionImage1 = ImageIO.read(new File("explosion1.png"));
               explosionImage2 = ImageIO.read(new File("explosion2.png"));
               explosionImage3 = ImageIO.read(new File("explosion3.png"));
               explosionImage4 = ImageIO.read(new File("explosion4.png"));
               explosionImage5 = ImageIO.read(new File("explosion5.png"));
               explosionImage6 = ImageIO.read(new File("explosion6.png"));
               explosionImage7 = ImageIO.read(new File("explosion7.png"));
               explosionImage8 = ImageIO.read(new File("explosion8.png"));
            } 
            switch (frame) {
               case 0:
                  g.drawImage(explosionImage1, x, y, player.getSize(), player.getSize(), null);
                  break;
               case 1:
                  g.drawImage(explosionImage2, x, y, player.getSize(), player.getSize(), null);
                  break;
               case 2:
                  g.drawImage(explosionImage3, x, y, player.getSize(), player.getSize(), null);
                  break;
               case 3:
                  g.drawImage(explosionImage4, x, y, player.getSize(), player.getSize(), null);
                  break;
               case 4:
                  g.drawImage(explosionImage5, x, y, player.getSize(), player.getSize(), null);
                  break;
               case 5:
                  g.drawImage(explosionImage6, x, y, player.getSize(), player.getSize(), null);
                  break;
               case 6:
                  g.drawImage(explosionImage7, x, y, player.getSize(), player.getSize(), null);
                  break;
               case 7:
                  g.drawImage(explosionImage8, x, y, player.getSize(), player.getSize(), null);
                  break;
               default:
                  g.setColor(Color.WHITE);
                  g.fillRect(x, y, player.getSize(), player.getSize());
            }
         } catch (IOException e) { }
         frame++;
      }
   }
   
   private int lastMouseX = 300, lastMouseY = 300;
   private int windowSize = 500;
   
   private BufferedImage woodTile;
   private int[][] layout = GameConstants.level1Layout;
   
   private Media menuSong;
   private MediaPlayer menuSongPlayer;
   private Media gameSong;
   private MediaPlayer gameSongPlayer;
   
   public void flipAnimation() {
      if (timer == null) {
         ActionListener listener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
               repaint();
            }
         }; 
         timer = new Timer(tickLength, listener);
      }
      if (playing) {
         timer.stop();
         playing = false;
      }   else {
         playing = true;
         timer.start();
      }
   }
   
   public void setFrame(JFrame frame) { this.frame = frame; }
   
   public void initializeWindow() {
      frame.addMouseMotionListener(new MouseMotion());
      frame.addMouseListener(new MouseClick());
      frame.addKeyListener(new KeyClick());
      player = new Tank(false, 100, 250);
      for (Tank enemy: GameConstants.getLevel1Enemies()) {
         enemies.add(enemy);
      }
      generateBarrelRotationImages(player, playerBarrelImages);
      generateBarrelRotationImages(enemies.get(0), enemyBarrelImages);
      generateTankRotationImages(enemies.get(0), enemyTankImages);
      JFXPanel fxPanel = new JFXPanel();
      menuSong  = new Media(new File("Ultimate-Victory-WST010901.mp3").toURI().toString());
      menuSongPlayer = new MediaPlayer(menuSong);
      gameSong  = new Media(new File("Yankee-Doodle-Dandy-PMT012401.mp3").toURI().toString());
      gameSongPlayer = new MediaPlayer(gameSong);
      levelLayouts.add(GameConstants.level1Layout);
      levelLayouts.add(GameConstants.level2Layout);
      levelLayouts.add(GameConstants.level3Layout);
      levelLayouts.add(GameConstants.level4Layout);
      levelLayouts.add(GameConstants.level5Layout);
      levelLayouts.add(GameConstants.level6Layout);
      levelLayouts.add(GameConstants.level7Layout);
      levelLayouts.add(GameConstants.level8Layout);
      levelLayouts.add(GameConstants.level9Layout);
      levelLayouts.add(GameConstants.level10Layout);
      accuracyStars = new int[levelLayouts.size()];
      timerStars = new int[levelLayouts.size()];
   }
   
   public void drawMainMenu(int frameCount, Graphics g) {
      try {
         if (mainMenu1 == null) {
            mudTile = ImageIO.read(new File("mud.png"));
            holeTile = ImageIO.read(new File("hole.png"));
            levelSummary = ImageIO.read(new File("levelSummary.png"));
            levelSummaryNextLevel = ImageIO.read(new File("levelSummaryNextLevel.png"));
            levelSummaryLevelSelect = ImageIO.read(new File("levelSummaryLevelSelect.png"));
            starSymbol = ImageIO.read(new File("star.png"));
            timerSymbol = ImageIO.read(new File("timer.png"));
            accuracySymbol = ImageIO.read(new File("target.png"));
            redTimerSymbol = ImageIO.read(new File("redTimer.png"));
            redAccuracySymbol = ImageIO.read(new File("redTarget.png"));
            blackStarSymbol = ImageIO.read(new File("blackStar.png"));
            levelSelect = ImageIO.read(new File("levelSelect.png"));
            optionsSoundOnMusicOnEasy = ImageIO.read(new File("optionsSoundOnMusicOnEasy.png"));
            optionsSoundOnMusicOffEasy = ImageIO.read(new File("optionsSoundOnMusicOffEasy.png"));
            optionsSoundOffMusicOnEasy = ImageIO.read(new File("optionsSoundOffMusicOnEasy.png"));
            optionsSoundOffMusicOffEasy = ImageIO.read(new File("optionsSoundOffMusicOffEasy.png"));
            optionsSoundOnMusicOnMedium = ImageIO.read(new File("optionsSoundOnMusicOnMedium.png"));
            optionsSoundOnMusicOffMedium = ImageIO.read(new File("optionsSoundOnMusicOffMedium.png"));
            optionsSoundOffMusicOnMedium = ImageIO.read(new File("optionsSoundOffMusicOnMedium.png"));
            optionsSoundOffMusicOffMedium = ImageIO.read(new File("optionsSoundOffMusicOffMedium.png"));
            optionsSoundOnMusicOnHard = ImageIO.read(new File("optionsSoundOnMusicOnHard.png"));
            optionsSoundOnMusicOffHard = ImageIO.read(new File("optionsSoundOnMusicOffHard.png"));
            optionsSoundOffMusicOnHard = ImageIO.read(new File("optionsSoundOffMusicOnHard.png"));
            optionsSoundOffMusicOffHard = ImageIO.read(new File("optionsSoundOffMusicOffHard.png"));
            level1Image1 = ImageIO.read(new File("level1Image1.png"));
            level1Image2 = ImageIO.read(new File("level1Image2.png"));
            level1Image3 = ImageIO.read(new File("level1Image3.png"));
            level2Image1 = ImageIO.read(new File("level2Image1.png"));
            level2Image2 = ImageIO.read(new File("level2Image2.png"));
            level2Image3 = ImageIO.read(new File("level2Image3.png"));
            level3Image1 = ImageIO.read(new File("level3Image1.png"));
            level3Image2 = ImageIO.read(new File("level3Image2.png"));
            level3Image3 = ImageIO.read(new File("level3Image3.png"));
            mainMenu1Normal = ImageIO.read(new File("main_menu_1.png"));
            mainMenu2Normal = ImageIO.read(new File("main_menu_2.png"));
            mainMenu3Normal = ImageIO.read(new File("main_menu_3.png"));
            mainMenu1Play = ImageIO.read(new File("main_menu_1_play.png"));
            mainMenu2Play = ImageIO.read(new File("main_menu_2_play.png"));
            mainMenu3Play = ImageIO.read(new File("main_menu_3_play.png"));
            mainMenuWhite1 = ImageIO.read(new File("main_menu_4.png"));
            mainMenuWhite2 = ImageIO.read(new File("main_menu_5.png"));
            mainMenuWhite3 = ImageIO.read(new File("main_menu_6.png"));
            mainMenuWhite4 = ImageIO.read(new File("main_menu_7.png"));
            mainMenuWhite5 = ImageIO.read(new File("main_menu_8.png"));
            mainMenuWhite6 = ImageIO.read(new File("main_menu_9.png"));
            mainMenu1 = mainMenu1Normal;
            mainMenu2 = mainMenu2Normal;
            mainMenu3 = mainMenu3Normal;
         }
         if (frameCount % 3 == 0) {
            g.drawImage(mainMenu1, 0, 0, windowSize, windowSize, null);
         } else if (frameCount % 3 == 1) {
            g.drawImage(mainMenu2, 0, 0, windowSize, windowSize, null);
         } else if (frameCount % 3 == 2) {
            g.drawImage(mainMenu3, 0, 0, windowSize, windowSize, null);
         } else {
            g.drawImage(mainMenu3, 0, 0, windowSize, windowSize, null);
            
         }
      } catch (IOException e) {
         System.out.println("Images not found.");
      }
   }
   
   public void drawModeSelect(Graphics g) throws FontFormatException, IOException {
      g.setColor(Color.BLACK);
      Font tankFont = Font.createFont(Font.TRUETYPE_FONT, new File("zagreb_underground.ttf")).deriveFont(Font.PLAIN, 32);
      g.setFont(tankFont);
      g.drawString("Mode Select", windowSize * 1/4, windowSize * 3/16);
      
      tankFont = Font.createFont(Font.TRUETYPE_FONT, new File("zagreb_underground.ttf")).deriveFont(Font.PLAIN, 16);
      g.setFont(tankFont);
      
      switch (levelModeHovered) {
         case 0:
            g.setColor(Color.BLACK);
            g.drawString("Story Mode", windowSize * 3/8, windowSize * 5/16);
            g.drawRect(windowSize * 3/8 - 20, windowSize * 1/4, windowSize * 1/3, windowSize * 1/8);
            g.drawString("Endless Mode", windowSize * 3/8, windowSize * 9/16);
            g.drawRect(windowSize * 3/8 - 20, windowSize * 1/2, windowSize * 1/3, windowSize * 1/8);
      
            break;
         case 1:
            g.setColor(Color.BLACK);
            g.drawString("Story Mode", windowSize * 3/8, windowSize * 5/16);
            g.setColor(Color.RED);
            g.drawRect(windowSize * 3/8 - 20, windowSize * 1/4, windowSize * 1/3, windowSize * 1/8);
            g.setColor(Color.BLACK);
            g.drawString("Endless Mode", windowSize * 3/8, windowSize * 9/16);
            g.drawRect(windowSize * 3/8 - 20, windowSize * 1/2, windowSize * 1/3, windowSize * 1/8);
      
            break;
         default:
            g.setColor(Color.BLACK);
            g.drawString("story Mode", windowSize * 3/8, windowSize * 5/16);
            g.drawRect(windowSize * 3/8 - 20, windowSize * 1/4, windowSize * 1/3, windowSize * 1/8);
            g.drawString("Endless Mode", windowSize * 3/8, windowSize * 9/16);
            g.setColor(Color.RED);
            g.drawRect(windowSize * 3/8 - 20, windowSize * 1/2, windowSize * 1/3, windowSize * 1/8);
      
                        
            
      }      
   }
   
   public void drawLevelSelect(Graphics g) throws FontFormatException, IOException {
      //read star data from playerSave.txt
      try {
         File saveFile = new File("playerSave.txt");
         Scanner fileReader = new Scanner(saveFile);
         String levelStars = "";
         for (int i = 0; i < levelLayouts.size(); i++) {
            levelStars = fileReader.nextLine();
            Scanner starReader = new Scanner(levelStars);
            int level = starReader.nextInt();
            int accuracyStar = starReader.nextInt();
            int timerStar = starReader.nextInt();
            accuracyStars[level-1] = accuracyStar;
            timerStars[level-1] = timerStar;
         }
      } catch (FileNotFoundException e) {
         System.err.println("Could not read player save data");
      }
      g.drawImage(levelSelect, 0, 0, windowSize, windowSize, null);
      if (accuracyStarsOn) {
         g.drawImage(redAccuracySymbol, windowSize * 8/10, windowSize * 1/20, windowSize * 1/20, windowSize * 1/20, null);
         g.drawImage(timerSymbol, windowSize * 17/20, windowSize * 1/20, windowSize * 1/20, windowSize * 1/20, null);
      } else {
         g.drawImage(accuracySymbol, windowSize * 8/10, windowSize * 1/20, windowSize * 1/20, windowSize * 1/20, null);
         g.drawImage(redTimerSymbol, windowSize * 17/20, windowSize * 1/20, windowSize * 1/20, windowSize * 1/20, null);
      }
      int currLevel = 0;
      int x = windowSize * 3 / 20;
      int y = windowSize / 5; 
      for (int[][] layout : levelLayouts) { 
         int tileSize = windowSize / 10;
         x = ((currLevel % 4) * 2 * tileSize + windowSize * 3/20);
         y = ((currLevel) / 4 + 1) * windowSize / 5;
         if (layout.length > 10 && windowSize < 1000) tileSize = tileSize * 4/5;
         int miniTileSize = tileSize / layout.length;
         for (int row = 0; row < layout.length; row++) {
            for (int col = 0; col < layout.length; col++) {
               if (layout[row][col] == 1) {
                  g.setColor(Color.BLACK);
                  g.fillRect(x + col * miniTileSize, y + row * miniTileSize, miniTileSize, miniTileSize);
               } else if (layout[row][col] == 2) {
                  g.setColor(Color.ORANGE);
                  g.fillRect(x + col * miniTileSize, y + row * miniTileSize, miniTileSize, miniTileSize);
               } else if (layout[row][col] == 3) {
                  g.setColor(Color.GRAY);
                  g.fillRect(x + col * miniTileSize, y + row * miniTileSize, miniTileSize, miniTileSize);
               }
            }
         }
         currLevel++;
         if (currLevel == levelHovered) {
            g.setColor(Color.RED);
            g.drawRect(x, y, tileSize, tileSize);
            g.setColor(Color.BLACK);
         } else { 
            g.setColor(Color.BLACK);
            g.drawRect(x, y, tileSize, tileSize);
         }
         if (layout.length > 10 && windowSize < 1000) tileSize = tileSize * 5/4;
         g.setColor(Color.BLACK);
         Font tankFont = Font.createFont(Font.TRUETYPE_FONT, new File("zagreb_underground.ttf")).deriveFont(Font.PLAIN, 14);
         g.setFont(tankFont);
         g.drawString("Level " + currLevel, x, y - tileSize / 10);
         g.drawImage(accuracySymbol, x - tileSize * 1 / 10, y + tileSize * 21/20, tileSize * 1/5, tileSize * 1/5, null);
         g.drawImage(timerSymbol, x - tileSize * 1 / 10, y + tileSize * 25/20, tileSize * 1/5, tileSize * 1/5, null);
         for (int i = 0; i < 3; i++) {
            if (accuracyStars[currLevel-1] > i) {
               g.drawImage(starSymbol, x + tileSize / 5 + (i * tileSize / 4), y + tileSize * 21/20, tileSize * 1/5, tileSize * 1/5, null);
            } else {
               g.drawImage(blackStarSymbol, x + tileSize / 5 + (i * tileSize / 4), y + tileSize * 21/20, tileSize * 1/5, tileSize * 1/5, null);
            }
            if (timerStars[currLevel-1] > i) {
               g.drawImage(starSymbol, x + tileSize / 5 + (i * tileSize / 4), y + tileSize * 25/20, tileSize * 1/5, tileSize * 1/5, null);
            } else {
               g.drawImage(blackStarSymbol, x + tileSize / 5 + (i * tileSize / 4), y + tileSize * 25/20, tileSize * 1/5, tileSize * 1/5, null);
            }
         }
      }
   }
   
   public void drawGameOver(Graphics g) {
      if (frameCount > 9 && frameRepeat > 0) {
         frameRepeat--;
         frameCount = 0;
      }
      try {
         if (gameOverScreen1 == null) {
            gameOverScreen1 = ImageIO.read(new File("gameOver1.png"));
            gameOverScreen2 = ImageIO.read(new File("gameOver2.png"));
            gameOverScreen3 = ImageIO.read(new File("gameOver3.png"));
         }
         if (frameCount % 3 == 2) {
            g.drawImage(gameOverScreen1, 0, 0, windowSize, windowSize, null);
         } else if (frameCount % 3 == 1) {
            g.drawImage(gameOverScreen2, 0, 0, windowSize, windowSize, null);
         } else {
            g.drawImage(gameOverScreen3, 0, 0, windowSize, windowSize, null);
         }
      } catch (IOException e) {
      
      }
   }
   
   public void drawWhiteScreen(int frameNum, Graphics g) {
      switch (frameNum) {
         case 0:
            g.drawImage(mainMenuWhite1, 0, 0, windowSize, windowSize, null);
            break;
         case 1:
            g.drawImage(mainMenuWhite2, 0, 0, windowSize, windowSize, null);
            break;
         case 2:
            g.drawImage(mainMenuWhite3, 0, 0, windowSize, windowSize, null);
            break;
         case 3:
            g.drawImage(mainMenuWhite4, 0, 0, windowSize, windowSize, null);
            break;
         case 4:
            g.drawImage(mainMenuWhite5, 0, 0, windowSize, windowSize, null);
            break;
         case 5:
            g.drawImage(mainMenuWhite6, 0, 0, windowSize, windowSize, null);
            break;
         default:
            g.setColor(Color.WHITE);
            g.fillRect(0, 0, windowSize, windowSize);
      }
   }
   
   private void drawLayout(Graphics g) throws FontFormatException {
      try {
         if (woodTile == null) { 
            woodTile = ImageIO.read(new File("woodTile.png"));
         }
         int layoutSize = layout.length;
         int tileSize = windowSize / layoutSize;
         for (int i = 0; i < layoutSize; i++) {
            for (int j = 0; j < layoutSize; j++) {
               if (layout[i][j] == 1) {
                  g.drawImage(woodTile, j * tileSize, i * tileSize, tileSize, tileSize, null);
               } else if (layout[i][j] == 2) {
                  g.drawImage(mudTile, j * tileSize, i * tileSize, tileSize, tileSize, null);
               } else if (layout[i][j] == 3) {
                  g.drawImage(holeTile, j * tileSize, i * tileSize, tileSize, tileSize, null);
               } 
            }
         }
         for (int i = 0; i < playerLives; i++) {
            g.drawImage(player.getTankImage1(), 60 + i * 30, 15, player.getSize() / 2, player.getSize() / 2, null);
            g.drawImage(player.getBarrelImage1(), 60 + i * 30, 15, player.getSize() / 2, player.getSize() / 2, null);
         }
         
         //Draw control instructions
         g.setColor(Color.BLACK);
         Font tankFont = Font.createFont(Font.TRUETYPE_FONT, new File("zagreb_underground.ttf")).deriveFont(Font.PLAIN, 12);
         g.setFont(tankFont);
         g.drawString("Controls", 75, 250);
         g.drawString("Movement -> WASD", 75, 270);
         g.drawString("Shoot -> Space", 75, 290);
         g.drawString("Pause -> P or ESC", 75, 310);
         g.drawString("Return to Level Select -> R", 75, 330);
         
         long minutes = (System.currentTimeMillis() - levelTimeStart) / 1000 / 60;
         long seconds = (System.currentTimeMillis() - levelTimeStart) / 1000 % 60;
         g.setColor(Color.WHITE);
         if (endlessModeActive) {
            g.setColor(Color.BLACK);
            g.drawString("Enemies Killed: " + enemyCount, windowSize * 3/5, 35);
         } else if (accuracyStarsOn) {
            g.drawString("Missiles Fired: " + missilesFired, windowSize * 3/5, 35);  
         } else {
            g.drawString("Time: " + String.format("%02d", minutes) + ":" + String.format("%02d", seconds), windowSize * 4/5, 35);
         }
      } catch (IOException e) { }
   }
   
   public void drawExplosion(Graphics g) {
      try {
         if (explosionImage1 == null) {
            explosionImage1 = ImageIO.read(new File("explosion1.png"));
            explosionImage2 = ImageIO.read(new File("explosion2.png"));
            explosionImage3 = ImageIO.read(new File("explosion3.png"));
            explosionImage4 = ImageIO.read(new File("explosion4.png"));
            explosionImage5 = ImageIO.read(new File("explosion5.png"));
            explosionImage6 = ImageIO.read(new File("explosion6.png"));
            explosionImage7 = ImageIO.read(new File("explosion7.png"));
            explosionImage8 = ImageIO.read(new File("explosion8.png"));
         }  
         switch (frameCount) {
            case 0:
               g.drawImage(explosionImage1, 0, 0, windowSize, windowSize, null);
               break;
            case 1:
               g.drawImage(explosionImage2, 0, 0, windowSize, windowSize, null);
               break;
            case 2:
               g.drawImage(explosionImage3, 0, 0, windowSize, windowSize, null);
               break;
            case 3:
               g.drawImage(explosionImage4, 0, 0, windowSize, windowSize, null);
               break;
            case 4:
               g.drawImage(explosionImage5, 0, 0, windowSize, windowSize, null);
               break;
            case 5:
               g.drawImage(explosionImage6, 0, 0, windowSize, windowSize, null);
               break;
            case 6:
               g.drawImage(explosionImage7, 0, 0, windowSize, windowSize, null);
               break;
            case 7:
               g.drawImage(explosionImage8, 0, 0, windowSize, windowSize, null);
               break;
            default:
               g.setColor(Color.WHITE);
               g.fillRect(0, 0, frame.getWidth(), frame.getHeight());
         }
      } catch (IOException e) { }
   }
   
   private void drawTankExplosions(Graphics g) {
      ArrayList<Explosion> toRemove = new ArrayList<>();
      for (Explosion explosion: explosions) {
         explosion.draw(g);
         if (explosion.frame > 9) { toRemove.add(explosion); }
      }
      for (Explosion explosion: toRemove) {
         explosions.remove(explosion);
      }
   }
   
   private void drawLevel1(Graphics g) {
      if (frameCount == 0) {   
         frameRepeat--;
      }
      if (frameRepeat > 0) {
         switch (frameCount % 3) {
            case 0:
               g.drawImage(level1Image1, 0, 0, windowSize, windowSize, null);
               break;
            case 1:
               g.drawImage(level1Image2, 0, 0, windowSize, windowSize, null);
               break;
            default:
               g.drawImage(level1Image3, 0, 0, windowSize, windowSize, null);
         }
      } else {
         gamePlayActive = true;
         drawLevel1 = false;
      }
   }
   
   private void drawLevel2(Graphics g) {
      if (frameCount == 0) {   
         frameRepeat--;
      }
      if (frameRepeat > 0) {
         switch (frameCount % 3) {
            case 0:
               g.drawImage(level2Image1, 0, 0, windowSize, windowSize, null);
               break;
            case 1:
               g.drawImage(level2Image2, 0, 0, windowSize, windowSize, null);
               break;
            default:
               g.drawImage(level2Image3, 0, 0, windowSize, windowSize, null);
         }
      } else {
         gamePlayActive = true;
         drawLevel2 = false;
      }
   }
   
   private void drawLevel3(Graphics g) {
      if (frameCount == 0) {   
         frameRepeat--;
      }
      if (frameRepeat > 0) {
         switch (frameCount % 3) {
            case 0:
               g.drawImage(level3Image1, 0, 0, windowSize, windowSize, null);
               break;
            case 1:
               g.drawImage(level3Image2, 0, 0, windowSize, windowSize, null);
               break;
            default:
               g.drawImage(level3Image3, 0, 0, windowSize, windowSize, null);
         }
      } else {
         gamePlayActive = true;
         drawLevel3 = false;
      }
   }
   
   private void drawOptionsMenu(Graphics g) {
      if (soundOn) {
         if (musicOn) {
            if (difficulty == 1) {
               g.drawImage(optionsSoundOnMusicOnEasy, 0, 0, windowSize, windowSize, null);
            } else if (difficulty == 2) {
               g.drawImage(optionsSoundOnMusicOnMedium, 0, 0, windowSize, windowSize, null);
            } else {
               g.drawImage(optionsSoundOnMusicOnHard, 0, 0, windowSize, windowSize, null);
            }
         } else {
            if (difficulty == 1) {
               g.drawImage(optionsSoundOnMusicOffEasy, 0, 0, windowSize, windowSize, null);
            } else if (difficulty == 2) {
               g.drawImage(optionsSoundOnMusicOffMedium, 0, 0, windowSize, windowSize, null);
            } else {
               g.drawImage(optionsSoundOnMusicOffHard, 0, 0, windowSize, windowSize, null);
            }
         }
      } else {
         if (musicOn) {
            if (difficulty == 1) {
               g.drawImage(optionsSoundOffMusicOnEasy, 0, 0, windowSize, windowSize, null);
            } else if (difficulty == 2) {
               g.drawImage(optionsSoundOffMusicOnMedium, 0, 0, windowSize, windowSize, null);
            } else {
               g.drawImage(optionsSoundOffMusicOnHard, 0, 0, windowSize, windowSize, null);
            }
         } else {
            if (difficulty == 1) {
               g.drawImage(optionsSoundOffMusicOffEasy, 0, 0, windowSize, windowSize, null);
            } else if (difficulty == 2) {
               g.drawImage(optionsSoundOffMusicOffMedium, 0, 0, windowSize, windowSize, null);
            } else {
               g.drawImage(optionsSoundOffMusicOffHard, 0, 0, windowSize, windowSize, null);
            }
         }
      }
   }
   
   private void drawLevelSummary(Graphics g) throws FontFormatException, IOException {
      if (nextLevelHovered) {
         g.drawImage(levelSummaryNextLevel, 0, 0, windowSize, windowSize, null);
      } else if (levelSelectHovered) {
         g.drawImage(levelSummaryLevelSelect, 0, 0, windowSize, windowSize, null);
      } else {
         g.drawImage(levelSummary, 0, 0, windowSize, windowSize, null);
      }
      if (accuracyStarsOn) {
         Font tankFont = Font.createFont(Font.TRUETYPE_FONT, new File("zagreb_underground.ttf")).deriveFont(Font.PLAIN, 15);
         g.setFont(tankFont);
         g.drawString("Enemy Tanks Killed: " + enemyCount, windowSize * 1/4, windowSize * 3/ 8);
         g.drawString("Missiles Fired: " + missilesFired, windowSize * 1/4, windowSize * 7/ 16);
         g.drawString("Accuracy: " + (int) (((double) enemyCount / (double) missilesFired) * 100) + "%", windowSize * 1/4, windowSize / 2);
         for (int i = 0; i < 3; i++) {
            if (currStar > i) {
               g.drawImage(starSymbol, windowSize * 3/10 + (i * windowSize * 3/ 20), windowSize * 11/20 , windowSize / 10, windowSize / 10, null);
            } else {
               g.drawImage(blackStarSymbol, windowSize * 3/10 + (i * windowSize * 3 / 20), windowSize * 11/20 , windowSize / 10, windowSize / 10, null);
            }
         }

      } else {
         g.drawString("Enemy Tanks Killed: " + enemyCount, windowSize * 1/4, windowSize * 3 / 8);
         g.drawString("Time Elapsed: " + String.format("%02d", (levelTimeEnd - levelTimeStart) / 1000 / 60)
             + ":" + String.format("%02d", (levelTimeEnd - levelTimeStart) / 1000 % 60), windowSize * 1/4, windowSize * 7/ 16);
         for (int i = 0; i < 3; i++) {
            if (currStar > i) {
               g.drawImage(starSymbol, windowSize * 3/10 + (i * windowSize * 3/ 20), windowSize * 11/20 , windowSize / 10, windowSize / 10, null);
            } else {
               g.drawImage(blackStarSymbol, windowSize * 3/10 + (i * windowSize  * 3/ 20), windowSize * 11/20 , windowSize / 10, windowSize / 10, null);
            }
         }
      
      }
   }
   
   private void generateTankRotationImages(Tank tank, BufferedImage[][] imageArrays) {
      //Preprocess tank rotation images to reduce load on animation
      BufferedImage enemySprite1 = tank.getTankImage1();
      BufferedImage enemySprite2 = tank.getTankImage2();
      BufferedImage enemySprite3 = tank.getTankImage3();
      double locationX = enemySprite1.getWidth() / 2; 
      double locationY = enemySprite1.getHeight() / 2;
      for (int i = 0; i < 360; i++) {
         double rotationRequired = Math.toRadians(i);
         AffineTransform tx = AffineTransform.getRotateInstance(rotationRequired, locationX, locationY);
         AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
         BufferedImage enemySprite1rotate = op.filter(enemySprite1, null);
         BufferedImage enemySprite2rotate = op.filter(enemySprite2, null);
         BufferedImage enemySprite3rotate = op.filter(enemySprite3, null);
         imageArrays[0][i] = enemySprite1rotate;
         imageArrays[1][i] = enemySprite2rotate;
         imageArrays[2][i] = enemySprite3rotate;
      }

   }
   
   private void generateBarrelRotationImages(Tank tank, BufferedImage[][] imageArrays) {
      //Preprocess barrel rotation images to reduce load on animation
      BufferedImage barrelSprite1 = tank.getBarrelImage1();
      BufferedImage barrelSprite2 = tank.getBarrelImage2();
      BufferedImage barrelSprite3 = tank.getBarrelImage3();
      double locationX = barrelSprite1.getWidth() / 2; 
      double locationY = barrelSprite1.getHeight() / 2;
      for (int i = 0; i < 360; i++) {
         double rotationRequired = Math.toRadians(i);
         AffineTransform tx = AffineTransform.getRotateInstance(rotationRequired, locationX, locationY);
         AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
         BufferedImage barrelSprite1rotate = op.filter(barrelSprite1, null);
         BufferedImage barrelSprite2rotate = op.filter(barrelSprite2, null);
         BufferedImage barrelSprite3rotate = op.filter(barrelSprite3, null);
         imageArrays[0][i] = barrelSprite1rotate;
         imageArrays[1][i] = barrelSprite2rotate;
         imageArrays[2][i] = barrelSprite3rotate;
      }
   }
   
   private void updateTankBarrelImage(Tank tank, int xFollow, int yFollow, BufferedImage[][] imageArrays) {
      //Update barrel direction
      int followX = xFollow, followY = yFollow;
      int tankX = tank.x, tankY = tank.y;
      double relX = followX - tankX, relY = followY - tankY;
      double angle = Math.atan(relY / relX);
      if (relX < 0) {
         angle += Math.PI;
      }
      int rotationRequired = (int) Math.toDegrees(angle + 4*Math.PI);
      int degreeIndex =  rotationRequired % 360;
      if (degreeIndex == -1) { degreeIndex = 270; }
      BufferedImage barrelSprite1rotate = imageArrays[0][degreeIndex];
      BufferedImage barrelSprite2rotate = imageArrays[1][degreeIndex];
      BufferedImage barrelSprite3rotate = imageArrays[2][degreeIndex];
      tank.setBarrelImage(barrelSprite1rotate, barrelSprite2rotate, barrelSprite3rotate);
   }
   
   private void checkCollisions() {
      ArrayList<Missile> toRemove = new ArrayList<>();
      boolean clearMissiles = false;
      //Check for missile collisions with other missiles
      for (Missile missile: missiles) {
         for (Missile missile2: missiles) {
            if (missile.x != missile2.x || missile.y != missile2.y) {
               if (Math.sqrt((missile.x - missile2.x)*(missile.x - missile2.x) + (missile.y - missile2.y)*(missile.y - missile2.y)) < 10) {
                  if (missile.is_enemy != missile2.is_enemy) {
                     toRemove.add(missile);
                     toRemove.add(missile2);
                     if (soundOn) {
                        player.playExplosion();
                     }
                  }
               }
            }
         }
      }
      
      //Check for missile collisions with walls
      for (Missile missile: missiles) {
         if (missile.getX() < 0 || missile.getX() > 1000 || missile.getY() < 0 || missile.getY() > 1000) {
            toRemove.add(missile);
            continue;
         }
         int layoutSize = layout.length;
         int tileSize = windowSize / layoutSize;
         int tileX = ((int) (missile.getX() + missile.getDX())) / tileSize, tileY = ((int) (missile.getY() + missile.getDY())) / tileSize;
         if (layout[tileY][tileX] == 1) {
            if (missile.getBounces() > 0) {
               double mDX = missile.getDX();
               double mDY = missile.getDY();
               //Change velocity vector based on collision trajectory
               if ((layout[(int) (missile.getY() / tileSize)][tileX] == 1 &&
                  layout[tileY][(int) (missile.getX() / tileSize)] == 1)) {
                  if (missile.getY() % 50.0 > .3) {
                     missile.y -= 2 * mDY;
                  }
                  if (missile.getX() % 50.0 > .3) {
                     missile.x -= 2 * mDX;
                  }
                  if (Math.abs(mDX) > Math.abs(mDY)) {
                     missile.setDX(-missile.getDX());
                  } else {
                     missile.setDY(-missile.getDY());
                  }
               }
               else if (layout[(int) (missile.getY() / tileSize)][tileX] == 1) {
                  if (missile.getY() % 50.0 > .3) {
                     missile.y -= 2 * mDY;
                  }
                  missile.setDX(-missile.getDX());
               } else {
                  if (missile.getX() % 50.0 > .3) {
                     missile.x -= 2 * mDX;
                  }
                  missile.setDY(-missile.getDY());
               }
               
               missile.setBounces(missile.getBounces()-1);
               double relX = missile.getDX(), relY = missile.getDY();
               double angle = Math.atan(relY / relX);
               if (relX < 0) {
                  angle += Math.PI;
               }
               try {
                  if (missileSprite1 == null) {
                     missileSprite1 = ImageIO.read(new File("missile1.png"));
                     missileSprite2 = ImageIO.read(new File("missile2.png"));
                     missileSprite3 = ImageIO.read(new File("missile3.png"));
                  }
                  
                  double rotationRequired = angle;
                  double locationX = missileSprite1.getWidth() / 2;
                  double locationY = missileSprite1.getHeight() / 2;
                  AffineTransform tx = AffineTransform.getRotateInstance(rotationRequired, locationX, locationY);
                  AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
                  BufferedImage missileSprite1rotate = op.filter(missileSprite1, null);
                  BufferedImage missileSprite2rotate = op.filter(missileSprite2, null);
                  BufferedImage missileSprite3rotate = op.filter(missileSprite3, null);
                  missile.setSprites(missileSprite1rotate, missileSprite2rotate, missileSprite3rotate);
               } catch (IOException x) { }

            } else {
               toRemove.add(missile);
            }
         }
      }
      for (Missile missile: toRemove) {
         missiles.remove(missile);
      }  
      toRemove.clear();
      
      ArrayList<Tank> toRemoveTank = new ArrayList<>();
      for (Missile missile: missiles) {
         if (!missile.is_enemy) {
            //Check for missile collisions with enemies
            for (Tank enemy: enemies) {
               //If missile is close, remove both
               double diffX = Math.abs(missile.getX() - enemy.getX()), diffY = Math.abs(missile.getY() - enemy.getY());
               if (Math.sqrt(diffX * diffX + diffY * diffY) < 30) {
                  explosions.add(new Explosion(enemy.x - enemy.getSize() / 2, enemy.y - enemy.getSize() / 2, enemy.getBarrelImage1(), enemy.getTankImage1()));
                  toRemoveTank.add(enemy);
                  toRemove.add(missile);
                  if (soundOn) {
                     enemy.playExplosion();
                  }
                  if (endlessModeActive) {
                     enemyCount++;
                  }
               }
            }
         }   
         //Check enemy missile collision with player
         if (missile.is_enemy) {
            double diffX = Math.abs(missile.getX() - player.getX()), diffY = Math.abs(missile.getY() - player.getY());
            if (Math.sqrt(diffX * diffX + diffY * diffY) < 30) {
               System.out.println("WE'VE BEEN HIT!! AHHRRGGHHBBLRAGGRGGGgghh....");
               toRemove.add(missile);
               playerLives--;
               int formerX = player.x;
               int formerY = player.y;
               if (level >= 1 && level <= 5) {
                  player.x = 100;
                  player.y = 100;
               } else if (endlessModeActive) {
                  //Reduce enemies to 1
                  while (enemies.size() > 1) {
                     enemies.remove(enemies.get(enemies.size()-1));
                  }
                  player.x = 500;
                  player.y = 500;
               } else {
                  player.x = 100;
                  player.y = 250;
               }
               clearMissiles = true;
               if (playerLives == 0) {
                  player.x = formerX;
                  player.y = formerY;
                  gamePlayActive = false;
                  frameCount = 0;
                  drawExplosion = true;
                  gameOverScreen = true;
                  frameRepeat = 4;
                  up_pressed = false;
                  down_pressed = false;
                  left_pressed = false;
                  right_pressed = false;
               }
               break;
            }
         }
      }
      if (clearMissiles) { missiles.clear(); }
      for (Missile missile: toRemove) {
         missiles.remove(missile);
      }
      for (Tank enemy: toRemoveTank) {
         enemies.remove(enemy);
      }
   }
   
   private void updatePlayer() {
      int tileSize = windowSize / layout.length;
      boolean stuckInMud = false;
      if ((player.x < 20 && left_pressed) || (player.x > 980 && right_pressed) || (player.y < 20 && up_pressed) || (player.y > 800 && down_pressed)) {
         return;
      }
      if (layout[(int) (player.getY() / tileSize)][(int) (player.getX() / tileSize)] == 2) {
         playerSpeed = playerSpeed / 2;
         stuckInMud = true;
      }
      if (down_pressed && right_pressed && layout[(int) ((player.getY() + 20) / tileSize)][(int) ((player.getX() + 20) / tileSize)] != 1 && 
         layout[(int) ((player.getY() + 15) / tileSize)][(int) ((player.getX()) / tileSize)] != 1 &&
         layout[(int) ((player.getY()) / tileSize)][(int) ((player.getX() + 15) / tileSize)] != 1 && 
          layout[(int) ((player.getY() + 20) / tileSize)][(int) ((player.getX() + 20) / tileSize)] != 3 && 
         layout[(int) ((player.getY() + 15) / tileSize)][(int) ((player.getX()) / tileSize)] != 3 &&
         layout[(int) ((player.getY()) / tileSize)][(int) ((player.getX() + 15) / tileSize)] != 3) {
         player.direction = Tank.Cardinal.SOUTHEAST;
         player.y += (int) playerSpeed / Math.sqrt(2);
         player.x += (int) playerSpeed / Math.sqrt(2);
      } else if (down_pressed && left_pressed && layout[(int) ((player.getY() + 20) / tileSize)][(int) ((player.getX() - 20) / tileSize)] != 1 && 
         layout[(int) ((player.getY() + 15) / tileSize)][(int) ((player.getX()) / tileSize)] != 1 &&
         layout[(int) ((player.getY()) / tileSize)][(int) ((player.getX() - 15) / tileSize)] != 1
         && layout[(int) ((player.getY() + 20) / tileSize)][(int) ((player.getX() - 20) / tileSize)] != 3 && 
         layout[(int) ((player.getY() + 15) / tileSize)][(int) ((player.getX()) / tileSize)] != 3 &&
         layout[(int) ((player.getY()) / tileSize)][(int) ((player.getX() - 15) / tileSize)] != 3) {
         player.direction = Tank.Cardinal.SOUTHWEST;
         player.y += (int) playerSpeed / Math.sqrt(2);
         player.x -= (int) playerSpeed / Math.sqrt(2);
      } else if (down_pressed && layout[(int) ((player.getY() + 15) / tileSize)][(int) ((player.getX()) / tileSize)] != 1 && 
         layout[(int) ((player.getY() + 15) / tileSize)][(int) ((player.getX() + 15) / tileSize)] != 1 &&
         layout[(int) ((player.getY() + 15) / tileSize)][(int) ((player.getX() - 15) / tileSize)] != 1 &&
         layout[(int) ((player.getY() + 15) / tileSize)][(int) ((player.getX()) / tileSize)] != 3 && 
         layout[(int) ((player.getY() + 15) / tileSize)][(int) ((player.getX() + 15) / tileSize)] != 3 &&
         layout[(int) ((player.getY() + 15) / tileSize)][(int) ((player.getX() - 15) / tileSize)] != 3) {
         player.direction = Tank.Cardinal.SOUTH;   
         player.y += playerSpeed;
      } else if (up_pressed && right_pressed && layout[(int) ((player.getY() - 20) / tileSize)][(int) ((player.getX() + 20) / tileSize)] != 1 && 
         layout[(int) ((player.getY() - 15) / tileSize)][(int) ((player.getX()) / tileSize)] != 1 &&
         layout[(int) ((player.getY()) / tileSize)][(int) ((player.getX() + 15) / tileSize)] != 1 &&
         layout[(int) ((player.getY() - 20) / tileSize)][(int) ((player.getX() + 20) / tileSize)] != 3 && 
         layout[(int) ((player.getY() - 15) / tileSize)][(int) ((player.getX()) / tileSize)] != 3 &&
         layout[(int) ((player.getY()) / tileSize)][(int) ((player.getX() + 15) / tileSize)] != 3) {
         player.direction = Tank.Cardinal.NORTHEAST;
         player.y -= (int) playerSpeed / Math.sqrt(2);
         player.x += (int) playerSpeed / Math.sqrt(2);
      } else if (up_pressed && left_pressed && layout[(int) ((player.getY() - 20) / tileSize)][(int) ((player.getX() - 20) / tileSize)] != 1 && 
         layout[(int) ((player.getY() - 15) / tileSize)][(int) ((player.getX()) / tileSize)] != 1 &&
         layout[(int) ((player.getY()) / tileSize)][(int) ((player.getX() - 15) / tileSize)] != 1 &&
         layout[(int) ((player.getY() - 20) / tileSize)][(int) ((player.getX() - 20) / tileSize)] != 3 && 
         layout[(int) ((player.getY() - 15) / tileSize)][(int) ((player.getX()) / tileSize)] != 3 &&
         layout[(int) ((player.getY()) / tileSize)][(int) ((player.getX() - 15) / tileSize)] != 3) {
         player.direction = Tank.Cardinal.NORTHWEST;
         player.y -= (int) playerSpeed / Math.sqrt(2);
         player.x -= (int) playerSpeed / Math.sqrt(2);
      } else if (up_pressed && layout[(int) ((player.getY() - 15) / tileSize)][(int) ((player.getX()) / tileSize)] != 1 && 
         layout[(int) ((player.getY() - 15) / tileSize)][(int) ((player.getX() - 15) / tileSize)] != 1 &&
         layout[(int) ((player.getY() - 15) / tileSize)][(int) ((player.getX() + 15) / tileSize)] != 1 &&
         layout[(int) ((player.getY() - 15) / tileSize)][(int) ((player.getX()) / tileSize)] != 3 && 
         layout[(int) ((player.getY() - 15) / tileSize)][(int) ((player.getX() - 15) / tileSize)] != 3 &&
         layout[(int) ((player.getY() - 15) / tileSize)][(int) ((player.getX() + 15) / tileSize)] != 3) {
         player.direction = Tank.Cardinal.NORTH;
         player.y -= playerSpeed;
      } else if (right_pressed && layout[(int) ((player.getY()) / tileSize)][(int) ((player.getX() + 15) / tileSize)] != 1 && 
         layout[(int) ((player.getY() + 15) / tileSize)][(int) ((player.getX() + 15) / tileSize)] != 1 &&
         layout[(int) ((player.getY() - 15) / tileSize)][(int) ((player.getX() + 15) / tileSize)] != 1 &&
         layout[(int) ((player.getY()) / tileSize)][(int) ((player.getX() + 15) / tileSize)] != 3 && 
         layout[(int) ((player.getY() + 15) / tileSize)][(int) ((player.getX() + 15) / tileSize)] != 3 &&
         layout[(int) ((player.getY() - 15) / tileSize)][(int) ((player.getX() + 15) / tileSize)] != 3) {
         player.direction = Tank.Cardinal.EAST; 
         player.x += playerSpeed;  
      } else if (left_pressed && layout[(int) ((player.getY()) / tileSize)][(int) ((player.getX() - 15) / tileSize)] != 1 && 
         layout[(int) ((player.getY() + 15) / tileSize)][(int) ((player.getX() - 15) / tileSize)] != 1 &&
         layout[(int) ((player.getY() - 15) / tileSize)][(int) ((player.getX() - 15) / tileSize)] != 1 &&
         layout[(int) ((player.getY()) / tileSize)][(int) ((player.getX() - 15) / tileSize)] != 3 && 
         layout[(int) ((player.getY() + 15) / tileSize)][(int) ((player.getX() - 15) / tileSize)] != 3 &&
         layout[(int) ((player.getY() - 15) / tileSize)][(int) ((player.getX() - 15) / tileSize)] != 3) {
         player.direction = Tank.Cardinal.EAST;  
         player.x -= playerSpeed; 
      } 
      if (stuckInMud) {
         playerSpeed *= 2;
      }
   }
   
   private void activateEnemy(Tank enemy) {
      if (enemy.x > 0 && enemy.x < 1000 && enemy.y > 0 && enemy.y < 1000 && enemy.canSee(player, layout, windowSize / layout[0].length)) {
         double missileDX, missileDY;
         int targetX = player.x, targetY = player.y;
         int shooterX = enemy.x, shooterY = enemy.y;
         double relX = targetX - shooterX, relY = targetY - shooterY;
         double angle = Math.atan(relY / relX);
         if (relX < 0) {
            angle += Math.PI;
         }
         double missileX = enemy.x + enemy.getSize() / 2 * Math.cos(angle);
         double missileY = enemy.y + enemy.getSize() / 2 * Math.sin(angle);
         missileDX = difficulty * missileSpeed * Math.cos(angle);
         missileDY = difficulty * missileSpeed * Math.sin(angle);
         if (System.currentTimeMillis() - enemy.lastFiredTime > 2000) { 
            enemy.lastFiredTime = System.currentTimeMillis();
            try {
               if (missileSprite1 == null) {
                  missileSprite1 = ImageIO.read(new File("missile1.png"));
                  missileSprite2 = ImageIO.read(new File("missile2.png"));
                  missileSprite3 = ImageIO.read(new File("missile3.png"));
               }
               
               double rotationRequired = angle;
               double locationX = missileSprite1.getWidth() / 2;
               double locationY = missileSprite1.getHeight() / 2;
               AffineTransform tx = AffineTransform.getRotateInstance(rotationRequired, locationX, locationY);
               AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
               BufferedImage missileSprite1rotate = op.filter(missileSprite1, null);
               BufferedImage missileSprite2rotate = op.filter(missileSprite2, null);
               BufferedImage missileSprite3rotate = op.filter(missileSprite3, null);
               if (enemy.rocket_ai) {
                  Missile toShoot = new Missile(missileX, missileY, 2 * missileDX, 2 * missileDY, true, missileSprite1rotate, missileSprite2rotate, missileSprite3rotate);
                  toShoot.bouncesLeft = 3;
                  missiles.add(toShoot);
                  if (soundOn) {
                     enemy.playExplosion();
                  }
               } else if (enemy.tripleshot_ai) {
                  Missile toShoot = new Missile(missileX + missileDX, missileY + missileDY, missileDX, missileDY, true, missileSprite1rotate, missileSprite2rotate, missileSprite3rotate);
                  toShoot.bouncesLeft = 1;
                  missiles.add(toShoot);
                  AffineTransform tx2 = AffineTransform.getRotateInstance(rotationRequired + Math.PI/6, locationX, locationY);
                  AffineTransformOp op2 = new AffineTransformOp(tx2, AffineTransformOp.TYPE_BILINEAR);
                  BufferedImage missileSprite1rotate2 = op2.filter(missileSprite1, null);
                  BufferedImage missileSprite2rotate2 = op2.filter(missileSprite2, null);
                  BufferedImage missileSprite3rotate2 = op2.filter(missileSprite3, null);
                  double missileDX2 = missileSpeed * difficulty * Math.cos(angle + Math.PI / 6);
                  double missileDY2 = missileSpeed * difficulty * Math.sin(angle + Math.PI / 6);
                  Missile toShoot2 = new Missile(missileX + missileDX2, missileY + missileDY2, missileDX2, missileDY2, true, missileSprite1rotate2, missileSprite2rotate2, missileSprite3rotate2);
                  toShoot2.bouncesLeft = 1;
                  missiles.add(toShoot2);
                  AffineTransform tx3 = AffineTransform.getRotateInstance(rotationRequired - Math.PI/6, locationX, locationY);
                  AffineTransformOp op3 = new AffineTransformOp(tx3, AffineTransformOp.TYPE_BILINEAR);
                  BufferedImage missileSprite1rotate3 = op3.filter(missileSprite1, null);
                  BufferedImage missileSprite2rotate3 = op3.filter(missileSprite2, null);
                  BufferedImage missileSprite3rotate3 = op3.filter(missileSprite3, null);
                  double missileDX3 = missileSpeed * difficulty * Math.cos(angle - Math.PI / 6);
                  double missileDY3 = missileSpeed * difficulty * Math.sin(angle - Math.PI / 6);
                  Missile toShoot3 = new Missile(missileX + missileDX3, missileY + missileDY3, missileDX3, missileDY3, true, missileSprite1rotate3, missileSprite2rotate3, missileSprite3rotate3);
                  toShoot3.bouncesLeft = 1;
                  missiles.add(toShoot3);
                  if (soundOn) {   
                     enemy.playExplosion();
                  }
               } else if (enemy.homing_ai) {
                  Missile toShoot = new Missile(missileX, missileY, missileDX / 2, missileDY / 2, true, missileSprite1rotate, missileSprite2rotate, missileSprite3rotate);
                  toShoot.bouncesLeft = 0;
                  toShoot.homing = true;
                  missiles.add(toShoot);
                  if (soundOn) {   
                     enemy.playExplosion();
                  }
               } else {
                  Missile toShoot = new Missile(missileX, missileY, missileDX, missileDY, true, missileSprite1rotate, missileSprite2rotate, missileSprite3rotate);
                  toShoot.bouncesLeft = 1;
                  missiles.add(toShoot);
                  if (soundOn) {
                     enemy.playExplosion();
                  }
               }
            } catch (IOException x) { }
         }
         if (enemy.travelling_ai) {
            int rotationRequired = (int) Math.toDegrees(angle + 4*Math.PI);
            int degreeIndex =  rotationRequired % 360;
            if (degreeIndex == -1) { degreeIndex = 270; }
            BufferedImage enemySprite1rotate = enemySpritesRotated1[degreeIndex];
            BufferedImage enemySprite2rotate = enemySpritesRotated2[degreeIndex];
            BufferedImage enemySprite3rotate = enemySpritesRotated3[degreeIndex];
            enemy.setTankImages(enemySprite1rotate, enemySprite2rotate, enemySprite3rotate);
            if (layout[(int) ((enemy.y + missileDY * 3) / (windowSize / layout.length))][(int) ((enemy.x + missileDX * 3) / (windowSize / layout.length))] == 0 ||
            layout[(int) ((enemy.y + missileDY * 3) / (windowSize / layout.length))][(int) ((enemy.x + missileDX * 3) / (windowSize / layout.length))] == 2) {
               enemy.x += missileDX / 4;
               enemy.y += missileDY / 4;
            }
         } 
      } 
      if (enemy.pathfinding_ai) {
         //Establish angle to player and missile trajectory
         double missileDX, missileDY;
         int targetX = player.x, targetY = player.y;
         int shooterX = enemy.x, shooterY = enemy.y;
         double relX = targetX - shooterX, relY = targetY - shooterY;
         double angle = Math.atan(relY / relX);
         if (relX < 0) {
            angle += Math.PI;
         }
         double missileX = enemy.x + enemy.getSize() / 2 * Math.cos(angle);
         double missileY = enemy.y + enemy.getSize() / 2 * Math.sin(angle);
         missileDX = difficulty * missileSpeed * Math.cos(angle);
         missileDY = difficulty * missileSpeed * Math.sin(angle);

         //Create rotated images for ank
         int rotationRequired = (int) Math.toDegrees(angle + 4*Math.PI);
         int degreeIndex =  rotationRequired % 360;
         if (degreeIndex == -1) { degreeIndex = 270; }
         BufferedImage enemySprite1rotate = enemySpritesRotated1[degreeIndex];
         BufferedImage enemySprite2rotate = enemySpritesRotated2[degreeIndex];
         BufferedImage enemySprite3rotate = enemySpritesRotated3[degreeIndex];
         enemy.setTankImages(enemySprite1rotate, enemySprite2rotate, enemySprite3rotate);
         //Check if you can go straight to player
         double tileSize = windowSize / layout.length;
         int[] directionChecks = new int[4];
         boolean moved = false;
         if ((enemy.x < 0 || enemy.x > windowSize || enemy.y < 0 || enemy.y > windowSize) || 
         (layout[(int) ((enemy.y + missileDY / difficulty * 7) / (windowSize / layout.length))][(int) ((enemy.x + missileDX / difficulty * 7) / (windowSize / layout.length))] == 0 ||
         layout[(int) ((enemy.y + missileDY / difficulty * 7) / (windowSize / layout.length))][(int) ((enemy.x + missileDX / difficulty * 7) / (windowSize / layout.length))] == 2)) {
            enemy.x += missileDX / difficulty;
            enemy.y += missileDY / difficulty;
            moved = true;
         } else {
            int pathDX = 0, pathDY = 0;
            int pathTargetX = (int) (player.x / tileSize), pathTargetY = (int) (player.y / tileSize);
            int sourceX = (int) ((enemy.x + missileDX / difficulty * 7) / (windowSize / layout.length));
            int sourceY = (int) ((enemy.y + missileDY / difficulty * 7) / (windowSize / layout.length));
            //Correct for out of bounds
            if (sourceX < 0) {
               sourceX = 0;
            } 
            if (sourceX >= layout.length) {
               sourceX = layout.length - 1;
            }
            if (sourceY < 0) {
               sourceY = 0;
            }
            if (sourceY >= layout.length) {
               sourceY = layout.length - 1;
            }
            int enemyTileX = (int) ((enemy.x) / (windowSize / layout.length)), 
               enemyTileY = (int) ((enemy.y) / (windowSize / layout.length));
            int wallCenterX = (int) ((2 * sourceX + 1) * tileSize / 2);
            int wallCenterY = (int) ((2 * sourceY + 1) * tileSize / 2);
            if (Math.abs(shooterX - wallCenterX) > 35 && Math.abs(missileDX) > Math.abs(missileDY)) {
               pathDY = (int) Math.signum(missileDY);
            } else {
               pathDX = (int) Math.signum(missileDX);
            }
            directionChecks = pathFind(pathTargetX, pathTargetY, sourceX, sourceY, pathDX, pathDY);
         }
         for (int i = 0; i < 4; i++) {
            if (!moved) {
               switch (directionChecks[i]) {
                  case 1:
                     //Go up
                     if (enemy.y > 1000 || ((layout[(int) ((enemy.y - 25) / tileSize)][(int) ((enemy.x) / tileSize)] == 0 ||
                     layout[(int) ((enemy.y - 25) / tileSize)][(int) ((enemy.x) / tileSize)] == 2)) && 
                     ((layout[(int) ((enemy.y - 25) / tileSize)][(int) ((enemy.x + 15) / tileSize)] == 0 ||
                     layout[(int) ((enemy.y - 25) / tileSize)][(int) ((enemy.x + 15) / tileSize)] == 2)) && 
                     ((layout[(int) ((enemy.y - 25) / tileSize)][(int) ((enemy.x - 15) / tileSize)] == 0 ||
                     layout[(int) ((enemy.y - 25) / tileSize)][(int) ((enemy.x - 15) / tileSize)] == 2))) {
                        enemySprite1rotate = enemySpritesRotated1[270];
                        enemySprite2rotate = enemySpritesRotated2[270];
                        enemySprite3rotate = enemySpritesRotated3[270];
                        enemy.setTankImages(enemySprite1rotate, enemySprite2rotate, enemySprite3rotate);
                        enemy.y -= missileSpeed;
                        moved = true;
                     }
                     break;
                  case 2:
                     //Go down
                     if (enemy.y < 0 || ((layout[(int) ((enemy.y + 25) / tileSize)][(int) ((enemy.x) / tileSize)] == 0 ||
                     layout[(int) ((enemy.y + 25) / tileSize)][(int) ((enemy.x) / tileSize)] == 2)) && 
                     ((layout[(int) ((enemy.y + 25) / tileSize)][(int) ((enemy.x + 15) / tileSize)] == 0 ||
                     layout[(int) ((enemy.y + 25) / tileSize)][(int) ((enemy.x + 15) / tileSize)] == 2)) && 
                     ((layout[(int) ((enemy.y + 25) / tileSize)][(int) ((enemy.x - 15) / tileSize)] == 0 ||
                     layout[(int) ((enemy.y + 25) / tileSize)][(int) ((enemy.x - 15) / tileSize)] == 2))) {
                        enemySprite1rotate = enemySpritesRotated1[90];
                        enemySprite2rotate = enemySpritesRotated2[90];
                        enemySprite3rotate = enemySpritesRotated3[90];
                        enemy.setTankImages(enemySprite1rotate, enemySprite2rotate, enemySprite3rotate);
                        enemy.y += missileSpeed;
                        moved = true;
                     }
                     break;
                  case 3:
                     //Go right
                     if (enemy.x < 0 || ((layout[(int) ((enemy.y) / tileSize)][(int) ((enemy.x + 25) / tileSize)] == 0 ||
                     layout[(int) ((enemy.y) / tileSize)][(int) ((enemy.x + 25) / tileSize)] == 2)) && 
                     ((layout[(int) ((enemy.y + 15) / tileSize)][(int) ((enemy.x + 25) / tileSize)] == 0 ||
                     layout[(int) ((enemy.y + 15) / tileSize)][(int) ((enemy.x + 25) / tileSize)] == 2)) && 
                     ((layout[(int) ((enemy.y - 15) / tileSize)][(int) ((enemy.x + 25) / tileSize)] == 0 ||
                     layout[(int) ((enemy.y - 15) / tileSize)][(int) ((enemy.x + 25) / tileSize)] == 2))) {
                        enemySprite1rotate = enemySpritesRotated1[0];
                        enemySprite2rotate = enemySpritesRotated2[0];
                        enemySprite3rotate = enemySpritesRotated3[0];
                        enemy.setTankImages(enemySprite1rotate, enemySprite2rotate, enemySprite3rotate);
                        enemy.x += missileSpeed;
                        moved = true;
                     }
                     break;
                  case 4:
                     //Go left
                     if (enemy.x > 1000 || ((layout[(int) ((enemy.y) / tileSize)][(int) ((enemy.x - 25) / tileSize)] == 0 ||
                     layout[(int) ((enemy.y) / tileSize)][(int) ((enemy.x - 25) / tileSize)] == 2)) && 
                     ((layout[(int) ((enemy.y - 15) / tileSize)][(int) ((enemy.x - 25) / tileSize)] == 0 ||
                     layout[(int) ((enemy.y - 15) / tileSize)][(int) ((enemy.x - 25) / tileSize)] == 2)) && 
                     ((layout[(int) ((enemy.y + 15) / tileSize)][(int) ((enemy.x - 25) / tileSize)] == 0 ||
                     layout[(int) ((enemy.y + 15) / tileSize)][(int) ((enemy.x - 25) / tileSize)] == 2))) {
                        enemySprite1rotate = enemySpritesRotated1[180];
                        enemySprite2rotate = enemySpritesRotated2[180];
                        enemySprite3rotate = enemySpritesRotated3[180];
                        enemy.setTankImages(enemySprite1rotate, enemySprite2rotate, enemySprite3rotate);
                        enemy.x -= missileSpeed;
                        moved = true;
                     }
                     break;
                  default:
                     //Go left
                     if (enemy.x > 1000 || (layout[(int) ((enemy.y) / tileSize)][(int) ((enemy.x - 25) / tileSize)] == 0 ||
                     layout[(int) ((enemy.y) / tileSize)][(int) ((enemy.x - 25) / tileSize)] == 2)) {
                        enemySprite1rotate = enemySpritesRotated1[180];
                        enemySprite2rotate = enemySpritesRotated2[180];
                        enemySprite3rotate = enemySpritesRotated3[180];
                        enemy.setTankImages(enemySprite1rotate, enemySprite2rotate, enemySprite3rotate);
                        enemy.x -= missileSpeed;
                        moved = true;
                     }
               }     
            }
         }
      }
   }
   
   private int[] pathFind(int playerX, int playerY, int wallX, int wallY, int dx, int dy) {
      if (dy == 0) {
         dy++;
         dy--;
      }
      boolean foundGap = false;
      int tempX = wallX;
      int tempY = wallY;
      while (!(layout[tempY][tempX] == 0 || layout[tempY][tempX] == 2) && !(tempX == playerX || tempY == playerY)) {
         tempX += dx;
         tempY += dy;
      }
      if (layout[tempY][tempX] == 0 || layout[tempY][tempX] == 2) {
         if (dx > 0) {
            if (playerY > wallY) {
               return new int[]{3, 4, 1, 2};
            } else {
               return new int[]{3, 4, 2, 1};
            }
         } else if (dx < 0) {
            if (playerY > wallY) {
               return new int[]{4, 3, 1, 2};
            } else {
               return new int[]{4, 3, 2, 1};
            }
         } else if (dy > 0) {
            if (playerX > wallX) {
               return new int[]{2, 1, 3, 4};
            } else {
               return new int[]{2, 1, 4, 3};
            }
         } else {
            if (playerX > wallX) {
               return new int[]{1, 2, 3, 4};
            } else {
               return new int[]{1, 2, 4, 3};
            }
         }
      }
      tempX += dx;
      tempY += dy;
      
      int backX = wallX - dx;
      int backY = wallY - dy;
      while (((tempX >= 0 && tempX < layout.length && tempY >= 0 && tempY < layout.length) &&
            !(layout[tempY][tempX] == 0 || layout[tempY][tempX] == 2)) &&
            ((backX >= 0 && backX < layout.length && backY >= 0 && backY < layout.length) &&
             !(layout[backY][backX] == 0 || layout[backY][backX] == 2)))  {
            tempX += dx;
            tempY += dy;
            backX -= dx;
            backY -= dy;
      }
      if (backX >= 0 && backX < layout.length && backY >= 0 && backY < layout.length) {
         if (layout[backY][backX] == 0 || layout[backY][backX] == 2) {
            if (dx > 0) {
               if (playerY > wallY) {
                  return new int[]{4, 3, 2, 1};
               } else {
                  return new int[]{4, 3, 1, 2};
               }
            } else if (dx < 0) {
               if (playerY > wallY) {
                  return new int[]{3, 4, 2, 1};
               } else {
                  return new int[]{3, 4, 1, 2};
               }
            } else if (dy > 0) {
               if (playerX > wallX) {
                  return new int[]{1, 2, 3, 4};
               } else {
                  return new int[]{1, 2, 4, 3};
               }
            } else {
               if (playerX > wallX) {
                  return new int[]{2, 1, 3, 4};
               } else {
                  return new int[]{2, 1, 4, 3};
               }
            }
         }
      } 
      if (tempX >= 0 && tempX < layout.length && tempY >= 0 && tempY < layout.length) {
         if (layout[tempY][tempX] == 0 || layout[tempY][tempX] == 2) {
            if (dx > 0) {
               if (playerY > wallY) {
                  return new int[]{3, 4, 2, 1};
               } else {
                  return new int[]{3, 4, 1, 2};
               }
            } else if (dx < 0) {
               if (playerY > wallY) {
                  return new int[]{4, 3, 2, 1};
               } else {
                  return new int[]{4, 3, 1, 2};
               }
            } else if (dy > 0) {
               if (playerX > wallX) {
                  return new int[]{2, 1, 3, 4};
               } else {
                  return new int[]{2, 1, 4, 3};
               }
            } else {
               if (playerX > wallX) {
                  return new int[]{1, 2, 3, 4};
               } else {
                  return new int[]{1, 2, 4, 3};
               }
            }
         }
      } 
      return new int[]{1, 2, 3, 4}; 
   }
   
   private void updateStars() {
      if (accuracyStarsOn) {
         if (missilesFired <= enemyCount) {
            currStar = 3;
         } else if (missilesFired <= enemyCount * 2) {
            currStar = 2; 
         } else {
            currStar = 1;
         }
      } else {
         if (System.currentTimeMillis() - levelTimeStart < enemyCount * 15000) {
            currStar = 3;
         } else if (System.currentTimeMillis() - levelTimeStart < 2 * enemyCount * 15000) {
            currStar = 2;
         } else {
            currStar = 1;
         } 
      }
   }
   
   private void spawnEnemy() {
      Random rand = new Random();
      int randSpawnX = rand.nextInt(1150) - 50;
      int randSpawnY = rand.nextInt(1150) - 50;
      int randSwitch = rand.nextInt(2);
      int spawnX, spawnY;
      if (randSpawnX < 1000 && randSpawnX > 0) {
         spawnX = randSpawnX;
         if (randSpawnY > 0 && randSpawnY < 1000) {
            if (randSwitch == 0) {
               randSpawnY = -25;
            } else {
               randSpawnY = 1050;
            }
         } 
         spawnY = randSpawnY;
      } else {
         spawnY = randSpawnY;
         spawnX = randSpawnX;
      }
      Tank enemy = new Tank(true, spawnX, spawnY);
      int aiSelect = rand.nextInt(3);
      switch (aiSelect) {
         case 0:
         enemy.homing_ai = true;
            break;
         case 1:
         enemy.tripleshot_ai = true;
            break;
         default:
            enemy.rocket_ai = true;
            
      }
      enemy.pathfinding_ai = true;
      enemies.add(enemy);
   }
   
   @Override
   protected void paintComponent(Graphics g) {
      super.paintComponent(g);
      
      //Check if all enemies are dead naturally
      if (enemies.isEmpty() && !modeSelectActive && !drawExplosion && !gameOverScreen && !levelSelectActive && !levelSummaryScreen) {
         frameCount = 0;
         levelTimeEnd = System.currentTimeMillis();
         drawExplosion = true;
         levelSummaryScreen = true;
         missiles.clear();
         //Update star save data
         if (accuracyStarsOn) {
            if (currStar > accuracyStars[level-1]) {
               accuracyStars[level-1] = currStar;
            }  
         } else {
            if (currStar > timerStars[level-1]) {
               timerStars[level-1] = currStar;
            }  
         }
         try {
            //Save data to file
            File saveFile = new File("playerSave.txt");
            PrintWriter printer = new PrintWriter(saveFile);
            printer.print("");
            String starString = "";
            for (int i = 0; i < levelLayouts.size(); i++) {
               starString += (i + 1) + " " + accuracyStars[i] + " " + timerStars[i] + "\n";
            }
            printer.print(starString);
            printer.close();
         } catch (FileNotFoundException e) {
            System.err.println("Could not save star data.");
         }
      }
      
      
      frameCount++;
      try {
         if (frameCount > 11) frameCount = 0;
         if (fadeToWhite) {
         
            drawWhiteScreen(frameCount, g);
            if (frameCount > 6) fadeToWhite = false;
         
         } else if (drawLevel1) {
            drawLevel1(g);
         } else if (drawLevel2) {
            drawLevel2(g);
         } else if (drawLevel3) {
            drawLevel3(g);
         } else if (drawExplosion) {
            drawLayout(g);
            player.draw(g);
            drawTankExplosions(g);
            drawExplosion(g);
            if (frameCount > 10) {
               drawExplosion = false;
               //Update the level layout
               if (gameOverScreen) {
                  frameCount = 0;
               } else if (modeSelectActive) {
                  frameCount = 0;
               }else if (levelSelectActive) {
                  frameCount = 0;
               } else if (levelSummaryScreen) {
                  frameCount = 0;
               } else if (level == 1) {
                  frameCount = 0;
                  frameRepeat = 3;
                  drawLevel2 = true;
                  level = 2;
                  layout = GameConstants.level2Layout;
                  windowSize = 500;
                  frame.setSize(515, 530); 
                  playerLives = 3;
                  player.x = 100;
                  player.y = 100;
                  enemies.clear();
                  for (Tank enemy: GameConstants.getLevel2Enemies()) {
                     enemies.add(enemy);
                  }
                  missiles.clear();
                  missilesFired = 0;
                  levelTimeStart = System.currentTimeMillis();
                  currStar = 3;
                  enemyCount = enemies.size();
               } else if (level == 2) {
                  frameCount = 0;
                  frameRepeat = 3;
                  drawLevel3 = true;
                  layout = GameConstants.level3Layout;
                  windowSize = 500;
                  frame.setSize(515, 530); 
                  playerLives = 3;
                  player.x = 100;
                  player.y = 100;
                  enemies.clear();
                  for (Tank enemy: GameConstants.getLevel3Enemies()) {
                     enemies.add(enemy);
                  }
                  missiles.clear();
                  missilesFired = 0;
                  levelTimeStart = System.currentTimeMillis();
                  currStar = 3;
                  enemyCount = enemies.size();
               } else if (level == 3) {
                  layout = GameConstants.level4Layout;
                  windowSize = 500;
                  frame.setSize(515, 530); 
                  playerLives = 3;
                  player.x = 100;
                  player.y = 100;
                  enemies.clear();
                  for (Tank enemy: GameConstants.getLevel4Enemies()) {
                     enemies.add(enemy);
                  }
                  missiles.clear();
                  missilesFired = 0;
                  levelTimeStart = System.currentTimeMillis();
                  currStar = 3;
                  enemyCount = enemies.size();
               } else if (level == 4) {
                  layout = GameConstants.level5Layout;
                  windowSize = 500;
                  frame.setSize(515, 530); 
                  playerLives = 3;
                  player.x = 100;
                  player.y = 250;
                  enemies.clear();
                  for (Tank enemy: GameConstants.getLevel5Enemies()) {
                     enemies.add(enemy);
                  }
                  missiles.clear();
                  missilesFired = 0;
                  levelTimeStart = System.currentTimeMillis();
                  currStar = 3;
                  enemyCount = enemies.size();
               } else if (level == 5) {
                  layout = GameConstants.level6Layout;
                  windowSize = 500;
                  frame.setSize(515, 530); 
                  playerLives = 3;
                  player.x = 100;
                  player.y = 250;
                  enemies.clear();
                  for (Tank enemy: GameConstants.getLevel6Enemies()) {
                     enemies.add(enemy);
                  }
                  missiles.clear();
                  missilesFired = 0;
                  levelTimeStart = System.currentTimeMillis();
                  currStar = 3;
                  enemyCount = enemies.size();
               } else if (level == 6) {
                  layout = GameConstants.level7Layout;
                  windowSize = 1000;
                  frame.setSize(1015, (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight());
                  playerLives = 3;
                  player.x = 100;
                  player.y = 250;
                  enemies.clear();
                  for (Tank enemy: GameConstants.getLevel7Enemies()) {
                     enemies.add(enemy);
                  }
                  missiles.clear();
                  missilesFired = 0;
                  levelTimeStart = System.currentTimeMillis();
                  currStar = 3;
                  enemyCount = enemies.size();
               } else if (level == 7) {
                  layout = GameConstants.level8Layout;
                  windowSize = 1000;
                  frame.setSize(1015, (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight());
                  playerLives = 3;
                  player.x = 100;
                  player.y = 250;
                  enemies.clear();
                  for (Tank enemy: GameConstants.getLevel8Enemies()) {
                     enemies.add(enemy);
                  }
                  missiles.clear();
                  missilesFired = 0;
                  levelTimeStart = System.currentTimeMillis();
                  currStar = 3;
                  enemyCount = enemies.size();
               } else if (level == 8) {
                  layout = GameConstants.level9Layout;
                  windowSize = 1000;
                  frame.setSize(1015, (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight());
                  playerLives = 3;
                  player.x = 100;
                  player.y = 250;
                  enemies.clear();
                  for (Tank enemy: GameConstants.getLevel9Enemies()) {
                     enemies.add(enemy);
                  }
                  missiles.clear();
                  missilesFired = 0;
                  levelTimeStart = System.currentTimeMillis();
                  currStar = 3;
                  enemyCount = enemies.size();
               } else if (level == 9) {
                  layout = GameConstants.level10Layout;
                  windowSize = 1000;
                  frame.setSize(1015, (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight());
                  playerLives = 3;
                  player.x = 100;
                  player.y = 250;
                  enemies.clear();
                  for (Tank enemy: GameConstants.getLevel10Enemies()) {
                     enemies.add(enemy);
                  }
                  missiles.clear();
                  missilesFired = 0;
                  levelTimeStart = System.currentTimeMillis();
                  currStar = 3;
                  enemyCount = enemies.size();
               } else if (level == 10) {
                  levelSelectActive = true;
                  gamePlayActive = false;
               }
               timer.setDelay(tickLength);
            }
         } else if (modeSelectActive) {
            drawModeSelect(g);
         } else if (levelSummaryScreen) {
            drawLevelSummary(g);
         } else if (mainMenuActive) {
            if (menuSongPlayer.getStatus() != MediaPlayer.Status.PLAYING) {
               menuSongPlayer.setCycleCount(1000000);
               menuSongPlayer.play();
            }
            drawMainMenu(frameCount, g);
            
         } else if (levelSelectActive) {
            drawLevelSelect(g);
         } else if (optionsMenuActive) {
            drawOptionsMenu(g);        
         } else if (gamePlayActive) {
            updatePlayer();
            if (endlessModeActive && 5 - 4/5.0 * Math.min((System.currentTimeMillis() - levelTimeStart) / 1000 / 60, 5.0) < 
                                    (System.currentTimeMillis() - lastEnemySpawnTime) / 1000) {
               spawnEnemy();
               lastEnemySpawnTime = System.currentTimeMillis();
            } else {
               updateStars();
            }
            if (gameSongPlayer.getStatus() != MediaPlayer.Status.PLAYING && musicOn) {
               gameSongPlayer.setCycleCount(1000000);
               menuSongPlayer.stop();
               gameSongPlayer.play();
            }
            g.setColor(Color.WHITE);
            g.fillRect(0, 0, windowSize, windowSize);
            
            drawLayout(g);
            updateTankBarrelImage(player, lastMouseX, lastMouseY, playerBarrelImages);
            player.draw(g);
            ArrayList<Missile> toRemove = new ArrayList<>();
            //Draw enemy tanks
            for (Tank enemy: enemies) {
               updateTankBarrelImage(enemy, player.getX(), player.getY(), enemyBarrelImages);
               enemy.draw(g);
               activateEnemy(enemy);
            }
            
            //Draw all missiles
            for (Missile missile: missiles) {
               missile.draw(g, player, missileSprite1, missileSprite2, missileSprite3, difficulty * missileSpeed);
               if (missile.getX() > windowSize - 50 || missile.getX() < -50 || missile.getY() < -50 || missile.getY() > windowSize - 50) {
                  toRemove.add(missile);
               }
            }  
            for (Missile missile: toRemove) {
               missiles.remove(missile);
            }
            toRemove.clear();
            
            checkCollisions();
            
            drawTankExplosions(g);
            
         } else if (gameOverScreen) {
            drawGameOver(g);
            if (frameCount > 10) {
               gameOverScreen = false;
               levelSelectActive = true;
               playerLives = 3;
            }
         }
      } catch (FontFormatException e) { }
      catch (IOException e) { }
   }
   
   
   
   public class MouseMotion extends MouseMotionAdapter {
      
      @Override 
      public void mouseMoved(MouseEvent e) {
         if (mainMenuActive) {
            if (e.getX() > 355 && e.getX() < 465 && e.getY() > 250 && e.getY() < 325) {
               mainMenu1 = mainMenu1Play;
               mainMenu2 = mainMenu2Play;
               mainMenu3 = mainMenu3Play;
            }  //y in  (225, 300) x in (340, 450) 
            else {
               mainMenu1 = mainMenu1Normal;
               mainMenu2 = mainMenu2Normal;
               mainMenu3 = mainMenu3Normal;
            }
         } else if (modeSelectActive) {
            if (e.getX() > windowSize * 3/8 - 20 && e.getX() < windowSize * 17/24 - 20 && e.getY() > windowSize / 4 + 30 && e.getY() < windowSize * 3/8 + 30) {
               levelModeHovered = 1;
            } else if (e.getX() > windowSize * 3/8 - 20 && e.getX() < windowSize * 17/24 - 20 && e.getY() > windowSize * 1/2 + 30 && e.getY() < windowSize * 5/8 + 30) {
               levelModeHovered = 2;
            } else {
               levelModeHovered = 0;
            }
         } else if (levelSummaryScreen) {
            if (e.getX() > windowSize * 3/4 && e.getX() < windowSize * 17/20 && e.getY() > windowSize * 7/10 + 30 && e.getY() < windowSize * 4/5 + 30) {
               nextLevelHovered = true;  
            }  else {
               nextLevelHovered = false;
            }  
            if (e.getX() > windowSize * 1/10 && e.getX() < windowSize / 5 && e.getY() > 7/10 + 30 && e.getY() < windowSize * 4/5 + 30) {
               levelSelectHovered = true;
            } else {
               levelSelectHovered = false;
            }
         } else if (levelSelectActive) {
            if (e.getX() > windowSize * 3/20 && e.getX() < windowSize * 3/20 + windowSize / 10 && 
               e.getY() > windowSize / 5 + 30 && e.getY() < windowSize / 5 + windowSize / 10 + 30) {
               levelHovered = 1;
            } else if (e.getX() > windowSize * 7/20 && e.getX() < windowSize * 7/20 + windowSize / 10 && 
               e.getY() > windowSize / 5 + 30 && e.getY() < windowSize / 5 + windowSize / 10 + 30) {
               levelHovered = 2;
            } else if (e.getX() > windowSize * 11/20 && e.getX() < windowSize * 11/20 + windowSize / 10 && 
               e.getY() > windowSize / 5  + 30 && e.getY() < windowSize / 5 + windowSize / 10 + 30) {
               levelHovered = 3;
            } else if (e.getX() > windowSize * 15/20 && e.getX() < windowSize * 15/20 + windowSize / 10 && 
               e.getY() > windowSize / 5 + 30 && e.getY() < windowSize / 5 + windowSize / 10 + 30) {
               levelHovered = 4;
            } else if (e.getX() > windowSize * 3/20 && e.getX() < windowSize * 3/20 + windowSize / 10 && 
               e.getY() > 2 * windowSize / 5 + 30 && e.getY() < 2 * windowSize / 5 + windowSize / 10 + 30) {
               levelHovered = 5;
            } else if (e.getX() > windowSize * 7/20 && e.getX() < windowSize * 7/20 + windowSize / 10 && 
               e.getY() > 2 * windowSize / 5 + 30 && e.getY() < 2 * windowSize / 5 + windowSize / 10 + 30) {
               levelHovered = 6;
            } else if (e.getX() > windowSize * 11/20 && e.getX() < windowSize * 11/20 + windowSize / 10 && 
               e.getY() > 2 * windowSize / 5 + 30 && e.getY() < 2 * windowSize / 5 + windowSize / 10 + 30) {
               levelHovered = 7;
            } else if (e.getX() > windowSize * 15/20 && e.getX() < windowSize * 15/20 + windowSize / 10 && 
               e.getY() > 2 * windowSize / 5 + 30 && e.getY() < 2 * windowSize / 5 + windowSize / 10 + 30) {
               levelHovered = 8;
            } else if (e.getX() > windowSize * 3/20 && e.getX() < windowSize * 3/20 + windowSize / 10 && 
               e.getY() > 3 * windowSize / 5 + 30 && e.getY() < 3 * windowSize / 5 + windowSize / 10 + 30) {
               levelHovered = 9;
            } else if (e.getX() > windowSize * 7/20 && e.getX() < windowSize * 7/20 + windowSize / 10 && 
               e.getY() > 3 * windowSize / 5 + 30 && e.getY() < 3 * windowSize / 5 + windowSize / 10 + 30) {
               levelHovered = 10;
            } else {
               levelHovered = 0;
            }
         }
         
         lastMouseX = e.getX();
         lastMouseY = e.getY();
      }
      
   }
   
   public class MouseClick extends MouseInputAdapter {
   
      @Override
      public void mouseClicked(MouseEvent e) {
         //System.out.println("X: " + e.getX() + ",  Y: " + e.getY());
         if (e.getX() > 355 && e.getX() < 465 && e.getY() > 250 && e.getY() < 325 && mainMenuActive) {
            fadeToWhite = true;
            mainMenuActive = false;
            modeSelectActive = true;
            frameCount = 0;
         }
         if (modeSelectActive) {
            if (e.getX() > windowSize * 3/8 - 20 && e.getX() < windowSize * 17/24 - 20 && e.getY() > windowSize / 4 + 30 && e.getY() < windowSize * 3/8 + 30) {
               levelSelectActive = true;
               modeSelectActive = false;
            } else if (e.getX() > windowSize * 3/8 - 20 && e.getX() < windowSize * 17/24 - 20 && e.getY() > windowSize * 1/2 + 30&& e.getY() < windowSize * 5/8 + 30) {
               endlessModeActive = true;
               levelSummaryScreen = false;
               levelSelectActive = false;
               gamePlayActive = true;
               modeSelectActive = false;
               frameCount = 0;
               frameRepeat = 3;
               level = 10;
               layout = GameConstants.levelEndlessLayout;
               windowSize = 1000;
               frame.setSize(1015, (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight()); 
               playerLives = 3;
               player.x = 500;
               player.y = 500;
               enemies.clear();
               missiles.clear();
               missilesFired = 0;
               levelTimeStart = System.currentTimeMillis();
               currStar = 3;
               spawnEnemy();
               spawnEnemy();
               spawnEnemy();
               spawnEnemy();
               enemyCount = 0;
            }
         }
         if (levelSelectActive) {
            //Change level star preference
            if (e.getX() > windowSize * 8/10 && e.getX() < windowSize * 17/20 && e.getY() > windowSize * 1/20 + 30 && e.getY() < windowSize * 1/10 + 30) {
               accuracyStarsOn = true;
            }
            if (e.getX() > windowSize * 17/20 && e.getX() < windowSize * 9/10 && e.getY() > windowSize * 1/
            20 + 30 && e.getY() < windowSize * 1/10 + 30) {
               accuracyStarsOn = false;
            }
            //Go through level select thumbnails and activate level based on selection
            if (e.getX() > windowSize * 3/20 && e.getX() < windowSize * 3/20 + windowSize / 10 && 
               e.getY() > windowSize / 5 + 30 && e.getY() < windowSize / 5 + windowSize / 10 + 30) {
               level = 1;
               drawLevel1 = true;
               gamePlayActive = true;
               levelSelectActive = false;
               playerLives = 3;
               player.x = 100;
               player.y = 100;
               layout = GameConstants.level1Layout;
               windowSize = 500;
               frame.setSize(515, 530); 
               enemies.clear();
               for (Tank enemy: GameConstants.getLevel1Enemies()) {
                  enemies.add(enemy);
               }
               missilesFired = 0;
               levelTimeStart = System.currentTimeMillis();
               currStar = 3;
               enemyCount = enemies.size();
            }
            if (e.getX() > windowSize * 7/20 && e.getX() < windowSize * 7/20 + windowSize / 10 && 
               e.getY() > windowSize / 5 + 30 && e.getY() < windowSize / 5 + windowSize / 10 + 30) {
               level = 2;
               drawLevel2 = true;
               gamePlayActive = true;
               levelSelectActive = false;
               playerLives = 3;
               player.x = 100;
               player.y = 100;
               layout = GameConstants.level2Layout;
               windowSize = 500;
               frame.setSize(515, 530); 
               enemies.clear();
               for (Tank enemy: GameConstants.getLevel2Enemies()) {
                  enemies.add(enemy);
               }
               missilesFired = 0;
               levelTimeStart = System.currentTimeMillis();
               currStar = 3;
               enemyCount = enemies.size();
            }
            if (e.getX() > windowSize * 11/20 && e.getX() < windowSize * 11/20 + windowSize / 10 && 
               e.getY() > windowSize / 5 + 30 && e.getY() < windowSize / 5 + windowSize / 10 + 30) {
               level = 3;
               drawLevel3 = true;
               gamePlayActive = true;
               levelSelectActive = false;
               playerLives = 3;
               player.x = 100;
               player.y = 100;
               layout = GameConstants.level3Layout;
               windowSize = 500;
               frame.setSize(515, 530); 
               enemies.clear();
               for (Tank enemy: GameConstants.getLevel3Enemies()) {
                  enemies.add(enemy);
               }
               missilesFired = 0;
               levelTimeStart = System.currentTimeMillis();
               currStar = 3;
               enemyCount = enemies.size();
            }
            if (e.getX() > windowSize * 15/20 && e.getX() < windowSize * 15/20 + windowSize / 10 && 
               e.getY() > windowSize / 5 + 30 && e.getY() < windowSize / 5 + windowSize / 10 + 30) {
               level = 4;
               gamePlayActive = true;
               levelSelectActive = false;
               playerLives = 3;
               player.x = 100;
               player.y = 250;
               layout = GameConstants.level4Layout;
               windowSize = 500;
               frame.setSize(515, 530); 
               enemies.clear();
               for (Tank enemy: GameConstants.getLevel4Enemies()) {
                  enemies.add(enemy);
               }
               missilesFired = 0;
               levelTimeStart = System.currentTimeMillis();
               currStar = 3;
               enemyCount = enemies.size();
            }
            if (e.getX() > windowSize * 3/20 && e.getX() < windowSize * 3/20 + windowSize / 10 && 
               e.getY() > 2 * windowSize / 5 + 30 && e.getY() < 2 * windowSize / 5 + windowSize / 10 + 30) {
               level = 5;
               gamePlayActive = true;
               levelSelectActive = false;
               playerLives = 3;
               player.x = 100;
               player.y = 250;
               layout = GameConstants.level5Layout;
               windowSize = 500;
               frame.setSize(515, 530); 
               enemies.clear();
               for (Tank enemy: GameConstants.getLevel5Enemies()) {
                  enemies.add(enemy);
               }
               missilesFired = 0;
               levelTimeStart = System.currentTimeMillis();
               currStar = 3;
               enemyCount = enemies.size();
            }
            if (e.getX() > windowSize * 7/20 && e.getX() < windowSize * 7/20 + windowSize / 10 && 
               e.getY() > 2 * windowSize / 5 + 30 && e.getY() < 2 * windowSize / 5 + windowSize / 10 + 30) {
               level = 6;
               gamePlayActive = true;
               levelSelectActive = false;
               playerLives = 3;
               player.x = 100;
               player.y = 250;
               layout = GameConstants.level6Layout;
               windowSize = 1000;
               frame.setSize(1015, (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight()); 
               enemies.clear();
               for (Tank enemy: GameConstants.getLevel6Enemies()) {
                  enemies.add(enemy);
               }
               missilesFired = 0;
               levelTimeStart = System.currentTimeMillis();
               currStar = 3;
               enemyCount = enemies.size();
            }
            if (e.getX() > windowSize * 11/20 && e.getX() < windowSize * 11/20 + windowSize / 10 && 
               e.getY() > 2 * windowSize / 5 + 30 && e.getY() < 2 * windowSize / 5 + windowSize / 10 + 30) {
               level = 7;
               gamePlayActive = true;
               levelSelectActive = false;
               playerLives = 3;
               player.x = 100;
               player.y = 250;
               layout = GameConstants.level7Layout;
               windowSize = 1000;
               frame.setSize(1015, (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight()); 
               enemies.clear();
               for (Tank enemy: GameConstants.getLevel7Enemies()) {
                  enemies.add(enemy);
               }
               missilesFired = 0;
               levelTimeStart = System.currentTimeMillis();
               currStar = 3;
               enemyCount = enemies.size();
            }
            if (e.getX() > windowSize * 15/20 && e.getX() < windowSize * 15/20 + windowSize / 10 && 
               e.getY() > 2 * windowSize / 5 + 30 && e.getY() < 2 * windowSize / 5 + windowSize / 10 + 30) {
               level = 8;
               gamePlayActive = true;
               levelSelectActive = false;
               playerLives = 3;
               player.x = 100;
               player.y = 250;
               layout = GameConstants.level8Layout;
               windowSize = 1000;
               frame.setSize(1015, (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight()); 
               enemies.clear();
               for (Tank enemy: GameConstants.getLevel8Enemies()) {
                  enemies.add(enemy);
               }
               missilesFired = 0;
               levelTimeStart = System.currentTimeMillis();
               currStar = 3;
               enemyCount = enemies.size();
            }
            if (e.getX() > windowSize * 3/20 && e.getX() < windowSize * 3/20 + windowSize / 10 && 
               e.getY() > 3 * windowSize / 5 + 30 && e.getY() < 3 * windowSize / 5 + windowSize / 10 + 30) {
               level = 9;
               gamePlayActive = true;
               levelSelectActive = false;
               playerLives = 3;
               player.x = 100;
               player.y = 250;
               layout = GameConstants.level9Layout;
               windowSize = 1000;
               frame.setSize(1015, (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight()); 
               enemies.clear();
               for (Tank enemy: GameConstants.getLevel9Enemies()) {
                  enemies.add(enemy);
               }
               missilesFired = 0;
               levelTimeStart = System.currentTimeMillis();
               currStar = 3;
               enemyCount = enemies.size();
            }
            if (e.getX() > windowSize * 7/20 && e.getX() < windowSize * 7/20 + windowSize / 10 && 
               e.getY() > 3 * windowSize / 5 + 30 && e.getY() < 3 * windowSize / 5 + windowSize / 10 + 30) {
               level = 10;
               gamePlayActive = true;
               levelSelectActive = false;
               playerLives = 3;
               player.x = 100;
               player.y = 250;
               layout = GameConstants.level10Layout;
               windowSize = 1000;
               frame.setSize(1015, (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight()); 
               enemies.clear();
               for (Tank enemy: GameConstants.getLevel10Enemies()) {
                  enemies.add(enemy);
               }
               missilesFired = 0;
               levelTimeStart = System.currentTimeMillis();
               currStar = 3;
               enemyCount = enemies.size();
            }

         } else if (levelSummaryScreen) {
            if (e.getX() > windowSize * 3/4 && e.getX() < windowSize * 17/20 && e.getY() > windowSize * 7/10 + 30 && e.getY() < windowSize * 4/5 + 30) {
               drawExplosion = true;
               gamePlayActive = true;
               levelSummaryScreen = false;
            }  
            if (e.getX() > windowSize * 1/10 && e.getX() < windowSize / 5 && e.getY() > 7/10 + 30 && e.getY() < windowSize * 4/5 + 30) {
               levelSummaryScreen = false;
               levelSelectActive = true;
            } 
         } else if (optionsMenuActive) {
            if (e.getX() > 160 * windowSize / 500 && e.getX() < 175 * windowSize / 500 && e.getY() > 215 * windowSize / 500 && e.getY() < 240 * windowSize / 500) {
               if (soundOn) {
                  soundOn = false;
                  
               } else {
                  soundOn = true;
               }
            }
            if (e.getX() > 330 * windowSize / 500 && e.getX() < 350 * windowSize / 500 && e.getY() > 215 * windowSize / 500 && e.getY() < 235 * windowSize / 500) {
               if (musicOn) {
                  musicOn = false;
                  gameSongPlayer.pause();
               } else {
                  musicOn = true;
                  gameSongPlayer.play();
               }
            }
            if (e.getX() > 255 * windowSize / 500 && e.getX() < 270 * windowSize / 500 && e.getY() > 315 * windowSize / 500 && e.getY() < 355 * windowSize / 500) {
               difficulty = 1;
            }
            if (e.getX() > 370 * windowSize / 500 && e.getX() < 390 * windowSize / 500 && e.getY() > 320 * windowSize / 500 && e.getY() < 350 * windowSize / 500) {
               difficulty = 2;
            }
            if (e.getX() > 460 * windowSize / 500 && e.getX() < 475 * windowSize / 500 && e.getY() > 315 * windowSize / 500 && e.getY() < 350 * windowSize / 500) {
               difficulty = 3;
            }
         }
      }
      
   }
   
   public class KeyClick extends KeyAdapter {
      @Override
      public void keyPressed(KeyEvent e) {
         if (e.getKeyCode() == KeyEvent.VK_R) {
            gameOverScreen = false;
            gamePlayActive = false;
            modeSelectActive = true;
            endlessModeActive = false;
            enemies.clear();
            missiles.clear();
         }
         if (optionsMenuActive) {
            if (e.getKeyCode() == KeyEvent.VK_ESCAPE || e.getKeyCode() == KeyEvent.VK_P) {
               optionsMenuActive = false;
               gamePlayActive = true;
            }
         } else if (gamePlayActive) {
            if (e.getKeyCode() == KeyEvent.VK_P || e.getKeyCode() == KeyEvent.VK_ESCAPE) {
               gamePlayActive = false;
               optionsMenuActive = true;
            }
            if (e.getKeyCode() == KeyEvent.VK_2) {
               layout = GameConstants.level2Layout;
               windowSize = 1000;
               frame.setSize(1015, (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight()); 
               enemies.clear();
               for (Tank enemy: GameConstants.getLevel2Enemies()) {
                  enemies.add(enemy);
               }
            }
            if (e.getKeyCode() == KeyEvent.VK_3) {
               layout = GameConstants.level3Layout;
               windowSize = 1000;
               frame.setSize(1015, (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight()); 
               enemies.clear();
               for (Tank enemy: GameConstants.getLevel3Enemies()) {
                  enemies.add(enemy);
               }
            }
            if (e.getKeyCode() == KeyEvent.VK_D) {
               right_pressed = true;
            } 
            if (e.getKeyCode() == KeyEvent.VK_S) {
               down_pressed = true;
            } 
            if (e.getKeyCode() == KeyEvent.VK_W) {
               up_pressed = true;
            } 
            if (e.getKeyCode() == KeyEvent.VK_A) {
               left_pressed = true;
            }
            //If you press space and it's been a second since you last fired...
            if (e.getKeyCode() == KeyEvent.VK_SPACE && System.currentTimeMillis() - player.lastFiredTime > 1000) {
               player.lastFiredTime = System.currentTimeMillis();
               int mouseX = lastMouseX, mouseY = lastMouseY;
               int playerX = player.x, playerY = player.y;
               double relX = mouseX - playerX, relY = mouseY - playerY;
               double angle = Math.atan(relY / relX);
               if (relX < 0) {
                  angle += Math.PI;
               }
               try {
                  if (missileSprite1 == null) {
                     missileSprite1 = ImageIO.read(new File("missile1.png"));
                     missileSprite2 = ImageIO.read(new File("missile2.png"));
                     missileSprite3 = ImageIO.read(new File("missile3.png"));
                  }
                  
                  double rotationRequired = angle;
                  double locationX = missileSprite1.getWidth() / 2;
                  double locationY = missileSprite1.getHeight() / 2;
                  AffineTransform tx = AffineTransform.getRotateInstance(rotationRequired, locationX, locationY);
                  AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
                  BufferedImage missileSprite1rotate = op.filter(missileSprite1, null);
                  BufferedImage missileSprite2rotate = op.filter(missileSprite2, null);
                  BufferedImage missileSprite3rotate = op.filter(missileSprite3, null);
                  double missileX = player.x + player.getSize() / 2 * Math.cos(angle);
                  double missileY = player.y + player.getSize() / 2 * Math.sin(angle);
                  double missileDX = difficulty * missileSpeed * Math.cos(angle);
                  double missileDY = difficulty * missileSpeed * Math.sin(angle);
                  Missile toShoot = new Missile(missileX, missileY, missileDX, missileDY, false, missileSprite1rotate, missileSprite2rotate, missileSprite3rotate);
                  toShoot.bouncesLeft = 1;
                  missiles.add(toShoot);
                  if (soundOn) {
                     player.playExplosion();
                  }
                  missilesFired++;
               } catch (IOException x) { }
            }
         }
      }
      
      @Override
      public void keyReleased(KeyEvent e) {
         if (gamePlayActive) {
            if (e.getKeyCode() == KeyEvent.VK_D) {
               right_pressed = false;
            } 
            if (e.getKeyCode() == KeyEvent.VK_S) {
               down_pressed = false;
            } 
            if (e.getKeyCode() == KeyEvent.VK_W) {
               up_pressed = false;
            } 
            if (e.getKeyCode() == KeyEvent.VK_A) {
               left_pressed = false;
            }
         }
      }
   }
}