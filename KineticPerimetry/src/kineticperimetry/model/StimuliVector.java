package kineticperimetry.model;

import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Shape;

public class StimuliVector {
	double startXDeg;
	double startYDeg;
	double endXDeg;
	double endYDeg;
	double startXPix;
	double startYPix;
	double endXPix;
	double endYPix;
	int speed;
	double radiusX;
	double radiusY;
	int brightness;
	int displayTime;
	Shape shape;
	double[] answers;
	public StimuliVector() {}
	public StimuliVector(double startXDeg, double startYDeg, double endXDeg, double endYDeg, double radX, double radY, double speed, int brightness) {
		this.startXDeg = startXDeg;
		this.startYDeg = startYDeg;
		this.endXDeg = endXDeg;
		this.endYDeg = endYDeg;
		startXPix = DegPixConverter.convertDegToPixX(startXDeg);
		startYPix = DegPixConverter.convertDegToPixY(startYDeg);
		endXPix = DegPixConverter.convertDegToPixX(endXDeg);
		endYPix = DegPixConverter.convertDegToPixY(endYDeg);
		radiusX = radX;
		radiusY = radY;
		this.brightness = brightness;
		displayTime = (int)(Math.sqrt((endYDeg-startYDeg)*(endYDeg-startYDeg)+(endXDeg-startXDeg)*(endXDeg-startXDeg))/speed);
		shape = new Ellipse(startXPix, startYPix, radiusX, radiusY);
		answers = new double[1];
		for(int i = 0; i < 1; i++) {
			answers[i] = 0;
		}
	}
	public StimuliVector(double xDeg, double yDeg, double radXDeg, double radYDeg, int time, int brightness) {
		startXDeg = endXDeg = xDeg;
		startYDeg = endYDeg = yDeg;
		startXPix = DegPixConverter.convertDegToPixX(startXDeg);
		startYPix = DegPixConverter.convertDegToPixY(startYDeg);
		endXPix = DegPixConverter.convertDegToPixX(endXDeg);
		endYPix = DegPixConverter.convertDegToPixY(endYDeg);
		radiusX = (DegPixConverter.convertDegToPixX(xDeg+radXDeg) - DegPixConverter.convertDegToPixX(xDeg-radXDeg))/2;
		radiusY = (DegPixConverter.convertDegToPixY(yDeg+radYDeg) - DegPixConverter.convertDegToPixY(yDeg-radYDeg))/2;
		this.brightness = brightness;
		displayTime = time;
		shape = new Ellipse(startXPix, startYPix, radiusX, radiusY);
		answers = new double[1];
		for(int i = 0; i < 1; i++) {
			answers[i] = 0;
		}
	}
	public double getStartXDeg() {
		return startXDeg;
	}
	public void setStartXDeg(double startXDeg) {
		this.startXDeg = startXDeg;
	}
	public double getStartYDeg() {
		return startYDeg;
	}
	public void setStartYDeg(double startYDeg) {
		this.startYDeg = startYDeg;
	}
	public double getEndXDeg() {
		return endXDeg;
	}
	public void setEndXDeg(double endXDeg) {
		this.endXDeg = endXDeg;
	}
	public double getEndYDeg() {
		return endYDeg;
	}
	public void setEndYDeg(double endYDeg) {
		this.endYDeg = endYDeg;
	}
	public double getStartXPix() {
		return startXPix;
	}
	public void setStartXPix(double startXPix) {
		this.startXPix = startXPix;
	}
	public double getStartYPix() {
		return startYPix;
	}
	public void setStartYPix(double startYPix) {
		this.startYPix = startYPix;
	}
	public double getEndXPix() {
		return endXPix;
	}
	public void setEndXPix(double endXPix) {
		this.endXPix = endXPix;
	}
	public double getEndYPix() {
		return endYPix;
	}
	public void setEndYPix(double endYPix) {
		this.endYPix = endYPix;
	}
	public int getSpeed() {
		return speed;
	}
	public void setSpeed(int speed) {
		this.speed = speed;
	}
	public double getRadiusX() {
		return radiusX;
	}
	public void setRadiusX(double radiusX) {
		this.radiusX = radiusX;
	}
	public double getRadiusY() {
		return radiusY;
	}
	public void setRadiusY(double radiusY) {
		this.radiusY = radiusY;
	}
	public int getBrightness() {
		return brightness;
	}
	public void setBrightness(int brightness) {
		this.brightness = brightness;
	}
	public int getDisplayTime() {
		return displayTime;
	}
	public void setDisplayTime(int displayTime) {
		this.displayTime = displayTime;
	}
	public Shape getShape() {
		return shape;
	}
	public void setShape(Shape shape) {
		this.shape = shape;
	}
	public double[] getAnswers() {
		return answers;
	}
	public void setAnswers(double[] answers) {
		this.answers = answers;
	}
}
