package problemos.bitvector;

import static org.junit.Assert.*;

import org.junit.Test;
import org.mitlware.mutable.Evaluate;

import statelet.bitvector.BitVector;

public final class TestEvaluationFns {

	@Test
	public void testOnemax() {
		
		Evaluate.Directional< BitVector, Double > f = new BitVectorProblems.Onemax();
		assertEquals( 1.0, f.apply( BitVector.fromBinaryString( "1111" ) ), 0.0 );
		assertEquals( 0.25, f.apply( BitVector.fromBinaryString( "1000" ) ), 0.0 );
		assertEquals( 0.5, f.apply( BitVector.fromBinaryString( "1001" ) ), 0.0 );
		assertEquals( 0.75, f.apply( BitVector.fromBinaryString( "1101" ) ), 0.0 );		
		assertEquals( 0.0, f.apply( BitVector.fromBinaryString( "0000" ) ), 0.0 );		
	}
	
	///////////////////////////////
	
	@Test
	public void testMimicry() {
		
		{
			Evaluate.Directional< BitVector, Double > f = new BitVectorProblems.Mimicry( BitVector.fromBinaryString( "1111" ) );
			assertEquals( 0.0, f.apply( BitVector.fromBinaryString( "1111" ) ), 0.0 );
			assertEquals( 0.75, f.apply( BitVector.fromBinaryString( "1000" ) ), 0.0 );
			assertEquals( 0.5, f.apply( BitVector.fromBinaryString( "1001" ) ), 0.0 );
			assertEquals( 0.25, f.apply( BitVector.fromBinaryString( "1101" ) ), 0.0 );		
			assertEquals( 1.0, f.apply( BitVector.fromBinaryString( "0000" ) ), 0.0 );
		}
		
		{
			Evaluate.Directional< BitVector, Double > f = new BitVectorProblems.Mimicry( BitVector.fromBinaryString( "0000" ) );
			assertEquals( 1.0, f.apply( BitVector.fromBinaryString( "1111" ) ), 0.0 );
			assertEquals( 0.25, f.apply( BitVector.fromBinaryString( "1000" ) ), 0.0 );
			assertEquals( 0.5, f.apply( BitVector.fromBinaryString( "1001" ) ), 0.0 );
			assertEquals( 0.75, f.apply( BitVector.fromBinaryString( "1101" ) ), 0.0 );		
			assertEquals( 0.0, f.apply( BitVector.fromBinaryString( "0000" ) ), 0.0 );
		}
	}

	///////////////////////////////
	
	@Test
	public void testTrapGlobal() {
		Evaluate.Directional< BitVector, Integer > f = new BitVectorProblems.Trap(4, 4);
		assertEquals( 4, f.apply( BitVector.fromBinaryString( "0000" ) ), 0 );
		assertEquals( 3, f.apply( BitVector.fromBinaryString( "0001" ) ), 0 );
		assertEquals( 2, f.apply( BitVector.fromBinaryString( "0011" ) ), 0 );
		assertEquals( 1, f.apply( BitVector.fromBinaryString( "0111" ) ), 0 );
		assertEquals( 5, f.apply( BitVector.fromBinaryString( "1111" ) ), 0 );		
	}
	

	///////////////////////////////
	
	@Test
	public void testTrap5() {
		Evaluate.Directional< BitVector, Integer > f = new BitVectorProblems.Trap(10, 5);
		assertEquals( 10, f.apply( BitVector.fromBinaryString( "0000000000" ) ), 0 );
		assertEquals( 9, f.apply( BitVector.fromBinaryString( "0000100000" ) ), 0 );
		assertEquals( 8, f.apply( BitVector.fromBinaryString( "0001100000" ) ), 0 );
		assertEquals( 7, f.apply( BitVector.fromBinaryString( "0011100000" ) ), 0 );
		assertEquals( 6, f.apply( BitVector.fromBinaryString( "0111100000" ) ), 0 );
		assertEquals( 11, f.apply( BitVector.fromBinaryString( "1111100000" ) ), 0 );	
		assertEquals( 11, f.apply( BitVector.fromBinaryString( "0000011111" ) ), 0 );
		assertEquals( 10, f.apply( BitVector.fromBinaryString( "0000111111" ) ), 0 );
		assertEquals( 9, f.apply( BitVector.fromBinaryString( "0001111111" ) ), 0 );
		assertEquals( 8, f.apply( BitVector.fromBinaryString( "0011111111" ) ), 0 );
		assertEquals( 7, f.apply( BitVector.fromBinaryString( "0111111111" ) ), 0 );
		assertEquals( 12, f.apply( BitVector.fromBinaryString( "1111111111" ) ), 0 );
		assertEquals( 8, f.apply( BitVector.fromBinaryString( "0000100001" ) ), 0 );
		assertEquals( 6, f.apply( BitVector.fromBinaryString( "0001100011" ) ), 0 );
		assertEquals( 4, f.apply( BitVector.fromBinaryString( "0011100111" ) ), 0 );
		assertEquals( 2, f.apply( BitVector.fromBinaryString( "0111101111" ) ), 0 );		
	}
}

// End ///////////////////////////////////////////////////////////////
