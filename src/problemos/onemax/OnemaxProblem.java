package problemos.onemax;

import java.util.Arrays;
import java.util.Random;
import java.util.stream.Stream;

import org.mitlware.SearchDirection;
import org.mitlware.mutable.Evaluate;
import org.mitlware.mutable.Locality;
import org.mitlware.mutable.Perturb;

import statelet.bitvector.BitVector;

//////////////////////////////////////////////////////////////////////

public final class OnemaxProblem {

	public static final class CountOnes 
	extends Evaluate.Directional< BitVector, Double > {

		@Override
		public SearchDirection direction() { return SearchDirection.MAXIMIZING; }
		
		@Override
		public Double apply( BitVector x ) {
			return x.cardinality() / (double)x.length();
		}
	}
	
	///////////////////////////////
	
	public static final class UniformMutation implements Perturb< BitVector > {
		
		private final Random random;
		
		///////////////////////////
		
		public UniformMutation( Random random ) {
			this.random = random;
		}

		@Override
		public BitVector apply( BitVector x ) {
			BitVector result = x.clone();
			result.flip( random.nextInt( x.length() ) );
			return result;
		}
	}
	
	///////////////////////////////

	public static final class Hamming1Locality implements Locality< BitVector > {
		
		public Stream< BitVector > apply( BitVector x ) {
			
			BitVector [] neighbours = new BitVector [ x.length() ]; 
			for( int i=0; i<x.length(); ++i ) {
				BitVector neighbor = x.clone();
				neighbor.flip( i );
				neighbours[ i ] = neighbor;
			}

			return Arrays.stream( neighbours );
		}
	}
	
	///////////////////////////////	
}

// End ///////////////////////////////////////////////////////////////
