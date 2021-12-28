package agh.ics.oop.gui;

import agh.ics.oop.*;
import agh.ics.oop.interfaces.IEngineMoveObserver;
import agh.ics.oop.maps.AbstractWorldMap;
import agh.ics.oop.maps.BendedMap;
import agh.ics.oop.maps.RectangularMap;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.util.concurrent.CountDownLatch;


public class App extends Application implements IEngineMoveObserver {
    GridPane grid1 = new GridPane();
    GridPane grid2 = new GridPane();
    AbstractWorldMap map1;
    AbstractWorldMap map2;
    SimulationEngine engine1;
    SimulationEngine engine2;
    WelcomeScreen welcomeScreen = new WelcomeScreen(this);
    VBox mainScreen = new VBox();

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Pieski");
        mainScreen.setAlignment(Pos.CENTER);
        mainScreen.getChildren().add(welcomeScreen.getWelcomeScreen());
        Scene scene = new Scene(mainScreen, 1000, 800);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    @Override
    public void mapChanged(AbstractWorldMap map) {
        final CountDownLatch latchToWaitForJavaFx = new CountDownLatch(1);

        Platform.runLater(() -> {
            if (map.equals(map1)){
                grid1.setGridLinesVisible(false);
                grid1.getChildren().clear();
                map1.drawGridPane(grid1,false,"nth");
            }
            else{
                grid2.setGridLinesVisible(false);
                grid2.getChildren().clear();
                map2.drawGridPane(grid2,false,"nth");
            }
            latchToWaitForJavaFx.countDown();
        });
        try {
            latchToWaitForJavaFx.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void startSimulation(){
        //mapy
        map1 = new BendedMap(welcomeScreen.giveWidth(), welcomeScreen.giveHeight(), welcomeScreen.giveJungleRatio(), welcomeScreen.giveStartEnergy(),
                welcomeScreen.giveMoveEnergy(),welcomeScreen.givePlantEnergy());
        map2 = new RectangularMap(welcomeScreen.giveWidth(), welcomeScreen.giveHeight(), welcomeScreen.giveJungleRatio(), welcomeScreen.giveStartEnergy(),
                welcomeScreen.giveMoveEnergy(),welcomeScreen.givePlantEnergy());
        //wykresy
        AllCharts allCharts1 = new AllCharts(map1);
        AllCharts allCharts2 = new AllCharts(map2);
        allCharts1.addChart("Days","Nr of animals");
        allCharts1.addChart("Days","Nr of grass");
        allCharts1.addChart("Days","Avg energy");
        allCharts1.addChart("Days","Avg length of life");
        allCharts1.addChart("Days","Avg nr of children");
        allCharts2.addChart("Days","Nr of animals");
        allCharts2.addChart("Days","Nr of grass");
        allCharts2.addChart("Days","Avg energy");
        allCharts2.addChart("Days","Avg length of life");
        allCharts2.addChart("Days","Avg number of children");
        //magia
        MagicInformation magic1 = new MagicInformation();
        MagicInformation magic2 = new MagicInformation();
        //pola na dominujacy genotyp
        Label domGen1 = new Label();
        Label domGen2 = new Label();
        //silnik i watek
        engine1 = new SimulationEngine(map1, welcomeScreen.giveStartAnimals(),10,welcomeScreen.giveIsMagic(),allCharts1,magic1,domGen1);
        engine1.addObserver(this);
        Thread engineThread1 = new Thread(engine1);
        engine2 = new SimulationEngine(map2, welcomeScreen.giveStartAnimals(),10,welcomeScreen.giveIsMagic(),allCharts2,magic2,domGen2);
        engine2.addObserver(this);
        Thread engineThread2 = new Thread(engine2);
        //guziki
        Button buttonCsv1 = new Button("Don't touch");
        Button buttonCsv2 = new Button("Don't touch");
        Button buttonStart1 = new Button("Start");
        Button buttonPause1 = new Button("Pause");
        Button buttonStart2 = new Button("Start");
        Button buttonPause2 = new Button("Pause");
        Button light1 = new Button("Don't touch");
        Button light2 = new Button("Don't touch");
        light1.setOnAction(e -> {
            map1.drawGridPane(grid1,true,engine1.getDominantGenotype());
        });
        light2.setOnAction(e -> {
            map2.drawGridPane(grid2,true,engine2.getDominantGenotype());
        });
        buttonCsv1.setOnAction(e -> {
            engine1.setCsv();
            engine1.continueThread();
            buttonPause1.setText("Pause");
        });
        buttonCsv2.setOnAction(e -> {
            engine2.setCsv();
            engine2.continueThread();
            buttonPause2.setText("Pause");
        });
        buttonStart1.setOnAction(e -> {
            engineThread1.start();
        });
        buttonPause1.setOnAction(e -> {
            if(!engine1.getIsPaused()){
                engine1.pauseThread();
                buttonPause1.setText("Continue");
                buttonCsv1.setText("Write to csv");
                light1.setText("See animals with dominant genotype");
            }
            else{
                engine1.continueThread();
                buttonPause1.setText("Pause");
                buttonCsv1.setText("Don't touch");
                light1.setText("Don't touch");
            }

        });
        buttonStart2.setOnAction(e -> {
            engineThread2.start();
        });
        buttonPause2.setOnAction(e -> {
            if(!engine2.getIsPaused()){
                engine2.pauseThread();
                buttonPause2.setText("Continue");
                buttonCsv2.setText("Write to csv");
                light2.setText("See animals with dominant genotype");
            }
            else{
                engine2.continueThread();
                buttonPause2.setText("Pause");
                buttonCsv2.setText("Don't touch");
                light2.setText("Don't touch");
            }

        });
        //ustalam ulozenie
        HBox charts1Part1 = new HBox(allCharts1.getLineCharts().get(0),allCharts1.getLineCharts().get(1),
                allCharts1.getLineCharts().get(2));
        HBox charts1Part2 = new HBox(allCharts1.getLineCharts().get(3),allCharts1.getLineCharts().get(4));
        HBox charts2Part1 = new HBox(allCharts2.getLineCharts().get(0),allCharts2.getLineCharts().get(1),
                allCharts2.getLineCharts().get(2));
        HBox charts2Part2 = new HBox(allCharts2.getLineCharts().get(3),allCharts2.getLineCharts().get(4));
        charts1Part1.setAlignment(Pos.CENTER);
        charts1Part2.setAlignment(Pos.CENTER);
        charts2Part1.setAlignment(Pos.CENTER);
        charts2Part1.setAlignment(Pos.CENTER);
        VBox rMap = new VBox(new Label("Bended Map"),new HBox(buttonStart1,buttonPause1,buttonCsv1,light1,magic1.getLabel()),grid1,charts1Part1,
                charts1Part2,new HBox(new Label("Dominant genotype"),domGen1));
        VBox bMap = new VBox(new Label("Rectangular Map"),new HBox(buttonStart2,buttonPause2,buttonCsv2,light2,magic2.getLabel()),grid2,charts2Part1,
                charts2Part2,new HBox(new Label("Dominant genotype"),domGen2));
        HBox mapScreen = new HBox(rMap,bMap);
        map1.drawGridPane(grid1,false,"nth");
        map2.drawGridPane(grid2,false,"nth");
        mainScreen.getChildren().clear();
        mainScreen.getChildren().add(mapScreen);
    }
}
