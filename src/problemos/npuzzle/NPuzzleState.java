package problemos.npuzzle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;

//////////////////////////////////////////////////////////////////////

final class NPuzzleState {
	
	enum Move { left, right, up, down };

	private	int []	state_;

	private int getIndex( int row, int col ) 
	{
		assert( 0 <= row && row < size() );
		assert( 0 <= col && col < size() );
		return col + ( row * size() );
	}

	private static void swap( int [] array, int i, int j ) {
		final int temp = array[ i ];
		array[ i ] = array[ j ];
		array[ j ] = temp;
	}

	///////////////////////////////
	
	public static final class RowAndCol {
		final int row;
		final int col;	
		
		RowAndCol( int row, int col ) {
			this.row = row;
			this.col = col;		
		}
	}
	
	///////////////////////////////	

	public NPuzzleState( int size )
	{
		if( size <= 0 )
			throw new IllegalArgumentException();

		state_ = new int [ size * size ];
		for( int i=0; i<size * size; ++i )
			state_[ i ] = i;

		assert( isIdentityPermutation( state_ ) );
		assert( invariant() );
	}

	public NPuzzleState( int [] state )
	{
		if( state.length == 0 || !is_perfect_square( state.length ) )
			throw new IllegalArgumentException();
		if( !isPermutation( state ) )
			throw new IllegalArgumentException();

		state_ = state.clone();
		
		assert( invariant() );
	}

	public NPuzzleState( NPuzzleState rhs ) {
		state_ = rhs.state_.clone();
		assert( invariant() );
	}
	
	private NPuzzleState( NPuzzleState parent, Move op )
	{
		assert( parent.is_valid_move( op ) );
		
		state_ = parent.state_.clone();
		

		// int blank_row, blank_column;
		// boost::tie( blank_row, blank_column ) = get_blank_coords();
		RowAndCol blankRowAndCol = get_blank_coords();

	    if( op == Move.up )
		{
			// std::swap( get( blank_row - 1, blank_column ), get( blank_row, blank_column ) );
	    	swap( state_, getIndex( blankRowAndCol.row - 1, blankRowAndCol.col ), 
	    			getIndex( blankRowAndCol.row, blankRowAndCol.col ) );	    	
			// --blank_row;
		}
		else if( op == Move.down )
		{
			// std::swap( get( blank_row + 1, blank_column ), get( blank_row, blank_column ) );
			swap( state_, getIndex( blankRowAndCol.row + 1, blankRowAndCol.col ), 
					getIndex( blankRowAndCol.row, blankRowAndCol.col ) );			
			// ++blank_row;
		}
		else if( op == Move.right )
		{
			// std::swap( get( blank_row, blank_column + 1 ), get( blank_row, blank_column ) );
			swap( state_, getIndex( blankRowAndCol.row, blankRowAndCol.col + 1 ), 
					getIndex( blankRowAndCol.row, blankRowAndCol.col ) );			
			// ++blank_column;
		}
		else if( op == Move.left )
		{
			// std::swap( get( blank_row, blank_column - 1 ), get( blank_row, blank_column ) );
			swap( state_, getIndex( blankRowAndCol.row, blankRowAndCol.col - 1 ), 
					getIndex( blankRowAndCol.row, blankRowAndCol.col ) );
			// --blank_column;
		}
	    
		assert( invariant() );	    
	}

	//////////////////////////////////
	
	public List< NPuzzleState > neighbors() {
		List< Move > moves = valid_moves( this );
		List< NPuzzleState > result = new ArrayList< NPuzzleState >();
		for( Move m : moves )
			result.add( new NPuzzleState( this, m )) ;
		
		return result;
	}
	
	//////////////////////////////////	

	public int size() { return (int)Math.floor( Math.sqrt( state_.length ) ); }

	public int getTileAtIndex( int index ) {
		return state_[ index ];		
	}
	
	public int get_tile( int row, int col )
	{
		assert( 0 <= row && row < size() );
		assert( 0 <= col && col < size() );
		return state_[ col + ( row * size() ) ];
	}

	private RowAndCol get_blank_coords()
	{
		return get_tile_coords( 0 );
	}

	private RowAndCol get_tile_coords( int value )
	{
		final int index = ArrayUtils.indexOf( state_, value );
		assert( index != -1 );
		return new RowAndCol( index / size(), index % size() );
	}

	private boolean is_valid_move( Move op )
	{
		RowAndCol rowAndCol = get_blank_coords();

		if( op == Move.up )
			return rowAndCol.row > 0;
		else if( op == Move.down )
			return rowAndCol.row < size() - 1;
		else if( op == Move.left )
			return rowAndCol.col > 0;
		else if( op == Move.right )
			return rowAndCol.col < size() - 1;
		else
			throw new IllegalArgumentException();
	}

	private static List< Move > 
	valid_moves( NPuzzleState x )
	{
		List< Move > result = new ArrayList< Move >();

		RowAndCol rowAndCol = x.get_blank_coords();

		if( rowAndCol.row > 0 )
			result.add( Move.up );

		if( rowAndCol.row < x.size() - 1 )
			result.add( Move.down );

		if( rowAndCol.col > 0 )
			result.add( Move.left );

		if( rowAndCol.col < x.size() - 1 )
			result.add( Move.right );
			
		return result;
	}		
	
	//////////////////////////////////

	@Override
	public int hashCode() {
		return Arrays.hashCode( state_ );
	}

	@Override
	public boolean equals( Object o ) {
		if( !( o instanceof NPuzzleState ) )
			return false;
		
		NPuzzleState rhs = (NPuzzleState)o;
		return Arrays.equals( state_, rhs.state_ );
	}

	@Override
	public NPuzzleState clone() {
		return new NPuzzleState( this );
	}
	
	@Override
	public String toString()
	{
		StringBuffer s = new StringBuffer();
		s.append( '[' );
		RowAndCol blankRowAndCol = get_blank_coords();
	    for( int r=0; r<size(); ++r )    
	    {
	    	s.append( '[' );

	        for( int c=0; c<size(); ++c )        
	        {
	            if( c == blankRowAndCol.col && r == blankRowAndCol.row )
	            	s.append( '*' );
	            else
	            	s.append( get_tile( r, c ) );
	        }
	        
	        s.append( ']' );
	    }

	    return s.append( ']').toString();
	}
	
	public boolean invariant()
	{
		return size() * size() == state_.length
			&& isPermutation( state_ );
	}

	private static boolean is_perfect_square( int x ) {
		final int floorOfSqrt = (int)Math.floor( Math.sqrt( x ) ); 
		return floorOfSqrt * floorOfSqrt == x;
	}
	
	private static boolean isIdentityPermutation( int [] x )
	{
		for( int i=0; i<x.length; ++i )
			if( x[ i ] != i )
				return false;

		return true;
	}
	
	public static boolean isPermutation( int ... perm ) {
		
		BitSet check = new BitSet( perm.length );
		for( int i=0; i<perm.length; ++i ) {
			if( perm[ i ] < 0 || perm[ i ] >= perm.length )
				return false;
			
			check.set( perm[ i ] );
		}
		return check.cardinality() == perm.length;
	}
}

// End ///////////////////////////////////////////////////////////////
