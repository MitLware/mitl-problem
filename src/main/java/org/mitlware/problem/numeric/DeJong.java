package org.mitlware.problem.numeric;

import java.util.Arrays;
import java.util.Random;
import java.util.function.Function;

import jeep.math.ClosedInterval;
import metaxa.FunctionPoint;

import org.mitlware.mutable.Evaluate;

//////////////////////////////////////////////////////////////////////

/**
 * DeJong's function suite.
 * https://cs.gmu.edu/~eclab/kdj_thesis/appendices.pdf
 */

public final class DeJong {

	public static final RealVectorProblem F1 = new Sphere( 3, ClosedInterval.create( -5.12, 5.12 ) );
	public static final RealVectorProblem F2 = new Rosenbrock( 2, ClosedInterval.create( -2.048, 2.048 ) );
	public static final RealVectorProblem F3 = new Step( 5, ClosedInterval.create( -5.12, 5.12 ), false );
	public static final Function< Random, RealVectorProblem > F4 = ( Random random ) -> new QuarticWithNoise( 30, 
			ClosedInterval.create( -1.28, 1.28 ), random );
	public static final RealVectorProblem F5 = new Shekel( 2, ClosedInterval.create( -65.536, 65.536 ), 500, 25, 
			filledArray( 25, ( Integer i ) -> i + 1.0 ), 
			new double [][] { 
				filledArray( 25, a1DeJongShekel() ), 
				filledArray( 25, a2DeJongShekel() ) } );
	
	public static RealVectorProblem [] problems( Random random ) {
		return new RealVectorProblem [] { F1, F2, F3, F4.apply(random), F5 };
	}
	
	///////////////////////////////	

	public static double sphereFn( double [] x ) {
		double result = 0.0;
		for( int i=0; i<x.length; ++i )
			result += x[ i ] * x[ i ];
		
		return result;
	}

	public static double RosenbrockFn( double [] x ) {
		double result = 0;
		for( int i=0; i<x.length - 1; ++i )
			result += 100 * sqr( x[i+1] - sqr( x[i] ) ) + sqr( x[i] - 1 ); 				
		
		return result;
	}

	/**
	 * http://www2.denizyuret.com/pub/aitr1569/node19.html gives an offset of 6 * dim,
	 * but the appendix of DeJong does not.
	 */
	
	public static double stepFn( double [] x, boolean offset ) {
		double result = offset ? 6 * x.length : 0.0;
		for( int i=0; i<x.length; ++i )
			result +=  Math.floor( x[ i ] ); 				
		
		return result;
	}

	public static double quarticWithNoiseFn( double [] x, Random random ) {
		double result = 0.0;
		for( int i=0; i<x.length; ++i )
			result += i * Math.pow( x[ i ], 4.0 ) + random.nextGaussian(); 				
		
		return result;
	}

	public static double ShekelFn( double [] x, int K, int numMinima, double [] c, double [][] a ) {
		if( c.length != numMinima )
			throw new IllegalArgumentException( "Expected array of length " + numMinima + ", found " + c.length );
			
		final int dim = x.length;
		
		double sum  = 0.0;
		for( int j=0; j<numMinima; ++j ) {
			double fjx = c[ j ];
			for( int i=0; i<dim; ++i )
				fjx += Math.pow( x[ i ] - a[i][j], 6 );
			
			sum += 1.0 / fjx;
		}
		
		return 1.0 / ( ( 1.0 / K ) + sum );
	}
	
	///////////////////////////////

	public static final class Sphere extends RealVectorProblem.ExplicitImpl {

		public Sphere( int numDimensions, ClosedInterval bounds ) {
			super( Evaluate.Directional.minimizing( ( double [] input ) -> { 
						if( input.length != numDimensions )
							throw new IllegalArgumentException();
						return sphereFn(input); }),					
				numDimensions,
				RealVectorProblem.constantBounds( numDimensions, bounds ),				
				FunctionPoint.create( new double [ numDimensions ], 0.0 ) );
		}
	}

	///////////////////////////////
	
	public static final class Rosenbrock extends RealVectorProblem.ExplicitImpl {

		public Rosenbrock( int numDimensions, ClosedInterval bounds ) {
			super( Evaluate.Directional.minimizing( ( double [] input ) -> { 
				if( input.length != numDimensions )
					throw new IllegalArgumentException();
				return RosenbrockFn(input); }),					
			numDimensions, 
			RealVectorProblem.constantBounds( numDimensions, bounds ), 
			FunctionPoint.create( filledArray( numDimensions, 1.0 ), 0.0 ) );
		}
	}

