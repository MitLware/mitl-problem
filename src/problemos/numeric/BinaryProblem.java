package problemos.numeric;

import java.util.Optional;
import java.util.function.Function;

import jeep.math.ClosedInterval;
import metaxa.FunctionPoint;

import org.mitlware.SearchDirection;
import org.mitlware.mutable.Evaluate;

import statelet.bitvector.BitVector;

//////////////////////////////////////////////////////////////////////

public interface BinaryProblem {

	public int getNumDimensions();
	
	public ClosedInterval getBounds( int dim );

	public Evaluate.Directional< BitVector, Double > evaluator();
	
	public Optional< FunctionPoint< BitVector, Double > > getGlobalOptimum();
	
	///////////////////////////////
	
	// FIXME: assumes same bits per dimension
	public static BinaryProblem fromReal( RealVectorProblem problem, int numBitsPerDimension, 
			BinaryEncoder encoding ) {
		return new BinaryProblem() {

			private Function< Integer, ClosedInterval > bounds = ( Integer index ) -> problem.getBounds( index );
			
			///////////////////////
			
			@Override
			public int getNumDimensions() { return problem.getNumDimensions(); }

			@Override
			public ClosedInterval getBounds(int dim) { return problem.getBounds( dim ); }

			@Override
			public Evaluate.Directional<BitVector, Double> evaluator() {
				
				return new Evaluate.Directional<BitVector, Double>() {
					public Double apply( BitVector x ) { 
						double [] asRealVector = encoding.decode( x, numBitsPerDimension, bounds );
						return problem.evaluator().apply( asRealVector );
					}

					@Override
					public SearchDirection direction() {
						return problem.evaluator().direction();
					}
				};
			}

			@Override
			public Optional<FunctionPoint<BitVector, Double>> getGlobalOptimum() {
				return problem.getGlobalOptimum().map( fp -> 
					FunctionPoint.create( 
						encoding.encode( fp.getInput(), numBitsPerDimension, bounds ), 
							fp.getOutput() ) );
			}
		};
	}
	
	///////////////////////////////	

}

// End ///////////////////////////////////////////////////////////////
