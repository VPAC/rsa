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

package org.vpac.web.controller;

import org.vpac.ndg.common.datamodel.TaskState;
import org.vpac.ndg.common.datamodel.TaskType;
import org.vpac.ndg.query.Progress;
import org.vpac.ndg.storage.dao.JobProgressDao;
import org.vpac.ndg.storage.model.JobProgress;

public class QueryProgress implements Progress {

	private long totalQuanta;
	private long totalProcessedQuanta;
	private JobProgress jobProgress;
	JobProgressDao jobProgressDao;
	
	public String getTaskId() {
		return jobProgress.getId();
	}

	public QueryProgress(JobProgressDao dao) {
		this.jobProgressDao = dao;
		jobProgress = new JobProgress();
		jobProgress.setNumberOfSteps(1);
		jobProgress.setTaskType(TaskType.Query);
		jobProgressDao.save(jobProgress);
	}

	@Override
	public void setNsteps(int nsteps) {
	}

	@Override
	public void setStep(int step, String message) {
		jobProgress.setStepDescription(message);
		jobProgressDao.save(jobProgress);
	}

	@Override
	public void setTotalQuanta(long totalQuanta) {
		this.totalQuanta = totalQuanta;
	}

	@Override
	public void addProcessedQuanta(long processedQuanta) {
		totalProcessedQuanta += processedQuanta;
		double fraction = ((double)totalProcessedQuanta / (double)totalQuanta) * 100;
		jobProgress.setCurrentStepProgress(fraction);
		jobProgressDao.save(this.jobProgress);
	}

	@Override
	public void finishedStep() {
	}

	@Override
	public void finished() {
		jobProgress.setCompleted();
		jobProgressDao.save(this.jobProgress);
	}
	
	public void setErrorMessage(String message) {
		jobProgress.setErrorMessage(message);
		jobProgress.setState(TaskState.EXECUTION_ERROR);
		jobProgressDao.save(this.jobProgress);
	}
}
