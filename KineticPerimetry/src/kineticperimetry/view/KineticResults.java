package kineticperimetry.view;

import java.util.ArrayList;

import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.stage.Stage;
import kineticperimetry.model.DegPixConverter;
import kineticperimetry.model.StimuliVector;

public class KineticResults extends Stage {
	public KineticResults(ArrayList<StimuliVector> data) {
		Pane pane = new Pane();
		pane.setStyle("-fx-background-color: hsb(" + 0 + ", " + 0 + "%, " + 0 + "%);");
		for(int i = 0; i < data.size(); i++) {
			Color color = Color.hsb(
		        0,
		        0,
		        1
		    );
			Ellipse ellipse=(Ellipse)data.get(i).getShape();
			ellipse=new Ellipse(DegPixConverter.convertDegToPixX(data.get(i).getStartXDeg()+data.get(i).getAnswers()[0]*(data.get(i).getEndXDeg()-data.get(i).getStartXDeg())), DegPixConverter.convertDegToPixY(data.get(i).getStartYDeg()+data.get(i).getAnswers()[0]*(data.get(i).getEndYDeg()-data.get(i).getStartYDeg())), ellipse.getRadiusX()/2, ellipse.getRadiusY()/2);
			ellipse.setFill(color);
			ellipse.setStroke(Color.WHITE);
			ellipse.setVisible(true);
			pane.getChildren().add(ellipse);
		}
		this.setScene(new Scene(pane));
	}
}
