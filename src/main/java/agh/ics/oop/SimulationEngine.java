package agh.ics.oop;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SimulationEngine implements IEngine,Runnable{
    private MoveDirection[] moves;
    private IWorldMap map;
    protected List<IEngineMoveObserver> observers = new ArrayList<>();
    int moveDelay;
    ArrayList<Animal> animals = new ArrayList<Animal>();
    public SimulationEngine(IWorldMap map,int startAnimals,int moveDelay){
        this.map = map;
        this.moveDelay = moveDelay;
        this.moves = moves;
        int width = map.getWidth();
        int height = map.getHeight();

        for(int i = 0;i<startAnimals;i++){
            int x = getRandomNumber(0,width);
            int y = getRandomNumber(0,height);
            Animal animal = new Animal(map,new Vector2d(x,y),"s",null,null);
            map.place(animal);
            animals.add(animal);
        }
    }
    public int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
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
            if (animals.size() > 0){
                animals.get(j).move(moves[i]);
            }


            //zmniejszam mu energie, ktora zuzyl na ruch
            if (animals.size() > 0){
                animals.get(j).energy -= map.getMoveEnergy();
            }

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
                        Animal[] parents = map.getParents(pos);
                        Animal child = new Animal(map,pos,"c",parents[0],parents[1]);
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
