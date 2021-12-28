package agh.ics.oop.objects;

import agh.ics.oop.data.MapDirection;
import agh.ics.oop.data.MoveDirection;
import agh.ics.oop.data.OptionsParser;
import agh.ics.oop.data.Vector2d;
import agh.ics.oop.maps.AbstractWorldMap;
import agh.ics.oop.maps.BendedMap;
import agh.ics.oop.maps.RectangularMap;

import java.util.Arrays;

public class Animal extends AbstractWorldMapElement {
    private final Animal parent1;
    private final Animal parent2;
    public MapDirection orientation;
    private final AbstractWorldMap map;
    public float energy;
    public int lifeLength;
    public int numOfChildren;
    public Integer[] genes = new Integer[32];

    public Animal(AbstractWorldMap map, Vector2d initialPosition, String type, Animal parent1, Animal parent2, Animal toCopy){
        this.position = initialPosition;
        this.map = map;
        this.parent1 = parent1;
        this.parent2 = parent2;
        //losuje kierunek startowy
        MapDirection[] options = {MapDirection.N,MapDirection.NE,MapDirection.E,MapDirection.SE,
                MapDirection.S,MapDirection.SW,MapDirection.W,MapDirection.NW};
        int option = getRandomNumber(0,8);
        this.orientation = options[option];
        this.lifeLength = 0;
        this.numOfChildren = 0;
        //jesli zwierze jest startowe
        if (type.equals("s"))
        {
            this.makeGenes();
            this.energy = this.map.getStartEnergy();
        }
        //jesli zwierze urodzilo sie w czasie symulacji
        if (type.equals("c")){
            this.makeChildrenGenes();
            this.energy = (float) 0.25* parent1.energy + (float) 0.25* parent2.energy;
        }
        //jesli zwierze jest kopia
        if (type.equals("copy")){
            for(int i = 0;i<32;i++){
                this.genes[i] = toCopy.genes[i];
            }
            this.energy = this.map.getStartEnergy();
        }

    }

    public String getStringGenes(){
        return Arrays.toString(this.genes);
    }

    public int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }

    public float getEnergy(){
        return this.energy;
    }

    public void addEnergy(int value){
        this.energy += value;
    }

    //tworzy geny dla zwierzat startowych
    private void makeGenes(){
        for (int i=0;i<32;i++){
            this.genes[i] = getRandomNumber(0,8);
        }
        Arrays.sort(this.genes);
    }

    //tworzy geny dla dzieci
    private void makeChildrenGenes() {
        float allEnergy = parent1.energy + parent2.energy;
        float p1Per = parent1.energy / allEnergy;
        float p2Per = 1 - p1Per;
        int p1Space = 0;
        float i = 0;
        while (i<p1Per){
            p1Space += 1;
            i += (float) 1/32;
        }
        int p2Space = 32 - p1Space;
        int half = getRandomNumber(0,2);
        //najpierw bierzemy od rodzica 1 a potem od rodzica 2
        if((p1Per > p2Per && half == 0) || (p1Per < p2Per && half == 1)){
            for(int j = 0; j<p1Space;j++){
                this.genes[j] = parent1.genes[j];
            }
            for(int j = p1Space; j<32;j++){
                this.genes[j] = parent2.genes[j];
            }
        }
        //najpierw bierzemy od rodzica 2 a potem od rodzica 1
        else{
            for(int j = 0; j<p2Space;j++){
                this.genes[j] = parent2.genes[j];
            }
            for(int j = p2Space; j<32;j++){
                this.genes[j] = parent1.genes[j];
            }
        }
        Arrays.sort(this.genes);
        //rodzice musza stracic energie na dziecko
        parent1.energy = (float) 0.75*parent1.energy;
        parent2.energy = (float) 0.75*parent2.energy;
    }
    //wykonuje ruch zwierzecia w zaleznosci od jego orientacji
    public void move(MoveDirection direction){
        switch(direction) {
            case FR:
                this.orientation = this.orientation.next();
                break;
            case R:
                this.orientation = this.orientation.next().next();
                break;
            case BR:
                this.orientation = this.orientation.next().next().next();
                break;
            case FL:
                this.orientation = this.orientation.previous();
                break;
            case L:
                this.orientation = this.orientation.previous().previous();
                break;
            case BL:
                this.orientation = this.orientation.previous().previous().previous();
                break;
            case F:
                this.realMove(0);
                break;
            case B:
                this.realMove(1);
                break;
        }
    }
    //zmienia polozenie przy ruchu do przodu lub do tylu
    private void realMove(int type){
        Vector2d go1 = this.orientation.toUnitVector();
        if (type == 1) {
            go1 = go1.opposite();
        }
        if (this.map instanceof RectangularMap) {
            if (map.canMoveTo(this.position.add(go1))) {
                this.positionChanged(this.position.add(go1));
                this.position = this.position.add(go1);
            }
        }
        if (this.map instanceof BendedMap){
            Vector2d oldPosition = this.position;
            Vector2d newPosition = new Vector2d(oldPosition.x, oldPosition.y);
            newPosition = this.position.add(go1);
            if(newPosition.x<0){
                newPosition.x = map.getWidth();
            }
            if(newPosition.x>map.getWidth()){
                newPosition.x = 0;
            }
            if(newPosition.y<0){
                newPosition.y = map.getHeight();
            }
            if(newPosition.y>map.getHeight()){
                newPosition.y = 0;
            }
            this.positionChanged(newPosition);
            this.position = newPosition;

        }

    }
    //zwraca indeks zdjecia zwierzecia w zaleznosci od orientacji
    public int getIndex(){
        switch(this.orientation){
            case N:
                return 0;
            case NE:
                return 1;
            case E:
                return 2;
            case SE:
                return 3;
            case S:
                return 4;
            case SW:
                return 5;
            case W:
                return 6;
            case NW:
                return 7;
        }
        return 0;
    }

    //zwraca indeks zdjecia energii w zaleznosci od jej poziomu
    public int getEnergyIndex(){
        int maxEnergy =  this.map.getStartEnergy();
        if (this.energy <= 0.2*maxEnergy){
            return 0;
        }
        else if (this.energy <= 0.4*maxEnergy){
            return 1;
        }
        else if (this.energy <= 0.6*maxEnergy){
            return 2;
        }
        else if (this.energy <= 0.8*maxEnergy){
            return 3;
        }
        else{
            return 4;
        }
    }
    //losuje ruch z genotypu
    public MoveDirection getMove(){
        int number = getRandomNumber(0,31);
        MoveDirection move = new OptionsParser().parse(this.genes[number]);
        return move;
    }
}
