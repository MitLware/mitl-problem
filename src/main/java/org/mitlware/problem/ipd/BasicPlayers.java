package org.mitlware.problem.ipd;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.mitlware.solution.bitvector.BitVector;

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
	
	/**copies last move of opponent. If no history, defaults to C*/
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
		private final int numberOfRoundsOfHistoryRequired;
		private final List<Move> phantomHistory;
		private final List<Move> phantomOpponentHistory;
		
		///////////////////////////
		
		// r rounds requires a string of length l=4^r
		
		private static boolean isPowerOf4( int n ) {
			return n >= 4 && (~ (n & (n-1)) & (n & 0x55555554)) == n; 
		}
		
		private static int logBase4( int v ) {
			int r = 0;
			while ((v >>= 2)!=0) {
				r++;
			}
			return r;
		}
		
		public static AxelrodBinaryEncodedPlayer 
		noPhantomMemory( BitVector actionFunction ) {
			if( !isPowerOf4( actionFunction.length() ) )
				throw new IllegalArgumentException("BitVector must be 4^r bits long, but was actually " + actionFunction.length() + " bits");

			return new AxelrodBinaryEncodedPlayer( actionFunction, Collections.emptyList(), Collections.emptyList() );
		}
		
		/**
		 * solution has 4^r bits for the strategy based on the history
		 * and 2r bits for the phantom memory, so overall we have 4^r+2r bits
		 * so: number of rounds is floor(log_4(bits))
		 */
		public static AxelrodBinaryEncodedPlayer 
		phantomMemory( BitVector actionFunction ) {
			//final int rounds = (int)( Math.floor(Math.log(actionFunction.length() / Math.log(4))) );
			final int rounds = logBase4(actionFunction.length());
			
			if( !isPowerOf4( actionFunction.length() - (rounds * 2) ) )
				throw new IllegalArgumentException("BitVector must be 4^r+2r bits long, but was actually " + actionFunction.length() + " bits");

			// extract histories
			List<Move> phantomHistory = new ArrayList<>();
			List<Move> phantomOpponentHistory = new ArrayList<>();
			for (int i = 0; i < (rounds * 2); i+=2) {
				phantomHistory.add(Move.fromBoolean(actionFunction.get(i)));
				phantomOpponentHistory.add(Move.fromBoolean(actionFunction.get(i + 1)));
			}
			
			return new AxelrodBinaryEncodedPlayer( actionFunction.subVector(rounds * 2, actionFunction.length()), phantomHistory, phantomOpponentHistory );
		}

		private AxelrodBinaryEncodedPlayer( BitVector actionFunction, List<Move> phantomHistory, List<Move> phantomOpponentHistory ) {
			this.actionFunction = actionFunction;
			this.numberOfRoundsOfHistoryRequired = logBase4(actionFunction.length());
			this.phantomHistory = phantomHistory;
			this.phantomOpponentHistory = phantomOpponentHistory;
		}
		
		///////////////////////////
		
		/**
		 * which index (i.e. which specific move) in this player's strategy should we use
		 * @throws IllegalArgumentException if the combined history and phantom history do not provide enough
		 *         moves to determine an index
		 */
		private int index( List<Move> history, List<Move> opponentHistory ) {
			assert( history.size() == opponentHistory.size() );
			
			// what we want here is the last n rounds that we need for the strategy
			// this might include the history, but might also include the phantom rounds as needed
			// this is then used to point to the right place in the strategy
			
			// see P11 of Marks1989
			// q = \sum_{i=1}^{r} 4^(r-i) [own(i)+2*other(i)]
			// where r is number of rounds remembered
			// own(i)/other(i) is move taken i rounds ago 

			if (numberOfRoundsOfHistoryRequired > (history.size() + phantomHistory.size())) {
				throw new IllegalArgumentException("Combination of history (" + history.size() 
								+ ") and phantom history (" + phantomHistory.size() + ") is not enough to "
								+ "provide the required moves (" + numberOfRoundsOfHistoryRequired + ")");
			}

			// consume the provided history first, then when that's done, consume the phantom history
			int q = 0;
			for (int i = 1; i <= numberOfRoundsOfHistoryRequired; i++) {
				final int own, other;
				if (i <= history.size()) {
					own = history.get( history.size() - i ).toBoolean() ? 1 : 0;
					other = opponentHistory.get( opponentHistory.size() - i ).toBoolean() ? 1 : 0;
				} else {
					own = phantomHistory.get( phantomHistory.size() - i ).toBoolean() ? 1 : 0;
					other = phantomOpponentHistory.get( phantomOpponentHistory.size() - i ).toBoolean() ? 1 : 0;
				}
				
				q += (int)Math.pow( 4, numberOfRoundsOfHistoryRequired - i ) * ( own + (2 * other) );
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
