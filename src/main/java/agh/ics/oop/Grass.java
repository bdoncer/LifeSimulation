package agh.ics.oop;

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
    public float getEnergy(){
        return 0;
    }
}
