package ui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import inventory.Item;
import main.GamePanel;

public class UI {
    
    GamePanel gp;

    //slot box
    BufferedImage slotBoxImage;

    //inventory
    public boolean inventoryOpen = false;
    
    //inventory drag , drop , swap items mechanics
    Item draggedItem = null;
    Slot draggedFrom = null;

    List<Slot> slots = new ArrayList<>();

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
        if(inventoryOpen){
            drawDimBackground(g2);
            drawInventoryGrid(g2);
        }

        if(draggedItem != null){
            g2.drawImage(draggedItem.icon, gp.mouseH.x - gp.tileSize/2, gp.mouseH.y - gp.tileSize/2 , gp.tileSize , gp.tileSize , null);
        }
    }

    //toggle inventory
    public void toggleInventory() {
        inventoryOpen = !inventoryOpen;
    }

    //update method for ui grid
    public void update() {
        if(!inventoryOpen) return;

        //start drag
        if(gp.mouseH.pressed && draggedItem == null){
            for (Slot s : slots){
                if(isInsideSlot(gp.mouseH.x, gp.mouseH.y, s)) {
                    if(s.inventorySlot){
                        Item item = gp.player.getInventoryItems()[s.index];
                        if(item != null && !gp.player.isItemEquipped(item)){
                            draggedItem = item;
                            draggedFrom = s;
                            break;
                        }
                    } else {
                        Item item = gp.player.getEquippedItems()[s.index];
                        if(item != null){
                            draggedItem = item;
                            draggedFrom = s;
                            break;
                        }
                    }
                }
            }
        }
        
        //drop
        if(!gp.mouseH.pressed && draggedItem != null){
            handleDrop();
            draggedItem = null;
            draggedFrom = null;
        }
    }

    private void handleDrop() {
        for (Slot target : slots){
            if(isInsideSlot(gp.mouseH.x, gp.mouseH.y, target)){
                //imventory -> inventory(swap)
                if(draggedFrom.inventorySlot && target.inventorySlot){
                    gp.player.swapInventory(draggedFrom.index , target.index);
                } else if(draggedFrom.inventorySlot && !target.inventorySlot){
                    //inventory -> equpped
                    gp.player.swapInventoryToEquipped(draggedFrom.index, target.index);
                } else if(!draggedFrom.inventorySlot && target.inventorySlot){
                    //equipped-> inventory
                    gp.player.swapEquippedToInventory(draggedFrom.index, target.index);
                } else {
                    //equpped -> equipped
                    gp.player.swapEquipped(draggedFrom.index, target.index);
                }
                break;
            }
        }
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

    //render inventory grid -> 3 * 4 inventory grid + gap + 1 * 4 equipped grid
    private void drawInventoryGrid(Graphics2D g2){
        //clear slots
        slots.clear();

        //get inventory items
        Item[] inventory = gp.player.getInventoryItems();

        //get equipped items
        Item[] equipped  = gp.player.getEquippedItems();

        int slotSize = gp.tileSize;
        int padding = 10; //padding btw each box
        int gap = 30; //gap between inv grid and eqp grid

        int inventoryCols = 3;
        int inventoryRows = 4;

        //total width = inventory + gap + equipped col
        int totalWidth = inventoryCols * slotSize + (inventoryCols - 1) * padding + gap + slotSize;
        
        int startX = (gp.screenWidth - totalWidth) / 2;
        int startY = (gp.screenHeight - (inventoryRows * slotSize + (inventoryRows - 1) * padding)) / 2;

        //inventory render (3 * 4)>
        int index = 0;

        for(int row = 0 ; row < inventoryRows ; row ++){
            for(int col = 0 ; col < inventoryCols ; col ++){
                int x = startX + col * (slotSize + padding);
                int y = startY + row * (slotSize + padding);

                //make slot for each box we store ever reference to this slot objct s which is of type Slot class.
                Slot s = new Slot();
                s.x = x;
                s.y = y;
                s.size = slotSize;
                s.index = index;
                s.inventorySlot = true;

                //add slot
                slots.add(s);

                //draw inventory grid
                g2.drawImage(slotBoxImage, x, y , slotSize , slotSize , null);

                if(index < inventory.length && inventory[index] != null && !gp.player.isItemEquipped(inventory[index])){
                    g2.drawImage(inventory[index].icon, x + 6, y + 6 , slotSize - 12 , slotSize - 12 , null);
                }
                index ++;
            }
        }

        //equipper render 1*4>
        int equippedX = startX + inventoryCols * (slotSize + padding) + gap;

        for (int i = 0 ; i < equipped.length ; i++) {
            int y = startY + i * (slotSize + padding);

            Slot s = new Slot();
            s.x = equippedX;
            s.y = y;
            s.size = slotSize;
            s.index = i;
            s.inventorySlot = false;
            slots.add(s);


            //after these things the gui knows where each and every slot iss
            
            g2.drawImage(slotBoxImage, equippedX, y , slotSize , slotSize , null);

            if(equipped[i] != null){
                g2.drawImage(equipped[i].icon, equippedX + 6, y + 6 , slotSize - 12 , slotSize - 12 , null);
            }
        }
    }

    private boolean isInsideSlot(int mX , int mY , Slot s){
        return mX >= s.x && mX <= s.x + s.size && mY >= s.y && mY <= s.y + s.size;
    }

    private void drawDimBackground(Graphics2D g2){
        g2.setColor(new Color(0, 0, 0, 150)); // semi-transparent black
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);
    }
}
