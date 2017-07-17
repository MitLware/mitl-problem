package org.mitlware.problem.tsp;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import java.util.function.BiFunction;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import org.mitlware.solution.permutation.ArrayForm;
import org.mitlware.support.lang.BadFormatException;
import org.mitlware.support.lang.UnsupportedFormatException;

public interface TSP {

	public int numCities();
	public boolean isSymmetric();
	
	public BiFunction< Integer, Integer, Double > getDistanceFn();
	
	public Optional<String> toTSPLibFormat();
	
	///////////////////////////////
	
	public static double tourLength( ArrayForm tour, BiFunction< Integer, Integer, Double > distanceFn ) {
		double sum = 0;
		for( int i=1; i<tour.size(); ++i )
			sum += distanceFn.apply( tour.get(i-1) + 1, tour.get(i) + 1 );
		
		// sum += distanceFn.apply( tour.get(tour.size()-1) + 1, tour.get(0) + 1 );
		sum += distanceFn.apply( tour.get(0) + 1, tour.get(tour.size()-1) + 1 );		
		return sum;
	}
	
	///////////////////////////////	
	
	public static final class TSPLibInstance implements TSP {

		private final TSPLibFormat impl;
		
		///////////////////////////
		
		public TSPLibInstance( InputStream is ) throws IOException, BadFormatException {
			impl = TSPLibFormat.read( is );
		}
		
		@Override
		public int numCities() { return impl.getDimension(); }

		@Override
		public boolean isSymmetric() { return impl.getType() == TSPLibFormat.Type.TSP; } 

		@Override
		public BiFunction<Integer, Integer, Double> getDistanceFn() {
			return (i,j) -> (double)impl.getDistanceFn().apply( i, j );
		}
		
		@Override		
		public Optional<String> toTSPLibFormat() {
			return impl.toTSPLibFormat();
		}
		
		@Override
		public String toString() {
			return ToStringBuilder.reflectionToString( impl, ToStringStyle.MULTI_LINE_STYLE );
		}
	}
}

// End ///////////////////////////////////////////////////////////////

