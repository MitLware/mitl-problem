package problemos.pending.ipd;

public enum Move {
	
	C {
		public boolean toBoolean() { return false; }		
	}, 
	D {
		public boolean toBoolean() { return true; }		
	};
	
	///////////////////////////////
	
	public abstract boolean toBoolean();
	
	public static Move fromBoolean( boolean x ) { 
		return x ? Move.D : Move.C; 
	}
}

// End ///////////////////////////////////////////////////////////////

