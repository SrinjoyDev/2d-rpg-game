package object;

import java.io.IOException;

import javax.imageio.ImageIO;

public class Obj_chest extends SuperObject {

    public Obj_chest() {
        this.name = "key";
        this.bounce = false;
        
        try {
            this.ObjectImage = ImageIO.read(getClass().getResourceAsStream("/objects-assets/chest.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
