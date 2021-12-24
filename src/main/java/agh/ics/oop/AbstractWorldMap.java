package agh.ics.oop;

import agh.ics.oop.gui.GuiElementBox;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public abstract class AbstractWorldMap implements IWorldMap, IPositionChangeObserver {
    private final MapVisualizer visualizer = new MapVisualizer(this);
    HashMap<Vector2d, ArrayList<IMapElement>> mapElements = new HashMap<>();
    protected int width;
    protected int height;
    protected double jungleRatio;
    protected int moveEnergy;
    protected int startEnergy;
    protected int plantEnergy;

    public int getWidth(){
        return this.width;
    }
    public int getHeight(){
        return this.height;
    }
    public int getStartEnergy(){
        return this.startEnergy;
    }
    public int getMoveEnergy(){
        return this.moveEnergy;
    }
    public int getPlantEnergy(){
        return this.plantEnergy;
    }
    public HashMap<Vector2d, ArrayList<IMapElement>> getmapElements(){
        return mapElements;
    }
    public Vector2d jungleLowerLeft(){
        int area = (width+1)*(height+1);
        int jungleArea = (int) ((jungleRatio*area)/(1+jungleRatio));
        int jungleWidth = (int) Math.sqrt((jungleArea*(width+1))/(height+1));
        int jungleHeight = jungleArea/jungleWidth;
        return new Vector2d((int) (width+1)/2-jungleWidth/2,(int) (height+1)/2-jungleHeight/2);
    }
    public Vector2d jungleUpperRight(){
        Vector2d tmp = this.jungleLowerLeft();
        int area = (width+1)*(height+1);
        int jungleArea = (int) ((jungleRatio*area)/(1+jungleRatio));
        int jungleWidth = (int) Math.sqrt((jungleArea*width)/height);
        int jungleHeight = jungleArea/jungleWidth;
        return new Vector2d((int) tmp.x+jungleWidth-1,(int) tmp.y+jungleHeight-1);
    }
    private int howManyObInArea(Vector2d start,Vector2d end){
        int res = 0;
        for(int i = start.x;i<=end.x;i++){
            for(int j = start.y;j<=end.y;j++){
                if (mapElements.get(new Vector2d(i,j)) != null){
                    res += 1;
                }
            }
        }
        return res;
    }
    public int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }
    public void addGrass(int n){
        int area = (width+1)*(height+1);
        int jungleArea = (int) ((jungleRatio*area)/(1+jungleRatio));
        Vector2d lowerLeft = this.jungleLowerLeft();
        Vector2d upperRight = this.jungleUpperRight();
        /*System.out.println(mapElements.size());
        System.out.println(howManyObInArea(lowerLeft,upperRight));
        System.out.println(area-jungleArea);*/
        if(mapElements.size()-howManyObInArea(lowerLeft,upperRight) < area-jungleArea )
        {
            Random rand = new Random();
            rand.setSeed(42);
            int i = 0;
            while (i < n) {
                int x = rand.nextInt((int) (width + 1));
                int y = rand.nextInt((int) (height + 1));
                if (!(this.isOccupied(new Vector2d(x, y))) && !(x>= lowerLeft.x && x<=upperRight.x && y>= lowerLeft.y && y<=upperRight.y)) {
                    Grass tmp = new Grass(new Vector2d(x, y));
                    ArrayList<IMapElement> el = mapElements.get(new Vector2d(x, y));
                    if (el == null){
                        el = new ArrayList<IMapElement>();
                        mapElements.put(new Vector2d(x, y),el);
                        el.add(tmp);
                    }
                    i += 1;
                }
            }
        }

    }
    public void addJungleGrass(int n){
        int area = (width+1)*(height+1);
        int jungleArea = (int) ((jungleRatio*area)/(1+jungleRatio));
        Vector2d lowerLeft = this.jungleLowerLeft();
        Vector2d upperRight = this.jungleUpperRight();
        if(howManyObInArea(lowerLeft,upperRight) < jungleArea)
        {
            Random rand = new Random();
            rand.setSeed(42);
            int i = 0;
            while (i < n) {
                int x = getRandomNumber(lowerLeft.x, upperRight.x+1);
                int y = getRandomNumber(lowerLeft.y, upperRight.y+1);
                if (!(this.isOccupied(new Vector2d(x, y)))) {
                    Grass tmp = new Grass(new Vector2d(x, y));
                    ArrayList<IMapElement> el = mapElements.get(new Vector2d(x, y));
                    if (el == null){
                        el = new ArrayList<IMapElement>();
                        mapElements.put(new Vector2d(x, y),el);
                        el.add(tmp);
                    }
                    i += 1;
                }
            }
        }

    }
    public boolean place(Animal animal) {
        ArrayList<IMapElement> el = mapElements.get(animal.getPosition());
        if (el == null){
            el = new ArrayList<IMapElement>();
            mapElements.put(animal.getPosition(),el);
        }
        el.add(animal);
        animal.addObserver(this);
        return true;
    }


    @Override
    public boolean isOccupied(Vector2d position) {
        if(mapElements.containsKey(position))
        {
            return true;
        }
        return false;
    }
    @Override
    public Object objectAt(Vector2d position) {
        return mapElements.get(position);
    }
    @Override
    public void removeElement(IMapElement element, Vector2d position){
        ArrayList<IMapElement> el = mapElements.get(element.getPosition());
        el.remove(element);
        if (el.size() == 0){
            mapElements.remove(position);
        }
    }
    public void addElement(IMapElement element, Vector2d position){
        ArrayList<IMapElement> el = mapElements.get(position);
        if (el == null){
            el = new ArrayList<>();
            mapElements.put(position, el);
        }
        el.add(element);
    }

    public void positionChanged(Animal animal, Vector2d newPosition){
        removeElement(animal,animal.getPosition());
        addElement(animal,newPosition);
    }
    public String toString(){
        return visualizer.draw(new Vector2d(0,0), new Vector2d(width,height));
    }

    //zwraca tablice najsilniejszych zwierzat na pozycji
    public ArrayList<Animal> getBest(Vector2d position){
        ArrayList<Animal> strongest = new ArrayList<>();
        ArrayList<IMapElement> el= mapElements.get(position);
        float maxEnergy = 0;
        for (IMapElement ob:el){
            if (ob instanceof Animal && ob.getEnergy() > maxEnergy){
                maxEnergy = ob.getEnergy();
            }
        }
        for (IMapElement ob:el){
            if (ob instanceof Animal && ob.getEnergy() == maxEnergy){
                strongest.add((Animal) ob);
            }
        }
        return strongest;
    }

    public Animal[] getParents(Vector2d position){
        ArrayList<IMapElement> el= mapElements.get(position);
        Animal[] parents = new Animal[2];
        int sem = 0;
        for (IMapElement ob:el){
            if (ob instanceof Animal){
                parents[sem] = (Animal) ob;
                sem += 1;
            }
            if (sem == 2){
                break;
            }
        }
        if (sem < 2){
            return null;
        }
        return parents;
    }



    //zwraca trawę na danej pozycji (lub null, jeśli się nie da)
    public IMapElement isGrass(Vector2d position){
        ArrayList<IMapElement> el= mapElements.get(position);
        for (IMapElement ob:el){
            if (ob instanceof Grass){
                return ob;
            }
        }
        return null;
    }

    public void drawGridPane(GridPane grid) {
        Vector2d lowerLeft = new Vector2d(0,0);
        Vector2d upperRight = new Vector2d(width,height);
        int width = upperRight.x - lowerLeft.x;
        int height = upperRight.y - lowerLeft.y;
        grid.setGridLinesVisible(true);
        Label start = new Label("y/x");
        grid.add(start, 0, 0, 1, 1);
        GridPane.setHalignment(start, HPos.CENTER);
        for (int i = 1;i<width+2;i++)
        {
            Integer a = i-1;
            Label tmp = new Label(a.toString());
            grid.add(tmp, i, 0, 1, 1);
            GridPane.setHalignment(tmp, HPos.CENTER);
        }
        for (int i = height; i >=0; i--){
            Integer a = height-i;
            Label tmp = new Label(a.toString());
            grid.add(tmp, 0, i+1, 1, 1);
            GridPane.setHalignment(tmp, HPos.CENTER);
        }
        for (ArrayList<IMapElement> el: mapElements.values()){
            for (IMapElement ob: el){
                VBox b;
                GuiElementBox g;
                if (ob instanceof Grass)
                {
                    g = new GuiElementBox(ob,1);
                    b = g.getVBox();
                    grid.add(b,ob.getPosition().x+1, height-ob.getPosition().y+1, 1, 1);
                    b.setAlignment(Pos.CENTER);
                }
                else{
                    g = new GuiElementBox( ob,1);
                    GuiElementBox g2 = new GuiElementBox(ob, 2);
                    VBox b1 = g.getVBox();
                    VBox b2 = g2.getVBox();
                    VBox a = new VBox(b1,b2);
                    a.setAlignment(Pos.CENTER);
                    grid.add(a,ob.getPosition().x+1, height-ob.getPosition().y+1, 1, 1);
                    a.setAlignment(Pos.CENTER);
                }

            }
        }

        grid.getRowConstraints().clear();
        grid.getColumnConstraints().clear();
        for (int i = 0; i < height+2; i++) {
            grid.getRowConstraints().add(new RowConstraints(50));

        }
        for (int i = 0; i < width+2; i++) {
            grid.getColumnConstraints().add(new ColumnConstraints(50));
        }

    }




}
