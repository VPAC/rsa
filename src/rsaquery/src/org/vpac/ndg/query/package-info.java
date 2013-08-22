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

/**
<p>RSA Query Engine: an image processing pipeline for
<a href="http://www.unidata.ucar.edu/software/netcdf/">NetCDF</a> data.</p>

<p>The goal of the Query Engine is to make it easy for users to run filters
over NetCDF data. The system provides ways for users to define:</p>

<ul>
    <li>Filters that transform NetCDF data.</li>
    <li>Queries that link filters to specific datasets.</li>
</ul>

<img src="doc-files/Query_class.png" />

<p>{@link org.vpac.ndg.query.Filter Filters} are implemented as Java classes.
{@link org.vpac.ndg.query.Query Queries} are defined using the
{@link org.vpac.ndg.query.QueryDefinition} class and its internal classes.
Usually, a QueryDefinition will be read from XML, although it's possible to
create one programmatically.</p>

@see org.vpac.ndg.query.Filter
@see org.vpac.ndg.query.QueryDefinition
@see org.vpac.ndg.query.QueryRunner
 */

package org.vpac.ndg.query;
