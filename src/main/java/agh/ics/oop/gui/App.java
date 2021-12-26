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
import java.util.concurrent.ScheduledExecutorService;


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

/*    private ScheduledExecutorService scheduledExecutorService;
    int WINDOW_SIZE = 10;*/
    public void startSimulation(){
        map = new BendedMap(welcomeScreen.giveWidth(), welcomeScreen.giveHeight(), welcomeScreen.giveJungleRatio(), welcomeScreen.giveStartEnergy(),
                welcomeScreen.giveMoveEnergy(),welcomeScreen.givePlantEnergy());
        AllCharts allCharts = new AllCharts(map);
        //proba dodania wykresu
        allCharts.addChart("Days","Number of animals","hyhy");
        allCharts.addChart("Days","Number of grass","hyhy");
        allCharts.addChart("Days","Average energy","hyhy");
        allCharts.addChart("Days","Average length of life","hyhy");
        allCharts.addChart("Days","Average number of children","hyhy");
        engine = new SimulationEngine(map, welcomeScreen.giveStartAnimals(),500,welcomeScreen.giveIsMagic(),allCharts);
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


        /*final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        scheduledExecutorService.scheduleAtFixedRate(() -> {
            Platform.runLater(() -> {
                Date now = new Date();
                series.getData().add(new XYChart.Data<>(simpleDateFormat.format(now), engine.getNumOfAnimals()));

                if (series.getData().size() > WINDOW_SIZE)
                    series.getData().remove(0);
            });
        }, 0, 1, TimeUnit.SECONDS);*/
        HBox charts = new HBox(allCharts.getLineCharts().get(0),allCharts.getLineCharts().get(1),
                allCharts.getLineCharts().get(2),allCharts.getLineCharts().get(3),allCharts.getLineCharts().get(4));
        VBox mapScreen = new VBox(new HBox(buttonStart,buttonPause),grid,charts);
        //VBox mapScreen = new VBox(new HBox(buttonStart,buttonPause),allCharts.getLineCharts().get(0),allCharts.getLineCharts().get(1),grid);
        map.drawGridPane(grid);
        mainScreen.getChildren().clear();
        mainScreen.getChildren().add(mapScreen);

    }


}
