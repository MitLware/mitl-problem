package problemos.pending.ipd;

import static org.junit.Assert.*;

import java.util.List;
import java.util.Random;

import org.junit.Test;

import problemos.pending.ipd.IPD.Result;
import problemos.pending.ipd.BasicPlayers.AxelrodBinaryEncodedPlayer;
import statelet.bitvector.BitVector;

public class TestIPD {

	@Test
	public void test1() {
		Random random = new Random( 0 );
		int numRounds = 10; 
		Payoff payoff = Payoff.defaultPayoff; 
		
		Player p1 = new BasicPlayers.AllD(); 
		Player p2 = new BasicPlayers.AllD();
		
		Result result = IPD.play( p1, p2, payoff, numRounds, random );
		assertEquals( numRounds * payoff.dd, result.payoff1, 0.0 );
		assertEquals( numRounds * payoff.dd, result.payoff2, 0.0 );		
	}
	
	@Test
	public void test2() {
		Random random = new Random( 0 );
		int numRounds = 2; 
		Payoff payoff = Payoff.defaultPayoff; 
		
		Player p1 = new BasicPlayers.AllD(); 
		Player p2 = new BasicPlayers.TFT();
		
		Result result = IPD.play( p1, p2, payoff, numRounds, random );
		assertEquals( payoff.dc + payoff.dd, result.payoff1, 0.0 );
		assertEquals( payoff.cd + payoff.dd, result.payoff2, 0.0 );		
	}

	///////////////////////////////
	
	private void encodedEqualAgainstTFT( Player encoded, Player other, int numRounds ) {
		Random random = new Random( 0 );
		Payoff payoff = Payoff.defaultPayoff; 

		Player tft = new BasicPlayers.TFT();
		
		IPDListener l = new IPDListener() {

			@Override
			public void onGameStart(Player p1, Player p2) {}

			@Override
			public void onRoundStart(Player p1, List<Move> history1, Player p2, List<Move> history2) {}

			@Override
			public void onRoundEnd(Player p1, List<Move> history1, Player p2, List<Move> history2) {
				System.out.println( "Player1:" + history1.get( history1.size() - 1 ) + ", Player2:" + history2.get( history2.size() - 1 ) ); 
			}

			@Override
			public void onGameEnd(Player p1, double totalPayoff1, Player p2, double totalPayoff2) {}
		}; 
		
		///////////////////////////
		
		for( int i=0; i<numRounds; ++i ) {
			Result result1 = IPD.play( encoded, tft, payoff, numRounds, random, l );			
			Result result2 = IPD.play( other, tft, payoff, numRounds, random );
			assertEquals( result1, result2 );
		}
	}
	
	///////////////////////////////
	
	@Test
	public void test3() {
		
		{
			BitVector allC = BitVector.fromBinaryString( "0000" );
			Player encoded = BasicPlayers.AxelrodBinaryEncodedPlayer.noPhantomMemory( allC );
			encodedEqualAgainstTFT( encoded, new BasicPlayers.AllC(), 10 );
		} 
		{
			BitVector allD = BitVector.fromBinaryString( "1111" );
			Player encoded = BasicPlayers.AxelrodBinaryEncodedPlayer.noPhantomMemory( allD );
			encodedEqualAgainstTFT( encoded, new BasicPlayers.AllD(), 10 );
		} 
	}
}

// End ///////////////////////////////////////////////////////////////
