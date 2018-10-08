package kineticperimetry.model;

import javafx.stage.Screen;

public final class DegPixConverter {
	public static double convertPixToDegX(double pix, double fixX, double distToScreen) {
    	return Math.atan((pix-fixX)/Screen.getPrimary().getDpi()*2.54/distToScreen);
    }
    
    public static double convertPixToDegY(double pix, double fixY, double distToScreen) {
    	return Math.atan((pix-fixY)/Screen.getPrimary().getDpi()*2.54/distToScreen);
    }
    
    public static double convertDegToPixX(double deg, double fixX, double distToScreen) {
    	return Math.tan(Math.toRadians(deg))*distToScreen/2.54*Screen.getPrimary().getDpi()+fixX;
    }
    
    public static double convertDegToPixY(double deg, double fixY, double distToScreen) {
    	return Math.tan(Math.toRadians(deg))*distToScreen/2.54*Screen.getPrimary().getDpi()+fixY;
    }
}
