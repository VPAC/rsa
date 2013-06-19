These XML files configure the queries to run: they bind filters to inputs and
outputs.

== Queries ==

 1. wettingextents.xml: input/abstract.nc -> output/watermark.nc

	WATER ( Query returns 0 or 1 ) - For a period between date A and date B,
	display pixels with a value of NBAR Landsat band 5 value of less than 1000
	(reflectance scaled by 10000) - true = 1 false = 0. The pixel level metadata
	for positive results should provide capture date information.

 2. activefire.xml: input/abstract.nc -> output/on_fire.nc

	ACTIVE FIRE ( Query returns temperature, metadata enables identification of
	date of fire per pixel) – For a period between date A and date B, display
	the greatest pixel temperature for Top of Atmosphere temperature with a
	value > 360K. The pixel level metadata for the result should provide capture
	date information.

 3. qualityselection.xml: input/colour.nc -> output/quality_colour.nc

	THE MOST RECENT CLOUD FREE PIXELS BEFORE A GIVEN DATE (Query returns a RGB
	colour image for a selection of bands) – Display the latest quality assured
	pixels.

 4. not implemented.

	ANALYSIS OF PIXEL VALUES: for a period between data A and date B for pixels,
	which pixels meet some statistical measure (i.e. max, mean, min, stdv, mode
	etc.)

 5a. timeseries.xml: input/abstract.nc -> output/timeseries.nc
 5b. graphicalplot.xml: input/abstract.nc -> output/plot.nc

	TIMESERIES PLOT FOR A GIVEN INDEX  – graphical plot of NDVI over a time
	period

	In a, a single 1D NetCDF variable is produced; effectively a slice through
	the input for a single spatial coordinate. In b, a 2D plot is produced.

 6a. minimisevariance.xml: input/noise.nc -> output/minvariance.nc
 6b. minimisevariance_twopass.xml: input/noise.nc -> output/minvariance_twopass.nc

	WITHIN A WINDOW OF PIXELS, VARIANCE IS MINIMISED (Query returns an RGB
	colour image for a selection of bands)   – for a 3x3 kernel which iterates
	through the combination of pixels for that kernel in the stack within the
	time period - output pixels for a date range which minimise the variance
	within the search window for a given band i.e. reduce noise.

	This query uses a filter with an internal 2D loop to determine the variance
	around each pixel.

	In a, the whole query is performed by one filter. In b, two filters are
	used: in the first pass, the variance is calculated and stored in a free
	variable (i.e. a variable that is not attached to a dataset); in the second
	pass, the precomputed variance is used to select the best layer.
