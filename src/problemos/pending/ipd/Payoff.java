package problemos.pending.ipd;

public final class Payoff {
	
	public final double cc;
	public final double cd;
	public final double dc;
	public final double dd;
	
	///////////////////////////////
	
	public static Payoff defaultPayoff = new Payoff( 3.0, 0.0, 5.0, 1.0 );
	
	///////////////////////////////	
	
	public Payoff( double cc, double cd, double dc, double dd ) {
		this.cc = cc;
		this.cd = cd;
		this.dc = dc;
		this.dd = dd;
	}
	
	public double value( Move m1, Move m2 ) {
		switch( m1 ) {
			case C : if( m2 == Move.C ) return cc; else return cd;
			case D : if( m2 == Move.C ) return dc; else return dd;
			default : throw new IllegalStateException();
		}
	}
}

// End ///////////////////////////////////////////////////////////////

