<%@ include file="/WEB-INF/views/include.jsp" %>

<html>
  <head><title><fmt:message key="title"/></title></head>
  <body>
    <h1><fmt:message key="heading"/></h1>
    <p><fmt:message key="greeting"/> <c:out value="${model.now}"/></p>
    <h3>Loan - FrenchMethod</h3>
    
    Loan debt: <i><c:out value="${model.products.getDebt()}"/></i><br/>
    Interest: <i><c:out value="${model.products.getInterest()}"/></i><br/>
    Num Fees: <i><c:out value="${model.products.getAmortizationTime()}"/></i><br/>
    Openning Comission: 0.0
    
	<h3>Amortization Table:</h3>
    
    <c:forEach items="${model.products.calcPayments()}" var="prod">
	      <c:out value="${prod.getExpiration()}"/> 
	      <i>$<c:out value="${prod.getImportOfTerm()}"/></i>
	      <i>$<c:out value="${prod.getAmortization()}"/></i>
	      <i>$<c:out value="${prod.getInterests()}"/></i>
	      <i>$<c:out value="${prod.getOutstandingCapital()}"/></i>
	      <i>$<c:out value="${prod.isPaid()}"/></i>
	      <br><br>
      </c:forEach>
    
    <br>
    <a href="<c:url value="feeLimits.htm"/>">Change number of Fees</a>
    <br>
  </body>
</html>