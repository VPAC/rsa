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

package org.vpac.web.model.response;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.vpac.ndg.storage.model.Band;
 
@XmlRootElement(name = "BandCollection")
public class BandCollectionResponse {
	private List<BandResponse> items;

	public List<BandResponse> getItems() {
		return items;
	}

	@XmlElement(name = "Band")
	public void setItems(List<BandResponse> items) {
		this.items = items;
	}
	
	public BandCollectionResponse(Collection<Band> bands) {
		if(bands != null) {
			items = new ArrayList<BandResponse>();
			for(Band band: bands)
				this.items.add(new BandResponse(band));
		}
	}
	
	public BandCollectionResponse() {
		items = new ArrayList<BandResponse>();
	}

	public List<Band> toBand() {
		List<Band> list = new ArrayList<Band>();
		for(BandResponse br : this.items) {
			list.add(br.toBand());
		}
		return list;
	}
}
