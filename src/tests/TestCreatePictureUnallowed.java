package tests;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import imageprocessing.ConnectedComponentImage;

/**
 * RIGHT B.I.C.E.P => B, E 
 * Test expected unallowed-picture-file-extensions and
 * buggy file locations, exception should be thrown in each case
 * 
 * @author Thai Kha Le
 */
@RunWith(Parameterized.class)
public class TestCreatePictureUnallowed {

	private String fileLocation;

	public TestCreatePictureUnallowed(String fileLocation) {
		// System.out.println("Setup");
		this.fileLocation = fileLocation;
	}

	@Parameterized.Parameters
	public static Collection getPictures() {
		return Arrays.asList(new String[] { "images/testImages/picDoc.doc", "images/testImages/picNote.ppt",
				"images/testImages/picZip.zip", "images/testImages/tiff.tif", "images/testImages/picNoExtension",
				"",	" ", "p", "images\testImages\bitmap.bmp", "images/testImages\bitmap.bmp", "images/testImages/bitmap",
				"/images/testImages/bitmap.bmp", "@@@@@@@@@@", "2462564332546", "!@$#$%$%^%&!@$",
				"aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" });
	}

	@Test
	public void doTest() {
		try {
			ConnectedComponentImage c = new ConnectedComponentImage(fileLocation, 0);
			fail("Should have thrown an exception");
		} catch (Exception e) {
			//System.out.println(fileLocation + " ok");
			assertTrue(true);
		}
	}
}
