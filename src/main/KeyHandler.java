package main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

//class to handle user key strokes
//it implements keyListener , the listener interface for receiving keyboard events
public class KeyHandler implements KeyListener {

    //we add this keyhandler to the gamePanel class so that it knows when keys are pressed or released.

    public boolean upPressed , downPressed , leftPressed , rigthPressed = false; //defaulted to false
    public boolean shiftPressed = false;
    public boolean walk  = true; //default is walk.
    public boolean sprint = false; //shift + movement keys = run

    public boolean equipPressed = false; //we will use equip key as interaction , house enter , pick up object ..etc
    public boolean equipPressedOnce = false;
    

    //TODO : add diagonal movement booleans also

    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode(); //returns the integer keyCode associated with the key in this event

        //w key pressed
        if (code == KeyEvent.VK_W){
            upPressed = true;
        }

        //s key pressed
        if (code == KeyEvent.VK_S){
            downPressed = true;
        }

        //a key pressed
        if (code == KeyEvent.VK_A){
            leftPressed = true;
        }

        //d key pressed
        if (code == KeyEvent.VK_D){
            rigthPressed = true;
        }

        //shift key pressed for sprint events
        if(code == KeyEvent.VK_SHIFT){
            shiftPressed = true;
        }

        //F key pressed which is for equip
        if(code == KeyEvent.VK_F && !equipPressedOnce){
            equipPressed = true;
            equipPressedOnce = true;
        }

        //TODO : we can update diagonal movements too.
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode(); //returns the integer keyCode associated with the key in this event
        
        //w key released
        if (code == KeyEvent.VK_W){
            upPressed = false;
        }

        //s key released
        if (code == KeyEvent.VK_S){
            downPressed = false;
        }

        //a key released
        if (code == KeyEvent.VK_A){
            leftPressed = false;
        }

        //d key released
        if (code == KeyEvent.VK_D){
            rigthPressed = false;
        }

        //shift release means walk again.
        if (code == KeyEvent.VK_SHIFT){
            shiftPressed = false;
        }

        //equip released
        if(code == KeyEvent.VK_F){
            equipPressed = false;
            equipPressedOnce = false;
        }

    }

    @Override
    //we will not use this here.
    public void keyTyped(KeyEvent e) {
        
    }
    
}
