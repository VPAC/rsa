package org.vpac.ndg.storagemanager;

import org.aspectj.lang.JoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.vpac.ndg.common.datamodel.TaskState;
import org.vpac.ndg.storage.dao.ActivityInfoDao;
import org.vpac.ndg.storage.dao.UploadDao;
import org.vpac.ndg.storage.model.ActivityInfo;
import org.vpac.ndg.storage.model.Band;
import org.vpac.ndg.storage.model.Dataset;
import org.vpac.ndg.storage.model.TimeSlice;
import org.vpac.ndg.storage.model.Upload;

/**
 * Class containing all the interceptor method which to be executed whenever the
 * pointcut condition in StorageManagerBean.xml is satisfied. This logging
 * feature is achieved by utilising the springframework AOP capability.
 * 
 * @author hsumanto
 */
public class ActivityLogger {
	final private Logger log = LoggerFactory.getLogger(ActivityLogger.class);

	@Autowired
	UploadDao uploadDao;
	@Autowired
	ActivityInfoDao activityDao;

	/**
	 * After method to be executed when successful in creating a dataset.
	 * 
	 * @param jp
	 *            The specified join point parameter.
	 */
	public void afterCreateDatasetReturning(JoinPoint jp) {
		if (jp.getArgs().length != 1) {
			log.error("Invalid arguments detected during afterCreateDatasetReturning intercepting method.");
			return;
		}

		Dataset ds = (Dataset) jp.getArgs()[0];

		String log = String.format("rsa dataset create \"%s\" %s", ds.getName(),
				ds.getResolution());
		if (ds.getAbst() != null && !ds.getAbst().isEmpty()) {
			log += String.format(" --abstract \"%s\"", ds.getAbst());
		}

		addLog(log, String.format("Creating %s", ds), TaskState.FINISHED);
	}

	/**
	 * After method to be executed when failure in creating a dataset.
	 * 
	 * @param jp
	 *            The specified join point parameter.
	 */
	public void afterCreateDatasetThrowing(JoinPoint jp, Throwable error) {
		if (jp.getArgs().length != 1) {
			log.error("Invalid arguments detected during afterCreateDatasetThrowing intercepting method.");
			return;
		}

		Dataset ds = (Dataset) jp.getArgs()[0];

		String log = String.format("rsa dataset create \"%s\" %s", ds.getName(),
				ds.getResolution());
		if (ds.getAbst() != null && !ds.getAbst().isEmpty()) {
			log += String.format(" --abstract \"%s\"", ds.getAbst());
		}

		addLog(log, error.getMessage(), TaskState.EXECUTION_ERROR);
	}

	/**
	 * After method to be executed when successful in renaming a dataset.
	 * 
	 * @param jp
	 *            The specified join point parameter.
	 */
	public void afterRenameDatasetReturning(JoinPoint jp) {
		if (jp.getArgs().length != 1) {
			log.error("Invalid arguments detected during afterRenameDatasetReturning intercepting method.");
			return;
		}

		Dataset ds = (Dataset) jp.getArgs()[0];

		addLog(String.format("rsa dataset update %s --name \"%s\"",
				ds.getId(), ds.getName()),
				String.format("Renaming dataset name into %s", ds),
				TaskState.FINISHED);
	}

	/**
	 * After method to be executed when failure in renaming a dataset.
	 * 
	 * @param jp
	 *            The specified join point parameter.
	 */
	public void afterRenameDatasetThrowing(JoinPoint jp, Throwable error) {
		if (jp.getArgs().length != 1) {
			log.error("Invalid arguments detected during afterRenameDatasetThrowing intercepting method.");
			return;
		}

		Dataset ds = (Dataset) jp.getArgs()[0];

		addLog(String.format("rsa dataset update %s --name \"%s\"",
				ds.getId(), ds.getName()), error.getMessage(),
				TaskState.EXECUTION_ERROR);
	}

	/**
	 * After method to be executed when successful in updating a dataset.
	 * 
	 * @param jp
	 *            The specified join point parameter.
	 */
	public void afterUpdateDatasetReturning(JoinPoint jp) {
		if (jp.getArgs().length != 1) {
			log.error("Invalid arguments detected during afterUpdateDatasetReturning intercepting method.");
			return;
		}

		Dataset ds = (Dataset) jp.getArgs()[0];

		addLog(String.format("rsa dataset update %s --abstract \"%s\"",
				ds.getId(), ds.getAbst()),
				String.format("Updating info on %s", ds), TaskState.FINISHED);
	}

