package agh.ics.oop.gui;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
//klasa do uzyskiwania parametrow wejsciowych, ktore wpisuje uzytkownik
public class WelcomeScreen {
    VBox welcomeScreen = new VBox();
    TextArea widthText = new TextArea("8");
    TextArea heightText = new TextArea("8");
    TextArea jungleRatioText = new TextArea("0.2");
    TextArea startEnergyText = new TextArea("10");
    TextArea moveEnergyText = new TextArea("1");
    TextArea plantEnergyText = new TextArea("2");
    TextArea startAnimalsText = new TextArea("10");
    TextArea isMagicText = new TextArea("False");
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
                startEnergyText,
                new Label("move energy"),
                moveEnergyText,
                new Label("plant energy"),
                plantEnergyText,
                new Label("start animals"),
                startAnimalsText,
                new Label("turn on magical mode? (True/False)"),
                isMagicText

        );
        welcomeScreen.getChildren().add(startButton);
        welcomeScreen.setAlignment(Pos.CENTER);
    }

    public VBox getWelcomeScreen() {
        return welcomeScreen;
    }

    public int giveWidth(){
        return Integer.parseInt(widthText.getText());
    }

    public int giveHeight(){
        return Integer.parseInt(heightText.getText());
    }

    public float giveJungleRatio(){
        return Float.parseFloat(jungleRatioText.getText());
    }

    public int giveStartEnergy(){
        return Integer.parseInt(startEnergyText.getText());
    }

    public int giveMoveEnergy(){
        return Integer.parseInt(moveEnergyText.getText());
    }

    public int givePlantEnergy(){
        return Integer.parseInt(plantEnergyText.getText());
    }

    public int giveStartAnimals(){
        return Integer.parseInt(startAnimalsText.getText());
    }

    public boolean giveIsMagic(){
        return Boolean.parseBoolean(isMagicText.getText());
    }

}
