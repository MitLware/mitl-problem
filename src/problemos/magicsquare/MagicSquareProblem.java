package problemos.magicsquare;

import java.util.Random;

import org.mitlware.SearchDirection;
import org.mitlware.mutable.Evaluate;
import org.mitlware.mutable.Perturb;

//////////////////////////////////////////////////////////////////////

public final class MagicSquareProblem {

	public static final class UnconstrainedProblem 
	extends Evaluate.Directional< MagicSquareState, Integer > {

		@Override
		public SearchDirection direction() { 
			return SearchDirection.MINIMIZING;
		}
		
		@Override
		public Integer apply( MagicSquareState s ) {
			
			int result = 0;
			for (int i=0; i<s.order(); ++i) {
				result += Math.abs( s.getMagicNumber() - s.getSumRow(i) );
				result += Math.abs( s.getMagicNumber() - s.getSumCol(i) );
			}
			result += Math.abs( s.getMagicNumber() - s.getSumDiag1() );
			result += Math.abs( s.getMagicNumber() - s.getSumDiag2() );
			
			return result;
		}
	}
	
	///////////////////////////////
	
	public static final class RandomSwap 
	implements Perturb< MagicSquareState > {
		
		private final Random random;
		
		///////////////////////////
		
		public RandomSwap( Random random ) {
			this.random = random;
		}

		@Override
		public MagicSquareState apply( MagicSquareState x ) {
			MagicSquareState result = x.clone();
			int i = random.nextInt( x.order() );
			int j = random.nextInt( x.order() - 1 );
			if (i==j) j = x.order() - 1;
			result.transpose(i, j);
			return result;
		}
	}
	
}

// End ///////////////////////////////////////////////////////////////

