/*
 * This file is part of the Raster Storage Archive (RSA).
 *
 * The RSA is free software: you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * The RSA is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE.  See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * the RSA.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright 2013 CRCSI - Cooperative Research Centre for Spatial Information
 * http://www.crcsi.com.au/
 */

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
