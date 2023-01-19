import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.BufferedWriter;
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

   return;
}

   public class Write {
       public static void write(String file_path,String word) throws IOException {
           FileOutputStream fileOutputStream = null;
           if(word!="\r\n") {
        	   word = word + ' ';
           }
           File file = new File(file_path);
           if(!file.exists()){
               file.createNewFile();
           }
           fileOutputStream = new FileOutputStream(file,true);
           fileOutputStream.write((word).getBytes("gbk"));
//           fileOutputStream.flush();
           fileOutputStream.close();
       }
   }
   
   public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
	   String fileName = "C:\\Users\\lc010\\eclipse-workspace\\INF421\\input\\0.in";
	   Utils utils=new Utils();
	   utils.testReadFile2(fileName);
	}
}

}
