package agh.ics.oop.interfaces;

import agh.ics.oop.objects.Animal;
import agh.ics.oop.data.Vector2d;

import java.util.ArrayList;
import java.util.HashMap;

public interface IWorldMap {

    boolean canMoveTo(Vector2d position);

    boolean place(Animal animal);

    Object objectAt(Vector2d position);

    void addGrass(int n);

    boolean isOccupied(Vector2d currentPosition);

    void addJungleGrass(int i);

    int getStartEnergy();

    int getMoveEnergy();

    int getPlantEnergy();

    HashMap<Vector2d, ArrayList<IMapElement>> getmapElements();

    int getWidth();

    int getHeight();

    void removeElement(IMapElement a, Vector2d tmp);

    IMapElement isGrass(Vector2d el);

    ArrayList<Animal> getBest(Vector2d pos);

    Animal[] getParents(Vector2d pos);

    void addElement(IMapElement element, Vector2d position);

    int getNumOfGrass();
}