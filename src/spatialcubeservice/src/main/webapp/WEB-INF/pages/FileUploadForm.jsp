<html>
<head>
<title>Data Upload</title>
</head>
<body>
<h1>Please upload a file</h1>
<form method="POST" action="/SpatialCubeService/Data/Upload.xml" enctype="multipart/form-data">
Time Slice Id : <input type="text" name="timeSliceId"/><BR />
File Id : <input type="text" name="taskId"/><BR />
Files : <input type="file" name="files"/><BR />
Files : <input type="file" name="files"/><BR />
<input type="submit"/>
</form>
</body>
</html>