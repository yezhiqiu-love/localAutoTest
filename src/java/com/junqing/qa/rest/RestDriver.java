package com.junqing.qa.rest;

import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;

import org.apache.commons.beanutils.NestedNullException;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.io.IOUtils;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;

public class RestDriver {
	HttpClient client = HttpClientBuilder.create().build();
	
	private String _uri;
	
	private String _content = "";
	
	private String _contentType = "";
	
	public void setUrl(String uri){
		_uri =uri;
		
	}
	
	public void setContent(String content){
		
	}
	
	public void setContentType(String contentType){
		
	}
	
	public HttpEntity get() throws ClientProtocolException, IOException {
		HttpGet httpGet = new HttpGet(_uri);
		HttpResponse response = client.execute(httpGet);
		HttpEntity entity = response.getEntity();
		return entity;
	}
	
	public HttpEntity post() throws ClientProtocolException, IOException{
		HttpPost httppost = new HttpPost(_uri);
		httppost.addHeader("ContentType", _contentType);
		httppost.setEntity(new StringEntity(_content));
	
		HttpResponse response = client.execute(httppost);
		HttpEntity entity = response.getEntity();
		return entity;
	}
	
	public String getResponse() throws IllegalStateException, ClientProtocolException, IOException{
		InputStreamReader json = new InputStreamReader(post().getContent(),
				Consts.UTF_8);
		String response = IOUtils.toString(json);
		return response;
	}
	
	public String getValue(String key) throws IOException {
		/*
		 * 这一段代码是用来给查询Json里数组的key加上小括号的。因为要查询数组，
		 * key要这样写(array)[0],PropertyUtils 只认识这样的key.
		 */
		String[] keys = key.split("\\.");
		for (int i = 0; i < keys.length; i++) {
			if (keys[i].contains("[")) {
				String keyWord = keys[i].split("\\[")[0];
				String NewkeyWord = "(" + keyWord + ")";
				key = key.replace(keyWord + "[", NewkeyWord + "[");
			}
		}
		
		Object object = null;
		
		try {

			object = PropertyUtils.getProperty(getResponse(), key.trim());

		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (NestedNullException e) {
			e.printStackTrace();
		}
		return String.valueOf(object);
		
	}
	
	

	
}
