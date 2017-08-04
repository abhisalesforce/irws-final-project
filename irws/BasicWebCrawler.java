package abhishek;

import org.jsoup.Jsoup;
import org.jsoup.Jsoup.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Document.*;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Element.*;
import org.jsoup.select.Elements;
import org.jsoup.select.Elements.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import java.security.SecureRandom;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.math.BigInteger;

import java.io.*;
import java.net.*;


public class BasicWebCrawler {
	static File fi = new File("urlmap.txt");
	
	
	static List<String> lis=new ArrayList<String>();
	public static long fname =0;
	public static long i =0;
	public String nuse;
	public static int lim=2000;
	static PrintWriter p;
    public static int number =1;
    private HashSet<String> links;
//    public static int lim=5;
//    public static int i=0;
    public BasicWebCrawler() {
        links = new HashSet<String>();
    }

    public void getPageLinks(String URL) throws FileNotFoundException {
        //4. Check if you have already crawled the URLs
        //
    	
    	if(lis.size()<=lim && !(lis.contains(URL)))
		{
    	
    	Myclass s= new Myclass();
    	
    	if (!links.contains(URL) && URL!=("") && URL.matches(".*unt.*") && URL.matches("^(http|https)://.*$") && !(URL.contains("#"))&& !(URL.endsWith("/")))
    	{
    		lis.add(URL);
    		nuse =URL;
    		
    	}
    	CookieHandler.setDefault(new CookieManager(null, CookiePolicy.ACCEPT_ALL));
    	//System.out.println("----------------------------------------");
    	//System.out.println("----------------------------------------");
    	//System.out.println("before my fun");
    	//System.out.println("***************************************");

    	//System.out.println("- - - - - - - - - - - - - - - - - - - -");
    	//System.out.println(lis);
    	//System.out.println("***************************************");
    	if (!links.contains(URL) && URL!=("") && URL.matches(".*unt.*") && URL.matches("^(http|https)://.*$") && !(URL.contains("#"))&& !(URL.endsWith("/")))
    	{
    		System.out.println("Final Url"+number+".)"+URL);
            //4. (i) If not add it to the index
            number++;
    		
    			Myclass.myfun(URL, fname, lis,p);
    		    fname++;
    		
    	}
    	//System.out.println("after my fun");
    	//System.out.println("----------------------------------------");
    	//System.out.println("----------------------------------------");
        if (!links.contains(URL) && URL!=("") && URL.matches(".*unt.*") && URL.matches("^(http|https)://.*$") && !(URL.contains("#"))&& !(URL.endsWith("/"))) {
            try {
            	
            	
     		    p.println(i+".txt");
     		    p.flush();
     		    i++;
     		    p.println(URL);
     		    p.flush();
     		
            	
                if (links.add(URL)) {
                    
                }

                Document document = Jsoup.connect(URL).timeout(5000).get();
                Elements linksOnPage = document.select("a[href]");
                for (Element page : linksOnPage) {
                	//System.out.println(linksOnPage);
                	String UR = page.attr("abs:href");
                	if(UR.matches(".*\\bunt\\b.*\\bedu\\b.*")&&!(UR.matches(".*\\bcatalog\\b.*"))&&!(UR.matches(".*\\bmaps\\b.*"))&&!(UR.matches(".*\\bcalendar\\b.*")))
                	{
                		getPageLinks(UR); 
                		
                	}
                   
                }
            	}
             catch (IOException e) {
                
            }
		}
    	
    }
   
    }

     
    	
    	
    
    public static void main(String[] args) throws FileNotFoundException {
        //1. Pick a URL from the frontier
    	FileOutputStream f = new FileOutputStream(fi);
      	p = new PrintWriter(f);
    	
        new BasicWebCrawler().getPageLinks("http://www.unt.edu");
        System.out.println(lis);
        
        
    }
    
}


