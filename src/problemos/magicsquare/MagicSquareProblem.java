package problemos.magicsquare;

import java.util.Random;

import org.mitlware.SearchDirection;
import org.mitlware.mutable.Evaluate;
import org.mitlware.mutable.Perturb;

import statelet.permutation.ArrayForm;

//////////////////////////////////////////////////////////////////////

public final class MagicSquareProblem {

	public static final class UnconstrainedProblem 
	extends Evaluate.Directional< ArrayForm, Integer > {

		@Override
		public SearchDirection direction() { 
			return SearchDirection.MINIMIZING;
		}
		
		@Override
		public Integer apply( ArrayForm s ) {

			int result = 0;
			for (int i=0; i<order(s); ++i) {
				result += Math.abs( getMagicNumber(s) - getSumRow(s, i) );
				result += Math.abs( getMagicNumber(s) - getSumCol(s, i) );
			}
			result += Math.abs( getMagicNumber(s) - getSumDiag1(s) );
			result += Math.abs( getMagicNumber(s) - getSumDiag2(s) );
			
			return result;
		}

		private int getElement( ArrayForm s, int row, int col ) {
			assert( 0 <= row && row < order(s) );
			assert( 0 <= col && col < order(s) );
			return s.get( col + ( row * order(s) ) ) + 1;
		}
		
		private int order(ArrayForm s) { 
			return (int)Math.floor( Math.sqrt( s.size() ) ); 
		}
				
		private int getMagicNumber(ArrayForm s) {
			return (order(s)*(order(s)*order(s)+1))/2;
		}
		
		private int getSumRow(ArrayForm s, int row) {
			assert( 0 <= row && row < order(s) );
			int sum = 0;
			for (int i=0; i<order(s); ++i) {
				sum += getElement(s, row, i);
			}
			return sum;
		}
		
		private int getSumCol(ArrayForm s, int col) {
			assert( 0 <= col && col < order(s) );
			int sum = 0;
			for (int i=0; i<order(s); ++i) {
				sum += getElement(s, i, col);
			}
			return sum;
		}
		
		private int getSumDiag1(ArrayForm s) {
			int sum = 0;
			for (int i=0; i<order(s); ++i) {
				sum += getElement(s, i, i);
			}
			return sum;
		}
		
		private int getSumDiag2(ArrayForm s) {
			int sum = 0;
			for (int i=order(s)-1; i>=0; --i) {
				sum += getElement(s, order(s)-1-i, i);
			}
			return sum;
		}

	}
	
	///////////////////////////////
	
	public static final class RandomSwap 
	implements Perturb< ArrayForm > {
		
		private final Random random;
		
		///////////////////////////
		
		public RandomSwap( Random random ) {
			this.random = random;
		}

		@Override
		public ArrayForm apply( ArrayForm x ) {
			ArrayForm result = x.clone();
			result.randomSwap(random);
			return result;
		}
	}
	
}

// End ///////////////////////////////////////////////////////////////