package problemos.cvrp;

import static org.junit.Assert.*;

import org.junit.Test;

import problemos.cvrp.ChristophidesEtAlCVRPFormat.CVRPProblemDescription;


//////////////////////////////////////////////////////////////////////

public final class TestChristophidesEtAlCVRPFormat {

	@Test
	public void test() {
		String problemFile = "vrpnc1.txt";
		CVRPProblemDescription vrp = ChristophidesEtAlCVRPFormat.read( 
			ChristophidesEtAlCVRPFormat.class.getResourceAsStream( 
					"resources/ChristofidesMingozziToth1979/" + problemFile ) );
		
		vrp.writeChristophidesEtAlFormat( System.out );
		
		System.out.println( "All done." );
	}
}

// End ///////////////////////////////////////////////////////////////

