<%@ page import="java.util.*" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
    <head>
        <title>Game of life</title>
        <link rel="stylesheet" href="css/bootstrap.min.css">
        <script src="js/bootstrap.min.js"></script>
    </head>
    <body>
        <c:if test="${empty ground}">
            <%
                session.setAttribute("message", "The session has expired - The game is lost");
                response.setStatus(response.SC_MOVED_TEMPORARILY);
                response.setHeader("Location", "index.jsp");
            %>
        </c:if>
        <div class="jumbotron container">
            <div class="panel panel-default">
                <div class="panel-heading">
                    <span class="panel-title pull-left">Play</span>
                    <a href="/newGame">
                        <button class="btn btn-danger btn-xs pull-right">New Game</button>
                    </a>
                    <div class="clearfix"></div>
                </div>
                <div class="panel-body">
                    THIS IS IT
                </div>
            </div>
        </div>     
    </body>
</html>
