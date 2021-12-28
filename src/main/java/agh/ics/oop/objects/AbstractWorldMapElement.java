package agh.ics.oop.objects;


import agh.ics.oop.data.Vector2d;
import agh.ics.oop.interfaces.IMapElement;
import agh.ics.oop.interfaces.IPositionChangeObserver;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractWorldMapElement implements IMapElement {
    public Vector2d position;
    protected List<IPositionChangeObserver> observers = new ArrayList<>();

    public Vector2d getPosition() {
        return this.position;
    }

    public void addObserver(IPositionChangeObserver observer){
        observers.add(observer);
    }

    protected void positionChanged(Vector2d newPosition){
        for(IPositionChangeObserver obs:observers){
            obs.positionChanged((Animal) this, newPosition);
        }
    }


}