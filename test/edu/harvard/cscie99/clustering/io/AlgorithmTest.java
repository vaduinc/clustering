package edu.harvard.cscie99.clustering.io;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.BitSet;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.harvard.cscie99.clustering.util.Utility;


public class AlgorithmTest {
    
	 public final static double EPSILON = 1E-4;
	private String FILENAME = "./testdata/iris.txt";
    
    public AlgorithmTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    
    /**
     * Test the distance from 2 bitsets.
     * It is called from cluster algorithm when 
     * fingerprint input is used. 
     */
    @Test
    public void testFingerprintDistance(){
    	
    	// create 2 bitsets
	      BitSet point1 = new BitSet();
	      BitSet point2 = new BitSet();
		
	   // assign values to bitset1
	      point1.set(2);
	      point1.set(4);
	      point1.set(5);
	      point1.set(10);
	      point1.set(12);
	      point1.set(19);
	      point1.set(26);

	      // assign values to bitset2
	      point2.set(2);
	      point2.set(4);
	      point2.set(9);
	      point2.set(12);
	      point2.set(13);
	      point2.set(20);
	      point2.set(25);
	      point2.set(26);
    	
	      int distance = Utility.distance(point1,point2);
    	
	      assertEquals(distance,7);
    }
    
    
    
    /**
     * Test the distance between 2 points.
     * It is called from cluster algorithm when 
     * Matrix input is used. 
     * @throws Exception 
     */
    @Test
    public void testMatrixDistance() throws Exception{
    	
    	// create 2 bitsets
	      double[] point1 = { 5.1 , 3.5 , 1.4 , 0.2 };
	      double[] point2 = { 4.9 , 3 , 1.4 , 0.2  };
		
	      double distance = Utility.distance(point1,point2);
    	
	      assertTrue(Math.abs(distance - 0.5385164807134502 ) < EPSILON);
	      
    }
    
    
    
    /**
     * Test of loadData method, of class MatrixReader.
     */
    @Test
    public void testLoadData() throws Exception {
        System.out.println("loadData");
        MatrixReader instance = new MatrixReader();
        try {
            instance.loadData(FILENAME);
        } catch(IOException ioe) {
            fail("Failed to load file " + FILENAME);
        }
        assertEquals(instance.getRowHeaders().size(),150);
        assertEquals(instance.getColumnHeaders().size(),4);
        assertEquals(instance.getRawMatrix().length, 150);
        assertEquals(instance.getRawMatrix()[0].length, 4);
    }

    /**
     * Test of clearData method, of class MatrixReader.
     */
    @Test
    public void testClearData() {
        System.out.println("clearData");
        MatrixReader instance = new MatrixReader();
        try {
            instance.loadData(FILENAME);
        } catch(IOException ioe) {
            System.err.println(ioe.getMessage());
            fail();
        }
        instance.clearData();
        assertEquals(instance.getRawMatrix().length,0);
    }

    /**
     * Test of determineDelimiter method, of class MatrixReader.
     */
    @Test
    public void testDetermineDelimiter() {
        System.out.println("determineDelimiter");
        ArrayList<String> lines = new ArrayList<String>();
        MatrixReader instance = new MatrixReader();
        lines.add("hello\ta\tb\tc\td");
        lines.add("row1\t5.1\t3.5\t1.4\t0.2");
        lines.add("row2\t4.9\t3\t1.4\t0.2");
        lines.add("row3\t4.7\t3.2\t1.3\t0.2");
        String expResult = "\\t";
        String result = instance.determineDelimiter(lines);
        assertEquals(expResult, result);
    }

    /**
     * Test of determineIfRowHeader method, of class MatrixReader.
     */
    @Test
    public void testDetermineIfRowHeader() {
        System.out.println("determineIfRowHeader");
        ArrayList<String> firstLines = new ArrayList<String>();
        firstLines.add("header\t0.1\t0.2");
        firstLines.add("header1\t0.1\t0.2");
        String delimiter = "\\t";
        MatrixReader instance = new MatrixReader();
        boolean expResult = true;
        boolean result = instance.determineIfRowHeader(firstLines, delimiter);
        assertEquals(expResult, result);
    }

    /**
     * Test of determineIfRowHeader method, of class MatrixReader.
     */
    @Test
    public void testDetermineIfRowHeaderWithOrderedy() {
        System.out.println("determineIfRowHeaderWithNumericOnly");
        ArrayList<String> firstLines = new ArrayList<String>();
        firstLines.add("1\t0.1\t0.2");
        firstLines.add("2\t0.3\t0.7");
        firstLines.add("3\t0.5\t0.6");
        String delimiter = "\\t";
        MatrixReader instance = new MatrixReader();
        boolean expResult = true;
        boolean result = instance.determineIfRowHeader(firstLines, delimiter);
        assertEquals(expResult, result);
    }

