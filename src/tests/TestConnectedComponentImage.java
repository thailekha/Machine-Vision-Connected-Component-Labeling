package tests;

import static org.junit.Assert.*;
import java.awt.Color;
import java.awt.Point;
import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.TimeoutException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import edu.princeton.cs.introcs.Picture;
import imageprocessing.ConnectedComponentImage;

public class TestConnectedComponentImage {

	String pre;
	String[] names;

	@Before
	public void setUp() throws Exception {
		pre = "images/testImages/";
		names = new String[] { "onePixelInMiddle", "twoPixelsInMiddle", "twoConsecutivePixelsInMiddle",
				"manyConsecutivePixelsInMiddle", "onePixelsatPerCornerAndEdge", "rectangleOfOnePixelThickSurrounding",
				"oneDiagonalLine", "twoDiagonalLines", "twoCurvesOf3pxThick", "twoCollideCurvesOf3pxThick", "blank",
				"allFilled" };
	}

	@After
	public void tearDown() throws Exception {
		pre = null;
		names = null;
	}

	/**
	 * Type: C.O.R.R.E.C.T => Conformance 
	 * Test if ConnectedComponentImage construct a picture of type Picture
	 */
	@Test
	public void testCreatePicture() {
		try {
			Picture pic = new Picture("images/testImages/bitmap.bmp");
			ConnectedComponentImage processor = new ConnectedComponentImage("images/testImages/bitmap.bmp", 0);
			assertEquals(processor.getPicture(), pic);
			assertEquals(processor.getHeight(), 103);
			assertEquals(processor.getWidth(), 100);

			Picture pic2 = new Picture("images/testImages/bitmap.bmp");
			ConnectedComponentImage processor2 = new ConnectedComponentImage("images/testImages/bitmap.bmp", 0);
			assertEquals(processor2.getPicture(), pic2);
			assertEquals(processor2.getHeight(), 103);
			assertEquals(processor2.getWidth(), 100);

			assertEquals(processor.getPicture(), processor2.getPicture());
		} catch (Exception e) {
			//e.printStackTrace();
			fail(e.getMessage());
		}
	}

	/**
	 * Type: C.O.R.R.E.C.T => Existance / RIGHT B.I.C.E.P => B 
	 * Test if expected allowed-picture-file-extensions can be created
	 */
	@Test // File type boundary
	public void testCreatePictureAllowedTypes() {
		try {
			String[] types = new String[] { "gif.gif", "jpeg.jpg", "bitmap.bmp", "png.png", "16ColorBitMap.bmp",
					"monochromeBitmap.bmp" };
			for (int i = 0; i < types.length; i++) {
				Picture p = new Picture("images/testImages/" + types[i]);
				ConnectedComponentImage c = new ConnectedComponentImage("images/testImages/" + types[i], 0);
				assertEquals(p, c.getPicture());
			}
		} catch (Throwable e) {
			//e.printStackTrace();
			fail(e.getMessage());
		}
	}

	/**
	 * RIGHT B.I.C.E.P => B, E 
	 * Test if expected allowed-picture-file-extensions can be created, if not an exception
	 * should be thrown
	 */
	@Test // File type boundary
	public void testCreatePictureUnallowedTypesAndLocation() {
		Result result = JUnitCore.runClasses(TestCreatePictureUnallowed.class);
		assertEquals(result.getFailures().size(), 0);
	}

	/**
	 * RIGHT B.I.C.E.P => B, E 
	 * Test if an expected allowed-picture-file-extension can be created but it is a broken image,
	 * an exception should be thrown
	 */
	@Test
	public void testCreateBrokenPicture() {
		try {
			ConnectedComponentImage c = new ConnectedComponentImage("nullImage.jpg", 0);
			fail("Should have thrown an exception");
		} catch (Exception e) {
			assertTrue(true);
		}
	}

	/**
	 * RIGHT B.I.C.E.P => right
	 * Test binarizing method
	 */
	@Test
	public void testBinarizedPicture() {
		try {
			ConnectedComponentImage[] cs = new ConnectedComponentImage[]{
					new ConnectedComponentImage("images/shapes.bmp",0),
					new ConnectedComponentImage("images/shapes.bmp",1)};
			for(int i = 0; i < cs.length; i++) {
				Picture binarized = cs[i].binaryComponentImage();
				for(int x = 0; x < binarized.width(); x++) {
					for(int y = 0; y < binarized.height(); y++) {
						Color color = binarized.get(x, y);
						assertTrue(color.equals(Color.black) || color.equals(Color.white));
					}
				}
			}			
		} catch (Exception e) {
			//e.printStackTrace();
			fail(e.getMessage());
		}
	}
	
