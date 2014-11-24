package fitnesse.responders.run;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

public class CopyDirectory {
	
	public String copyDirectory(){
		//此处配置要拷贝的共享文件夹路径，有几个分机就要定义几个resFile
        File resFile1 = new File("//JAMES-340282BB1/files/testResults"); 
    //  File resFile2 = new File("//JAMES-340282AA2/files/testResults"); 
        File distFile = new File("D:/fitnesse/git/qing-automation/FitNesseRoot/files/testResults"); 
        try {
        	//此处做拷贝，有几个分机就要做几次拷贝。
			FileUtils.copyDirectory(resFile1,distFile);
		//	FileUtils.copyDirectory(resFile2,distFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
        return "Directory copy sucess";
	}

}
