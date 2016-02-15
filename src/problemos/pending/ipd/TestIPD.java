package problemos.pending.ipd;

import static org.junit.Assert.assertEquals;

import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.junit.Test;
import org.mitlware.mutable.Evaluate;

import problemos.pending.ipd.IPD.Result;
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
		
		System.out.println("3.1:");
		
		{
			BitVector allC = BitVector.fromBinaryString( "000000" );
			Player encoded = BasicPlayers.AxelrodBinaryEncodedPlayer.phantomMemory( allC );
			encodedEqualAgainstTFT( encoded, new BasicPlayers.AllC(), 10 );
		}
		
		System.out.println("3.2:");
		
		{
			BitVector allD = BitVector.fromBinaryString( "111111" );
			Player encoded = BasicPlayers.AxelrodBinaryEncodedPlayer.phantomMemory( allD );
			encodedEqualAgainstTFT( encoded, new BasicPlayers.AllD(), 10 );
		}
		
		System.out.println("3.3:");
		
		{
			BitVector tft = BitVector.fromBinaryString( "110000" );
			Player encoded = BasicPlayers.AxelrodBinaryEncodedPlayer.phantomMemory( tft );
			encodedEqualAgainstTFT( encoded, new BasicPlayers.TFT(), 10 ); // NB - BasicPlayers.TFT
		}
		
		System.out.println("3.4:");
		
		{
			BitVector tft = BitVector.fromBinaryString( "00000000000000000000" );
			Player encoded = BasicPlayers.AxelrodBinaryEncodedPlayer.phantomMemory( tft );
			encodedEqualAgainstTFT( encoded, new BasicPlayers.AllC(), 10 ); // NB - BasicPlayers.TFT
		}
		
		System.out.println("3.5:");
		
		{
			BitVector tft = BitVector.fromBinaryString( "11111111111111111111" );
			Player encoded = BasicPlayers.AxelrodBinaryEncodedPlayer.phantomMemory( tft );
			encodedEqualAgainstTFT( encoded, new BasicPlayers.AllD(), 10 ); // NB - BasicPlayers.TFT
		}
		
		System.out.println("3.6:");
		
		{
			BitVector tft = BitVector.fromBinaryString( "11001100110011000000" );
			Player encoded = BasicPlayers.AxelrodBinaryEncodedPlayer.phantomMemory( tft );
			encodedEqualAgainstTFT( encoded, new BasicPlayers.TFT(), 10 ); // NB - BasicPlayers.TFT
		}
		
		System.out.println();

	}
	

	///////////////////////////////
	
	@Test
	public void testAxelrodBinaryWithNoPhantomMemory() {
		// this won't work with IPD.play() because the first round will fail
		{
			Player p1 = BasicPlayers.AxelrodBinaryEncodedPlayer.noPhantomMemory(BitVector.fromBinaryString("0000")); // always cooperate
			Player p2 = BasicPlayers.AxelrodBinaryEncodedPlayer.noPhantomMemory(BitVector.fromBinaryString("1010")); // always cooperate (remember, BV is right-to-left)
			assertEquals(Move.C, p1.move(Collections.singletonList(Move.C), Collections.singletonList(Move.C), null));
			assertEquals(Move.C, p2.move(Collections.singletonList(Move.C), Collections.singletonList(Move.C), null));
		}
		
		{
			Player p3 = BasicPlayers.AxelrodBinaryEncodedPlayer.noPhantomMemory(BitVector.fromBinaryString("1111")); // always defect
			Player p4 = BasicPlayers.AxelrodBinaryEncodedPlayer.noPhantomMemory(BitVector.fromBinaryString("0101")); // always defect
			assertEquals(Move.D, p3.move(Collections.singletonList(Move.C), Collections.singletonList(Move.C), null));
			assertEquals(Move.D, p4.move(Collections.singletonList(Move.C), Collections.singletonList(Move.C), null));
		}
		
		{
			Player p1 = BasicPlayers.AxelrodBinaryEncodedPlayer.noPhantomMemory(BitVector.fromBinaryString("1100")); // TFT
			assertEquals(Move.D, p1.move(Collections.singletonList(Move.D), Collections.singletonList(Move.D), null));
			assertEquals(Move.C, p1.move(Collections.singletonList(Move.C), Collections.singletonList(Move.C), null));
		}
	}


	///////////////////////////////
	
	@Test
	public void testIPDFitness() {
		Random random = new Random( 0 );		
		Payoff payoff = Payoff.defaultPayoff; 
		Evaluate.Directional<BitVector, Double> ipd = new IPD.IPDProblem(new BasicPlayers.AllD(), payoff, 10, random );
		
		assertEquals(0, ipd.apply(BitVector.fromBinaryString( "000000" )).doubleValue(), 0); // all C
		assertEquals(10, ipd.apply(BitVector.fromBinaryString( "111111" )).doubleValue(), 0); // all D
		assertEquals(9, ipd.apply(BitVector.fromBinaryString( "110000" )).doubleValue(), 0); // TFT
	}
}

// End ///////////////////////////////////////////////////////////////
