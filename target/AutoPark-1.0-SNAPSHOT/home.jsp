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
            <a href="home">На початкову сторінку.</a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
            <a href="/drivers">Водії</a>&nbsp;&nbsp;&nbsp;
            <a href="/automobiles">Автомобілі</a>&nbsp;&nbsp;&nbsp;
            <a href="/automanufacturers">Список виробників авто</a>&nbsp;&nbsp;&nbsp;
            <a href="/drivinglicencecategories"><i>Довідка:</i> Список категорій транспортних засобів</a>
            <br><br><br>
            В програмі є наступні сторінки:
            <ul>
                <li><a href="/drivers">Водії</a></li>
                <li><a href="/automobiles">Автомобілі</a></li>
                <li><a href="/automanufacturers">Список виробників авто</a></li>
                <br>
                <li><a href="/drivinglicencecategories"><i>Довідка:</i> Список категорій транспортних засобів</a></li>
            </ul>
            <h3>Довідка.</h3>
            Якщо при додаванні/редагуванні запису відбувається помилка - запис повертається на повторне редагування.<br><br>
            Для того щоб додати автомобіль - потрібна наявність автовиробика в списку.
            Якщо для авто немає вибору якогось автовиробника - додайте спочатку його у список автовиробників.<br><br>
            Для водія, заповніть його данні, відмітьте категорії транспортних засобів якими може керувати водій, та
            натисніть кнопку додати.<br><br>
            Ви можете змінити список автомобілів водія, перейшовши за відповідним посиланням. Для водія дозволяється
            вибрати лише ті авто, якими він може керувати згідно категорій водійського посвідчення. Авто якими він не
            має права керувати - не будуть відображатись для вибору.<br><br>
            Для того щоб видалити водія - спочатку треба очистити список закріплених за ним авто.<br>
            Для видалення автомобіля, спочатку треба зняти його привязку за водіями.<br>
            Для видалення автовиробника - видаліть автомобілі закріплені за ним.<br>
        </div>
    </div>
</div>

</body>
</html>
