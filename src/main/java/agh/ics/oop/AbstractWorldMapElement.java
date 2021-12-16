package agh.ics.oop;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractWorldMapElement implements IMapElement{
    protected Vector2d position;
    protected List<IPositionChangeObserver> observers = new ArrayList<>();
    public Vector2d getPosition() {
        return this.position;
    }

    void addObserver(IPositionChangeObserver observer){
        observers.add(observer);
    }
    void removeObserver(IPositionChangeObserver observer){
        observers.remove(observer);
    }
    void positionChanged(Vector2d newPosition){
        for(IPositionChangeObserver obs:observers){
            obs.positionChanged((Animal) this, newPosition);
        }
    }

}