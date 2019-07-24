<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="include/jstl.jsp"%>
<!doctype html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>책 소개</title>

<script type="text/javascript" src="https://code.jquery.com/jquery-3.2.1.min.js"></script>
<link rel="stylesheet" href="//unpkg.com/bootstrap/dist/css/bootstrap.min.css">
<link rel="stylesheet" type="text/css" href="/css/style.css">
<style type="text/css">
body.loading .container .media {
	visibility: hidden;
}
</style>
</head>
<body class="loading">
	<div class="main-top-layout">
		<div class="container">
			<%@ include file="include/gnb.jsp"%>
			<h1 class="book-title">읽어오는 중입니다.</h1>
		</div>
	</div>
	<div class="container mt-3">
		<div class="media">
			<div>
				<div class="book-img mb-1"></div>
			</div>
			<div class="media-body">
				<p>
					카테고리 : <span class="book-category"></span>
				</p>
				<p>
					저자 : <span class="book-authors"></span>
				</p>
				<p>
					번역 : <span class="book-translators"></span>
				</p>
				<p>
					출판사 : <span class="book-publisher"></span>
				</p>
				<p>
					ISBN : <span class="book-isbn"></span>
				</p>
				<p>
					바코드 : <span class="book-barcode"></span>
				</p>
				<p>
					판매가 (정상가) : <span class="book-price"></span>
				</p>
				<p>
					상태 : <span class="book-status"></span>
				</p>
				<h5 class="mt-0">책소개</h5>
				<p class="book-contents"></p>
			</div>
		</div>
	</div>

	<script type="text/javascript">
	

		function init() {
			$
					.ajax({
						url : "/ajax/getBookinfo/isbn/${param.isbn}",
						success : function(res) {
							if (res.meta.total_count > 0) {
								var doc = res.documents[0];
								var authors = "", trans = "", thumbnail = "", isbn = "";
								$(doc.authors).each(function() {
									authors += (this + " ")
								});
								$(doc.translators).each(function() {
									trans += (this + " ")
								});
								if (doc.thumbnail) {
									thumbnail = '<img class="mr-3 img-thumbnail" width="100" src="'+doc.thumbnail+'" alt="책 표지 ">'
								}
								$(".book-img").html(thumbnail);
								$(".book-title").text(doc.title);
								$(".book-category").text(doc.category);
								$(".book-authors").text(doc.authors);
								$(".book-translators").text(doc.trans);
								$(".book-publisher").text(doc.publisher);
								$(".book-isbn").text(doc.isbn);
								var arr_isbn = doc.isbn.split(" ");
								if(arr_isbn.length>1){
									isbn = arr_isbn[1];
								}else{
									isbn = arr_isbn[0];
								}
								$(".book-barcode").text(doc.barcoed);
								$(".book-price").text(
										doc.sale_price + "원 ( " + doc.price
												+ "원 )");
								$(".book-status").text(doc.status);
								$(".book-contents").text(doc.contents);

								$("body").removeClass("loading");
							} else {
								alert("정상적인 접근이 아닙니다.");
								location.replace("/");
							}
							console.log(res);
						},
						error : function(res) {
							console.log(res);
							alert("잠시후 다시 시도해주세요.");
							location.replace("/");
						},
						conplete : function() {

						}
					});
		}

		$(document).ready(function() {
			init();
			$(".btn-bookmark.add").on("click", btnBookmark);
			$(".btn-bookmark.delete").on("click", btnUnbookmark);
		});
	</script>
</body>
</html>