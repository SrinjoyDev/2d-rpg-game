package tile;

import java.awt.Graphics2D;

import javax.imageio.ImageIO;

import main.GamePanel;

public class TileManager {
    
    GamePanel gp;
    Tile[] tile;
    final int tileLength = 10;

    public TileManager(GamePanel gp) {
        this.gp = gp;
        tile = new Tile[tileLength];
        //call the getTileImage in the constructor to get the image of the tiles when the class of tile loads.
        getTileImage();
    }

    public void getTileImage(){
        try {
            
            //instantiate the tiles in the tile array
            //grass:
            tile[0] = new Tile();
            tile[0].tileImage = ImageIO.read(getClass().getResourceAsStream("/tiles-assets/grass-adv.png"));
            
            //wall:
            tile[1] = new Tile();
            tile[1].tileImage = ImageIO.read(getClass().getResourceAsStream("/tiles-assets/wall-adv.png"));

            //water:
            tile[2] = new Tile();
            tile[2].tileImage = ImageIO.read(getClass().getResourceAsStream("/tiles-assets/water-adv.png"));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isImageNull() {
       
        return tile[0] == null || tile[0].tileImage == null;
       
    }

    public void draw(Graphics2D g2){
        g2.drawImage(tile[0].tileImage, 0, 0 , gp.tileSize , gp.tileSize, null);
        g2.drawImage(tile[1].tileImage, 48, 0 , gp.tileSize , gp.tileSize, null);
        g2.drawImage(tile[2].tileImage, 96, 0, gp.tileSize , gp.tileSize,null);
    }
}
