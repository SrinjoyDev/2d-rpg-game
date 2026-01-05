package main;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MouseHandler extends MouseAdapter {
    
    public int x , y;
    public boolean pressed;

    @Override
    public void mousePressed(MouseEvent event) {
        this.pressed = true;
        this.x = event.getX();
        this.y = event.getY();
    }

    @Override
    public void mouseReleased(MouseEvent event){
        this.pressed = false;
        this.x = event.getX();
        this.y = event.getY();
    }

    @Override
    public void mouseDragged(MouseEvent event){
        this.x = event.getX();
        this.y = event.getY();
    }
}
