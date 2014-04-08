package org.vpac.worker;

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
import org.vpac.ndg.query.Progress;

public class WorkProgress implements Progress {

	private long totalQuanta;
	private long totalProcessedQuanta;
	private int totalSteps;
	private int currentStep;
	private long currentProgress;
	
	private String workId;
	
	public String getWorkId() {
		return workId;
	}

	public WorkProgress(String workId) {
		this.workId = workId;
	}

	@Override
	public void setNsteps(int nsteps) {
		this.totalSteps = nsteps;
	}

	@Override
	public void setStep(int step, String message) {
	}

	@Override
	public void setTotalQuanta(long totalQuanta) {
		this.totalQuanta = totalQuanta;
	}

	@Override
	public void addProcessedQuanta(long processedQuanta) {
		totalProcessedQuanta += processedQuanta;
		double fraction = ((double)totalProcessedQuanta / (double)totalQuanta) * 100;
	}

	@Override
	public void finishedStep() {
	}

	@Override
	public void finished() {
		this.currentProgress = 100;
	}
	
	public void setErrorMessage(String message) {
	}
}

