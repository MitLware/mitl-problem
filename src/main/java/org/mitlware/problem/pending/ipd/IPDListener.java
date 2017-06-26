package org.mitlware.problem.pending.ipd;

import java.util.List;

public interface IPDListener {

	public void onGameStart( Player p1, Player p2 );
	
	public void onRoundStart( Player p1, 
			List< Move > history1, Player p2, List< Move > history2 );	

	public void onRoundEnd( Player p1, 
			List< Move > history1, Player p2, List< Move > history2 );	
	
	public void onGameEnd( Player p1, double totalPayoff1, Player p2, double totalPayoff2 );
	
	///////////////////////////////
	
	public static final class NullListener implements IPDListener {

		@Override
		public void onGameStart(Player p1, Player p2) {}

		@Override
		public void onRoundStart(Player p1, List<Move> history1, Player p2,
				List<Move> history2) {}

		@Override
		public void onRoundEnd(Player p1, List<Move> history1, Player p2,
				List<Move> history2) {}

		@Override
		public void onGameEnd( Player p1, double totalPayoff1, Player p2, double totalPayoff2 ) {}
	}
}

// End ///////////////////////////////////////////////////////////////
