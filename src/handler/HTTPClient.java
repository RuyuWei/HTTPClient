package handler;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.omg.CORBA.Request;

import constants.Constants;

public class HTTPClient {
	
	public static void DoGet(String url) throws Exception {
		// 创建httpclient对象
		CloseableHttpClient httpClient = HttpClients.createDefault();
		// 创建Http get请求
		HttpGet httpGetRequeset = new HttpGet(url);
		
		CloseableHttpResponse httpGetResponse = null;
		try {
			// 执行请求
			httpGetResponse = httpClient.execute(httpGetRequeset);
			if (httpGetResponse.getStatusLine().getStatusCode() == 200) {
				System.out.println(httpGetResponse.toString());
				System.out.println("Response status line: " + httpGetResponse.getStatusLine() 
						+ "\nResponse protocol: " + httpGetResponse.getProtocolVersion() 
						+ "\nResponse status code: " + httpGetResponse.getStatusLine().getStatusCode());
				String responseContent = EntityUtils.toString(httpGetResponse.getEntity(), "UTF-8");
				System.out.println("Response content : " + responseContent);
			}
		}finally {
			if (httpGetResponse != null) {
				httpGetResponse.close();
			}
			httpClient.close();
		}
	}
	
	@SuppressWarnings({ "deprecation", "static-access" })
	public static void DoHTTPsGet(String url, boolean needCerts) throws Exception{
		
		SSLConnectionSocketFactory sslConnectionSocketFactory = null;
		if(needCerts) {
			InputStream keystore_inputStream = new FileInputStream(new File("D:/cert/client.p12"));
			InputStream truststore_inputStream = new FileInputStream(new File("D:/cert/tbb.jks"));
			
			KeyStore keyStore = KeyStore.getInstance("PKCS12");
			KeyStore trustStore = KeyStore.getInstance("JKS");
			keyStore.load(keystore_inputStream,"password".toCharArray());
			trustStore.load(truststore_inputStream,"password".toCharArray());
			
			if(keystore_inputStream != null) {
				keystore_inputStream.close();
			}
			if(truststore_inputStream != null) {
				truststore_inputStream.close();
			}
			 
			SSLContext sslContext = SSLContexts.custom()
					.loadTrustMaterial(trustStore).loadKeyMaterial(keyStore, "password".toCharArray()).build();
			sslConnectionSocketFactory = new SSLConnectionSocketFactory(sslContext, sslConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
		}else {
			SSLContext sslContext = SSLContexts.custom().loadTrustMaterial(null, new TrustStrategy() {
				
				public boolean isTrusted(X509Certificate[] chain, String authType)
						throws CertificateException {
					return true;
				}
			}).build();
			sslConnectionSocketFactory = new SSLConnectionSocketFactory(sslContext, new String[]{"TLSv1.2"}, null, sslConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
		}
		
		RequestConfig config = null;
		CloseableHttpClient httpClient = null;
		CloseableHttpResponse httpResponse = null;
		
		
		
	}
	public static void DoPost (String url) throws Exception {
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpPost httpPostRequest = new HttpPost(url);
		CloseableHttpResponse httpPostResponse = null;
		try {
			httpPostResponse = httpClient.execute(httpPostRequest);
			System.out.println("PostResponse: " + EntityUtils.toString(httpPostResponse.getEntity(), "UTF-8"));
		}finally {
			if(httpPostResponse != null) {
				httpPostResponse.close();
			}
			httpClient.close();
		}
	}
	
	public static void DoPut(String url) throws Exception {
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpPut httpPutRequest = new HttpPut(url);
		CloseableHttpResponse httpPutResponse = null;
		try {
			httpPutResponse = httpClient.execute(httpPutRequest);
			if(httpPutResponse.getStatusLine().getStatusCode() == 200) {
				System.out.println("PutResponse: " + EntityUtils.toString(httpPutResponse.getEntity(), "UTF-8"));
			}
		}finally {
			if (httpPutResponse != null) {
				httpPutResponse.close();
			}
			httpClient.close();
		}
	}
	
	public static void sendHttpRequest(String operation) throws Exception {
		switch(operation){
		case Constants.HTTP_GET:
			DoGet("http://httpbin.org/get");
			break;
		case Constants.HTTP_POST:
			DoPost("http://httpbin.org/post");
			break;
		case Constants.HTTP_PUT:
			DoPut("http://httpbin.org/put");
			break;
		default:
			System.out.println("Operation of httprequest this time is " + operation);
		}
	}
	
	public static void main(String[] args) throws Exception {
		sendHttpRequest("");
	}

}
