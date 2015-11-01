package imageprocessing;
import java.awt.Point;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import edu.princeton.cs.introcs.Stopwatch;

//TODO: comment on raw types discouragement and List's removeall slow speed
/**
 * To manage all coordinates(x,y) of one component
 * @author Thai Kha Le
 *
 */
public class Component {

	private HashSet<Point> pixels;
	
	/**
	 * Constructor
	 */
	public Component() {
		pixels = new HashSet<>();
	}
	
	/**
	 * Getter for the pixels set
	 * @return pixels set
	 */
	public HashSet<Point> getPixels(){
		return pixels;
	}

	//TODO: try sortedlist to see if faster
	/**
	 * Get minimum x,y and maximum x,y of the component
	 * @return array of minimum x,y and maximum x,y
	 * @throws IndexOutOfBoundsException if pixels collection
	 *  is still empty when being accessed
	 */
	public int[] getLimits() throws IndexOutOfBoundsException {
		//Stopwatch watch = new Stopwatch();
		List<Point> pixelsList = new ArrayList<Point>(pixels);
		int xMax = 0;
		int xMin = pixelsList.get(0).x;
		int yMax = 0;
		int yMin = pixelsList.get(0).y;
		for(Point p: pixels) {
			int x = p.x;
			int y = p.y;
			
			if(x < xMin)
				xMin = x;
			if(x > xMax)
				xMax = x;
			if(y < yMin)
				yMin = y;
			if(y > yMax)
				yMax = y;
		}
		//System.out.println(watch.elapsedTime());
		return (new int[]{xMin,xMax,yMin,yMax});
	}
	
	/**
	 * Add a pixel to the component
	 * @param x
	 * @param y
	 */
	public void addPixel(int x, int y) {
		if(x >= 0 && y >= 0)
			pixels.add(new Point(x,y));		
	}
	
	/**
	 * Combine two components (two sets of pixels) together
	 * @param c the component to be combined
	 */
	public void merge(Component c) {
		if( c != null && c.getPixels().size() > 0)
			pixels.addAll(c.getPixels());
	}
	
//	public List<Point> getPixelsList() {
//		return new ArrayList<>(pixels);
//	}
}
