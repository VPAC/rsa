This directory contains the inputs for the query tests.

	abstract.nc: 3x64x64 image. Contains two bands: Band1 and Quality. Band1 is
		a vertical gradient, and is supposed to represent a river. At t=0, the
		river is widest to the left. At t=1, the river is wider in the centre.
		At t=2, the river is wider to the right. The Quality band contains
		generated soft noise that changes with time.

	colour.nc: 3x64x64 image. Contains 4 bands: Red, Green and Blue and Quality.
		The colour bands contain constant values for any given time.
			t=0: yellow (255, 255, 0) (r,g,b)
			t=1: cyan (0, 255, 255)
			t=2: magenta (255, 0, 255)
		The Quality band contains generated soft noise that changes with time.

	nose.nc: 6x64x64 image. Contains 1 band: soft noise. Each time step contains
		the same soft (low-frequency) noise, but different sharp
		(high-frequency) noise.
