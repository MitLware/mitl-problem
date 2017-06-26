package org.mitlware.problem.pending.timetabling;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.StringTokenizer;

import jeep.lang.HashCode;
import jeep.math.ClosedInterval;
import jeep.math.ClosedOpenInterval;

public final class Period 
{
	private final long calendarTimeInMilliseconds;	
	private final long durationInMilliseconds;
	private final int penalty;	
	
	///////////////////////////////
	
	public static boolean isValidDate( String s )
	{
		// e.g. 31:05:2005
		// @see http://www.cs.qub.ac.uk/itc2007/examtrack/exam_track_index_files/Inputformat.htm
		return Character.isDigit( s.charAt( 0 ) )
			&& Character.isDigit( s.charAt( 1 ) )
			&& s.charAt( 2 ) == ':'
			&& Character.isDigit( s.charAt( 3 ) )
			&& Character.isDigit( s.charAt( 4 ) )
			&& s.charAt( 5 ) == ':'			
			&& Character.isDigit( s.charAt( 6 ) )
			&& Character.isDigit( s.charAt( 7 ) )
			&& Character.isDigit( s.charAt( 8 ) )
			&& Character.isDigit( s.charAt( 9 ) );			
	}

	public static boolean isValidTime( String s )
	{
		// e.g. 09:00:00 
		// @see http://www.cs.qub.ac.uk/itc2007/examtrack/exam_track_index_files/Inputformat.htm
		return Character.isDigit( s.charAt( 0 ) )
			&& Character.isDigit( s.charAt( 1 ) )
			&& s.charAt( 2 ) == ':'
			&& Character.isDigit( s.charAt( 3 ) )
			&& Character.isDigit( s.charAt( 4 ) )
			&& s.charAt( 5 ) == ':'			
			&& Character.isDigit( s.charAt( 6 ) )
			&& Character.isDigit( s.charAt( 7 ) );			
	}
	
	public static boolean isValidDateFormat( String s )
	{
		// e.g. 31:05:2005, 09:00:00
		// @see http://www.cs.qub.ac.uk/itc2007/examtrack/exam_track_index_files/Inputformat.htm

		StringTokenizer tokenizer = new StringTokenizer( s, ", " );
		if( !tokenizer.hasMoreElements() )
			return false;
		
		if( !isValidDate( tokenizer.nextToken() ) )
			return false;
		
		return isValidTime( tokenizer.nextToken() );
	}
	
	///////////////////////////////
	
	public Period( String date, int duration, int penalty )
	{
		if( !isValidDateFormat( date ) )
			throw new IllegalArgumentException();

		StringTokenizer tokenizer = new StringTokenizer( date, ", :" );
		int dayOfMonth = Integer.parseInt( tokenizer.nextToken() );
		int month = Integer.parseInt( tokenizer.nextToken() );
		int year = Integer.parseInt( tokenizer.nextToken() );
		
		int hours = Integer.parseInt( tokenizer.nextToken() );
		int minutes = Integer.parseInt( tokenizer.nextToken() );
		int seconds = Integer.parseInt( tokenizer.nextToken() );		

		GregorianCalendar calendar = new GregorianCalendar( 
				year, month, dayOfMonth, 
				hours, minutes, seconds );
		
		calendarTimeInMilliseconds = calendar.getTimeInMillis();

		this.durationInMilliseconds = duration * 60000;
		this.penalty = penalty;		
	}

	///////////////////////////////
	
	public int getDurationInMinutes() { return (int)( durationInMilliseconds / 60000 ); }
	public int getPenalty() { return penalty; }
	
	///////////////////////////////	
	
	public boolean equals( Object o )
	{
		if( !( o instanceof Period ) )
			return false;
		
		Period rhs = (Period)o;
		return calendarTimeInMilliseconds == rhs.calendarTimeInMilliseconds
			&& durationInMilliseconds == rhs.durationInMilliseconds 
			&& penalty == rhs.penalty;
	}
	
	public int hashCode()
	{
		return HashCode.apply( calendarTimeInMilliseconds, 
				durationInMilliseconds, penalty );
	}
	
	///////////////////////////////
	
	public String toString()
	{
		GregorianCalendar calendar = new GregorianCalendar();
		calendar.setTimeInMillis( calendarTimeInMilliseconds );
		int dayOfMonth = calendar.get( Calendar.DAY_OF_MONTH );		
		int month = calendar.get( Calendar.MONTH );
		int year = calendar.get( Calendar.YEAR );
		
		int hours = calendar.get( Calendar.HOUR );
		int minutes = calendar.get( Calendar.MINUTE );
		int seconds = calendar.get( Calendar.SECOND );		
		
		StringBuffer buffer = new StringBuffer();
		if( dayOfMonth < 10 )
			buffer.append( '0' );
		buffer.append( String.valueOf( dayOfMonth ) );
		buffer.append( ':' );		
		if( month < 10 )
			buffer.append( '0' );
		buffer.append( String.valueOf( month ) );		
		buffer.append( ':' );
		buffer.append( String.valueOf( year ) );		
		buffer.append( ", " );
		if( hours < 10 )
			buffer.append( '0' );
		buffer.append( String.valueOf( hours ) );
		buffer.append( ':' );
		if( minutes < 10 )
			buffer.append( '0' );
		buffer.append( String.valueOf( minutes ) );
		buffer.append( ':' );		
		if( seconds < 10 )
			buffer.append( '0' );
		buffer.append( String.valueOf( seconds ) );
		
		return buffer.toString();
	}
	
	///////////////////////////////
	
	public static boolean overlap( Period a, Period b )
	{
		ClosedOpenInterval< Long > ia = new ClosedOpenInterval< Long >( 
				a.calendarTimeInMilliseconds, 
				a.calendarTimeInMilliseconds + a.durationInMilliseconds ); 
		ClosedOpenInterval< Long > ib = new ClosedOpenInterval< Long >( 
				b.calendarTimeInMilliseconds, 
				b.calendarTimeInMilliseconds + b.durationInMilliseconds ); 
		
		return ia.overlaps( ib );
	}
	
	///////////////////////////////
	
	public static void main( String [] args )
	{
		Period p1 = new Period( "01:06:2005, 09:00:00", 10, 0 );
		Period p2 = new Period( "01:06:2005, 09:10:00", 10, 0 );
		
		System.out.println( "overlap( " + p1 + "," + p2 + "): " + overlap( p1, p2 ) );
	}
}

// End ///////////////////////////////////////////////////////////////

