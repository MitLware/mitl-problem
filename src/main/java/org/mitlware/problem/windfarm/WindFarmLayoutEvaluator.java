package org.mitlware.problem.windfarm;

import java.util.ArrayList;

/**
 * The class WindFarmLayoutEvaluator is an interface to easily exchange the
 * evaluation function of the wind farm layouts. The evaluator has to be initialized
 * with a wind scenario before being used to evaluate any layouts with the
 * evaluation function. After evaluation, the output data (energy output per 
 * turbine, per direction, etc.) are available by the means of the corresponding
 * getters.
 */
public abstract class WindFarmLayoutEvaluator {

	/**
	 * Evaluates a given layout and returns its cost of energy
	 * @param layout The layout to evaluate
	 * @return the cost of energy (positive) 
	 * and max_double if the layout is invalid
	 */
	public abstract double evaluate(double[][] layout);

	/**
	 * Returns the energy outputs per wind turbine and per direction of the last
	 * layout evaluated, ordered as in the layout vector provided to the
	 * evaluation method and the wind scenario wind directions.
	 * A layout must have been evaluated before this method is called.
	 * @return The energy outputs; null if no layout have been evaluated
	 */
	public abstract double[][] getEnergyOutputs();

	/**
	 * Returns the wake free ratio per wind turbine of the last layout
	 * evaluated, ordered as in the layout vector provided in the evaluation
	 * method.
	 * A layout must have been evaluated before this method is called.
	 * @return The wake free ratio per turbine
	 */
	public abstract double[] getTurbineFitnesses();

	/**
	 * Returns the global energy output of the last layout evaluated.
	 * A layout must have been evaluated before this method is called. 
	 * @return The global energy output
	 */
	public abstract double getEnergyOutput();

	/**
	 * Returns the global wake free ratio of the last layout evaluated.
	 * A layout must have been evaluated before this method is called. 
	 * @return The global wake free ratio
	 */
	public abstract double getWakeFreeRatio();

	/**
	 * Returns the energy cost of the last layout evaluated.
	 * A layout must have been evaluated before this method is called. 
	 * @return The energy cost
	 */
	public abstract double getEnergyCost();

	/**
	 * Check if a layout violate or not the constraints of a wind farm. It checks:
	 *     - no turbine violates the security distance
	 *     - no turbine is out of the farm
	 *     - no turbine is inside an obstacle
	 * @param layout The layout to check
	 * @result true if the layout is valid, false otherwise
	 */
	public abstract boolean checkConstraint(double layout[][]);

	/**
	 * Returns the radius of one turbine
	 */
	public abstract double getTurbineRadius();

	/**
	 * Returns the farm width
	 */
	public abstract double getFarmWidth();
    
	/**
	 * Returns the farm heigth
	 */
	public abstract double getFarmHeight();

	/**
	 * Return the obstacles of the farm
	 * @return an array of [xmin, ymin, xmax, ymax] for each obstacle.
	 */
	public abstract double[][] getObstacles();
	
	public abstract ArrayList<double[]> getGrid();
}
