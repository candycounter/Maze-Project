import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
//import java.io.ArrayIndexOutOfBoundsException;
import java.util.ArrayList;

public class MazeRunner extends JPanel implements KeyListener {

    JFrame frame;
    File fileName;
    char[][] maze = new char[16][43];
    Hero hero;
    int r=1,c=0;
    int pR1 = 0, pC1 = 0, pR2 = 0, pC2 = 0;
    int depth = 3;
    int dir=1;
    int x1 = (int)(Math.random()*120);
    int x2 = (int)(Math.random()*120);
    int moves=0;
    int shrink = 50;
    int shrinkMain = 83;
    int shrinkPaint = 40;
    int timesPlayed = 0;
    int size = 20;
    int roundNum = 1;
    boolean draw3D = false;
    boolean roundNotOver = true;
    boolean passedOne = false;
    boolean passedTwo = false;
    Location end;
    Font font;
    ArrayList<Wall> walls;
    ArrayList<Location>locationList;
    ArrayList<Location>breadCrumbs;

    public MazeRunner(){

        hero = new Hero(new Location(r,c),dir,size,new Color(65,182,230));
        breadCrumbs = new ArrayList<Location>();
        locationList = new ArrayList<Location>();
        if(roundNum == 1 || roundNum==2){
			setBoard(roundNum);
		}
        frame = new JFrame("Shraavan's Maze Project");
        frame.add(this);
        frame.setSize(1200,900);
        frame.addKeyListener(this);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }


    public void setBoard(int roundNum){
		if(roundNum==1)
        	fileName = new File("Maze1.txt");
        else if(roundNum==2)
        	fileName = new File("Maze2.txt");
        try{
            BufferedReader input = new BufferedReader(new FileReader(fileName));
            String text;
            int r = 0;

            draw3D = false;

            while((text=input.readLine())!=null){
                for(int c=0;c<text.length();c++)
                    maze[r][c] = text.charAt(c);
                r++;
            }

            if(draw3D){
				createWalls();
			}


        }catch (IOException e){
            System.out.println("File not found");
        }
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;
        g2.setColor(new Color(0,50,98));       //Navy Blue for 2D
        g2.fillRect(0,0,1200,900);
		dir = hero.getDir();
        g2.setColor(new Color(219,62,177));		//Pink
        g2.drawString("Moves: "+moves, 1000, 640);

        if(!draw3D){
            g2.setColor(new Color(219,62,177));

            for (int c = 0; c < maze[0].length; c++) {
                for (int r = 0; r < maze.length; r++) {
                    if (maze[r][c] == ' ' || maze[r][c]=='E'){
                        g2.fillRect(c * size + size, r * size + size, size, size);
                        locationList.add(new Location(r,c));
					}
                    else g2.drawRect(c * size + size, r * size + size, size, size);

                    if(maze[r][c]=='E'){
                    	end = new Location(r,c);
					}
                }
            }
			pR1 = locationList.get(x1).getR();
			pC1 = locationList.get(x1).getC();                                //Add on:  Power-Ups. When the hero reaches the powerup, their field of view will increase either from 3 spaces ahead to 4 spaces ahead
																			  //Or 4 spaces ahead to 5 spaces ahead
            if(!(passedOne)){
				g2.setColor(new Color(204, 0, 0)); //Red
				g2.fillRect(pC1 * size + size, pR1 * size + size, size, size);
			}
			else{
				g2.setColor(new Color(219,62,177));
				g2.fillRect(pC1 * size + size, pR1* size + size, size, size);
			}
			pR2 = locationList.get(x2).getR();
			pC2 = locationList.get(x2).getC();
            if(!(passedTwo)){
				g2.setColor(new Color(204, 0, 0));
				g2.fillRect(pC2 * size + size, pR2 * size + size, size, size);
			}
			else{
				g2.setColor(new Color(219,62,177));
				g2.fillRect(pC2 * size + size, pR2* size + size, size, size);
			}
            g2.setColor(hero.getColor());
            g2.fill(hero.getRect());
            breadCrumbs.add(new Location(hero.getLoc().getR(), hero.getLoc().getC()));

			for(int i = 0; i<breadCrumbs.size(); i++){                              //Add on: Breadcrumbs. The user will be able to see their tracks on the 2D Maze. It tracks every step that the hero takes.
				int bcR = breadCrumbs.get(i).getR();
				int bcC = breadCrumbs.get(i).getC();
				g2.fillOval(bcC * size + size+5, bcR * size+size+5, 10, 10);
			}


       }else{
            //left wall
            g2.setColor(Color.BLACK);
            g2.fillRect(0,0,1200,900);
            g2.setColor(new Color(255,182,18)); //Yellow
       		g2.drawString("Moves: "+moves, 1000, 640);
            for(int fov=0;fov<walls.size();fov++){
				g2.setPaint(walls.get(fov).getPaint()); //Add On: GradientPainting. The full scope of getPaint() is in the Wall class and it was used to give the user a smoother 3D Maze as they try and go through the maze
				g2.fillPolygon(walls.get(fov).getPolygon());
				g2.setColor(Color.BLACK);
                g2.drawPolygon(walls.get(fov).getPolygon());
            }
        }

        if(hero.getLoc().getR() == end.getR() && hero.getLoc().getC() == end.getC()){
			if(draw3D){
				g2.setColor(new Color(255,182,18));
			}
			else
				g2.setColor(new Color(219,62,177));
			font = new Font("Arial", Font.BOLD, 30);
			g2.setFont(font);
			g2.drawString("You are A-Maze-Ing!", 800, 400);
			g2.drawString("You reached the End", 800, 500);
			if(roundNum==1){
				g2.drawString("Press Enter to try Maze 2", 800, 600);
			}
			roundNotOver = false;
		}

		if((hero.getLoc().getR() == pR1 && hero.getLoc().getC() == pC1) && !(passedOne)){
			passedOne = true;
			depth++;
			if(depth==4)
				shrinkMain = 63;
			else if(depth==5)
				shrinkMain = 50;
		}
		if((hero.getLoc().getR() == pR2 && hero.getLoc().getC() == pC2) && !(passedTwo)){
			passedTwo = true;
			depth++;
			if(depth==4)
				shrinkMain = 63;
			else if(depth==5)
				shrinkMain = 50;
		}

    }

