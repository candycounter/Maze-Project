import java.awt.Color;
import java.awt.Polygon;
import java.awt.GradientPaint;
public class Wall{
	private int[]rows;
	private int[]cols;
	private int r;
	private int g;
	private int b;
	private String path;
	private int size;
	private int shrink;

	public Wall(int[]rows, int[]cols,int r, int g, int b, String path, int size, int shrink){
		this.rows = rows;
		this.cols = cols;
		this.r = r;
		this.g = g;
		this.b = b;
		this.size = size;
		this.path = path;
		this.shrink = shrink;
	}
	public int[] getRow(){
		return rows;
	}
	public int[] getCol(){
		return cols;
	}
	public String getPath(){
		return path;
	}
	public Polygon getPolygon(){
		if(getPath().equals("RightFloor") || getPath().equals("RightCeiling") || getPath().equals("LeftCeiling") || getPath().equals("LeftFloor")){
			return new Polygon(getCol(), getRow(), 3);
		}
		return new Polygon(getCol(), getRow(),  4);

	}
	public int getShrink(){
		return shrink;
	}
	public GradientPaint getPaint(){
		int endR = r-getShrink();
		int endG = g-getShrink();
		int endB = b-getShrink();
		switch(getPath()){
			case "Floor":
				return new GradientPaint(cols[0],rows[0],new Color(r,g,b),cols[0],rows[1],new Color(endR,endG,endB));
			case "Ceiling":
				return new GradientPaint(cols[0],rows[0],new Color(r,g,b),cols[0],rows[1],new Color(endR,endG,endB));
			case "EndWall":
				return new GradientPaint(cols[0],rows[0],new Color(r,g,b),rows[1],cols[0],new Color(endR,endG,endB));
			case "Right":
				return new GradientPaint(cols[1],rows[0],new Color(r,g,b),cols[0],rows[0],new Color(endR,endG,endB));
			case "RightPath":
				return new GradientPaint(cols[1],rows[0],new Color(r,g,b),cols[0],rows[0],new Color(endR,endG,endB));
			case "RightCeiling":
				return new GradientPaint(cols[1],rows[0],new Color(r,g,b),cols[0],rows[0],new Color(endR,endG,endB));
			case "RightFloor":
				return new GradientPaint(cols[1],rows[0],new Color(r,g,b),cols[0],rows[0],new Color(endR,endG,endB));
			case "PowerWall":
				return new GradientPaint(cols[0],rows[0],new Color(r,g,b),cols[0],rows[0],new Color(r,g,b));
			default:
				return new GradientPaint(cols[0],rows[0],new Color(r,g,b),cols[1],rows[0],new Color(endR,endG,endB));
		}
	}
}