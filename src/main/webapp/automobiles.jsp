<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="com.vitaliyhtc.autopark.util.User"%>
<%@ page import="java.util.ArrayList" %>
<%@ page import="com.vitaliyhtc.autopark.objects.AutoManufacturer" %>
<%@ page import="com.vitaliyhtc.autopark.objects.Truck" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html><head><meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta charset="UTF-8">
    <title>Automobiles</title>
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
            AutoPark : Список транспортних засобів автопарку
        </div>
        <div class="main-content">
            <a href="home">На початкову сторінку.</a><br>

            <h3>Список транспортних засобів автопарку</h3>
            <% ArrayList<Truck> listTrucks =
                    (ArrayList<Truck>) session.getAttribute("listTrucks");
            %>
            <c:if test="${!empty listTrucks}">
                <table class="tg">
                    <tr>
                        <th width="40">ID</th>
                        <th width="100%">Інформація про авто</th>
                        <th width="60">Редагуєм?</th>
                    </tr>
                    <c:forEach items="${listTrucks}" var="listItem">
                        <tr>
                            <td>${listItem.getId()}</td>
                            <td>
                                <b>${listItem.getManufacturerName()}</b>&nbsp;
                                <b>${listItem.getModel()}</b>&nbsp;&nbsp;&nbsp;
                                ${listItem.getEngineModel()}&nbsp;&nbsp;&nbsp;
                                ${listItem.getEnginePower()}&nbsp;HP&nbsp;&nbsp;&nbsp;
                                EURO${listItem.getEngineEco()}&nbsp;&nbsp;&nbsp;
                                ${listItem.getGearbox()}&nbsp;&nbsp;&nbsp;
                                ${listItem.getChassisType()}<br>
                                Масса без навантаження: ${listItem.getEquippedWeight()}кг;&nbsp;&nbsp;&nbsp;
                                Максимальна технічна масса: ${listItem.getMaxWeight()}кг;<br>
                                Категорія: ${listItem.getDrivingLicenceCategoryName()}; |&nbsp;
                                ${listItem.getLicencePlateNumber()} |&nbsp;
                                VIN:&nbsp;${listItem.getVinNumber()}<br>
                                ${listItem.getDescription()}
                            </td>
                            <td><a href="<c:url value='/automobiles?itemIDtoEdit=${listItem.getId()}'/>">Edit</a></td>
                        </tr>
                    </c:forEach>
                </table>
            </c:if>

            <br>



            <!-- end of main-content div-->
        </div>
    </div>
</div>

</body>
</html>
