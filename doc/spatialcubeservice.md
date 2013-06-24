# RSA spatialcubeservice API

This document describes the RESTful Web services available in the RSA's SpatialCubeService module.

1. [Dataset web services](#dataset) - for listing, creating, retrieving, and deleting dataset
1. [Timeslice web services](#timeslice) - for listing, creating, retrieving, and deleting timeslice for a dataset
1. [Band web services](#band) - for listing, creating, retrieving, and deleting band for a dataset
1. [Data web services](#data) - for importing, exporting, uploading, downloading data and also performing a query

## Dataset

| URL     | Method      | Parameter                                   | Return |
|:--------|:-----------:|:--------------------------------------------|:-------|
|/Dataset | GET         |name : dataset name<br />resolution : resolution<br /> abstract : dataAbstract<br />page : page number (Default: 0)<br />pageSize : page Size (Default:50)|DatasetCollectionResponse|
|/Dataset/{id}|GET||DatasetResponse|
|/Dataset|POST|id(optional) : if the value is not null it will be update the dataset otherwise it will be created a new one<br />name : dataset name<br />resolution : resolution<br />precision : precision<br />abstract : dataAbstract|DatasetResponse|
|/Dataset/Delete/{id}|POST||DatasetResponse|

## TimeSlice

| URL     | Method      | Parameter                                   | Return |
|:--------|:-----------:|:--------------------------------------------|:-------|
|/TimeSlice | GET |datasetId : id of dataset which is time slice belongs to<br />creationDate : time of time slice<br />searchBeginDate : search boundary start point<br />searchEndDate : search boundary end point page : page number (Default: 0)<br />pageSize : page Size (Default:50)|TimeSliceCollectionResponse|
|/TimeSlice/{id}|GET||TimeSliceResponse|
|/TimeSlice|POST|timeSliceId(optional) : if the value is not null it will be update the timeslice otherwise it will be created a new one<br />datasetId : id of dataset which is time slice belongs to<br />createDate : creationDate<br />comment|TimeSliceResponse|
|/TimeSlice/Delete/{id}|GET||TimeSliceResponse|

## Band

| URL     | Method      | Parameter                                   | Return |
|:--------|:-----------:|:--------------------------------------------|:-------|
|/Band|GET|datasetId : id of dataset which is time slice belongs to<br />page : page number<br />(Default: 0)<br />pageSize : page Size (Default:50)|BandCollectionResponse|
|/Band/{id}|GET||BandResponse|
|/Band|POST|datasetId : id of dataset which is time slice belongs to<br />name : name of band<br />dataType : dataType of band<br />metadata : is metadata continuous : is continuous|BandResponse|
|/Band/Delete/{id}|POST||BandResponse|

## Data

| URL     | Method      | Parameter                                   | Return |
|:--------|:-----------:|:--------------------------------------------|:-------|
|/Data/Import|POST|taskId : id of file which is uploaded<br />bandId : id of band which is going to import|ImportResponse(Okay, fail)|
|/Data/Export|POST|datasetId : id of dataset which is time slice belongs to<br />bandId : list of bandId s(same name request)<br />searchBeginDate : search boundary start point<br />searchEndDate : search boundary end point<br />projetion : projection type and value (eg.EPSG:123)<br />topLeft : spatial bounds<br />bottomRight : spatial bounds|ExportResponse(taskId)|
|/Data/ExportExpress|GET|datasetId : id of dataset which is time slice belongs to|ExportResponse(taskId)|
|/Data/Upload|POST|timeSliceId : id of time slice which is file belongs to<br />files : list of files (multipart-file)<br />fileId : existing file id|FileInfoResponse|
|/Data/Download/{taskId}|GET|Not allowed extention|Zipped file itself|
|/Data/Task|POST|searchType : Import / Export<br />status : status of task|TaskCollectionResponse|
|/Data/Task/{id}|POST|taskId : id of task came from export / import process|TaskResponse|
|/Data/Query|GET|file : configuration file which is used for query<br />threads : number of threads<br />minX : minimum X coordination<br />minY : minimum Y coordination<br />maxX : maximum X coordination<br />maxY : maximum Y coordination<br />startDate : date of timeslice querying date started<br />startDate : date of timeslice querying date started|QueryResponse|



