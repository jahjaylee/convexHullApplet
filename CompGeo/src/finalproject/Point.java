package finalproject;

public class Point {
	private int x;
	private int y;
	public boolean usedYet;
	
	public Point(int x, int y){
		this.x = x;
		this.y = y;
		usedYet = false;
	}
	
	public void setX(int newX){
		this.x = newX;
	}
	
	public void setY(int newY){
		this.y = newY;
	}
	
	public int getX(){
		return x;
	}

	public int getY(){
		return y;
	}
	
	public String toString(){
		return "(" + x + ", " + y + ")";
	}
}
