<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ include file="common/header.jsp" %>

<div class="container narrow">
    <h1><fmt:message key="status.check.title" bundle="${msg}" /></h1>

    <c:if test="${not empty errorMessage}">
        <div class="alert alert-error">${errorMessage}</div>
    </c:if>

    <form method="post" action="${pageContext.request.contextPath}/status">
        <div class="form-group">
            <label for="applicationNumber"><fmt:message key="status.check.label" bundle="${msg}" /></label>
            <input type="text" id="applicationNumber" name="applicationNumber" value="${applicationNumber}" required autofocus>
        </div>
        <button type="submit" class="btn"><fmt:message key="status.check.btn" bundle="${msg}" /></button>
    </form>

    <c:if test="${not empty application}">
        <h2><fmt:message key="status.result.title" bundle="${msg}" /></h2>
        <table>
            <tr><th><fmt:message key="label.applicationNumber" bundle="${msg}" /></th><td>${application.applicationNumber}</td></tr>
            <tr><th><fmt:message key="loan.form.customer" bundle="${msg}" /></th><td>${application.customer.fullName}</td></tr>
            <tr><th><fmt:message key="loan.form.loanType" bundle="${msg}" /></th><td>${application.loanType}</td></tr>
            <tr><th><fmt:message key="loan.form.amount" bundle="${msg}" /></th><td>${application.loanAmount}</td></tr>
            <tr>
                <th><fmt:message key="label.status" bundle="${msg}" /></th>
                <td><span class="status-badge status-${application.status}">${application.status}</span></td>
            </tr>
        </table>
    </c:if>
</div>

<%@ include file="common/footer.jsp" %>
