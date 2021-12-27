package agh.ics.oop.gui;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/*public class MagicInformation {*/
/*Label text;
public MagicInformation(){
    this.text = new Label("Magic happened!!!");
}*/
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

