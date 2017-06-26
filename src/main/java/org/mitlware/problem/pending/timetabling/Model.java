package problemos.pending.timetabling;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.StringTokenizer;

import jeep.lang.HashCode;

//////////////////////////////////////////////////////////////////////

public final class Model 
{
	private List< Exam >	exams = new ArrayList< Exam >();	
	private List< Period >	periods = new ArrayList< Period >();
	private List< Room >	rooms = new ArrayList< Room >();
	private List< Constraint >	periodHardConstraints = new ArrayList< Constraint >();
	private List< Constraint >	roomHardConstraints = new ArrayList< Constraint >();
	private List< Constraint >	institutionalWeightings = new ArrayList< Constraint >();	
	
	///////////////////////////////
	
	public Model( InputStream itcExamData )
	{
		Scanner scanner = new Scanner( itcExamData );
		
		///////////////////////////
		
		String s = scanner.findWithinHorizon( "\\[Exams:", 0 );
		if( s == null )
			throw new IllegalArgumentException();
		
		String next = scanner.next( "\\d*\\]" );
		next = next.substring( 0, next.length() - 1 );
		final int numExams = Integer.parseInt( next );
		if( numExams < 0 )
			throw new IllegalArgumentException();
		
		for( String line = scanner.nextLine(); 
			scanner.hasNextLine() && !scanner.hasNext( "\\[Periods:\\d*\\]" ); 
			line = scanner.nextLine() )
		{
			BitSet students = new BitSet();
			StringTokenizer tokenizer = new StringTokenizer( line, ", " );
			final int duration = Integer.valueOf( tokenizer.nextToken() );
			while( tokenizer.hasMoreTokens() )
				students.set( Integer.valueOf( tokenizer.nextToken() ) );
			
			exams.add( new Exam( duration, students ) );
		}

		if( exams.size() != numExams )
			throw new IllegalArgumentException();
		
		///////////////////////////
		
		if( scanner.findWithinHorizon( "\\[Periods:", 0 ) == null )
			throw new IllegalArgumentException();
		
		next = scanner.next( "\\d*\\]" );
		next = next.substring( 0, next.length() - 1 );
		final int numPeriods = Integer.parseInt( next );
		if( numPeriods < 0 )
			throw new IllegalArgumentException();
		
		for( String line = scanner.nextLine(); 
			scanner.hasNextLine() && !scanner.hasNext( "\\[Rooms:\\d*\\]" ); 			
			line = scanner.nextLine() )
		{
			// Period format is "15:04:2005, 09:30:00, 210, 0"
			String time = scanner.next( "\\d\\d:\\d\\d:\\d\\d\\d\\d," );
			String date = scanner.next( "\\d\\d:\\d\\d:\\d\\d," );
			
			next = scanner.next( "\\d*," );
			next = next.substring( 0, next.length() - 1 );			
			final int duration = Integer.valueOf( next );
			final int penalty = Integer.valueOf( scanner.next( "\\d*" ) );			
			
			periods.add( new Period( time + date, duration, penalty ) );
		}
		
		if( periods.size() != numPeriods )
			throw new IllegalArgumentException();
		
		///////////////////////////
		
		s = scanner.findWithinHorizon( "\\[Rooms:", 0 );
		if( s == null )
			throw new IllegalArgumentException();

		next = scanner.next( "\\d*\\]" );
		next = next.substring( 0, next.length() - 1 );
		final int numRooms = Integer.parseInt( next );
		if( numRooms < 0 )
			throw new IllegalArgumentException();
		
		for( String line = scanner.nextLine(); 
			scanner.hasNextLine() 
			&& !scanner.hasNext( "\\[PeriodHardConstraints\\]" );
			line = scanner.nextLine() )
		{
			next = scanner.next( "\\d*," );
			next = next.substring( 0, next.length() - 1 );			
			final int capacity = Integer.valueOf( next );

			next = scanner.next( "\\d*" );
			final int penalty = Integer.valueOf( next );
			
			rooms.add( new Room( capacity, penalty ) );			
		}

		if( rooms.size() != numRooms )
			throw new IllegalArgumentException();

		///////////////////////////

		s = scanner.findWithinHorizon( "\\[PeriodHardConstraints\\]", 0 );
		if( s == null )
			throw new IllegalArgumentException();
		
		scanner.nextLine();
		for( String line = scanner.nextLine(); 
			scanner.hasNextLine(); line = scanner.nextLine() )
		{
			/****
			next = scanner.next( "\\d*," );
			next = next.substring( 0, next.length() - 1 );			
			final int lhs = Integer.valueOf( next );
			String label = scanner.next( "\\w*," );

			next = scanner.next( "\\d*" );
			final int rhs = Integer.valueOf( next );

			periodHardConstraints.add( new Constraint( lhs, label, rhs ) );
			****/
			periodHardConstraints.add( 
					Constraint.parsePeriodHardConstraint( line ) );
			
			if( scanner.hasNext( "\\[RoomHardConstraints\\]" ) )
				break;
		}
		
		///////////////////////////
		
		s = scanner.findWithinHorizon( "\\[RoomHardConstraints\\]", 0 );
		if( s == null )
			throw new IllegalArgumentException();

		for( String line = scanner.nextLine(); 
			scanner.hasNextLine() 
			&& !scanner.hasNext( "\\[InstitutionalWeightings\\]" );
			line = scanner.nextLine() )
		{
			/********
			next = scanner.next( "\\d*," );
			next = next.substring( 0, next.length() - 1 );			
			final int roomId = Integer.valueOf( next );
			
			String label = scanner.next( "\\w*," );
			if( !label.equals( "ROOM_EXCLUSIVE" ) )
				throw new IllegalArgumentException();
			
			// roomHardConstraints.add( new Constraint.RoomExclusive( roomId ) );
			********/
			roomHardConstraints.add( 
					Constraint.parseRoomHardConstraint( line ) ); 
		}

		///////////////////////////		
		
		s = scanner.findWithinHorizon( "\\[InstitutionalWeightings\\]", 0 );
		if( s == null )
			throw new IllegalArgumentException();

		for( String line = scanner.nextLine(); scanner.hasNextLine(); )
		{
			line = scanner.nextLine();
			if( line.isEmpty() )
				break;
			
			institutionalWeightings.add( 
				Constraint.parseInstutionalModelWeighting( line ) );			
		}

		///////////////////////////

		
	}

