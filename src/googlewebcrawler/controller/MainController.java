package googlewebcrawler.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.Connection;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.UnsupportedMimeTypeException;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import googlewebcrawler.util.Constants;
import googlewebcrawler.util.FileUtil;
import googlewebcrawler.util.JsonUtil;
import googlewebcrawler.util.Util;

/**
 * The class MainController is used to manage request response, extracting links
 * from json, download html page from the links and extract paragraphs from the
 * downloaded html pages
 * 
 * @author Junaid Sarfraz
 */
public class MainController {

	/**
	 * The method makeGoogleCustomeSearchEngineRequest() is use to make a
	 * request to google custom search engine with the given keyword
	 * 
	 * @param keyWord
	 *            to be searched
	 * @param start
	 *            page index
	 * @return json in response
	 */
	private String makeGoogleCustomeSearchEngineRequest(String keyWord, Integer start) {

		String apiKey = Constants.CSE_API_KEY;
		String cxId = Constants.CSE_ID;

		String urlToSearch = Constants.CSE_BASE_URL + "?key=" + apiKey + "&cx=" + cxId + "&alt=json" + "&q=" + keyWord.replaceAll(" ", "%20")
				+ "&start=" + start;

		String line, json = "";

		try {
			URL url = new URL(urlToSearch);

			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept", "application/json");
			BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

			while ((line = br.readLine()) != null) {
				json += line;
			}

			conn.disconnect();
		} catch (Exception e) {

		}

		return json;
	}

	/**
	 * The method extractLinks() is use to extract links from the json
	 * 
	 * @param json
	 * @return links
	 */
	private List<String> extractLinks(String json) {
		String[] items = JsonUtil.getJsonStringArrayFromJsonString(json, "items");

		List<String> links = new ArrayList<>();

		for (String item : items) {
			// item
			String link = JsonUtil.getStringFieldValueFromJson(item, "link");
			links.add(link);
		}

		return links;
	}
	
	private String readAllBytes(String filePath) {
		String content = "";
		try {
			content = new String(Files.readAllBytes(Paths.get(filePath)));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return content;
	}

	/**
	 * The method makeReportUsingCSE() is use to search the keyword using google
	 * custom search engine, get the html pages, parse them and extract
	 * information from the pages.
	 * 
	 * @param keyToSearch
	 *            use to query the google custom search engine
	 * @param keyword
	 *            comma separated keywords use to search the html pages
	 * @return Success if report successfully generated else return error
	 *         message
	 */
	public String makeReportUsingCSE(String keyToSearch, String keyword) {

		List<String> keywords = new ArrayList<>();

		if (Util.isNotNullAndEmpty(keyword)) {
			keywords.addAll(Arrays.asList(keyword.split(",")));
		}

		try {

			String myOutput = makeGoogleCustomeSearchEngineRequest(keyToSearch, 1);

//			String myOutput = readAllBytes("json.txt");
			
			List<String> links = extractLinks(myOutput);

//			String queries = JsonUtil.getJsonValueFromJsonString(myOutput, "queries");
//
//			if (JsonUtil.containsKey(queries, "nextPage")) {
//				myOutput = makeGoogleCustomeSearchEngineRequest(keyToSearch, 11);
//
//				links.addAll(extractLinks(myOutput));
//			}

			Map<String, String> strs = new LinkedHashMap();

			for (String link : links) {

				try {
					// Jsoup Request
					Connection.Response response = Jsoup.connect(link).timeout(70000)
							.userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.1 (KHTML, like Gecko) Chrome/13.0.782.112 Safari/535.1")
							.method(Connection.Method.GET).execute();

					Document doc = response.parse();

					for (String word : keywords) {

						word = word.trim();

						// Case insensitive search
						Elements elems = doc.select("*:contains(" + word + ")");

						for (Element elem : elems) {
							strs.put(elem.select("*:contains(" + word + ")").last().text(), elem.select("*:contains(" + word + ")").last().text());
						}
					}
					
					strs.put(Constants.LINK_DETECTION_STRING + link, Constants.LINK_DETECTION_STRING + link);

				} catch (SocketTimeoutException e) {
					// ignore
					System.err.println("Unable to read " + link);
				} catch (UnsupportedMimeTypeException e) {
					// ignore
				} catch (HttpStatusException e) {
					// ignore
				}
			}

			List<String> texts = new ArrayList<String>(strs.keySet());

			Boolean wordDocCreated = FileUtil.createDocFile(texts, keyToSearch, keywords);

			if (Boolean.FALSE.equals(wordDocCreated)) {
				return "Unable to create the word document";
			}

		} catch (Exception e) {
			return "An unexcepted error has occurred"; // Error message
		}

		return Constants.SUCCESS;
	}

}
