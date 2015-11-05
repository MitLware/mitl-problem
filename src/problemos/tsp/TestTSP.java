package problemos.tsp;

import static org.junit.Assert.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

import jeep.io.DirectoryListing;
import jeep.lang.BadFormatException;
import jeep.lang.Diag;
import jeep.lang.UnsupportedFormatException;
import jeep.math.Vec2;

import org.junit.Test;

import statelet.permutation.ArrayForm;

//////////////////////////////////////////////////////////////////////

public class TestTSP {

	@Test
	public void testToyInstance() throws IOException, BadFormatException {
		String path = System.getProperty( "user.dir" ) + "/resources/" + "unitTest.tsp";		
		InputStream is = new FileInputStream( path );
		TSP tsp = new TSP.TSPLibInstance( is );
		System.out.println( tsp );

		Map< Integer, Vec2> coords = new HashMap< Integer, Vec2 >();
		coords.put( 1, new Vec2( 1380.0, 939.0 ) );
		coords.put( 2, new Vec2( 2848.0, 96.0 ) );
		coords.put( 3, new Vec2( 3510.0,  1671.0 ) );
		coords.put( 4, new Vec2( 457.0, 334.0 ) );
		
		BiFunction< Integer, Integer, Double > dist = tsp.getDistanceFn();
		
		List< Map.Entry< Integer, Vec2> > coordList = new ArrayList<Map.Entry< Integer, Vec2>>(coords.entrySet());
		
		for( int i=0; i<coordList.size(); ++i ) {
			for( int j=i+1; j<coordList.size(); ++j ) {
				
				Integer i1 = coordList.get( i ).getKey();
				Integer i2 = coordList.get( j ).getKey();
				Vec2 v1 = coordList.get( i ).getValue();
				Vec2 v2 = coordList.get( j ).getValue();				
				
				assertEquals( Math.round( Vec2.EuclidianDistance( v1, v2 ) ), dist.apply( i1, i2 ), 0.0 );
			}
		}
	}

	@Test
	public void testWi29() throws IOException, BadFormatException {

		String path = System.getProperty( "user.dir" ) + "/resources/" + "wi29.tsp";		
		InputStream is = new FileInputStream( path );
		TSP tsp = new TSP.TSPLibInstance( is );
		System.out.println( tsp );
		
		ArrayForm tour = new ArrayForm( 17, 16, 20, 21, 22, 28, 27, 25, 19, 24, 26, 23, 15, 13, 12, 8, 6, 2, 3, 7, 4, 0, 1, 5, 9, 10, 11, 14, 18 );
		System.out.println( TSP.tourLength(tour,tsp.getDistanceFn()) );
	}
	
	///////////////////////////////
	
	@Test
	public void testTSPLib() throws IOException, BadFormatException {
		String directoryName = System.getProperty( "user.dir" ) + "/resources/" + "tsplib/";		
		
		final boolean recurse = true;
		List< Path > tspFiles = DirectoryListing.listFiles( directoryName, "tsp", recurse );
		Diag.println( tspFiles );
		
		int numUnsupported = 0;
		int numAsymetric = 0;		
		for( Path p : tspFiles ) {
			// System.out.println( "Reading file: " + p );
			InputStream is = new FileInputStream( p.toFile() );
			try {
				TSP tsp = new TSP.TSPLibInstance( is );
				if( !tsp.isSymmetric() )
					++numAsymetric;
				// System.out.println( tsp );			
			} catch( UnsupportedFormatException use ) {
				Diag.println( p );				
				Diag.println( "Unsupported: " + use );				
				// Diag.println( use.getMessage() );
				// use.printStackTrace(System.out );				
				++numUnsupported;
			}

		}
		
		Diag.println( "#files " + tspFiles.size() );
		Diag.println( "numAsymetric: " + numAsymetric );		
		Diag.println( "numUnsupported: " + numUnsupported );
		
	}
}

// End ///////////////////////////////////////////////////////////////
