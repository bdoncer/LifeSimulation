package agh.ics.oop;

import java.util.Arrays;

public class Animal extends AbstractWorldMapElement{
    private final Animal parent1;
    private final Animal parent2;
    private MapDirection orientation = MapDirection.N;
    private final IWorldMap map;
    int energy;
    Integer[] genes = new Integer[32];

    public Animal(IWorldMap map,Vector2d initialPosition,String type,Animal parent1,Animal parent2){
        this.position = initialPosition;
        this.map = map;
        this.energy = this.map.getStartEnergy();
        this.parent1 = parent1;
        this.parent2 = parent2;
        //jesli zwierze jest startowe
        if (type.equals("s"))
        {
            this.makeGenes();
        }
        //jesli zwierze urodzilo sie w czasie symulacji
        if (type.equals("c")){
            this.makeChildrenGenes();
        }
        //DO ZMIANY!!!!
        else{
            this.makeGenes();
        }

    }
    public MapDirection getOrientation(){
        return this.orientation;
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
        int allEnergy = parent1.energy + parent2.energy;
        float p1Per = parent1.energy / allEnergy;
        System.out.println((allEnergy));

        //System.out.println((parent1.energy/allEnergy));
        System.out.println((p1Per));
        float p2Per = 32 - p1Per;
        int half = getRandomNumber(0,1);
        //System.out.println(half);
        /*int p1Start;
        int p2Start;
        if(p1Per > p2Per){
            if(half == 0){
                p1Start = 0;
                p2Start = p1Per;
            }
            else{
                p2Start = 0;
                p1Start = p2Per;
            }
        }
        else{
            if(half == 0){
                p2Start = 0;
                p1Start = p2Per;
            }
            else{
                p1Start = 0;
                p2Start = p1Per;
            }
        }
        for(int i = p1Start; i<p1Start+p1Per;i++){
            this.genes[i] = parent1.genes[i];
        }
        for(int i = p2Start; i<p2Start+p2Per;i++){
            this.genes[i] = parent2.genes[i];
        }*/
        /*System.out.println("rodzic 1");
        System.out.println((parent1.energy));
        System.out.println((p1Per));
        //System.out.println((p1Start));
        System.out.println(Arrays.toString(parent1.genes));
        System.out.println("rodzic 2");
        System.out.println((parent2.energy));
        System.out.println((p2Per));
        //System.out.println((p2Start));
        System.out.println(Arrays.toString(parent2.genes));
        System.out.println("dziecko");
        System.out.println(Arrays.toString(this.genes));*/

    }
    public int getEnergy(){
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
}
