<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <%@include file="head.jsp" %>
        <title>MDA's blog</title>
    </head>
    <body>
        <div id="title">
            <h1>MDA's blog!</h1>
        </div>
        <%@include file="menu.jsp"%>
        <%@include file="message.jsp"%>
        <div id="content">
            <form id="box" action="login" method="POST">
                <label for="txtLogin">Login:</label>
                <input type="text" id="txtLogin" name="login" value="" />

                <label for="txtPassword">Password:</label>
                <input type="password" id="txtPassword" name="password" value="" />

                <input type="submit" value="Log in!" name="loginButton" />
            </form>
        </div>
        <%@include file="foot.jsp" %>
    </body>
</html>