	/**
	 * After method to be executed when failure in updating a dataset.
	 * 
	 * @param jp
	 *            The specified join point parameter.
	 */
	public void afterUpdateDatasetThrowing(JoinPoint jp, Throwable error) {
		if (jp.getArgs().length != 1) {
			log.error("Invalid arguments detected during afterUpdateDatasetThrowing intercepting method.");
			return;
		}

		Dataset ds = (Dataset) jp.getArgs()[0];

		addLog(String.format("rsa dataset update %s --abstract \"%s\"",
				ds.getId(), ds.getAbst()), error.getMessage(),
				TaskState.EXECUTION_ERROR);
	}

	/**
	 * After method to be executed when successful in deleting a dataset.
	 * 
	 * @param jp
	 *            The specified join point parameter.
	 */
	public void afterDeleteDatasetReturning(JoinPoint jp) {
		if (jp.getArgs().length != 1) {
			log.error("Invalid arguments detected during afterDeleteDataset intercepting method.");
			return;
		}

		Dataset ds = (Dataset) jp.getArgs()[0];

		addLog(String.format("rsa dataset delete %s", ds.getId()),
				String.format("Deleting %s", ds), TaskState.FINISHED);
	}

	/**
	 * After method to be executed when failure in deleting a dataset.
	 * 
	 * @param jp
	 *            The specified join point parameter.
	 */
	public void afterDeleteDatasetThrowing(JoinPoint jp, Throwable error) {
		if (jp.getArgs().length != 1) {
			log.error("Invalid arguments detected during afterDeleteDatasetThrowing intercepting method.");
			return;
		}

		Dataset ds = (Dataset) jp.getArgs()[0];

		addLog(String.format("rsa dataset delete %s", ds.getId()),
				error.getMessage(), TaskState.EXECUTION_ERROR);
	}

	/**
	 * After method to be executed when successfully adding a band to a dataset.
	 * 
	 * @param jp
	 *            The specified join point parameter.
	 */
	public void afterAddBandReturning(JoinPoint jp) {
		if (jp.getArgs().length != 2) {
			log.error("Invalid arguments detected during afterAddBandReturning intercepting method.");
			return;
		}

		String datasetId = (String) jp.getArgs()[0];
		Band band = (Band) jp.getArgs()[1];

		String log = String.format("rsa band create %s \"%s\"", datasetId,
				band.getName());
		if (band.getType() != null) {
			log += String.format(" --type %s", band.getType());
		}
		if (band.getNodata() != null && !band.getNodata().isEmpty()) {
			log += String.format(" --nodata %s", band.getNodata());
		}
		addLog(log, String.format("Creating %s", band), TaskState.FINISHED);
	}

	/**
	 * After method to be executed when failing to add a band to a dataset.
	 * 
	 * @param jp
	 *            The specified join point parameter.
	 */
	public void afterAddBandThrowing(JoinPoint jp, Throwable error) {
		if (jp.getArgs().length != 2) {
			log.error("Invalid arguments detected during afterAddBandThrowing intercepting method.");
			return;
		}

		String datasetId = (String) jp.getArgs()[0];
		Band band = (Band) jp.getArgs()[1];

		String log = String.format("rsa band create %s \"%s\"", datasetId,
				band.getName());
		if (band.getType() != null) {
			log += String.format(" --type %s", band.getType());
		}
		if (band.getNodata() != null && !band.getNodata().isEmpty()) {
			log += String.format(" --nodata %s", band.getNodata());
		}
		addLog(log, error.getMessage(), TaskState.EXECUTION_ERROR);
	}

	/**
	 * After method to be executed when successful in renaming a dataset band
	 * name.
	 * 
	 * @param jp
	 *            The specified join point parameter.
	 */
	public void afterRenameBandReturning(JoinPoint jp) {
		if (jp.getArgs().length != 1) {
			log.error("Invalid arguments detected during afterRenameBandReturning intercepting method.");
			return;
		}

		Band band = (Band) jp.getArgs()[0];

		addLog(String.format("rsa band update %s --name \"%s\"", band.getId(),
				band.getName()), String.format("Renaming band name into %s",
				band), TaskState.FINISHED);
	}

