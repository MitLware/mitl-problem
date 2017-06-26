package problemos.pending.timetabling;

import jeep.lang.HashCode;

public final class Room 
{
	private final int capacity;
	private final int penalty;	
	
	///////////////////////////////
	
	public Room( int capacity, int penalty )
	{
		this.capacity = capacity;
		this.penalty = penalty;
	}
	
	///////////////////////////////
	
	public int getCapacity() { return capacity; }
	public int getPenalty() { return penalty; }
	
	///////////////////////////////
	
	public boolean equals( Object o )
	{
		if( !( o instanceof Room ) )
			return false;
		
		Room rhs = (Room)o;
		return capacity == rhs.capacity 
			&& penalty == rhs.penalty;
	}
	
	public int hashCode() 
	{ 
		return HashCode.apply( capacity, penalty ); 
	}
}

// End ///////////////////////////////////////////////////////////////
