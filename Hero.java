import java.awt.Color;
import java.awt.Rectangle;
public class Hero{
	private Location loc;
	private int size;
	private Color color;
	private int dir;

	public Hero(Location loc, int dir, int size, Color color){
		this.loc = loc;
		this.dir = dir;
		this.size = size;
		this.color = color;
	}

	public Color getColor(){
		return color;
	}
	public int getDir(){
		return dir;
	}
	public Location getLoc(){
		return loc;
	}
	public int getSize(){
		return size;
	}
	public void move(int key, char[][] maze){
		int r = getLoc().getR();
		int c = getLoc().getC();
		if(key == 38){
			if(dir==0){
				if(r>0 && (maze[r-1][c]==' ' || maze[r-1][c]=='E'))
					getLoc().setR(-1);
			}
			if(dir==1){
				if(c<maze[0].length-1 && (maze[r][c+1]==' '|| maze[r][c+1]=='E'))
					getLoc().setC(1);
			}
			if(dir==2){
				if(r<maze.length-1 && (maze[r+1][c]==' '|| maze[r+1][c]=='E'))
					getLoc().setR(1);
			}
			if(dir==3){
				if(c>0 && (maze[r][c-1]==' ' || maze[r][c-1]=='E'))
					getLoc().setC(-1);
			}
		}
		if(key == 37){
			dir--;
			if(dir<0)
				dir=3;
		}
		if(key==39){
			dir++;
			if(dir>3)
				dir=0;
		}
	}
	public Rectangle getRect()
	{
		int r = getLoc().getR();
		int c = getLoc().getC();
		return new Rectangle(c*size+size, r*size+size, size, size);
	}
}