package problemos.pending.ipd;

import java.util.List;
import java.util.Random;

public interface Player {

	public Move move( List< Move > history, List< Move > opponentHistory, Random random );
}

// End ///////////////////////////////////////////////////////////////
