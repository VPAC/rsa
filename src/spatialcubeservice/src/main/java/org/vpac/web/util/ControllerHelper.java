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

package org.vpac.web.util;

import java.beans.PropertyEditorSupport;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.web.bind.WebDataBinder;
import org.vpac.ndg.Utils;
import org.vpac.ndg.common.Default;
import org.vpac.ndg.common.datamodel.CellSize;

public class ControllerHelper {
	public final static String RESPONSE_ROOT = "Response";
	
	public ControllerHelper() {
	}

	public void BindDateTimeFormatter(WebDataBinder binder) {
		binder.registerCustomEditor(Date.class, new PropertyEditorSupport() {
		    public void setAsText(String value) {
	            setValue(Utils.parseDate(value));
		    }

		    public String getAsText() {
		        return new SimpleDateFormat(Default.MILLISECOND_PATTERN).format((Date) getValue());
		    }        

		});
	}

	public void BindCellSizeFormatter(WebDataBinder binder) {
		binder.registerCustomEditor(CellSize.class, new PropertyEditorSupport() {
					public void setAsText(String value) {
						try {
							setValue(CellSize.fromHumanString(value));
						} catch (IllegalArgumentException e) {
							setValue(CellSize.valueOf(value));
						}
					}

					public String getAsText() {
						return ((CellSize) getValue()).toHumanString();
					}

		});
	}
}
