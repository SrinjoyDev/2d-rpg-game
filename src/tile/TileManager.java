package tile;

import java.awt.Graphics2D;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.imageio.ImageIO;

import main.GamePanel;

public class TileManager {
    
    GamePanel gp;
    Tile[] tile;
    final int tileLength = 10;
    int mapTileNum[][];

    public TileManager(GamePanel gp) {
        this.gp = gp;
        tile = new Tile[tileLength];
        //call the getTileImage in the constructor to get the image of the tiles when the class of tile loads.
        mapTileNum = new int[gp.maxWorldCol][gp.maxWorldRow]; //map tile number 2d matrix will store all the numbers from thge map.txt file.
        getTileImage();
        //load the map
        loadMap("/maps/world_01.txt");
    }

    public void getTileImage(){
        try {
            
            //instantiate the tiles in the tile array
            //grass:
            tile[0] = new Tile();
            tile[0].tileImage = ImageIO.read(getClass().getResourceAsStream("/tiles-assets/grass.png"));
            
            //wall:
            tile[1] = new Tile();
            tile[1].tileImage = ImageIO.read(getClass().getResourceAsStream("/tiles-assets/wall-adv.png"));

            //water:
            tile[2] = new Tile();
            tile[2].tileImage = ImageIO.read(getClass().getResourceAsStream("/tiles-assets/water-adv.png"));

            //earth
            tile[3] = new Tile();
            tile[3].tileImage = ImageIO.read(getClass().getResourceAsStream("/tiles-assets/earth-adv.png"));
            
            //tree
            tile[4] = new Tile();
            tile[4].tileImage = ImageIO.read(getClass().getResourceAsStream("/tiles-assets/tree-adv.png"));
            
            //sand
            tile[5] = new Tile();
            tile[5].tileImage = ImageIO.read(getClass().getResourceAsStream("/tiles-assets/sand-adv.png"));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isImageNull() {
       
        return tile[0] == null || tile[0].tileImage == null;
       
    }

    //load the map
    public void loadMap(String mapLocation){
        try {
            //open the map text file , and read it efficiently.
            InputStream is = getClass().getResourceAsStream(mapLocation); //finds the resource , open it as stream of bytes , return a handle (InputStream) to read those bytes sequentially.
            BufferedReader br = new BufferedReader(new InputStreamReader(is)); //as input stream is raw bytes , u need to convert bytes -> charecters , this input stream reader does exactly that , buffered reader reads a biug chunk at once , stores it in memory , lets yiu read line by line efficiently.

            int col = 0;
            int row = 0;

            //2d game mechanics 101
            
            while(col < gp.maxWorldCol && row < gp.maxWorldRow){
                String line = br.readLine(); //it is gonna read a single line from the buffered reader and put into the String line var.
                //after we get the line we gonna get the numbers from the line
                while(col < gp.maxWorldCol){
                    String numbers[] = line.split(" "); //split by space gives us the charecters.
                    int num = Integer.parseInt(numbers[col]); //convert the current number from string to integer.
                    //then we store the extracted number in the mapTileNum 2d array we had.
                    mapTileNum[col][row] = num; //for that col and that row inseer that number we just got.
                    //incrment col.
                    col++;
                }
                if(col == gp.maxWorldCol){
                    col = 0;
                    row++;
                }
            }
            //close the buffered reader.
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void draw(Graphics2D g2){

        //TODO : infinite world map likke minecraft but in 2d.

        int worldCol = 0;
        int worldRow = 0;
        // int x = 0; deprecated use for single map not world maps
        // int y = 0; deprecated use for single map not world maps

        //display entire map.
        while(worldCol < gp.maxWorldCol && worldRow < gp.maxWorldRow){

            int tileNum = mapTileNum[worldCol][worldRow]; //get the number for that current index.

            //2d game mechanics 101

            //relative co-ordinate calc >
            int worldX = worldCol * gp.tileSize; //x cord relative to world
            int worldY = worldRow * gp.tileSize; //y cord relative to world

            //camera transformation that makes the world move around the player.
            int screenX = worldX - gp.player.worldX + gp.player.screenX; //worldx - gp.playerWorldX -> centre the player at centre of the world , + gp.player.screenX -> place them at the centre of the screen.
            int screenY = worldY - gp.player.worldY + gp.player.screenY; //same logic how x works

            //2d game mechanics 101

            //optimisation , so that we dont draw the entire map at once , we render the map to what player can only see
            if(screenX > -gp.tileSize && //left edge of tile is not too far left
                screenX < gp.screenWidth + gp.tileSize && //right edge of tile is not too far right
                screenY > -gp.tileSize && //top edge of tile is not too far top
                screenY < gp.screenHeight + gp.tileSize //bottolm edge of tile is not too far bottom..
            ) {
                //only draw the tiles if it is within the extended viewport.
                g2.drawImage(tile[tileNum].tileImage, screenX, screenY , gp.tileSize , gp.tileSize , null);
            }

            worldCol++; //increment col
            // x = x + gp.tileSize; deprecated use for single map not world maps
            if(worldCol == gp.maxWorldCol){
                worldCol = 0;
                // x = 0; deprecated use for single map not world maps
                worldRow++;
                // y = y + gp.tileSize; deprecated use for single map not world maps
            }
            
        }
    }
}
