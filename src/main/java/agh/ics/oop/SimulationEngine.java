package agh.ics.oop;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SimulationEngine implements IEngine,Runnable{
    private MoveDirection[] moves;
    private IWorldMap map;
    private Vector2d[] positions;
    protected List<IEngineMoveObserver> observers = new ArrayList<>();
    int moveDelay;
    ArrayList<Animal> animals = new ArrayList<Animal>();
    public SimulationEngine(IWorldMap map,Vector2d[] positions,int moveDelay){
        this.map = map;
        this.positions = positions;
        this.moveDelay = moveDelay;
        this.moves = moves;
        for(Vector2d pos:positions){
            Animal animal = new Animal(map,pos);
            map.place(animal);
            animals.add(animal);
        }
    }
    public void movesSetter(MoveDirection[] moves){
        this.moves = moves;
    }
    public void addObserver(IEngineMoveObserver observer){
        observers.add(observer);
    }
    public void removeObserver(IEngineMoveObserver observer){
        observers.remove(observer);
    }
    @Override
    public void run() {
        int i = 0;
        int j=0;
        while (i<moves.length){
            try {
                Thread.sleep(moveDelay);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            animals.get(j).move(moves[i]);

            //zmniejszam mu energie, ktora zuzyl na ruch
            animals.get(j).energy = animals.get(j).energy - map.getMoveEnergy();
            //
            j+=1;
            if (j == animals.size()){
                j = 0;
                ArrayList<Animal> animalsToRemove = new ArrayList<>();
                //usuwam martwe zwierzeta
                for(Animal a:animals)
                {
                    if(a.energy <= 0){
                        animalsToRemove.add(a);
                    }
                }
                for (Animal a: animalsToRemove) {
                    animals.remove(a);
                    map.removeElement(a,a.getPosition());
                }
                //dodaje energie z roslinek
                ArrayList<Grass> grassToRemove = new ArrayList<>();
                for (Vector2d pos: map.getmapElements().keySet()){
                    if (map.isGrass(pos) != null){
                        ArrayList<Animal> strongest = map.getBest(pos);
                        for (Animal a:strongest){
                            a.addEnergy(map.getPlantEnergy()/strongest.size());
                        }
                        if (strongest.size() != 0){
                            grassToRemove.add((Grass) map.isGrass(pos));
                        }

                    }
                }
                if (grassToRemove.size() != 0){
                    for(Grass g:grassToRemove){
                        map.removeElement(g,g.getPosition());
                    }

                }
                //rozmnazam zwierzatka
                ArrayList<Animal> animalsToAdd = new ArrayList<>();
                for (Vector2d pos: map.getmapElements().keySet()){
                    if (map.getParents(pos) != null){
                        Animal child = new Animal(map,pos);
                        animalsToAdd.add(child);
                    }
                }
                for (Animal animal: animalsToAdd) {
                    animals.add(animal);
                    map.place(animal);

                }

                map.addGrass(1);
                map.addJungleGrass(1);
            }
            for(IEngineMoveObserver obs:observers){
                obs.mapChanged();
            }
            i+=1;
        }
    }
}
