package org.mitlware.problem.pending.ipd;

import java.util.List;
import java.util.Random;

public interface Player {
	/**given the supplied histories of this player's moves and the opponent's moves, what will this player's next move be?*/
	public Move move( List< Move > history, List< Move > opponentHistory, Random random );
}

// End ///////////////////////////////////////////////////////////////
