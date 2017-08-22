package org.mitlware.problem.ipd;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.mitlware.SearchDirection;
import org.mitlware.mutable.Evaluate;

import org.mitlware.solution.bitvector.BitVector;

public final class IPD {

	/**given a particular strategy for comparison, evaluate the given strategy against it*/
	public static final class IPDProblem 
	extends Evaluate.Directional< BitVector, Double > {

		private final Player niche;
		private final Payoff payoff;
		private final int numRounds;
		private final Random random;
		
		public IPDProblem(Player niche, Payoff payoff, int numRounds, Random random) {
			super( SearchDirection.MAXIMIZING );
			this.niche = niche;
			this.payoff = payoff;
			this.numRounds = numRounds;
			this.random = random;
		}
		
		@Override
		public Double apply( BitVector x ) {
			Result result = IPD.play( BasicPlayers.AxelrodBinaryEncodedPlayer.phantomMemory(x), 
				niche, payoff, numRounds, random );
			
			return result.payoff1;
		}
	}
	
	///////////////////////////////
	
	public static final class Result {
		
		public final double payoff1;
		public final double payoff2;
		
		///////////////////////////
		
		public Result( double payoff1, double payoff2 ) {
			this.payoff1 = payoff1;
			this.payoff2 = payoff2;
		}
		
		///////////////////////////

		@Override
		public boolean equals( Object other ) {
			return EqualsBuilder.reflectionEquals( this, other );
		}
		
		@Override
		public int hashCode() {
			return HashCodeBuilder.reflectionHashCode( this );
		}

		@Override
		public String toString() {
			return ToStringBuilder.reflectionToString( this );
		}
	}
	
	///////////////////////////////

	public static Result play( Player p1, Player p2, 
			Payoff payoff, int numRounds, Random random ) {
		return play( p1, p2, 
			payoff, numRounds, random, new IPDListener.NullListener() );
	}
	
	public static Result play( Player p1, Player p2, 
			Payoff payoff, int numRounds, Random random, IPDListener listener ) {

		listener.onGameStart( p1, p2 );
		
		List< Move > history1 = new ArrayList< Move >();
		List< Move > history2 = new ArrayList< Move >();
		
		for( int i=0; i<numRounds; ++i ) {
			
			listener.onRoundStart( p1, history1, p2, history2 );
			
			Move m1 = p1.move( history1, history2, random );
			Move m2 = p2.move( history2, history1, random );
			history1.add( m1 );
			history2.add( m2 );			
			
			listener.onRoundEnd( p1, history1, p2, history2 );			
		}

		double totalPayoff1 = 0.0, totalPayoff2 = 0.0;
		assert( history1.size() == history2.size() );
		for( int i=0; i<history1.size(); ++i ) {
			totalPayoff1 += payoff.value( history1.get( i ), history2.get( i ) );
			totalPayoff2 += payoff.value( history2.get( i ), history1.get( i ) );			
		}
		
		listener.onGameEnd( p1, totalPayoff1, p2, totalPayoff2 );
		
		return new Result( totalPayoff1, totalPayoff2 );
	}
}

// End ///////////////////////////////////////////////////////////////
