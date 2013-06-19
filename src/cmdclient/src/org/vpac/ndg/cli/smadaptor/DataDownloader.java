package org.vpac.ndg.cli.smadaptor;

import java.nio.file.Path;

public interface DataDownloader {
	public void Download(String taskId, Path output);
}