	public void createWalls(){
		walls = new ArrayList<Wall>();
		int rr = hero.getLoc().getR();
		int cc = hero.getLoc().getC();
		breadCrumbs.add(new Location(rr,cc));
		int dir = hero.getDir();
		if((hero.getLoc().getR() == pR1 && hero.getLoc().getC() == pC1) && !(passedOne)){
			passedOne = true;
			depth++;
			if(depth==4)
				shrinkMain = 63;
			else if(depth==5)
				shrinkMain = 50;
		}
		if((hero.getLoc().getR() == pR2 && hero.getLoc().getC() == pC2) && !(passedTwo)){
			passedTwo = true;
			depth++;
			if(depth==4)
				shrinkMain = 63;
			else if(depth==5)
				shrinkMain = 50;
		}


		switch(dir)
		{
			case 0:

				for(int i = 0; i<depth; i++){
					try{
						if(maze[rr-i][cc]=='#'){
							walls.add(getEndWall(i));
							break;
						}
						if(maze[rr-i][cc-1]=='#')
							walls.add(getLeft(i));
						else{
							walls.add(getLeftPath(i+1));
							walls.add(getLeftCeiling(i+1));
							walls.add(getLeftFloor(i+1));
						}

						if(maze[rr-i][cc+1]=='#')
							walls.add(getRight(i));
						else{
							walls.add(getRightPath(i+1));
							walls.add(getRightCeiling(i+1));
							walls.add(getRightFloor(i+1));
						}
						walls.add(getCeiling(i));
						walls.add(getFloor(i));
						if((rr-i==pR1 && cc==pC1) && !(passedOne)){          //3D Version of the powerup, but I mimicked it like the Cool-Math game B-Cubed so that when it is touched, it goes from red to green and the power up is applied.
							walls.add(getPowerUpWall(i));                    //Unlike 2D, it won't disappear. It will, instead, turn green to let the user know that the powerup has been applied. It has no use after it turns green.
						}
						else if((rr-i==pR1 && cc==pC1) && (passedOne)){
							walls.add(getPoweredUpWall(i));
						}
						if((rr-i==pR2 && cc==pC2)  && !(passedTwo)){
							walls.add(getPowerUpWall(i));
						}
						else if((rr-i==pR2 && cc==pC2)  && (passedTwo)){
							walls.add(getPoweredUpWall(i));
						}

					}catch(ArrayIndexOutOfBoundsException e)
					{
					}
				}
				break;
			case 1:
				for(int i = 0; i<depth; i++){
					try{
						if(maze[rr][cc+i]=='#'){
							walls.add(getEndWall(i));
							break;
						}

						if(maze[rr-1][cc+i]=='#')
							walls.add(getLeft(i));
						else{
							walls.add(getLeftPath(i+1));
							walls.add(getLeftCeiling(i+1));
							walls.add(getLeftFloor(i+1));
						}

						if(maze[rr+1][cc+i]=='#')
							walls.add(getRight(i));
						else{
							walls.add(getRightPath(i+1));
							walls.add(getRightCeiling(i+1));
							walls.add(getRightFloor(i+1));
						}
						walls.add(getCeiling(i));
						walls.add(getFloor(i));
						if((rr==pR1 && cc+i==pC1) && !(passedOne)){
							walls.add(getPowerUpWall(i));
						}
						else if((rr==pR1 && cc+i==pC1) && (passedOne)){
							walls.add(getPoweredUpWall(i));
						}
						if((rr==pR2 && cc+i==pC2)  && !(passedTwo)){
							walls.add(getPowerUpWall(i));
						}
						else if((rr==pR2 && cc+i==pC2)  && (passedTwo)){
							walls.add(getPoweredUpWall(i));
						}


					}catch(ArrayIndexOutOfBoundsException e)
					{
					}
				}
				break;
			case 2:
				for(int i = 0; i<depth; i++){
					try{
						if(maze[rr+i][cc]=='#'){
							walls.add(getEndWall(i));
							break;
						}
						if(maze[rr+i][cc+1]=='#')
							walls.add(getLeft(i));
						else{
							walls.add(getLeftPath(i+1));
							walls.add(getLeftCeiling(i+1));
							walls.add(getLeftFloor(i+1));
						}

						if(maze[rr+i][cc-1]=='#')
							walls.add(getRight(i));
						else{
							walls.add(getRightPath(i+1));
							walls.add(getRightCeiling(i+1));
							walls.add(getRightFloor(i+1));
						}
						walls.add(getCeiling(i));
						walls.add(getFloor(i));
						if((rr+i==pR1 && cc==pC1) && !(passedOne)){
							walls.add(getPowerUpWall(i));
						}
						else if((rr+i==pR1 && cc==pC1) && (passedOne)){
							walls.add(getPoweredUpWall(i));
						}
						if((rr+i==pR2 && cc==pC2)  && !(passedTwo)){
							walls.add(getPowerUpWall(i));
						}
						else if((rr+i==pR2 && cc==pC2)  && (passedTwo)){
							walls.add(getPoweredUpWall(i));
						}
					}catch(ArrayIndexOutOfBoundsException e)
					{
					}
				}
				break;
			case 3:
				for(int i = 0; i<depth; i++){
					try{
						if(maze[rr][cc-i]=='#'){
							walls.add(getEndWall(i));
							break;
						}
						if(maze[rr+1][cc-i]=='#')
							walls.add(getLeft(i));
						else{
							walls.add(getLeftPath(i+1));
							walls.add(getLeftCeiling(i+1));
							walls.add(getLeftFloor(i+1));
						}

						if(maze[rr-1][cc-i]=='#')
							walls.add(getRight(i));
						else{
							walls.add(getRightPath(i+1));
							walls.add(getRightCeiling(i+1));
							walls.add(getRightFloor(i+1));
						}
						walls.add(getCeiling(i));
						walls.add(getFloor(i));
						if((rr==pR1 && cc-i==pC1) && !(passedOne)){
							walls.add(getPowerUpWall(i));
						}
						else if((rr==pR1 && cc-i==pC1) && (passedOne)){
							walls.add(getPoweredUpWall(i));
						}
						if((rr==pR2 && cc-i==pC2)  && !(passedTwo)){
							walls.add(getPowerUpWall(i));
						}
						else if((rr==pR2 && cc-i==pC2)  && (passedTwo)){
							walls.add(getPoweredUpWall(i));
						}
					}catch(ArrayIndexOutOfBoundsException e)
					{
					}
				}
				break;

		}
	}


