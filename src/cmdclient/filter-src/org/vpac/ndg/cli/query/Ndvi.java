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

package org.vpac.ndg.cli.query;

import java.io.IOException;

import org.vpac.ndg.query.Filter;
import org.vpac.ndg.query.InheritDimensions;
import org.vpac.ndg.query.QueryConfigurationException;
import org.vpac.ndg.query.math.BoxReal;
import org.vpac.ndg.query.math.Element;
import org.vpac.ndg.query.math.VectorReal;
import org.vpac.ndg.query.sampling.Cell;
import org.vpac.ndg.query.sampling.CellType;
import org.vpac.ndg.query.sampling.PixelSource;

/**
 * NDVI = Normalised Difference Vegetation Index described by the following
 * (band4 - band3)/(band4 + band3) - produces values between -1 and 1.
 * 
 * <p>
 * Formula provided by Geoscience Australia.
 * </p>
 * 
 * @author Alex Faser
 */
@InheritDimensions(from = "band3")
public class Ndvi implements Filter {

	// Input fields.
	public PixelSource band3;
	public PixelSource band4;

	// Use the same number of components as band3 for the output, but convert it
	// to a float type. While it is unlikely that the input will be vector, it
	// is good practice to keep things general, and shouldn't slow it down.
	@CellType(value="band3", as="float")
	public Cell output;

	Element<?> ndvi;
	Element<?> temp;

	@Override
	public void initialise(BoxReal bounds) throws QueryConfigurationException {
		ndvi = output.getPrototype().getElement().copy();
		temp = ndvi.copy();
	}

	@Override
	public void kernel(VectorReal coords) throws IOException {
		Element<?> b3 = band3.getPixel(coords);
		Element<?> b4 = band4.getPixel(coords);
		temp.addOf(b4, b3);
		ndvi.subOf(b4, b3);
		ndvi.div(temp);
		output.set(ndvi);
	}
}
