<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="com.vitaliyhtc.autopark.util.User"%>
<%@ page import="com.vitaliyhtc.autopark.objects.DriverLicenceCategory" %>
<%@ page import="java.util.ArrayList" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html><head><meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta charset="UTF-8">
    <title>Driving licence categories</title>
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
            AutoPark : Список категорій транспортних засобів
        </div>
        <div class="main-content">
            <a href="home">На початкову сторінку.</a><br><br><br>

            <h3>Список категорій транспортних засобів</h3>
            <% ArrayList<DriverLicenceCategory> listDLC =
                    (ArrayList<DriverLicenceCategory>) session.getAttribute("listDLC");
            %>
            <c:if test="${!empty listDLC}">
                <table class="tg">
                    <tr>
                        <th width="40">ID</th>
                        <th width="60">Категорія</th>
                        <th width="100%">Опис</th>
                    </tr>
                    <c:forEach items="${listDLC}" var="DLCategory">
                        <tr>
                            <td>${DLCategory.getId()}</td>
                            <td>${DLCategory.getCategory()}</td>
                            <td>${DLCategory.getDescription()}</td>
                        </tr>
                    </c:forEach>
                </table>
            </c:if>

        </div>
    </div>
</div>

</body>
</html>
