package agh.ics.oop.interfaces;

import agh.ics.oop.objects.Animal;
import agh.ics.oop.data.Vector2d;

public interface IPositionChangeObserver {
    public void positionChanged(Animal element, Vector2d newPosition);
}
