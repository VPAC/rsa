<html>
<head>
<title>Dataset</title>
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
	    url: '/SpatialCubeService/Dataset.xml',
	    dataType: 'json',
	    async: false,
	    //json object to sent to the authentication url
	    data: {name : 'test', resolution: 'm500', dataAbstract : 'test', precision:1},
	    success: function () {
		    alert("Thanks!"); 
	    }
	});
};

</script>
</head>
<body>
<h1>Dataset</h1>
<form method="POST" action="/SpatialCubeService/Dataset.xml">
	Name : <input type="text" name="name"/> <BR />
	Resolution : <input type="text" name="resolution" value="m100"/> <BR />
	Precision : <input type="text" name="precision" value="1 day"/> <BR />
	Abstract : <input type="text" name="dataAbstract"/> <BR />
	<input type="submit"/><input type="button" value="TestJson" onclick="SubmitJsonTest();">
</form>
</body>
</html>