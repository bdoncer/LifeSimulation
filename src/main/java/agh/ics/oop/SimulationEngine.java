package agh.ics.oop;

import agh.ics.oop.data.CvsFile;
import agh.ics.oop.data.Vector2d;
import agh.ics.oop.gui.AllCharts;
import agh.ics.oop.gui.MagicInformation;
import agh.ics.oop.interfaces.IEngine;
import agh.ics.oop.interfaces.IEngineMoveObserver;
import agh.ics.oop.maps.AbstractWorldMap;
import agh.ics.oop.maps.BendedMap;
import agh.ics.oop.maps.RectangularMap;
import agh.ics.oop.objects.Animal;
import agh.ics.oop.objects.Grass;
import javafx.application.Platform;
import javafx.scene.control.Label;

import java.util.ArrayList;
import java.util.HashMap;
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
    ArrayList<Animal> animals = new ArrayList<>();
    MagicInformation magInf;
    ArrayList<String[]> toFile = new ArrayList<>();
    HashMap<String, Integer> genes = new HashMap();
    boolean toCsv = false;
    String dominantGenotype;
    Label forGenes;

    public SimulationEngine(AbstractWorldMap map, int startAnimals, int moveDelay, boolean isMagic, AllCharts allCharts, MagicInformation magic,Label forGenes){
        this.map = map;
        this.moveDelay = moveDelay;
        this.magic = isMagic;
        this.startAnimals = startAnimals;
        this.allCharts = allCharts;
        this.magInf = magic;
        this.forGenes = forGenes;
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
                if (genes.get(animal.getStringGenes()) != null){
                    genes.put(animal.getStringGenes(),genes.get(animal.getStringGenes())+1);
                }
                else{
                    genes.put(animal.getStringGenes(), 1);
                }
                i+=1;
            }
        }
        dominantGenotype = countDominantGenotype();
        Platform.runLater(() -> {
            forGenes.setText(dominantGenotype);
        });

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


    private void writeToCsv(int day,ArrayList<String[]> toFile){
        CvsFile cvs = null;
        if (map instanceof RectangularMap)
        {
            cvs = new CvsFile("RectangularMap_"+String.valueOf(day));
        }
        if (map instanceof BendedMap)
        {
            cvs = new CvsFile("BendedMap_"+String.valueOf(day));
        }
        cvs.dataToFile(toFile);
    }

    public void setCsv(){
        toCsv = true;
    }
    private String countDominantGenotype(){
        int maxGenes = 0;
        String bestGenes = null;
        for (String key: genes.keySet()){
            if(maxGenes < genes.get(key)){
                maxGenes = genes.get(key);
                bestGenes = key;
            }
        }
        return bestGenes;
    }
    public String getDominantGenotype(){
        return dominantGenotype;
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
                        if(genes.get(a.getStringGenes())-1 == 0){
                            genes.remove(a.getStringGenes());
                        }
                        else{
                            genes.put(a.getStringGenes(),genes.get(a.getStringGenes())-1);
                        }
                        numOfDeadAnimals += 1;
                        lifeLengthOfDeadAnimals += a.lifeLength;
                    }
                }
                for (Animal a: animalsToRemove) {
                    animals.remove(a);
                    map.removeElement(a,a.getPosition());
                }
                dominantGenotype = countDominantGenotype();
                Platform.runLater(() -> {
                    forGenes.setText(dominantGenotype);
                });
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
                            if (genes.get(child.getStringGenes()) != null){
                                genes.put(child.getStringGenes(),genes.get(child.getStringGenes())+1);
                            }
                            else{
                                genes.put(child.getStringGenes(), 1);
                            }
                        }
                    }
                }
                for (Animal animal: animalsToAdd) {
                    animals.add(animal);
                    map.place(animal);
                }
                dominantGenotype = countDominantGenotype();
                Platform.runLater(() -> {
                    forGenes.setText(dominantGenotype);
                });
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
                float res3 = 0;
                float res5 = 0;
                int finalAllChildren = allChildren;
                if (numOfAnimals != 0){
                    res3 = (float) finalAllEnergy /numOfAnimals;
                    res5 =(float) finalAllChildren /numOfAnimals;
                }
                float finalRes = res3;
                Platform.runLater(() -> {
                    allCharts.getCharts().get(2).addData(finalDays2.toString(), finalRes);
                });
                int finalNumOfDeadAnimals = numOfDeadAnimals;
                int finalLifeLengthOfDeadAnimals = lifeLengthOfDeadAnimals;
                Integer finalDays3 = days;
                float res4;
                if (finalNumOfDeadAnimals != 0){
                    res4 = (float) finalLifeLengthOfDeadAnimals / finalNumOfDeadAnimals;
                }
                else{
                    res4 = (float) 0.0;
                }
                Platform.runLater(() -> {
                    allCharts.getCharts().get(3).addData(finalDays3.toString(), res4);
                });
                Integer finalDays5 = days;
                float finalRes1 = res5;
                Platform.runLater(() -> {
                    allCharts.getCharts().get(4).addData(finalDays5.toString(), finalRes1);
                });
                toFile.add(new String[]{String.valueOf(numOfAnimals),String.valueOf(map.getNumOfGrass()),String.valueOf(res3),
                        String.valueOf(res4),String.valueOf(res5)});
                if(toCsv == true){
                    writeToCsv(days,toFile);
                }
                toCsv = false;
            }
            for(IEngineMoveObserver obs:observers){
                obs.mapChanged(map);
            }
        }
    }
}
