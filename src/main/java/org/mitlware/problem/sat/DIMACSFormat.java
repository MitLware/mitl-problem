package org.mitlware.problem.sat;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger; 

//////////////////////////////////////////////////////////////////////

public final class DIMACSFormat {
	
	private static Logger logger = Logger.getLogger( DIMACSFormat.class.getName() );
	
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
		
		String incompleteLine = null; // the cnf format allows clauses to be split over multiple lines
		for( ; ; ) {
			
			String line = in.readLine(); 
			if( line == null ) 
				break;
			
			line = line.trim();

			if( line.startsWith( "c" ) ) {
				// ignore comments
				continue;
			}

			if( line.equals( "%" ) ) {
				
				// logger.warning( "not reading CNF file after '%'" + line );
				break;
				
			} else if( !line.equals( "" ) ) {
				if (incompleteLine != null) {
					line = incompleteLine + " " + line;
					incompleteLine = null;
				}
				
				String [] words = line.split("\\s+"); 
	
				if (words[words.length - 1].equals("0")) {
					int [] clauseList = new int [ words.length - 1 ];
				
					if (clauseList.length > 0) { // not an empty line
						for( int j=0; j<words.length; ++j ) {
							int var = Integer.parseInt( words[j] ); 
							if( var == 0 )
								clauses.add( new CNF.Clause( clauseList ) );						
							else 
								clauseList[ j ] = var;
						}
					}
				} else { // no "0" at the end of the line means that it wraps onto the next one
					incompleteLine = line;
				}
			}
		}
		
		if( clauses.size() != numClauses )
			throw new BadDIMACSFormatException( "expected #clauses=" + numClauses  + ", found " + clauses.size() );
		
		return new CNF( numVariables, clauses );
	} 
	
} 
