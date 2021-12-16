package agh.ics.oop.gui;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;

public class WelcomeScreen {
    VBox welcomeScreen = new VBox();
    TextArea widthText = new TextArea("8");
    TextArea heightText = new TextArea("8");
    TextArea jungleRatioText = new TextArea("0.2");
    TextArea startEnergyText = new TextArea("10");
    public WelcomeScreen(App app){
        Button startButton = new Button("Rozpocznij symulacje :))");
        startButton.setOnAction(event -> {
            app.startSimulation();
        });
        startButton.setPrefWidth(200);
        startButton.setPrefHeight(60);
        welcomeScreen.setPrefHeight(400);
        welcomeScreen.setPrefWidth(300);
        welcomeScreen.getChildren().addAll(
                new Label("width: "),
                widthText,
                new Label("height: "),
                heightText,
                new Label("Jungle Ratio: "),
                jungleRatioText,
                new Label("start energy"),
                startEnergyText
        );
        welcomeScreen.getChildren().add(startButton);
        welcomeScreen.setAlignment(Pos.CENTER);
    }

    public VBox getWelcomeScreen() {
        return welcomeScreen;
    }

    public int getWidth(){
        return Integer.parseInt(widthText.getText());
    }

    public int getHeight(){
        return Integer.parseInt(heightText.getText());
    }

    public float getJungleRatio(){
        return Float.parseFloat(jungleRatioText.getText());
    }

    public int getStartEnergy(){
        return Integer.parseInt(startEnergyText.getText());
    }


}
