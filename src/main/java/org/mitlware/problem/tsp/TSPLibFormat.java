package org.mitlware.problem.tsp;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.mitlware.support.lang.BadFormatException;
import org.mitlware.support.lang.Diag;
import org.mitlware.support.lang.UnsupportedFormatException;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.apache.commons.lang3.tuple.Pair;

import cformat.ScanfReader;

//////////////////////////////////////////////////////////////////////

public final class TSPLibFormat {

	private static final Logger LOGGER = Logger.getLogger( TSPLibFormat.class.getName() );
	
	private final String name;
	private final Type type;
	private final int dimension;
	private final String comment;
	private final EdgeWeightType edgeWeightType;
	private final EdgeWeightFormat edgeWeightFormat;
	private final List< Pair< String, Object > > symbolTable;

	///////////////////////////////	

	private static final String [] keywords = {	"NAME", "TYPE", "COMMENT", "DIMENSION",	
		"CAPACITY",	"EDGE_WEIGHT_TYPE",	"EDGE_WEIGHT_FORMAT", "EDGE_DATA_FORMAT", "NODE_COORD_TYPE",
		"DISPLAY_DATA_TYPE", "NODE_COORD_SECTION", "DEPOT_SECTION",	"DEMAND_SECTION", 
		"EDGE_DATA_SECTION", "FIXED_EDGES_SECTION", "DISPLAY_DATA_SECTION", "TOUR_SECTION", 
		"EDGE_WEIGHT_SECTION", "EOF" };
	
	///////////////////////////////
	
	private TSPLibFormat( String name, Type type, int dimension, 
			String comment, EdgeWeightType edgeWeightType, EdgeWeightFormat edgeWeightFormat, List< Pair< String, Object > > symbolTable ) {
		this.name = name;
		this.type = type;
		this.dimension = dimension;
		this.comment = comment;
		this.edgeWeightType = edgeWeightType;
		this.edgeWeightFormat = edgeWeightFormat;
		this.symbolTable = symbolTable;		
	}

	///////////////////////////////

	private static Object lookup(List< Pair< String, Object > > symbolTable, String key) {
		for( int i=0; i<symbolTable.size(); ++i )
			if( symbolTable.get(i).getKey().equals( key ) ) 
				return symbolTable.get(i).getValue();
		return null;
	}
	
	public static TSPLibFormat read( InputStream is ) throws IOException, BadFormatException {
		
		List< Pair< String, Object > > symbolTable = new ArrayList<>(); // new HashMap< String, Object >();
		Reader reader = new InputStreamReader( is );
		LineNumberReader lineReader = new LineNumberReader( reader );
		while( process( lineReader, symbolTable ) )
			;

		///////////////////////////
		
		String name = (String)lookup( symbolTable, "NAME" );
		Type type = (Type)lookup( symbolTable, "TYPE" );
		int dimension = (Integer)lookup( symbolTable, "DIMENSION" );
		String comment = (String)lookup( symbolTable, "COMMENT" );
		EdgeWeightType edgeWeightType = (EdgeWeightType)lookup( symbolTable, "EDGE_WEIGHT_TYPE" );
		EdgeWeightFormat edgeWeightFormat = (EdgeWeightFormat)lookup( symbolTable, "EDGE_WEIGHT_FORMAT" ); 		
		return new TSPLibFormat( name, type, dimension, 
				comment, edgeWeightType, edgeWeightFormat, symbolTable );
	}
	
	///////////////////////////////	
	
	public String getName() { return name; }
	
	enum Type { TSP, ATSP };
	
	public Type getType() { return type; }
	
	public int getDimension() { return dimension; }
	public String getComment() { return comment; }
	
	public BiFunction< Integer, Integer, Integer > getDistanceFn() {
		Map< String, Object > fastSymbolTable = new HashMap< String, Object >();
		for( Pair< String, Object > p: symbolTable )
			fastSymbolTable.put( p.getKey(), p.getValue() );
		
		return edgeWeightType.distanceFn( fastSymbolTable ); 
	}
	
