package problemos.magicsquare;

import static org.junit.Assert.*;

import org.junit.Test;
import org.mitlware.mutable.Evaluate;

import statelet.permutation.ArrayForm;

public final class TestMagicSquareProblem {

	@Test
	public void testEvaluate() {
		Evaluate.Directional< ArrayForm, Integer > f = new MagicSquareProblem.UnconstrainedProblem();
		ArrayForm s = new ArrayForm(9);
		assertEquals( 24, f.apply( s ), 0 );
	}

}

// End ///////////////////////////////////////////////////////////////
