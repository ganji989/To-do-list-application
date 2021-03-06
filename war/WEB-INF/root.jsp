<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page import="java.util.ArrayList"%>
<!DOCTYPE html>
<html lang="en">
<head>

<title>To-do List</title>

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
<script type="text/javascript">
	function removeElement(n) {
		var element = document.getElementById(id);
		// A bit of robustness helps...
		if (element && element.parentNode) {
			element.parentNode.removeChild(element);
		}
	}
</script>
</head>

<body>

	<br />
	<div class="boxed">
	<c:choose>
		<c:when test="${user != null}">
			<p style="text-align: center">
				Welcome ${user.email} <br />
			</p>
			<button onclick="window.location='${addLink}';">ADD TASK</button>
			</br></br>
			<script>
				if (document.getElementById("app")) {
					document.getElementById("app").parentNode
							.removeChild(document.getElementById("app"));
				}
			</script>
			<%
				out.write(request.getAttribute("to-do-list").toString());
			%>
			</br></br>
			<p style="text-align: center">
				You can signout <a href="${logout_url}">here</a><br />
			</p>
		</c:when>
		<c:otherwise>
			<div align="center">
				<h2>*Welcome*</h2>
				<br /> <br /> <a href="${login_url}">Sign in or register</a>
			</div>
			<br />
		</c:otherwise>
	</c:choose>
	</div>
</body>
</html>