    public void keyPressed(KeyEvent e){
		if(roundNotOver){
			hero.move(e.getKeyCode(),maze);
		}
		else{
			if(e.getKeyCode()==10 && roundNum!=2){
				hero = new Hero(new Location(3,0),dir,size,new Color(65,182,230));         //Add on: Key to another maze. The user has to press Enter in order to move to the second maze. Everything will reset and they will
				roundNum=2;																   //try to get through the new adventure!
				setBoard(roundNum);
				roundNotOver = true;
				breadCrumbs.clear();
				locationList.clear();
				passedOne = false;
				passedTwo = false;
				depth = 3;
				moves = 0;
				shrinkMain = 83;
			}
		}

        if(e.getKeyCode()==38 && roundNotOver){
			moves++;
		}


        if(e.getKeyCode() == 32)
        	draw3D = !draw3D;

        if(draw3D)
        	createWalls();
        repaint();
    }

    public void keyReleased(KeyEvent e){}
    public void keyTyped(KeyEvent e){}

    public Wall getLeftPath(int n)
    {
		int[]rLocs = new int[]{100+shrink*n,100+shrink*n,700-shrink*n,700-shrink*n};
		int[]cLocs = new int[]{shrink*n, 50+shrink*n, 50+shrink*n, shrink*n};
		return new Wall(rLocs, cLocs, 255-shrinkPaint*n, 255-shrinkPaint*n, 255-shrinkPaint*n,"LeftPath",size,shrinkMain);
	}

