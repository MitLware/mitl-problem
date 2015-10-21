package problemos.magicsquare;

import java.util.Arrays;
import java.util.BitSet;

//////////////////////////////////////////////////////////////////////

public final class MagicSquareState {
	
	private	int []	square_;
	
	private static void swap( int [] array, int i, int j ) {
		final int temp = array[ i ];
		array[ i ] = array[ j ];
		array[ j ] = temp;
	}
	
	///////////////////////////////
	
	public MagicSquareState( int order ) {
		if( order <= 0 )
			throw new IllegalArgumentException();

		square_ = new int [ order * order ];
		for( int i=0; i<order * order; ++i )
			square_[ i ] = i+1;

		assert( invariant() );
	}

	public MagicSquareState( int [] square )	{
		if( square.length == 0 || !isPerfectSquare( square.length ) )
			throw new IllegalArgumentException();
		if( !isPermutation( square ) )
			throw new IllegalArgumentException();

		square_ = square.clone();
		
		assert( invariant() );
	}

	public MagicSquareState( MagicSquareState rhs ) {
		square_ = rhs.square_.clone();
		assert( invariant() );
	}
	
	//////////////////////////////////
	
	public int order() { 
		return (int)Math.floor( Math.sqrt( square_.length ) ); 
	}
	
	public int getMagicNumber() {
		return (order()*(order()*order()+1))/2;
	}

	public int getElementAtIndex( int index ) {
		assert( 0 <= index && index < square_.length );
		return square_[ index ];		
	}
	
	public int getElement( int row, int col ) {
		assert( 0 <= row && row < order() );
		assert( 0 <= col && col < order() );
		return square_[ col + ( row * order() ) ];
	}
	
	public int getIndex( int row, int col ) {
		assert( 0 <= row && row < order() );
		assert( 0 <= col && col < order() );
		return col + ( row * order() );
	}
	
	public int getSumRow(int row) {
		assert( 0 <= row && row < order() );
		int sum = 0;
		for (int i=0; i<order(); ++i) {
			sum += getElement(row, i);
		}
		return sum;
	}
	
	public int getSumCol(int col) {
		assert( 0 <= col && col < order() );
		int sum = 0;
		for (int i=0; i<order(); ++i) {
			sum += getElement(i, col);
		}
		return sum;
	}
	
	public int getSumDiag1() {
		int sum = 0;
		for (int i=0; i<order(); ++i) {
			sum += getElement(i, i);
		}
		return sum;
	}
	
	public int getSumDiag2() {
		int sum = 0;
		for (int i=order()-1; i>=0; --i) {
			sum += getElement(order()-1-i, i);
		}
		return sum;
	}
	
	public void transpose( int i, int j ) {
		assert( invariant() );
		assert( 0 <= i && i < square_.length );
		assert( 0 <= j && j < square_.length );
		swap(square_,i,j);
		assert( invariant() );
	}
	
	//////////////////////////////////

	@Override
	public int hashCode() {
		return Arrays.hashCode( square_ );
	}

	@Override
	public boolean equals( Object o ) {
		if( !( o instanceof MagicSquareState ) )
			return false;
		
		MagicSquareState rhs = (MagicSquareState)o;
		return Arrays.equals( square_, rhs.square_ );
	}

	@Override
	public MagicSquareState clone() {
		return new MagicSquareState( this );
	}
	
	@Override
	public String toString() {
		
		StringBuffer s = new StringBuffer();
	    for( int r=0; r<order(); ++r ) {
	        for( int c=0; c<order(); ++c ) {
            	s.append( getElement( r, c ) );
            	if (c != order()-1)
            		s.append( ' ' );
	        }
	        if (r != order()-1)
	        	s.append( '\n' );
	    }

	    return s.toString();
	}
	
	public boolean invariant() {
		return order() * order() == square_.length
			&& isPermutation( square_ );
	}

	///////////////////////////////
	
	private static boolean isPerfectSquare( int x ) {
		final int floorOfSqrt = (int)Math.floor( Math.sqrt( x ) ); 
		return floorOfSqrt * floorOfSqrt == x;
	}
	
	private static boolean isPermutation( int ... perm ) {
		
		BitSet check = new BitSet( perm.length );
		for( int i=0; i<perm.length; ++i ) {
			if( perm[ i ] < 1 || perm[ i ] > perm.length )
				return false;
			
			check.set( perm[ i ] - 1 );
		}
		return check.cardinality() == perm.length;
	}
}

// End ///////////////////////////////////////////////////////////////
