<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%
    String localeTag = com.rajbank.loanapp.util.MessageUtil.resolveLocale(request).toLanguageTag();
    request.setAttribute("uiLocale", localeTag);
%>
<fmt:setLocale value="${uiLocale}" />
<fmt:setBundle basename="messages" var="msg" />
<!DOCTYPE html>
<html lang="${uiLocale}">
<head>
    <meta charset="UTF-8">
    <title><fmt:message key="app.title" bundle="${msg}" /></title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<header class="app-header">
    <div class="brand"><fmt:message key="app.title" bundle="${msg}" /></div>
    <nav>
        <c:if test="${not empty sessionScope.loggedInUser}">
            <c:choose>
                <c:when test="${sessionScope.loggedInUser.role == 'MAKER'}">
                    <a href="${pageContext.request.contextPath}/maker/dashboard"><fmt:message key="nav.dashboard" bundle="${msg}" /></a>
                </c:when>
                <c:otherwise>
                    <a href="${pageContext.request.contextPath}/checker/dashboard"><fmt:message key="nav.dashboard" bundle="${msg}" /></a>
                </c:otherwise>
            </c:choose>
        </c:if>
        <a href="${pageContext.request.contextPath}/status"><fmt:message key="nav.checkStatus" bundle="${msg}" /></a>
        <a href="${pageContext.request.contextPath}/locale?lang=en">EN</a>
        <a href="${pageContext.request.contextPath}/locale?lang=fr">FR</a>
        <c:if test="${not empty sessionScope.loggedInUser}">
            <a href="${pageContext.request.contextPath}/logout"><fmt:message key="label.logout" bundle="${msg}" /></a>
        </c:if>
    </nav>
</header>
