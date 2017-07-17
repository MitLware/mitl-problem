package org.mitlware.problem.sat;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.mitlware.SearchDirection;
import org.mitlware.mutable.Evaluate;

import org.mitlware.problem.sat.DIMACSFormat.BadDIMACSFormatException;

import org.mitlware.solution.bitvector.BitVector;

//////////////////////////////////////////////////////////////////////

public interface SATProblem {
	
	public CNF getCNF();

	///////////////////////////////
	
	public static final class NumUnsatisfiedClauses
	extends Evaluate.Directional< BitVector, Integer > {
		
		private final CNF cnf;
		
		public NumUnsatisfiedClauses(CNF cnf) {
			super(SearchDirection.MINIMIZING);
			this.cnf = cnf;
		}

		@Override
		public Integer apply(BitVector x) {
			return cnf.numUnsatisfiedClauses( x );
		}
	}
	
	///////////////////////////////
	
	public static class DIMACSFormatProblem implements SATProblem {

		private final CNF cnf;
		
		public DIMACSFormatProblem(InputStream is) throws IOException, BadDIMACSFormatException {
			cnf = DIMACSFormat.readDIMACS( is );
		}
		
		///////////////////////////
		
		@Override
		public CNF getCNF() { return cnf; }
	}
}

// End ///////////////////////////////////////////////////////////////

