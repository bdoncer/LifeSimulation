package agh.ics.oop.objects;

import agh.ics.oop.data.Vector2d;

public class Grass extends AbstractWorldMapElement {
    public Grass(Vector2d position){
        this.position = position;
    }

    //niepotrzebne funkcje
    public String toString(){
        return "*";
    }
    public int getIndex(){
        return 0;
    }
    public int getEnergyIndex(){
        return 0;
    }
    @Override
    public Object getStringGenes() {
        return null;
    }
    public float getEnergy(){
        return 0;
    }
}
