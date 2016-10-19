<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="com.vitaliyhtc.autopark.util.User"%>
<%@ page import="java.util.ArrayList" %>
<%@ page import="com.vitaliyhtc.autopark.objects.AutoManufacturer" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html><head><meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta charset="UTF-8">
    <title>Auto manufacturers</title>
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
            AutoPark : Список виробників транспортних засобів
        </div>
        <div class="main-content">
            <a href="home">На початкову сторінку.</a><br>

            <h3>Список виробників транспортних засобів</h3>
            <% ArrayList<AutoManufacturer> listAM =
                    (ArrayList<AutoManufacturer>) session.getAttribute("listAM");
            %>
            <c:if test="${!empty listAM}">
                <table class="w100">
                    <tr>
                        <th width="40">ID</th>
                        <th width="120">Ім'я</th>
                        <th width="100%">Опис</th>
                        <th width="60">Редагуєм?</th>
                    </tr>
                    <c:forEach items="${listAM}" var="listItem">
                        <tr>
                            <td>${listItem.getId()}</td>
                            <td>${listItem.getManufacturer_name()}</td>
                            <td>${listItem.getDescription()}</td>
                            <td><a href="<c:url value='/automanufacturers?itemIDtoEdit=${listItem.getId()}'/>">Edit</a></td>
                        </tr>
                    </c:forEach>
                </table>
            </c:if>

            <br>

            <h3>Додавання/редагування автовиробника</h3>
            <c:if test="${!empty AddUpdSuccessful}">
                <span class="Successful">${AddUpdSuccessful}</span><br><br>
            </c:if>
            <c:if test="${!empty AddUpdFailed}">
                <span class="Failed">${AddUpdFailed}</span><br><br>
            </c:if>


            <c:choose>
                <c:when test="${!empty itemAMtoEdit}">
                    Редагування виробника.<br>
                    <form action="automanufacturers" method="post">
                        ID:<br><input type="hidden" name="manufacturer_id" value ="${itemAMtoEdit.getId()}">
                            <input type="text" value="${itemAMtoEdit.getId()}" disabled><br>
                        Ім'я:<br><input type="text" name="manufacturer_name" maxlength="64"
                                                         value="${itemAMtoEdit.getManufacturer_name()}"><br>
                        Опис:<br>
                            <textarea name="manufacturer_description" maxlength="255" rows="7"
                                        cols="51">${itemAMtoEdit.getDescription()}</textarea><br>
                        <input type="submit" value="Редагуєм!">
                    </form>
                    <a href="automanufacturers">Не редагуєм.</a>

                </c:when>
                <c:otherwise>
                    Додаємо автовиробника.<br>
                    <form action="automanufacturers" method="post">
                        ID:<br><input type="hidden" name="manufacturer_id" value ="-1">
                            <input type="text" value="-1" disabled><br>
                        Ім'я:<br><input type="text" name="manufacturer_name" value="" maxlength="64"><br>
                        Опис:<br>
                            <textarea name="manufacturer_description" maxlength="255" rows="7" cols="51"></textarea><br>
                        <input type="submit" value="Додаємо.">
                    </form>

                </c:otherwise>
            </c:choose>

            <!-- end of main-content div-->
        </div>
    </div>
</div>

</body>
</html>
