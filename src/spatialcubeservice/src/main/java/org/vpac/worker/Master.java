package org.vpac.worker;

import java.io.Serializable;

public class Master {
	public static final class Ack implements Serializable {
		final String workId;
		
		public Ack(String workId) {
			this.workId = workId;
		}
		
		@Override
		public String toString() {
			return "Ack{" +
				"workId='" + workId + "\'" +
				"}";
		}
	}
}
