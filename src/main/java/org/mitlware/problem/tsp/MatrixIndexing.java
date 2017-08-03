package org.mitlware.problem.tsp;

import java.util.ArrayList;
import java.util.List;

import org.mitlware.support.math.DoubleMatrix;
import org.mitlware.support.math.IntMatrix;

import org.apache.commons.lang3.tuple.Pair;


public final class MatrixIndexing {

	public static final int ithTriangularNumber( int i ) { return i * ( i + 1 ) / 2; } 

	/**
	 * From http://calculus-geometry.hubpages.com/hub/How-to-Tell-If-a-Number-is-Triangular
	 * Triangular number index 
	 * @param x
	 */
	public static final double R( int x ) {
		return 0.5 * Math.sqrt(8*x + 1) - 0.5;
	}

	///////////////////////////////
	
	// http://stackoverflow.com/questions/242711/algorithm-for-index-numbers-of-triangular-matrix-coefficients
	public static Pair< Integer, Integer > upperTriangularRowMajorIndex( int i, int dim ) {
		final int triNumberOfDim = ithTriangularNumber( dim );
		final int ii = triNumberOfDim - i;
		final int K = (int)Math.floor( ( Math.sqrt(8.0 *ii) - 1.0 ) / 2.0 );
	    final int rowIndex = dim - K - 1;
	    final int columnIndex = i - triNumberOfDim + ithTriangularNumber( K + 1 ); // (K+1)*(K+2)/2;
		return Pair.of( rowIndex, rowIndex + columnIndex );
	}

	public static Pair< Integer, Integer > lowerTriangularRowMajorIndex( int i, int dim ) {
		Pair< Integer, Integer > upperIndex = upperTriangularRowMajorIndex( i, dim );
		return Pair.of( upperIndex.getRight(), upperIndex.getLeft() );
	}
	
	public static List< Pair< Integer, Integer > >
	upperTriangularNonzeroIndicesRowMajor( int dim ) {
		final int nnz = ( dim * ( dim + 1 ) ) / 2;
		
		List< Pair< Integer, Integer > > result = new ArrayList< Pair< Integer, Integer > >(); 
		for( int i=0; i<nnz; ++i )
			result.add( upperTriangularRowMajorIndex( i, dim ) );
		
		return result;
	}

	public static List< Pair< Integer, Integer > >
	lowerTriangularNonzeroIndicesRowMajor( int dim ) {
		final int nnz = ( dim * ( dim + 1 ) ) / 2;
		
		List< Pair< Integer, Integer > > result = new ArrayList< Pair< Integer, Integer > >(); 
		for( int i=0; i<nnz; ++i )
			result.add( lowerTriangularRowMajorIndex( i, dim ) );
		
		return result;
	}
	
	///////////////////////////////
	
	private static void fill( DoubleMatrix m, double value ) {
		for( int i=0; i<m.getNumRows(); ++i )
			for( int j=0; j<m.getNumColumns(); ++j )
				m.set( i, j, value );
	}
	
/*	public static void main( String [] args ) {
		
		final int dim = 4;
		
		DoubleMatrix upper = new DoubleMatrix( dim, dim );
		fill( upper, Double.NaN );
		
		List< Pair< Integer, Integer > > coordsU = upperTriangularNonzeroIndicesRowMajor( dim );
		for( int i=0; i<coordsU.size(); ++i ) {
			Pair< Integer, Integer > p = coordsU.get( i );
			upper.set( p.getLeft(), p.getRight(), i );
		}
		
		System.out.println( "upper: " + upper );

		DoubleMatrix lower = new DoubleMatrix( dim, dim );
		fill( lower, Double.NaN );
		
		List< Pair< Integer, Integer > > coordsL = lowerTriangularNonzeroIndicesRowMajor( dim );
		for( int i=0; i<coordsU.size(); ++i ) {
			Pair< Integer, Integer > p = coordsL.get( i );
			lower.set( p.getLeft(), p.getRight(), i );
		}
		
		System.out.println( "lower: " + lower );
	}
*/
}

// End ///////////////////////////////////////////////////////////////
