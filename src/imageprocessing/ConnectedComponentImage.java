package imageprocessing;

import java.awt.Color;
import java.awt.Point;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import edu.princeton.cs.introcs.Picture;
import edu.princeton.cs.introcs.Stopwatch;

/**
 * Model class of an image processor
 * 
 * @author Thai Kha Le
 *
 */
public class ConnectedComponentImage implements ComponentImage, UnionFind {

	private static Color[] colors = new Color[] { Color.BLUE, Color.CYAN, Color.GREEN, Color.MAGENTA, Color.ORANGE,
			Color.PINK, Color.YELLOW };
	private ConnectedComponentImage processor;
	private double time = 0;
	private Picture picture, pic; // original picture; binarized picture
	private Color foreground;
	private int hei, wid, count, currentLabel, mode;
	private int[] labels, equi; // equivalence table
	private HashMap<Integer, Component> components;
	private Component background;
	private boolean readyToUse = false;

	/**
	 * Initialise fields
	 * 
	 * @param fileLocation
	 *            file location of type String
	 */
	public ConnectedComponentImage(String fileLocation, int mode) throws Exception {
		picture = new Picture(fileLocation);
		pic = new Picture(picture);
		wid = picture.width();
		hei = picture.height();
		labels = new int[hei * wid];
		equi = new int[hei * wid];
		currentLabel = 1;
		count = 0;
		components = new HashMap<>();
		background = new Component();
		this.mode = (mode == 0) ? 0 : 1; // Mode 0: brighter is a foreground,
											// Mode 1: darker is a foreground
		foreground = (mode == 0) ? Color.white : Color.black; // after
																// binarizing,
																// foreground is
																// either white
																// or black
		processor = this; // "this" is a reference value
	}

	/**
	 * Height getter
	 * 
	 * @return height of the picture
	 */
	public int getHeight() {
		return hei;
	}

	/**
	 * Width getter
	 * 
	 * @return width of the picture
	 */
	public int getWidth() {
		return wid;
	}

	/**
	 * Mode getter
	 * 
	 * @return running mode of the processor
	 */
	public int getMode() {
		return mode;
	}

	/**
	 * Time getter
	 * 
	 * @return the processing time
	 * @throws Exception
	 *             if setup() failed
	 */
	public double getProcessingTime() throws Exception {
		if (!readyToUse)
			setup();
		return time;
	}

	/**
	 * Run the two-pass scanning and complete the components field within a time
	 * interval. If an TimeoutException is thrown, which indicates that
	 * executing time has exceeded the given allowed time, the whole program
	 * will be shutdown (this is handled by whatever "driver" is using an
	 * instance of this class).
	 * 
	 * @throws Exception
	 *             including TimeoutException and other Exception types
	 */
	@Override
	public void setup() throws Exception {
		Stopwatch watch = new Stopwatch();
		// Create a Callable object, give it to an ExecutorService, execute and
		// retrieve the result
		Callable<ConnectedComponentImage> s = new MyTask(this);
		final ExecutorService service = Executors.newSingleThreadExecutor();
		try {
			// the result is wrapped up by a Future object
			processor = service.submit(s).get(20, TimeUnit.SECONDS); // 20
																		// seconds
																		// is
																		// allowed
		} finally {
			service.shutdown(); // shutdown the ExecutorService after executing
		}
		readyToUse = true;
		time = watch.elapsedTime();
	}

	/**
	 * Local class to create a Callable object
	 * 
	 * @author Thai Kha Le
	 *
	 */
	private class MyTask implements Callable<ConnectedComponentImage> {
		private ConnectedComponentImage c;

		/**
		 * Constructor
		 * 
		 * @param c
		 *            ConnectedComponentImage instance
		 */
		public MyTask(ConnectedComponentImage c) {
			this.c = c;
		}

		/**
		 * Attempt to execute the defined tasks when invoked
		 */
		@Override
		public ConnectedComponentImage call() throws Exception {
			c.twopassProcess();
			//c.completeDB();
			//c.readyToUse = true;
			return c;
		}
	}