	private static <T>  String listToString(List<T> l){
		StringBuilder sb = new StringBuilder();
		for( int i=0; i<l.size() - 1; ++i )
			sb.append( l.get(i) + " " );
		sb.append( l.get(l.size() - 1) );		
		return sb.toString();
	}
	
	public Optional<String> toTSPLibFormat() {
		StringBuilder sb = new StringBuilder();
		for( Pair< String, Object > p: symbolTable ) {
			switch( p.getKey() ) {
				case "NODE_COORD_SECTION": {
					sb.append( "NODE_COORD_SECTION\n" );					
					switch( edgeWeightType ) {
						case EUC_2D : {
							Map< Integer, List< Double > > nodeCoords = (Map< Integer, List<Double>>)lookup(symbolTable,"NODE_COORD_SECTION");
							for( Map.Entry< Integer, List< Double > > e: nodeCoords.entrySet() )
								sb.append( e.getKey() + " " + listToString( e.getValue() ) + "\n" );
						} break;
						case ATT: case CEIL_2D: case EUC_3D: case EXPLICIT: case GEO:
						case MAN_2D: case MAN_3D: case MAX_2D: case MAX_3D:
						case SPECIAL: case XRAY1: case XRAY2:
						default: return Optional.empty();
					}
				} break;
				default:
					sb.append( p.getKey() + ": " + p.getValue() + "\n");
				break;				
			}
		}
		
		sb.append( "EOF\n" );
		return Optional.of(sb.toString());
	}
	
	///////////////////////////////
	
	private static double sqr(double x ) { return x*x; }
	
	enum EdgeWeightType { 
		EXPLICIT {
			BiFunction< Integer, Integer, Integer > distanceFn(Map< String, Object > symbolTable) { 
				throw new UnsupportedFormatException(); 
			}
			Optional< Integer > weightDimension() { return Optional.empty(); }			
		}, 
		EUC_2D {
			BiFunction< Integer, Integer, Integer > distanceFn( Map< String, Object > symbolTable ) { 
				return ( a, b ) -> {
					@SuppressWarnings("unchecked")
					Map< Integer, List< Double > > nodeCoords = (Map< Integer, List<Double>>)symbolTable.get( "NODE_COORD_SECTION");
					
					final List< Double > ac = nodeCoords.get( a );
					final List< Double > bc = nodeCoords.get( b );
					final double xd = ac.get(0) - bc.get(0);
					final double yd = ac.get(1) - bc.get(1);
					return (int)Math.round( Math.sqrt( sqr(xd) + sqr(yd) ) );
				};
			}
			
			Optional< Integer > weightDimension() { return Optional.of( 2 ); }			
		}, 
		EUC_3D {
			BiFunction< Integer, Integer, Integer > distanceFn(Map< String, Object > symbolTable) { 
				throw new UnsupportedFormatException("EUC_3D"); 
			}
			
			Optional< Integer > weightDimension() { return Optional.of( 3 ); }			
		}, 
		MAX_2D {
			BiFunction< Integer, Integer, Integer > distanceFn(Map< String, Object > symbolTable) { 
				throw new UnsupportedFormatException("MAX_2D"); 
			}
			
			Optional< Integer > weightDimension() { return Optional.of( 2 ); }			
		}, 
		MAX_3D {
			BiFunction< Integer, Integer, Integer > distanceFn(Map< String, Object > symbolTable) { 
				throw new UnsupportedFormatException("MAX_3D"); 
			}
			
			Optional< Integer > weightDimension() { return Optional.of( 3 ); }			
		}, 
		MAN_2D {
			BiFunction< Integer, Integer, Integer > distanceFn(Map< String, Object > symbolTable) { 
				throw new UnsupportedFormatException("MAN_2D"); 
			}
			Optional< Integer > weightDimension() { return Optional.of( 2 ); }			
		}, 
		MAN_3D {
			BiFunction< Integer, Integer, Integer > distanceFn(Map< String, Object > symbolTable) { 
				throw new UnsupportedFormatException("MAN_3D"); 
			}
			Optional< Integer > weightDimension() { return Optional.of( 3 ); }			
		}, 
		CEIL_2D {
			BiFunction< Integer, Integer, Integer > distanceFn(Map< String, Object > symbolTable) { 
				throw new UnsupportedFormatException("CEIL_2D"); 
			}
			Optional< Integer > weightDimension() { return Optional.of( 2 ); }			
		}, 
		GEO {
			BiFunction< Integer, Integer, Integer > distanceFn(Map< String, Object > symbolTable) { 
				throw new UnsupportedFormatException("GEO");
			}
			Optional< Integer > weightDimension() { throw new UnsupportedFormatException("GEO"); }			
		}, 
		ATT {
			BiFunction< Integer, Integer, Integer > distanceFn(Map< String, Object > symbolTable) { 
				throw new UnsupportedFormatException("ATT");
			}
			Optional< Integer > weightDimension() { throw new UnsupportedFormatException("ATT"); }			
		}, 
		XRAY1 {
			BiFunction< Integer, Integer, Integer > distanceFn(Map< String, Object > symbolTable) { 
				throw new UnsupportedFormatException("XRAY1"); 
			}
			Optional< Integer > weightDimension() { throw new UnsupportedFormatException("XRAY1"); }			
		}, 
		XRAY2 {
			BiFunction< Integer, Integer, Integer > distanceFn(Map< String, Object > symbolTable) { 
				throw new UnsupportedFormatException("XRAY2"); 
			}
			Optional< Integer > weightDimension() { throw new UnsupportedFormatException("XRAY2"); }			
		}, 
		SPECIAL {
			BiFunction< Integer, Integer, Integer > distanceFn(Map< String, Object > symbolTable) { 
				throw new UnsupportedFormatException("SPECIAL"); 
			}
			Optional< Integer > weightDimension() { throw new UnsupportedFormatException("SPECIAL"); }			
		};

