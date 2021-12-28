package agh.ics.oop.gui;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
//klasa do uzyskiwania parametrow wejsciowych, ktore wpisuje uzytkownik
public class WelcomeScreen {
    VBox welcomeScreen = new VBox();
    TextArea widthText = new TextArea("10");
    TextArea heightText = new TextArea("10");
    TextArea jungleRatioText = new TextArea("0.2");
    TextArea startEnergyText = new TextArea("20");
    TextArea moveEnergyText = new TextArea("1");
    TextArea plantEnergyText = new TextArea("10");
    TextArea startAnimalsText = new TextArea("25");
    TextArea isMagic1Text = new TextArea("False");
    TextArea isMagic2Text = new TextArea("False");
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
                new Label("turn on magical mode for Bended Map? (True/False)"),
                isMagic1Text,
                new Label("turn on magical mode for Rectangular Map? (True/False)"),
                isMagic2Text
        );
        welcomeScreen.getChildren().add(startButton);
        welcomeScreen.setAlignment(Pos.CENTER);
    }

    public VBox getWelcomeScreen() {
        return welcomeScreen;
    }

    public int giveWidth(){
        int width = Integer.parseInt(widthText.getText());
        if(width < 0){
            throw new IllegalArgumentException("Width can't be negative");
        }
        return width ;
    }

    public int giveHeight(){
        int height = Integer.parseInt(heightText.getText());
        if(height < 0){
            throw new IllegalArgumentException("Height can't be negative");
        }
        return height ;
    }

    public float giveJungleRatio(){
        float jR = Float.parseFloat(jungleRatioText.getText());
        if(jR < 0){
            throw new IllegalArgumentException("Jungle Ratio can't be negative");
        }
        return jR;
    }

    public int giveStartEnergy(){
        int startEnergy = Integer.parseInt(startEnergyText.getText());
        if(startEnergy < 0){
            throw new IllegalArgumentException("Start Energy can't be negative");
        }
        return startEnergy;
    }

    public int giveMoveEnergy(){
        int moveEnergy = Integer.parseInt(moveEnergyText.getText());
        if(moveEnergy < 0){
            throw new IllegalArgumentException("Move Energy can't be negative");
        }
        return moveEnergy;
    }

    public int givePlantEnergy(){
        int plantEnergy = Integer.parseInt(plantEnergyText.getText());
        if(plantEnergy < 0){
            throw new IllegalArgumentException("Plant Energy can't be negative");
        }
        return plantEnergy;
    }

    public int giveStartAnimals(){
        int startAnimals = Integer.parseInt(startAnimalsText.getText());
        if(startAnimals < 0){
            throw new IllegalArgumentException("Number of Start Animals can't be negative");
        }
        if(startAnimals > giveHeight()*giveWidth()){
            throw new IllegalArgumentException("Too many animals");
        }
        return startAnimals;
    }

    public boolean giveIsMagic1(){
        return Boolean.parseBoolean(isMagic1Text.getText());
    }

    public boolean giveIsMagic2(){
        return Boolean.parseBoolean(isMagic2Text.getText());
    }

}
