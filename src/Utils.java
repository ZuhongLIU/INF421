import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;

public class Utils {
	
	static String Onehot2String(int[]label) {
		String str="";
		for(int i=0;i<label.length;i++) {
			if(label[i]==1) {
				str+=(char)(i+65);
			}
		}
		if (str.length()==0) return "$ ";
		//System.out.println(str);
		return str+" ";
	}
	
   void testReadFile2(String fileName) throws IOException {
 
   // 读取文件内容到Stream流中，按行读取
   //List<String> lines = Files.readAllLines(Paths.get(fileName));
   //System.out.println(lines.findFirst().get());
   // 随机行顺序进行数据处理
   //lines.forEach(ele -> {
   //   System.out.println(ele);
   //});
   return;
}
   public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
	   String fileName = "C:\\Users\\lc010\\eclipse-workspace\\INF421\\input\\0.in";
	   Utils utils=new Utils();
	   utils.testReadFile2(fileName);
	   
	   

	}


}
