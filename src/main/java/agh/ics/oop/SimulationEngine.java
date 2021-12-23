package agh.ics.oop;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SimulationEngine implements IEngine,Runnable{
    private final int startAnimals;
    private MoveDirection[] moves;
    private IWorldMap map;
    protected List<IEngineMoveObserver> observers = new ArrayList<>();
    int moveDelay;
    boolean magic;
    ArrayList<Animal> animals = new ArrayList<Animal>();
    public SimulationEngine(IWorldMap map,int startAnimals,int moveDelay,boolean isMagic){
        this.map = map;
        this.moveDelay = moveDelay;
        this.moves = moves;
        this.magic = isMagic;
        this.startAnimals = startAnimals;
        int width = map.getWidth();
        int height = map.getHeight();

        //umieszczam poczatkowe zwierzeta
        int i = 0;
        while(i<startAnimals){
            int x = getRandomNumber(0,width+1);
            int y = getRandomNumber(0,height+1);
            Vector2d pos = new Vector2d(x,y);
            if (!map.isOccupied(pos)){
                Animal animal = new Animal(map,new Vector2d(x,y),"s",null,null,null);
                map.place(animal);
                animals.add(animal);
                i+=1;
            }

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
        int magicDays = 0;
        int numOfAnimals = this.startAnimals;
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

                numOfAnimals -= animalsToRemove.size();

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
                        if (parents[0].getEnergy() >= 0.5*map.getStartEnergy() && parents[1].getEnergy() >= 0.5*map.getStartEnergy())
                        {
                            Animal child = new Animal(map,pos,"c",parents[0],parents[1],null);
                            animalsToAdd.add(child);
                        }
                    }
                }
                for (Animal animal: animalsToAdd) {
                    animals.add(animal);
                    map.place(animal);
                }
                numOfAnimals += animalsToAdd.size();
                //jesli magiczna i jest 5 zwierzatek to dodaje je do mapy
                if (magic && magicDays < 3 && numOfAnimals == 5)
                {
                    ArrayList<Animal> animalsToCopy = new ArrayList<>();
                    for(Animal a:animals){
                        int x = getRandomNumber(0,map.getWidth()+1);
                        int y = getRandomNumber(0,map.getHeight()+1);
                        Animal copy = new Animal(map,new Vector2d(x,y),"copy",null,null,a);
                        animalsToCopy.add(copy);
                    }
                    for (Animal animal: animalsToCopy) {
                        animals.add(animal);
                        map.place(animal);
                    }
                    magicDays += 1;
                    numOfAnimals += 5;
                }
                //dodaje trawke
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
