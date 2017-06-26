package org.mitlware.problem.numeric;

import static org.junit.Assert.*;

import java.util.Optional;
import java.util.Random;

import metaxa.FunctionPoint;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.Test;

public class TestDeJongRealVector {

	public Pair< Double, Double > testImpl( RealVectorProblem p ) {
		FunctionPoint< double [], Double > fp = p.getGlobalOptimum().get();
		return Pair.of( fp.getOutput(), p.evaluator().apply( fp.getInput() ) );
	}
	
	@Test
	public void test() {
		
		{
			Pair< Double, Double > p = testImpl( DeJong.F1 );
			assertEquals( p.getLeft(), p.getRight() );
		} {
			Pair< Double, Double > p = testImpl( DeJong.F2 );
			assertEquals( p.getLeft(), p.getRight() );
		} {
			Pair< Double, Double > p = testImpl( DeJong.F3 );
			assertEquals( p.getLeft(), p.getRight() );
		} {
			Random mockRandom = new java.util.Random() {
				private static final long serialVersionUID = 1L;

				@Override
				public double nextGaussian() { return 0.0; }
			};
			Pair< Double, Double > p = testImpl( DeJong.F4.apply( mockRandom ) );
			assertEquals( p.getLeft(), p.getRight() );
		} {
			Pair< Double, Double > p = testImpl( DeJong.F5 );
			assertEquals( p.getLeft(), p.getRight(), 0.01 );
		}
	}
}

// End ///////////////////////////////////////////////////////////////
