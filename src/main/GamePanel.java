package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

import entity.Player;
import object.SuperObject;
import tile.TileManager;
import ui.UI;

//thgis game panel class extends the Jpanel in built class that is used for GUI
public class GamePanel extends JPanel implements Runnable {

  // SCREEN SETTINGS>
  final int originalTileSize = 16; // 16 * 16 tile , means 16 * 16 pixels , thjis is a convention to keep objects// of a 2d game size to 16 * 16 .                           
  // 16 * 16 will look very small in the screen for a modern monitor
  // thus we have to scale it.
  final int scale = 3; // 16 * 16 will acquire that much area but to show it larger we will multiply it // with 3 , that is 16 * 3 is 48 so in modern monitors
  // with higher resolutions it will appear a little bit bigger here.
  public final int tileSize = originalTileSize * scale; // 48 * 48 tile size.
  // defining the size of the game screen>
  public final int maxScreenCol = 16;
  public final int maxScreenRow = 12;
  public final int screenWidth = maxScreenCol * tileSize; // 768 pixels
  public final int screenHeight = maxScreenRow * tileSize; // 576 pixels

  //WORLD MAP SETTINGS>
  public final int maxWorldCol = 50;
  public final int maxWorldRow = 50;
  public final int worldWidth = tileSize * maxWorldCol;
  public final int worldHeight = tileSize * maxWorldRow;

  //FPS SETTINGS>
  final int FPS = 100;
  public int currentFps = 0; //track current fps

  Thread gameThread; // define gameThread.

  //start game thread method
  public void startGameThread() {
    //this is how u instantiate a thread for a class.
    gameThread = new Thread(this); //instantiate a new thread here , we are passing the GamePanel class only coz that is the thread we need to do.
    //start the thead>
    gameThread.start(); //it will automatically call the run method which will run the thread.
  }

  //init key handler
  public KeyHandler keyH = new KeyHandler(); //instantiate the key handler object from the keyhandler class

  //init mouse handler
  public MouseHandler mouseH = new MouseHandler();
  TileManager tileM = new TileManager(this); //instantiate the tile manager.

  //init object for asset setter>
  public AssetPlacer aPlacer = new AssetPlacer(this);

  //instantiate player class >
  public Player player = new Player(this, keyH , this.screenWidth / 2 - (this.tileSize/2) , this.screenHeight / 2 - (this.tileSize/2)); //pass the game panel class and the key handler object
  
  //init collission detector object
  public  CollisionDetector cDetector = new CollisionDetector(this);

  //init object for objects in game. for now we are keeping upto 10 objects getting rendered at the same time.
  public SuperObject obj[] = new SuperObject[20];

  //load ui elements to render on screen
  public UI ui = new UI(this);
  
  // consturctor for the GamePanel class
  public GamePanel() {

    this.setPreferredSize(new Dimension(screenWidth, screenHeight)); // size of the game panel
    this.setBackground(Color.black); // make background color black

    // in short , setDoubleBuffered(true) tells swing to render frames off screen
    this.setDoubleBuffered(true);

    //add kety listener for constructor
    this.addKeyListener(keyH); //with this the game panel will recognise the key input.

    //add mouse listeners>
    this.addMouseListener(mouseH);
    this.addMouseMotionListener(mouseH);

    this.setFocusable(true); //with this gamePanle can be focussed to recive key input .
    
    //set default value of player
    //position the player to the center of the screen, starting in the open grassy area
    player.setDefaultValues(this.tileSize * 23,this.tileSize * 23,false);
  }


