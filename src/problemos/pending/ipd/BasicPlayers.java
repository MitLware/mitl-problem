package problemos.pending.ipd;

import java.util.List;
import java.util.Random;

import statelet.bitvector.BitVector;

public class BasicPlayers {

	public static final class AllC implements Player {
		
		@Override
		public Move move(List<Move> history, List<Move> opponentHistory, Random random ) {
			return Move.C;
		}
	}

	///////////////////////////////
	
	public static final class AllD implements Player {
		
		@Override
		public Move move(List<Move> history, List<Move> opponentHistory, Random random) {
			return Move.D;
		}
	}
	
	///////////////////////////////
	
	public static final class Rand implements Player {
		
		@Override
		public Move move(List<Move> history, List<Move> opponentHistory, Random random) {
			return random.nextBoolean() ? Move.D : Move.C;
		}
	}
	
	///////////////////////////////
	
	public static final class TFT implements Player {
		
		@Override
		public Move move(List<Move> history, List<Move> opponentHistory, Random random) {
			if( opponentHistory.isEmpty() )
				return Move.C;
			else
				return opponentHistory.get( opponentHistory.size() - 1 );				
		}
	}
	
	
	///////////////////////////////
	
	public static final class AxelrodBinaryEncodedPlayer implements Player {
		
		private final BitVector actionFunction;
		
		///////////////////////////
		
		// r rounds requires a string of length l=4^r
		
		private static boolean isPowerOf4( int n ) {
			return n >= 4 && (~ (n & (n-1)) & (n & 0x55555554)) == n; 
		}
		
		public static AxelrodBinaryEncodedPlayer 
		noPhantomMemory( BitVector actionFunction ) {
			if( !isPowerOf4( actionFunction.length() ) )
				throw new IllegalArgumentException();

			return new AxelrodBinaryEncodedPlayer( actionFunction );
		}

		private AxelrodBinaryEncodedPlayer( BitVector actionFunction ) {
			this.actionFunction = actionFunction;
		}
		
		///////////////////////////
		
		private static int index( List<Move> history, List<Move> opponentHistory ) {
			assert( history.size() == opponentHistory.size() );
			
			final int numRounds = history.size();
			
			int q = 0;
			for( int i=1; i<=numRounds; ++i ) {
				final int own = history.get( numRounds - i ).toBoolean() ? 1 : 0;
				final int other = opponentHistory.get( numRounds - i ).toBoolean() ? 1 : 0;
				// System.out.println( 4 << ( numRounds - i ) );
				q += (int)Math.pow( 4, numRounds - i ) * ( own + 2 * other );
			}
			
			return q;
		}	
		
		@Override
		public Move move(List<Move> history, List<Move> opponentHistory,
				Random random) {

			final int index = index( history, opponentHistory ) ;
			return Move.fromBoolean( actionFunction.get( index % actionFunction.length() ) );
		}
	}
}

// End ///////////////////////////////////////////////////////////////
