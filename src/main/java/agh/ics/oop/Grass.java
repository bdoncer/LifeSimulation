package agh.ics.oop;

public class Grass extends AbstractWorldMapElement {
    public Grass(Vector2d position){
        this.position = position;
    }
    public String toString(){
        return "*";
    }
    public int getIndex(){
        return 0;
    }
    public int getEnergyIndex(){
        return 0;
    }
    public float getEnergy(){
        return 0;
    }
}
