package inventory;

import java.awt.image.BufferedImage;

public class Item {
    public String name;
    public BufferedImage icon;

    public Item(String name , BufferedImage icon){
        this.name = name;
        this.icon = icon;
    }
}
