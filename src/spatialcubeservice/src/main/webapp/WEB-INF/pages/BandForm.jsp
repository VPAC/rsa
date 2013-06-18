<html>
<head>
<title>Band</title>
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
	    url: '/SpatialCubeService/Band.xml',
	    dataType: 'json',
	    async: false,
	    //json object to sent to the authentication url
	    data: "{datasetId : '2c9f852437961c9f0137961ca3140001', name: 'test', metadata:'false'}",
	    success: function () {
		    alert("Thanks!"); 
	    }
	})
};

</script>
</head>
<body>
<h1>Band</h1>
<form method="POST" action="/SpatialCubeService/Band.xml">
	DatasetId : <input type="text" name="datasetId"/> <BR />
	Name : <input type="text" name="name"/> <BR />
	IsMetadata : <input type="text" name="metadata" value="false"/> <BR />	
	IsContinuous : <input type="text" name="continuous" value="true"/> <BR />
	<input type="submit"/><input type="button" value="TestJson" onclick="SubmitJsonTest();">
</form>
</body>
</html>