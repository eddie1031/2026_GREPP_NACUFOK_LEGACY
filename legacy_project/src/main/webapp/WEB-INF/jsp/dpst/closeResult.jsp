<%@ page contentType="text/html; charset=UTF-8" %>
<html>
<head><title>정기예금 해지 결과</title></head>
<body>
<h2>정기예금 해지가 완료되었습니다</h2>
<table border="1">
    <tr><th>계좌번호</th><td>${acno}</td></tr>
    <tr><th>이자</th><td>${intrAmt}</td></tr>
    <tr><th>세금</th><td>${taxAmt}</td></tr>
    <tr><th>지급액</th><td>${pymAmt}</td></tr>
</table>
</body>
</html>
