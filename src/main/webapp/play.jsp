<%@ page import="java.util.*" %>
<%@ page import="solution.ground.Ground" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
    <head>
        <title>Game of life</title>
        <link rel="stylesheet" href="css/bootstrap.min.css">
        <script src="js/bootstrap.min.js"></script>
        <style>
        .square {
            width: 65vh;
            height: 65vh;
        }
        .square tr {
            width: 65vh;
            height: 1vh;
        }
        .square td {
            width: 1vh;
            height: 1vh;
        }
        </style>

        <c:if test="${not empty playFW && playFW}">
            <meta http-equiv="refresh" content="1;url=?nextX=" />
        </c:if>
    </head>
    <body>
        <c:if test="${empty ground}">
            <%
                session.setAttribute("message", "The session has expired - The game is lost");
                response.setStatus(response.SC_MOVED_TEMPORARILY);
                response.setHeader("Location", "index.jsp");
            %>
        </c:if>
        <c:if test="${not empty ground}">
            <div class="jumbotron container">
                <div class="panel panel-default">
                    <div class="panel-heading">
                        <span class="panel-title pull-left"><%
                            out.println("Tick: " +
                                        ((Number) session.getAttribute("tick")).intValue());
                        %></span>
                        <a href="/newGame">
                            <button class="btn btn-danger btn-xs pull-right">New Game</button>
                        </a>
                        <div class="clearfix"></div>
                    </div>
                    <div class="panel-body">
                        <c:if test="${not empty message}">
                            <div class="alert alert-success">
                                ${message}
                            </div>
                            <%
                                session.removeAttribute("message");
                            %>
                        </c:if>
                        <form action="/play" method="get">
                            <div class="btn-group">
                                <button type="submit" class="btn btn-default" name="zeroX" ${(tick eq 0) ? 'disabled="disabled"' : ''}>
                                    <span class="glyphicon glyphicon-fast-backward"></span>
                                </button>
                                <button type="submit" class="btn btn-default" name="prevX" ${(tick eq 0) ? 'disabled="disabled"' : ''}>
                                    <span class="glyphicon glyphicon-step-backward"></span>
                                </button>
                                <button type="submit" class="btn btn-default" name="playpause">
                                        <span class="glyphicon ${playFW ? 'glyphicon-pause' : 'glyphicon-play'}"></span>
                                </button>
                                <button type="submit" class="btn btn-default" name="nextX">
                                        <span class="glyphicon glyphicon-step-forward"></span>
                                </button>
                                <button type="submit" class="btn btn-default" name="endX" ${endable ? '' : 'disabled="disabled"'}>
                                        <span class="glyphicon glyphicon-fast-forward"></span>
                                </button>
                            </div>
                            <div class="btn-group">
                                <c:if test="${windowing}">
                                    <button type="submit" class="btn btn-default" name="upB">
                                        <span class="glyphicon glyphicon-arrow-up"></span>
                                    </button>
                                    <button type="submit" class="btn btn-default" name="downB">
                                        <span class="glyphicon glyphicon-arrow-down"></span>
                                    </button>
                                    <button type="submit" class="btn btn-default" name="leftB">
                                        <span class="glyphicon glyphicon-arrow-left"></span>
                                    </button>
                                    <button type="submit" class="btn btn-default" name="rightB">
                                        <span class="glyphicon glyphicon-arrow-right"></span>
                                    </button>
                                    <button type="submit" class="btn btn-default" name="plusU">
                                        <span class="glyphicon glyphicon-resize-full"></span>
                                    </button>
                                    <button type="submit" class="btn btn-default" name="minusU" ${(showwidth gt 1 && showheight gt 1) ? '' : 'disabled'}>
                                        <span class="glyphicon glyphicon-resize-small"></span>
                                    </button>
                                </c:if>
                                <c:if test="${not windowing}">
                                    <button type="submit" class="btn btn-default" name="windowOn">
                                        <span class="glyphicon glyphicon-move"></span>
                                    </button>
                                </c:if>
                            </div>
                        </form>
                        <table class="table table-hover table-bordered square">
                            <%
                                session.setAttribute("fields",
                                    ((Ground) session.getAttribute("ground")).getCells(
                                        ((Number) session.getAttribute("tick")).intValue(),
                                        ((Number) session.getAttribute("showleft")).intValue(),
                                        ((Number) session.getAttribute("showleft")).intValue() +
                                        ((Number) session.getAttribute("showwidth")).intValue(),
                                        ((Number) session.getAttribute("showtop")).intValue(),
                                        ((Number) session.getAttribute("showtop")).intValue() +
                                        ((Number) session.getAttribute("showheight")).intValue())
                                );
                            %>
                            <c:forEach items="${fields}" var="lines" varStatus="yLoop">
                                <tr>
                                    <c:forEach items="${lines}" var="cell" varStatus="xLoop">
                                        <td data-toggle="tooltip"  data-container="body"  data-placement="bottom"
                                            title="X: ${showleft + xLoop.index} Y: ${showtop + yLoop.index}"
                                            <%=(pageContext.getAttribute("cell").toString().equals("G") ? "style='background-color:green;'" :
                                                pageContext.getAttribute("cell").toString().equals("B") ? "style='background-color:blue;'" : "")%> ></td>
                                    </c:forEach>
                                </tr>
                            </c:forEach>
                        </table>
                    </div>
                </div>
            </div>
        </c:if>
    </body>
</html>
