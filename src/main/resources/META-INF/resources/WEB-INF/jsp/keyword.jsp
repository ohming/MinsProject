<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="include/jstl.jsp"%>
<!doctype html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>인기키워드</title>
<script type="text/javascript" src="https://code.jquery.com/jquery-3.2.1.min.js"></script>
<link rel="stylesheet" href="//unpkg.com/bootstrap/dist/css/bootstrap.min.css">
<link rel="stylesheet" type="text/css" href="/css/style.css">
</head>
<body>
	<div class="main-top-layout">
		<div class="container">
			<%@ include file="include/gnb.jsp"%>
		</div>
	</div>
	<div class="container mt-3">
		<div class="row">
			<div class="col list-bookmark">
				<h3>인기키워드</h3>
				<form action="./keyword" id="searchForm" name="searchForm" method="get">
				</form>
				<ul class="list-group">
					<c:forEach var="b" items="${keywords}">
					<li class="list-group-item"><dl>
								<dt>
									검색어: ${b.search_word}
								</dt>
								<dd>
									<span>검색수 : ${b.cnt}</span>
								</dd>
							</dl></li>
					</c:forEach>
				</ul>
			</div>
		</div>
		<div class="paging-layout center-block"></div>
	</div>
	<script type="text/javascript" src="/js/jquery.bootpag.min.js"></script>
</body>
</html>