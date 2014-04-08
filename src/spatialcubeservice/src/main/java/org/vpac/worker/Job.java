package org.vpac.worker;

import java.io.Serializable;

import org.vpac.ndg.geometry.Box;

import ucar.nc2.NetcdfFileWriter.Version;

public class Job {

 public static final class Work implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public final String workId;
    //public final Object job;
    public final String queryDefinitionString;
//    public final QueryProgress qp;
    public final String outputPath;
    public final Version netcdfVersion;
    public final Box bound;

    public Work(String workId, String queryDefinitionString, String path, Version ver, Box bound) {
      this.workId = workId;
      this.queryDefinitionString = queryDefinitionString;
      this.outputPath = path;
      this.netcdfVersion = ver;
      this.bound = bound;
      //this.job = job;
    }

    @Override
    public String toString() {
      return "Work{" +
        "workId='" + workId + '\'' +
        ", qd=" + queryDefinitionString +
        ", path=" + outputPath +
        ", ver=" + netcdfVersion +
        ", bound=" + bound +
        '}';
    }
 
 }
 
}
