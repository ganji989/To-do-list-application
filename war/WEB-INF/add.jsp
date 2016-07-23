<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Add Task</title>
<style>
.boxed {
	border: 3px solid #C9C9C9;
}
body {
    background-color: #959492;
}
</style>
</head>
<body>
	<br />
	<div class="boxed">
	<c:choose>
		<c:when test="${ loggedin !=null }">
			<!-- 			<h2 align="center">This page is for Adding tasks</h2><br/>-->
			<div align="center" action="/">
				<form method="post">
					<br /> <br />
					<h3>Add To-do Task</h3>
					<br /> <input type="text" name="name" /><br /> 
					<input type="text" name="date" /><br />
					<input name="add" type="submit" />
				</form>
				<br />
				<br />
			</div>
			<br />
			<br />
		</c:when>
		<c:otherwise>
			<div align="center">
				<h2>Please Login First...</h2>
				<br /> <br /> <a href="${login_url}">Login or register</a>
			</div>
			<br />
			<br />
		</c:otherwise>

	</c:choose>
	</div>
</body>
</html>