	/**
	 * After method to be executed when failure in renaming a dataset band name.
	 * 
	 * @param jp
	 *            The specified join point parameter.
	 */
	public void afterRenameBandThrowing(JoinPoint jp, Throwable error) {
		if (jp.getArgs().length != 1) {
			log.error("Invalid arguments detected during afterRenameBandThrowing intercepting method.");
			return;
		}

		Band band = (Band) jp.getArgs()[0];

		addLog(String.format("rsa band update %s --name \"%s\"", band.getId(),
				band.getName()), error.getMessage(), TaskState.EXECUTION_ERROR);
	}

	/**
	 * After method to be executed when successful in updating a dataset.
	 * 
	 * @param jp
	 *            The specified join point parameter.
	 */
	public void afterUpdateBandReturning(JoinPoint jp) {
		if (jp.getArgs().length != 1) {
			log.error("Invalid arguments detected during afterUpdateBandReturning intercepting method.");
			return;
		}

		Band band = (Band) jp.getArgs()[0];

		addLog(String.format(
				"rsa band update %s --type %s --continuous %s -- metadata %s",
				band.getId(), band.getType(), band.isContinuous(),
				band.isMetadata()), String.format("Updating info on %s", band),
				TaskState.FINISHED);
	}

	/**
	 * After method to be executed when failure in updating a dataset.
	 * 
	 * @param jp
	 *            The specified join point parameter.
	 */
	public void afterUpdateBandThrowing(JoinPoint jp, Throwable error) {
		if (jp.getArgs().length != 1) {
			log.error("Invalid arguments detected during afterUpdateBandThrowing intercepting method.");
			return;
		}

		Band band = (Band) jp.getArgs()[0];

		addLog(String.format(
				"rsa band update %s --type %s --continuous %s -- metadata %s",
				band.getId(), band.getType(), band.isContinuous(),
				band.isMetadata()), error.getMessage(),
				TaskState.EXECUTION_ERROR);
	}

	/**
	 * After method to be executed when successfully deleting a band from a
	 * dataset.
	 * 
	 * @param jp
	 *            The specified join point parameter.
	 */
	public void afterDeleteBandReturning(JoinPoint jp) {
		if (jp.getArgs().length != 1) {
			log.error("Invalid arguments detected during afterDeleteBandReturning intercepting method.");
			return;
		}

		Band band = (Band) jp.getArgs()[0];

		addLog(String.format("rsa band delete %s", band.getId()),
				String.format("Deleting %s", band), TaskState.FINISHED);
	}

	/**
	 * After method to be executed when failing to add a band to a dataset.
	 * 
	 * @param jp
	 *            The specified join point parameter.
	 */
	public void afterDeleteBandThrowing(JoinPoint jp, Throwable error) {
		if (jp.getArgs().length != 1) {
			log.error("Invalid arguments detected during afterDeleteBandThrowing intercepting method.");
			return;
		}

		Band band = (Band) jp.getArgs()[0];

		addLog(String.format("rsa band delete %s", band.getId()),
				error.getMessage(), TaskState.EXECUTION_ERROR);
	}

	/**
	 * After method to be executed when successful in adding a timeslice to a
	 * dataset.
	 * 
	 * @param jp
	 *            The specified join point parameter.
	 */
	public void afterAddTimeSliceReturning(JoinPoint jp) {
		if (jp.getArgs().length != 2) {
			log.error("Invalid arguments detected during afterAddTimeSliceReturning intercepting method.");
			return;
		}

		String datasetId = (String) jp.getArgs()[0];
		TimeSlice ts = (TimeSlice) jp.getArgs()[1];

		String log = String.format("rsa timeslice create %s \"%s\"", datasetId,
				ts.getCreated());
		if (ts.getDataAbstract() != null && !ts.getDataAbstract().isEmpty()) {
			log += String.format(" --abstract \"%s\"", ts.getDataAbstract());
		}

		addLog(log, String.format("Creating %s", ts), TaskState.FINISHED);
	}

	/**
	 * After method to be executed when failure in adding a timeslice to a
	 * dataset.
	 * 
	 * @param jp
	 *            The specified join point parameter.
	 */
	public void afterAddTimeSliceThrowing(JoinPoint jp, Throwable error) {
		if (jp.getArgs().length != 2) {
			log.error("Invalid arguments detected during afterAddTimeSliceThrowing intercepting method.");
			return;
		}

		String datasetId = (String) jp.getArgs()[0];
		TimeSlice ts = (TimeSlice) jp.getArgs()[1];

		String log = String.format("rsa timeslice create %s \"%s\"", datasetId,
				ts.getCreated());
		if (ts.getDataAbstract() != null && !ts.getDataAbstract().isEmpty()) {
			log += String.format(" --abstract \"%s\"", ts.getDataAbstract());
		}

		addLog(log, error.getMessage(), TaskState.EXECUTION_ERROR);
	}

