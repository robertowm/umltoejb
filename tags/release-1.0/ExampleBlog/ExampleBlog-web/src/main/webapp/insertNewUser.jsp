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
            <h1>Insert new user</h1>
            <form id="box" action="insertNewUser" method="POST">
                <label for="txtFirstName">First Name:</label>
                <input type="text" id="txtFirstName" name="firstname" value="" />

                <label for="txtLastName">Last Name:</label>
                <input type="text" id="txtLastName" name="lastname" value="" />

                <label for="txtNickname">Nickname:</label>
                <input type="text" id="txtNickname" name="nickname" value="" />

                <label for="txtEmail">E-mail:</label>
                <input type="text" id="txtEmail" name="email" value="" />

                <label for="txtLogin">Login:</label>
                <input type="text" id="txtLogin" name="login" value="" />

                <label for="txtPassword">Password:</label>
                <input type="password" id="txtPassword" name="password" value="" />

                <input type="submit" value="Save" name="saveButton" />
            </form>
        </div>
        <%@include file="foot.jsp" %>
    </body>
</html>
