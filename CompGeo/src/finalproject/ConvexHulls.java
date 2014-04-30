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
	Button button1, button2, button3, button4;

	// The coordinates of the rectangle we will draw. 
	// It is easier to specify this here so that we can later 
	// use it to see if the mouse is in that area. 

	// The variable that will tell whether or not the mouse 
	// is in the applet area. 
	boolean mouseEntered;

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

		button3 = new Button("Quick Hull");
		add(button3);
		button3.addActionListener(this);
		
		button4 = new Button("Merge Hull");
		add(button4);
		button4.addActionListener(this);

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


		for (int i = 0; i < points.size(); i++){
			g.setColor(Color.black);
			Point tempPoint = points.get(i);
			g.fillRect(tempPoint.getX(), tempPoint.getY(), 5, 5);
			g.setColor(Color.red);
			g.drawString("(" + tempPoint.getX() + ", " + tempPoint.getY() + ")", tempPoint.getX(), tempPoint.getY());
		}
		//Graphics2D g2 = (Graphics2D) g;
		//g2.setStroke(new BasicStroke(2));
		//g2.draw(new Line2D.Float(30, 20, 80, 90));

		//		Point testPoint1 = new Point (99, 50);
		//		Point testPoint2 = new Point(100, 100);
		//		Point testPoint3 = new Point(150, 100);
		//		System.out.println(angleBetween(testPoint3, testPoint2, testPoint1));
		//		System.out.println(orientation(testPoint3, testPoint2, testPoint1));
		g.setColor(Color.black);
		for(Line x : lines){
			drawLine(x, g);
		}
		g.setColor(Color.red);

		// When the user clicks this will show the coordinates of the click 
		// at the place of the click. 
		//g.drawString("("+xpos+","+ypos+")",xpos,ypos);

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
			if (e.getSource() == button2){
				System.out.println("Button 2 was pressed");
				grahamScan();
			}
			else{
				if (e.getSource() == button3){
					System.out.println("Button 3 was pressed");
					quickHull();
				}
				else{
					System.out.println("Button 4 was pressed");
					mergeHull();
				}
			}
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

	public void quickHull(){
		ArrayList<Point> topLeft = new ArrayList<Point>();
		ArrayList<Point> topRight = new ArrayList<Point>();
		ArrayList<Point> bottomLeft = new ArrayList<Point>();
		ArrayList<Point> bottomRight = new ArrayList<Point>();

		ArrayList<Point> listByX = new ArrayList<Point>();
		ArrayList<Point> listByY = new ArrayList<Point>();
		ArrayList<Point> tempPoints = new ArrayList<Point>(); //list that will later contain all points except for the extreme ones
		//setup the lists
		for (int i = 0; i < points.size(); i++){
			listByX.add(points.get(i));
			listByY.add(points.get(i));
			tempPoints.add(points.get(i));
		}
		//sort by x and by y
		Collections.sort(listByX, new PointXCompare());
		Collections.sort(listByY, new PointYCompare());

		ArrayList<Point> extremePoints = new ArrayList<Point>();
		extremePoints.add(listByX.get(0));
		extremePoints.add(listByY.get(listByY.size() - 1));
		extremePoints.add(listByX.get(listByX.size() - 1));
		extremePoints.add(listByY.get(0));

		//removes extremePoints duplicates (e.g. if it's a triangle)
		for (int i = 0; i < extremePoints.size(); i++){
			if (i == extremePoints.size() - 1){
				if (extremePoints.get(i).getX() == extremePoints.get(0).getX() && extremePoints.get(i).getY() == extremePoints.get(0).getY()){
					extremePoints.remove(i);
				}
			}
			else{
				if (extremePoints.get(i).getX() == extremePoints.get(i+1).getX() && extremePoints.get(i).getY() == extremePoints.get(i+1).getY()){
					extremePoints.remove(i);
				}
			}
		}

		//add lines for initial shape creation
		for (int i = 0; i < extremePoints.size() - 1; i++){
			lines.add(new Line(extremePoints.get(i), extremePoints.get(i+1)));
		}
		lines.add(new Line(extremePoints.get(extremePoints.size()-1), extremePoints.get(0)));

		//removes extreme points from tempPoints
		for (int i = 0; i < extremePoints.size(); i++){
			for (int j = 0; j < tempPoints.size(); j++){
				if (extremePoints.get(i).getX() == tempPoints.get(j).getX() && extremePoints.get(i).getY() == tempPoints.get(j).getY()){
					tempPoints.remove(j);
				}
			}
		}

		ArrayList<Point> temp = PointsNotInRegion(tempPoints, extremePoints);

		//seperate temp into topLeft, topRight, bottomLeft, and bottomRight
		ArrayList<Point> topLeftTriangle = new ArrayList<Point>();
		topLeftTriangle.add(listByY.get(0));
		topLeftTriangle.add(new Point(listByX.get(0).getX(),listByY.get(0).getY()));
		topLeftTriangle.add(listByX.get(0));
		topLeft = PointsInRegion(temp, topLeftTriangle);

		ArrayList<Point> bottomLeftTriangle = new ArrayList<Point>();
		bottomLeftTriangle.add(listByX.get(0));
		bottomLeftTriangle.add(new Point(listByX.get(0).getX(), listByY.get(listByY.size()-1).getY()));
		bottomLeftTriangle.add(listByY.get(listByY.size()-1));
		bottomLeft = PointsInRegion(temp, bottomLeftTriangle);

		ArrayList<Point> bottomRightTriangle = new ArrayList<Point>();
		bottomRightTriangle.add(listByY.get(listByY.size()-1));
		bottomRightTriangle.add(new Point(listByX.get(listByX.size()-1).getX(), listByY.get(listByY.size() - 1).getY()));
		bottomRightTriangle.add(listByX.get(listByX.size() - 1));
		bottomRight = PointsInRegion(temp, bottomRightTriangle);

		ArrayList<Point> topRightTriangle = new ArrayList<Point>();
		topRightTriangle.add(listByX.get(listByX.size() - 1));
		topRightTriangle.add(new Point(listByX.get(listByX.size() - 1).getX(), listByY.get(0).getY()));
		topRightTriangle.add(listByY.get(0));
		topRight = PointsInRegion(temp, topRightTriangle);
		

		System.out.println("Top left:");
		for (int i = 0; i < topLeft.size(); i++){
			System.out.println("Point " + i + ": " + topLeft.get(i));
		}

		System.out.println("Top Right:");
		for (int i = 0; i < topRight.size(); i++){
			System.out.println("Point " + i + ": " + topRight.get(i));
		}

		System.out.println("Bottom left:");
		for (int i = 0; i < bottomLeft.size(); i++){
			System.out.println("Point " + i + ": " + bottomLeft.get(i));
		}

		System.out.println("Bottom right:");
		for (int i = 0; i < bottomRight.size(); i++){
			System.out.println("Point " + i + ": " + bottomRight.get(i));
		}
		
		ArrayList<Point> topLeftHull = new ArrayList<Point>();
		ArrayList<Point> bottomLeftHull = new ArrayList<Point>();
		ArrayList<Point> bottomRightHull = new ArrayList<Point>();
		ArrayList<Point> topRightHull = new ArrayList<Point>();


		topLeftHull = quickHullHelper(topLeft, listByY.get(0), listByX.get(0));

		bottomLeftHull = quickHullHelper(bottomLeft, listByX.get(0), listByY.get(listByY.size() - 1));

		bottomRightHull = quickHullHelper(bottomRight, listByY.get(listByY.size() - 1), listByX.get(listByX.size() - 1));

		topRightHull = quickHullHelper(topRight, listByX.get(listByX.size() - 1), listByY.get(0));



		ArrayList<Point> convexHull = new ArrayList<Point>();
		
		convexHull.add(listByY.get(0));
		convexHull.addAll(topLeftHull);

		convexHull.add(listByX.get(0));
		convexHull.addAll(bottomLeftHull);

		convexHull.add(listByY.get(listByY.size() - 1));
		convexHull.addAll(bottomRightHull);

		convexHull.add(listByX.get(listByX.size() - 1));
		convexHull.addAll(topRightHull);
		
		//remove duplicates from convex hull (can happen if the top-most point is also the right-most point, etc)
		for (int i = 0; i < convexHull.size() - 1; i++){
			if (convexHull.get(i).getX() == convexHull.get(i+1).getX() && convexHull.get(i).getY() == convexHull.get(i+1).getY()){
				convexHull.remove(i);
			}
		}
		if (convexHull.get(convexHull.size() - 1).getX() == convexHull.get(0).getX() 
				&& convexHull.get(convexHull.size() - 1).getY() == convexHull.get(0).getY()){
			convexHull.remove(convexHull.size() - 1);
		}
		
		for (int i = 0; i < convexHull.size() - 1; i++){
			lines.add(new Line(convexHull.get(i), convexHull.get(i+1)));
		}
		lines.add(new Line(convexHull.get(convexHull.size()-1), convexHull.get(0)));
		
		
		
		System.out.println("ConvexHull Points:");
		for (int i = 0; i < convexHull.size(); i++){
			System.out.println("Point " + i+ ": " + convexHull.get(i));
		}

		repaint();

	}

	//points go from right to left (CCW order)
	public ArrayList<Point> quickHullHelper(ArrayList<Point> pointsList, Point a, Point b){
		if (a.getX() == b.getX() && a.getY() == b.getY()){
			return new ArrayList<Point>();
		}
		if (pointsList.size() == 0){
			return new ArrayList<Point>();
		}

		int indexOfFarthestPoint = -1;
		double distanceOfFarthestPoint = -1;
		for (int i = 0; i < pointsList.size(); i++){
			double tempDistance = pointToLineDistance(a, b, pointsList.get(i));
			if (tempDistance > distanceOfFarthestPoint){
				distanceOfFarthestPoint = tempDistance;
				indexOfFarthestPoint = i;
			}
		}

		Point c = pointsList.get(indexOfFarthestPoint);
		System.out.println("Farthest point is " + c + " with distance " + distanceOfFarthestPoint + " and index " + indexOfFarthestPoint);
		double slopeOfAB = ((double)(a.getY() - b.getY()))/((double)(a.getX() - b.getX()));
		double slopeOfDividingLine = -1/slopeOfAB;

		double newY = -a.getX()*slopeOfAB*slopeOfDividingLine + a.getY()*slopeOfDividingLine + c.getX()*slopeOfAB*slopeOfDividingLine - c.getY()*slopeOfAB;
		newY = newY/(slopeOfDividingLine - slopeOfAB);

		double newX = (newY - a.getY())/slopeOfAB + a.getX();
		Point d = new Point((int)Math.round(newX), (int)Math.round(newY));

		ArrayList<Point> triangleList = new ArrayList<Point>();
		triangleList.add(a);
		triangleList.add(c);
		triangleList.add(b);
		ArrayList<Point> tempPointsList = PointsNotInRegion(pointsList, triangleList);


		//orientation: true = left, false = right
		ArrayList<Point> leftList = new ArrayList<Point>();
		ArrayList<Point> rightList = new ArrayList<Point>();



		for (int i = 0; i < tempPointsList.size(); i++){
			if (!(tempPointsList.get(i).getX() == c.getX() && tempPointsList.get(i).getY() == c.getY())){
				if (orientation(d, c, tempPointsList.get(i))){
					leftList.add(tempPointsList.get(i));
				}
				else{
					rightList.add(tempPointsList.get(i));
				}
			}
		}

		ArrayList<Point> returnListRight = quickHullHelper(rightList, a, c);
		ArrayList<Point> returnListLeft = quickHullHelper(leftList, c, b);
		ArrayList<Point> returnList = new ArrayList<Point>();

		returnList.addAll(returnListRight);
		returnList.add(c);
		returnList.addAll(returnListLeft);

		return returnList;
	}

	public void mergeHull(){
		ArrayList<Point> listByX = new ArrayList<Point>();
		listByX.addAll(points);
		Collections.sort(listByX, new PointXCompare());
		ArrayList<Point> convexHull = mergeHelper(listByX);
		System.out.println("ConvexHull Points:");
		for (int i = 0; i < convexHull.size(); i++){
			System.out.println("Point " + i+ ": " + convexHull.get(i));
		}
	}
	
	public ArrayList<Point> mergeHelper(ArrayList<Point> pointsList){
		ArrayList<Point> returnList = new ArrayList<Point>();
		System.out.println("Entire List:");
		for (int i = 0; i < pointsList.size(); i++){
			System.out.println("Point: "+ pointsList.get(i));
		}
		
		//compute by brute force if size <= 3
		if (pointsList.size() == 3){
			for (int i = 0; i < pointsList.size(); i++){
				for (int j = 0; j < pointsList.size(); j++){
					for (int k = 0; k < pointsList.size(); k++){
						if (!(i == j || j == k || k == i)){
							if (orientation(pointsList.get(i), pointsList.get(j), pointsList.get(k))){
								returnList.add(pointsList.get(i));
								returnList.add(pointsList.get(j));
								returnList.add(pointsList.get(k));
								return returnList;
							}
						}
					}
				}
			}
		}
		if (pointsList.size() <= 2){
			for (int i = 0; i < pointsList.size(); i++){
				returnList.add(pointsList.get(i));
			}
			return returnList;
		}
		
		ArrayList<Point> leftList = new ArrayList<Point>();
		ArrayList<Point> rightList = new ArrayList<Point>();
		
		for (int i = 0; i < pointsList.size()/2; i++){
			leftList.add(pointsList.get(i));
		}
		for (int i = pointsList.size()/2; i < pointsList.size(); i++){
			rightList.add(pointsList.get(i));
		}
		System.out.println("Left List:");
		for (int i = 0; i < leftList.size(); i ++){
			System.out.println("Point: " + leftList.get(i));
		}
		System.out.println("Right List:");
		for (int i = 0; i < rightList.size(); i++){
			System.out.println("Point: " + rightList.get(i));
		}
		
		ArrayList<Point> leftHull = mergeHelper(leftList);
		ArrayList<Point> rightHull = mergeHelper(rightList);
		
		int rightMostLeftHullIndex = -1;
		int rightMostLeftHullxCoord = -1;
		int leftMostRightHullIndex = 0;
		int leftMostRightHullxCoord = rightHull.get(0).getX();
		
		for (int i = 0; i < leftHull.size(); i++){
			if (leftHull.get(i).getX() > rightMostLeftHullxCoord){
				rightMostLeftHullIndex = i;
				rightMostLeftHullxCoord = leftHull.get(i).getX();
			}
		}
		for (int i = 0; i < rightHull.size(); i++){
			if (rightHull.get(i).getX() < leftMostRightHullxCoord){
				leftMostRightHullIndex = i;
				leftMostRightHullxCoord = rightHull.get(i).getX();
			}
		}
		
		int a = rightMostLeftHullIndex;
		int b = leftMostRightHullIndex;
		Point upperTangentPt1;
		Point upperTangentPt2;
		
		//upper tangent
		boolean firstCheck = orientation(leftHull.get((a-1)%leftHull.size()), leftHull.get(a), rightHull.get(b));
		boolean secondCheck = orientation(leftHull.get((a+1)%leftHull.size()), leftHull.get(a), rightHull.get(b));
		boolean thirdCheck = orientation(leftHull.get(a), rightHull.get(b), rightHull.get((b-1) %rightHull.size() ));
		boolean fourthCheck = orientation(leftHull.get(a), rightHull.get(b), rightHull.get((b+1) %rightHull.size() ));
		
		while (!(!firstCheck && !secondCheck && !thirdCheck && !fourthCheck)){
			while (!(!firstCheck && !secondCheck)){
				a = (a+1)%leftHull.size();
				firstCheck = orientation(leftHull.get((a-1)%leftHull.size()), leftHull.get(a), rightHull.get(b));
				secondCheck = orientation(leftHull.get((a+1)%leftHull.size()), leftHull.get(a), rightHull.get(b));
			}
			
			while (!(!thirdCheck && !fourthCheck)){
				b = (b-1)%rightHull.size();
				thirdCheck = orientation(leftHull.get(a), rightHull.get(b), rightHull.get((b-1) %rightHull.size() ));
				fourthCheck = orientation(leftHull.get(a), rightHull.get(b), rightHull.get((b+1) %rightHull.size() ));
			}
			
			firstCheck = orientation(leftHull.get((a-1)%leftHull.size()), leftHull.get(a), rightHull.get(b));
			secondCheck = orientation(leftHull.get((a+1)%leftHull.size()), leftHull.get(a), rightHull.get(b));
			thirdCheck = orientation(leftHull.get(a), rightHull.get(b), rightHull.get((b-1) %rightHull.size() ));
			fourthCheck = orientation(leftHull.get(a), rightHull.get(b), rightHull.get((b+1) %rightHull.size() ));
		}
		
		upperTangentPt1 = leftHull.get(a);
		upperTangentPt2 = rightHull.get(b);
		
		//lower tangent
		a = rightMostLeftHullIndex;
		b = leftMostRightHullIndex;
		
		firstCheck = orientation(leftHull.get((a-1)%leftHull.size()), leftHull.get(a), rightHull.get(b));
		secondCheck = orientation(leftHull.get((a+1)%leftHull.size()), leftHull.get(a), rightHull.get(b));
		thirdCheck = orientation(leftHull.get(a), rightHull.get(b), rightHull.get((b-1) %rightHull.size() ));
		fourthCheck = orientation(leftHull.get(a), rightHull.get(b), rightHull.get((b+1) %rightHull.size() ));
		
		
		
		
		
		return null;
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

	public class PointYCompare
	implements Comparator<Point> {

		public int compare(final Point a, final Point b) {
			if (a.getY() < b.getY()) {
				return -1;
			}
			else if (a.getY() > b.getY()) {
				return 1;
			}
			else {
				return 0;
			}
		}
	}

	public ArrayList<Point> PointsNotInRegion(ArrayList<Point> points, ArrayList<Point> region){
		ArrayList<Point> pointsNotInRegion = new ArrayList<Point>();
		//if the region is of size 0, then all points are not in the region
		if (region.size() == 0){
			pointsNotInRegion.addAll(points);
			return pointsNotInRegion;
		}
		
		//checks if the points are in the region with orientation test
		for (int i = 0; i < points.size(); i++){
			boolean inRegion = true;
			for (int j = 0; j < region.size() - 1; j++){
				if (!orientation(region.get(j), region.get(j+1), points.get(i))){
					inRegion = false;
				}
			}
			if (!orientation(region.get(region.size() - 1), region.get(0), points.get(i))){
				inRegion = false;
			}
			if (!inRegion){
				pointsNotInRegion.add(points.get(i));
			}
		}

		return pointsNotInRegion;
	}

	public ArrayList<Point> PointsInRegion(ArrayList<Point> points, ArrayList<Point> region){
		ArrayList<Point> pointsInRegion = new ArrayList<Point>();
		//if region is size 0, then no points are in the region
		if (region.size() == 0){
			return pointsInRegion;
		}
		
		for (int i = 0; i < points.size(); i++){
			boolean inRegion = true;
			for (int j = 0; j < region.size() - 1; j++){//add a check for null pointers
				if (!orientation(region.get(j), region.get(j+1), points.get(i))){
					inRegion = false;
				}
			}
			if (!orientation(region.get(region.size() - 1), region.get(0), points.get(i))){
				inRegion = false;
			}
			if (inRegion){
				pointsInRegion.add(points.get(i));
			}
		}

		return pointsInRegion;
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


	//source: http://www.ahristov.com/tutorial/geometry-games/point-line-distance.html
	public double pointToLineDistance(Point A, Point B, Point P) {
		double normalLength = Math.sqrt((B.getX()-A.getX())*(B.getX()-A.getX())+(B.getY()-A.getY())*(B.getY()-A.getY()));
		double returnValue = Math.abs((P.getX()-A.getX())*(B.getY()-A.getY())-(P.getY()-A.getY())*(B.getX()-A.getX()))/normalLength;
		System.out.println("Point: " + P + "'s distance is " + returnValue);
		return returnValue;
	}


	/* So now you can use the MouseListener instead of Buttons. These methods will be ones that you will 
often use. These methods are good for mouseClicks, but when you need mouseOvers like in Javascript 
then you'll need the MouseMotionListener. 
Go to MouseMotionExample.java 
	 */

} 