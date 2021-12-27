package agh.ics.oop.gui;

import agh.ics.oop.*;
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
        Scene scene = new Scene(mainScreen, 800, 800);
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
                map1.drawGridPane(grid1);
            }
            else{
                grid2.setGridLinesVisible(false);
                grid2.getChildren().clear();
                map2.drawGridPane(grid2);
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
        //silnik i watek
        engine1 = new SimulationEngine(map1, welcomeScreen.giveStartAnimals(),10,welcomeScreen.giveIsMagic(),allCharts1,magic1);
        engine1.addObserver(this);
        Thread engineThread1 = new Thread(engine1);
        engine2 = new SimulationEngine(map2, welcomeScreen.giveStartAnimals(),10,welcomeScreen.giveIsMagic(),allCharts2,magic2);
        engine2.addObserver(this);
        Thread engineThread2 = new Thread(engine2);
        //guziki
        Button buttonStart1 = new Button("Start");
        buttonStart1.setOnAction(e -> {
            engineThread1.start();
        });
        Button buttonPause1 = new Button("Pause");
        buttonPause1.setOnAction(e -> {
            if(!engine1.getIsPaused()){
                engine1.pauseThread();
                buttonPause1.setText("Continue");
            }
            else{
                engine1.continueThread();
                buttonPause1.setText("Pause");
            }

        });
        Button buttonStart2 = new Button("Start");
        buttonStart2.setOnAction(e -> {
            engineThread2.start();
        });
        Button buttonPause2 = new Button("Pause");
        buttonPause2.setOnAction(e -> {
            if(!engine2.getIsPaused()){
                engine2.pauseThread();
                buttonPause2.setText("Continue");
            }
            else{
                engine2.continueThread();
                buttonPause2.setText("Pause");
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
        VBox rMap = new VBox(new Label("Bended Map"),new HBox(buttonStart1,buttonPause1,magic1.getLabel()),grid1,charts1Part1,charts1Part2);
        VBox bMap = new VBox(new Label("Rectangular Map"),new HBox(buttonStart2,buttonPause2,magic2.getLabel()),grid2,charts2Part1,charts2Part2);
        HBox mapScreen = new HBox(rMap,bMap);
        map1.drawGridPane(grid1);
        map2.drawGridPane(grid2);
        mainScreen.getChildren().clear();
        mainScreen.getChildren().add(mapScreen);

    }
}
