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
            <h1>Post new article</h1>
            <form id="box" action="postNewArticle" method="POST">
                <input type="submit" value="Post it!" name="postButton" />
                <div>
                    <label for="txtTitle">Title:</label>
                    <input type="text" id="txtTitle" name="title" value="" />
                </div>
                <div>
                    <label for="txtText">Text:</label>
                    <textarea id="txtText" name="text" rows="20" cols="80"/>
                </div>
                <br/>
            </form>
        </div>
        <%@include file="foot.jsp" %>
    </body>
</html>
