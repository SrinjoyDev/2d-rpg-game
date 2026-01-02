package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

//thgis game panel class extends the Jpanel in built class that is used for GUI
public class GamePanel extends JPanel implements Runnable {

  // screen settings>
  final int originalTileSize = 16; // 16 * 16 tile , means 16 * 16 pixels , thjis is a convention to keep objects// of a 2d game size to 16 * 16 .                           
  // 16 * 16 will look very small in the screen for a modern monitor
  // thus we have to scale it.
  final int scale = 3; // 16 * 16 will acquire that much area but to show it larger we will multiply it // with 3 , that is 16 * 3 is 48 so in modern monitors
  // with higher resolutions it will appear a little bit bigger here.
  final int tileSize = originalTileSize * scale; // 48 * 48 tile size.
  // defining the size of the game screen>
  final int maxScreenCol = 16;
  final int maxScreenRow = 12;
  final int screenWidth = maxScreenCol * tileSize; // 768 pixels
  final int screenHeight = maxScreenRow * tileSize; // 576 pixels

  //FPS
  final int FPS = 120;

  KeyHandler keyH = new KeyHandler(); //instantiate the key handler object from the keyhandler class

  //PLAYER DEFAULT POSITIONS>
  int playerX = 100;
  int playerY = 100;


  // consturctor for the GamePanel class
  public GamePanel() {

    this.setPreferredSize(new Dimension(screenWidth, screenHeight)); // size of the game panel
    this.setBackground(Color.black); // make background color black

    // one thing is that games , is withour double buffering the swing would draw
    // graphics directly to screeen
    // that is a bad expericence as user might see screen flickering , partially
    // draw frames , incomplete or torn visulas ..etc
    // this happens because drawing operatons are not instantaneous

    // how double buffering works is :
    // swing creates off screen buffer (a hidden image in memory)
    // all drawing operations (paintComponent) are performed on this buffer
    // once the frame is fully rendered on the buffer thebuffer is copied to screen
    // in a single operation.
    // that is much more efficient that synchronous rendering whcih is bad. , this
    // makes rendering appear smooth and stable

    // in short , setDoubleBuffered(true) tells swing to render frames off screen
    // first in a hidden image in memory and then display
    // them all at once , resulting in smooth , flicker -free graphics which is
    // essential for real-time game graphics rendering
    this.setDoubleBuffered(true);

    //add kety listener for constructor
    this.addKeyListener(keyH); //with this the game panel will recognise the key input.
    this.setFocusable(true); //with this gamePanle can be focussed to recive key input .
  }

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

  public void startGameThread() {
    gameThread = new Thread(this); //instantiate a new thread here , we are passing the GamePanel class only coz that is the thread we need to do.
    //this is how u instantiate a thread for a class.
    //start the thead>
    gameThread.start(); //it will automatically call the run method which will run the thread.
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

    while(gameThread != null){
      update();

      repaint();

      //core game loop logic for update and repaint
      try {
        //after that repaint also ,we need to find the remaining time that is there and make the thread sleep during that time , potherwise next update will be again fast .
        double remainingTime = (nextDrawTime - System.nanoTime())/1000000; //conv to ms , as theread dosent accept nano seconds

        if(remainingTime < 0){
          remainingTime = 0;
        }

        Thread.sleep((long) remainingTime);  

        nextDrawTime = nextDrawTime + drawInterval;
      } catch (Exception e) {
        e.printStackTrace();
      }
      
      
    }
  }

  public void update() {
    int playerSpeed = keyH.sprint ? 4 : 2; //if sprint then move 4 pixels else walk is 2 pixels
    if(keyH.upPressed == true){
      //make player char go up>
      playerY = playerY - playerSpeed; //based on the speed the player is moving we update the y cordinate as up involved y cordinate only.
    } else if(keyH.downPressed == true){
      playerY = playerY + playerSpeed;
    } else if(keyH.leftPressed == true){
      playerX = playerX - playerSpeed;
    } else if(keyH.rigthPressed == true){
      playerX = playerX + playerSpeed;
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
    g2.setColor(Color.white); //color for drawing.

    //when u want to draw something on the screen with grahics 2d , it asks for the x,y coordinates , and the width and the height for the screen , so that it can draw and render it.
    g2.fillRect(playerX, playerY, tileSize, tileSize);
    //after drawing is done , we should dispose the graphgics so that java garbage collectors can remove the resources that the graphics was sharing
    g2.dispose();

    //now we want that rectangle to move around.
  }

}
