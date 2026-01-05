package entity;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;

//entity class this stores variables will be used in player , monster and NPC classes.
public class Entity {
    // public int x ,y; //x y co-ordinates of the entity
    public int worldX , worldY; //players position on the world map
    public int speed; //speed of the entity

    //buffered image describes an image with an accesivblwe buffer of image data . (we use this to store our image files)
    public BufferedImage up1 ,up2 , down1 , down2 , left1, left2 , right1 , right2;
    public String direction;

    //walking animation>
    public int spriteCounter = 0;
    public int spriteNum = 1;

    //collision detection
    public Rectangle solidArea; //player's body is solid part  , not the entire thing incuding heads , hands ...etc , that is bad game mechanics
    public boolean playerCollision = false;

    //for object interaction
    public int solid_area_default_X , solid_area_default_Y;
}
