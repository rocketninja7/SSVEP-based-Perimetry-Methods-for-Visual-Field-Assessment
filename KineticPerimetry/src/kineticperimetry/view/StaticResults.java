package kineticperimetry.view;

import java.util.ArrayList;

import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.stage.Stage;
import kineticperimetry.model.DegPixConverter;
import kineticperimetry.model.StimuliVector;

public class StaticResults extends Stage {
	private double limit;
	
	private double interval;
	
	private ArrayList<StimuliVector> data;
	
	public StaticResults(double limit, double interval, ArrayList<StimuliVector> data) {
		this.limit = limit;
		this.interval = interval;
		this.data = data;
		
		Pane pane = new Pane();
		pane.setStyle("-fx-background-color: hsb(" + 0 + ", " + 0 + "%, " + 0 + "%);");
		
		for(int i = 0; i < data.size(); i++) {
			int sum = 0;
			for(int j = 0; j < data.get(i).getAnswers().length; j++) {
				sum+=(int)data.get(i).getAnswers()[j];
			}
			Color color;
			if(sum == 2) {
				color = Color.hsb(
	                0,
	                0,
	                0
	            );
			}
			else if(sum == 1) {
				color = Color.hsb(
		                0,
		                0,
		                0.5
		            );
				}
			else {
				color = Color.hsb(
		            0,
		            0,
		            1
		        );
			}
			Ellipse ellipse=(Ellipse)data.get(i).getShape();
			ellipse=new Ellipse(DegPixConverter.fixX/2+ellipse.getCenterX()/2, DegPixConverter.fixY/2+ellipse.getCenterY()/2, ellipse.getRadiusX()/2, ellipse.getRadiusY()/2);
			ellipse.setFill(color);
			ellipse.setStroke(Color.WHITE);
			ellipse.setVisible(true);
			pane.getChildren().add(ellipse);
		}
		
		this.setScene(new Scene(pane));
	}
}
