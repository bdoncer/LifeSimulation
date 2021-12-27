package agh.ics.oop.gui;

import javafx.scene.image.Image;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class AllImages {
    public Image[] dog = new Image[8];
    public Image[] energy = new Image[5];
    public Image grass;
    public AllImages() throws FileNotFoundException {
        grass = new Image(new FileInputStream("src/main/resources/grass.jpg"));
        dog[0] = new Image(new FileInputStream("src/main/resources/1.jpg"));
        dog[1] = new Image(new FileInputStream("src/main/resources/2.jpg"));
        dog[2] = new Image(new FileInputStream("src/main/resources/3.jpg"));
        dog[3] = new Image(new FileInputStream("src/main/resources/4.jpg"));
        dog[4] = new Image(new FileInputStream("src/main/resources/5.jpg"));
        dog[5] = new Image(new FileInputStream("src/main/resources/6.jpg"));
        dog[6] = new Image(new FileInputStream("src/main/resources/7.jpg"));
        dog[7] = new Image(new FileInputStream("src/main/resources/8.jpg"));
        energy[0] = new Image(new FileInputStream("src/main/resources/energy1.jpg"));
        energy[1] = new Image(new FileInputStream("src/main/resources/energy2.jpg"));
        energy[2] = new Image(new FileInputStream("src/main/resources/energy3.jpg"));
        energy[3] = new Image(new FileInputStream("src/main/resources/energy4.jpg"));
        energy[4] = new Image(new FileInputStream("src/main/resources/energy5.jpg"));
    }
}