	/**
	 * The two-pass scan
	 */
	private void twopassProcess() {
		// First pass
		for (int x = 0; x < wid; x++) {
			for (int y = 0; y < hei; y++) {
				Color nColor = lumi(pic, x, y);
				pic.set(x, y, nColor);
				if (nColor.equals(foreground)) {
					int id = y * wid + x;
					int idAbove = (y - 1) * wid + x;
					int idLeft = y * wid + x - 1;

					// For labelling, check if would go out of picture's
					// boundary
					boolean yAbove = y - 1 >= 0;
					boolean xLeft = x - 1 >= 0;

					int labelAbove = yAbove ? labels[idAbove] : -1;
					int labelLeft = xLeft ? labels[idLeft] : -1;

					boolean bothForeground = yAbove && xLeft && (labelAbove != -1 && labelLeft != -1)
							&& pic.get(x, y - 1).equals(foreground) && pic.get(x - 1, y).equals(foreground);
					boolean aboveForeground = yAbove && (labelAbove != -1) && pic.get(x, y - 1).equals(foreground);
					boolean leftForeground = xLeft && (labelLeft != -1) && pic.get(x - 1, y).equals(foreground);

					if (bothForeground && labelAbove != labelLeft) { // check
						// both
						// above
						// and
						// left
						int smallLabel = labelAbove < labelLeft ? labelAbove : labelLeft;
						int bigLabel = labelAbove > labelLeft ? labelAbove : labelLeft;
						labels[id] = smallLabel;
						components.get(smallLabel).addPixel(x, y);
						union(smallLabel, bigLabel);
					} else if (bothForeground && labelAbove == labelLeft) {
						labels[id] = labelAbove;
						components.get(labelAbove).addPixel(x, y);
					} else if (aboveForeground && labelAbove != 0) { // check
																		// above
						labels[id] = labelAbove;
						components.get(labelAbove).addPixel(x, y);
					} else if (leftForeground && labelLeft != 0) { // check left
						labels[id] = labelLeft;
						components.get(labelLeft).addPixel(x, y);
					} else {
						labels[id] = currentLabel;
						equi[currentLabel] = currentLabel;

						Component c = new Component();
						c.addPixel(x, y);
						components.put(currentLabel, c);

						currentLabel++;
						count++;
					}
				}

				// Store background
				else if (mode == 1) {
					background.addPixel(x, y);
				}
			}
		}

		// Second pass
		for (int x = 0; x < wid; x++) {
			for (int y = 0; y < hei; y++) {
				int labelOfPixel = labels[y * wid + x];
				if (labelOfPixel != 0) {
					int rootLabel = findRootR(labelOfPixel, 1)[0];
					labels[y * wid + x] = rootLabel;
					equi[labelOfPixel] = rootLabel;
					
					//Complete the components field
					if(rootLabel != labelOfPixel) {
						Component c = components.get(rootLabel);
						c.merge(components.get(labelOfPixel));
						components.remove(labelOfPixel);
					}
				}
			}
		}
	}

//	/**
//	 * Complete the components field using the equivalence table
//	 */
//	private void completeDB() {
//		for (int i = 0; i < equi.length; i++) {
//			int root = equi[i];
//			if (root != 0 && root != i) {
//				Component c = components.get(root);
//				c.merge(components.get(i));
//				components.remove(i);
//			}
//		}
//	}

	/**
	 * Getter for components
	 * 
	 * @return components map associated with a label
	 */
	public HashMap<Integer, Component> getComponentsDB() {
		return components;
	}

	/**
	 * Get a set of all components
	 * 
	 * @return set of all components
	 * @throws Exception
	 *             if setup() failed
	 */
	public HashSet<Component> getSetComponents() throws Exception {
		if (!readyToUse)
			setup();
		return new HashSet<>(components.values());
	}

	/**
	 * Returns the number of components identified in the image.
	 * 
	 * @return the number of components (between 1 and N)
	 * @throws Exception
	 *             if setup() failed
	 */
	public int countComponents() throws Exception {
		if (!readyToUse)
			setup();
		return count;
	}

	/**
	 * Returns a picture with each object updated to a random colour.
	 * 
	 * @return a picture object with all components coloured.
	 * @throws Exception
	 *             if setup() failed
	 */
	public Picture colourComponentImage() throws Exception {
		if (!readyToUse)
			setup();
		Picture coloredPic = new Picture(pic);
		Random randomGenerator = new Random();
		Iterator<Component> i = components.values().iterator();
		while (i.hasNext()) {
			Color col = colors[randomGenerator.nextInt(colors.length)];

			Iterator<Point> pixels = i.next().getPixels().iterator();
			while (pixels.hasNext()) {
				Point p = pixels.next();
				coloredPic.set(p.x, p.y, col);
			}
		}

		//Make sure background is always black 
		if (mode == 1) {
			Iterator<Point> backgroundPixels = background.getPixels().iterator();
			while (backgroundPixels.hasNext()) {
				Point b = backgroundPixels.next();
				coloredPic.set(b.x, b.y, Color.BLACK);
			}
		}
		return coloredPic;

	}

