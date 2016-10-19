<%@page import="com.vitaliyhtc.autopark.util.User"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html><head><meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta charset="UTF-8">
    <title>Home Page</title>
    <link rel="stylesheet" href="./css/style.css" type="text/css" media="screen">
</head>
<body>

<div class="UserAccountOptionsOuter">
    <div class="UserAccountOptionsInner">
        <div class="UserInfo">
            <%User user = (User) session.getAttribute("User"); %>
            Hi <b><%=user.getUsername() %></b>;&nbsp;&nbsp;&nbsp;
            <%=user.getEmail() %>&nbsp;|&nbsp;<%=user.getFirstname() %>&nbsp;<%=user.getLastname() %>;
        </div>
        <div class="UserLogoutForm">
            <form action="Logout" method="post">
                <input type="submit" value="Logout" >
            </form>
        </div>
    </div>
</div>

<div class="all">
    <div class="all-wrap">
        <div class="main-header">
            AutoPark
        </div>
        <div class="main-content">
            <br><br><br>
            В програмі є наступні таблиці:
            <ul>
                <li><a href="/drivers">Водії</a></li>
                <li><a href="/automobiles">Автомобілі</a></li>
                <br>
                <li><a href="/automanufacturers">Список виробників авто</a></li>
                <br>
                <li><a href="/drivinglicencecategories"><i>Довідка:</i> Список категорій транспортних засобів</a></li>
            </ul>

            <br><br><br>
            <h3>Довідка.</h3>
            Для того щоб додати автомобіль - потрібна наявність автовиробика в списку.
            Якщо для авто немає вибору якогось автовиробника - додайте спочатку його у список автовиробників.<br><br>
            Закріпляти водіїв за автомобілями можна лише за наявності доданих водія та автомобіля.<br><br>
        </div>
    </div>
</div>

</body>
</html>
