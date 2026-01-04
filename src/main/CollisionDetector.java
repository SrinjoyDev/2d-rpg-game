package main;

import entity.Entity;

public class CollisionDetector {
    GamePanel gp;

    public CollisionDetector(GamePanel gp){
        this.gp = gp;
    }

    public void checkTile(Entity entity){

        //check whether a player is hitting a solid tile or not.

        //here we need 4 things.
        //we need to find the solid area of the player's world left x , world right x , world top y , world bottom y , for the solid area , not the player. 
        
        int entity_world_left_X = entity.worldX + entity.solidArea.x;
        int entity_world_right_x = entity.worldX + entity.solidArea.x + entity.solidArea.width;

        int entity_world_top_y = entity.worldY + entity.solidArea.y;
        int entity_world_bottom_y = entity.worldY + entity.solidArea.y + entity.solidArea.height;

        //row and col of the player.
        int entity_left_col = entity_world_left_X/gp.tileSize;
        int entity_right_col = entity_world_right_x/gp.tileSize;

        int entity_top_row = entity_world_top_y/gp.tileSize;
        int entity_bottom_row = entity_world_bottom_y/gp.tileSize;

        int tileNum1 , tileNum2; //this is used for tile index of the tile in the world map
        switch(entity.direction){
            case "up":
                entity_top_row = (entity_world_top_y - entity.speed)/gp.tileSize; //predicting after movement where top row would be for entity. upwards y means relatively speed we have to increase to predict.
                tileNum1 = gp.tileM.mapTileNum[entity_left_col][entity_top_row];
                tileNum2 = gp.tileM.mapTileNum[entity_right_col][entity_top_row];
                if(gp.tileM.tile[tileNum1].collision == true || gp.tileM.tile[tileNum2].collision == true) entity.playerCollision = true;
                break;
            case "down" :
                entity_bottom_row = (entity_world_bottom_y + entity.speed)/gp.tileSize;
                tileNum1 = gp.tileM.mapTileNum[entity_left_col][entity_bottom_row];
                tileNum2 = gp.tileM.mapTileNum[entity_right_col][entity_bottom_row];
                if(gp.tileM.tile[tileNum1].collision == true || gp.tileM.tile[tileNum2].collision == true) entity.playerCollision = true;
                break;
            case "left" :
                entity_left_col = (entity_world_left_X - entity.speed)/gp.tileSize;
                tileNum1 = gp.tileM.mapTileNum[entity_left_col][entity_top_row];
                tileNum2 = gp.tileM.mapTileNum[entity_left_col][entity_bottom_row];
                if(gp.tileM.tile[tileNum1].collision == true || gp.tileM.tile[tileNum2].collision == true) entity.playerCollision = true;
                break;
            case "right":
                entity_right_col = (entity_world_right_x + entity.speed)/gp.tileSize;
                tileNum1 = gp.tileM.mapTileNum[entity_right_col][entity_top_row];
                tileNum2 = gp.tileM.mapTileNum[entity_right_col][entity_bottom_row];
                if(gp.tileM.tile[tileNum1].collision == true || gp.tileM.tile[tileNum2].collision == true) entity.playerCollision = true;
                break;
        }

    }
}
