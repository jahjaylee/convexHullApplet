package finalproject;

import java.awt.*; 
import java.applet.*; 
// import an extra class for the MouseListener 
import java.awt.event.*;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import finalproject.Point;

// Tells the applet you will be using the MouseListener methods.
//(aei+bfg+cdh)-(ceg+bdi+afh)


public class ConvexHulls extends Applet implements MouseListener, ActionListener
{ 
	// The X-coordinate and Y-coordinate of the last click. 
	int xpos;
	int ypos;
	ArrayList<Point> currentHull = new ArrayList<Point>();
	ArrayList<Point> points = new ArrayList<Point>();
	ArrayList<Line> lines = new ArrayList<Line>();
	Button button1, button2;

	// The coordinates of the rectangle we will draw. 
	// It is easier to specify this here so that we can later 
	// use it to see if the mouse is in that area. 

	// The variable that will tell whether or not the mouse 
	// is in the applet area. 
	boolean mouseEntered;

	// variable that will be true when the user clicked i the rectangle  
	// the we will draw. 
	boolean rect1Clicked;

	public void init()  
	{ 
		// Add the MouseListener to your applet 
		setSize(800, 800);
		button1 = new Button("Jarvis March");
		add(button1);
		button1.addActionListener(this);

		button2 = new Button("Graham's Scan");
		add(button2);
		button2.addActionListener(this);
		addMouseListener(this); 
	}

	public boolean orientation(Point p, Point q, Point r){
		//IF SOMETHING IS REALLY FUCKED UP IT IS PROBABLY HERE
		int result = q.getX()*r.getY() + p.getX()*q.getY() + p.getY()*r.getX() - (p.getY()*q.getX()+p.getX()*r.getY()+q.getY()*r.getX());
		if (result==0){
			//Don't fuck with general position
		}
		return result<0;
		//True means that it turns left
		//False means that it turns right
	}

	public void paint(Graphics g)  
	{ 
		g.setColor(Color.black);

		for (int i = 0; i < points.size(); i++){
			Point tempPoint = points.get(i);
			g.fillRect(tempPoint.getX(), tempPoint.getY(), 5, 5);
		}
		Graphics2D g2 = (Graphics2D) g;
		g2.setStroke(new BasicStroke(2));
		//g2.draw(new Line2D.Float(30, 20, 80, 90));

//		Point testPoint1 = new Point (99, 50);
//		Point testPoint2 = new Point(100, 100);
//		Point testPoint3 = new Point(150, 100);
//		System.out.println(angleBetween(testPoint3, testPoint2, testPoint1));
//		System.out.println(orientation(testPoint3, testPoint2, testPoint1));
		
		for(Line x : lines){
			drawLine(x, g);
		}
		g.setColor(Color.red);

		// When the user clicks this will show the coordinates of the click 
		// at the place of the click. 
		g.drawString("("+xpos+","+ypos+")",xpos,ypos);

		//if (mouseEntered) g.drawString("Mouse is in the applet area",20,160); 
		//else g.drawString("Mouse is outside the Applet area",20,160); 
	}

	/* These methods always have to present when you implement MouseListener

 public void mouseClicked (MouseEvent me) {} 
 public void mouseEntered (MouseEvent me) {} 
 public void mousePressed (MouseEvent me) {} 
 public void mouseReleased (MouseEvent me) {}  
 public void mouseExited (MouseEvent me) {}  
	 */

	// This method will be called when the mouse has been clicked. 
	public void mouseClicked (MouseEvent me) {

		// Save the coordinates of the click lke this. 
		xpos = me.getX(); 
		ypos = me.getY();
		points.add(new Point(xpos, ypos));

		//show the results of the click 
		repaint();

	}

	// This is called when the mous has been pressed 
	public void mousePressed (MouseEvent me) {}

	// When it has been released 
	// not that a click also calls these Mouse-Pressed and Released. 
	// since they are empty nothing hapens here. 
	public void mouseReleased (MouseEvent me) {} 

	// This is executed when the mouse enters the applet. it will only 
	// be executed again when the mouse has left and then re-entered. 
	public void mouseEntered (MouseEvent me) { 
		// Will draw the "inside applet message" 
		//mouseEntered = true; 
		//repaint(); 
	}

