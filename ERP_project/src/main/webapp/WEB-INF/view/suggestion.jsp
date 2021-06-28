<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>    
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html style="width: 100%;height: 100%;">
<head>
<meta charset="UTF-8">
    <title>Document</title>
    <style>
        table{border-collapse: collapse; text-align: center; margin: 50px 50px 0 0; width: 50%;}
        .temp-box{
            width: 100%;
            height: 100%;
            font-size: 20px;
            text-align: center;
            border: 1px solid #dee3eb;
        }
        .main-container{
            width: 1080px;
            margin: auto;            
            grid-template-columns: repeat(3, 1fr);
            grid-template-rows: repeat(24, 40px);
        }

    </style>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.1/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-+0n0xVW2eSR5OomGNYDnhzAbDsOXxcvSN1TPprVMTNDbiYZCxYbOOl7+AMvyTG2x" crossorigin="anonymous">
    <link rel="canonical" href="https://getbootstrap.kr/docs/5.0/content/tables/">
    <script src="https://code.jquery.com/jquery-3.5.1.min.js" type="text/javascript"></script> 

    <script src="/static/bootstrap/assets/dist/js/bootstrap.bundle.min.js"></script>
</head>

<body>
	<c:if test="${empl.manager=='권한' }"> 
		<tiles:insertAttribute name="root_side" />
	</c:if>
	<c:if test="${empl.manager=='비권한' }">
		<tiles:insertAttribute name="empl_side" />
	</c:if>
	<section style="float: right; width: 100%; height: 100%; top:100px">
	<div style="width: 80%; margin:40px auto;">
	<div style="text-align: center;">
   		<H2>건의게시판</H2>
	</div>
	<table class="table table-sm table-hover"  >
    <thead class="table-light">
        <tr>
        
            <th scope="col">번호</th>
            <th scope="col">제목</th>
            <th scope="col">작성자</th>
            <th scope="col">날짜</th>
            <th scope="col">조회수</th>
      
        </tr>
    </thead>
    
    <tbody>
   
   	 <c:forEach var="l" items="${list.tablelist }">
    	<tr onclick="window.open('/suggestionContent?id=${l.id }')">
       	 	<td>${l.id }</td>
      		<td>${l.title }</td>
        	<td>${l.writer }</td>
        	<td>${l.writeDate }</td>
        	<td>${l.count }</td>
   		</tr>
  	 </c:forEach>
   
   
	</tbody>
</table>

<div class="border-dee3eb" style="text-align: center;">
<c:if test="${list.totPage<list.lastPage }">
	<c:forEach var="link" begin="${list.firstPage }" end="${list.lastPage }">
		<a href="/suggestion?page=${link }">[${link }]</a>
	</c:forEach>
</c:if>
	<c:forEach var="link" begin="${list.firstPage }" end="${list.lastPage }">
		<a href="/suggestion?page=${link }">[${link }]</a>
	</c:forEach>
<c:if test="${list.firstPage>1 && list.lastPage<totPage }">
	<a href="/suggestion?page=${list.firstPage-1 }">[이전]</a>
		<c:forEach var="link" begin="${list.firstPage }" end="${list.lastPage }">
			<a href="/suggestion?page=${link }">[${link}]</a>
		</c:forEach>
	<a href="/suggestion?page=${list.lastPage+1 }">[다음]</a>
</c:if>
<c:if test="${ list.firstPage>1 && list.lastPage>=list.totPage }">
	<a href="/suggestion?page=${list.firstPage-1 }">[이전]</a>
		<c:forEach var="link" begin="${list.firstPage }" end="${list.totPage}">
			<a href="/suggestion?page=${link }">[${link}]</a>
		</c:forEach>

</c:if>	

</div>
	<div class="temp-box" box-three></div>
	<div class="border-dee3eb" style="text-align: right;"> <button>작성</button></div>
</div>
</section>
<script type="text/javascript">

	$(".click").click(()=>{
		var plus=$(".click").val()+1;
		$(".click").val()=plus;
	})

</script>
</body>
</html>