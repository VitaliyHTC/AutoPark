<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="com.vitaliyhtc.autopark.util.User"%>
<%@ page import="java.util.HashMap" %>
<%@ page import="com.vitaliyhtc.autopark.objects.Driver" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html><head><meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta charset="UTF-8">
    <title>Drivers list</title>
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
            AutoPark : Список водіїв
        </div>
        <div class="main-content">
            <a href="home">На початкову сторінку.</a><br><br><br>

            <h3>Список водіїв</h3>

            <% HashMap<Integer, Driver> driversMap = (HashMap<Integer, Driver>) session.getAttribute("driversMap"); %>
            <c:if test="${!empty driversMap}">
                <table class="tg">
                    <tr>
                        <th width="40">ID</th>
                        <th width="100%">Інформація про водія</th>
                        <th width="60">Редагуєм?</th>
                    </tr>
                    <c:forEach items="${driversMap}" var="driver">
                        <tr>
                            <td>${driver.value.getId()}</td>
                            <td>
                                <b>${driver.value.getUsername()}</b>&nbsp;&nbsp;&nbsp;
                                <b>${driver.value.getFirstname()}&nbsp;${driver.value.getLastname()}</b>&nbsp;&nbsp;&nbsp;
                                ${driver.value.getPhone1()}&nbsp;${driver.value.getPhone2()}&nbsp;${driver.value.getPhoneRelatives()}&nbsp;
                                ${driver.value.getEmail()}<br>
                                <c:if test="${!empty driver.value.getDriverCategoriesMap()}">
                                    Категорії:&nbsp;
                                    <b>
                                    <c:forEach items="${driver.value.getDriverCategoriesMap()}" var="drivingLicenceCategory">
                                        ${drivingLicenceCategory.value.getCategory()}&nbsp;
                                    </c:forEach>
                                    </b><a href="<c:url value='/drivers?itemIDtoEditCategories=${driver.value.getId()}'/>">Змінити категорії</a><br>
                                </c:if>
                                <c:if test="${!empty driver.value.getDriverCategoriesMap()}">
                                    <table class="tg">
                                        <tr>
                                            <th width="40">ID</th>
                                            <th width="100">Виробник</th>
                                            <th width="120">Модель</th>
                                            <th width="120">Номер</th>
                                        </tr>
                                        <c:forEach items="${driver.value.getDriverTrucksMap()}" var="truck">
                                            <tr>
                                                <td>${truck.value.getId()}</td>
                                                <td>${truck.value.getManufacturer()}</td>
                                                <td>${truck.value.getModel()}</td>
                                                <td>${truck.value.getLicencePlateNumber()}</td>
                                            </tr>
                                        </c:forEach>
                                    </table>
                                    <a href="<c:url value='/drivers?itemIDtoEditTrucks=${driver.value.getId()}'/>">Змінити список авто</a>
                                    <hr>
                                </c:if>
                            </td>
                            <td><a href="<c:url value='/drivers?itemIDtoEdit=${driver.value.getId()}'/>">Edit</a></td>
                        </tr>
                    </c:forEach>
                </table>
            </c:if>

        </div>
    </div>
</div>

</body>
</html>
