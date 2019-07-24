<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="include/jstl.jsp"%>
<!doctype html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>책검색</title>
<script type="text/javascript" src="https://code.jquery.com/jquery-3.2.1.min.js"></script>
<link rel="stylesheet" href="//unpkg.com/bootstrap/dist/css/bootstrap.min.css">
<link rel="stylesheet" type="text/css" href="/css/style.css">
</head>
<body>
	<div class="main-top-layout">
		<div class="container">
			<%@ include file="include/gnb.jsp"%>
			<h1>책 검색</h1>
			<div class="row">
				<div class="col-12">
					<form id="search_form" name="search_form" onsubmit="return submitKaKaoSearch();">
						<input type="hidden" name="page" value="1">
						<input type="hidden" id="api" name="api" value="kakao">
						<div class="form-row">
							<div class="col">
								<div class="form-group">
									<label for="select-category">카테고리 </label>
									<select id="select-category" class="form-control" name="category">
										<c:forEach var="c" items="${EnumCategory }">
											<option value="${c.code }">${c.desc}</option>
										</c:forEach>
									</select>
								</div>
							</div>
							<div class="col">
								<div class="form-group">
									<label for="select-target">검색 필드</label>
									<select class="form-control" name="target">
										<c:forEach var="t" items="${EnumTarget }">
											<option value="${t.code }">${t }</option>
										</c:forEach>
									</select>
								</div>
							</div>
							<div class="col">
								<div class="form-group">
									<label for="input-searchword">검색어</label>
									<input id="input-searchword" class="form-control" name="searchWord" placeholder="검색어를 입력하세요.">
								</div>
							</div>
							<div class="col">
								<div class="form-group">
									<label>&nbsp;</label>
									<div><button class="btn btn-primary" type="submit">검색</button></div>
								</div>
							</div>
						</div>
					</form>
				</div>
			</div>
		</div>
	</div>
	<div class="container">
		<hr>
		<h2>책 리스트</h2>
		<div class="row">
			<div class="col-12">
				<div id="books">
					<ul class="list-group">
					</ul>
					<div id="resultMessage" class="alert" role="alert" style="display: none;">검색된 책이 없습니다.</div>
				</div>
			</div>
		</div>
		<hr>
	</div>
	<script type="text/javascript">
		function submitKaKaoSearch(page) {
			var pg = page ? page : 1;
			var frm = document.search_form;
			frm.page.value = pg;
			if (frm.searchWord.value == "") {
				alert("검색어를 입력하세요. ");
			} else {
				$.ajax({
							url : "/ajax/searchBooks",
							data : $(frm).serialize(),
							beforeSend: function(){
								$(frm).find("button[type=submit]").prop("disabled", true);
								$(frm).find("button[type=submit]").text("Searching for ...");
								console.log($(frm).find("button[type=submit]"));
							},
							success : function(res) {
								if (res.meta.total_count < 1) {
									$("#books > ul").html("");
									$("#books > #resultMessage").show();
								} else {
									$("#books > #resultMessage").hide();
									var html = "";
									$(res.documents)
											.each(
													function(idx) {
														var authors = "", trans = "", thumbnail = "", isbn = "";
														$(this.authors)
																.each(
																		function() {
																			authors += (this + " ")
																		});
														$(this.translators)
																.each(
																		function() {
																			trans += (this + " ")
																		});
														if (this.thumbnail) {
															thumbnail = "<img src='"+this.thumbnail+"' width='100'>";
														}
														var arr_isbn = this.isbn.split(" ");
														if(arr_isbn.length>1){
															isbn = arr_isbn[1];
														}else{
															isbn = arr_isbn[0];
														}

														html += "<li class='list-group-item'>";
														html += "<dl><dt><a href='./detail?isbn="
																+ isbn
																+ "'>"
																+ this.title
																+ " | "
																+ this.publisher
																+ "</a>"
																+"</dt>";
														html += "<dd><div class='left'>"
																+ thumbnail
																+ "</div><div class='right'>저자: "
																+ authors
																+ "<br> 번역자: "
																+ trans
																+ "<br> 상태: "
																+ this.status
																+ "</div></dd></dl></li>";
													});
									if (!res.meta.is_end) {
										html += "<li><button class='btn btn-primary btn-lg btn-block' onclick='submitKaKaoSearch("
												+ (pg + 1)
												+ "); $(this).parent().remove();'>더보기 </button></li>";
									}
									if (pg > 1) {
										$("#books > ul").append(html);
									} else {
										$("#books > ul").html(html);
									}
								}
								console.log(res);
							},
							error : function(res) {

								submitNaverSearch(1);
							},
							complete : function() {
								$(frm).find("button[type=submit]").prop("disabled", false);
								$(frm).find("button[type=submit]").text("검색");
							}
						});
			}

			return false;
		}
		function submitNaverSearch(page) {
			var pg = page ? page : 1;
			var frm = document.search_form;
			frm.page.value = pg;
			if (frm.searchWord.value == "") {
				alert("검색어를 입력하세요. ");
			} else {
				//NAVER
				$("#api").val("naver");
				$.ajax({
					url : "/ajax/searchBooks",
					data : $(frm).serialize(),
					beforeSend: function(){
						$(frm).find("button[type=submit]").prop("disabled", true);
						$(frm).find("button[type=submit]").text("Searching for ...");
						console.log($(frm).find("button[type=submit]"));
					},
					success : function(res) {
						if (res.total_count < 1) {
							$("#books > ul").html("");
							$("#books > #resultMessage").show();
						} else {
							$("#books > #resultMessage").hide();
							var html = "";
							$(res.items)
									.each(
											function(idx) {
												var authors = "", trans = "", thumbnail = "", isbn = "";

												html += "<li class='list-group-item'>";
												html += "<dl><dt><a href='./detail?isbn="
														+ isbn
														+ "'>"
														+ this.title
														+ " | "
														+ this.publisher
														+ "</a>"
														+"</dt>";
												html += "<dd><div class='left'>"
														+ this.image
														+ "</div><div class='right'>저자: "
														+ this.author
														+ "<br> 번역자: "
														+ trans
														+ "<br> 상태: "
														+ this.status
														+ "</div></dd></dl></li>";
											});
							if (pg > 1) {
								$("#books > ul").append(html);
							} else {
								$("#books > ul").html(html);
							}
						}
						console.log(res);
					},
					error : function(res) {
						console.log(res);
						alert(res);
					},
					complete : function() {
						$(frm).find("button[type=submit]").prop("disabled", false);
						$(frm).find("button[type=submit]").text("검색");
					}
				});
			}

			return false;
		}


	</script>
</body>
</html>