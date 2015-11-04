package problemos.windfarm;

import static org.junit.Assert.*;

import org.junit.Test;
import org.mitlware.mutable.Evaluate;

import statelet.bitvector.BitVector;

public final class TestWindFarmProblem {

	@Test
	public void testEvaluate() {

		try {
			WindScenario ws = new WindScenario("resources/windfarmscenarios/obs_00.xml");
			KusiakLayoutEvaluator wfl = new KusiakLayoutEvaluator();
			wfl.initialize(ws);			
			Evaluate.Directional< BitVector, Double > f = new WindFarmProblem.BitVectorRepresentationProblem(wfl);		
			BitVector s = new BitVector(wfl.getGrid().size());
			s.clear();
			s.set(0);
			assertEquals( 0.10080718929716016, f.apply( s ), 0.0 );
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}

// End ///////////////////////////////////////////////////////////////
