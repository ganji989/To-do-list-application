<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<style>
.boxed {
	border: 3px solid #C9C9C9;
}
.boxed1 {
	border: 3px solid #78B848;
}
body {
    background-color: #959492;
}
</style>
<title>Edit Task</title>

</head>

<body>
	<br />
	<c:choose>

		<c:when test="${ loggedin==null }">
			<div align="center">
				<h1>Please Login or Signup...</h1>
				<br/><br/>
				<a href="${login_url}">Login or Signup</a>
			</div>
			<br/><br/>
		</c:when>
		<c:otherwise>
			<div action="/">
				<form method="post">
					<br /> <br />
					<h2>Update Task</h2>
					<br /> <input type="text" name="task"
						value=<%=request.getAttribute("task")%> /><br /> <input
						type="text" name="date" value=<%=request.getAttribute("date")%> /><br />
					 <input sname="Update" type="submit" />
				</form>
				<br /> <br />
			</div><br /><br />
		</c:otherwise>
	</c:choose>
</body>
</html>