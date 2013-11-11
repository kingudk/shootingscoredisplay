package dk.kingu.shootingscoredisplay.utils;

import java.util.List;

import dk.kingu.shootingscoredisplay.datastore.Shot;

public class TargetUtils {

	public static String makeTarget(List<Shot> shots) {
		StringBuilder sb = new StringBuilder();
		sb.append(makeSVGStartTag(1000, 1000));
		sb.append(makeRing(500, 500, 455, "#000000", "#FFFFFF"));
		sb.append(makeRing(500, 500, 405, "#000000", "#FFFFFF"));
		sb.append(makeRing(500, 500, 355, "#000000", "#FFFFFF"));
		sb.append(makeRing(500, 500, 305, "#000000", "#000000"));
		sb.append(makeRing(500, 500, 255, "#FFFFFF", "#000000"));
		sb.append(makeRing(500, 500, 205, "#FFFFFF", "#000000"));
		sb.append(makeRing(500, 500, 155, "#FFFFFF", "#000000"));
		sb.append(makeRing(500, 500, 105, "#FFFFFF", "#000000"));
		sb.append(makeRing(500, 500, 55, "#FFFFFF", "#000000"));
		sb.append(makeRing(500, 500, 5, "#FFFFFF", "#FFFFFF"));
		for(int i = 1; i <= 8; i++) {
			String color;
			if(i <= 3) {
				color = "#000000";
			} else {
				color = "#FFFFFF";
			}
			sb.append(makeRingNumber(975-(i*50), 510, color, i));
			sb.append(makeRingNumber(15+(i*50), 510, color, i));
			sb.append(makeRingNumber(493, 990-(i*50), color, i));
			sb.append(makeRingNumber(493, 30+(i*50), color, i));
		}
		
		if(shots != null) {
			for(Shot shot : shots) {
				sb.append(makeShotMarking((int) (500 + (shot.getXCoord() * 20000)), 
						(int) (500 - (shot.getYCoord() * 20000)), "red"));
			}
		}
		
		sb.append(makeSVGEndTag());
		return sb.toString();
	}
	
	private static String makeSVGStartTag(int width, int height) {
		String startTag = "<svg  xmlns=\"http://www.w3.org/2000/svg\" " +
				"version=\"1.1\" width=\"" + width + "\" height=\"" + height + "\" " +
				"preserveAspectRatio=\"xMinYMin meet\"  viewBox=\"0 0 "+ width +" " + height + "\">";
		
		return startTag;
	}
	
	private static String makeSVGEndTag() {
		return "</svg>";
	}
	
	private static String makeRing(int x, int y, int diameter, String strokeColor, String fillColor) {
		String ring = "<circle cx=\"" + x + "\" cy=\"" + y + "\" " +
				"r=\"" + diameter + "\" stroke=\"" + strokeColor + "\" " +
				"stroke-width=\"2\" fill=\"" + fillColor + "\"/>";
		
		return ring;
	}
	
	private static String makeRingNumber(int x, int y, String color, int val) {
	    String text = "<text x=\"" + x + "\" y=\"" + y + "\" fill=\"" + color + "\" " +
	    		"font-family=\"Verdana\" font-size=\"30\">" + val + "</text>";
	    return text;
	}
	
	private static String makeShotMarking(int x, int y, String color) {
		String ring = "<circle cx=\"" + x + "\" cy=\"" + y + "\" " +
				"r=\"" + 45 + "\" stroke=\"" + color + "\" " +
				"stroke-width=\"2\" fill=\"" + color + "\"/>";
		
		return ring;
	}
}
