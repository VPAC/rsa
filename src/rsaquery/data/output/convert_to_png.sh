#!/usr/bin/env bash

# Blur
echo blur.nc
gdal_translate -b 1 -of PNG blur.nc blur_t0.png
gdal_translate -b 2 -of PNG blur.nc blur_t1.png
gdal_translate -b 3 -of PNG blur.nc blur_t2.png
gdal_translate -b 4 -of PNG blur.nc blur_t3.png
gdal_translate -b 5 -of PNG blur.nc blur_t4.png
gdal_translate -b 6 -of PNG blur.nc blur_t5.png

# Wetting
echo watermark.nc
gdal_translate -of PNG NETCDF:"watermark.nc":wet watermark_wet.png
gdal_translate -of PNG NETCDF:"watermark.nc":time watermark_time.png

# Fire
echo on_fire.nc
gdal_translate -of PNG NETCDF:"on_fire.nc":temp on_fire_temp.png
gdal_translate -of PNG NETCDF:"on_fire.nc":time on_fire_time.png

# QualityColour
echo quality_colour.nc
gdal_translate -of PNG NETCDF:"quality_colour.nc":colour1 quality_colour_Red.png
gdal_translate -of PNG NETCDF:"quality_colour.nc":colour2 quality_colour_Green.png
gdal_translate -of PNG NETCDF:"quality_colour.nc":colour3 quality_colour_Blue.png
gdal_translate -of PNG NETCDF:"quality_colour.nc":quality quality_colour_Quality.png
gdal_translate -of PNG NETCDF:"quality_colour.nc":time quality_colour_time.png

# graphical plot
echo plot.nc
gdal_translate -of PNG plot.nc plot.png

