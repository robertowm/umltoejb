<div id="tabs">
    <li><a href="index.jsp"><span>Home</span></a></li>
    <li><a href="articles"><span>View articles</span></a></li>
    <li><a href="insertNewUser.jsp"><span>Insert new user</span></a></li>
    <%
        if (request.getSession().getAttribute("user") != null) {
    %>
            <li><a href="postNewArticle.jsp"><span>Post new article</span></a></li>
    <%
        }
    %>
    <li><a href="about.jsp"><span>About</span></a></li>

    <%
        if (request.getSession().getAttribute("user") == null) {
    %>
            <li><a href="login.jsp"><span>Log in</span></a></li>
    <%
        }
    %>
    <%
        if (request.getSession().getAttribute("user") != null) {
    %>
            <li><a href="logout"><span>Log out</span></a></li>
    <%
        }
    %>
</div>