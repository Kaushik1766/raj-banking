<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ include file="common/header.jsp" %>

<div class="container">
    <c:if test="${not empty sessionScope.flashMessageKey}">
        <div class="alert alert-success"><fmt:message key="${sessionScope.flashMessageKey}" bundle="${msg}" /></div>
        <c:remove var="flashMessageKey" scope="session" />
    </c:if>
    <c:if test="${not empty sessionScope.flashError}">
        <div class="alert alert-error">${sessionScope.flashError}</div>
        <c:remove var="flashError" scope="session" />
    </c:if>

    <fmt:message key="maker.dashboard.deleteConfirm" bundle="${msg}" var="deleteConfirmMsg" />

    <div class="actions-row">
        <h1><fmt:message key="maker.dashboard.title" bundle="${msg}" /></h1>
        <a class="btn" href="${pageContext.request.contextPath}/maker/customer/choice">
            <fmt:message key="maker.dashboard.newBtn" bundle="${msg}" />
        </a>
    </div>

    <c:choose>
        <c:when test="${empty applications}">
            <p><fmt:message key="maker.dashboard.empty" bundle="${msg}" /></p>
        </c:when>
        <c:otherwise>
            <table>
                <thead>
                <tr>
                    <th><fmt:message key="maker.dashboard.col.appNo" bundle="${msg}" /></th>
                    <th><fmt:message key="maker.dashboard.col.customer" bundle="${msg}" /></th>
                    <th><fmt:message key="maker.dashboard.col.loanType" bundle="${msg}" /></th>
                    <th><fmt:message key="maker.dashboard.col.amount" bundle="${msg}" /></th>
                    <th><fmt:message key="maker.dashboard.col.status" bundle="${msg}" /></th>
                    <th><fmt:message key="maker.dashboard.col.createdDate" bundle="${msg}" /></th>
                    <th><fmt:message key="maker.dashboard.col.actions" bundle="${msg}" /></th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="app" items="${applications}">
                    <tr>
                        <td>${app.applicationNumber}</td>
                        <td>${app.customer.fullName} (${app.customer.customerId})</td>
                        <td>${app.loanType}</td>
                        <td>${app.loanAmount}</td>
                        <td><span class="status-badge status-${app.status}">${app.status}</span></td>
                        <td>${app.createdDate}</td>
                        <td>
                            <c:if test="${app.status == 'PENDING'}">
                                <form method="post" action="${pageContext.request.contextPath}/maker/dashboard"
                                      onsubmit="return confirm('${deleteConfirmMsg}');"
                                      style="display:inline;">
                                    <input type="hidden" name="applicationNumber" value="${app.applicationNumber}">
                                    <button type="submit" class="btn danger">
                                        <fmt:message key="maker.dashboard.delete" bundle="${msg}" />
                                    </button>
                                </form>
                            </c:if>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </c:otherwise>
    </c:choose>
</div>

<%@ include file="common/footer.jsp" %>
