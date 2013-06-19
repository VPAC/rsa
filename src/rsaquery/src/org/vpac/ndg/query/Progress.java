package org.vpac.ndg.query;

/**
 * Interface for progress reporting for long-running tasks.
 * @author Alex Fraser
 */
public interface Progress {

	/**
	 * @param nsteps The total number of major steps that will be taken.
	 */
	void setNsteps(int nsteps);

	/**
	 * Start a new step.
	 * @param step The number of the step (starting from 1).
	 * @param message A description of the step.
	 */
	void setStep(int step, String message);

	/**
	 * Called just after a step finishes, and before setStep is called again.
	 */
	void finishedStep();

	/**
	 * @param totalQuanta The total number of countable objects that will be
	 *        processed, across all steps.
	 */
	void setTotalQuanta(long totalQuanta);

	/**
	 * Indicate that processing has progressed.
	 *
	 * @param volume The number of items that have been processed since the last
	 *        time this method was called (or since the Progress object was
	 *        created if this is the fist call to this method).
	 */
	void addProcessedQuanta(long volume);

	/**
	 * Called after all steps are complete.
	 */
	void finished();

}