	public Wall getLeft(int n)
	{
		int[]rLocs = new int[]{100+shrink*n,150+shrink*n,650-shrink*n,700-shrink*n};
		int[]cLocs = new int[]{50+shrink*n, 100+shrink*n, 100+shrink*n, 50+shrink*n};
		return new Wall(rLocs, cLocs, 255-shrinkMain*n, 255-shrinkMain*n, 255-shrinkMain*n,"Left",size,shrinkMain);
	}

	public Wall getRight(int n)
	{
		int[]rLocs = new int[]{150+shrink*n,100+shrink*n,700-shrink*n,650-shrink*n};
		int[]cLocs = new int[]{600-shrink*n, 650-shrink*n, 650-shrink*n, 600-shrink*n};
		return new Wall(rLocs, cLocs, 255-shrinkMain*n, 255-shrinkMain*n, 255-shrinkMain*n,"Right",size,shrinkMain);
	}

	public Wall getRightPath(int n){
		int[]rLocs = new int[]{100+shrink*n,100+shrink*n,700-shrink*n,700-shrink*n};
		int[]cLocs = new int[]{650-shrink*n, 700-shrink*n, 700-shrink*n, 650-shrink*n};
		return new Wall(rLocs, cLocs, 255-shrinkPaint*n, 255-shrinkPaint*n, 255-shrinkPaint*n,"RightPath",size,shrinkMain);
	}

