package agh.ics.oop.interfaces;

import agh.ics.oop.data.Vector2d;

public interface IMapElement {
    public Vector2d getPosition();

    float getEnergy();

    int getIndex();

    int getEnergyIndex();

    Object getStringGenes();
}
