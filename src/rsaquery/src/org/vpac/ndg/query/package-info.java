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
