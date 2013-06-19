package org.vpac.ndg.lock;

import org.vpac.ndg.common.datamodel.RunningTaskState;

public interface HasRunningState {
	void setState(RunningTaskState state);
}
