//to z przechodzeniem
package agh.ics.oop;


public class BendedMap extends AbstractWorldMap implements IWorldMap {
    public BendedMap(int width,int height,double jungleRatio,int startEnergy,int moveEnergy,int plantEnergy){
        this.width = width;
        this.height = height;
        this.jungleRatio = jungleRatio;
        this.startEnergy = startEnergy;
        this.moveEnergy = moveEnergy;
        this.plantEnergy = plantEnergy;
    }

    public boolean canMoveTo(Vector2d position) {
        return true;
    }




}