<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ include file="common/header.jsp" %>

<div class="container">
    <h1><fmt:message key="customer.choice.title" bundle="${msg}" /></h1>
    <p><fmt:message key="customer.choice.question" bundle="${msg}" /></p>

    <div class="choice-options">
        <div class="choice-card">
            <h2><fmt:message key="customer.choice.existing" bundle="${msg}" /></h2>
            <a class="btn" href="${pageContext.request.contextPath}/maker/customer/search">
                <fmt:message key="customer.choice.existing" bundle="${msg}" />
            </a>
        </div>
        <div class="choice-card">
            <h2><fmt:message key="customer.choice.new" bundle="${msg}" /></h2>
            <a class="btn" href="${pageContext.request.contextPath}/maker/customer/new">
                <fmt:message key="customer.choice.new" bundle="${msg}" />
            </a>
        </div>
    </div>
</div>

<%@ include file="common/footer.jsp" %>
