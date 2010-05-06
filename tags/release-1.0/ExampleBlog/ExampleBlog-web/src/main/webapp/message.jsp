<%
    String error = (String) request.getAttribute("error");
    if (error != null) {
%>
        <div id="error">
            <%= error%>
        </div>
<%
    }
%>
<%
    String success = (String) request.getAttribute("success");
    if (success != null) {
%>
        <div id="success">
            <%= success%>
        </div>
<%
    }
%>