package main;

import entity.Entity;

public class CollisionDetector {
    GamePanel gp;

    public CollisionDetector(GamePanel gp){
        this.gp = gp;
    }

    /*
        METHOD to check whether a player will collide with a tile or not.
        if it moves in its current directionm , if yes then sets entity.playerCollison = true to prevent movemetns.
        predicts entity's next position and checks ahead.
        called every frame , before actual movement (from the Player class.)

        NOTE: we are not defining solid area for the tile , and using the intersect method here because
        that will be inefficient as we wll be checking all the tiles , that will be inefficient and slow down the performance.
        so instead what we are doing is that we are checking the tile's collision flag on each direction for 2 tiles on the direction
        coz on each direction only 2 tiles can occur.
    */
    public void checkTile(Entity entity){

        //check whether a player is hitting a solid tile or not.

        //here we need 4 things.
        //we need to find the solid area of the player's world left x , world right x , world top y , world bottom y , for the solid area , not the player. 
        
        //1. GET ENTITY'S SOLID AREA POSITION IN THE WORLD
        int entity_world_left_X = entity.worldX + entity.solidArea.x; //offset of solid area from world x for the entity
        int entity_world_right_x = entity.worldX + entity.solidArea.x + entity.solidArea.width; //left + width covers so gives us the right.

        int entity_world_top_y = entity.worldY + entity.solidArea.y;
        int entity_world_bottom_y = entity.worldY + entity.solidArea.y + entity.solidArea.height; //top + height gives us the bottom

        //2.convert solid area to grid co-ordinates.
        // tiles are in a grid , to get grid indices we divide by tileSize.
        int entity_left_col = entity_world_left_X/gp.tileSize;
        int entity_right_col = entity_world_right_x/gp.tileSize;

        int entity_top_row = entity_world_top_y/gp.tileSize;
        int entity_bottom_row = entity_world_bottom_y/gp.tileSize;

        int tileNum1 , tileNum2; //this is used for tile index of the tile in the world map

        //3. direction based prediction and collision check.
        //for each direction we will predict where the entity will be after movement and check the tile at that position for collision flag.
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

    /*
        METHOD to check whether an entity is colliding with any object in the game world.
        if yes , sets entity.playerCollision = true to prevent movement.
        called every frame before actual movement (from the Player class.)
        returns the index of the object collided with , or 999 if no collision.
    */
    public int checkObject(Entity entity , boolean player) {
        //we check  if the entity is colliding with the object or not , if yes then we return the index of the oject.
        int index = 999;

        for(int i =  0 ; i < gp.obj.length ; i++){
            if(gp.obj[i] != null ){ //if that object at that index is not null
                
                //get entity solid area position
                entity.solidArea.x += entity.worldX;
                entity.solidArea.y += entity.worldY;

                //get object solid area position
                gp.obj[i].solidArea.x += gp.obj[i].worldX;
                gp.obj[i].solidArea.y += gp.obj[i].worldY;

                switch(entity.direction){
                    
                    case "up":
                        //simulate entity movemenbnt when direction is up
                        entity.solidArea.y -= entity.speed;
                        if(entity.solidArea.intersects(gp.obj[i].solidArea)){
                            gp.obj[i].is_player_close_to_object = true;
                            if(gp.obj[i].objectCollision == true){
                                //if object collision is true
                                entity.playerCollision = true; //make the player as collided
                            }
                            //check for player
                            if(player == true){
                                index = i;
                            }//for npc monsters they donbt interact with object.
                        }
                        break;

                    case "down":
                        entity.solidArea.y += entity.speed;
                        if(entity.solidArea.intersects(gp.obj[i].solidArea)){
                            gp.obj[i].is_player_close_to_object = true;
                            if(gp.obj[i].objectCollision == true){
                                //if object collision is true
                                entity.playerCollision = true; //make the player as collided/
                            }
                            //check for player
                            if(player == true){
                                index = i;
                            }//for npc monsters they donbt interact with object.
                        }
                        break;

                    case "left":
                        entity.solidArea.x -= entity.speed;
                        if(entity.solidArea.intersects(gp.obj[i].solidArea)){
                            gp.obj[i].is_player_close_to_object = true;
                            if(gp.obj[i].objectCollision == true){
                                //if object collision is true
                                entity.playerCollision = true; //make the player as collided/
                                //make the flag player close to object as true
                            }
                            //check for player
                            if(player == true){
                                index = i;
                            }//for npc monsters they donbt interact with object.
                        }
                        break;

                    case "right":
                        entity.solidArea.x += entity.speed;
                        if(entity.solidArea.intersects(gp.obj[i].solidArea)){
                            gp.obj[i].is_player_close_to_object = true;
                            if(gp.obj[i].objectCollision == true){
                                //if object collision is true
                                entity.playerCollision = true; //make the player as collided
                            }
                            //check for player
                            if(player == true){
                                index = i;
                            }//for npc monsters they donbt interact with object.
                        }
                        break;
                }

                //reset the entity solid area

                //entity
                entity.solidArea.x = entity.solid_area_default_X;
                entity.solidArea.y = entity.solid_area_default_Y;

                //object
                gp.obj[i].solidArea.x = gp.obj[i].solid_area_default_X;
                gp.obj[i].solidArea.y = gp.obj[i].solid_area_default_Y;
            }
        }
        return index;
    }
}