		abstract BiFunction< Integer, Integer, Integer > distanceFn( Map< String, Object > symbolTable );		
		abstract Optional< Integer > weightDimension();
	};
	
	public EdgeWeightType getEdgeWeightType() { return edgeWeightType; }	

	enum EdgeWeightFormat { FUNCTION, FULL_MATRIX, UPPER_ROW, LOWER_ROW, UPPER_DIAG_ROW, LOWER_DIAG_ROW, UPPER_COL, LOWER_COL, UPPER_DIAG_COL, LOWER_DIAG_COL };
	public EdgeWeightFormat getEdgeWeightFormat() { return edgeWeightFormat; }
	
	enum EdgeDataFormat { EDGE_LIST, ADJ_LIST };
	
	enum NodeCoordType { TWOD_COORDS, THREED_COORDS, NO_COORDS };
	
	enum DisplayDataType { COORD_DISPLAY, TWOD_DISPLAY, NO_DISPLAY };
	
	///////////////////////////////
	
	private static void throwIfRedefined( String keyStr, List< Pair< String, Object > > symbolTable ) {
		Object existingValue = lookup( symbolTable, keyStr );
		if( existingValue != null )
			throw new RuntimeException( "Redefinition of " + keyStr );
	}
	
	private static Pair< String, String > parseKeyword( String line ) throws UnsupportedFormatException {
		Optional< String > optKey = Optional.empty();
		Optional< String > optValue = Optional.empty();		
		for( String keyword : keywords ) {
			if( line.startsWith( keyword ) ) {
				optKey = Optional.of( keyword );
				final int separatorIndex = line.indexOf( ": " );
				if( separatorIndex != -1 ) {
					optValue = Optional.of( line.substring( separatorIndex + 2, line.length() ) );
				}
				break;
			}
		}

		String key = optKey.orElseThrow( () -> new UnsupportedFormatException( "keyword expected, found : " + line ) );
		return Pair.of( key.trim(), optValue.orElse( "" ).trim() );
	}

