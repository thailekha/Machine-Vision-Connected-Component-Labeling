package imageprocessing;

import java.util.concurrent.TimeoutException;

import edu.princeton.cs.introcs.Picture;

public interface ComponentImage {

	/**
	 * Run the scanning algorithm and complete the data-structure that stores
	 * the components within a time interval. If an TimeoutException is thrown,
	 * which indicates that executing time has exceeded the given allowed time,
	 * the whole program will be shutdown (this is handled by whatever "driver"
	 * is using an instance of the processor class).
	 *
	 * @throws Exception
	 *             including TimeoutException and other Exception types
	 */
	void setup() throws Exception;

	/**
	 * Returns the number of components identified in the image.
	 * 
	 * @return the number of components (between 1 and N)
	 * @throws Exception
	 *             if setup phase failed
	 */
	int countComponents() throws Exception;

	/**
	 * Returns a binarised version of the original image
	 * 
	 * @return a black and white picture object
	 * @throws Exception
	 *             if setup phase failed
	 */
	Picture binaryComponentImage() throws Exception;

	/**
	 * Returns a picture with each object updated to a random colour.
	 * 
	 * @return a picture object with all components coloured.
	 * @throws Exception
	 *             if setup phase failed
	 */
	Picture colourComponentImage() throws Exception;

	/**
	 * Returns the binarized image with each object bounded by a red box.
	 * 
	 * @return a black and white picture object with all components surrounded
	 *         by a red box
	 * @throws Exception
	 *             Exception if setup phase failed
	 */
	Picture highlightComponentImage() throws Exception;

	/**
	 * Returns the original image with each object bounded by a red box.
	 * 
	 * @return a picture object with all components surrounded by a red box
	 * @throws Exception
	 *             if setup phase failed
	 */
	Picture identifyComponentImage() throws Exception;
}
