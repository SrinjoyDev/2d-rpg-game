package entity;

import java.awt.image.BufferedImage;

//entity class this stores variables will be used in player , monster and NPC classes.
public class Entity {
    public int x ,y; //x y co-ordinates of the entity
    public int speed; //speed of the entity

    //buffered image describes an image with an accesivblwe buffer of image data . (we use this to store our image files)
    public BufferedImage up1 ,up2 , down1 , down2 , left1, left2 , right1 , right2;
    public String direction;

    //walking animation>
    public int spriteCounter = 0;
    public int spriteNum = 1;
}
