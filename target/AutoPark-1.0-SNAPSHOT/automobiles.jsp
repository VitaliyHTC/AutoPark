<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="com.vitaliyhtc.autopark.util.User"%>
<%@ page import="java.util.ArrayList" %>
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
            <a href="home">На початкову сторінку.</a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
            <a href="/drivers">Водії</a>&nbsp;&nbsp;&nbsp;
            <a href="/automobiles">Автомобілі</a>&nbsp;&nbsp;&nbsp;
            <a href="/automanufacturers">Список виробників авто</a>&nbsp;&nbsp;&nbsp;
            <a href="/drivinglicencecategories"><i>Довідка:</i> Список категорій транспортних засобів</a>
            <br>
            <c:if test="${!empty DeleteSuccessful}">
                <br><span class="Successful">${DeleteSuccessful}</span><br>
            </c:if>
            <c:if test="${!empty DeleteFailed}">
                <br><span class="Failed">${DeleteFailed}</span><br>
            </c:if>
            <br>
            <% ArrayList<Truck> listTrucks =
                    (ArrayList<Truck>) session.getAttribute("listTrucks");
            %>
            <c:if test="${!empty listTrucks}">
                <table class="w100">
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
                            <td>
                                <a href="<c:url value='/automobiles?itemIDtoEdit=${listItem.getId()}'/>">Edit</a><br>
                                <a href="<c:url value='/automobiles?itemIDtoDelete=${listItem.getId()}'/>">Delete</a>
                            </td>
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
                <c:when test="${!empty itemTruckToEdit}">
                    Редагування автомобіля.<br>
                    <form action="automobiles" method="post">
                        ID:<br><input type="hidden" name="truck_id" value ="${itemTruckToEdit.getId()}">
                        <input type="text" value="${itemTruckToEdit.getId()}" disabled><br>
                        <br>
                        Ім'я:<br>
                        <c:forEach items="${listAM}" var="listItem">
                            <input type="radio" name="manufacturerID" value="${listItem.getId()}"
                            <c:if test="${itemTruckToEdit.getManufacturerID()==listItem.getId()}">
                                   checked
                            </c:if>
                            >${listItem.getManufacturer_name()}&nbsp;
                        </c:forEach>
                        <br>
                        Модель авто:<br>
                            <input type="text" name="truckModel" value="${itemTruckToEdit.getModel()}" maxlength="64"><br>
                        Модель двигуна:<br>
                            <input type="text" name="engineModel" value="${itemTruckToEdit.getEngineModel()}" maxlength="64"><br>
                        Потужність двигуна HP:<br>
                            <input type="number" name="enginePower" value="${itemTruckToEdit.getEnginePower()}" min="10" max="10000"><br>
                        Екологічний клас двигуна EURO_:<br>
                            <input type="number" name="engineEco" value="${itemTruckToEdit.getEngineEco()}" min="1" max="7"><br>
                        Коробка передач:<br>
                            <input type="text" name="gearbox" value="${itemTruckToEdit.getGearbox()}" maxlength="64"><br>
                        Тип шассі, колісна формула:<br>
                            <input type="text" name="chassisType" value="${itemTruckToEdit.getChassisType()}" maxlength="64"><br>
                        Масса авто в спорядженому стані кг:<br>
                            <input type="number" name="equippedWeight" value="${itemTruckToEdit.getEquippedWeight()}" min="100" max="400000"><br>
                        Максимальна технічна масса авто кг:<br>
                            <input type="number" name="maxWeight" value="${itemTruckToEdit.getMaxWeight()}" min="100" max="1000000"><br>
                        Необхідна категорія водійських прав:<br>
                        <c:forEach items="${listDLC}" var="listItem">
                            <input type="radio" name="drivingLicenceCategoryID" value="${listItem.getId()}"
                            <c:if test="${itemTruckToEdit.getDrivingLicenceCategoryID()==listItem.getId()}">
                                   checked
                            </c:if>
                            >${listItem.getCategory()}&nbsp;
                        </c:forEach>
                        <br>
                        Номерний знак авто::<br>
                            <input type="text" name="licencePlateNumber" value="${itemTruckToEdit.getLicencePlateNumber()}" maxlength="32"><br>
                        VIN::<br>
                        <input type="text" name="vinNumber" value="${itemTruckToEdit.getVinNumber()}" maxlength="32"><br>
                            Опис авто::<br>
                        <textarea name="description" maxlength="255" rows="7" cols="51">${itemTruckToEdit.getDescription()}</textarea><br>

                        <input type="submit" value="Редагуєм!">
                    </form>
                    <a href="automobiles">Не редагуєм.</a>
                </c:when>
                <c:otherwise>
                    Додаємо автомобіль.<br>
                    <form action="automobiles" method="post">
                        ID:<br><input type="hidden" name="truck_id" value ="-1">
                        <input type="text" value="-1" disabled><br>
                        <br>
                        Ім'я:<br>
                        <c:forEach items="${listAM}" var="listItem">
                            <input type="radio" name="manufacturerID" value="${listItem.getId()}">${listItem.getManufacturer_name()}&nbsp;
                        </c:forEach>
                        <br>
                        Модель авто:<br>
                            <input type="text" name="truckModel" value="" maxlength="64"><br>
                        Модель двигуна:<br>
                            <input type="text" name="engineModel" value="" maxlength="64"><br>
                        Потужність двигуна HP:<br>
                            <input type="number" name="enginePower" value="" min="10" max="10000"><br>
                        Екологічний клас двигуна EURO_:<br>
                            <input type="number" name="engineEco" value="" min="1" max="7"><br>
                        Коробка передач:<br>
                            <input type="text" name="gearbox" value="" maxlength="64"><br>
                        Тип шассі, колісна формула:<br>
                            <input type="text" name="chassisType" value="" maxlength="64"><br>
                        Масса авто в спорядженому стані кг:<br>
                            <input type="number" name="equippedWeight" value="" min="100" max="400000"><br>
                        Максимальна технічна масса авто кг:<br>
                            <input type="number" name="maxWeight" value="" min="100" max="1000000"><br>
                        Необхідна категорія водійських прав:<br>
                        <c:forEach items="${listDLC}" var="listItem">
                            <input type="radio" name="drivingLicenceCategoryID" value="${listItem.getId()}">${listItem.getCategory()}&nbsp;
                        </c:forEach>
                        <br>
                        Номерний знак авто::<br>
                            <input type="text" name="licencePlateNumber" value="" maxlength="32"><br>
                        VIN::<br>
                            <input type="text" name="vinNumber" value="" maxlength="32"><br>
                        Опис авто::<br>
                            <textarea name="description" maxlength="255" rows="7" cols="51"></textarea><br>

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
