package entity;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;

/*
    Entity class for all kind of entitites in the game
    for example , NPCs  , players , monsters ...etc
*/
public class Entity {

    //WORLD SETTINGS
    public int worldX , worldY; 
    public int speed;
    public String direction;

    //ASSET MANAGEMENT
    public BufferedImage up1 ,up2 , down1 , down2 , left1, left2 , right1 , right2;
    
    //WALKING ANIMATION
    public int spriteCounter = 0;
    public int spriteNum = 1;

    //COLLISON DETECTION
    public Rectangle solidArea; //player's body is solid part . excliding hands , head , and some part of legs from solid part , as those parts of a player are flexible.
    public boolean playerCollision = false;

    //OBJECT INTERACTION
    public int solid_area_default_X , solid_area_default_Y;
}
