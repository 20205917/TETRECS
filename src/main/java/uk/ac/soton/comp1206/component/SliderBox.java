package uk.ac.soton.comp1206.component;

import javafx.beans.property.DoubleProperty;
import javafx.geometry.Pos;
import javafx.scene.control.Slider;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import uk.ac.soton.comp1206.utils.Multimedia;

public class SliderBox extends VBox {
    Slider slider;
    public SliderBox(String name,int x,double volume){
        super(x);
        setAlignment(Pos.CENTER);
        var text = new Text(name);
        text.getStyleClass().add("heading");
        slider = new Slider(0, 100, volume);
        slider.setPrefSize(300, 20);
        slider.setShowTickMarks(true);
        slider.setMajorTickUnit(25);
        getChildren().addAll(text, slider);
    }

    public Slider getSlider(){
        return slider;
    }
}
