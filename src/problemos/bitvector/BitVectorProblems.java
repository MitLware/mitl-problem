package problemos.bitvector;

import java.util.Arrays;
import java.util.Random;
import java.util.stream.Stream;

import org.mitlware.SearchDirection;
import org.mitlware.mutable.Evaluate;
import org.mitlware.mutable.Locality;
import org.mitlware.mutable.Perturb;

import statelet.bitvector.BitVector;

//////////////////////////////////////////////////////////////////////

public final class BitVectorProblems {

	public static final class Onemax 
	extends Evaluate.Directional< BitVector, Double > {

		@Override
		public SearchDirection direction() { return SearchDirection.MAXIMIZING; }
		
		@Override
		public Double apply( BitVector x ) {
			return x.cardinality() / (double)x.length();
		}
	}

	///////////////////////////////
	
	public static final class Mimicry
	extends Evaluate.Directional< BitVector, Double > {
		
		private BitVector target;
		
		///////////////////////////
		
		public Mimicry( BitVector target ) {
			this.target = target;
		}
		
		@Override
		public SearchDirection direction() { return SearchDirection.MINIMIZING; }
		
		@Override
		public Double apply( BitVector x ) {
			
			if( x.length() != target.length() )
				throw new IllegalArgumentException();
			
			BitVector diff = target.clone(); 
			diff.xor( x );
			assert( diff.length() == x.length() );
	        return diff.cardinality() / (double)diff.length();        
	    }
	}
	
	///////////////////////////////

    public static final class Palindrome
	extends Evaluate.Directional< BitVector, Integer > {

		@Override
		public SearchDirection direction() { return SearchDirection.MAXIMIZING; }
		
		@Override
		public Integer apply( BitVector x ) {
        
            int numMirrored = 0;
            for( int i=0; i<x.length()/2; ++i )
            	if( x.get(i) == x.get(x.length()-i - 1 ) )
            		++numMirrored;
            return numMirrored;        
        }
    }

	///////////////////////////////
    
	public static final class Checkerboard 
	extends Evaluate.Directional< BitVector, Double > {
		
		private final BitVector pattern1;
		private final BitVector pattern2;
	
		///////////////////////////////
	
		public Checkerboard( int n ) {
			StringBuffer s = new StringBuffer();
			for( int i=0; i<n; ++i )
				s.append( n % 2 == 0 ? '0' : '1' );
		
			pattern1 = BitVector.fromBinaryString( s.toString() );
			pattern2 = pattern1.clone();
			pattern2.not();
		}

		@Override
		public SearchDirection direction() { return SearchDirection.MINIMIZING; }
		
		///////////////////////////////
	
		@Override
		public Double apply( BitVector x ) {
			return (double)Math.min( BitVector.HammingDistance( pattern1, x ), BitVector.HammingDistance( pattern2, x ) ); 
		}
	}
	
	///////////////////////////////

	public static final class LeadingOnes
	extends Evaluate.Directional< BitVector, Integer > {

		@Override
		public SearchDirection direction() { return SearchDirection.MAXIMIZING; }

		@Override
		public Integer apply( BitVector x ) {

			int numLeadingOnes = 0;
			for( int i=0; i<x.length() && x.get( i ); ++i )
				++numLeadingOnes;
				
	        return numLeadingOnes;        
	    }
	}
	
	///////////////////////////////

	public static final class Trap
	extends Evaluate.Directional< BitVector, Integer > {

		@Override
		public SearchDirection direction() { return SearchDirection.MAXIMIZING; }

		@Override
		public Integer apply( BitVector x ) {

	        final int numZeroes = x.length() - x.cardinality();
	        if( numZeroes == 0 )
	        	return x.length() + 1;
	        else
	        	return numZeroes;        
	    }
	}

	///////////////////////////////

	public static final class RoyalRoad 
	extends Evaluate.Directional< BitVector, Double > {
		
	    private final int theBlockSize;

	    ///////////////////////////////

	    public static final int DEFAULT_BLOCK_SIZE = 8;
	    // ^^ MM says so.
	    
	    public RoyalRoad() {
	        this( DEFAULT_BLOCK_SIZE );
	    }
	    
	    public RoyalRoad(int blockSize) {
	        theBlockSize = blockSize;
	    }
	    
	    ///////////////////////////////

		@Override
		public SearchDirection direction() { return SearchDirection.MAXIMIZING; }
	    
		@Override
		public Double apply(BitVector x) {
			
			if( x.length() % theBlockSize != 0 )
				throw new IllegalArgumentException();
			
	        final int blockSize = theBlockSize;
	        int[] permutation = new int[x.length()];
	        
	        // Set the permutation
	        // Option 1 - just in order
	        for (int i = 0; i < permutation.length; ++i )
	            permutation[i] = i;
//	        // Option 2 - random order, based on a seed
//	        Random rrRand = new Random(10);
//	        for (int i = 0; i < (1 * permutation.length); i++) // Take the sorted order above and shuffle it n times
//	        {
//	            int i1 = rrRand.nextInt(permutation.length), i2 = rrRand.nextInt(permutation.length);
//	            int temp = permutation[i1];
//	            permutation[i1] = permutation[i2];
//	            permutation[i2] = temp;
//	        }
	        // End of option 2
	        
	        // Debugging - if required to do so, display the permutation
//	        if (debugging)
//	            for (int i = 0; i < permutation.length; i++)
//	                System.out.print(permutation[i] + " ");
	        
	        // Now, step through the permutation, and count the number of 1's in each group
	        int score = 0, curBlockCount = 0, curBlock = 0;
	        for (int i = 0; i < x.length(); ++i )
	        {
	            curBlock = i / blockSize;
	            boolean curBlockBit = (curBlock % 2) == 0;
	            
	            // We count the number of bits with the wrong value. If there are none, then it's complete block
	            // and we can add to the fitness
	            // (Saves problems with last block if chromlength is no a multiple of blocksize)
	            if (x.get(i) != curBlockBit)
	                curBlockCount++;
	            
	            if (((i % blockSize) == (blockSize - 1)) || (i == (x.length() - 1))) // If at end of current block
	            {
	                if (curBlockCount == 0)
	                    score+=blockSize;
	                curBlockCount = 0;
	            }
	        }
	        
	      //  theLastPermutation = permutation;
	        
	        // return (double)score;
	        return (double)( x.length() - score ); 
	    }
	}

