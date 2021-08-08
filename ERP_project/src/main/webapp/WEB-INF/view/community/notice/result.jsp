<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<c:if test="${not empty resultBoolean }">
<c:if test="${resultBoolean }">
<script>
	alert("게시글이 ${resultType}되었습니다.");
	location.href=("/notice");
</script>
</c:if>	
<c:if test="${!resultBoolean }">
<script>
	alert("데이터가 처리에 실패하였습니다.");
	location.href=("/notice");
</script>
	
</c:if>
</c:if>
<c:if test="${not empty comment }">
<c:if test="${comment }">
<script>
	alert("댓글이 ${resultType}되었습니다.");
	location.href=('/noticeSearch?id=${id }');
</script>
</c:if>
<c:if test="${!comment }">
<script>
	alert("데이터가 처리에 실패하였습니다.");
	location.href=('/noticeSearch?id=${id }');
</script>
</c:if>
</c:if>
<body>

</body>
</html>