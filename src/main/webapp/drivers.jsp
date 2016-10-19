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
                <table class="w100">
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
                                    </b>
                                </c:if>
                                <c:if test="${!empty driver.value.getDriverTrucksMap()}">
                                    <table class="truckList">
                                        <tr>
                                            <th width="40">ID</th>
                                            <th width="120">Виробник</th>
                                            <th width="160">Модель</th>
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
                                </c:if>
                                <a href="<c:url value='/drivers?itemIDtoEditTrucks=${driver.value.getId()}'/>">Змінити список авто</a>
                                <hr>
                            </td>
                            <td>
                                <a href="<c:url value='/drivers?itemIDtoEdit=${driver.value.getId()}'/>">Edit</a><br>
                                <a href="<c:url value='/drivers?itemIDtoDelete=${driver.value.getId()}'/>">Delete</a>
                            </td>
                        </tr>
                    </c:forEach>
                </table>
            </c:if>

            <br>

            <h3>Додавання/редагування водія</h3>
            <c:if test="${!empty AddUpdSuccessful}">
                <span class="Successful">${AddUpdSuccessful}</span><br><br>
            </c:if>
            <c:if test="${!empty AddUpdFailed}">
                <span class="Failed">${AddUpdFailed}</span><br><br>
            </c:if>

            <c:choose>
                <c:when test="${!empty itemDriverToEdit}">
                    <!--edit driver and categories list-->
                    Редагуєм водія.<br>
                    <form action="drivers" method="post">
                        ID:<br>
                        <input type="hidden" name="driver_id" value="${itemDriverToEdit.getId()}">
                        <input type="text" value="${itemDriverToEdit.getId()}" disabled><br>
                        <br>
                        Ім'я користувача:<br>
                        <input type="text" name="username" value="${itemDriverToEdit.getUsername()}" maxlength="64"><br>
                        Ім'я:<br>
                        <input type="text" name="firstname" value="${itemDriverToEdit.getFirstname()}" maxlength="64"><br>
                        Прізвище:<br>
                        <input type="text" name="lastname" value="${itemDriverToEdit.getLastname()}" maxlength="64"><br>
                        Номер телефону 1:<br>
                        <input type="text" name="phone1" value="${itemDriverToEdit.getPhone1()}" maxlength="24"><br>
                        Номер телефону 2:<br>
                        <input type="text" name="phone2" value="${itemDriverToEdit.getPhone2()}" maxlength="24"><br>
                        Номер телефону рідних:<br>
                        <input type="text" name="phoneRelatives" value="${itemDriverToEdit.getPhoneRelatives()}" maxlength="24"><br>
                        Адрес електронної пошти:<br>
                        <input type="text" name="email" value="${itemDriverToEdit.getEmail()}" maxlength="255"><br>
                        Категорії водія:<br>
                        <c:forEach items="${listDLC}" var="listItem">
                            <input type="checkbox" name="${listItem.getId()}_${listItem.getCategory()}"
                            <c:if test="${dlcChecked.get(listItem.getId()-1)}"> checked </c:if>
                            >${listItem.getCategory()}&nbsp;
                        </c:forEach>
                        <br>

                        <input type="submit" value="Редагуєм!">
                    </form>
                    <a href="drivers">Не редагуєм.</a>
                </c:when>
                <c:when test="${!empty itemDriverForTrucksListEditing}">
                    <!--edit driver trucks list-->
                    Редагуєм список автомобілів водія.<br>
                    <form action="drivers" method="post">
                        Driver ID:<br>
                        <input type="hidden" name="driverTrucks_id" value="${itemDriverForTrucksListEditing.getId()}">
                        <input type="text" value="${itemDriverForTrucksListEditing.getId()}" disabled><br>
                        <b>
                        ${itemDriverForTrucksListEditing.getUsername()}&nbsp;&nbsp;&nbsp;
                        ${itemDriverForTrucksListEditing.getFirstname()}&nbsp;
                        ${itemDriverForTrucksListEditing.getLastname()}
                        </b>
                        <br>

                        Список авто якими може керувати водій:<br>
                        <c:forEach items="${listPossibleTrucks}" var="listItem">
                            <input type="checkbox" name="truckID_${listItem.getId()}"
                            <c:if test="${selectedTrucksSet.contains(listItem.getId())}"> checked </c:if>
                            >&nbsp;
                            ID=${listItem.getId()};&nbsp;&nbsp;&nbsp;
                            ${listItem.getManufacturer()}&nbsp;
                            ${listItem.getModel()}&nbsp;&nbsp;&nbsp;
                            ${listItem.getLicencePlateNumber()}
                            <br>
                        </c:forEach>
                        <br>

                        <input type="submit" value="Редагуєм!">
                    </form>
                    <a href="drivers">Не редагуєм.</a>
                </c:when>
                <c:otherwise>
                    <!--add driver with categories list-->
                    Додаємо водія.<br>
                    <form action="drivers" method="post">
                        ID:<br>
                            <input type="hidden" name="driver_id" value="-1">
                        <input type="text" value="-1" disabled><br>
                        <br>
                        Ім'я користувача:<br>
                            <input type="text" name="username" value="" maxlength="64"><br>
                        Ім'я:<br>
                            <input type="text" name="firstname" value="" maxlength="64"><br>
                        Прізвище:<br>
                            <input type="text" name="lastname" value="" maxlength="64"><br>
                        Номер телефону 1:<br>
                            <input type="text" name="phone1" value="" maxlength="24"><br>
                        Номер телефону 2:<br>
                            <input type="text" name="phone2" value="" maxlength="24"><br>
                        Номер телефону рідних:<br>
                            <input type="text" name="phoneRelatives" value="" maxlength="24"><br>
                        Адрес електронної пошти:<br>
                            <input type="text" name="email" value="" maxlength="255"><br>
                        Категорії водія:<br>
                        <c:forEach items="${listDLC}" var="listItem">
                            <input type="checkbox" name="${listItem.getId()}_${listItem.getCategory()}">${listItem.getCategory()}&nbsp;
                        </c:forEach>
                        <br>

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
