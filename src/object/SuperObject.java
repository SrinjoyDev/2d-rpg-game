package object;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import inventory.Item;
import main.GamePanel;

public class SuperObject {
    
    public BufferedImage ObjectImage;
    public String name;
    public boolean objectCollision = false;
    public int worldX , worldY;
    public boolean bounce = false;
    
    //text animations when player is close.
    public boolean is_player_close_to_object = false;
    public String textToDisplay;


    //solid area for object interaction
    //if we want we can place different solid area for each object , but for this game all object will have the same solid area.
    public Rectangle solidArea = new Rectangle(0 , 0 , 48 , 48); //x,y,width,height -> wifth and height are the size of a tile , so entire object will be a solid area.

    public int solid_area_default_X = 0;
    public int solid_area_default_Y = 0;

    //animation
    int floatOffset = 0;
    int floatDirection = 1; //direction , +1 one movement , -1 opposite movement

    //object to item
    public Item toItem(){
        return new Item(this.name, this.ObjectImage); //convert this world object to item for inventory usage.
    }

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

            if(is_player_close_to_object){
                switch (name) {
                    case "key" :
                        this.textToDisplay = "press F to pick up the key!";
                        g2.drawString(textToDisplay, screenX, screenY + 1);
                        break;

                    case "door" :
                        this.textToDisplay = "press F to open the door!";
                        g2.drawString(textToDisplay, screenX, screenY + 2);
                        break;

                    case "chest" :
                        this.textToDisplay = "press F to open the chest";
                        g2.drawString(textToDisplay, screenX, screenY + 1);
                        break;
                }
            }
        }
    }
}