  //called before the game starts , so that we load the assets and all before the game starts.
  public void setUpGame(){
    aPlacer.setObject(); //set objects in the game
  }
  //implement run method to run the thread
  @Override
  public void run() {
    // //this run method will have the game loop ,whcih will be the core of the game
    double drawInterval = 1000000000/FPS; //close to 0.01666 seconds , that means we draw the screen every that much seconds
    double nextDrawTime = System.nanoTime() + drawInterval ; //after this draw interval we draw the graphics

    int frameCount = 0;
    long fpsTimer = System.nanoTime();
    
    // Main game loop
    // This loop runs continuously while the game thread exists.
    // Each iteration = one frame (update + render), paced to a fixed FPS.
    while (gameThread != null) {

        // 1. UPDATE GAME LOGIC
        // --------------------
        // Update the game state:
        // - player movement
        // - enemy AI
        // - collisions
        // - animations
        // This runs once per frame.
        update();

        // 2. REQUEST RENDER
        // -----------------
        // Tells Swing to repaint the screen.
        // The actual drawing happens later on the EDT
        // inside paintComponent(Graphics g).
        repaint();

        //count rendered frames>
        frameCount++;
        //calulate fps once every second
        long now = System.nanoTime();
        if(now - fpsTimer > 1_000_000_000L){ // 1 B nano secs = 1 sec
          //that means 1 second done
          currentFps = frameCount;
          frameCount = 0;
          fpsTimer = now;
        }

        try {
            // 3. FRAME TIMING CALCULATION
            // ---------------------------
            // nextDrawTime = the exact time (in nanoseconds) when this frame
            // is supposed to finish.
            //
            // System.nanoTime() = current time.
            //
            // Subtracting them gives the remaining time left for this frame.
            //
            // Divide by 1_000_000 to convert nanoseconds → milliseconds
            // because Thread.sleep() only accepts milliseconds.
            double remainingTime =  (nextDrawTime - System.nanoTime()) / 1_000_000;
                   
            // 4. FRAME OVERRUN PROTECTION
            // ---------------------------
            // If update + render took longer than the target frame time,
            // remainingTime becomes negative.
            //
            // In that case, we skip sleeping so the game can catch up
            // instead of slowing down further.
            if (remainingTime < 0) {
                remainingTime = 0;
            }

            // 5. CONTROLLED SLEEP (FRAME PACING)
            // ---------------------------------
            // Pause the game thread for the remaining time of this frame.
            // This ensures each frame takes approximately the same amount
            // of time, maintaining a stable FPS.
            Thread.sleep((long) remainingTime);

            // 6. SCHEDULE NEXT FRAME (DRIFT-FREE)
            // -----------------------------------
            // Move the target time forward by exactly one frame interval.
            //
            // IMPORTANT:
            // We add drawInterval instead of recalculating from current time
            // to prevent timing drift and keep the game synced to real time.
            nextDrawTime = nextDrawTime + drawInterval;

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

  }

  public void update() { 
    //update player
    player.update(keyH);
    
    ui.update();
    //inventory toggle
    if(keyH.inventoryPressedOnce){
      ui.toggleInventory(); //will open inventory;
      keyH.inventoryPressedOnce = false; //consume input
    }
    
    //update object animmations
    for(int i = 0 ; i < obj.length ; i ++){
      if(obj[i] != null){
        obj[i].update();
      }
    }

    
  }

  //paintComponent is one of the built in methods in java , this is used to draw frames in the JPanel.
  //this function expects a graphics class , that is a class that has many functions to draw objects on the screen.
  //u can imagine this graphics g as a paint brush or pencil , with the help of that u draw stuff on screen that is the rendering of stuff  in games
  public void paintComponent(Graphics g) {
    //u tell the Jpanel to paint the compoinenet with the graphics.
    //so to call the method of the parent class which is Jpanel here we have to user super here./
    super.paintComponent(g);

    //lets convert the graphics class to graphics2d class.
    //graphics2d calss extends the graphics class to provide more sophisticated control over geometrry , coordinate transformations,
    //color management and text layouts.
    Graphics2D g2 = (Graphics2D)g;

    g2.setColor(Color.white);

    //draw the tile , then draw the player.

    //render tile
    if(!tileM.isImageNull()) tileM.draw(g2);
    
    //render object
    for(int i = 0 ; i < obj.length ; i++){
      if(obj[i] != null && !obj[i].isImageNull()){
        obj[i].draw(this, g2);
      }
    }

    //renbder player
    if(!player.isImageNull()) player.draw(g2, this);

    //render ui on top
    g2.drawString("FPS: " + this.currentFps , 10 ,20 );
    ui.draw(g2);

    //after drawing is done , we should dispose the graphgics so that java garbage collectors can remove the resources that the graphics was sharing
    g2.dispose();
  }

}
