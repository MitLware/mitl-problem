package org.mitlware.problem.numeric;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.function.Function;

import statelet.bitvector.BitVector;
import jeep.math.ClosedInterval;
import jeep.math.LinearInterpolation;

//////////////////////////////////////////////////////////////////////

public abstract class BinaryEncoder {
	
	public abstract BitVector encode( double value, int numBits, ClosedInterval range );
	public abstract double decode( BitVector encoded, int numBits, ClosedInterval range );
	
	///////////////////////////////
	
	public BitVector encode( double [] values, int numBitsPerDimension, Function< Integer, ClosedInterval > bounds ) {

		// FIXME: this assumes equal number of bits for each dimension
		
		BigInteger result = encode( values[ 0 ], numBitsPerDimension, bounds.apply( 0 ) ).toBigInteger();
		for( int i=1; i<values.length; ++i ) {
			result = result.shiftLeft( numBitsPerDimension );
			result = result.or( encode( values[ i ], numBitsPerDimension, bounds.apply( i ) ).toBigInteger() );
		}

		return new BitVector( values.length * numBitsPerDimension, result );
	}
	
	public double [] decode( BitVector encoded, int numDimensions, Function< Integer, ClosedInterval > bounds ) {
		
		// FIXME: this assumes equal number of bits for each dimension
		
		if( encoded.length() % numDimensions != 0 )
			throw new IllegalArgumentException();

		final int numBitsPerDimension = encoded.length() / numDimensions;
		
		double [] result = new double [ numDimensions ];
		final BigInteger mask = BigInteger.ONE.shiftLeft( 
			numBitsPerDimension ).subtract( BigInteger.ONE );
		
		BigInteger x = encoded.toBigInteger();
		for( int i=0; i<result.length; ++i ) {
			BigInteger lowOrderBits = x.and( mask );
			result[ result.length - i - 1 ] = decode( new BitVector( numBitsPerDimension, lowOrderBits ), 
				numBitsPerDimension, bounds.apply( i ) );
			x = x.shiftRight( numBitsPerDimension );
		}
		
		assert( result.length == numDimensions );
		return result;
	}
	
	///////////////////////////////
	
	public static final class LinearEncoder extends BinaryEncoder {

		@Override
		public BitVector encode(double value, int numBits, ClosedInterval range) {
			
			final long discreteValue = (long)LinearInterpolation.apply( value, 
				range.getLower(), range.getUpper(), 0, ( 1L << numBits ) - 1L );
			
			BitVector result = new BitVector(numBits);
			for( int j=0; j<numBits; ++j )
				if( ( discreteValue & ( 1L << j ) ) != 0 )
					result.set( j );
			
			return result;
		}

		@Override
		public double decode(BitVector encoded, int numBits, ClosedInterval range) {
			long longValue = 0;
			for( int j=0; j<numBits; ++j )
				if( encoded.get( j ) )
					longValue |= 1 << j;

			return LinearInterpolation.apply( longValue, 
				0, ( 1L << numBits ) - 1, range.getLower(), range.getUpper() );				
		}
	}
	
	///////////////////////////////	
	
	public static class GrayEncoder extends BinaryEncoder {

		@Override
		public BitVector encode(double value, int numBits, ClosedInterval range) {
			if( numBits % 8 != 0 )
				throw new IllegalArgumentException();			
			if( !range.contains( value ) )
				throw new IllegalArgumentException();
			
			final double maxOutputValue = Math.pow( 2.0, numBits ) - 1; 
			BigInteger result = new BigInteger( new byte [ numBits / 8 ] );		
			long discreteValue = (long)LinearInterpolation.apply( value, 
					range.getLower(), range.getUpper(), 0, maxOutputValue );

			int bitIndex = 0;		
			for( int j=0; j<numBits; ++j, ++bitIndex )
				if( ( discreteValue & ( 1L << j ) ) != 0 )
					result = result.setBit( bitIndex );
				else
					result = result.clearBit( bitIndex );					

			return new BitVector( numBits, binaryToGrey( result ) );
		}

		public static int binaryToGrey( int n ) { return n ^ ( n >> 1 ); }
		private static BigInteger binaryToGrey( BigInteger n ) { return n.xor(n.shiftRight(1));	}

		///////////////////////////
		
		@Override
		public double decode(BitVector encoded, int numBits, ClosedInterval range) {
			final double maxInputValue = Math.pow( 2.0, numBits ) - 1;
			
			BigInteger bi = greyToBinary( encoded.toBigInteger() );
			double dValue = bi.doubleValue();
			return LinearInterpolation.apply( dValue, 0, maxInputValue, range.getLower(), range.getUpper() );
		}

		private static int greyToBinary( int n ) {
			for( int i= (n >> 1); i != 0 ; n ^= i, i >>= 1 )
				;
			
			return n;
		}
		
		private static BigInteger greyToBinary( BigInteger n ) {
			for( BigInteger i= n.shiftRight(1); i.compareTo(BigInteger.ZERO) != 0; n = n.xor(i), i=i.shiftRight(1) )
				;
			
			return n;
		}
	}
}

// End ///////////////////////////////////////////////////////////////

