<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head><title>정기예금 해지 이력</title></head>
<body>
<h2>최근 1년 해지 이력</h2>
<table border="1">
    <tr>
        <th><a href="closeHist.do?sort=ACNO">계좌번호</a></th>
        <th><a href="closeHist.do?sort=CNCL_DT">해지일자</a></th>
        <th><a href="closeHist.do?sort=INTR_AMT">이자</a></th>
        <th>세금</th>
    </tr>
    <c:forEach var="row" items="${list}">
    <tr>
        <td>${row.ACNO}</td>
        <td>${row.CNCL_DT}</td>
        <td>${row.INTR_AMT}</td>
        <td>${row.TAX_AMT}</td>
    </tr>
    </c:forEach>
</table>
<p>이자 합계: ${totIntr}</p>
</body>
</html>
