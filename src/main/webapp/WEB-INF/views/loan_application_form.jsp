<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ include file="common/header.jsp" %>

<div class="container">
    <h1><fmt:message key="loan.form.title" bundle="${msg}" /></h1>

    <c:if test="${not empty fieldErrors}">
        <div class="alert alert-error"><fmt:message key="error.validationFailed" bundle="${msg}" /></div>
    </c:if>

    <div class="form-group">
        <label><fmt:message key="loan.form.customer" bundle="${msg}" /></label>
        <input type="text" value="${customer.fullName} (${customer.customerId})" readonly>
    </div>

    <form method="post" action="${pageContext.request.contextPath}/maker/loan/save">
        <input type="hidden" name="customerId" value="${customer.customerId}">

        <div class="form-group">
            <label for="loanType"><fmt:message key="loan.form.loanType" bundle="${msg}" /></label>
            <select id="loanType" name="loanType" required>
                <option value="">--</option>
                <option value="PERSONAL" ${application.loanType == 'PERSONAL' ? 'selected' : ''}>Personal</option>
                <option value="HOME" ${application.loanType == 'HOME' ? 'selected' : ''}>Home</option>
                <option value="VEHICLE" ${application.loanType == 'VEHICLE' ? 'selected' : ''}>Vehicle</option>
                <option value="EDUCATION" ${application.loanType == 'EDUCATION' ? 'selected' : ''}>Education</option>
                <option value="BUSINESS" ${application.loanType == 'BUSINESS' ? 'selected' : ''}>Business</option>
            </select>
        </div>

        <div class="form-group">
            <label for="loanAmount"><fmt:message key="loan.form.amount" bundle="${msg}" /></label>
            <input type="number" step="0.01" min="0.01" id="loanAmount" name="loanAmount" value="${application.loanAmount}" required>
            <c:if test="${not empty fieldErrors['loanAmount']}"><div class="field-error">${fieldErrors['loanAmount']}</div></c:if>
        </div>

        <div class="form-group">
            <label for="tenureMonths"><fmt:message key="loan.form.tenure" bundle="${msg}" /></label>
            <input type="number" step="1" min="1" id="tenureMonths" name="tenureMonths" value="${application.tenureMonths}" required>
            <c:if test="${not empty fieldErrors['tenureMonths']}"><div class="field-error">${fieldErrors['tenureMonths']}</div></c:if>
        </div>

        <div class="form-group">
            <label for="interestRate"><fmt:message key="loan.form.interestRate" bundle="${msg}" /></label>
            <input type="number" step="0.01" min="0" id="interestRate" name="interestRate" value="${application.interestRate}" required>
            <c:if test="${not empty fieldErrors['interestRate']}"><div class="field-error">${fieldErrors['interestRate']}</div></c:if>
        </div>

        <div class="form-group">
            <label for="purpose"><fmt:message key="loan.form.purpose" bundle="${msg}" /></label>
            <textarea id="purpose" name="purpose" rows="2">${application.purpose}</textarea>
        </div>

        <button type="submit" class="btn"><fmt:message key="loan.form.submit" bundle="${msg}" /></button>
    </form>
</div>

<%@ include file="common/footer.jsp" %>
