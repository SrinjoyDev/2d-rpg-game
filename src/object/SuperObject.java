package object;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import main.GamePanel;

public class SuperObject {
    
    public BufferedImage ObjectImage;
    public String name;
    public boolean objectCollision = false;
    public int worldX , worldY;
    public boolean bounce = false;

    //animation
    int floatOffset = 0;
    int floatDirection = 1; //direction , +1 one movement , -1 opposite movement

    public boolean isImageNull() {
        return ObjectImage == null;
    }

    public void update() {
        //coing floating animation , frame manipulation technique
        if(bounce){
            floatOffset += floatDirection;
            if(floatOffset > 10 || floatOffset < -10){
                floatDirection *= -1; //limnit reach direction flip
            }
        }
    }

    public void draw(GamePanel gp , Graphics2D g2){
        int screenX = worldX - gp.player.worldX + gp.player.screenX;
        int screenY = worldY - gp.player.worldY + gp.player.screenY + floatOffset;

        if(screenX > -gp.tileSize &&
           screenX < gp.screenWidth + gp.tileSize &&
           screenY > -gp.tileSize &&
           screenY < gp.screenHeight + gp.tileSize 
        ) {
            g2.drawImage(ObjectImage, screenX, screenY , gp.tileSize , gp.tileSize , null);
        }
    }
}
