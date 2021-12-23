package agh.ics.oop;

import java.util.ArrayList;
import java.util.List;

public class Grass extends AbstractWorldMapElement {
    public Grass(Vector2d position){
        this.position = position;
    }
    public String toString(){
        return "*";
    }
    public String getUrl(){
        return "src/main/resources/grass.jpg";
    }
    public String getEnergyUrl(){
        return " ";
    }
    public int getEnergy(){
        return 0;
    }
}
