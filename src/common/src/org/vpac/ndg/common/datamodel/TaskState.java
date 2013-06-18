package org.vpac.ndg.common.datamodel;

public enum TaskState {
	// Stored as a string in the database. Reordering is OK.
	RUNNING,
	CANCELLING,
	FINISHED,
	INITIALISATION_ERROR,
	EXECUTION_ERROR
}
