package object;

import java.io.IOException;

import javax.imageio.ImageIO;

public class Obj_key extends SuperObject {
    
    public Obj_key() {
        this.name = "key";
        this.bounce = true;
        
        try {
            this.ObjectImage = ImageIO.read(getClass().getResourceAsStream("/objects-assets/key.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