	/**
	 * Returns the original picture
	 * 
	 * @return original picture
	 */
	public Picture getPicture() {
		return new Picture(picture);
	}

	/**
	 * Returns a binarised version of the original image
	 * 
	 * @return a black and white picture object
	 * @throws Exception
	 *             if setup() failed
	 */
	public Picture binaryComponentImage() throws Exception {
		if (!readyToUse)
			setup();
		return new Picture(pic);
	}

	/**
	 * Returns the binarized image with each object bounded by a red box.
	 * 
	 * @return a black and white picture object with all components surrounded
	 *         by a red box
	 * @throws Exception
	 *             Exception if setup() failed
	 */
	@Override
	public Picture highlightComponentImage() throws Exception {
		if (!readyToUse)
			setup();
		return boundingBox(new Picture(pic));
	}

	/**
	 * Returns the original image with each object bounded by a red box.
	 * 
	 * @return a picture object with all components surrounded by a red box
	 * @throws Exception
	 *             if setup() failed
	 */
	public Picture identifyComponentImage() throws Exception {
		if (!readyToUse)
			setup();
		return boundingBox(new Picture(picture));
	}

	/**
	 * Draw a red box around each component
	 * 
	 * @param hPic
	 *            the picture object to draw upon
	 * @return red boxes drawn picture
	 */
	private Picture boundingBox(Picture hPic) {
		Iterator<Component> i = components.values().iterator();
		while (i.hasNext()) {
			int[] limits = i.next().getLimits();

			int xMin = limits[0];
			int xMax = limits[1];
			int yMin = limits[2];
			int yMax = limits[3];
			
			if (!(xMin > xMax || yMin > yMax)) {
				for (int k = xMin; k <= xMax; k++) {
					hPic.set(k, yMax, Color.RED);
					hPic.set(k, yMin, Color.RED);
				}
				for (int k = yMin; k <= yMax; k++) {
					hPic.set(xMin, k, Color.RED);
					hPic.set(xMax, k, Color.RED);
				}
			}
		}
		return hPic;
	}

	/**
	 * Union two components
	 * 
	 * @param p
	 * @param q
	 */
	@Override
	public void union(int p, int q) {
		if (connected(p, q))
			return;

		int[] pRoot = findRootR(p, 1);
		int[] qRoot = findRootR(q, 1);

		if (pRoot[1] < qRoot[1]) {
			equi[pRoot[0]] = qRoot[0];
		} else {
			equi[qRoot[0]] = pRoot[0];
		}

		count--;
	}

	// @Override
	// public int[] findRoot(int p) {
	// int depth = 1;
	// int par = equi[p];
	// while (equi[par] != par) {
	// equi[par] = equi[equi[par]];
	// par = equi[par];
	// depth++;
	// }
	// return (new int[] { par, depth });
	// }

	/**
	 * Chase the root of the component
	 * 
	 * @param p
	 * @param depth
	 *            initially 1
	 * @return an array of size 2, 
	 * including the root of p and the depth
	 */
	@Override
	public int[] findRootR(int p, int depth) {
		if (equi[p] == p)
			return (new int[] { p, depth });
		else {
			equi[p] = equi[equi[p]];
			p = equi[p];
			return findRootR(p, ++depth);
		}
	}

	/**
	 * Check if two components are connected
	 * 
	 * @param p
	 * @param q
	 * @return boolean value if they are connected
	 */
	@Override
	public boolean connected(int p, int q) {
		return findRootR(p, 1)[0] == findRootR(q, 1)[0];
	}


	/**
	 * Get the monochrome luminance of given point on a picture
	 * 
	 * @param pic
	 *            the picture
	 * @param x
	 * @param y
	 * @return the monochrome luminance
	 */
	private Color lumi(Picture pic, int x, int y) {
		double val = Luminance.lum(pic.get(x, y));
		return val < 128 ? Color.black : Color.white;
	}
}
