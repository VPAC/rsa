#!/usr/bin/env bash

echo noise.nc
gdal_translate -b 1 -of PNG noise.nc noise_t0.png
gdal_translate -b 2 -of PNG noise.nc noise_t1.png
gdal_translate -b 3 -of PNG noise.nc noise_t2.png
gdal_translate -b 4 -of PNG noise.nc noise_t3.png
gdal_translate -b 5 -of PNG noise.nc noise_t4.png
gdal_translate -b 6 -of PNG noise.nc noise_t5.png

echo colour.nc
gdal_translate -of PNG NETCDF:"colour.nc":Red colour_Red.png
gdal_translate -of PNG NETCDF:"colour.nc":Green colour_Green.png
gdal_translate -of PNG NETCDF:"colour.nc":Blue colour_Blue.png
gdal_translate -of PNG NETCDF:"colour.nc":Quality colour_Quality.png

echo abstract.nc
gdal_translate -of PNG NETCDF:"abstract.nc":Band1 abstract_Band1.png
gdal_translate -of PNG NETCDF:"abstract.nc":Quality abstract_Quality.png

