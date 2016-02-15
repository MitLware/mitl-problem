package problemos.pending.sat;

import java.util.ArrayList;
import java.util.List;

import jeep.util.Arrays;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.mitlware.SearchDirection;
import org.mitlware.mutable.Evaluate;

import statelet.bitvector.BitVector;

//////////////////////////////////////////////////////////////////////

public final class CNF {
	
	/**
	 * In this implementation, the fitness is the number of unsatisfied clauses,
	 * so the optima for every instance will have fitness 0
	 */
	public static final class MAXSAT
	extends Evaluate.Directional< BitVector, Double > {

		private CNF impl;
		
		///////////////////////////
		
		public MAXSAT( CNF impl ) {
			super( SearchDirection.MINIMIZING );
			this.impl = impl;
		}
		
		@Override
		public Double apply( BitVector x ) {
			return (double)impl.numUnsatisfiedClauses( x );
		}
	}
	
///////////////////////////
	
	public static final class Clause {
		
		private int [] impl;
		
		///////////////////////////
		
		public Clause( int [] clause ) {
			if( !isValidClause( clause )  )
				throw new IllegalArgumentException("Invalid clause: " + java.util.Arrays.toString(clause));
			
			impl = new int [ clause.length ];
			System.arraycopy( clause, 0, impl, 0, clause.length );
		}
		
		public int size() { return impl.length; }
		public int get( int i ) { return impl[ i ]; }
		
		///////////////////////////
		
		public boolean isSatisfied( BitVector assignments ) {
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
		
		public static boolean isValidClause( int [] value ) {
			for( int i=0; i<value.length; ++i )
			{
				if( value[ i ] == 0 )
					return false;
				
				/* contradictions are allowed (frequently appear in known unsatisfiable instances) - this has moved below
				for( int j=i+1; j<value.length; ++j )
					if( value[ j ] == -value[ i ] )
						return false;
				*/
			}
			
			return true;
		}
		
		/**@return true iff this contains a variable and its negation*/
		public boolean isContradiction() {
			for( int i=0; i<impl.length; ++i )
				for( int j=i+1; j<impl.length; ++j )
					if( impl[ j ] == -impl[ i ] )
						return true;
			
			return false;
		}
		
		/**has at most one positive variable*/
		public boolean isHornClause() {
			int positiveCount = 0;
			for( int i=0; (positiveCount<=1) && (i<impl.length); ++i ) {
				if (impl[i] > 0) {
					positiveCount++;
				}
			}
			
			return positiveCount <= 1;
		}
		
		///////////////////////////
		
		@Override		
		public int hashCode() {
			return HashCodeBuilder.reflectionHashCode( this );
		}

		@Override
		public boolean equals( Object other ) {
			return EqualsBuilder.reflectionEquals( this, other );
		}
		
		@Override
		public String toString() {
			StringBuffer sb = new StringBuffer();
			for( int i=0; i<impl.length; ++i ) {
				if( impl[ i ] > 0 )
					sb.append( "v" + impl[ i ] );					
				else
					sb.append( "!v" + Math.abs( impl[ i ] ) );					

				if( i < impl.length - 1 )
					sb.append( " || " );
			}
			
			return sb.toString();			
		}
	}

	///////////////////////////////
	
	private final int		numVariables;
	private List< Clause >	clauses;
	
	///////////////////////////////
	
	public CNF( int numVariables, List< Clause > clauses ) {
		this.clauses = new ArrayList< Clause >( clauses );
		this.numVariables = numVariables;
	}

	///////////////////////////////
	
	public int getNumVariables() { return numVariables; }	
	public int getNumClauses() { return clauses.size(); }
	public Clause getClause( int i ) { return clauses.get( i ); }
	
	///////////////////////////////
	
	public boolean isSatisfied( BitVector assignments ) {
		if( assignments.length() != getNumVariables() )
			throw new IllegalArgumentException();
		
		for( int i=0; i<clauses.size(); ++i )
			if( !clauses.get( i ).isSatisfied( assignments ) )
				return false;
		
		return true;
	}
	
	public int numUnsatisfiedClauses( BitVector assignments ) {
		
		if( assignments.length() != getNumVariables() )
			throw new IllegalArgumentException();
		
		int result = 0;
		for( int i=0; i<clauses.size(); ++i )
			if( !clauses.get( i ).isSatisfied( assignments ) )
				++result;
		
		return result;
	}
	
	///////////////////////////////	

	@Override		
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode( this );
	}

	@Override
	public boolean equals( Object other ) {
		return EqualsBuilder.reflectionEquals( this, other );
	}
	
	@Override
	public String toString() {
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