	///////////////////////////////
	
	public static final class Step extends RealVectorProblem.ExplicitImpl {

		public Step( int numDimensions, ClosedInterval bounds, boolean includeOffset ) {
			super( Evaluate.Directional.minimizing( ( double [] input ) -> { 
				if( input.length != numDimensions )
					throw new IllegalArgumentException();
				return stepFn(input,includeOffset); }),					
			numDimensions, 
			RealVectorProblem.constantBounds( numDimensions, bounds ), 
			FunctionPoint.create( filledArray( numDimensions, -5.12 ), -30.0 ) );
		}
	}
	
	///////////////////////////////
	
	public static final class QuarticWithNoise extends RealVectorProblem.ExplicitImpl {

		public QuarticWithNoise( int numDimensions, ClosedInterval bounds, Random random ) {
			super( Evaluate.Directional.minimizing( ( double [] input ) -> { 
				if( input.length != numDimensions )
					throw new IllegalArgumentException();
				return quarticWithNoiseFn(input,random); }),					
			numDimensions, 
			RealVectorProblem.constantBounds( numDimensions, bounds ), 
			FunctionPoint.create( new double [ numDimensions ], 0.0 ) );
		}
	}
	
	///////////////////////////////

	public static final class Shekel extends RealVectorProblem.ExplicitImpl {

		/**
		 * From DeJong thesis, p207:
		 * It is a continuous, non-quadratic, two-dimensional function
		 * with 25 local minima approximately at the points (a_1j,a_2j).
		 * The function value of (a_1j,a_2j) is approximately c_j. 
		 */
		private static FunctionPoint< double [], Double > 
		globalMinimum( double [] c, double [][] a ) {
			if( a.length != 2 )
				throw new IllegalArgumentException( "Expected array of length 2, found " + a.length );			

			final int minIndex = minIndex( c );
			return FunctionPoint.create( new double [] { a[0][minIndex], a[1][minIndex] }, c[minIndex]);
		}
		
		public Shekel( int numDimensions, ClosedInterval bounds, int K, int numMinima, double [] c, double [][] a ) {
			super( Evaluate.Directional.minimizing( ( double [] input ) -> { 
				if( input.length != numDimensions )
					throw new IllegalArgumentException();
				return ShekelFn(input,K, numMinima, c, a ); }),					
			numDimensions, 
			RealVectorProblem.constantBounds( numDimensions, bounds ), 
			globalMinimum( c, a ) );
		}
	}
	
	///////////////////////////////
	
	private static Function< Integer, Double > a1DeJongShekel() {
		return ( Integer index ) -> {
			double [] values = new double [] { -32.0, -16.0, 0.0, 16.0, 32.0 };
			return values[ index % values.length ];
		};
	}

	private static Function< Integer, Double > a2DeJongShekel() {
		return ( Integer index ) -> {
			double [] values = new double [] { -32.0, -16.0, 0.0, 16.0, 32.0 };
			return values[ ( index / 5 ) % values.length ];
		};
	}

	///////////////////////////////	
	
	private static double sqr( double x ) { return x * x; }
	
	private static double [] filledArray( int size, double value ) {
		double [] a = new double [ size ];
		Arrays.fill( a, value );
		return a;
	}

	private static double [] filledArray( int size, Function< Integer, Double > f ) {
		double [] a = new double [ size ];
		for( int i=0; i<a.length; ++i )
			a[ i ] = f.apply( i );
		return a;
	}
	
	public static int minIndex( double [] a ) {
        
		int minIndex = -1;
        
	    if( a.length > 0 ) {
	    	minIndex = 0;	    	
	        double min = a[0];
	    	for( int i=1; i<a.length; ++i  ) {
	    		if( a[i] < min ) {
	    			minIndex = i;
	    			min = a[ i ];
	    		}
	        }
	    }
	    return minIndex;
	}
	
	///////////////////////////////
	
	public static void main( String [] args ) {
		
		for( int i=0; i<25; ++i )
			System.out.println( "i:" + i + " -> " + a2DeJongShekel().apply( i ) );
	}
}

// End ///////////////////////////////////////////////////////////////

