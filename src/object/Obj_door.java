package object;

import java.io.IOException;

import javax.imageio.ImageIO;

public class Obj_door extends SuperObject {
    public Obj_door() {
        this.name = "door";
        this.bounce = false;

        try {
            this.ObjectImage = ImageIO.read(getClass().getResourceAsStream("/objects-assets/door.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.objectCollision = true ; //for door there will be collision

        
    }
}
