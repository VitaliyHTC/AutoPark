<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html><head><meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta charset="UTF-8">
    <title>Register Page</title>
    <link rel="stylesheet" href="./css/style.css" type="text/css" media="screen">
</head>
<body>

<div class="RegisterFormOuter">
    <div class="RegisterForm">
        <div class="RegisterFormHeader">
            <span>Provide all the fields for registration.</span>
        </div>
        <div class="RegisterFormContent">
            <% String  s1  = (String) session.getAttribute("errors"); %>
            <%= s1 %>
            <form action="Register" method="post">
                <strong>Email</strong>:<br><input type="text" name="email"><br>
                <strong>Password</strong>:<br><input type="password" name="password"><br>
                <strong>Username</strong>:<br><input type="text" name="username"><br>
                <strong>Firstname</strong>:<br><input type="text" name="firstname"><br>
                <strong>Lastname</strong>:<br><input type="text" name="lastname"><br>
                <input type="submit" value="Register">
            </form>
            <br>
            If you are registered user, please <a href="login.html">login</a>.
        </div>
    </div>
</div>

</body>
</html>