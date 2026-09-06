<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ include file="common/header.jsp" %>

<div class="container">
    <c:if test="${not empty sessionScope.flashMessageKey}">
        <div class="alert alert-success"><fmt:message key="${sessionScope.flashMessageKey}" bundle="${msg}" /></div>
        <c:remove var="flashMessageKey" scope="session" />
    </c:if>

    <h1><fmt:message key="checker.dashboard.title" bundle="${msg}" /></h1>

    <c:choose>
        <c:when test="${empty applications}">
            <p><fmt:message key="checker.dashboard.empty" bundle="${msg}" /></p>
        </c:when>
        <c:otherwise>
            <table>
                <thead>
                <tr>
                    <th><fmt:message key="maker.dashboard.col.appNo" bundle="${msg}" /></th>
                    <th><fmt:message key="maker.dashboard.col.customer" bundle="${msg}" /></th>
                    <th><fmt:message key="maker.dashboard.col.loanType" bundle="${msg}" /></th>
                    <th><fmt:message key="maker.dashboard.col.amount" bundle="${msg}" /></th>
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
                        <td>${app.createdDate}</td>
                        <td>
                            <a class="btn" href="${pageContext.request.contextPath}/checker/review?applicationNumber=${app.applicationNumber}">
                                <fmt:message key="checker.dashboard.review" bundle="${msg}" />
                            </a>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </c:otherwise>
    </c:choose>
</div>

<%@ include file="common/footer.jsp" %>
