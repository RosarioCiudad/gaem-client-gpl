/*
 * Copyright (c) 2016 Municipalidad de Rosario, Coop. de Trabajo Tecso Ltda.
 *
 * This file is part of GAEM.
 *
 * GAEM is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License.
 *
 * GAEM is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with GAEM.  If not, see <http://www.gnu.org/licenses/>.
 */

package coop.tecso.daa.domain.web;

import android.util.Log;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.security.KeyStore;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * Models a a Web Service call.
 */
public final class WebService {

	/**
	 * The webServiceUrl should be the name of the service 
	 * you are going to be using.
	 */
	public WebService(String webServiceUrl) {        
		this.httpClient = createHttpClient();
		this.localContext = new BasicHttpContext();
		this.webServiceUrl = webServiceUrl;
	}

	/**
	 * Makes a HttpGet/WebGet on a Web service.
	 */
	public Reply<String> webGet(String methodName, Map<String, String> params) {
		// Build URL
		StringBuffer getUrl = new StringBuffer(webServiceUrl + methodName);
		int i = 0;
		for (Map.Entry<String, String> param : params.entrySet()) {
			if(i++ == 0) {
				getUrl.append("?");
			} else {
				getUrl.append("&");
			}
			try {
				getUrl.append(param.getKey()+"="+URLEncoder.encode(param.getValue(),"UTF-8"));
			} catch (UnsupportedEncodingException e) {
				Log.e(LOG_TAG, "**ERROR**", e);
			} 
		}

		// 
		httpRequest = new HttpGet(getUrl.toString());
		Log.i(LOG_TAG, getUrl.toString());
		try {
			response = httpClient.execute(httpRequest);
		} catch (Exception e) {
			Log.e(LOG_TAG,"webGet: **ERROR**", e);
		}

		try {
			strResponse = EntityUtils.toString(response.getEntity());
		} catch (IOException e) {
			strResponse = null;
			Log.e(LOG_TAG, "**ERROR**", e);
		}

		return toReply();
	}

	/**
	 * Makes a HttpPost on a Web service.
	 */
	public Reply<String> webPost(String methodName, Map<String,String> params) {
		List<NameValuePair> postParameters = new ArrayList<NameValuePair>();
		for (Map.Entry<String, String> param : params.entrySet()) {
			postParameters.add(new BasicNameValuePair(param.getKey(), param.getValue()));
		}

		httpClient.getParams().setParameter(ClientPNames.COOKIE_POLICY, CookiePolicy.RFC_2109);
		httpRequest = new HttpPost(webServiceUrl + methodName);

		// Encode data
		try {
			((HttpPost)httpRequest).setEntity(new UrlEncodedFormEntity(postParameters));
		} catch (UnsupportedEncodingException e) {
			Log.e(LOG_TAG,"**ERROR**", e);
		}
		// Set header
		httpRequest.setHeader("Accept", 
				"text/html,application/xml,application/xhtml+xml,text/html;q=0.9,text/plain;q=0.8,image/png,*/*;q=0.5");
		// Set the content type
		httpRequest.setHeader("Content-Type", "application/x-www-form-urlencoded");

		Log.d(LOG_TAG, webServiceUrl + methodName);

		// Execute the call
		try {
			response = httpClient.execute(httpRequest, localContext); 
		} catch (Exception e) {
			Log.e(LOG_TAG,"**ERROR**", e);
		}

		try {
			strResponse = EntityUtils.toString(response.getEntity());
		} catch (IOException e) {
			strResponse = null;
			Log.e(LOG_TAG, "**ERROR**", e);
		}

		return toReply();
	}

	/**
	 * 
	 */
	public void abort() {
		try {
			if (httpClient != null) {
				Log.i(LOG_TAG, "Aborting web service call");                
				httpRequest.abort();
				httpClient.getConnectionManager().shutdown();
			}
		} catch (Exception e) {
			Log.e(LOG_TAG, "Failed to abort connection: ",  e);
		}
	}   

	/**
	 * 
	 * @return
	 */
	private Reply<String> toReply(){
		try {
			JsonParser parser = new JsonParser();
			JsonObject obj = parser.parse(strResponse).getAsJsonObject();
			return  new Reply<String>(
					obj.get("code").getAsInt(),
					obj.get("message").getAsString(),
					obj.get("result").toString());
		} catch (Exception e) {
			Log.e(LOG_TAG, "toReply: **ERROR**", e);
			return  new Reply<String>(503, "Internal error", strResponse);
		}
	}



	public InputStream getHttpStream(String urlString) throws IOException {
		InputStream in = null;
		int response = -1;

		URL url = new URL(urlString);
		URLConnection conn = url.openConnection();

		if (!(conn instanceof HttpURLConnection))
			throw new IOException("Not an HTTP connection");

		try{
			HttpURLConnection httpConn = (HttpURLConnection) conn;
			httpConn.setAllowUserInteraction(false);
			httpConn.setInstanceFollowRedirects(true);
			httpConn.setRequestMethod("GET");
			httpConn.connect(); 

			response = httpConn.getResponseCode();                 

			if (response == HttpURLConnection.HTTP_OK) {
				in = httpConn.getInputStream();
			}
		} catch (Exception e) {
			throw new IOException("Connection error");
		}  
		return in;
	}

	public void clearCookies() {
		httpClient.getCookieStore().clear();
	}

	// Implementation helpers
	private static final String LOG_TAG = WebService.class.getName();

	private String webServiceUrl;
	private String strResponse; 

	private DefaultHttpClient httpClient;
	private HttpContext localContext;
	private HttpResponse response;
	private HttpRequestBase httpRequest;

	private class MRSSLSocketFactory extends SSLSocketFactory {
		private SSLContext sslContext = SSLContext.getInstance("TLS");

		public MRSSLSocketFactory(KeyStore truststore) throws Exception {
			super(truststore);

			TrustManager[] trustAllCerts = new TrustManager[] {new X509TrustManager() {
				public java.security.cert.X509Certificate[] getAcceptedIssuers() {
					return null;
				}
				public void checkClientTrusted(X509Certificate[] certs, String authType) {
				}
				public void checkServerTrusted(X509Certificate[] certs, String authType) {
				}
			}};
			sslContext.init(null, trustAllCerts, null);
		}
		@Override
		public Socket createSocket(Socket socket, String host, int port, boolean autoClose)
				throws IOException, UnknownHostException {
			return sslContext.getSocketFactory().createSocket(socket, host, port, autoClose);
		}
		@Override
		public Socket createSocket() throws IOException {
			return sslContext.getSocketFactory().createSocket();
		}
	}

	private DefaultHttpClient createHttpClient() {
		try {
			KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
			trustStore.load(null, null);

			SSLSocketFactory sf = new MRSSLSocketFactory(trustStore);
			sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

			HttpParams params = new BasicHttpParams();
			// Set up the http version and charset.
			HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
			HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);
			// Set up the timeouts for the connection.
			HttpConnectionParams.setConnectionTimeout(params, 30000);
			HttpConnectionParams.setSoTimeout(params, 30000);

			SchemeRegistry registry = new SchemeRegistry();
			registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
			registry.register(new Scheme("https", sf, 443));

			ClientConnectionManager ccm = new ThreadSafeClientConnManager(params, registry);

			return new DefaultHttpClient(ccm, params);
		} catch (Exception e) {
			Log.e(LOG_TAG, "ERRROR: createHttpClient", e);
			return new DefaultHttpClient();
		}
	}
}