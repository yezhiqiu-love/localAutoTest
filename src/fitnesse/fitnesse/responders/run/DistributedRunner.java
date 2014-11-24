package fitnesse.responders.run;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import com.junqing.qa.rest.RestDriver;

import fitnesse.wiki.WikiPage;

public class DistributedRunner {
	private static final Logger LOG = Logger.getLogger(DistributedRunner.class
			.getName());

	public void configUrlAndDistributedRunner(List<WikiPage> pageList) {
		// 这里要配置别的服务器的ip地址
		List<String> ipAddressList = new ArrayList<String>();
		ipAddressList.add("192.168.1.111");
//		ipAddressList.add("192.168.1.107");
//		ipAddressList.add("192.168.1.108");
//		ipAddressList.add("192.168.1.109");

		//List<String> urlList = new ArrayList<>();
		Map<String,List<String>> urlMap = new HashMap<String,List<String>>();

		// 这里把每个运行案例的url给配置好。
		int ipIndex = 0;
		for (WikiPage wikipage : pageList) {

			String remoteUrl = "http://"
					+ ipAddressList.get(ipIndex)
					+ ":8001/"
					+ wikipage.toString().split(
							"fitnesse.wiki.fs.FileSystemPage at ./FitNesseRoot/")[1]
							.replace("/", ".") + "?test";
			ipIndex++;
			// 如果这里ip地址第一轮改完，那开始改第二轮。
			if (ipIndex == ipAddressList.size())
				ipIndex = 0;
            System.out.println(remoteUrl);
			LOG.info(remoteUrl);
			if(urlMap.containsKey(ipAddressList.get(ipIndex))){
				List<String> urlList = urlMap.get(ipAddressList.get(ipIndex));
				urlList.add(remoteUrl);
				urlMap.put(ipAddressList.get(ipIndex), urlList);
				
			} else {
				List<String> urlList = new ArrayList<>();
				urlList.add(remoteUrl);
				urlMap.put(ipAddressList.get(ipIndex), urlList);
			}

		}
		// 从这里我们使用线程池把这些请求并发的发出去，注意线程池里线程数是ipAddressList的大小。
		// 这样如果线程池里如果所有测试案例都在执行的话，新的测试案例会等待有某个旧执行完毕再去执行。
		//此处传入的参数是ip和urlMap,每一个线程都唯一指定一个ip,然后此线程把urlMap里和此ip想匹配的list找到，然后此线程处理此list里面的url.
	//	ExecutorService service = Executors.newCachedThreadPool();
		for (int i = 0; i < ipAddressList.size(); i++) {
			new TestRunn(ipAddressList.get(i),urlMap);
		//	service.execute(new TestRunn(ipAddressList.get(i),urlMap));
			
			
		}
	//	service.shutdown();

	}

	/**
	 * 此线程类用来去发送执行fitnesse测试案例的请求。
	 * 
	 */
//	implements Runnable
	class TestRunn  {
		String _ip = null;
		Map<String,List<String>> _urlMap = new HashMap();

		public TestRunn(String ip,Map<String,List<String>> urlMap) {
			_ip = ip;
			_urlMap = urlMap;
			LOG.info(_ip);
		}
       
		//这里先迭代url map里面的内容，找到和此线程匹配的url List（每一个线程都唯一指定一个ip。）
        //找到后把此List里面的url都用Rest 的get api发送出去。
		public void run() {
			List<String> urlList = _urlMap.get(_ip);
			for(String url:urlList){
			RestDriver rest = new RestDriver();
			rest.setUrl(url);
			try {
				rest.get();
				//System.out.println(rest.getResponse());
			} catch (Exception e) {
				LOG.info(this.toString() + e.getMessage());
			}
			}
		}
	}
}
