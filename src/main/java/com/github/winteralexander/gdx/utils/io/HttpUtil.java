package com.github.winteralexander.gdx.utils.io;


import com.sun.net.httpserver.HttpExchange;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import static com.github.winteralexander.gdx.utils.Validation.ensureNotNull;

/**
 * Utility class to handle http requests
 * <p>
 * Created on 2018-03-11.
 *
 * @author Alexander Winter
 */
public class HttpUtil {
	private HttpUtil() {}

	public static void sendResponse(HttpExchange exchange, int code, String response) throws IOException {
		byte[] bytes = response.getBytes();
		exchange.sendResponseHeaders(code, bytes.length);
		OutputStream os = exchange.getResponseBody();
		os.write(bytes);
		os.close();
	}

	public static String get(String url, Object... params) throws IOException {
		if(params.length % 2 == 1)
			throw new IllegalArgumentException("Http params array must contain an even amount of elements");

		StringBuilder urlWithParams = new StringBuilder(url);

		for(int i = 0; i < params.length; i += 2) {
			urlWithParams.append(i == 0 ? '?' : '&');
			urlWithParams.append(URLEncoder.encode(params[i].toString(), "UTF-8"));
			urlWithParams.append('=');
			urlWithParams.append(URLEncoder.encode(params[i + 1].toString(), "UTF-8"));
		}

		return get(urlWithParams.toString());
	}

	public static String get(String url, Map<String, String> parameters) throws IOException {
		StringBuilder urlWithParams = new StringBuilder(url);

		int i = 0;
		for(Entry<String, String> entry : parameters.entrySet()) {
			urlWithParams.append(i == 0 ? '?' : '&');
			urlWithParams.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
			urlWithParams.append('=');
			urlWithParams.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
			i++;
		}

		return get(urlWithParams.toString());
	}

	public static String get(String url) throws IOException {
		ensureNotNull(url, "url");

		URL urlObj = new URL(url);

		HttpURLConnection connection = (HttpURLConnection)urlObj.openConnection();
		connection.setRequestMethod("GET");
		connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

		connection.setUseCaches(false);
		connection.setDoOutput(true);

		int responseCode = connection.getResponseCode();

		// read response
		InputStream input = responseCode < 300 ? connection.getInputStream() : connection.getErrorStream();
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(input));

		StringBuilder responseBuilder = new StringBuilder();

		String line;
		while((line = bufferedReader.readLine()) != null)
			responseBuilder.append(line).append("\n");
		bufferedReader.close();

		if(responseCode != 200)
			throw new HttpRequestFailedException(responseCode, responseBuilder.toString());

		return responseBuilder.toString();
	}

	public static String post(String url, Object... params) throws IOException {
		if(params.length % 2 == 1)
			throw new IllegalArgumentException("Http params array must contain an even amount of elements");

		Map<String, String> map = new HashMap<>();

		for(int i = 0; i < params.length; i += 2)
			map.put(params[i].toString(), params[i + 1].toString());

		return post(url, map);
	}

	public static String post(String url,
	                          Map<String, String> parameters) throws IOException {
		return post(url, parameters, null, HttpContentType.X_WWW_FORM_URLENCODED);
	}

	public static String post(String url,
	                          Map<String, String> parameters,
	                          Map<String, String> requestProperties,
	                          HttpContentType contentType) throws IOException {
		ensureNotNull(url, "url");
		ensureNotNull(parameters, "parameters");

		StringBuilder paramsBuilder = new StringBuilder();

		if(contentType == HttpContentType.JSON) {
			paramsBuilder.append("{");
			for(Entry<String, String> entry : parameters.entrySet()) {
				paramsBuilder.append("\"")
						.append(entry.getKey())
						.append("\":");

				// don't quote json or already quoted content
				if(entry.getValue().startsWith("{") || entry.getValue().startsWith("\""))
					paramsBuilder.append(entry.getValue()).append(",");
				else
					paramsBuilder.append("\"").append(entry.getValue()).append("\",");
			}

			if(paramsBuilder.length() > 0)
				paramsBuilder.deleteCharAt(paramsBuilder.length() - 1);

			paramsBuilder.append("}");
		} else if(contentType == HttpContentType.X_WWW_FORM_URLENCODED) {
			for(Entry<String, String> entry : parameters.entrySet())
				paramsBuilder.append(URLEncoder.encode(entry.getKey(), "UTF-8"))
						.append("=")
						.append(URLEncoder.encode(entry.getValue(), "UTF-8"))
						.append("&");

			if(paramsBuilder.length() > 0)
				paramsBuilder.deleteCharAt(paramsBuilder.length() - 1);
		} else
			throw new UnsupportedOperationException("Content type " + contentType + " is not supported");

		String params = paramsBuilder.toString();

		URL urlObj = new URL(url);

		HttpURLConnection connection = (HttpURLConnection)urlObj.openConnection();
		connection.setRequestMethod("POST");
		connection.setRequestProperty("Content-Type", contentType.getValue());
		connection.setRequestProperty("Content-Length", String.valueOf(params.getBytes().length));

		if(requestProperties != null)
			for(Entry<String, String> property : requestProperties.entrySet())
				connection.setRequestProperty(property.getKey(), property.getValue());

		connection.setUseCaches(false);
		connection.setDoOutput(true);

		// Send request
		DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
		wr.writeBytes(params);
		wr.close();

		int responseCode = connection.getResponseCode();

		// read response
		InputStream input = responseCode < 300 ? connection.getInputStream() : connection.getErrorStream();
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(input));

		StringBuilder responseBuilder = new StringBuilder();

		String line;
		while((line = bufferedReader.readLine()) != null)
			responseBuilder.append(line).append("\n");
		bufferedReader.close();

		if(responseCode != 200)
			throw new HttpRequestFailedException(responseCode, responseBuilder.toString());

		return responseBuilder.toString();
	}
}
