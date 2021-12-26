package agh.ics.oop;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Animal extends AbstractWorldMapElement{
    private final Animal parent1;
    private final Animal parent2;
    private MapDirection orientation;
    private final IWorldMap map;
    float energy;
    int lifeLength;
    int numOfChildren;
    Integer[] genes = new Integer[32];

    public Animal(IWorldMap map,Vector2d initialPosition,String type,Animal parent1,Animal parent2,Animal toCopy){
        this.position = initialPosition;
        this.map = map;
        this.parent1 = parent1;
        this.parent2 = parent2;
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
            this.energy = (float) (1/4)* parent1.energy + (1/4)* parent2.energy;
        }
        //jesli zwierze jest kopia
        if (type.equals("copy")){
            for(int i = 0;i<32;i++){
                this.genes[i] = toCopy.genes[i];
            }
            this.energy = this.map.getStartEnergy();
        }

    }
    public MapDirection getOrientation(){
        return this.orientation;
    }

    public Integer[] getGenes(){
        return this.genes;
    }

    public int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }

    private void makeGenes(){
        for (int i=0;i<32;i++){
            this.genes[i] = getRandomNumber(0,8);
        }
    }
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
        //rodzice musza stracic energie na dziecko
        parent1.energy = (3/4)*parent1.energy;
        parent2.energy = (3/4)*parent2.energy;
    }
    public float getEnergy(){
        return this.energy;
    }

    public void addEnergy(int value){
        this.energy += value;
    }
    public String toString(){
        switch(this.orientation){
            case N:
                return "N";
            case S:
                return "S";
            case NE:
                return "NE";
            case SE:
                return "SE";
            case NW:
                return "NW";
            case SW:
                return "SW";
            case E:
                return "E";
            case W:
                return "W";
        }
        return null;

    }
    public String result(){
        return this.position +", "+this.orientation;
    }
    boolean isAt(Vector2d position){
        return this.equals(position);
    }
    public void move(MoveDirection direction){
        switch(direction){
            case FR:
                this.orientation = this.orientation.next();
                break;
            case R:
                this.orientation = this.orientation.next();
                this.orientation = this.orientation.next();
                break;
            case BR:
                this.orientation = this.orientation.next();
                this.orientation = this.orientation.next();
                this.orientation = this.orientation.next();
                break;
            case FL:
                this.orientation = this.orientation.previous();
                break;
            case L:
                this.orientation = this.orientation.previous();
                this.orientation = this.orientation.previous();
                break;
            case BL:
                this.orientation = this.orientation.previous();
                this.orientation = this.orientation.previous();
                this.orientation = this.orientation.previous();
                break;
            case F:
                Vector2d go1 = this.orientation.toUnitVector();
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
                break;
            case B:
                Vector2d go2 = this.orientation.toUnitVector();
                go2 = go2.opposite();
                if (this.map instanceof RectangularMap) {
                    if (map.canMoveTo(this.position.add(go2))) {
                        Vector2d oldPosition = this.position;
                        this.positionChanged(oldPosition);
                        this.position = this.position.add(go2);

                    }
                }
                if (this.map instanceof BendedMap){
                    Vector2d oldPosition = this.position;
                    Vector2d newPosition = new Vector2d(oldPosition.x, oldPosition.y);

                    newPosition = newPosition.add(go2);
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
                break;
            default:
                break;
        }
    }
    public String getUrl(){
        switch(this.orientation){
            case N:
                return "src/main/resources/1.jpg";
            case NE:
                return "src/main/resources/2.jpg";
            case E:
                return "src/main/resources/3.jpg";
            case SE:
                return "src/main/resources/4.jpg";
            case S:
                return "src/main/resources/5.jpg";
            case SW:
                return "src/main/resources/6.jpg";
            case W:
                return "src/main/resources/7.jpg";
            case NW:
                return "src/main/resources/8.jpg";
        }
        return null;
    }

    public String getEnergyUrl(){
        int maxEnergy =  this.map.getStartEnergy();
        if (this.energy <= 0.2*maxEnergy){
            return "src/main/resources/energy1.jpg";
        }
        else if (this.energy <= 0.4*maxEnergy){
            return "src/main/resources/energy2.jpg";
        }
        else if (this.energy <= 0.6*maxEnergy){
            return "src/main/resources/energy3.jpg";
        }
        else if (this.energy <= 0.8*maxEnergy){
            return "src/main/resources/energy4.jpg";
        }
        else{
            return "src/main/resources/energy5.jpg";
        }
    }

    public MoveDirection getMove(){
        int number = getRandomNumber(0,31);
        MoveDirection move = new OptionsParser().parse(this.genes[number]);
        return move;
    }
}
