package agh.ics.oop;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * The interface responsible for interacting with the map of the world.
 * Assumes that Vector2d and MoveDirection classes are defined.
 *
 * @author apohllo
 *
 */
public interface IWorldMap {
    /**
     * Indicate if any object can move to the given position.
     *
     * @param position
     *            The position checked for the movement possibility.
     * @return True if the object can move to that position.
     */
    boolean canMoveTo(Vector2d position);

    /**
     * Place a animal on the map.
     *
     * @param animal
     *            The animal to place on the map.
     * @return True if the animal was placed. The animal cannot be placed if the map is already occupied.
     */
    boolean place(Animal animal);

    /**
     * Return true if given position on the map is occupied. Should not be
     * confused with canMove since there might be empty positions where the animal
     * cannot move.
     *
     * @param position
     *            Position to check.
     * @return True if the position is occupied.
     */

    /**
     * Return an object at a given position.
     *
     * @param position
     *            The position of the object.
     * @return Object or null if the position is not occupied.
     */
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
}