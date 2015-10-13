package problemos.hanoi;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.Comparator;
import java.util.List;
import java.util.Stack;
import java.util.stream.Stream;

import org.apache.commons.lang3.builder.ToStringBuilder;

//////////////////////////////////////////////////////////////////////

public final class HanoiState {

	private final int numRings;
	private final List< Stack< Integer > > poles;
	
	///////////////////////////////
	
	public static HanoiState startState( int numPoles, int numRings ) {
		List< Stack< Integer > > poles = new ArrayList< Stack< Integer > >();
		for( int i=0; i<numPoles ; ++i )
			poles.add( new Stack< Integer >() );
		
		for( int i=0; i<numRings; ++i )
			poles.get( 0 ).push( numRings - i - 1 );
			
		return new HanoiState( numRings, poles );
	}

	public static HanoiState goalState( int numPoles, int numRings ) {
		List< Stack< Integer > > poles = new ArrayList< Stack< Integer > >();
		for( int i=0; i<numPoles ; ++i )
			poles.add( new Stack< Integer >() );
		
		for( int i=0; i<numRings; ++i )
			poles.get( poles.size() - 1 ).push( numRings - i - 1 );
			
		return new HanoiState( numRings, poles );
	}
	
	///////////////////////////////
	
	private HanoiState( int numRings, List< Stack< Integer > > poles ) {
		this.numRings = numRings;
		this.poles = poles;
		assert invariant();
	}
	
	///////////////////////////////
	
	private HanoiState moveFromPoleToPole( int sourcePole, int targetPole ) {
		if( sourcePole < 0 || sourcePole >= poles.size() || poles.get( sourcePole ).isEmpty() )
			throw new IllegalArgumentException();
		if( targetPole < 0 || targetPole >= poles.size() )
			throw new IllegalArgumentException();

		final int ring = poles.get( sourcePole ).peek();
		List< Stack< Integer > > newPoles = new ArrayList< Stack< Integer > >(); // (List< Stack< Integer > >)poles.clone();
		for( int i=0; i<poles.size(); ++i ) {
			Stack< Integer > x = poles.get( i );			
			@SuppressWarnings("unchecked")
			Stack< Integer > newStack = (Stack< Integer >)x.clone();
			if( i == sourcePole )
				newStack.pop();
			else if( i == targetPole )
				newStack.push( ring );
			
			newPoles.add( newStack );
		}
		
		return new HanoiState( numRings, newPoles );		
	}

	///////////////////////////////

	public Stream< HanoiState > neighbors() {
		
		List< HanoiState > result = new ArrayList< HanoiState >();
		for( int sourcePole=0; sourcePole<poles.size(); ++sourcePole ) {
			if( !poles.get( sourcePole ).isEmpty() ) {
				for( int targetPole=0; targetPole<poles.size(); ++targetPole ) {
					if( sourcePole != targetPole ) {
						if( poles.get( targetPole ).isEmpty() || 
								poles.get( sourcePole ).peek() < poles.get( targetPole ).peek() )
								result.add( moveFromPoleToPole( sourcePole, targetPole ) );
					}
				}
			}
		}
		
		return result.stream();
	}

	///////////////////////////////
	
	public static int HammingDistance( HanoiState a, HanoiState b ) {
		if( a.numRings != b.numRings || a.poles.size() != b.poles.size() )
			throw new IllegalArgumentException();
		
		int result = 0;
		for( int i=0; i<a.poles.size(); ++i )
			result += jeep.util.Collections.HammingDistance( a.poles.get( i ), b.poles.get( i ) );
		
		return result;
	}
	
	///////////////////////////////	
	
	@Override	
	public int hashCode() { return poles.hashCode(); }
	
	@Override
	public boolean equals( Object o ) {
		if( !( o instanceof HanoiState ) )
			return false;
		
		HanoiState rhs = (HanoiState)o;
		return poles.equals( rhs.poles );
	}
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString( this );
	}
	
	///////////////////////////////

	private static boolean isPartition( int numRings, List< Stack< Integer > > poles ) {
		BitSet rings = new BitSet( numRings );
		for( int i=0; i<poles.size(); ++i ) {
			for( int j=0; j<poles.get(i).size(); ++j )
				rings.set( poles.get( i ).get( j ) );
		}
		
		return rings.cardinality() == numRings;
	}
	
	private static boolean isValidPole( Stack< Integer > pole ) {
		return jeep.util.Collections.isSorted( pole, new Comparator< Integer >() {
			@Override
			public int compare(Integer a, Integer b) {
				return b - a;
			}} );
	}
	
	public boolean invariant() {
		for( Stack< Integer > pole : poles )
			if( !isValidPole( pole ) )
				return false;
		
		return isPartition( numRings, poles );
	}
}

// End ///////////////////////////////////////////////////////////////

