//to ze sztywnymi krawedziami

package agh.ics.oop;

public class RectangularMap extends AbstractWorldMap implements IWorldMap {
    public RectangularMap(int width,int height, double jungleRatio,int startEnergy,int moveEnergy,int plantEnergy){
        this.width = width;
        this.height = height;
        this.jungleRatio = jungleRatio;
        this.startEnergy = startEnergy;
        this.moveEnergy = moveEnergy;
        this.plantEnergy = plantEnergy;
    }
    @Override
    public boolean canMoveTo(Vector2d position) {
        if (position.x < 0 || position.x > width || position.y < 0 || position.y > height){
            return false;
        }
        return true;
    }





}
