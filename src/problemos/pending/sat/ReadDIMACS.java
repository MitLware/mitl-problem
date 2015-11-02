package problemos.pending.sat;

import java.util.*; 
import java.util.logging.Logger;
import java.io.*; 

//////////////////////////////////////////////////////////////////////

public final class ReadDIMACS {
	
	private static Logger logger = Logger.getLogger( ReadDIMACS.class.getName() );
	
	///////////////////////////////
	
	public static final class BadDIMACSFormatException extends Exception {
		
		public BadDIMACSFormatException( String message ) { super(message); }
		
		private static final long serialVersionUID = -5775109281197015724L;
	}

	///////////////////////////////
	
	public static CNF 
	readDIMACS( InputStream is ) throws IOException, BadDIMACSFormatException	{
		
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
				logger.fine( words[ i ] + " " );
			logger.fine( "\n" );			
			
			if( words[0].equals( "c" ) )
				continue;
			else if( words[0].equals( "p" ) )
			{ 
				if( !words[1].equals("cnf") )
					throw new BadDIMACSFormatException( "expect cnf after p" );					

				numVariables = Integer.parseInt( words[2] );
				numClauses = Integer.parseInt( words[3] );
				break;
			}
		}

		///////////////////////////
		
		List< CNF.Clause > clauses = new ArrayList< CNF.Clause >();
		
		for( ; ; ) {
			
			String line = in.readLine(); 
			if( line == null ) 
				break;
			
			line = line.trim(); 
			String [] words = line.split("\\s+"); 

			if( words[0].equals( "c" ) )
				// ignore comments
				continue;
			
			if( line.equals( "%" ) ) {
				// logger.warning( "not reading CNF file after '%'" + line );
				break;
			}
			else if( !line.equals( "" ) ) {
				
				int [] clauseList = new int [ words.length - 1 ];
				for( int j=0; j<words.length; ++j ) {
					
					int var = Integer.parseInt( words[j] ); 
					if( var == 0 )
						clauses.add( new CNF.Clause( clauseList ) );						
					else 
						clauseList[ j ] = var;						
				} 
			}
		}
		
		if( clauses.size() != numClauses )
			throw new BadDIMACSFormatException( "expected #clauses=" + numClauses  + ", found " + clauses.size() );
		
		return new CNF( numVariables, clauses );
	} 

	///////////////////////////////
	
	public static void main( String[] argv ) 
	throws FileNotFoundException, IOException, BadDIMACSFormatException 
	{ 
		// String fileName = "resources/sat/unif-c500-v250-s453695930.cnf";		
		String fileName = "resources/sat/simple_v3_c2.cnf";
		CNF cnf = readDIMACS( ReadDIMACS.class.getResourceAsStream( fileName ) );
		//CNF cnf = readDIMACS( new FileInputStream( fileName ) );
		System.out.println( cnf );
		System.out.println( "All done." );
	} 
} 