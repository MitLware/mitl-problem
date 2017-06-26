package problemos.pending.timetabling;

import java.util.StringTokenizer;

import jeep.lang.Diag;
import jeep.lang.HashCode;

//////////////////////////////////////////////////////////////////////

public abstract class Constraint 
{
	/**
	 * Room hard constraints 
	 */
	
	public static final class RoomExclusive
	extends Constraint
	{
		private final int roomId;
		
		public RoomExclusive( int roomId )
		{
			this.roomId = roomId;
		}
		
		public boolean equals( Object o )
		{
			if( !( o instanceof RoomExclusive ) )
				return false;
			
			RoomExclusive rhs = (RoomExclusive)o;
			return roomId == rhs.roomId; 
		}
		
		public int hashCode() { return roomId; }

		@Override
		public boolean isViolated(Timetable timetable, Model model) {
			throw new UnsupportedOperationException("FIXME: Implement this"); // FIXME
		}
	}

	///////////////////////////////

	/**
	 * Period hard constraints 
	 */

	public static final class ExamCoincidence
	extends Constraint
	{
		private final int examId1;
		private final int examId2;
		
		public ExamCoincidence( int examId1, int examId2 )
		{
			this.examId1 = examId1;
			this.examId2 = examId2;
		}
		
		public boolean equals( Object o )
		{
			if( !( o instanceof ExamCoincidence ) )
				return false;
			
			ExamCoincidence rhs = (ExamCoincidence)o;
			return examId1 == rhs.examId1
				&& examId2 == rhs.examId2;
		}
	
		public int hashCode() { return HashCode.apply( examId1, examId2 ); }

		@Override
		public boolean isViolated(Timetable timetable, Model model) {
			throw new UnsupportedOperationException("FIXME: Implement this"); // FIXME
		}		
	}

	public static final class Exclusion
	extends Constraint
	{
		private final int examId1;
		private final int examId2;
		
		///////////////////////////
		
		public Exclusion( int examId1, int examId2 )
		{
			this.examId1 = examId1;
			this.examId2 = examId2;
		}
	
		///////////////////////////

		@Override
		public boolean isViolated( Timetable timetable, Model model ) 
		{
			for( int i=0; i<model.getNumExams(); ++i )
			{
				Period pi = model.getPeriod( timetable.getAssignment( i ).getPeriodId() );
				for( int j=i+1; j<model.getNumExams(); ++j )
				{
					Period pj = model.getPeriod( timetable.getAssignment( j ).getPeriodId() );
					if( Period.overlap( pi, pj ) )
						return true;
				}
			}
			
			return false;
		}		
		
		///////////////////////////
		
		public boolean equals( Object o )
		{
			if( !( o instanceof Exclusion ) )
				return false;
		
			Exclusion rhs = (Exclusion)o;
			return examId1 == rhs.examId1
				&& examId2 == rhs.examId2;
		}

		public int hashCode() { return HashCode.apply( examId1, examId2 ); }
	}

	public static final class After
	extends Constraint
	{
		private final int examId1;
		private final int examId2;
		
		public After( int examId1, int examId2 )
		{
			this.examId1 = examId1;
			this.examId2 = examId2;
		}
	
		public boolean equals( Object o )
		{
			if( !( o instanceof After ) )
				return false;
		
			After rhs = (After)o;
			return examId1 == rhs.examId1
				&& examId2 == rhs.examId2;
		}

		public int hashCode() { return HashCode.apply( examId1, examId2 ); }

		@Override
		public boolean isViolated(Timetable timetable, Model model) {
			throw new UnsupportedOperationException("FIXME: Implement this"); // FIXME
		}		
	}

	///////////////////////////////	
	
	/**
	 * Institutional model weightings
	 * Represents information on values given to 'global' soft constraints. 
	 */
	 
	public static final class TwoInARow
	extends Constraint 
	{	
		private final int penalty;
		
		public TwoInARow( int penalty ) 
		{
			this.penalty = penalty;
		}
		
		public boolean equals( Object o )
		{
			if( !( o instanceof TwoInARow ) )
				return false;
			
			TwoInARow rhs = (TwoInARow)o;
			return penalty == rhs.penalty; 
		}
		
		public int hashCode() { return penalty; }

		@Override
		public boolean isViolated(Timetable timetable, Model model) {
			throw new UnsupportedOperationException("FIXME: Implement this"); // FIXME
		}
	};

	public static final class TwoInADay
	extends Constraint 
	{
		private final int penalty;
		
		public TwoInADay( int penalty ) 
		{
			this.penalty = penalty;
		}
		
		public boolean equals( Object o )
		{
			if( !( o instanceof TwoInADay ) )
				return false;
			
			TwoInADay rhs = (TwoInADay)o;
			return penalty == rhs.penalty; 
		}
		
		public int hashCode() { return penalty; }

		@Override
		public boolean isViolated(Timetable timetable, Model model) {
			throw new UnsupportedOperationException("FIXME: Implement this"); // FIXME
		}
	};