	public Wall getCeiling(int n){
		int[]rLocs = new int[]{100+shrink*n, 150+shrink*n, 150+shrink*n, 100+shrink*n};
		int[]cLocs = new int[]{50+shrink*n,100+shrink*n,600-shrink*n,650-shrink*n};
		return new Wall(rLocs, cLocs, 255-shrinkMain*n, 255-shrinkMain*n, 255-shrinkMain*n,"Ceiling",size,shrinkMain);
	}

	public Wall getFloor(int n){
		int[]rLocs = new int[]{700-shrink*n, 650-shrink*n, 650-shrink*n, 700-shrink*n};
		int[]cLocs = new int[]{50+shrink*n, 100+shrink*n,  600-shrink*n, 650-shrink*n};
		return new Wall(rLocs, cLocs, 255-shrinkMain*n, 255-shrinkMain*n, 255-shrinkMain*n,"Floor",size,shrinkMain);
	}

	public Wall getRightCeiling(int n){
		int[]rLocs = new int[]{100+shrink*n,50+shrink*n,100+shrink*n};
		int[]cLocs = new int[]{650-shrink*n, 700-shrink*n, 700-shrink*n};
		return new Wall(rLocs, cLocs, 255-shrinkPaint*n, 255-shrinkPaint*n, 255-shrinkPaint*n,"RightCeiling",size,shrinkMain);
	}
	public Wall getRightFloor(int n){
		int[]rLocs = new int[]{700-shrink*n, 750-shrink*n, 700-shrink*n};
		int[]cLocs = new int[]{650-shrink*n, 700-shrink*n, 700-shrink*n};
		return new Wall(rLocs, cLocs, 255-shrinkPaint*n, 255-shrinkPaint*n, 255-shrinkPaint*n,"RightFloor",size,shrinkMain);
	}
	public Wall getLeftCeiling(int n){
		int[]rLocs = new int[]{50+shrink*n, 100+shrink*n, 100+shrink*n};
		int[]cLocs = new int[]{shrink*n, 50+shrink*n, shrink*n};
		return new Wall(rLocs, cLocs, 255-shrinkPaint*n, 255-shrinkPaint*n, 255-shrinkPaint*n,"LeftCeiling",size,shrinkMain);
	}
	public Wall getLeftFloor(int n){
		int[]rLocs = new int[]{700-shrink*n, 700-shrink*n, 750-shrink*n};
		int[]cLocs = new int[]{shrink*n, 50+shrink*n, shrink*n};
		return new Wall(rLocs, cLocs, 255-shrinkPaint*n, 255-shrinkPaint*n, 255-shrinkPaint*n,"LeftFloor",size,shrinkMain);
	}

	public Wall getEndWall(int n){
		int[]rLocs = new int[]{100+shrink*n, 100+shrink*n, 700-shrink*n, 700-shrink*n};
		int[]cLocs = new int[]{50+shrink*n, 650-shrink*n,650-shrink*n,50+shrink*n};
		return new Wall(rLocs, cLocs, 255-shrinkMain*n, 255-shrinkMain*n, 255-shrinkMain*n,"EndWall",size,shrinkMain);
	}

	public Wall getPowerUpWall(int n){
		int[]rLocs = new int[]{700-shrink*n, 650-shrink*n, 650-shrink*n, 700-shrink*n};
		int[]cLocs = new int[]{50+shrink*n, 100+shrink*n,  600-shrink*n, 650-shrink*n};
		return new Wall(rLocs, cLocs, 204,0,0,"PowerWall", size,shrinkMain); //Red Floor
	}

	public Wall getPoweredUpWall(int n){
		int[]rLocs = new int[]{700-shrink*n, 650-shrink*n, 650-shrink*n, 700-shrink*n};
		int[]cLocs = new int[]{50+shrink*n, 100+shrink*n,  600-shrink*n, 650-shrink*n};
		return new Wall(rLocs, cLocs, 144,238,144,"PowerWall", size,shrinkMain);  //Green Floor
	}
    public static void main(String [] args){
        MazeRunner app = new MazeRunner();

    }

}