	///////////////////////////////
	
	public int getNumPeriods() { return periods.size();	}
	public Period getPeriod( int i ) { return periods.get( i );	}

	public int getNumRooms() { return rooms.size(); }
	public Room getRoom( int i ) { return rooms.get( i ); }

	public int getNumExams() { return exams.size(); }
	public Exam getExam( int examId ) { return exams.get( examId ); }	

	public int getNumPeriodHardConstraints() { return periodHardConstraints.size(); }
	public int getNumRoomHardConstraints() { return roomHardConstraints.size(); }
	public int getNumInstitutionalWeightings() { return institutionalWeightings.size(); }	
	
	///////////////////////////////////

	public boolean equals( Object o )
	{
		if( !( o instanceof Model ) )
			return false;
		
		Model rhs = (Model)o;

		return exams.equals( rhs.exams )	
			&& periods.equals( rhs.periods )
			&& rooms.equals( rhs.rooms )
			&& periodHardConstraints.equals( rhs.periodHardConstraints )
			&& roomHardConstraints.equals( rhs.roomHardConstraints )
			&& institutionalWeightings.equals( rhs.institutionalWeightings );
	}

	public int hashCode()
	{
		return HashCode.apply( exams.hashCode(),	
			periods.hashCode(), 
			rooms.hashCode(),	
			periodHardConstraints.hashCode(), 
			roomHardConstraints.hashCode(), 
			institutionalWeightings.hashCode() );	
	}
	
	///////////////////////////////////
	
	public static void main( String args [] ) 
	throws URISyntaxException, FileNotFoundException
	{
        String fileName = "resources/exam_comp_set1.exam";		
		File itcExamData = new File( Model.class.getResource( 
				fileName ).toURI() );
		Model model = new Model( new FileInputStream( itcExamData ) );

		System.out.println( "numExams: " + model.getNumExams() );		
		System.out.println( "numPeriods: " + model.getNumPeriods() );
		System.out.println( "numRooms: " + model.getNumRooms() );
		System.out.println( "period hard constraints: " + model.getNumPeriodHardConstraints() );
		System.out.println( "room hard constraints: " + model.getNumRoomHardConstraints() );		
		System.out.println( "institutionalWeightings: " + model.getNumInstitutionalWeightings() );
	}
}

// End ///////////////////////////////////////////////////////////////

