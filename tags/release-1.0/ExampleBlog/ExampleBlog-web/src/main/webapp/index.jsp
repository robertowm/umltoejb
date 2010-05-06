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
            <p>
                If you want to log in and create your own account, log as [login:'admin', password:'admin'].
            </p>
            <p>
                Enjoy!
            </p>
            <br/>
            <p>
            -- Dev Team
            </p>
        </div>
        <%@include file="foot.jsp" %>
    </body>
</html>
