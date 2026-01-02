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
        mapTileNum = new int[gp.maxScreenCol][gp.maxScreenRow]; //map tile number 2d matrix will store all the numbers from thge map.txt file.
        getTileImage();
        //load the map
        loadMap("/maps/map_01.txt");
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

    //load the map
    public void loadMap(String mapLocation){
        try {
            //open the map text file , and read it efficiently.
            InputStream is = getClass().getResourceAsStream(mapLocation); //finds the resource , open it as stream of bytes , return a handle (InputStream) to read those bytes sequentially.
            BufferedReader br = new BufferedReader(new InputStreamReader(is)); //as input stream is raw bytes , u need to convert bytes -> charecters , this input stream reader does exactly that , buffered reader reads a biug chunk at once , stores it in memory , lets yiu read line by line efficiently.

            int col = 0;
            int row = 0;
            
            while(col < gp.maxScreenCol && row < gp.maxScreenRow){
                String line = br.readLine(); //it is gonna read a single line from the buffered reader and put into the String line var.
                //after we get the line we gonna get the numbers from the line
                while(col < gp.maxScreenCol){
                    String numbers[] = line.split(" "); //split by space gives us the charecters.
                    int num = Integer.parseInt(numbers[col]); //convert the current number from string to integer.
                    //then we store the extracted number in the mapTileNum 2d array we had.
                    mapTileNum[col][row] = num; //for that col and that row inseer that number we just got.
                    //incrment col.
                    col++;
                }
                if(col == gp.maxScreenCol){
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
        // g2.drawImage(tile[0].tileImage, 0, 0 , gp.tileSize , gp.tileSize, null);
        // g2.drawImage(tile[1].tileImage, 48, 0 , gp.tileSize , gp.tileSize, null);
        // g2.drawImage(tile[2].tileImage, 96, 0, gp.tileSize , gp.tileSize,null);

        //we dont draw like this . that is a noobie approach.
        //we can use a while loop here to do this .
        //later we will build inifite world system like minecraft.

        int col = 0;
        int row = 0;
        int x = 0;
        int y = 0;

        //display entire map.
        while(col < gp.maxScreenCol && row < gp.maxScreenRow){
            int tileNum = mapTileNum[col][row]; //get the number for that current index.
            g2.drawImage(tile[tileNum].tileImage, x, y , gp.tileSize , gp.tileSize , null);
            col++; //increment col
            x = x + gp.tileSize;
            if(col == gp.maxScreenCol){
                col = 0;
                x = 0;
                row++;
                y = y + gp.tileSize;
            }
            
        }
    }
}
