package tile;

import java.awt.Graphics2D;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.imageio.ImageIO;

import main.GamePanel;

public class TileManager {
    
    GamePanel gp;
    public Tile[] tile; //array of objects whrere we will store each tile object.
    final int tileLength = 10; //number of different tiles we have in the game.
    public int mapTileNum[][]; //2d array that will store the numbers of each tile from the map text file.

    public TileManager(GamePanel gp) {
        this.gp = gp;
        tile = new Tile[tileLength];

        mapTileNum = new int[gp.maxWorldCol][gp.maxWorldRow]; //map covers the entire wolrd. so size of the mapTileNum 2d array is max world col and max world row.
        
        //load tile images
        getTileImage();

        //load the map
        loadMap("/maps/world_01.txt");
    }

    public void getTileImage(){
        try {
            
            //grass:
            tile[0] = new Tile();
            tile[0].tileImage = ImageIO.read(getClass().getResourceAsStream("/tiles-assets/grass.png"));
            tile[0].collision = false;

            //wall:
            tile[1] = new Tile();
            tile[1].tileImage = ImageIO.read(getClass().getResourceAsStream("/tiles-assets/wall.png"));
            tile[1].collision = true;

            //water:
            tile[2] = new Tile();
            tile[2].tileImage = ImageIO.read(getClass().getResourceAsStream("/tiles-assets/water-adv.png"));
            tile[2].collision = true;

            //earth
            tile[3] = new Tile();
            tile[3].tileImage = ImageIO.read(getClass().getResourceAsStream("/tiles-assets/earth-adv.png"));
            
            //tree
            tile[4] = new Tile();
            tile[4].tileImage = ImageIO.read(getClass().getResourceAsStream("/tiles-assets/tree.png"));
            tile[4].collision = true;
            //sand
            tile[5] = new Tile();
            tile[5].tileImage = ImageIO.read(getClass().getResourceAsStream("/tiles-assets/sand.png"));
            tile[5].collision = false;

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isImageNull() {
       
        return tile[0] == null || tile[0].tileImage == null;
       
    }

    /*
        LOAD MAP:
        read the map from the map string we have line by line.
        and parse each row's columns.
    */
    public void loadMap(String mapLocation){
        try {
            //open the map text file , and read it efficiently.
            InputStream is = getClass().getResourceAsStream(mapLocation); //finds the resource , open it as stream of bytes , return a handle (InputStream) to read those bytes sequentially.
            BufferedReader br = new BufferedReader(new InputStreamReader(is)); //as input stream is raw bytes , u need to convert bytes -> charecters , this input stream reader does exactly that , buffered reader reads a biug chunk at once , stores it in memory , lets yiu read line by line efficiently.

            int col = 0;
            int row = 0;

            //2d game mechanics 101
            
            while(row < gp.maxWorldRow){ //constraint over rows to not read more rows than our world supports.
                String line = br.readLine(); //read tghe charecters from the buffer reader. in a single line.
                //after we get the line we gonna get the numbers from the line
                while(col < gp.maxWorldCol){ //after reading the line , we proces each column of that line basiclly each number of that line
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

    /*
        render map. --> This method is called every frame from the game panel class.
        NOTE : this mechanics of map rendering is very basic.
        efficient for small maps.
        but for very large maps , we would have to implement load unload methods
        for example : load maps when player appproaches , and remove renders from the memory once player 
        goes past that part of the map , for efficient rendering.
    */
    public void draw(Graphics2D g2){

        //TODO : infinite world map likke minecraft but in 2d.

        //loop counters for iterating through the 2d map grid mapTileNum.
        int worldCol = 0;
        int worldRow = 0;

        //display entire map.
        while(worldCol < gp.maxWorldCol && worldRow < gp.maxWorldRow){

            //1. get the tile type. , 0 -> grass  , 1 , 2 , 3 , 4 , 5 ...etc
            
            int tileNum = mapTileNum[worldCol][worldRow];

            //2. absolute tile position in the world map
            
            int worldX = worldCol * gp.tileSize; //x cord relative to world
            int worldY = worldRow * gp.tileSize; //y cord relative to world

            //3. camera transformation , world -> screen.

            // 3.1 tile offset calc from player in screen.
            
            int x_cord_offset_from_player = worldX - gp.player.worldX;
            int y_cord_offset_from_player = worldY - gp.player.worldY;

            // 3.2 calc screen position to render the tile based on player position.
            
            int screenX = x_cord_offset_from_player + gp.player.screenX; //worldx - gp.playerWorldX -> centre the player at centre of the world , + gp.player.screenX -> place them at the centre of the screen.
            int screenY = y_cord_offset_from_player + gp.player.screenY; //same logic how x works

            //4. render what the player can only see -> viewport optimisation.
            //we render the edges also as buffer , to prevent bad experience of tiles popping in and out.

            //UNOPTIMAL APPROACH

            // if(screenX >= 0 && screenX < gp.screenWidth && screenY >= 0 && screenY < gp.screenHeight){
            //      g2.drawImage(tile[tileNum].tileImage, screenX, screenY , gp.tileSize , gp.tileSize , null);
            // }


            ///OPTIMAL APPROACH

            if(screenX > -gp.tileSize && //left edge of tile is not too far left
                screenX < gp.screenWidth + gp.tileSize && //right edge of tile is not too far right
                screenY > -gp.tileSize && //top edge of tile is not too far top
                screenY < gp.screenHeight + gp.tileSize //bottolm edge of tile is not too far bottom..
            ) {
                //only draw the tiles if it is within the extended viewport.
                g2.drawImage(tile[tileNum].tileImage, screenX, screenY , gp.tileSize , gp.tileSize , null);
            }

            worldCol++; //increment col
            if(worldCol == gp.maxWorldCol){
                worldCol = 0;
                worldRow++;
            }
        }
    }
}