	public static final class PeriodSpread
	extends Constraint 
	{	
		private final int penalty;
		
		public PeriodSpread( int penalty ) 
		{
			this.penalty = penalty;
		}
		
		public boolean equals( Object o )
		{
			if( !( o instanceof PeriodSpread ) )
				return false;
			
			PeriodSpread rhs = (PeriodSpread)o;
			return penalty == rhs.penalty; 
		}
		
		public int hashCode() { return penalty; }

		@Override
		public boolean isViolated(Timetable timetable, Model model) {
			throw new UnsupportedOperationException("FIXME: Implement this"); // FIXME
		}
	};

	public static final class NonMixedDurations
	extends Constraint 
	{	
		private final int penalty;
		
		public NonMixedDurations( int penalty ) 
		{
			this.penalty = penalty;
		}
		
		public boolean equals( Object o )
		{
			if( !( o instanceof NonMixedDurations ) )
				return false;
			
			NonMixedDurations rhs = (NonMixedDurations)o;
			return penalty == rhs.penalty; 
		}
		
		public int hashCode() { return penalty; }

		@Override
		public boolean isViolated(Timetable timetable, Model model) {
			throw new UnsupportedOperationException("FIXME: Implement this"); // FIXME
		}
	};

	public static final class FrontLoad
	extends Constraint 
	{	
		private final int numLargestExams; 
		private final int numLastPeriods;
		private final int penalty;
	
		public FrontLoad( int numLargestExams, int numLastPeriods, int penalty )
		{
			this.numLargestExams = numLargestExams; 
			this.numLastPeriods = numLastPeriods;
			this.penalty = penalty;
		}
		
		public boolean equals( Object o )
		{
			if( !( o instanceof FrontLoad ) )
				return false;
		
			FrontLoad rhs = (FrontLoad)o;
			return numLargestExams == rhs.numLargestExams
				&& numLastPeriods == rhs.numLastPeriods
				&& penalty == rhs.penalty; 
		}

		public int hashCode() { return HashCode.apply( numLargestExams, numLastPeriods, penalty ); }

		@Override
		public boolean isViolated(Timetable timetable, Model model) {
			throw new UnsupportedOperationException("FIXME: Implement this"); // FIXME
		}		
	};

	///////////////////////////////

	public static Constraint parseRoomHardConstraint( String s )
	{
		StringTokenizer tokenizer = new StringTokenizer( s, ", " );
		if( !tokenizer.hasMoreElements() )
			throw new IllegalArgumentException();
		
		String token = tokenizer.nextToken();
		if( token.equals( "ROOM_EXCLUSIVE" ) )
		{
			return new RoomExclusive( Integer.parseInt( tokenizer.nextToken() ) );			
		}
		else
		{
			throw new IllegalArgumentException();			
		}
	}

	///////////////////////////////
	
	public static Constraint parsePeriodHardConstraint( String s )
	{
		StringTokenizer tokenizer = new StringTokenizer( s, ", " );
		if( !tokenizer.hasMoreElements() )
			throw new IllegalArgumentException( "Period hard constraint expected, found <<" + s + ">>" );
		
		String token0 = tokenizer.nextToken();
		String token1 = tokenizer.nextToken();
		String token2 = tokenizer.nextToken();		
		
		if( token1.equals( "EXAM_COINCIDENCE" ) )
		{
			return new ExamCoincidence( 
					Integer.parseInt( token0 ), 
					Integer.parseInt( token2 ) );			
		}
		else if( token1.equals( "EXCLUSION" ) )
		{
			return new Exclusion( 
					Integer.parseInt( token0 ), 
					Integer.parseInt( token2 ) );			
			
		}
		else if( token1.equals( "AFTER" ) )
		{
			return new After( 
					Integer.parseInt( token0 ), 
					Integer.parseInt( token2 ) );			
		
		}
		else
		{
			throw new IllegalArgumentException();			
		}
	}
	
	public static Constraint parseInstutionalModelWeighting( String s )
	{
		StringTokenizer tokenizer = new StringTokenizer( s, ", " );
		if( !tokenizer.hasMoreElements() )
			throw new IllegalArgumentException();
		
		String token = tokenizer.nextToken();
		
		if( token.equals( "TWOINAROW" ) )
		{
			return new TwoInARow( Integer.parseInt( tokenizer.nextToken() ) );
		}
		else if( token.equals( "TWOINADAY" ) )
		{
			return new TwoInADay( Integer.parseInt( tokenizer.nextToken() ) );
		}
		else if( token.equals( "PERIODSPREAD" ) )
		{
			return new PeriodSpread( Integer.parseInt( tokenizer.nextToken() ) );
		}
		else if( token.equals( "NONMIXEDDURATIONS" ) )
		{
			return new NonMixedDurations( Integer.parseInt( tokenizer.nextToken() ) );
		}
		else if( token.equals( "FRONTLOAD" ) )
		{
			final int numLargestExams = Integer.parseInt( tokenizer.nextToken() ); 
			final int numLastPeriods = Integer.parseInt( tokenizer.nextToken() );
			final int penalty = Integer.parseInt( tokenizer.nextToken() );
			
			return new FrontLoad( 
					numLargestExams, 
					numLastPeriods, 
					penalty );			
		}
		else
		{
			throw new IllegalArgumentException();
		}
	}
	
	///////////////////////////////
	
	public abstract boolean isViolated( Timetable timetable, Model model );

	public String toString() { return getClass().getSimpleName(); }
	
	///////////////////////////////	
} 

// End ///////////////////////////////////////////////////////////////
