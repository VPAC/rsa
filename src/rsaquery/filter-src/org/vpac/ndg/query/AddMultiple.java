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

package org.vpac.ndg.query;

import java.io.IOException;

import org.vpac.ndg.query.math.BoxReal;
import org.vpac.ndg.query.math.Element;
import org.vpac.ndg.query.math.ElementInt;
import org.vpac.ndg.query.math.ScalarElement;
import org.vpac.ndg.query.math.VectorReal;
import org.vpac.ndg.query.sampling.Cell;
import org.vpac.ndg.query.sampling.CellScalar;
import org.vpac.ndg.query.sampling.CellType;
import org.vpac.ndg.query.sampling.PixelSource;


/**
 * Multiplies two bands together.
 *
 * @author Alex Fraser
 */
@Description(name = "Add Multiple", description = "Add pixels from multiple layers")
@InheritDimensions(from = "inputA")
public class AddMultiple implements Filter {

	public PixelSource inputA;

	@CellType("int")
	public CellScalar output;

	private ScalarElement total;
	
	@Override
	public void initialise(BoxReal bounds) throws QueryConfigurationException {
		total = (ScalarElement) output.getPrototype().getElement().copy();
	}

	@Override
	public void kernel(VectorReal coords) throws IOException {
		Element<?> element = inputA.getPixel(coords);
		
		int total = 0;
		for (ScalarElement comp : element.getComponents()) {
			total += comp.intValue();
		}
		this.total.set(total);
		
		output.set(this.total);
	}

}
