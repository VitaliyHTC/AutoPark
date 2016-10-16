<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html><head><meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta charset="UTF-8">
    <title>Login Page</title>
    <link rel="stylesheet" href="./css/style.css" type="text/css" media="screen">
</head>
<body>

<div class="LoginFormOuter">
    <div class="LoginForm">
        <div class="LoginFormHeader">
            <span>Login</span>
        </div>
        <div class="LoginFormContent">
            <% String  s1  = (String) session.getAttribute("errors"); %>
            <%= s1 %>
            <form action="Login" method="post">
                <strong>User Email</strong>:<br><input type="text" name="email"><br>
                <strong>Password</strong>:<br><input type="password" name="password"><br>
                <input type="submit" value="Login">
            </form>
            <br>
            If you are new user, please <a href="register.html">register</a>.
        </div>
    </div>
</div>

</body>
</html>