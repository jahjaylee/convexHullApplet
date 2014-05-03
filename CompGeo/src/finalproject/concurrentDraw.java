package finalproject;

import java.util.ArrayList;

public class concurrentDraw implements Runnable {
	
	Thread runner;
	ArrayList<Line> lines = new ArrayList<Line>();
	
	public void start() {
	   if (runner == null) {
	       runner = new Thread();
	       runner.start();
	   }
	}
	
	@SuppressWarnings("deprecation")
	public void stop() {
	  if (runner != null) {
	      runner.stop();
	      runner = null;
	  }
	}
	
	public void run() {
		while(true){
			
			try { Thread.sleep(1000); }
				catch (InterruptedException e) { }
			
		}
	}

}
