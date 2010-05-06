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
            <h1>Articles</h1>
            <%
                        Collection<Article> articles = (Collection) request.getAttribute("articles");
                        if (articles == null) {
            %>
                            <div id="error">Can't load the articles' list.</div>
            <%
                        } else if (articles.isEmpty()) {
            %>
                            <div id="warn">No article was posted.</div>
            <%
                        } else {
                            for (Article article : articles) {
                                ArticleDataObject articleDataObject = article.getArticleDataObject();
            %>
                                <form action="article" method="GET" class="article">
                                    <h2><%= articleDataObject.getTitle()%></h2>
                                    <h3><%= articleDataObject.getText()%></h3>
                                    <input type="hidden" value="<%= articleDataObject.getArticleKey().articleID %>" name="articleID" />
                                    <input type="submit" value="Detail this article!" />
                                </form>
            <%
                            }
                        }
            %>
        </div>
        <%@include file="foot.jsp" %>
    </body>
</html>
