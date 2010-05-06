<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="java.util.*" %>
<%@ page import="app.*" %>
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
            <%
                        Article article = (Article) request.getAttribute("article");
                        ArticleDataObject data = article.getArticleDataObject();
            %>
            <h1><%= data.getTitle()%></h1>
            <h2><%= data.getText()%></h2>


            <fieldset>
                <label>Comments</label>
                <%
                        List<CommentDataObject> comments = data.getComments();
                        if (comments == null || comments.isEmpty()) {
            %>
            <div id="warn">No comment was posted.</div>
            <%          } else {
                            for (CommentDataObject comment : comments) {
            %>
            <div class="comment">
                <h4>From: <%= comment.getName()%></h4>
                <h4><%= comment.getText()%></h4>
            </div>
            <%
                            }
                        }
            %>
                <form action="commentArticle" method="GET" id="formComment">
                    <h4>Comment this article!</h4>
                    <input type="submit" value="Comment!" name="" />

                    <input type="hidden" name="articleID" value="<%= data.getArticleKey().articleID%>" />

                    <label for="txtName">Your name:</label>
                    <input type="text" id="txtName" name="name" value="" />

                    <label for="txtEmail">Your e-mail:</label>
                    <input type="text" id="txtEmail" name="email" value="" />

                    <label for="txtWebsite">Your website:</label>
                    <input type="text" id="txtWebsite" name="website" value="" />

                    <label for="txtText">Write your comment:</label>
                    <textarea id="txtText" name="text" rows="8" cols="40"/>

                </form>
            </fieldset>
        </div>
        <%@include file="foot.jsp" %>
    </body>
</html>
