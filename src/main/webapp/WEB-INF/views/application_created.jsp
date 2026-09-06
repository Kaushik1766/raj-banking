<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ include file="common/header.jsp" %>

<div class="container narrow">
    <h1><fmt:message key="created.title" bundle="${msg}" /></h1>
    <p><fmt:message key="created.message" bundle="${msg}" /></p>
    <p><fmt:message key="created.appNoLabel" bundle="${msg}" /></p>
    <div class="app-number-box">${applicationNumber}</div>
    <a class="btn" href="${pageContext.request.contextPath}/maker/dashboard"><fmt:message key="nav.dashboard" bundle="${msg}" /></a>
</div>

<%@ include file="common/footer.jsp" %>
