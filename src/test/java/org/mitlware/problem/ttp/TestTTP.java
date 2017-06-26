package org.mitlware.problem.ttp;

import org.junit.Test;

import java.io.File;

import static org.junit.Assert.assertEquals;

public class TestTTP {

	/* Code to generate the results used in the GECCO 2015 article
    "Approximate Approaches to the Traveling Thief Problem" by
    Hayden Faulkner, Sergey Polyakovskiy, Tom Schultz, Markus Wagner
    http://dl.acm.org/citation.cfm?doid=2739480.2754716
	For more general information, check http://cs.adelaide.edu.au/~optlog/research/ttp.php
	For all instance files, check http://cs.adelaide.edu.au/~optlog/CEC2014COMP_InstancesNew/
	In case of problems, email wagner@acrocon.com
	
	Note: 
	- if nothing is printed, then it is likely that the instance files are missing
	- if you are interested in the final solution in a file in the root directory, 
	  then use the following command once the solution was generated: 
	    newSolution.writeResult(resultTitle);
	 */
	
	    
    @Test
	public void testComputeObjectiveNoPacking() {
    	File ttpFile = new File("resources/ttp/a280_n1395_uncorr-similar-weights_05.ttp");
        TTPInstance instance = new TTPInstance(ttpFile);
        
        // generate a Linkern tour (or read it if it already exists)
        int[] tour = Utils.linkernTour(instance);
       
        // do not pack anything this time
        TTPSolution newSolution = new TTPSolution(tour, new int[instance.numberOfItems]);
        
        // run the objective score calculation
        instance.evaluate(newSolution, false);
    
        // print some information on the screen (the second but last number is the objective score)
//        System.out.print(ttpFile.getName()+": ");
//        newSolution.println();
        
        assertEquals(newSolution.finalCapacityFree,637010,0);
        assertEquals(newSolution.finalWeight,0,0);
        assertEquals(newSolution.finalProfit,0,0);
        assertEquals(newSolution.finalDistance,2613,0);
        assertEquals(newSolution.finalTravelTime,2613,0);
        assertEquals(newSolution.objectiveScore,-189965.1,0); // this is the important one
    }
    
    @Test
	public void testComputeObjectiveTakeOneItem1() {
    	File ttpFile = new File("resources/ttp/a280_n1395_uncorr-similar-weights_05.ttp");
        TTPInstance instance = new TTPInstance(ttpFile);
        int[] tour = Utils.linkernTour(instance);
        int[] packing = new int[instance.numberOfItems];
        
        // pack the first item: id=1, profit=1, weight=1008, in city 2
        packing[0] = 1;
        TTPSolution newSolution = new TTPSolution(tour, packing);
        
        // run the objective score calculation
        instance.evaluate(newSolution, false);
            
        assertEquals(newSolution.finalCapacityFree,636002,0);
        assertEquals(newSolution.finalWeight,1008,0);
        assertEquals(newSolution.finalProfit,1,0);
        assertEquals(newSolution.finalDistance,2613,0);
        assertEquals(newSolution.finalTravelTime,2616.6980965969638,0);
        assertEquals(newSolution.objectiveScore,-190232.95162259927,0); // this is the important one
    }
    
    @Test
	public void testComputeObjectiveTakeOneItem2() {
    	File ttpFile = new File("resources/ttp/a280_n1395_uncorr-similar-weights_05.ttp");
        TTPInstance instance = new TTPInstance(ttpFile);
        int[] tour = Utils.linkernTour(instance);
        int[] packing = new int[instance.numberOfItems];
        
        // pack the second item (==second item in city 2): id=280, profit=375, weight=1008, in city 2
        packing[1] = 1;
        TTPSolution newSolution = new TTPSolution(tour, packing);
        
        // run the objective score calculation
        instance.evaluate(newSolution, false);
            
        assertEquals(newSolution.finalCapacityFree,636001,0);
        assertEquals(newSolution.finalWeight,1009,0);
        assertEquals(newSolution.finalProfit,375,0);
        assertEquals(newSolution.finalDistance,2613,0);
        assertEquals(newSolution.finalTravelTime,2616.701770581105,0);
        assertEquals(newSolution.objectiveScore,-189859.21872124635,0); // this is the important one
    }
    
    @Test
	public void testComputeObjectiveTakeFiveItems() {
    	File ttpFile = new File("resources/ttp/a280_n1395_uncorr-similar-weights_05.ttp");
        TTPInstance instance = new TTPInstance(ttpFile);
        int[] tour = Utils.linkernTour(instance);
        int[] packing = new int[instance.numberOfItems];
        
        /* pack all the five items that are in city with id=3 
         * (this is a very late city, as the travel sequence in the given 
         *  file a280.linkern.tour is "... 279, 3, 280, 0")
         *  
         * the item ids are: [241, 520, 799, 1078, 1357]
         * 
         * INDEX, PROFIT, WEIGHT, ASSIGNED NODE NUMBER:
         * 241	341	1006	242
         * 520	681	1000	242
         * 799	618	1006	242
         * 1078	485	1006	242
         * 1357	134	1006	242
         */
        packing[5] = 1;
        packing[6] = 1;
        packing[7] = 1;
        packing[8] = 1;
        packing[9] = 1;
        
        TTPSolution newSolution = new TTPSolution(tour, packing);
        
        // run the objective score calculation
        instance.evaluate(newSolution, false);
            
        assertEquals(newSolution.finalCapacityFree,631986,0);
        assertEquals(newSolution.finalWeight,5024,0);
        assertEquals(newSolution.finalProfit,2259,0);
        assertEquals(newSolution.finalDistance,2613,0);
        assertEquals(newSolution.finalTravelTime,2631.394134659233,0);
        assertEquals(newSolution.objectiveScore,-189043.35358972623,0); // this is the important one
    }
    
    
    
    
//    // note: run some manual tests
//    public static void main(String[] args)
//    {
//    	File ttpFile = new File("resources/ttp/a280_n1395_uncorr-similar-weights_05.ttp");
//        TTPInstance instance = new TTPInstance(ttpFile);
//        
//        // generate a Linkern tour (or read it if it already exists)
//        int[] tour = Utils.linkernTour(instance);
//        
//        long startTime = System.currentTimeMillis();
//
//        System.out.print(ttpFile.getName()+": ");
//
//        // do not pack anything this time
//        int[] packing = new int[instance.numberOfItems];
//        
//        // pack the first item
//        packing[1] = 1;
////        packing[5] = 1;
////        packing[6] = 1;
////        packing[7] = 1;
////        packing[8] = 1;
////        packing[9] = 1;
//        
//        TTPSolution newSolution = new TTPSolution(tour, packing);
//        
//        // run the objective score calculation
//        instance.evaluate(newSolution, false);
//    
//        newSolution.computationTime = System.currentTimeMillis() - startTime;
//
//        // print some information on the screen (the second but last number is the objective score)
//        newSolution.println();
//        
//        // uncomment the following three lines if you want to write the result to disk
////      String resultTitle = "results/";
////      resultTitle += instance.file.getName() + ".packNothing." + startTime;
////      newSolution.writeResult(resultTitle); // write solution to file
//        
//        // print some additional information on the screen
////            newSolution.printFull();
////            newSolution.printFullForCCode();
//        System.out.println(newSolution.answer());
//        
////        // 151124: Freiburg study 
////        System.out.println("151124: Freiburg Study");
////        instance.evaluate(newSolution, true);
////        newSolution.altPrint();
//    }
}
