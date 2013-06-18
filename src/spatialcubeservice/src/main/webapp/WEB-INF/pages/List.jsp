<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<body>
	<h1>Spring HTML Object Test</h1>

	<c:forEach var="obj" items="${Response.items}">
		Object Id : <c:out value="${obj.id}" />
		object Name : <c:out value="${obj.name}" /> <br />
	</c:forEach>

</body>
</html>