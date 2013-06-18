<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<meta charset="utf-8">
<%@page contentType="text/html; charset=UTF-8" %>
<head>
<title>Time series plot for dataset ${datasetName} at point(${pointX}, ${pointY})</title>
<link href="<c:url value='/resources/src/nv.d3.css' />" rel="stylesheet" type="text/css" />
<style>
body {
  overflow-y:scroll;
}

text {
  font: 12px sans-serif;
}

svg {
  display: block;
}

#chart1 svg {
  height: 500px;
  min-width: 100px;
  min-height: 100px;
/*
  margin: 50px;
  Minimum height and width is a good idea to prevent negative SVG dimensions...
  For example width should be =< margin.left + margin.right + 1,
  of course 1 pixel for the entire chart would not be very useful, BUT should not have errors
*/
}
.nvd3.nv-line .nvd3.nv-scatter .nv-groups .nv-point {
  fill-opacity: inherit;
  stroke-opacity: inherit;
}
</style>
<script src="<c:url value='/resources/lib/d3.v2.js' />" ></script>
<script src="<c:url value='/resources/lib/nv.d3.js' />" ></script>
<script src="<c:url value='/resources/src/tooltip.js' />" ></script>
<script src="<c:url value='/resources/src/utils.js' />" ></script>
<script src="<c:url value='/resources/src/models/legend.js' />" ></script>
<script src="<c:url value='/resources/src/models/axis.js' />" ></script>
<script src="<c:url value='/resources/src/models/scatter.js' />" ></script>
<script src="<c:url value='/resources/src/models/line.js' />" ></script>
<script src="<c:url value='/resources/src/models/cumulativeLineChart.js' />" ></script>
<script>
// Wrapping in nv.addGraph allows for '0 timeout render', stores rendered charts in nv.graphs, and may do more in the future... it's NOT required
var chart;
nv.addGraph(function() {  

   chart = nv.models.lineChart()
             .x(function(d) { return d[0] })
             .y(function(d) { return d[1] })
             .color(d3.scale.category10().range())
             .clipVoronoi(false);

   chart.xAxis
      .tickFormat(function(d) {
          return d3.time.format('%x')(new Date(d))
        });

  chart.yAxis
      .tickFormat(d3.format('.2f'));
  
  chart.lines.forceY([0]);
  
  d3.select('#chart1 svg')
      // .datum(cumulativeTestData())
      .datum(cumulativeTestData())
    //.transition().duration(500)
      .call(chart);

  //TODO: Figure out a good way to do this automatically
  nv.utils.windowResize(chart.update);
  //nv.utils.windowResize(function() { d3.select('#chart1 svg').call(chart) });


  chart.dispatch.on('stateChange', function(e) { nv.log('New State:', JSON.stringify(e)); });

  return chart;
});


function flatTestData() {
  return [
    {
      key: "Snakes",
      values: [0,1,2,3,4,5,6,7,8,9].map(function(d) {
        var currentDate = new Date();
        currentDate.setDate(currentDate.getDate() + d);
        return [currentDate, 0]
      })
    }
  ];
}


function cumulativeTestData() {
  return [
          	<c:forEach items="${bandPlotValues}" var="bp">
          	{
			    key: "${bp.key}",
			    values: ${bp.value}
			},
          	</c:forEach>
          ];
}

</script>
</head>
<body>
	<h1>Time series plot for ${datasetName} at (y x)(${pointY} ${pointX})</h1>
  <div id="chart1">
    <svg style="height: 500px;"></svg>
  </div>
</body>
