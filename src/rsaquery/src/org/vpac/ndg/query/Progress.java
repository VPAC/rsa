/*
 * This file is part of the Raster Storage Archive (RSA).
 *
 * The RSA is free software: you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * The RSA is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE.  See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * the RSA.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright 2013 CRCSI - Cooperative Research Centre for Spatial Information
 * http://www.crcsi.com.au/
 */

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
