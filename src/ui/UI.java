package ui;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import inventory.Item;
import main.GamePanel;

public class UI {
    
    GamePanel gp;

    //slot box
    BufferedImage slotBoxImage;

    public UI(GamePanel gp){
        this.gp = gp;
        
        try {
            slotBoxImage = ImageIO.read(getClass().getResourceAsStream("/gui-assets/inventory-box.png"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void draw(Graphics2D g2){
        drawEquippedSlots(g2);
    }

    //render equipped slot inventory item
    private void drawEquippedSlots(Graphics2D g2){

        //get equipped items of the player
        Item[] equipped = gp.player.getEquippedItems();
        
        int slotSize = gp.tileSize; //slot size is how tilesize is big
        int padding = 0;

        //start from top right
        int startX = gp.screenHeight - (slotSize * equipped.length) - padding*2;
        int y = padding;

        for(int i = 0 ; i < equipped.length ; i++){
            int x = startX + i * (slotSize + padding);

            //draw slot box>
            g2.drawImage(slotBoxImage, x, y , slotSize , slotSize , null);

            //draw item icon if equipped
            if(equipped[i] != null){
                g2.drawImage(equipped[i].icon, x + 6, y + 6 , slotSize -12 , slotSize - 12 , null);
            }
        }
    }
}
