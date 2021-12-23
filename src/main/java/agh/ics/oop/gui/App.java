package agh.ics.oop.gui;

import agh.ics.oop.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;


public class App extends Application implements IEngineMoveObserver {
    GridPane grid = new GridPane();
    AbstractWorldMap map;
    SimulationEngine engine;
    WelcomeScreen welcomeScreen = new WelcomeScreen(this);
    VBox mainScreen = new VBox();
    public void init(){
        String[] args = getParameters().getRaw().toArray(new String[0]);
        try {
            //MoveDirection[] directions = new OptionsParser().parse(args);


            //engineThread.start();
            //System.out.println(map.toString());
        }
        catch(IllegalArgumentException ex){
            System.out.println(ex.getMessage());
            System.exit(1);
        }
    }
    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Pieski");
        mainScreen.setAlignment(Pos.CENTER);
        mainScreen.getChildren().add(welcomeScreen.getWelcomeScreen());
        Scene scene = new Scene(mainScreen, 800, 800);
        //Scene scene = new Scene(grid, 800, 800);
        primaryStage.setScene(scene);
        primaryStage.show();
        //primaryStage.show();
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


        Vector2d[] positions = {new Vector2d(1,1),new Vector2d(1,2)};
        engine = new SimulationEngine(map, welcomeScreen.giveStartAnimals(),500);
        engine.addObserver(this);

        TextField text = new TextField();
        Button button = new Button("Start");
        button.setOnAction(e -> {
            MoveDirection[] directions = new OptionsParser().parse(
                    text.getText().split(" ")
            );
            engine.movesSetter(directions);
            Thread engineThread = new Thread(engine);
            engineThread.start();
        });
        /*TextField text2 = new TextField();
        Label l2 = new Label("<-- Set jungle/savanna ratio");*/
        VBox mapScreen = new VBox(new HBox(text, button), grid);
        map.drawGridPane(grid);
        mainScreen.getChildren().clear();
        mainScreen.getChildren().add(mapScreen);

    }
}
