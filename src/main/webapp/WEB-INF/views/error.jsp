<%@ page contentType="text/html;charset=UTF-8" isErrorPage="true" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ include file="common/header.jsp" %>

<div class="container narrow">
    <h1><fmt:message key="error.generic" bundle="${msg}" /></h1>
    <div class="alert alert-error">
        <c:choose>
            <c:when test="${not empty errorMessage}">${errorMessage}</c:when>
            <c:when test="${not empty errorMessageKey}"><fmt:message key="${errorMessageKey}" bundle="${msg}" /></c:when>
            <c:otherwise><fmt:message key="error.generic" bundle="${msg}" /></c:otherwise>
        </c:choose>
    </div>
    <a class="btn" href="${pageContext.request.contextPath}/login"><fmt:message key="btn.back" bundle="${msg}" /></a>
</div>

<%@ include file="common/footer.jsp" %>
