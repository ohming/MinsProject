package com.mins.application.service;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mins.application.enums.EnumBookCategory;
import com.mins.application.enums.EnumBookTarget;
import com.mins.application.utils.Utils;

@Service
public class ApiService {

	private static final Logger logger = LoggerFactory.getLogger(ApiService.class);

	private static final String API_REST_API_KEY = "dbcb995a0b8dba849daf76a45e0901a8";
	private static final String API_BOOK_URL = "https://dapi.kakao.com/v3/search/book";
	private static final String API_NAVER_REST_API_KEY = "xO9OXuEQqlDR12hxZXaW";
	private static final String API_NAVER_REST_API_SECRET = "DK1rhl9t1j";
	private static final String API_NVAER_URL = "https://openapi.naver.com/v1/search/book.json";

	public Map<String, Object> searchBooks(String searchWord, String target, String category, int page) {
		//
		final String URL = API_BOOK_URL + "?target=" + target + "&target=" + target + "&category=" + category + "&page="
				+ page;
		Map<String, String> headers = new HashMap<>();
		headers.put("Authorization", "KakaoAK " + API_REST_API_KEY);
		Map<String, String> params = new HashMap<>();
		params.put("query", searchWord);
		String jsonString = null;
		Map<String, Object> resultData = null;
		try {
			jsonString = Utils.getHttpPOST2String(URL, headers, params, false);
			ObjectMapper mapper = new ObjectMapper();
			resultData = mapper.readValue(jsonString, new TypeReference<Map<String, Object>>() {
			});

			logger.debug(URL + " - get API Info : " + jsonString);
			// resultData = JsonUtils.readJsonToStringObjectUnparse(jsonString);

		} catch (Exception e) {
			logger.info(URL + " - get API Exception : " + jsonString);
			
		}
		return resultData;
	}
	
	public Map<String, Object> searchBooksNaver(String searchWord, String target, String category, int page) {
		String jsonString = null;
		Map<String, Object> resultData = null;
		//NVAER 검색 시작
		String query = API_NVAER_URL + "?query=" + searchWord + "&display=10&start="+ page;
        try {
            String text = URLEncoder.encode("안녕하세요. 오늘 기분은 어떻습니까?", "UTF-8");
            URL url = new URL(query);
            HttpURLConnection con = (HttpURLConnection)url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("X-Naver-Client-Id", API_NAVER_REST_API_KEY);
            con.setRequestProperty("X-Naver-Client-Secret", API_NAVER_REST_API_SECRET);
            // post request
            String postParams = "source=ko&target=en&text=" + text;
            con.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.writeBytes(postParams);
            wr.flush();
            wr.close();
            int responseCode = con.getResponseCode();
            BufferedReader br;
            if(responseCode==200) { // 정상 호출
                br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            } else {  // 에러 발생
                br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
            }
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = br.readLine()) != null) {
                response.append(inputLine);
            }
            br.close();
            ObjectMapper mapper = new ObjectMapper();
			resultData = mapper.readValue(response.toString(), new TypeReference<Map<String, Object>>() {
			});
        } catch (Exception e) {
            System.out.println(e);
        }
		
		return resultData;
	}

	/**
	 * 책 정보 가져오기
	 * 
	 * 데이터 참조 :
	 * https://developers.kakao.com/docs/restapi/search#%EC%B1%85-%EA%B2%80%EC%83%89
	 * 
	 * @param ISBN
	 * @return JSON -> Map<String,Object>
	 */
	public Map<String, Object> getBookByISBN(String ISBN) {
		Map<String, Object> book = null;
		Map<String, Object> json = this.searchBooks(ISBN, EnumBookTarget.전체.getCode(), EnumBookCategory.전체.getCode(),
				1);
		int cnt = (Integer) ((Map) json.get("meta")).get("total_count");
		if (cnt > 0) {
			book = (Map) ((List) json.get("documents")).get(0);
		}
		return book;
	}

}
