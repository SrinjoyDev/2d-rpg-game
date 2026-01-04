package entity;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import main.GamePanel;
import main.KeyHandler;

public class Player extends Entity {
    
    GamePanel gp;
    KeyHandler kh;

    public boolean sprint = false;

    //bacgornnd map co-ordintes , the map also needs to move whenn player moves from one location to another
    public final int screenX;
    public final int screenY;
    
    //player collison detector pixels , pixels of hand and head so that we can make advanced collision detectors and improve game mechanics
    //NOTE: we can adjust this values based on what works best for our game
    public int handPixel = 8; //the entire player asset is 48 * 48 so except hands that is soft body will be 32px , rest covers 8px and 8px hand on both side
    public int headPixel = 16; //we want until neck top of head to eye 8px + eye to mouth 8px if u look in the asset 
    
    public Player(GamePanel gp , KeyHandler kh , int screenX , int screenY) {
        this.gp = gp;
        this.kh = kh;
        this.direction = "down";

        this.screenX = screenX;
        this.screenY = screenY;
        
        //solid area of player init
        solidArea = new Rectangle();

        solidArea.x = handPixel;
        solidArea.y = headPixel;
        solidArea.width = 32;
        solidArea.height = 24;

        //call the player image method here ,to get the player image when the constructor loads.
        getPlayerImage();
    }

    //set default values>
    public void setDefaultValues(int x , int y ,boolean sprint){
        this.worldX = x;
        this.worldY = y;
        this.sprint = sprint;
        
    }

    //method to get player image
    public void getPlayerImage() {
        try {
            
            //load images
            up1 = ImageIO.read(getClass().getResourceAsStream("/player-assets/player_up_1.png"));
            up2 = ImageIO.read(getClass().getResourceAsStream("/player-assets/player_up_2.png"));
            down1 = ImageIO.read(getClass().getResourceAsStream("/player-assets/player_down_1.png"));
            down2 = ImageIO.read(getClass().getResourceAsStream("/player-assets/player_down_2.png"));
            left1 = ImageIO.read(getClass().getResourceAsStream("/player-assets/player_left_1.png"));
            left2 = ImageIO.read(getClass().getResourceAsStream("/player-assets/player_left_2.png"));
            right1 = ImageIO.read(getClass().getResourceAsStream("/player-assets/player_right_1.png"));
            right2 = ImageIO.read(getClass().getResourceAsStream("/player-assets/player_right_2.png"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isImageNull() {
       
        return down1 == null;
        
    }

    //method to update the position of player
    public void update(KeyHandler keyH) {

        int speed = sprint ? 6 : 2;
        boolean isMoving = keyH.upPressed || keyH.downPressed || keyH.leftPressed || keyH.rigthPressed;

        if(isMoving){
        //TODO: add sprint direction also

            //update player speed when sprint.
            //this is usually done in game mechanics , user would press movement keys then sprint , then only it works , normal sprint logic
            if (isMoving && keyH.shiftPressed) sprint = true;
            if (!keyH.shiftPressed) sprint = false;

            if(keyH.upPressed == true){
            //make player char go up>
                this.direction = "up";    
                // worldY = worldY - speed; //based on the speed the player is moving we update the y cordinate as up involved y cordinate only.            
            } else if(keyH.downPressed == true){
                this.direction = "down";
                // worldY = worldY + speed;
            } else if(keyH.leftPressed == true){
                this.direction = "left";
                // worldX = worldX - speed;
            } else if(keyH.rigthPressed == true){
                this.direction = "right";
                // worldX = worldX + speed;
            }

            //collison checker
            playerCollision = false;
            gp.cDetector.checkTile(this);

            //check if not collided then only we let them move
            if(playerCollision == false){
                switch (direction) {
                    case "up": worldY = worldY - speed; break;
                    case "down" : worldY = worldY + speed; break;
                    case "left" : worldX = worldX - speed; break;
                    case "right" : worldX = worldX + speed;
                    default: break;
                }
            }


            //after movement incement spritecounter > 
            //think of it like a mental image , after N frames ,we need to change the image ,that is the animation logic here.
            //sprite counter determines,  how many frames have passed.
            spriteCounter++;
            //we should already know by now , this update method gets called x frames per seconds depending upon our FPS settings

            //every frame we increment the counter.
            //since update runs every frame , after 20 frames passed while moving we are changing the image.
            if(spriteCounter > 20) {
                //spriteNum depicts which animation frame to draw
                if(spriteNum == 1){ //for eg up1
                    spriteNum = 2; //up2
                } else if(spriteNum == 2){ //for eg down2
                    spriteNum = 1; //next down 1 , like how walking works
                }
                spriteCounter = 0;
            }
        }
    }

    //method to update graphics of the player.
    public void draw(Graphics2D g2 , GamePanel gp) {
        g2.setColor(Color.white);
        g2.drawString("FPS: " + gp.currentFps , 10 ,20 );

        // //when u want to draw something on the screen with grahics 2d , it asks for the x,y coordinates , and the width and the height for the screen , so that it can draw and render it.
        // g2.fillRect(x, y, gp.tileSize, gp.tileSize); //rectangle depricated we will use our player now.

        //TODO : add sprint graphics also

        //load buffer image as null
        BufferedImage playerImage = null;
        switch (direction) {
            case "up":
                if (spriteNum == 1) playerImage = up1;
                if(spriteNum == 2) playerImage = up2;
                break;
            case "down":
                if (spriteNum == 1) playerImage = down1;
                if(spriteNum == 2) playerImage = down2;
                break;
            case "left":
                if (spriteNum == 1) playerImage = left1;
                if(spriteNum == 2) playerImage = left2;
                break;
            case "right":
                if (spriteNum == 1) playerImage = right1;
                if(spriteNum == 2) playerImage = right2;
                break;
            default:
                break;
        }

        //draw the player image>
        if (playerImage == null) System.out.println("player image found null");
        //the player sticks to the centre of the screen , based on the movement of the player , the world aroung it changes.
        g2.drawImage(playerImage, screenX, screenY , gp.tileSize , gp.tileSize , null); //null is the image observer here
    }
}
