package problemos.sat;

import java.util.ArrayList;
import java.util.List;

import statelet.bitvector.BitVector;

//////////////////////////////////////////////////////////////////////

public final class CNF
{
	public static final class Clause
	{
		private int [] impl;
		
		///////////////////////////
		
		public Clause( int [] clause )
		{
			if( !isValidClause( clause )  )
				throw new IllegalArgumentException();
			
			impl = new int [ clause.length ];
			System.arraycopy( clause, 0, impl, 0, clause.length );
		}
		
		public int size() { return impl.length; }
		public int get( int i ) { return impl[ i ]; }
		
		///////////////////////////
		
		public boolean isSatisfied( BitVector assignments )
		{
			boolean result = false;
			for( int i=0; i<impl.length; ++i )
			{
				assert impl[ i ] != 0;
				boolean value = impl[ i ] > 0 
					? assignments.get( impl[ i ] - 1 )
					: !assignments.get( Math.abs( impl[ i ] ) - 1 );
				result = result || value;
			}
				
			return result;
		}
		
		public boolean isValidClause( int [] value )
		{
			for( int i=0; i<value.length; ++i )
			{
				if( value[ i ] == 0 )
					return false;
				
				for( int j=i+1; j<value.length; ++j )
					if( value[ j ] == -value[ i ] )
						return false;
			}
			
			return true;
		}
		
		public String toString()
		{
			StringBuffer sb = new StringBuffer();
			for( int i=0; i<impl.length; ++i )
			{
				if( impl[ i ] > 0 )
				{
					sb.append( "v" + impl[ i ] );					
				}
				else
				{
					sb.append( "!v" + Math.abs( impl[ i ] ) );					
				}
				if( i < impl.length - 1 )
					sb.append( " || " );
			}
			
			return sb.toString();			
		}
	}

	///////////////////////////////
	
	private final int			numVariables;
	private ArrayList< Clause >	clauses;
	
	///////////////////////////////
	
	public CNF( int numVariables, List< Clause > clauses )
	{
		this.clauses = new ArrayList< Clause >( clauses );
		this.numVariables = numVariables;
	}

	///////////////////////////////
	
	public int getNumVariables() { return numVariables; }	
	public int getNumClauses() { return clauses.size(); }
	public Clause getClause( int i ) { return clauses.get( i ); }
	
	///////////////////////////////
	
	public boolean isSatisfied( BitVector assignments )
	{
		if( assignments.length() != getNumVariables() )
			throw new IllegalArgumentException();
		
		for( int i=0; i<clauses.size(); ++i )
			if( !clauses.get( i ).isSatisfied( assignments ) )
				return false;
		
		return true;
	}
	
	public int numUnsatisfiedClauses( BitVector assignments )
	{
		if( assignments.length() != getNumVariables() )
			throw new IllegalArgumentException();
		
		int result = 0;
		for( int i=0; i<clauses.size(); ++i )
			if( !clauses.get( i ).isSatisfied( assignments ) )
				++result;
		
		return result;
	}
	
	///////////////////////////////	
	
	public String toString()
	{
		StringBuffer sb = new StringBuffer(); 
		for( int i=0; i<clauses.size(); ++i )
		{
			sb.append( "(" );			
			sb.append( clauses.get( i ) );
			sb.append( ")" );			
			if( i < clauses.size() - 1 )
				sb.append( " && " );
		}
		
		return sb.toString();
	}
}

// End ///////////////////////////////////////////////////////////////
