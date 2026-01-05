package main;

import object.Obj_chest;
import object.Obj_door;
import object.Obj_key;

public class AssetPlacer {
    GamePanel gp;

    public AssetPlacer(GamePanel gp) {
        this.gp = gp;
    }

    public void setObject() {

        //key
        gp.obj[0] = new Obj_key(); //place key in the object.
        gp.obj[0].worldX = 23 * gp.tileSize;
        gp.obj[0].worldY = 7 * gp.tileSize;

        //key
        gp.obj[1] = new Obj_key();
        gp.obj[1].worldX = 23 * gp.tileSize;
        gp.obj[1].worldY = 40 * gp.tileSize;

        //key
        gp.obj[2] = new Obj_key();
        gp.obj[2].worldX = 37 * gp.tileSize;
        gp.obj[2].worldY = 7 * gp.tileSize;

        
        //door
        gp.obj[3] = new Obj_door();
        gp.obj[3].worldX = 10 * gp.tileSize;
        gp.obj[3].worldY = 11 * gp.tileSize;

        //door
        gp.obj[4] = new Obj_door();
        gp.obj[4].worldX = 8 * gp.tileSize;
        gp.obj[4].worldY = 28 * gp.tileSize;

        //door
        gp.obj[5] = new Obj_door();
        gp.obj[5].worldX = 12 * gp.tileSize;
        gp.obj[5].worldY = 22 * gp.tileSize;
        
        //chest
        gp.obj[6] = new Obj_chest();
        gp.obj[6].worldX = 10 * gp.tileSize;
        gp.obj[6].worldY = 7 * gp.tileSize;

        //key
        gp.obj[7] = new Obj_key();
        gp.obj[7].worldX = 26 * gp.tileSize;
        gp.obj[7].worldY = 7 * gp.tileSize;

        //key
        gp.obj[8] = new Obj_key();
        gp.obj[8].worldX = 20 * gp.tileSize;
        gp.obj[8].worldY = 7 * gp.tileSize;

        //key
        gp.obj[9] = new Obj_key();
        gp.obj[9].worldX = 20 * gp.tileSize;
        gp.obj[9].worldY = 10 * gp.tileSize;

        //key
        gp.obj[10] = new Obj_key();
        gp.obj[10].worldX = 21 * gp.tileSize;
        gp.obj[10].worldY = 14 * gp.tileSize;

        //key
        gp.obj[11] = new Obj_key();
        gp.obj[11].worldX = 24 * gp.tileSize;
        gp.obj[11].worldY = 20 * gp.tileSize;
    }
}
