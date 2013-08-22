/*
 * This file is part of SpatialCube.
 *
 * SpatialCube is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * SpatialCube is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * SpatialCube.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright 2013 CRCSI - Cooperative Research Centre for Spatial Information
 * http://www.crcsi.com.au/
 */

package org.vpac.ndg.rasterservices;

import java.io.File;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vpac.ndg.common.datamodel.Format;
import org.vpac.ndg.geometry.Projection;

/**
 * This class creates ncml aggregation for spatial tiling based on the specified vrt for all the specified tiles.
 * @author hsumanto
 *
 */
public class NcmlCreator {

	final static private Logger log = LoggerFactory.getLogger(NcmlCreator.class);

	/**
	 * Creates ncml aggregation for spatial tiling based on the specified vrt for all the specified tiles
	 * and stores the result at the specified ncml file location.
	 * @param vrtInfo The specified vrt
	 * @param tileList The list of all tiles names
	 * @param ncmlFile The location of ncml file
	 * @throws IOException 
	 */
	public static void createNcml(FileInformation vrtInfo, List<Path> tileList, Path ncmlFile) throws IOException {		
		String dimension = "x y";
		if(Projection.isDefaultMapEpsgGeographic()) {
			dimension = "lon lat";
		}
		
		String ncml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n";
		ncml += "<netcdf xmlns=\"http://www.unidata.ucar.edu/namespaces/netcdf/ncml-2.2\">\n";
		ncml += "<aggregation dimName=\"" + dimension + "\" type=\"tiled\">\n";
		FileInformation tileInfo;
		for(Path tileFile: tileList) {
			tileInfo = FileInformation.read(tileFile);

			// Find the pixel x and y offset of tile relative to vrt dataset
			// Note: Use the lower left point as netCDF is bottom up convention
			// Using upper left point will cause an issue in ncml
			// as coordinate values must increase or decrease monotonically
			double tmpOffX = (tileInfo.getBbox().getLlCorner().getX() - vrtInfo.getBbox().getLlCorner().getX()) / vrtInfo.getPixelSizeX();
			int offX = (int) Math.rint(tmpOffX); 
			double tmpOffY = (tileInfo.getBbox().getLlCorner().getY() - vrtInfo.getBbox().getLlCorner().getY()) / (vrtInfo.getPixelSizeY() * -1);
			int offY = (int) Math.rint(tmpOffY); 
			
			int extX = offX + tileInfo.getSizeX() - 1;
			int extY = offY + tileInfo.getSizeY() - 1;
					
			String tileFilename = tileFile.getFileName().toString();
			String ncString = String.format("<netcdf location=\"%s\" section=\"%d:%d,%d:%d\"/>\n", tileFilename, offX, extX, offY, extY);
			log.trace(ncString);
			ncml += ncString;
		}
		ncml += "</aggregation>\n";
		ncml += "</netcdf>";
		
		NcmlCreator.writeNcmlFile(ncml, ncmlFile);
	}
	
	/*
	 * Test tiling with existing tiles.
	 * You can use these tiles as test dataset
	 * enki/Dataset/Tiling_Test
	 */
	public static void main(String args[]) throws Exception {
		String dirLoc = "data/demTiling";

		File dir = new File(dirLoc);
		FileInformation vrtInfo = FileInformation.read(dir.toPath().resolve("composite.vrt"));
		
		String[] fileNames = dir.list(new FilenameFilter() {
			
			@Override
			public boolean accept(File dir, String name) {
				boolean bResult = false;
				if(name.endsWith(Format.NC.getExtension())) {
					bResult = true;
				}
				return bResult;
			}
		});
		
		List<Path> tileList = new ArrayList<Path>(); 
		for(int i=0; i< fileNames.length; i++) {
			tileList.add(dir.toPath().resolve(fileNames[i]));
		}
		
		Path ncmlFile = dir.toPath().resolve("agg.ncml");
		NcmlCreator.createNcml(vrtInfo, tileList, ncmlFile);
	}
	
	/**
	 * Writes the ncml content into the specified file.
	 * @param ncmlContent The specified ncml content 
	 * @param ncmlFile The ncml file to write the content to.
	 * @throws IOException
	 */
	private static void writeNcmlFile(String ncmlContent, Path ncmlFile) throws IOException {
		FileWriter outFile = new FileWriter(ncmlFile.toFile());
		PrintWriter out = new PrintWriter(outFile);
		out.println(ncmlContent);			
		out.close();
		outFile.close();
	}	
	
}
