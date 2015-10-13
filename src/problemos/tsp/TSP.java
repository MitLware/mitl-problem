package problemos.tsp;

import java.io.IOException;
import java.io.InputStream;
import java.util.function.BiFunction;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import jeep.lang.BadFormatException;
import jeep.lang.UnsupportedFormatException;

public interface TSP {

	public int numCities();
	public boolean isSymmetric();
	
	public BiFunction< Integer, Integer, Double > getDistanceFn();
	
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
		public String toString() {
			return ToStringBuilder.reflectionToString( impl, ToStringStyle.MULTI_LINE_STYLE );
		}
	}
}

// End ///////////////////////////////////////////////////////////////

