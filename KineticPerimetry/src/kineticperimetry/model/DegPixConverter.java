package kineticperimetry.model;

public final class DegPixConverter {
	
	public final static double screenResX=1366;//1920
	public final static double screenResY=768;//1080
	public final static double screenSizeInch=13.3;//22
	public final static double screenXInch=16/(Math.sqrt(9*9+16*16))*screenSizeInch;
	public final static double screenYInch=9/(Math.sqrt(9*9+16*16))*screenSizeInch;
	public final static double fixX=screenResX/2;
	public final static double fixY=screenResY/2;
	public final static double distToScreen=screenYInch/2*2.54/Math.tan(Math.toRadians(40));//In cm
	
	public static double convertPixToDegX(double pix) {
    	return Math.atan((pix-fixX)/(screenResX/screenXInch)*2.54/distToScreen);
    }
    
    public static double convertPixToDegY(double pix) {
    	return Math.atan((pix-fixY)/(screenResY/screenYInch)*2.54/distToScreen);
    }
    
    public static double convertDegToPixX(double deg) {
    	return Math.tan(Math.toRadians(deg))*distToScreen/2.54*(screenResX/screenXInch)+fixX;
    }
    
    public static double convertDegToPixY(double deg) {
    	return Math.tan(Math.toRadians(deg))*distToScreen/2.54*(screenResY/screenYInch)+fixY;
    }
}
