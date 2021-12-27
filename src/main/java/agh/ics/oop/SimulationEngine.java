package agh.ics.oop;

import agh.ics.oop.gui.AllCharts;
import agh.ics.oop.gui.MagicInformation;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.List;

public class SimulationEngine implements IEngine,Runnable{
    private final int startAnimals;
    private AbstractWorldMap map;
    protected List<IEngineMoveObserver> observers = new ArrayList<>();
    int moveDelay;
    boolean magic;
    boolean isPaused = false;
    int numOfAnimals;
    AllCharts allCharts;
    ArrayList<Animal> animals = new ArrayList<Animal>();
    MagicInformation magInf;
    public SimulationEngine(AbstractWorldMap map, int startAnimals, int moveDelay, boolean isMagic, AllCharts allCharts, MagicInformation magic){
        this.map = map;
        this.moveDelay = moveDelay;
        this.magic = isMagic;
        this.startAnimals = startAnimals;
        this.allCharts = allCharts;
        this.magInf = magic;
        int width = map.getWidth();
        int height = map.getHeight();
        numOfAnimals = this.startAnimals;
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

    public void pauseThread(){
        isPaused = true;
    }
    public void continueThread(){
       synchronized (this){
           notify();
       }
       isPaused = false;
    }
    public boolean getIsPaused(){
        return isPaused;
    }
    public int getNumOfAnimals(){
        return this.numOfAnimals;
    }
    public int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }
    public void addObserver(IEngineMoveObserver observer){
        observers.add(observer);
    }
    public void removeObserver(IEngineMoveObserver observer){
        observers.remove(observer);
    }


    public void run() {
        int magicDays = 0;
        Integer days = 0;
        int j = 0;
        int numOfDeadAnimals = 0;
        int lifeLengthOfDeadAnimals = 0;
        while (numOfAnimals>0){
            try {
                synchronized (this){
                    while(isPaused){
                        wait();
                    }
                }
                Thread.sleep(moveDelay);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //poruszam zwierzatkiem
            animals.get(j).move(animals.get(j).getMove());
            //zmniejszam mu energie, ktora zuzyl na ruch
            animals.get(j).energy -= map.getMoveEnergy();
            j+=1;
            if (j == animals.size()){
                Platform.runLater(() -> {
                    magInf.takeAwayMagic();
                });
                j = 0;
                ArrayList<Animal> animalsToRemove = new ArrayList<>();
                //usuwam martwe zwierzeta
                for(Animal a:animals)
                {
                    if(a.energy <= 0){
                        animalsToRemove.add(a);
                        numOfDeadAnimals += 1;
                        lifeLengthOfDeadAnimals += a.lifeLength;
                    }
                }
                for (Animal a: animalsToRemove) {
                    animals.remove(a);
                    map.removeElement(a,a.getPosition());
                }
                numOfAnimals -= animalsToRemove.size();

                //dodaje dzien do dlugosci zycia tych, ktore przezyly
                for(Animal a:animals) {
                    a.lifeLength += 1;
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
                        if (parents[0].getEnergy() >= 0.5*map.getStartEnergy() && parents[1].getEnergy() >= 0.5*map.getStartEnergy())
                        {
                            parents[0].numOfChildren += 1;
                            parents[1].numOfChildren += 1;
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
                if (magic && magicDays < 3 && numOfAnimals == 5) {
                    Platform.runLater(() -> {
                        magInf.giveMagic();
                    });
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
                days+=1;
                //obliczam sredni poziom energii
                int allEnergy = 0;
                for(Animal a:animals) {
                    allEnergy += a.energy;
                }
                //obliczam srednia ilosc dzieci
                int allChildren = 0;
                for(Animal a:animals) {
                    allChildren += a.numOfChildren;
                }
                //aktualizuje wykresy
                Integer finalDays = days;
                Platform.runLater(() -> {
                    allCharts.getCharts().get(0).addData(finalDays.toString(),numOfAnimals);
                });
                Integer finalDays1 = days;
                Platform.runLater(() -> {
                    allCharts.getCharts().get(1).addData(finalDays1.toString(),map.getNumOfGrass());
                });
                int finalAllEnergy = allEnergy;
                Integer finalDays2 = days;
                Platform.runLater(() -> {
                    allCharts.getCharts().get(2).addData(finalDays2.toString(),(float) finalAllEnergy /numOfAnimals);
                });
                int finalNumOfDeadAnimals = numOfDeadAnimals;
                int finalLifeLengthOfDeadAnimals = lifeLengthOfDeadAnimals;
                Integer finalDays3 = days;
                Integer finalDays4 = days;
                Platform.runLater(() -> {
                    if (finalNumOfDeadAnimals != 0){
                        allCharts.getCharts().get(3).addData(finalDays3.toString(),(float) finalLifeLengthOfDeadAnimals / finalNumOfDeadAnimals);
                    }
                    else{
                        allCharts.getCharts().get(3).addData(finalDays4.toString(),0.0);
                    }
                });
                int finalAllChildren = allChildren;
                Integer finalDays5 = days;
                Platform.runLater(() -> {
                    allCharts.getCharts().get(4).addData(finalDays5.toString(),(float) finalAllChildren /numOfAnimals);
                });
            }
            for(IEngineMoveObserver obs:observers){
                obs.mapChanged(map);
            }
        }
    }
}
