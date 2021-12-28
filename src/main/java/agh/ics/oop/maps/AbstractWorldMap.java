package agh.ics.oop.maps;

import agh.ics.oop.objects.Animal;
import agh.ics.oop.objects.Grass;
import agh.ics.oop.data.Vector2d;
import agh.ics.oop.gui.AllImages;
import agh.ics.oop.interfaces.IMapElement;
import agh.ics.oop.interfaces.IPositionChangeObserver;
import agh.ics.oop.interfaces.IWorldMap;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.Random;


public abstract class AbstractWorldMap implements IWorldMap, IPositionChangeObserver {
    HashMap<Vector2d, ArrayList<IMapElement>> mapElements = new HashMap<>();
    AllImages allImages;
    {
        try {
            allImages = new AllImages();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    protected int width;
    protected int height;
    protected double jungleRatio;
    protected int moveEnergy;
    protected int startEnergy;
    protected int plantEnergy;
    private int numOfGrass=0;

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
        Vector2d lowerLeft = this.jungleLowerLeft();
        Vector2d upperRight = this.jungleUpperRight();
        int jungleRealArea = (upperRight.x-lowerLeft.x+1)*(upperRight.y-lowerLeft.y+1);
        if(mapElements.size()-howManyObInArea(lowerLeft,upperRight) < area-jungleRealArea )
        {
            numOfGrass += 1;
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
        Vector2d lowerLeft = this.jungleLowerLeft();
        Vector2d upperRight = this.jungleUpperRight();
        int jungleRealArea = (upperRight.x-lowerLeft.x+1)*(upperRight.y-lowerLeft.y+1);
        if(howManyObInArea(lowerLeft,upperRight) < jungleRealArea)
        {
            numOfGrass += 1;
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

    public void drawGridPane(GridPane grid,boolean areLights,String genotype) {
        Vector2d lowerLeft = new Vector2d(0,0);
        Vector2d upperRight = new Vector2d(width,height);
        int width = upperRight.x - lowerLeft.x;
        int height = upperRight.y - lowerLeft.y;
        int dimension = Math.max(width,height);
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
                if (ob instanceof Grass)
                {
                    Image image = allImages.grass;
                    ImageView pic = new ImageView();
                    pic.setFitWidth(400/(dimension));
                    pic.setFitHeight(400/(dimension));
                    pic.setImage(image);
                    b = new VBox(pic);
                    b.setAlignment(Pos.CENTER);
                    grid.add(b,ob.getPosition().x+1, height-ob.getPosition().y+1, 1, 1);
                    b.setAlignment(Pos.CENTER);
                }
                else{
                    Image image = allImages.dog[ob.getIndex()];
                    ImageView pic = new ImageView();
                    pic.setFitWidth(400/(3*(dimension)));
                    pic.setFitHeight(400/(3*(dimension)));
                    pic.setImage(image);
                    Image image2 = allImages.energy[ob.getEnergyIndex()];
                    ImageView pic2 = new ImageView();
                    pic2.setFitWidth(400/(dimension));
                    pic2.setFitHeight(400/(4*(dimension)));
                    pic2.setImage(image2);
                    b = new VBox(pic,pic2);
                    if(areLights && Objects.equals(ob.getStringGenes(), genotype)){
                        b.setBackground(new Background(new BackgroundFill(Color.YELLOW, CornerRadii.EMPTY, Insets.EMPTY)));
                    }
                    b.setAlignment(Pos.CENTER);
                    grid.add(b,ob.getPosition().x+1, height-ob.getPosition().y+1, 1, 1);
                    b.setAlignment(Pos.CENTER);
                }

            }
        }
        grid.getRowConstraints().clear();
        grid.getColumnConstraints().clear();
        for (int i = 0; i < height+2; i++) {
            grid.getRowConstraints().add(new RowConstraints(400/dimension));

        }
        for (int i = 0; i < width+2; i++) {
            grid.getColumnConstraints().add(new ColumnConstraints(400/dimension));
        }
    }
    public int getNumOfGrass(){
        return numOfGrass;
    }



}
