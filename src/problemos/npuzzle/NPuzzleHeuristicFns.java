package problemos.npuzzle;

import heu4j.Evaluate;

public final class NPuzzleHeuristicFns {

	public static final class HammingDistance 
	implements Evaluate< NPuzzleState, Integer > {

		private final NPuzzleState target;

		///////////////////////////
		
		public HammingDistance( NPuzzleState target ) {
			this.target = target;
		}
		
		///////////////////////////
		
		@Override
		public Integer apply(NPuzzleState s) {
			if( s.size() != target.size() )
				throw new IllegalArgumentException();
			
			int result = 0;
			for( int i=0; i<s.size(); ++i )
				if( s.getTileAtIndex( i ) != target.getTileAtIndex( i ) )
					++result;
			
			return result;
		}
	};
	
	///////////////////////////////

	public static final class ManhattanDistance 
	implements Evaluate< NPuzzleState, Integer > {

		private final NPuzzleState target;
		private final NPuzzleState.RowAndCol [] targetCoords;
		
		///////////////////////////
		
		public ManhattanDistance( NPuzzleState target ) {
			this.target = target;
			targetCoords = mapPermToCoords( target );
		}
		
		///////////////////////////
		
		@Override
		public Integer apply(NPuzzleState s) {
			if( s.size() != target.size() )
				throw new IllegalArgumentException();
			
	        NPuzzleState.RowAndCol [] sCoords = mapPermToCoords( s );
	        
	        int sum = 0;
	        // do not count the blank square.
	        for( int i=1; i<sCoords.length; ++i )
	        {
	            sum += Math.abs( sCoords[i].row - targetCoords[i].row );
	            sum += Math.abs( sCoords[i].col - targetCoords[i].col );
	        }

	        return sum;
	    }

		///////////////////////////
		
		private static NPuzzleState.RowAndCol [] mapPermToCoords( NPuzzleState s ) {
			final int size = s.size();
			NPuzzleState.RowAndCol [] result = new NPuzzleState.RowAndCol [ size * size ];
			for( int row = 0; row<size; ++row )
				for( int col = 0; col<size; ++col )
					result[ s.get_tile( row, col ) ] = new NPuzzleState.RowAndCol( row, col );
		
			return result;
		}
	}
}

// End ///////////////////////////////////////////////////////////////

