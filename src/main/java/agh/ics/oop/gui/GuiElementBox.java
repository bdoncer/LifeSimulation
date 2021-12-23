package agh.ics.oop.gui;


import agh.ics.oop.IMapElement;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import java.io.FileInputStream;
import java.io.FileNotFoundException;


public class GuiElementBox {
    VBox b;
    public GuiElementBox(IMapElement ob,int type){
        try{
            String url;
            //chcemy obrazek
            if (type == 1){
                url = ob.getUrl();
            }
            //chcemy energie
            else{
                url = ob.getEnergyUrl();
            }
            Image image = new Image(new FileInputStream(url));
            ImageView pic = new ImageView();
            pic.setFitWidth(20);
            pic.setFitHeight(20);
            pic.setImage(image);
            b = new VBox(pic);
            b.setAlignment(Pos.CENTER);
        }
        catch(FileNotFoundException ex) {
            System.out.println(ex.getMessage());
            System.exit(1);
        }
    }
    public VBox getVBox(){
        return this.b;
    }
}

