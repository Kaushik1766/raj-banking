<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ include file="common/header.jsp" %>

<div class="container narrow">
    <h1><fmt:message key="label.login" bundle="${msg}" /></h1>

    <c:if test="${not empty errorMessageKey}">
        <div class="alert alert-error"><fmt:message key="${errorMessageKey}" bundle="${msg}" /></div>
    </c:if>

    <form method="post" action="${pageContext.request.contextPath}/login">
        <div class="form-group">
            <label for="username"><fmt:message key="label.username" bundle="${msg}" /></label>
            <input type="text" id="username" name="username" value="${username}" required autofocus>
        </div>
        <div class="form-group">
            <label for="password"><fmt:message key="label.password" bundle="${msg}" /></label>
            <input type="password" id="password" name="password" required>
        </div>
        <div class="checkbox-row">
            <input type="checkbox" id="keepSignedIn" name="keepSignedIn">
            <label for="keepSignedIn" style="margin:0;font-weight:normal;"><fmt:message key="label.keepSignedIn" bundle="${msg}" /></label>
        </div>
        <button type="submit" class="btn"><fmt:message key="label.login" bundle="${msg}" /></button>
    </form>
</div>

<%@ include file="common/footer.jsp" %>
