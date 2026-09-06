<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ include file="common/header.jsp" %>

<div class="container">
    <h1><fmt:message key="checker.review.title" bundle="${msg}" /> - ${application.applicationNumber}</h1>

    <h2><fmt:message key="loan.form.customer" bundle="${msg}" /></h2>
    <table>
        <tr><th><fmt:message key="customer.form.fullName" bundle="${msg}" /></th><td>${application.customer.fullName}</td></tr>
        <tr><th><fmt:message key="label.customerId" bundle="${msg}" /></th><td>${application.customer.customerId}</td></tr>
        <tr><th><fmt:message key="customer.form.dob" bundle="${msg}" /></th><td>${application.customer.dateOfBirth} (Age: ${application.customer.age})</td></tr>
        <tr><th><fmt:message key="customer.form.employmentType" bundle="${msg}" /></th><td>${application.customer.employmentType}</td></tr>
        <tr><th><fmt:message key="customer.form.annualIncome" bundle="${msg}" /></th><td>${application.customer.annualIncome}</td></tr>
    </table>

    <h2><fmt:message key="loan.form.title" bundle="${msg}" /></h2>
    <table>
        <tr><th><fmt:message key="loan.form.loanType" bundle="${msg}" /></th><td>${application.loanType}</td></tr>
        <tr><th><fmt:message key="loan.form.amount" bundle="${msg}" /></th><td>${application.loanAmount}</td></tr>
        <tr><th><fmt:message key="loan.form.tenure" bundle="${msg}" /></th><td>${application.tenureMonths}</td></tr>
        <tr><th><fmt:message key="loan.form.interestRate" bundle="${msg}" /></th><td>${application.interestRate}</td></tr>
        <tr><th><fmt:message key="loan.form.purpose" bundle="${msg}" /></th><td>${application.purpose}</td></tr>
    </table>

    <h2><fmt:message key="checker.review.eligibilityBtn" bundle="${msg}" /></h2>
    <div class="alert ${eligibility.eligible ? 'alert-success' : 'alert-error'}">
        <fmt:message key="${eligibilityMessageKey}" bundle="${msg}" />
    </div>
    <c:if test="${not eligibility.eligible}">
        <ul class="reasons-list">
            <c:forEach var="reason" items="${eligibility.reasons}">
                <li>${reason}</li>
            </c:forEach>
        </ul>
    </c:if>

    <form method="post" action="${pageContext.request.contextPath}/checker/review">
        <input type="hidden" name="applicationNumber" value="${application.applicationNumber}">
        <div class="form-group">
            <label for="remarks"><fmt:message key="checker.review.remarks" bundle="${msg}" /></label>
            <textarea id="remarks" name="remarks" rows="2"></textarea>
        </div>
        <button type="submit" name="action" value="approve" class="btn approve"><fmt:message key="checker.review.approveBtn" bundle="${msg}" /></button>
        <button type="submit" name="action" value="reject" class="btn reject"><fmt:message key="checker.review.rejectBtn" bundle="${msg}" /></button>
    </form>
</div>

<%@ include file="common/footer.jsp" %>
