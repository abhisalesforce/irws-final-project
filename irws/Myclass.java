package abhishek;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.math.BigInteger;

import java.io.*;
import java.net.*;

public class Myclass {
	public static HashMap<String, String> url_doc =new HashMap<String,String>();
	public static long num;
	public static String urrl;
	static PrintWriter p;
	
	public static void myfun(String URLs,long i, List <String> lis,PrintWriter p2) throws FileNotFoundException {
	   URL url;
	   num =i;
	   Myclass.p=p2;
	   urrl =URLs;
	   InputStream is = null;
	   BufferedReader br;
	   String line;
        String s = "./links";
       
         SecureRandom random = new SecureRandom();
         int j=0;
         
          
	     try {
	       url = new URL(URLs);
	       is = url.openStream();  // thows an IOException
	       br = new BufferedReader(new InputStreamReader(is));
	       Random rand = new Random();					
	        File file = new File("./Links/"+i+".txt");
	       
	    	FileOutputStream f = new FileOutputStream(file);
	    	PrintWriter p = new PrintWriter(f);
	    	
	       while ((line = br.readLine()) != null) {
	       		p.print(line);
	       	 p.flush();
	       	
	           //System.out.println(line);
	       	
	       	i++;
	       }
	       
	   } catch (MalformedURLException mue) {
	        mue.printStackTrace();
	   } catch (IOException ioe) {
	        //ioe.printStackTrace();
	   } finally {
	       try {
	           if (is != null) is.close();
	       } catch (IOException ioe) {
	           // nothing to see here
	       }
	   }

	     	
		
	}
	
	
}