	///////////////////////////////
	
	public static final class FourPeaks 
	extends Evaluate.Directional< BitVector, Double > {
		
		private final int theT;
		private final int reward;
	    // private static final int DEFAULT_REWARD = 100;
	    
	    public FourPeaks(int T, int reward ) {
	        theT = T;
	        this.reward = reward;
	    }

		@Override
		public SearchDirection direction() { return SearchDirection.MAXIMIZING; }
	    
		@Override
		public Double apply( BitVector x) {
	        
	        int Zx = 0, Ox = 0; // Number of contiguous zeros and ones starting at position 100 and 0 respectively
	        //int T = chrom.length / 10;//10; // parameter of the four peaks problem
	        
	        // number of contiguous 0s ending at position 100 (actually, the end position, whatever it is)
	        for( int i=x.length() - 1; (i >= 0) && !x.get(i); --i )
	            ++Zx;
	        
	        // number of contiguous 1s starting at position 0
	        for( int i = 0; i < x.length() && x.get(i); ++i )
	            ++Ox;
	        
	        final int actualReward = (Zx > theT) && (Ox > theT) ? reward : 0;
	        return (double)(x.length() + reward - theT) - ( Math.max(Zx, Ox) + actualReward );
	    }
	}
	
	///////////////////////////////
	
	public static final class HIFF 
	extends Evaluate.Directional< BitVector, Double > {

		public static boolean isPositivePowerOf2( int x ) {
			int numSetBits = 0;
			for( int i=0; i<32; ++i )
				if( ( x & ( 1 << i ) ) != 0 )
					++numSetBits;
			return numSetBits == 1;
		}

		///////////////////////////////
		
		@Override
		public SearchDirection direction() { return SearchDirection.MAXIMIZING; }
		
		@Override
		public Double apply( BitVector x ) {
			
			if( !isPositivePowerOf2( x.length() ) )
				throw new IllegalArgumentException( "Expected positive power of 2, found: " + x.length() );
				
			if( x.length() == 1 )
				// return 0.0;
				return 1.0;				
			else
			{
				BitVector l = x.subVector( 0, x.length() / 2 );
				BitVector r = x.subVector( x.length() / 2, x.length() );
				
				assert l.length() + r.length() == x.length(); 
			
				return apply( l ) + apply( r )
						// + ( hiffImpl( x ) == 1 ? x.length() : 0 );
						+ ( hiffImpl( x ) == 1 ? 0.0 : x.length() );						
			}
		}
		
		///////////////////////////////
		
		private static int hiffImpl( BitVector x ) {
			
			assert( !isPositivePowerOf2( x.length() ) );
			
			if( x.length() == 1 )
				// return 1;
				return 0;				
			else
			{
				BitVector l = x.subVector( 0, x.length() / 2 );
				BitVector r = x.subVector( x.length() / 2, x.length() );
				assert l.length() + r.length() == x.length();			
				
//				if( l.equals( r ) && hiffImpl( l ) == 1 )
//				return 1;
//			else
//				return 0;				
			if( l.equals( r ) && hiffImpl( l ) == 0 )
				return 0;
			else
				return 1;				
			}
		}
	}
	
	///////////////////////////////
	
	public static final class UniformMutation implements Perturb< BitVector > {
		
		private final Random random;
		
		///////////////////////////
		
		public UniformMutation( Random random ) {
			this.random = random;
		}

		@Override
		public BitVector apply( BitVector x ) {
			BitVector result = x.clone();
			result.flip( random.nextInt( x.length() ) );
			return result;
		}
	}
	
	///////////////////////////////

	public static final class Hamming1Locality implements Locality< BitVector > {
		
		public Stream< BitVector > apply( BitVector x ) {
			
			BitVector [] neighbours = new BitVector [ x.length() ]; 
			for( int i=0; i<x.length(); ++i ) {
				BitVector neighbor = x.clone();
				neighbor.flip( i );
				neighbours[ i ] = neighbor;
			}

			return Arrays.stream( neighbours );
		}
	}
	
	///////////////////////////////	
}

// End ///////////////////////////////////////////////////////////////
