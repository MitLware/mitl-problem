/*
 * Copyright (C) Jerry Swan, 2010-2012.
 * 
 * This file is part of DCPP, a solver for the 'To-work Daily Car-Pooling Problem'.
 * 
 * DCPP is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * DCPP is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with DCPP.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

//////////////////////////////////////////////////////////////////////

package problemos.pending.cvrp;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

import jeep.math.Vec2;

//////////////////////////////////////////////////////////////////////

public final class ChristophidesEtAlCVRPFormat {

	/**
	 * Format of the 14 test problems from
	 * Chapter 11 of N.Christofides, A.Mingozzi, P.Toth and C.Sandi (eds)
	 *  "Combinatorial optimization", John Wiley, Chichester 1979.
	 *  
	 * The format of these data files is:
	 * number of customers, vehicle capacity, maximum route time, drop time
	 * depot x-coordinate, depot y-coordinate
	 * for each customer in turn: x-coordinate, y-coordinate, quantity
	 */

	///////////////////////////////
	
	public static final class Customer {
		private final Vec2 coord;
		private final int quantity;

		///////////////////////////
		
		public Customer( String line ) {
			Scanner scanner = new Scanner( line );

			final int xCoord = scanner.nextInt();
			final int yCoord = scanner.nextInt();
			coord = new Vec2( xCoord, yCoord );
			quantity = scanner.nextInt();
			scanner.close();
		}
		
		///////////////////////////
		
		public Vec2 getCoord() { return coord; }
		public int getQuantity() { return quantity; }
	}
	
	///////////////////////////////
	
	public static final class CVRPProblemDescription {
		private final int vehicleCapacity;
		private final int maximumRouteTime;
		private final int dropTime;
		private final Vec2 depotCoord;
		private List< Customer > customers;
		
		///////////////////////////
		
		public CVRPProblemDescription( int vehicleCapacity, int maximumRouteTime, int dropTime,
				Vec2 depotCoord, List< Customer > customers ) {
			this.vehicleCapacity = vehicleCapacity;
			this.maximumRouteTime = maximumRouteTime;
			this.dropTime = dropTime;
			this.depotCoord = depotCoord;
			this.customers = Collections.unmodifiableList( customers );
		}
		
		///////////////////////////

		public int getVehicleCapacity() { return vehicleCapacity; }
		public int getMaximumRouteTime() { return maximumRouteTime; }
		public int getDropTime() { return dropTime; }
		public Vec2 getDepotCoord() { return depotCoord; }
		public List< Customer > getCustomers() { return customers; }
		
		///////////////////////////
		
		public void writeChristophidesEtAlFormat( PrintStream out ) {
			out.println( customers.size() + " " 
					+ vehicleCapacity + " " 
					+ maximumRouteTime + " " 
					+ dropTime );
			out.println( depotCoord.getX() + " " + depotCoord.getY() );
			for( Customer c : customers )
				out.println( c.getCoord().getX() + " " + c.getCoord().getY() + " " + c.quantity );
		}
	}

	///////////////////////////////
	
	public static CVRPProblemDescription read( InputStream in ) {
		
		Scanner scanner = new Scanner( in );
		try {
			
			String line = scanner.nextLine();
			Scanner lineScanner = new Scanner( line );
			// FIXME: potential resource leak...
			
			final int numCustomers = lineScanner.nextInt();		
			final int vehicleCapacity = lineScanner.nextInt();
			final int maximumRouteTime = lineScanner.nextInt();
			final int dropTime = lineScanner.nextInt();
		
			line = scanner.nextLine();
			lineScanner.close();
			lineScanner = new Scanner( line );
			final int depotXCoord = lineScanner.nextInt();
			final int depotYCoord = lineScanner.nextInt();		
			Vec2 depotCoord = new Vec2( depotXCoord, depotYCoord );
		
			List< Customer > customers = new ArrayList< Customer >();
			for( int i=0; i<numCustomers; ++i ) {
				line = scanner.nextLine();
				customers.add( new Customer( line ) );
			}
		
			lineScanner.close();
			return new CVRPProblemDescription( vehicleCapacity, maximumRouteTime, dropTime,
				depotCoord, customers );
		}
		catch( Exception ex ) {
			scanner.close();
			throw ex;
		}
		finally {
			scanner.close();			
		}
	}

	///////////////////////////////
	
	public static void main( String [] args ) throws FileNotFoundException {
		
		String problemFile = "vrpnc1.txt";
		CVRPProblemDescription vrp = ChristophidesEtAlCVRPFormat.read( 
			ChristophidesEtAlCVRPFormat.class.getResourceAsStream( 
					"resources/CMT-vrp/" + problemFile ) );
		
		vrp.writeChristophidesEtAlFormat( System.out );
		
		System.out.println( "All done." );
	}
}

// End ///////////////////////////////////////////////////////////////

