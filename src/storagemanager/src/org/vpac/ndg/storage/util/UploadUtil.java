package org.vpac.ndg.storage.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.vpac.ndg.configuration.NdgConfigManager;
import org.vpac.ndg.storage.model.Upload;

public class UploadUtil {
	NdgConfigManager ndgConfigManager;

	public NdgConfigManager getNdgConfigManager() {
		return ndgConfigManager;
	}

	public void setNdgConfigManager(NdgConfigManager ndgConfigManager) {
		this.ndgConfigManager = ndgConfigManager;
	}

	public UploadUtil() {
	}

	public void createDirectory(Upload u) throws IOException {
		Path uPath = getDirectory(u);
		Files.createDirectories(uPath);
	}

	public Path getDirectory(Upload u) throws IOException {
		String uploadDir = getNdgConfigManager().getConfig().getDefaultUploadLocation();
		Path rootDirectory = Paths.get(uploadDir);
		Path dir = rootDirectory.resolve(u.getFileId());
		return dir;
	}

}
