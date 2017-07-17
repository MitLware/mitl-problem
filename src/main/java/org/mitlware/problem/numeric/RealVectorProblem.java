package org.mitlware.problem.numeric;

import java.util.Optional;
import java.util.function.Function;

import org.mitlware.support.math.ClosedInterval;

import org.mitlware.mutable.Evaluate;

import org.mitlware.support.util.FunctionPoint;

//////////////////////////////////////////////////////////////////////

public interface RealVectorProblem {

	public int getNumDimensions();
	
	public ClosedInterval getBounds( int dim );

	public Evaluate.Directional< double [], Double > evaluator();
	
	public Optional< FunctionPoint< double [], Double > > getGlobalOptimum();
	
	///////////////////////////////
	
	public static Function< Integer, ClosedInterval > 
	constantBounds( int dimension, ClosedInterval bounds ) {
		return ( Integer dim ) -> {
			if( dim < 0 || dim >= dimension )
				throw new IllegalArgumentException();
			
			return bounds;
		};
	}

	public static Function< Integer, ClosedInterval > 
	explicitBounds( ClosedInterval [] bounds ) {
		return ( Integer dim ) -> {
			if( dim < 0 || dim >= bounds.length )
				throw new IllegalArgumentException();
			
			return bounds[dim];
		};
	}
	
	///////////////////////////////	
	
	public static class ExplicitImpl implements RealVectorProblem {

		private final Evaluate.Directional< double [], Double > evaluator;		
		private final int dimension;
		private final Function< Integer, ClosedInterval > bounds;
		private final Optional< FunctionPoint< double [], Double > > globalOptimum;
		
		///////////////////////////

		public ExplicitImpl( Evaluate.Directional< double [], Double > evaluator, 
				int dimension, Function< Integer, ClosedInterval > bounds ) {
			this.evaluator = evaluator;			
			this.dimension = dimension;
			this.bounds = bounds;
			this.globalOptimum = Optional.empty();
		}
		
		public ExplicitImpl( Evaluate.Directional< double [], Double > evaluator,				
				int dimension, 
			Function< Integer, ClosedInterval > bounds, 
			FunctionPoint< double [], Double > globalOptimum ) {
			
			this.evaluator = evaluator;			
			this.dimension = dimension;
			this.bounds = bounds;  
			this.globalOptimum = Optional.of( globalOptimum );
		}
		
		///////////////////////////

		@Override		
		public Evaluate.Directional< double [], Double > evaluator() { return evaluator; }

		///////////////////////////		
		
		@Override
		public int getNumDimensions() { return dimension; }
		
		@Override
		public ClosedInterval getBounds( int dim ) { 
		
			if( dim < 0 || dim >= getNumDimensions() )
				throw new IllegalArgumentException();
			
			return bounds.apply( dim );
		}
		
		@Override		
		public Optional< FunctionPoint< double [], Double > > getGlobalOptimum() { return globalOptimum; }		
	}
}

// End ///////////////////////////////////////////////////////////////