	///////////////////////////////
	
	private static boolean process( LineNumberReader lineReader, List< Pair< String, Object > > symbolTable ) 
	throws IOException, UnsupportedFormatException, BadFormatException {
		
		if( !lineReader.ready() )
			return false;
		
		String line = lineReader.readLine();
		
		///////////////////////////
		
// Diag.println( line );
		Pair< String, String > p = parseKeyword( line );
		String key = p.getKey();
		String value = p.getValue();		
		
		boolean isEOF = false;
		
		switch( key ) {
			case "NAME" : {
				throwIfRedefined( key, symbolTable );
				symbolTable.add( Pair.of( "NAME", value ) );
			} break;
			case "TYPE" : {
				throwIfRedefined( key, symbolTable );
				String [] tokens = value.split("\\s+");
				if( tokens.length == 0 )
					new UnsupportedFormatException( "TSP type expected, found : " + value );
				
				Optional< Type > opt = Arrays.asList( Type.values() ).stream().filter( t -> t.toString().equals( tokens[0] ) ).findFirst();
				final Type type = opt.orElseThrow( () -> new UnsupportedFormatException( "TSP type expected, found : " + value ) );
				symbolTable.add( Pair.of( "TYPE", type ) );
			} break;
			case "COMMENT" : {
				String existing = (String)lookup( symbolTable,"COMMENT");
				if( existing == null )
					symbolTable.add( Pair.of( "COMMENT", value ) );
				else
					symbolTable.add( Pair.of( "COMMENT", existing + " "+ value ) );					
			} break;
			case "DIMENSION" : {
				throwIfRedefined( key, symbolTable );
				
				final int dimension = Integer.valueOf( value );
				symbolTable.add( Pair.of( "DIMENSION", dimension ) );
			} break;
			case "CAPACITY" : { 
				throwIfRedefined( key, symbolTable );
				
				final int capacity = Integer.valueOf( value );
				if( capacity <= 0 )
					new UnsupportedFormatException( "Valid CAPACITY expected, found : " + capacity );
				
				symbolTable.add( Pair.of( "CAPACITY", capacity ) );
			} break;
			case "EDGE_WEIGHT_TYPE" : { 
				throwIfRedefined( key, symbolTable );
				
				Optional< EdgeWeightType > opt = Arrays.asList( EdgeWeightType.values() ).stream().filter( t -> t.toString().equals( value ) ).findFirst();		
				final EdgeWeightType type = opt.orElseThrow( () -> new UnsupportedFormatException( "Valid EDGE_WEIGHT_TYPE expected, found : " + value ) );
				symbolTable.add( Pair.of( "EDGE_WEIGHT_TYPE", type ) );
			} break;
			case "EDGE_WEIGHT_FORMAT" : {
				throwIfRedefined( key, symbolTable );				
				
				Optional< EdgeWeightFormat > opt = Arrays.asList( EdgeWeightFormat.values() ).stream().filter( t -> t.toString().equals( value ) ).findFirst();		
				final EdgeWeightFormat type = opt.orElseThrow( () -> new UnsupportedFormatException( "Valid EDGE_WEIGHT_FORMAT expected, found : " + value ) );
				symbolTable.add( Pair.of( "EDGE_WEIGHT_FORMAT", type ) );
			} break;
			case "EDGE_DATA_FORMAT" : {
				throwIfRedefined( key, symbolTable );
				
				Optional< EdgeDataFormat > opt = Arrays.asList( EdgeDataFormat.values() ).stream().filter( t -> t.toString().equals( value ) ).findFirst();		
				final EdgeDataFormat type = opt.orElseThrow( () -> new UnsupportedFormatException( "Valid EDGE_DATA_FORMAT expected, found : " + value ) );
				symbolTable.add( Pair.of( "EDGE_DATA_FORMAT", type ) );
			} break;
			case "NODE_COORD_TYPE" : {
				throwIfRedefined( key, symbolTable );
				
				Optional< NodeCoordType > opt = Arrays.asList( NodeCoordType.values() ).stream().filter( t -> t.toString().equals( value ) ).findFirst();		
				final NodeCoordType type = opt.orElseThrow( () -> new UnsupportedFormatException( "Valid NODE_COORD_TYPE expected, found : " + value ) );
				symbolTable.add( Pair.of( "NODE_COORD_TYPE", type ) );
			} break;
			case "DISPLAY_DATA_TYPE" : {
				throwIfRedefined( key, symbolTable );
				
				Optional< DisplayDataType > opt = Arrays.asList( DisplayDataType.values() ).stream().filter( t -> t.toString().equals( value ) ).findFirst();		
				final DisplayDataType type = opt.orElseThrow( () -> new UnsupportedFormatException( "Valid DISPLAY_DATA_TYPE expected, found : " + value ) );
				symbolTable.add( Pair.of( "DISPLAY_DATA_TYPE", type ) );
			} break;
			case "NODE_COORD_SECTION" : {
				
				final NodeCoordType t = (NodeCoordType)lookup( symbolTable, "NODE_COORD_TYPE" );
				final EdgeWeightType ewt = (EdgeWeightType)lookup( symbolTable, "EDGE_WEIGHT_TYPE" );
				
				if( t == null && ewt == null ) 
					throw new BadFormatException( "Unknown number of coords in NODE_COORD_SECTION" );

				int numCoords = -1; 
				if( t == NodeCoordType.TWOD_COORDS || t == NodeCoordType.THREED_COORDS ) {				
					numCoords = t == NodeCoordType.TWOD_COORDS ? 2 : 3;
				}
				else
					numCoords = ewt.weightDimension().orElseThrow( () -> new BadFormatException( "Unknown number of coords in NODE_COORD_SECTION" ) );

				final Integer dimension = (Integer)lookup( symbolTable, "DIMENSION");
				if( dimension == null )
					throw new BadFormatException( "DIMENSION must be defind before EDGE_WEIGHT_SECTION" );
				
				final Map< Integer, List< Double > > nodes = new HashMap< Integer, List< Double > >();
				for( int i=0; i<dimension; ++i ) {
					if( !lineReader.ready() )
						throw new BadFormatException( "NODE_COORD_SECTION: expected " + dimension + " nodes, found " + ( i + 1 ) );
				
					line = lineReader.readLine().trim();
					if( "EOF".equals( line ) )
						throw new BadFormatException( "NODE_COORD_SECTION: expected " + dimension + " nodes, found " + ( i + 1 ) );						

					List< Double > row = Arrays.stream( line.split("\\s+") ).map( x -> Double.valueOf( x )).collect(Collectors.toList());
					List< Double > tail = row.subList(1, row.size());
					if( tail.size() != numCoords )
						throw new BadFormatException( "NODE_COORD_SECTION: expected " + dimension + " coords, found " + tail );
					
					nodes.put( row.get( 0 ).intValue(), tail );					
				}		
				
				symbolTable.add( Pair.of("NODE_COORD_SECTION", nodes ));
			} break;
			case "DEPOT_SECTION" : { 

				final List< Integer > depotNodes = new ArrayList< Integer >();
				while( lineReader.ready() ) {
					line = lineReader.readLine().trim();
					// if( "EOF".equals( line ) ) {
					// isEOF = true;
					// break;
					// }
						
					ScanfReader scanf = new ScanfReader( new StringReader( line ) );
					
					final int nodeId = scanf.scanInt();
					if( nodeId == -1 )
						break;
					
					depotNodes.add( nodeId );
					scanf.close();
				}	
				
				symbolTable.add( Pair.of("DEPOT_SECTION", depotNodes ) );
				
			} break;
			case "DEMAND_SECTION" : {
				throw new UnsupportedFormatException( "Not supported:" + key );
			} // break;
			case "EDGE_DATA_SECTION" : { 
				throw new UnsupportedFormatException( "Not supported:" + key );				
			} // break;
			case "FIXED_EDGES_SECTION" : {
				throw new UnsupportedFormatException( "Not supported:" + key );				
			} // break;
			case "DISPLAY_DATA_SECTION" : {
				throw new UnsupportedFormatException( "Not supported:" + key );				
			} // break;
			case "TOUR_SECTION" : {
				throw new UnsupportedFormatException( "Not supported:" + key );				
			} // break;
			case "EDGE_WEIGHT_SECTION" : {
				if( lookup( symbolTable, "EDGE_WEIGHT_FORMAT") == null )
					throw new RuntimeException( "EDGE_WEIGHT_FORMAT must be defind before EDGE_WEIGHT_SECTION" );
				
				final EdgeWeightFormat ewf = (EdgeWeightFormat)lookup(symbolTable, "EDGE_WEIGHT_FORMAT" );

				if( lookup( symbolTable, "DIMENSION") == null )
					throw new RuntimeException( "DIMENSION must be defind before EDGE_WEIGHT_SECTION" );

				final int dimension = (Integer)lookup( symbolTable, "DIMENSION" );
				
				Map< Pair< Integer, Integer >, Integer > matrix = null;
				
				switch( ewf ) {
					case FUNCTION :
					case UPPER_ROW : 
					case LOWER_ROW : 
					case UPPER_DIAG_ROW : 
					case UPPER_COL : 
					case LOWER_COL : 
					case UPPER_DIAG_COL : 
					case LOWER_DIAG_COL :
					case LOWER_DIAG_ROW :
						throw new UnsupportedFormatException( ewf.toString() );
					case FULL_MATRIX :
						matrix = parseEdgeWeight_FULL_MATRIX( dimension, lineReader );
					break;				
				}

				symbolTable.add( Pair.of("EDGE_WEIGHT_SECTION", matrix ));
	
			} break;
			case "EOF" : { isEOF = true; } break;
		}
		
		return !isEOF;
	}
	
