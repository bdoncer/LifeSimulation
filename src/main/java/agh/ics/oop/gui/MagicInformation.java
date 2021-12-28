package agh.ics.oop.gui;

import javafx.scene.control.Label;

public class MagicInformation{
    Label text = new Label(" ");
    public Label getLabel(){
        return text;
    }
    public void giveMagic(){
        text.setText("IT'S MAGIC!");
    }
    public void takeAwayMagic(){
        text.setText(" ");
    }
}

