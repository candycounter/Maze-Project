public class Location{
	private int r;
	private int c;
	public Location(int r, int c)
	{
		this.r = r;
		this.c = c;
	}
	public void setR(int a){
		r+=a;
	}
	public void setC(int b){
		c+=b;
	}
	public int getR(){
		return r;
	}
	public int getC(){
		return c;
	}
}