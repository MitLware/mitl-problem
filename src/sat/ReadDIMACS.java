package sat;

import java.util.*; 
import java.io.*; 

//////////////////////////////////////////////////////////////////////

public final class ReadDIMACS 
{ 
	public static CNF 
	readDIMACS( InputStream is ) 
	throws IOException 
	{ 
		BufferedReader in = new BufferedReader( new InputStreamReader( is ) ); 

		int numVariables, numClauses;
		
		for( int lineNumber = 0; ; ++lineNumber ) 
		{ 
			String line = in.readLine(); 
			if( line == null ) 
				throw new IllegalArgumentException( "Unexpected EOF" );
			
			line = line.trim(); 
			String [] words = line.split("\\s+");
			
			for( int i=0; i<words.length; ++i )
				System.out.print( words[ i ] + " " );
			System.out.println( );			
			
			if( words[0].equals( "c" ) )
				continue;
			else if( words[0].equals( "p" ) )
			{ 
				if( !words[1].equals("cnf") )
				{
					// System.err.println( "expect cnf after p" );
					throw new IllegalArgumentException( "expect cnf after p" );					
				}

				numVariables = Integer.parseInt( words[2] );
				numClauses = Integer.parseInt( words[3] );
				break;
			}
		}

		///////////////////////////
		
		ArrayList< CNF.Clause > clauses = new ArrayList< CNF.Clause >();
		
		for( ; ; ) 
		{ 
			String line = in.readLine(); 
			if( line == null ) 
				break;
			
			line = line.trim(); 
			String [] words = line.split("\\s+"); 

			if( line.equals( "%" ) )
			{
				System.out.println( "[WARNING] not reading CNF file after '%'" );
				break;
			}
			else if( !line.equals( "" ) ) 
			{
				int [] clauseList = new int [ words.length - 1 ];
				for( int j=0; j<words.length; ++j )
				{ 
					int var = Integer.parseInt( words[j] ); 
					if( var == 0 )
					{ 
						clauses.add( new CNF.Clause( clauseList ) );						
					} 
					else 
					{
						clauseList[ j ] = var;						
					} 
				} 
			}
		}
		
		if( clauses.size() != numClauses )
			throw new IllegalArgumentException( "expected #clauses=" + numClauses  + ", found " + clauses.size() );
		
		return new CNF( numVariables, clauses );
	} 

	///////////////////////////////
	
	public static void main( String[] argv ) 
	throws FileNotFoundException, IOException 
	{ 
		// String fileName = "resources/unif-c500-v250-s453695930.cnf";		
		String fileName = "resources/simple_v3_c2.cnf";
		CNF cnf = readDIMACS( ReadDIMACS.class.getResourceAsStream( fileName ) );
		System.out.println( cnf );
		System.out.println( "All done." );
	} 
} 