	/**
	 * After method to be executed when successful in renaming a dataset
	 * timeslice name.
	 * 
	 * @param jp
	 *            The specified join point parameter.
	 */
	public void afterRenameTimeSliceReturning(JoinPoint jp) {
		if (jp.getArgs().length != 1) {
			log.error("Invalid arguments detected during afterRenameTimeSliceReturning intercepting method.");
			return;
		}

		TimeSlice ts = (TimeSlice) jp.getArgs()[0];

		String log = String
				.format("rsa timeslice update %s --xmin %s --ymin %s --xmax %s --ymax %s",
						ts.getId(), ts.getXmin(), ts.getYmin(), ts.getXmax(),
						ts.getYmax());
		if (ts.getDataAbstract() != null && !ts.getDataAbstract().isEmpty()) {
			log += String.format(" --abstract \"%s\"", ts.getDataAbstract());
		}

		addLog(log, String.format("Changing acquisition time into %s", ts),
				TaskState.FINISHED);
	}

	/**
	 * After method to be executed when failure in renaming a dataset timeslice
	 * name.
	 * 
	 * @param jp
	 *            The specified join point parameter.
	 */
	public void afterRenameTimeSliceThrowing(JoinPoint jp, Throwable error) {
		if (jp.getArgs().length != 1) {
			log.error("Invalid arguments detected during afterRenameTimeSliceThrowing intercepting method.");
			return;
		}

		TimeSlice ts = (TimeSlice) jp.getArgs()[0];

		String log = String
				.format("rsa timeslice update %s --xmin %s --ymin %s --xmax %s --ymax %s",
						ts.getId(), ts.getXmin(), ts.getYmin(), ts.getXmax(),
						ts.getYmax());
		if (ts.getDataAbstract() != null && !ts.getDataAbstract().isEmpty()) {
			log += String.format(" --abstract \"%s\"", ts.getDataAbstract());
		}

		addLog(log, error.getMessage(), TaskState.EXECUTION_ERROR);
	}

	/**
	 * After method to be executed when successful in updating a timeslice.
	 * 
	 * @param jp
	 *            The specified join point parameter.
	 */
	public void afterUpdateTimeSliceReturning(JoinPoint jp) {
		if (jp.getArgs().length != 1) {
			log.error("Invalid arguments detected during afterUpdateTimeSliceReturning intercepting method.");
			return;
		}

		TimeSlice ts = (TimeSlice) jp.getArgs()[0];

		String log = String
				.format("rsa timeslice update %s --xmin %s --ymin %s --xmax %s --ymax %s",
						ts.getId(), ts.getXmin(), ts.getYmin(), ts.getXmax(),
						ts.getYmax());
		if (ts.getDataAbstract() != null && !ts.getDataAbstract().isEmpty()) {
			log += String.format(" --abstract \"%s\"", ts.getDataAbstract());
		}

		addLog(log, String.format("Updating info on %s", ts),
				TaskState.FINISHED);
	}

	/**
	 * After method to be executed when failure in adding a timeslice.
	 * 
	 * @param jp
	 *            The specified join point parameter.
	 */
	public void afterUpdateTimeSliceThrowing(JoinPoint jp, Throwable error) {
		if (jp.getArgs().length != 1) {
			log.error("Invalid arguments detected during afterUpdateTimeSliceThrowing intercepting method.");
			return;
		}

		TimeSlice ts = (TimeSlice) jp.getArgs()[0];

		String log = String
				.format("rsa timeslice update %s --xmin %s --ymin %s --xmax %s --ymax %s",
						ts.getId(), ts.getXmin(), ts.getYmin(), ts.getXmax(),
						ts.getYmax());
		if (ts.getDataAbstract() != null && !ts.getDataAbstract().isEmpty()) {
			log += String.format(" --abstract \"%s\"", ts.getDataAbstract());
		}

		addLog(log, error.getMessage(), TaskState.EXECUTION_ERROR);
	}

	/**
	 * After method to be executed when successful in adding a timeslice to a
	 * dataset.
	 * 
	 * @param jp
	 *            The specified join point parameter.
	 */
	public void afterDeleteTimeSliceReturning(JoinPoint jp) {
		if (jp.getArgs().length != 1) {
			log.error("Invalid arguments detected during afterDeleteTimeSliceReturning intercepting method.");
			return;
		}

		TimeSlice ts = (TimeSlice) jp.getArgs()[0];

		addLog(String.format("rsa timeslice delete %s", ts.getId()),
				String.format("Deleting %s", ts), TaskState.FINISHED);
	}

