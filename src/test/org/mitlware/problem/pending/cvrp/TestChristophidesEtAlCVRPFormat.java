package org.mitlware.problem.pending.cvrp;

import static org.junit.Assert.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import org.junit.Test;

import org.mitlware.problem.pending.cvrp.ChristophidesEtAlCVRPFormat.CVRPProblemDescription;


//////////////////////////////////////////////////////////////////////

public final class TestChristophidesEtAlCVRPFormat {

	@Test
	public void test() throws FileNotFoundException {
		String path = System.getProperty( "user.dir" ) + "/resources/cvrp/ChristofidesMingozziToth";
		String problemFile = "vrpnc1.txt";
		
		CVRPProblemDescription vrp = ChristophidesEtAlCVRPFormat.read( 
			new FileInputStream( path + "/" + problemFile ) );
		
		vrp.writeChristophidesEtAlFormat( System.out );
		
		// FIXME: more extensive tests
		
		assertEquals( 50, vrp.getCustomers().size() );
	}
}

// End ///////////////////////////////////////////////////////////////

