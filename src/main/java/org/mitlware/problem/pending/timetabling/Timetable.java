package org.mitlware.problem.pending.timetabling;

import java.util.Map;
import java.util.Set;

import java.util.List;

import jeep.lang.HashCode;
import jeep.lang.PubliclyCloneable;

//////////////////////////////////////////////////////////////////////

public final class Timetable implements PubliclyCloneable< Timetable >
{
	private Model model;
	private List< Assignment > assignments;
	
	///////////////////////////////
	
	public static final class Assignment
	{
		private final int roomId;
		private final int periodId;

		///////////////////////////////
		
		public Assignment( int roomId, int periodId )
		{
			this.roomId = roomId;
			this.periodId = periodId;
		}

		///////////////////////////////
		
		public int getRoomId() { return roomId;	}
		public int getPeriodId() { return periodId;	}
		
		///////////////////////////////
		
		public boolean equals( Object o )
		{
			if( !( o instanceof Assignment ) )
				return false;
			
			Assignment rhs = (Assignment)o;
			return roomId == rhs.roomId
				&& periodId == rhs.periodId;
		}
		
		public int hashCode()
		{
			return HashCode.apply( roomId, periodId );
		}
	}
	
	///////////////////////////////
	
	public Timetable( Model model )
	{
		this.model = model;
	}
	
	public Timetable( Timetable rhs )
	{
		throw new InternalError();
	}
	
	///////////////////////////////
	
	public int getNumExams() { return model.getNumExams(); }
	
	public Assignment getAssignment( int examId )
	{
		return assignments.get( examId );
	}
	
	/***
	public void setAssignment( int examId, int roomId, int periodId )
	{
		assignments.put( examId, new Assignment( roomId, periodId ) );
	}
	***/
	
	///////////////////////////////
	
	public Timetable clone()
	{
		return new Timetable( this );
	}
	
	public boolean meetsHardConstraints()
	{
		return numRoomCollisions() == 0;
	}
	
	///////////////////////////////
	
	public int numRoomCollisions()
	{
		int result = 0;
		for( int i=0; i<assignments.size(); ++i )
		{
			for( int j=i+1; j<assignments.size(); ++j )
			{
				if( assignments.get( i ).getRoomId() == assignments.get( j ).getRoomId() )
				{
					final int period1 = assignments.get( i ).getPeriodId(); 
					final int period2 = assignments.get( j ).getPeriodId();					
					if( Period.overlap( model.getPeriod( period1
						 ), model.getPeriod( period2 ) ) )
						++result;
				}
			}
		}

		return result;
	}
	
	///////////////////////////////
	
	public boolean equals( Object o )
	{
		if( !( o instanceof Timetable ) )
			return false;
		
		Timetable rhs = (Timetable)o;
		
		return model.equals( rhs.model )
			&& assignments.equals( rhs.assignments );
	}
	
	public int hashCode()
	{
		return HashCode.apply( model.hashCode(), assignments.hashCode() );
	}
}

// End ///////////////////////////////////////////////////////////////