	// When the Mouse leaves the applet. 
	public void mouseExited (MouseEvent me) { 
		// will draw the "outside applet message" 
		//mouseEntered = false; 
		//repaint(); 
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == button1) {
			System.out.println("Button 1 was pressed");
			jarvisMarch();
		}
		else{
			System.out.println("Button 2 was pressed");
			grahamScan();
		}

	} 
	
	public void jarvisMarch(){
		Point max = new Point(0,0);
		Point next = new Point(0,0);
		int count = 0;
		double angle = 360;
		
		for(Point i : points){
			if(i.getY()>max.getY()){
				max = i;
			}
		}
		
		currentHull.add(new Point(0,max.getY()));
		
		currentHull.add(max);
		max.usedYet = true;
		
		while(currentHull.get(count+1)!=currentHull.get(1)||count==0){
			System.out.println("Infinite loop?");
			for(Point i : points){
				if(i!=currentHull.get(count+1) && i != currentHull.get(count)){
					if(angle>angleBetween(currentHull.get(count),currentHull.get(count+1),i)){
						next = i;
						angle = angleBetween(currentHull.get(count),currentHull.get(count+1),i);
						System.out.println("Biggest Angle: " + angle);
					}
				}
			}
			next.usedYet = true;
			currentHull.add(next);
			angle = 360;
			count ++;
			lines.add(new Line(currentHull.get(count),next));
		}
		repaint();
		//lines.add(new Line(currentHull.get(0),currentHull.get(1)));
		//lines.add(new Line(currentHull.get(1),currentHull.get(2)));
		//lines.add(new Line(currentHull.get(2),currentHull.get(3)));
		//lines.add(new Line(currentHull.get(3),currentHull.get(4)));
		//lines.add(new Line(currentHull.get(4),currentHull.get(5)));
		
	}

	public void grahamScan(){
		
		ArrayList<Point> upper = new ArrayList<Point>();
		ArrayList<Point> lower = new ArrayList<Point>();
		Collections.sort(points, new PointXCompare());
		Point left = points.get(0);
		Point right = points.get(points.size()-1);
		//lines.add(new Line(points.get(0),points.get(points.size()-1)));
		
		
		upper.add(left);
		lower.add(left);
		for(Point i : points){
			if(orientation(left,right,i)){
				upper.add(i);
			}
			else{
				lower.add(i);
			}
		}
		upper.add(right);
		lower.add(right);
		
//		for(Point i : upper){
//			lines.add(new Line(upper.get(0),i));
//		}
		
		for(int i = 0; i < upper.size()-2; i++){
			if(orientation(upper.get(i),upper.get(i+1),upper.get(i+2))){
				upper.remove(i+1);
				i--;
			}
		}
		
		for(int i = 0; i < upper.size()-1; i++){
			lines.add(new Line(upper.get(i),upper.get(i+1)));
		}
		
		for(int i = 0; i < lower.size()-2; i++){
			if(!orientation(lower.get(i),lower.get(i+1),lower.get(i+2))){
				lower.remove(i+1);
				i--;
			}
		}
		
		for(int i = 0; i < lower.size()-1; i++){
			lines.add(new Line(lower.get(i),lower.get(i+1)));
		}
		
		repaint();
	}

    public class PointXCompare
        implements Comparator<Point> {

        public int compare(final Point a, final Point b) {
            if (a.getX() < b.getX()) {
                return -1;
            }
            else if (a.getX() > b.getX()) {
                return 1;
            }
            else {
                return 0;
            }
        }
    }
	
	public void drawLine(Line l, Graphics g){
		g.drawLine(l.p1.getX(), l.p1.getY(), l.p2.getX(), l.p2.getY());
	}

	public double angleBetween(Point previous, Point center, Point current) {
		double angle = Math.toDegrees(Math.atan2(current.getX() - center.getX(),current.getY() - center.getY())-
				Math.atan2(previous.getX()- center.getX(),previous.getY()- center.getY()));
		if(angle<0){
			angle += 360;
		}
		return angle;
	}

	/* So now you can use the MouseListener instead of Buttons. These methods will be ones that you will 
often use. These methods are good for mouseClicks, but when you need mouseOvers like in Javascript 
then you'll need the MouseMotionListener. 
Go to MouseMotionExample.java 
	 */

} 