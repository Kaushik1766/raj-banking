<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ include file="common/header.jsp" %>

<div class="container">
    <h1><fmt:message key="customer.form.title" bundle="${msg}" /></h1>

    <c:if test="${not empty fieldErrors}">
        <div class="alert alert-error"><fmt:message key="error.validationFailed" bundle="${msg}" /></div>
    </c:if>

    <c:set var="readOnlyAttr" value="${mode == 'EDIT' ? 'readonly' : ''}" />

    <form method="post" action="${pageContext.request.contextPath}/maker/customer/save">
        <input type="hidden" name="mode" value="${mode}">
        <c:if test="${mode == 'EDIT'}">
            <input type="hidden" name="customerId" value="${customer.customerId}">
            <div class="form-group">
                <label><fmt:message key="label.customerId" bundle="${msg}" /></label>
                <input type="text" value="${customer.customerId}" readonly>
            </div>
        </c:if>

        <div class="form-group">
            <label for="fullName"><fmt:message key="customer.form.fullName" bundle="${msg}" /></label>
            <input type="text" id="fullName" name="fullName" value="${customer.fullName}" required>
            <c:if test="${not empty fieldErrors['fullName']}"><div class="field-error">${fieldErrors['fullName']}</div></c:if>
        </div>

        <div class="form-group">
            <label for="dateOfBirth"><fmt:message key="customer.form.dob" bundle="${msg}" /></label>
            <input type="date" id="dateOfBirth" name="dateOfBirth" value="${customer.dateOfBirth}" ${readOnlyAttr} required>
            <c:if test="${not empty fieldErrors['dateOfBirth']}"><div class="field-error">${fieldErrors['dateOfBirth']}</div></c:if>
        </div>

        <div class="form-group">
            <label for="gender"><fmt:message key="customer.form.gender" bundle="${msg}" /></label>
            <select id="gender" name="gender" required>
                <option value="">--</option>
                <option value="MALE" ${customer.gender == 'MALE' ? 'selected' : ''}>Male</option>
                <option value="FEMALE" ${customer.gender == 'FEMALE' ? 'selected' : ''}>Female</option>
                <option value="OTHER" ${customer.gender == 'OTHER' ? 'selected' : ''}>Other</option>
            </select>
        </div>

        <div class="form-group">
            <label for="email"><fmt:message key="customer.form.email" bundle="${msg}" /></label>
            <input type="email" id="email" name="email" value="${customer.email}" required>
            <c:if test="${not empty fieldErrors['email']}"><div class="field-error">${fieldErrors['email']}</div></c:if>
        </div>

        <div class="form-group">
            <label for="phone"><fmt:message key="customer.form.phone" bundle="${msg}" /></label>
            <input type="text" id="phone" name="phone" value="${customer.phone}" maxlength="10" required>
            <c:if test="${not empty fieldErrors['phone']}"><div class="field-error">${fieldErrors['phone']}</div></c:if>
        </div>

        <div class="form-group">
            <label for="address"><fmt:message key="customer.form.address" bundle="${msg}" /></label>
            <textarea id="address" name="address" rows="2" required>${customer.address}</textarea>
            <c:if test="${not empty fieldErrors['address']}"><div class="field-error">${fieldErrors['address']}</div></c:if>
        </div>

        <div class="form-group">
            <label for="panNumber"><fmt:message key="customer.form.pan" bundle="${msg}" /></label>
            <input type="text" id="panNumber" name="panNumber" value="${customer.panNumber}" maxlength="10" ${readOnlyAttr} required>
            <c:if test="${not empty fieldErrors['panNumber']}"><div class="field-error">${fieldErrors['panNumber']}</div></c:if>
        </div>

        <div class="form-group">
            <label for="aadharNumber"><fmt:message key="customer.form.aadhar" bundle="${msg}" /></label>
            <input type="text" id="aadharNumber" name="aadharNumber" value="${customer.aadharNumber}" maxlength="12" ${readOnlyAttr} required>
            <c:if test="${not empty fieldErrors['aadharNumber']}"><div class="field-error">${fieldErrors['aadharNumber']}</div></c:if>
        </div>

        <div class="form-group">
            <label for="annualIncome"><fmt:message key="customer.form.annualIncome" bundle="${msg}" /></label>
            <input type="number" step="0.01" min="0" id="annualIncome" name="annualIncome" value="${customer.annualIncome}" required>
            <c:if test="${not empty fieldErrors['annualIncome']}"><div class="field-error">${fieldErrors['annualIncome']}</div></c:if>
        </div>

        <div class="form-group">
            <label for="employmentType"><fmt:message key="customer.form.employmentType" bundle="${msg}" /></label>
            <select id="employmentType" name="employmentType" required>
                <option value="">--</option>
                <option value="SALARIED" ${customer.employmentType == 'SALARIED' ? 'selected' : ''}>Salaried</option>
                <option value="SELF_EMPLOYED" ${customer.employmentType == 'SELF_EMPLOYED' ? 'selected' : ''}>Self Employed</option>
                <option value="BUSINESS" ${customer.employmentType == 'BUSINESS' ? 'selected' : ''}>Business</option>
                <option value="UNEMPLOYED" ${customer.employmentType == 'UNEMPLOYED' ? 'selected' : ''}>Unemployed</option>
            </select>
        </div>

        <button type="submit" class="btn"><fmt:message key="customer.form.submit" bundle="${msg}" /></button>
    </form>
</div>

<%@ include file="common/footer.jsp" %>
