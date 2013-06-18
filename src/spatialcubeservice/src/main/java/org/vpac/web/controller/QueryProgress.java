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
