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

package org.vpac.ndg.task;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vpac.ndg.application.Constant;
import org.vpac.ndg.datamodel.AggregationType;
import org.vpac.ndg.datamodel.AggregationDefinition;
import org.vpac.ndg.datamodel.AggregationDefinition.VarDef;
import org.vpac.ndg.datamodel.RsaAggregationFactory;
import org.vpac.ndg.exceptions.TaskException;
import org.vpac.ndg.exceptions.TaskInitialisationException;
import org.vpac.ndg.storagemanager.GraphicsFile;

public class NcmlBuilder extends Task {

	final static Logger log = LoggerFactory.getLogger(NcmlBuilder.class);
	
	private List<GraphicsFile> rawSource;
	private List<ScalarReceiver<AggregationDefinition>> nestedSource;

	private AggregationType type;
	// For UNION (optional) and JOIN_NEW
	private List<String> bandNames;
	// For JOIN_NEW
	private VarDef newDimension;
	// For JOIN_NEW
	private List<String> coordinateValues;

	private ScalarReceiver<AggregationDefinition> target;
	private Path targetPath;

	private RsaAggregationFactory factory;

	public NcmlBuilder() {
		this(Constant.TASK_DESCRIPTION_TILEAGGREGATOR);
	}

	public NcmlBuilder(String description) {
		super(description);
		factory = new RsaAggregationFactory();
	}

	@Override
	public void initialise() throws TaskInitialisationException {
		if (rawSource == null && nestedSource == null) {
			throw new TaskInitialisationException(getDescription(), Constant.ERR_NO_INPUT_IMAGES);
		}

		if (type == AggregationType.JOIN_NEW) {
			if (bandNames == null) {
				throw new TaskInitialisationException(getDescription(),
						"No bands specified");
			}
			if (newDimension == null) {
				throw new TaskInitialisationException(getDescription(),
						"No dimension specified");
			}
			if (coordinateValues == null) {
				throw new TaskInitialisationException(getDescription(),
						"No coordinate values specified");
			}
		}

		if (target == null) {
			throw new TaskInitialisationException(getDescription(), Constant.ERR_TARGET_DATASET_NOT_SPECIFIED);
		}
	}

	@Override
	public void execute() throws TaskException {
		AggregationDefinition ds = null;
		try {
			List<AggregationDefinition> children;			
			switch (type) {
			case UNION:
				children = getChildren();
				ds = factory.union(children, bandNames);
				break;
		
			case JOIN_NEW:
				children = getChildren();
				ds = factory.joinNew(children, bandNames, newDimension,
						coordinateValues);
				break;
		
			default:
				throw new TaskException(getDescription(),
						String.format("Unrecognised aggregation type \"%s\" specified", type));
			}
		}
		catch (IllegalArgumentException e) {
			throw new TaskException(getDescription(), e.getMessage());			
		}

		if(ds == null) {
			log.debug("Aggregation is empty.");
			return;
		}

		target.set(ds);

		if (targetPath != null) {
			try {
				ds.serialise(targetPath);
			} catch (IOException e) {
				throw new TaskException(e);
			}
		}
	}

	/**
	 * This class can have one of two input types: a list of
	 * {@link GraphicsFile}s, or a list of {@link AggregationDefinition}s. This function
	 * coerces them into a common type.
	 * 
	 * @return The input for this task.
	 * @throws TaskException
	 *             If neither of the sources were specified.
	 */
	protected List<AggregationDefinition> getChildren() throws TaskException {
		List<AggregationDefinition> children = new ArrayList<>();
		if (rawSource != null) {
			for (GraphicsFile child : rawSource) {
				if (!child.exists()) {
					// Ignore if aggregation definition doesn't exist
					log.info("Non-existent file excluded from ncml building process:\n{}", child.getFileLocation());
					continue;
				}
				AggregationDefinition aggDef = factory.raw(child, true);

				// Assume that the files will all be in the same directory as
				// the resulting .ncml.
				children.add(aggDef);
			}

		} else if (nestedSource != null) {
			int childIndex = 0;
			List<String> validCoordinateValues = new ArrayList<>();
			for (ScalarReceiver<AggregationDefinition> child : nestedSource) {
				AggregationDefinition aggDef = child.get();
				if (aggDef != null) {
					// If aggregation exists then only add this aggregation as child dataset  
					children.add(child.get());
					// Keep track valid time unit (of valid aggregation)
					validCoordinateValues.add(coordinateValues.get(childIndex));
				}
				childIndex++;
			}
			if(type == AggregationType.JOIN_NEW) {
				// Only interested with the time unit of valid aggregation
				setCoordinateValues(validCoordinateValues);
			}
		} else {
			throw new TaskException(Constant.ERR_NO_INPUT_IMAGES);
		}
		return children;
	}

	@Override
	public void rollback() {
		// Do nothing
	}

	@Override
	public void finalise() {

	}

	public List<GraphicsFile> getRawSource() {
		return rawSource;
	}

	public void setRawSource(List<GraphicsFile> rawSource) {
		this.rawSource = rawSource;
	}

	public List<ScalarReceiver<AggregationDefinition>> getNestedSource() {
		return nestedSource;
	}

	public void setNestedSource(List<ScalarReceiver<AggregationDefinition>> nestedSource) {
		this.nestedSource = nestedSource;
	}

	public ScalarReceiver<AggregationDefinition> getTarget() {
		return target;
	}

	public void setTarget(ScalarReceiver<AggregationDefinition> target) {
		this.target = target;
	}

	public AggregationType getType() {
		return type;
	}

	/**
	 * @param type The type of aggregation to create.
	 */
	public void setType(AggregationType type) {
		this.type = type;
	}

	public Path getTargetPath() {
		return targetPath;
	}

	public void setTargetPath(Path targetPath) {
		this.targetPath = targetPath;
	}

	public List<String> getBandNames() {
		return bandNames;
	}

	/**
	 * Set which variables (bands) will be aggregated. When creating a union,
	 * these must be specified.
	 * 
	 * @param bandNames
	 *            The name of each band to aggregate. This must have a 1:1
	 *            mapping with the source images (see
	 *            {@link #setRawSource(List)} and {@link #setNestedSource(List)}
	 *            ).
	 */
	public void setBandNames(List<String> bandNames) {
		this.bandNames = bandNames;
	}

	public List<String> getCoordinateValues() {
		return coordinateValues;
	}

	public void setCoordinateValues(List<String> coordinateValues) {
		this.coordinateValues = coordinateValues;
	}

	public VarDef getNewDimension() {
		return newDimension;
	}

	public void setNewDimension(VarDef newDimension) {
		this.newDimension = newDimension;
	}

}
