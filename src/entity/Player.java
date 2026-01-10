package entity;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import inventory.Item;
import main.GamePanel;
import main.KeyHandler;

public class Player extends Entity {
    
    GamePanel gp;
    KeyHandler kh;

    public boolean sprint = false;

    //screen x and screen y w.r.t player.
    public final int screenX;
    public final int screenY;
    
    //player solid area pixels.
    public int handPixel = 8; //the entire player asset is 48 * 48 so except hands that is soft body will be 32px , rest covers 8px and 8px hand on both side
    public int headPixel = 16; //we want until neck top of head to eye 8px + eye to mouth 8px if u look in the asset 
    public int legPixel = 16; //leg pixel of player..

    //INVENTORY>
    private Item[] inventory = new Item[16]; //total items player can keep
    private Item[] equipped = new Item[4]; //total items player can equip

    //constructor for player class
    public Player(GamePanel gp , KeyHandler kh , int screenX , int screenY) {
        this.gp = gp;
        this.kh = kh;
        this.direction = "down";

        //player position on screen (centre of the screen)
        this.screenX = screenX;
        this.screenY = screenY;
        
        //solid area of player init
        solidArea = new Rectangle();

        solidArea.x = handPixel;
        solidArea.y = headPixel;
        solidArea.width = gp.tileSize - handPixel - handPixel; //remove two hands whatever space is there
        solidArea.height = gp.tileSize - headPixel - legPixel; //remove head and some legs , whatever space is trher

        //object interactiom , default solid arrea is this , when objects interact then solid area changes.
        solid_area_default_X = solidArea.x;
        solid_area_default_Y=  solidArea.y;

        //load player assets
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

        int speed = sprint ? 4 : 2;

        //MOVEMENT DIRECTION MECHANICS.
        if(keyH.upPressed) this.direction = "up";
        else if(keyH.downPressed) this.direction = "down";
        else if(keyH.leftPressed) this.direction = "left";
        else if(keyH.rigthPressed) this.direction = "right";

        //TILE COLLISON CHECKER.
        playerCollision = false; //init player collison as false at beginning , flag becomes true when player collides.
        gp.cDetector.checkTile(this);

        //object collision checker
        int objectIndex = gp.cDetector.checkObject(this, true); //pass true as this is player moving here.
        
        //pick up object if user pressed equip key.
        if(keyH.equipPressed && objectIndex != 999) pickUpObject(objectIndex);

        boolean isMoving = (keyH.upPressed || keyH.downPressed || keyH.leftPressed || keyH.rigthPressed) && (!gp.ui.inventoryOpen);

        if(isMoving){
            //TODO: add sprint direction also

            sprint = keyH.shiftPressed;

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

    //object interaction -> what happens when player touches an object>
    public void pickUpObject(int index) {

        //if index != 999 that 999 index is that player didnt touch any object.
        if(index != 999){
            //convert object to item
            Item item = gp.obj[index].toItem();
            boolean is_added = addItem(item); //add item to inventory
            if (is_added) gp.obj[index] = null; //remove from world.
        }
    }

    public boolean isItemEquipped(Item item){
        for(Item eq : equipped){
            if(eq == item){
                return true;
            }
        }
        return false;
    }

    //add item to inventory
    public boolean addItem(Item item) {
        //add to inventory
        for(int i = 0 ; i < inventory.length ; i++ ){
            if(inventory[i] == null){ //if slot empty
                //add item to inventory
                inventory[i] = item;
                System.out.println("item added to inventory");
                break;
            }
        }
        
        //auto equip items only if space is there in equipped slots.
        for(int i = 0 ; i < equipped.length ; i++){
            if(equipped[i] == null){
                equipped[i] = item;
                System.out.println("item auto equipped");
                return true;
            }
        }

        return true;
    }

    //swap items mechanics logics>
    public void swapInventory(int a , int b){
        Item tmp = inventory[a];
        inventory[a] = inventory[b];
        inventory[b] = tmp;
    }

    public void swapEquipped(int a , int b){
        Item tmp = equipped[a];
        equipped[a] = equipped[b];
        equipped[b] = tmp;
    }

    public void swapInventoryToEquipped(int inv , int eq){
        Item tmp = equipped[eq];
        equipped[eq] = inventory[inv];
        inventory[inv] = tmp;
    }

    public void swapEquippedToInventory(int eq , int inv){
        Item tmp = inventory[inv];
        inventory[inv] = equipped[eq];
        equipped[eq] = tmp;
    }

    
//     private void printEquippedItems() {
//         for (int i = 0; i < equipped.length; i++) {
//             if (equipped[i] != null) {
//                 System.out.println("equipped slot " + i + ": " + equipped[i].name);
//             } else {
//                 System.out.println("equipped slot " + i + ": empty");
//             }
//         }
//         System.out.println();
//     }

//    private void printInventoryItems() {
//         for (int i = 0; i < inventory.length; i++) {
//             if (inventory[i] != null) {
//                 System.out.println("inventory slot " + i + ": " + inventory[i].name);
//             } else {
//                 System.out.println("inventory slot " + i + ": empty");
//             }
//         }
//         System.out.println();
//     }

    //getter for get equipped items
    public Item[] getEquippedItems(){
        return equipped;
    }

    //public get inventory
    public Item[] getInventoryItems(){
        return inventory;
    }

    //method to update graphics of the player.
    public void draw(Graphics2D g2 , GamePanel gp) {
        //when u want to draw something on the screen with grahics 2d , it asks for the x,y coordinates , and the width and the height for the screen , so that it can draw and render it.
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