    /**
     * Test of parseFirstLineAsHeader method, of class MatrixReader.
     */
    @Test
    public void testParseFirstLineAsHeader() {
        System.out.println("parseFirstLineAsHeader");
        String[] tokens = {"species", "sepal_length", "sepal_width", "petal_length","petal_width"};
        boolean hasRowHeader = true;
        MatrixReader instance = new MatrixReader();
        boolean expResult = true;
        boolean result = instance.parseFirstLineAsHeader(tokens, hasRowHeader);
        assertEquals(expResult, result);
    }

    /**
     * Test of parseFirstLineAsHeader method, of class MatrixReader.
     */
    @Test
    public void testParseFirstLineAsHeaderWithNumericValues() {
        System.out.println("parseFirstLineAsHeaderWithNumericValues");
        String[] tokens = {"species", "0.1", "0.3", "0.5","190"};
        boolean hasRowHeader = true;
        MatrixReader instance = new MatrixReader();
        boolean expResult = false;
        boolean result = instance.parseFirstLineAsHeader(tokens, hasRowHeader);
        assertEquals(expResult, result);
    }

    /**
     * Test of parseFirstLineAsHeader method, of class MatrixReader.
     */
    @Test
    public void testParseFirstLineAsHeaderWithOrderedInt() {
        System.out.println("parseFirstLineAsHeaderWithOrderedInt");
        String[] tokens = {"species", "1","2","3","4"};
        boolean hasRowHeader = true;
        MatrixReader instance = new MatrixReader();
        boolean expResult = true;
        boolean result = instance.parseFirstLineAsHeader(tokens, hasRowHeader);
        assertEquals(expResult, result);
    }

    /**
     * Test of parseLine method, of class MatrixReader.
     */
    @Test
    public void testParseLine() {
        System.out.println("parseLine");
        String delimiter = "\\t";
        boolean hasRowHeader = true;
        MatrixReader instance = new MatrixReader();
        instance.clearData();
        String line = "setosa1\t5.1\t3.5\t1.4\t0.2";
        instance.parseLine(line, delimiter, hasRowHeader);
        assertEquals(instance.getRowHeaders().get(0), "setosa1");

    }

    /**
     * Test of getColumnHeaders method, of class MatrixReader.
     */
    @Test
    public void testGetColumnHeaders() {
        System.out.println("getColumnHeaders");
        MatrixReader instance = new MatrixReader();
        try {
            instance.loadData(FILENAME);
        } catch(IOException ioe) {
            fail("Could not load " + FILENAME);
        }
        int expResult = 4;
        int result = instance.getColumnHeaders().size();
        assertEquals(expResult, result);
    }

    /**
     * Test of getRowHeaders method, of class MatrixReader.
     */
    @Test
    public void testGetRowHeaders() {
        System.out.println("getRowHeaders");
        MatrixReader instance = new MatrixReader();
        try {
            instance.loadData(FILENAME);
        } catch(IOException ioe) {
            fail("Could not load " + FILENAME);
        }
        int expResult = 150;
        int result = instance.getRowHeaders().size();
        assertEquals(expResult, result);
    }

    /**
     * Test of getRawMatrix method, of class MatrixReader.
     */
    @Test
    public void testGetRawMatrixPoint() {
        System.out.println("getRawMatrix");
        MatrixReader instance = new MatrixReader();
        try {
            instance.loadData(FILENAME);
        } catch(IOException ioe) {
            fail("Could not load " + FILENAME);
        }
        double expResult = 0.4;
        double result = instance.getRawMatrix()[5][3];
        assertEquals(expResult, result, 1E-5);
    }

    /**
     * Test of getNormalizedMatrix method, of class MatrixReader.
     */
    @Test
    public void testGetNormalizedMatrix() {
        System.out.println("getNormalizedMatrix");
        MatrixReader instance = new MatrixReader();
        try {
            instance.loadData(FILENAME);
        } catch(IOException ioe) {
            System.out.println("IOError : " + ioe.getMessage());
            fail("Could not load " + FILENAME);
        }
        double expResult = 0.788031;
        double[][] result = instance.getNormalizedMatrix();
        assertEquals(150, result.length);
        assertEquals(4, result[0].length);
        assertEquals(expResult, result[149][3], 1E-6);
    }
    
}