	/**
	 * After method to be executed when failure in adding a timeslice to a
	 * dataset.
	 * 
	 * @param jp
	 *            The specified join point parameter.
	 */
	public void afterDeleteTimeSliceThrowing(JoinPoint jp, Throwable error) {
		if (jp.getArgs().length != 1) {
			log.error("Invalid arguments detected during afterDeleteTimeSliceThrowing intercepting method.");
			return;
		}

		TimeSlice ts = (TimeSlice) jp.getArgs()[0];

		addLog(String.format("rsa timeslice delete %s", ts.getId()),
				error.getMessage(), TaskState.EXECUTION_ERROR);
	}

	/**
	 * After method to be executed after successful import.
	 * 
	 * @param jp
	 *            The specified join point parameter.
	 */
	public void afterCmdClientImportDataReturning(JoinPoint jp) {
		if (jp.getArgs().length != 0) {
			log.error("Invalid arguments detected during afterCmdClientImportData intercepting method.");
			return;
		}

		HasDescription importer = (HasDescription) jp.getTarget();
		if (importer == null) {
			log.error("Invalid class intercepted during afterCmdClientImportData intercepting method.");
			return;
		}
		String logCommand = importer.getDescription();
		String logMessage = "Successful timeslice import on the specified band";

		addLog(logCommand, logMessage, TaskState.FINISHED);
	}

	/**
	 * After method to be executed after failed import.
	 * 
	 * @param jp
	 *            The specified join point parameter.
	 */
	public void afterCmdClientImportDataThrowing(JoinPoint jp, Throwable error) {
		if (jp.getArgs().length != 0) {
			log.error("Invalid arguments detected during afterCmdClientImportData intercepting method.");
			return;
		}

		HasDescription importer = (HasDescription) jp.getTarget();
		if (importer == null) {
			log.error("Invalid class intercepted during afterCmdClientImportData intercepting method.");
			return;
		}
		String logCommand = importer.getDescription();
		String logMessage = error.getMessage();

		addLog(logCommand, logMessage, TaskState.EXECUTION_ERROR);
	}

	/**
	 * After method to be executed after successful import.
	 * 
	 * @param jp
	 *            The specified join point parameter.
	 */
	public void afterImportTimeSliceReturning(JoinPoint jp) {
		if (jp.getArgs().length != 3) {
			log.error("Invalid arguments detected during afterCreateDataset intercepting method.");
			return;
		}

		String uploadId = (String) jp.getArgs()[0];
		Upload upload = uploadDao.retrieve(uploadId);
		String timesliceId = upload.getTimeSliceId();
		String bandId = (String) jp.getArgs()[1];
		String logCommand = String.format("rsa data import %s %s", timesliceId,
				bandId);
		String logMessage = String
				.format("Importing dataset into TS(id=%s) as Band(id=%s) from Upload(id=%s)",
						timesliceId, bandId, uploadId);

		addLog(logCommand, logMessage, TaskState.FINISHED);
	}

	/**
	 * After method to be executed after failed import.
	 * 
	 * @param jp
	 *            The specified join point parameter.
	 */
	public void afterImportTimeSliceThrowing(JoinPoint jp, Throwable error) {
		if (jp.getArgs().length != 3) {
			log.error("Invalid arguments detected during afterCreateDataset intercepting method.");
			return;
		}

		String uploadId = (String) jp.getArgs()[0];
		Upload upload = uploadDao.retrieve(uploadId);
		String timesliceId = upload.getTimeSliceId();
		String bandId = (String) jp.getArgs()[1];
		String logCommand = String.format("rsa data import %s %s", timesliceId,
				bandId);
		String logMessage = error.getMessage();

		addLog(logCommand, logMessage, TaskState.EXECUTION_ERROR);
	}

	/*
	 * Add log into activity info table.
	 */
	private void addLog(String logCommand, String logMessage, TaskState state) {
		if (state == TaskState.FINISHED) {
			log.debug("{} --> Finished: {}", logCommand, logMessage);
		} else if (state == TaskState.EXECUTION_ERROR) {
			log.error("{} --> Execution Error: {}", logCommand, logMessage);
		}
		activityDao.save(new ActivityInfo(logCommand, logMessage, state));
	}
}
