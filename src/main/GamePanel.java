package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

import entity.Player;
import tile.TileManager;

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

  //FPS SETTINGS>
  final int FPS = 100;
  public int currentFps = 0; //track current fps

  //Theory :  in games , time is everything, without the track of time a game cant run.
  //when u start a game , the program of the game runs continuosuly on the background that has the knowledge of ur time of the game , where u r currently what u r doing.
  //time needs to run in a game like real life.
  //a game dosent render videos/
  //if u see 60fps that means , the game is producing 60 frames per seconds , it really basiclly 60 static images showing in that speed . whcih makes it look like ur charecrter is moving
  //or something , but underthe hood it is just 60 images where ur charecter is moving from one place to another , those images are streamed in ur screen efficiently.
  //mkaing it feel like it is moving 

  //we will make a thread here , thread is something u can start and stop
  Thread gameThread; // this thread is the key here that can make a game display all frames in a flow all those cool stuff.
  //to run a thread the class should implement Runnable , that Runnable calls a run method that basically runs the thread

  //start game thread method
  public void startGameThread() {
    //this is how u instantiate a thread for a class.
    gameThread = new Thread(this); //instantiate a new thread here , we are passing the GamePanel class only coz that is the thread we need to do.
    //start the thead>
    gameThread.start(); //it will automatically call the run method which will run the thread.
  }

  //init key handler
  KeyHandler keyH = new KeyHandler(); //instantiate the key handler object from the keyhandler class
  TileManager tileM = new TileManager(this); //instantiate the tile manager.

  //instantiate player class >
  Player player = new Player(this, keyH); //pass the game panel class and the key handler object

  // consturctor for the GamePanel class
  public GamePanel() {

    this.setPreferredSize(new Dimension(screenWidth, screenHeight)); // size of the game panel
    this.setBackground(Color.black); // make background color black

    // in short , setDoubleBuffered(true) tells swing to render frames off screen
    // first in a hidden image in memory and then display
    // them all at once , resulting in smooth , flicker -free graphics which is
    // essential for real-time game graphics rendering
    this.setDoubleBuffered(true);

    //add kety listener for constructor
    this.addKeyListener(keyH); //with this the game panel will recognise the key input.
    this.setFocusable(true); //with this gamePanle can be focussed to recive key input .
    
    //set default value of player
    player.setDefaultValues(100,100,false);
  }



  //implement run method to run the thread
  @Override
  public void run() {

    //THEORY:

    // //this run method will have the game loop ,whcih will be the core of the game
    // while(gameThread != null){ //this tells as long as the thread is there that is the process exists , then it will repeat the process that is running inside this loop
    //   //System.out.println("game thread is running!"); --> test ur game loop is running.

    //   //thread is like time
    //   //we have to do 2 things here. -> 1.update and draw

    //   //the game loop will keep calling theis update and repaint update and repaint.
    //   //which is nothing but update player position and draw the position based on the posiion of the player .
    //   //if u think deep that is how the universe works and time and reality works.

    //   // //1. UPDATE : update information such as charecter positions -> update() method

    //   // update();

    //   // //alse we need to update constraint , adding fps , otherwise it will appear that the movement vanished coz the cpu will execute it as fast as it can
    //   // //fot that we need that time .

    //   // //2. DRAW : draw the screen with updated information -> draw() method
    //   // repaint(); //yoooo , what is this ? this is nothing but , this is how u call the paintComponenet ,little RPG game traditions in game development , 
    //   // ///u can ignore this if u dont want to go deep in game development just here for the java journet. and just understand thsi is how it is used to call paintComponent in 2d game development , but yeah
    // }
    
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
    player.update(keyH);
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

    //draw the tile , then draw the player.
    if(!tileM.isImageNull()) tileM.draw(g2);
    
    if(!player.isImageNull()) player.draw(g2, this);
    
    //after drawing is done , we should dispose the graphgics so that java garbage collectors can remove the resources that the graphics was sharing
    g2.dispose();
  }

}
