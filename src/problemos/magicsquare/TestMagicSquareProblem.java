package problemos.magicsquare;

import static org.junit.Assert.*;

import org.junit.Test;
import org.mitlware.mutable.Evaluate;

import statelet.permutation.ArrayForm;

public final class TestMagicSquareProblem {

	@Test
	public void testEvaluateUnconstrainedProblem() {
		Evaluate.Directional< ArrayForm, Integer > f = new MagicSquareProblem.UnconstrainedProblem(3);		
		ArrayForm s = new ArrayForm(9);
		assertEquals( 24, f.apply( s ), 0 );
	}

	///////////////////////////////
	
	@Test
	public void testEvaluateConstrainedProblem() {
		Evaluate.Directional< ArrayForm, Integer > f = new MagicSquareProblem.ConstrainedProblem(4,0,1,10);
		ArrayForm s = new ArrayForm(16);
		assertEquals( 170, f.apply( s ), 0 );
	}
}

// End ///////////////////////////////////////////////////////////////