	///////////////////////////////
	
	private static List< Integer > 
	readEdgeWeightEntries( int dimension, LineNumberReader lineReader ) 
	throws BadFormatException, IOException {
		
		List< Integer > entries = new ArrayList< Integer >(); 
		while( lineReader.ready() ) {
			String line = lineReader.readLine().trim();

			boolean foundKeyword = false;
			for( String keyword : keywords ) {
				if( line.startsWith( keyword ) ) {
					foundKeyword = true;
					break;
				}
			}

			if( foundKeyword )
				break;
			
			List< Integer > row = Arrays.stream( line.split("\\s+") ).map( x -> Integer.valueOf( x )).collect(Collectors.toList());
			if( row.size() != dimension )
				throw new BadFormatException( "Unexpected EOF" );						
		
			entries.addAll( row );
		}
		
		return entries;
	}
	
	///////////////////////////////	
	
	private static Map< Pair< Integer, Integer >, Integer > 
	parseEdgeWeight_FULL_MATRIX( int dimension, LineNumberReader lineReader ) throws BadFormatException, IOException {
	
		Map< Pair< Integer, Integer >, Integer > matrix = new HashMap< Pair< Integer, Integer >, Integer >();
		
		List< Integer > entries = readEdgeWeightEntries( dimension, lineReader ); 
		for( int i=0; i<entries.size(); ++i )
			matrix.put( Pair.of( ( i % dimension ) + 1, ( i / dimension ) + 1 ), entries.get( i ) );
			
		return matrix;		
	}
}

// End ///////////////////////////////////////////////////////////////
