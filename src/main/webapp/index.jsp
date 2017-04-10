<%@ page import="java.util.*" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
    <head>
        <title>Game of life</title>
        <link rel="stylesheet" href="css/bootstrap.min.css">
        <script src="js/bootstrap.min.js"></script>
    </head>
    <body>
        <c:if test="${not empty ground}">
            <%
                response.setStatus(response.SC_MOVED_TEMPORARILY);
                response.setHeader("Location", "play.jsp");
            %>
        </c:if>
        <div class="jumbotron container">
            <div class="panel panel-default">
                <div class="panel-heading">
                    Generate a new Game of life field
                </div>
                <div class="panel-body">
                    <form class="form-horizontal" action="/play" method="post">
                        <c:if test="${not empty message}">                
                            <div class="alert alert-warning">
                                ${message}
                            </div>
                            <%
                                session.removeAttribute("message");
                            %>
                        </c:if> 
                        <div class="input-group">
                            <div class="input-group-addon">Finite</div>
                            <select class="form-control" name="finiteness" autocomplete="off"
                                onchange="javascript:document.getElementById('finite-settings').style.display =
                                    this.selectedIndex == 1 ? 'none' : 'inline'; if (this.selectedIndex == 1) {
                                        windowing = document.getElementById('windowing');
                                        windowing.selectedIndex = 0;
                                        windowing.onchange()
                                        windowing.disabled = 'disable';
                                    } else {
                                        windowing.disabled = false
                                    }">
                                <option value="finite" selected>true</option>
                                <option value="infinite">false</option>
                            </select>
                        </div>
                        <div id="finite-settings">
                            <div class="input-group">
                                <div class="input-group-addon">Cyclic</div>
                                <select class="form-control" name="cyclicity">
                                    <option value="cyclic" selected>true</option>
                                    <option value="acyclic">false</option>
                                </select>
                            </div>
                            <div class="input-group">
                                <div class="input-group-addon">Width</div>
                                <input type="number" class="form-control" name="field-width" value="10" min="1">
                                <div class="input-group-addon">Height</div>
                                <input type="number" class="form-control" name="field-height" value="10" min="1">
                            </div>
                        </div>
                        <div class="input-group">
                            <div class="input-group-addon">Windowing</div>
                            <select class="form-control" name="windowing" autocomplete="off"
                                onchange="javascript:document.getElementById('size-settings').style.display =
                                    this.selectedIndex == 1 ? 'none' : 'inline'">
                                <option value="windowing">true</option>
                                <option value="no-windowing" selected>false</option>
                            </select>
                        </div>
                        <div id="size-settings" style="display:none">
                            <div class="input-group">
                                <div class="input-group-addon">From left</div>
                                <input type="number" class="form-control" name="show-left" value="0">
                                <div class="input-group-addon">From top</div>
                                <input type="number" class="form-control" name="show-top" value="0">
                            </div>
                            <div class="input-group">
                                <div class="input-group-addon">Window width</div>
                                <input type="number" class="form-control" name="show-width" min="1" max="100" value="10">
                                <div class="input-group-addon">Window height</div>
                                <input type="number" class="form-control" name="show-height" min="1" max="100" value="10">
                            </div>
                        </div>
                        <div class="input-group">
                            <div class="input-group-addon">Seed</div>
                            <input type="number" class="form-control" name="seed" value="<% out.print(new Random().nextInt(65536) - 32768); %>">
                        </div>
                        <button type="submit" class="btn btn-default">Generate</button>
                    </form>
                </div>
            </div>
        </div>     
    </body>
</html>
