
package main;

import javax.swing.JFrame;

public class Main {
  public static void main(String[] args) {
    JFrame window = new JFrame(); // it is a class for defining the top level container for java based application // GUIs.
                                  
    // define configurations for our Grphical user interface.
    window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // this operation lets the window close properly when clicked  // on the x button of the gui
                                                          
    window.setResizable(false); // we dont let the user resize the window , coz our resolution will be smaller  // for thgis small game.
                               
    // set title of the application game
    window.setTitle("2d Adventure game in java");

    // add the game panel to this window
    GamePanel gamePanel = new GamePanel();
    window.add(gamePanel);

    window.pack(); // this causes the window to be sized to fit the preferred size and layouts of  // its subcomponenets that is the GamePanel class here.
                  
    window.setLocationRelativeTo(null); // making it null , then os will put the gui on centre of the screen
    // set visible to true to see the window
    window.setVisible(true);

    //load assets of the game>
    gamePanel.setUpGame();
    //call the thread of the gamePanel to start the game thread
    gamePanel.startGameThread();
  }
}
