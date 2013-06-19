<html>
<head>
<title>TimeSlice</title>
<script src="http://ajax.googleapis.com/ajax/libs/jquery/1.3/jquery.min.js" type="text/javascript"></script>
<script type="text/javascript">
//var SubmitJsonTest = function() {
//	$.post("/SpatialCubeService/RSA/Order", { OrderId: 1, Description: 'Test Desc', Location:'Test Location', Created : '01/02/2012' });
//};

var SubmitJsonTest = function () {
	$.ajax
	({
	    type: "POST",
	    //the url where you want to sent the userName and password to
	    url: '/SpatialCubeService/TimeSlice.xml',
	    async: false,
	    //json object to sent to the authentication url
	    data: {datasetId : 'dataset'},
	    success: function () {
		    alert("Thanks!"); 
	    }
	})
};

</script>
</head>
<body>
<h1>TimeSlice</h1>
<form method="POST" action="/SpatialCubeService/TimeSlice.xml">
	DatasetId : <input type="text" name="datasetId"/> <BR />
	CreationDate : <input type="text" name="creationDate"/> <BR />
	Comment : <input type="text" name="comment"/> <BR />
	<input type="submit"/><!--<input type="button" value="TestJson" onclick="SubmitJsonTest();"> -->
</form>
</body>
</html>