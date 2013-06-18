package org.vpac.ndg.task;

import org.vpac.ndg.application.Constant;
import org.vpac.ndg.exceptions.TaskException;

/**
 * This is just dummy task class which doesn't perform any operation.
 * @author hsumanto
 *
 */
public class NoOperation extends Task {

	public NoOperation(String description) {
		super(Constant.TASK_DESCRIPTION_NOOP);
	}

	@Override
	public void initialise() {
		// Do nothing
	}

	@Override
	public void execute() throws TaskException {
		// Do nothing
	}

	@Override
	public void rollback() {
		// Do nothing
	}

	@Override
	public void finalise() {
		// Do nothing
	}

}
