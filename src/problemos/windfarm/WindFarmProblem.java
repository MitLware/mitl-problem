package problemos.windfarm;

import java.util.Arrays;
import java.util.Random;
import java.util.stream.Stream;

import org.mitlware.SearchDirection;
import org.mitlware.mutable.Evaluate;
import org.mitlware.mutable.Locality;
import org.mitlware.mutable.Perturb;

import statelet.bitvector.BitVector;

//////////////////////////////////////////////////////////////////////

public final class WindFarmProblem {

	public static final class BitVectorRepresentationProblem 
	extends Evaluate.Directional< BitVector, Double > {

		private WindFarmLayoutEvaluator wfl;
		
		public BitVectorRepresentationProblem( WindFarmLayoutEvaluator evaluator ) {
			wfl = evaluator;
		}
		
		@Override
		public SearchDirection direction() { 
			return SearchDirection.MINIMIZING;
		}
		
		@Override
		public Double apply( BitVector s ) {

			double result = Double.MAX_VALUE;
			
			int nturbines=s.cardinality();
			if (nturbines == 0) {
				throw new IllegalArgumentException();
			}
			
			double[][] layout = new double[nturbines][2];
			int l_i = 0;
			for (int i=0; i<s.length(); i++) {
				if (s.get(i)) {
					layout[l_i][0] = wfl.getGrid().get(i)[0];
					layout[l_i][1] = wfl.getGrid().get(i)[1];
					l_i++;
				}
			}
			
			if (wfl.checkConstraint(layout)) {
				wfl.evaluate(layout);
				result = wfl.getEnergyCost();
			}

			return result;
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