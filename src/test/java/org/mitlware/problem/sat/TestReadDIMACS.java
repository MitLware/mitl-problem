package org.mitlware.problem.sat;

import org.junit.Test;
import org.mitlware.problem.sat.DIMACSFormat.BadDIMACSFormatException;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

//////////////////////////////////////////////////////////////////////

public class TestReadDIMACS {

	public void testImpl(String path) throws IOException, BadDIMACSFormatException {

		// String path = System.getProperty( "user.dir" ) + "/resources/sat/" + "simple_v3_c2.cnf";		
		InputStream is = new FileInputStream( path );
		CNF cnf = DIMACSFormat.readDIMACS( is );
		System.out.println( cnf );
	}
	
	@Test
	public void test() throws IOException, BadDIMACSFormatException {

		String path1 = System.getProperty( "user.dir" ) + "/resources/sat/" + "simple_v3_c2.cnf";
		testImpl( path1 );
		String path2 = System.getProperty( "user.dir" ) + "/resources/sat/" + "unif-c500-v250-s453695930.cnf";		
		testImpl( path2 );		
	}
}

// End ///////////////////////////////////////////////////////////////
