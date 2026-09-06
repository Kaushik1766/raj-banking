<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ include file="common/header.jsp" %>

<div class="container narrow">
    <h1><fmt:message key="customer.search.title" bundle="${msg}" /></h1>

    <c:if test="${not empty errorMessage}">
        <div class="alert alert-error">${errorMessage}</div>
    </c:if>

    <form method="post" action="${pageContext.request.contextPath}/maker/customer/search">
        <div class="form-group">
            <label for="customerId"><fmt:message key="customer.search.label" bundle="${msg}" /></label>
            <input type="text" id="customerId" name="customerId" value="${customerId}" required autofocus>
        </div>
        <button type="submit" class="btn"><fmt:message key="customer.search.btn" bundle="${msg}" /></button>
    </form>
</div>

<%@ include file="common/footer.jsp" %>
