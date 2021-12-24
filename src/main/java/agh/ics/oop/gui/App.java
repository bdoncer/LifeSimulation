package agh.ics.oop.gui;

import agh.ics.oop.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.awt.event.MouseEvent;


public class App extends Application implements IEngineMoveObserver {
    GridPane grid = new GridPane();
    AbstractWorldMap map;
    SimulationEngine engine;
    WelcomeScreen welcomeScreen = new WelcomeScreen(this);
    VBox mainScreen = new VBox();
    /*public void init(){
        String[] args = getParameters().getRaw().toArray(new String[0]);
        try {

        }
        catch(IllegalArgumentException ex){
            System.out.println(ex.getMessage());
            System.exit(1);
        }
    }*/
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
    public void mapChanged() {
        Platform.runLater(() -> { grid.setGridLinesVisible(false);
            grid.getChildren().clear();
            map.drawGridPane(grid); });
    }

    public void startSimulation(){
        map = new BendedMap(welcomeScreen.giveWidth(), welcomeScreen.giveHeight(), welcomeScreen.giveJungleRatio(), welcomeScreen.giveStartEnergy(),
                welcomeScreen.giveMoveEnergy(),welcomeScreen.givePlantEnergy());

        engine = new SimulationEngine(map, welcomeScreen.giveStartAnimals(),500,welcomeScreen.giveIsMagic());
        engine.addObserver(this);
        Thread engineThread = new Thread(engine);
        Button buttonStart = new Button("Start");
        buttonStart.setOnAction(e -> {
            engineThread.start();
        });
        Button buttonPause = new Button("Pause");
        buttonPause.setOnAction(e -> {
            if(!engine.getIsPaused()){
                engine.pauseThread();
                buttonPause.setText("Continue");
            }
            else{
                engine.continueThread();
                buttonPause.setText("Pause");
            }

        });

        VBox mapScreen = new VBox(new HBox(buttonStart,buttonPause), grid);
        map.drawGridPane(grid);
        mainScreen.getChildren().clear();
        mainScreen.getChildren().add(mapScreen);

    }


}
