package org.mitlware.problem.numeric;

import static org.junit.Assert.assertArrayEquals;
import jeep.math.ClosedInterval;

import org.junit.Test;

import statelet.bitvector.BitVector;

public class TestBinaryEncoder {

	@Test
	public void testGray() {
		
		final int numBits = 8;
		final double [] inputValues = { 1, 2 };
		ClosedInterval range = ClosedInterval.create( 0.0, 255.0 );
		final int numDimensions = 2;
		
		BinaryEncoder encoding = new BinaryEncoder.GrayEncoder();
		
		BitVector encoded = encoding.encode( inputValues, numBits, i -> range );
		double [] outputValues = encoding.decode( encoded, numDimensions, i -> range );
		assertArrayEquals( inputValues, outputValues, 0.0 );
	}

	@Test
	public void testLinear() {
		
		final int numBits = 8;
		final double [] inputValues = { 1, 2 };
		ClosedInterval range = ClosedInterval.create( 0.0, 255.0 );
		final int numDimensions = 2;
		
		BinaryEncoder encoding = new BinaryEncoder.LinearEncoder();
		
		BitVector encoded = encoding.encode( inputValues, numBits, i -> range );
		double [] outputValues = encoding.decode( encoded, numDimensions, i -> range );
		assertArrayEquals( inputValues, outputValues, 0.0 );
	}
	
	///////////////////////////////
	
	/********
	  @RunWith(Theories.class)
	    public class TestCoding {
	        @Theory public void decodeReversesEncode(
		        	@ForAll @InRange(min = "1", max = "20") int numDimensions,
	            @ForAll @InRange(min = "numDimensions", max = "128*numDimensions") int numBits,
	        	@ForAll @InRange(min = "1", max = "20") double lBound,
	        	@ForAll @InRange(min = "1", max = "20") double uBound,	        	
	            @ForAll double [] inputValues ) throws Exception {

	        	Diag.println();
	        	
	        	ClosedInterval range = ClosedInterval.create( lBound, uBound );
	    		BinaryEncoding encoding = new BinaryEncoding.LinearEncoding();
	        	BitVector encoded = encoding.encode( inputValues, numBits, i -> range );
	        	
	    		double [] outputValues = encoding.decode( encoded, numDimensions, i -> range );
	    		assertArrayEquals( inputValues, outputValues, 0.0 );
	        }
	    }	
********/	    
}

// End ///////////////////////////////////////////////////////////////
