package tests;

import static org.junit.Assert.*;

import java.awt.Point;
import java.util.HashSet;
import java.util.Iterator;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import imageprocessing.Component;

public class TestComponent {

	Component c,d;
	
	@Before
	public void setUp() throws Exception {
		c = new Component();
		d = new Component();
	}

	@After
	public void tearDown() throws Exception {
		c = null;
		d= null;
	}

	/**
	 * C.O.R.R.E.C.T => conformance, existence
	 * RIGHT B.I.C.E.P => E
	 */
	@Test
	public void testCreateComponentWithNoPixel() {
		assertNotEquals(c,null);
		assertEquals(c.getPixels(),(new HashSet<Point>()));
		assertEquals(c.getPixels().size(),0);
		
		try{
			c.getLimits();
			fail("Should have thrown Exception");
		} catch(IndexOutOfBoundsException e) {
			assertTrue(true);
		}
	}
	
	/**
	 * C.O.R.R.E.C.T => existence, cardinality, ordering
	 * RIGHT B.I.C.E.P => Right
	 * Test if the pixel exists, getLimits works 
	 * as exepected and gives results in the right order
	 */
	@Test
	public void testCreateComponentWithOnePixel() {
		c.addPixel(1, 2);
		assertEquals(c.getPixels().size(),1);
		Iterator<Point> iter = c.getPixels().iterator();
		Point added = iter.next();
		assertEquals(added,new Point(1,2));
		try{
			int[] limits = c.getLimits();
			assertEquals("xMin",limits[0],1);
			assertEquals("xMax",limits[1],1);
			assertEquals("yMin",limits[2],2);
			assertEquals("yMax",limits[3],2);
		} catch(IndexOutOfBoundsException e) {
			fail("IndexOutOfBoundsException thrown");
		}
	}
	
	/**
	 * C.O.R.R.E.C.T => cardinality, ordering
	 * RIGHT B.I.C.E.P => Right
	 * Test if the amount of actually added pixels is as expected,
	 * getLimits works as exepected and gives results in the right order
	 */
	@Test
	public void testCreateComponentWithDuplicatePixels() {
		c.addPixel(1, 1);
		c.addPixel(1, 1);
		c.addPixel(-50, -50);
		c.addPixel(-50, -50);
		c.addPixel(100, 100);
		c.addPixel(100, 100);
		c.addPixel(0, 0);
		c.addPixel(0, 0);
		assertEquals(c.getPixels().size(),3);
		
		try{
			int[] limits = c.getLimits();
			assertEquals("xMin",limits[0],0);
			assertEquals("xMax",limits[1],100);
			assertEquals("yMin",limits[2],0);
			assertEquals("yMax",limits[3],100);			
		} catch(IndexOutOfBoundsException e) {
			fail("IndexOutOfBoundsException thrown");
		}
	}

	/**
	 * C.O.R.R.E.C.T => cardinality, ordering
	 * RIGHT B.I.C.E.P => Right
	 * Test if the amount of added pixels is as expected,
	 * getLimits works as exepected and gives results in the right order
	 */
	@Test
	public void testCreateComponentWithManyPixels() {		
		c.addPixel(1, 0);
		c.addPixel(-1, 50);
		c.addPixel(-99, 98);
		c.addPixel(100, 160);
		c.addPixel(10, 20);
		c.addPixel(30, 40);
		
		assertEquals(c.getPixels().size(),4);
		
		try{
			int[] limits = c.getLimits();
			assertEquals("xMin",limits[0],1);
			assertEquals("xMax",limits[1],100);
			assertEquals("yMin",limits[2],0);
			assertEquals("yMax",limits[3],160);
		} catch(IndexOutOfBoundsException e) {
			fail("IndexOutOfBoundsException thrown");
		}
	}
	
	/**
	 * C.O.R.R.E.C.T => cardinality
	 * RIGHT B.I.C.E.P => Right
	 *Test if merge works as expected
	 */
	@Test
	public void testMergeThisEmpty() {
		d.addPixel(2, 3);
		c.merge(d);
		assertEquals(c.getPixels().size(),1);
	}
	
	/**
	 * C.O.R.R.E.C.T => cardinality
	 * RIGHT B.I.C.E.P => Right
	 *Test if merge works as expected
	 */
	@Test
	public void testMergeThatEmpty() {
		c.addPixel(2, 3);
		c.addPixel(4, 5);
		c.merge(d);
		assertEquals(c.getPixels().size(),2);
	}
	
	/**
	 * C.O.R.R.E.C.T => cardinality
	 * RIGHT B.I.C.E.P => Right, B
	 *Test if merge works as expected
	 */
	@Test
	public void testMergeThatNull() {
		c.addPixel(2, 3);
		c.addPixel(4, 5);
		c.merge(null);
		assertEquals(c.getPixels().size(),2);
	}
	
	/**
	 * C.O.R.R.E.C.T => cardinality
	 * RIGHT B.I.C.E.P => Right
	 *Test if merge works as expected
	 */
	@Test
	public void testMergeTwoEmpty() {
		c.merge(d);
		assertEquals(c.getPixels().size(),0);
	}
	
	/**
	 * C.O.R.R.E.C.T => cardinality
	 * RIGHT B.I.C.E.P => Right
	 *Test if merge works as expected
	 */
	@Test
	public void testMerge() {
		c.addPixel(2, 3);
		c.addPixel(4, 5);
		d.addPixel(8, 1);
		d.addPixel(6, 7);
		c.merge(d);
		assertEquals(c.getPixels().size(),4);
	}
	
	/**
	 * C.O.R.R.E.C.T => cardinality
	 * RIGHT B.I.C.E.P => Right
	 *Test if merge works as expected
	 */
	@Test
	public void testMergeDupliccate() {
		c.addPixel(2, 3);
		c.addPixel(4, 5);
		c.addPixel(8, 1);
		c.addPixel(6, 7);
		
		d.addPixel(3, 2);
		d.addPixel(4, 5);
		d.addPixel(8, 1);
		d.addPixel(6, 7);
		d.addPixel(100, 200);
		
		c.merge(d);
		assertEquals(c.getPixels().size(),6);
	}
}
