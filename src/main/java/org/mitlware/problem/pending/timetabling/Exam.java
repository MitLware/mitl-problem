package org.mitlware.problem.pending.timetabling;

import java.util.BitSet;

import jeep.lang.HashCode;

public final class Exam 
{
	private final int	duration;
	private BitSet		studentIds;	
	
	///////////////////////////////
	
	public Exam( int duration, BitSet studentIds )
	{ 
		this.duration = duration;
		this.studentIds = (BitSet)studentIds.clone();		
	}
	
	///////////////////////////////
	
	public int getDuration() { return duration; }	
	
	public int hashCode() 
	{ 
		return HashCode.apply( duration, studentIds.hashCode() ); 
	}
	
	public boolean equals( Object o )
	{
		if( !( o instanceof Exam ) )
			return false;
		
		Exam rhs = (Exam)o;
		return duration == rhs.duration 
			&& studentIds.equals( rhs.studentIds );
	}
}

// End ///////////////////////////////////////////////////////////////