	/**
	 * RIGHT B.I.C.E.P => Right 
	 * Test if the program scans and gives the expected component amount
	 */
	@Test
	public void testDarkComponentCount() {
		int[] results = new int[] { 1, 2, 1, 1, 8, 1, 200, 397, 2, 1, 0, 1 };

		for (int i = 0; i < names.length; i++) {
			try {
				ConnectedComponentImage c = new ConnectedComponentImage(pre + names[i] + ".bmp", 1);
				assertEquals("Error with case " + names[i], c.countComponents(), results[i]);
				assertEquals("Error with case " + names[i], c.getComponentsDB().keySet().size(), results[i]);
			} catch (Exception e) {
				e.printStackTrace();
				fail("Exception thrown with case name " + names[i]);
			}
		}
	}

	/**
	 * RIGHT B.I.C.E.P => Right 
	 * Test if the program scans and gives the expected component amount
	 */
	@Test
	public void testBrightComponentCount() {
		int[] results = new int[] { 1, 1, 1, 1, 1, 1, 2, 4, 1, 1, 1, 0 };

		for (int i = 0; i < names.length; i++) {
			try {
				ConnectedComponentImage c = new ConnectedComponentImage(pre + names[i] + ".bmp", 0);
				assertEquals("Error with case " + names[i], c.countComponents(), results[i]);
				assertEquals("Error with case " + names[i], c.getComponentsDB().keySet().size(), results[i]);
			} catch (Exception e) {
				e.printStackTrace();
				fail("Exception thrown with case name " + names[i]);
			}
		}
	}

	/**
	 * Right B.I.C.E.P: Right
	 * Test if the coordinate of a component is located
	 */
	@Test
	public void testLocateComponent() {
		try {
			ConnectedComponentImage c = new ConnectedComponentImage(pre + "onePixelInMiddle.bmp",1);
			assertEquals(1,c.countComponents());
			Point located = c.getSetComponents().iterator().next().getPixels().iterator().next();
			assertEquals(located.x,82);
			assertEquals(located.y,80);
			assertEquals(located,new Point(82,80));
		} catch (Exception e) {
			fail(e.getMessage());
			//e.printStackTrace();
		}
	}
	
	/**
	 * Right B.I.C.E.P: Right
	 * Test the red box drawn at the identified component
	 */
	@Test
	public void testBoundingBox() {
		String[] toBound = new String[]{"twoPixelsInMiddle","twoConsecutivePixelsInMiddle"}; 
		ArrayList<int[]> results = new ArrayList<>();
		results.add(new int[]{69,54});
		results.add(new int[]{85,57});
		results.add(new int[]{81,47});
		results.add(new int[]{82,47});
		
		int count = 0;
		for(int i = 0; i < toBound.length; i++) {
			try {
				System.out.println();
				ConnectedComponentImage c = new ConnectedComponentImage(pre + toBound[i] + ".bmp",1);
				Picture drawn = c.identifyComponentImage();
				int[] a = results.get(count++);
				int[] b = results.get(count++);
				assertEquals(drawn.get(a[0], a[1]),Color.red);
				assertEquals(drawn.get(b[0], b[1]),Color.red);
			} catch (Exception e) {
				fail(e.getMessage());
				e.printStackTrace();
			}
		}	
	}
	
	/**
	 * Right B.I.C.E.P: Right
	 * Test that red box is not drawn with a blank picture 
	 * (no component identified)
	 */
	@Test
	public void testBoundingBoxBlank() {
		try {
			ConnectedComponentImage c = new ConnectedComponentImage(pre + "blank" + ".bmp",1);
			assertEquals(c.countComponents(),0);
			
			Picture drawn = c.identifyComponentImage();
			for(int x = 0; x < drawn.width(); x++) {
				for(int y = 0; y < drawn.height(); y++) {
					Color color = drawn.get(x, y);
					assertTrue(! color.equals(Color.red));
				}
			}
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
	
	/**
	 * RIGHT B.I.C.E.P: Right
	 * Binarizing a black and white picture results in the same as the original
	 */
	@Test 
	public void testBinarizeBlackAndWhitePicture() {
		for (int i = 0; i < names.length; i++) {
			try {
				ConnectedComponentImage a = new ConnectedComponentImage(pre + names[i] + ".bmp", 1);
				assertEquals(a.getPicture(),a.binaryComponentImage());
			} catch (Exception e) {
				e.printStackTrace();
				fail("Exception thrown with case name " + names[i]);
			}
		}
	}
	
	/**
	 * Type: C.O.R.R.E.C.T => Time
	 * RIGHT B.I.C.E.P => P
	 * Test if the program shutdown if processing time takes too long
	 * (this case the cause is the picture's size)
	 */
	@Test
	public void testProcessingTimeSize() {
		try {
			ConnectedComponentImage c = new ConnectedComponentImage("images/testImages/heavyPicSize.bmp", 0);
			c.setup();
			fail("Should have thrown an exception");
		} catch (Exception e) {
			assertTrue("Not Time out exception", e instanceof TimeoutException);
		}
	}
}
