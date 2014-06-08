package com.junqing.qa.selenium;

public class SeleniumDriverFixture extends com.junqing.qa.selenium.xebium.SeleniumDriverFixture {

	/**
	 * <p>
	 * <code>
	 * | start browser | <i>firefox</i> | on url | <i>http://localhost</i> |
	 * </code>
	 * </p>
	 * 
	 * @param browser
	 * @param browserUrl
	 * @throws MalformedURLException
	 */
	public void startBrowserOnUrl(final String browser, final String browserUrl) {
			String os = System.getProperty("os.name");
			if (os.contains("Windows")) {
				super.setBrowser(browser);
			} else {
				super.setBrowser("htmlUnit+js");
				
			}
		
			startDriverOnUrl(super.defaultWebDriverInstance(), browserUrl);
	}
	
	/**
	 * set wait time
	 * @param waitTime
	 */
	public void setWaitTime(String waitTime){
		try {
			Thread.sleep(Integer.parseInt(waitTime)*1000);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
