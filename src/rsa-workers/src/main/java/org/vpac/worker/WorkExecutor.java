package org.vpac.worker;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.vpac.ndg.query.Query;
import org.vpac.ndg.query.QueryConfigurationException;
import org.vpac.ndg.query.QueryDefinition;
import org.vpac.worker.Job.Work;

import ucar.nc2.NetcdfFileWriter;
import ucar.nc2.NetcdfFileWriter.Version;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class WorkExecutor extends UntypedActor {

  private LoggingAdapter log = Logging.getLogger(getContext().system(), this);

  @Override
  public void onReceive(Object message) throws Exception {
    if (message instanceof Integer) {
      Integer n = (Integer) message;
      int n2 = n.intValue() * n.intValue();
      String result = n + " * " + n + " = " + n2;
      log.debug("Produced result {}", result);
      getSender().tell(new Worker.WorkComplete(result), getSelf());
    } else if (message instanceof Work) {
    	Work work = (Work) message;
    	String result = null;

    	final QueryDefinition qd = QueryDefinition.fromString(work.queryDefinitionString);
    	qd.output.grid.bounds = String.format("%f %f %f %f", work.bound.getXMin(), work.bound.getYMin(), work.bound.getXMax(), work.bound.getYMax());
    	//log.debug("QD Bound {}", qd.output.grid.bounds);
		final WorkProgress wp = new WorkProgress(work.workId);
		Path outputDir = Paths.get("output/" + work.workId + "/");
//		final Path queryPath = outputDir.resolve(work.outputPath);
		final Path queryPath = outputDir.resolve("out.nc");
		if (!Files.exists(outputDir))
			try {
				Files.createDirectories(outputDir);
			} catch (IOException e1) {
				log.error("directory creation error:", e1);
				e1.printStackTrace();
				throw e1;
			}

		try {
			executeQuery(qd, wp, queryPath, work.netcdfVersion);
		} catch (Exception e) {
			wp.setErrorMessage(e.getMessage());
			log.error("Task exited abnormally: ", e);
			throw e;
		}
        
        log.debug("Produced result {}", result);
        getSender().tell(new Worker.WorkComplete(result), getSelf());
    }
  }
	private void executeQuery(QueryDefinition qd, WorkProgress wp,
			Path outputPath, Version netcdfVersion)
			throws IOException, QueryConfigurationException {
		NetcdfFileWriter outputDataset = NetcdfFileWriter.createNew(
				netcdfVersion, outputPath.toString());
	
		try {
			Query q = new Query(outputDataset);
			q.setNumThreads(1);
			q.setMemento(qd, "preview:");
			try {
				q.setProgress(wp);
				q.run();
			} finally {
				q.close();
			}
		} finally {
			try {
				outputDataset.close();
			} catch (Exception e) {
				log.error("Failed to close output file", e);
			}
		}
	}
}
