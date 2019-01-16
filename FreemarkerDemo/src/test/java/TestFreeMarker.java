import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import freemarker.template.Configuration;
import freemarker.template.Template;


public class TestFreeMarker {
	
	public static void main(String[] args)throws Exception {
		Configuration configuration = new Configuration();
		//设置模板所在的目录
		configuration.setDirectoryForTemplateLoading(new File("E:\\Workspaces_MALL\\FreemarkerDemo\\src\\main\\resources"));
        //设置模板的字符集
		configuration.setDefaultEncoding("utf-8");
		//设置模板文件
		Template template = configuration.getTemplate("my.aaa");
		//创建数据
		Map map = new HashMap();
		map.put("name", "jiawei");
		map.put("success", true);
		
		
		List goodsList=new ArrayList();
		Map goods1=new HashMap();
		goods1.put("name", "苹果");
		goods1.put("price", 5.8);
		Map goods2=new HashMap();
		goods2.put("name", "香蕉");
		goods2.put("price", 2.5);
		Map goods3=new HashMap();
		goods3.put("name", "橘子");
		goods3.put("price", 3.2);
		goodsList.add(goods1);
		goodsList.add(goods2);
		goodsList.add(goods3);
		map.put("goodsList", goodsList);
		map.put("today", new Date());
		map.put("point", 102920122);
		map.put("aaa", 102920122);
		
		//可以生成静态页
		Writer write = new FileWriter(new File("e:/aaa.html"));
		template.process(map, write);
		write.close();
		
		
		
	}

}
