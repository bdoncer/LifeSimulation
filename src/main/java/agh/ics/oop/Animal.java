package agh.ics.oop;

public class Animal extends AbstractWorldMapElement{
    private MapDirection orientation = MapDirection.N;
    private IWorldMap map;
    int energy;
    Integer[] genes = new Integer[32];

    public Animal(IWorldMap map,Vector2d initialPosition){
        this.position = initialPosition;
        this.map = map;
        this.energy = this.map.getStartEnergy();
        this.makeGenes();
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




}
