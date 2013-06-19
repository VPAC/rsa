package org.vpac.ndg.cli.smadaptor.local;

import java.nio.file.Path;

import org.vpac.ndg.FileUtils;
import org.vpac.ndg.cli.smadaptor.DataDownloader;
import org.vpac.ndg.task.Exporter;

public class LocalDataDownloader implements DataDownloader {

	@Override
	public void Download(String taskId, Path output) {
		try {
			Path p = Exporter.findOutputPath(taskId);
			FileUtils.copy(p, output);
		}
		catch(Exception e) {
		}
		finally {
			
		}
	}
}
