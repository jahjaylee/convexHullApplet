package finalproject;

import java.awt.*; 
import java.applet.*; 
// import an extra class for the MouseListener 
import java.awt.event.*;
import java.awt.geom.Line2D;
import java.util.ArrayList;

import finalproject.Point;

// Tells the applet you will be using the MouseListener methods.

public class ConvexHulls extends Applet implements MouseListener, ActionListener
{ 
	// The X-coordinate and Y-coordinate of the last click. 
	int xpos;
	int ypos;
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
		button1 = new Button("Button 1");
		add(button1);
		button1.addActionListener(this);

		button2 = new Button("Button 2");
		add(button2);
		button2.addActionListener(this);
		addMouseListener(this); 
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
		
        Point testPoint1 = new Point (5, 5);
        Point testPoint2 = new Point(100, 100);
        Line testLine = new Line (testPoint1, testPoint2);
        drawLine(testLine, g);

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
		if (e.getSource() == button1) 
			System.out.println("Button 1 was pressed");
		else
			System.out.println("Button 2 was pressed");
		
	} 
	
	public void drawLine(Line l, Graphics g){
		g.drawLine(l.p1.getX(), l.p1.getY(), l.p2.getX(), l.p2.getY());
	}

	/* So now you can use the MouseListener instead of Buttons. These methods will be ones that you will 
often use. These methods are good for mouseClicks, but when you need mouseOvers like in Javascript 
then you'll need the MouseMotionListener. 
Go to MouseMotionExample.java 
	 */

} 