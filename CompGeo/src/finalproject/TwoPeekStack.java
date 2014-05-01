package finalproject;

import java.util.Stack;

public class TwoPeekStack {
	private Stack<Point> storage;
	private Point peek1;
	private Point peek2;
	
	public TwoPeekStack(){
		storage = new Stack<Point>();
		peek1 = null;
		peek2 = null;
	}
	
	public int size(){
		int count = storage.size();
		if (peek1 != null)
			count++;
		if (peek2 != null)
			count++;
		return count;
	}
	
	public void push(Point x){
		if (peek2 != null){
			storage.push(peek2);
		}
		peek2 = peek1;
		peek1 = x;
	}
	
	public Point pop(){
		Point returnPoint = peek1;
		peek1 = peek2;
		if (storage.size() == 0)
			peek2 = null;
		else
			peek2 = storage.pop();
		return returnPoint;
	}
	
	public Point firstPeek(){
		return peek1;
	}
	
	public Point secondPeek(){
		return peek2;
	}
}
