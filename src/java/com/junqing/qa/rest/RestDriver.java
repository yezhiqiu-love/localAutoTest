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
import org.codehaus.jackson.map.ObjectMapper;

public class RestDriver {
	HttpClient client = HttpClientBuilder.create().build();
	
	private String _uri;
	
	private String _content = "";
	
	private String _contentType = "";
	
	private HttpEntity entity;
	
	public void setUrl(String uri){
		_uri =uri;
		
	}
	
	public void setContent(String content){
		
	}
	
	public void setContentType(String contentType){
		
	}
	
	public void get() throws ClientProtocolException, IOException {
		HttpGet httpGet = new HttpGet(_uri);
		HttpResponse response = client.execute(httpGet);
		entity = response.getEntity();
		InputStreamReader json = new InputStreamReader(entity.getContent(),
				Consts.UTF_8);
	}
	
	public void post() throws ClientProtocolException, IOException{
		HttpPost httppost = new HttpPost(_uri);
		httppost.addHeader("ContentType", _contentType);
		httppost.setEntity(new StringEntity(_content));
	
		HttpResponse response = client.execute(httppost);
		entity = response.getEntity();
	}
	
	public String getResponse() throws IllegalStateException, ClientProtocolException, IOException{
		InputStreamReader json = new InputStreamReader(entity.getContent(),
				Consts.UTF_8);
		String response = IOUtils.toString(json);
		return response;
	}
	
	public String getValue(String key) throws IOException {
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
			ObjectMapper mapper = new ObjectMapper();
			Object jsonObj = mapper.readValue(getResponse(), Object.class);
			object = PropertyUtils.getProperty(jsonObj, key.trim());

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
