package abhishek;
import java.awt.Dimension;
import java.io.BufferedReader;
import javax.swing.*;
import java.io.File;
import java.net.*;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.Set;
import java.util.SortedSet;
import java.util.StringTokenizer;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.print.DocFlavor.URL;
import javax.swing.text.html.HTMLDocument.Iterator;

import org.jsoup.Jsoup;
import org.jsoup.Jsoup.*;

public class Indexvector {
	public static HashMap<String, HashMap<String, Float>> weights() {
		HashMap<String, HashMap<String, Integer>> finalmap = new HashMap<String, HashMap<String, Integer>>(); //final hashmap
		HashMap<String, HashMap<String, Integer>> Termcount = new HashMap<String, HashMap<String, Integer>>(); //temp hashmap to count the words 
		HashMap<String, HashMap<String, Float>> tf = new HashMap<String, HashMap<String, Float>>(); //term frequency hashmap
		HashMap<String, HashMap<String, Float>> tfidf = new HashMap<String, HashMap<String, Float>>(); //hashmap for weights
		HashMap<String, Float> idf = new HashMap<String, Float>(); //hashmap for idf
		HashMap<String, Integer> stopwordsmap = new HashMap<String, Integer>(); //hashmap for stop words
		Porter port1 = new Porter();  //object creation for porter class
		String sent ;
				

		File folder = new File("./Links/"); //loading folders of the docs
		
		File[] totalfiles = folder.listFiles();
	
		for (File file : totalfiles) 
		{
			
			if (file.isFile()) {
				String s = file.getName();
				try {
					BufferedReader br1 = new BufferedReader(
							
							new FileReader("./stopwords.txt")); //reading stop words into hashmap
				
					while ((sent = br1.readLine()) != null) {
						stopwordsmap.put(sent, 0);
						
					}
				
					BufferedReader br = new BufferedReader(
							new FileReader("./Links/" +s)); //reading content of doc
					
					String sCurrentLine;

					StringBuilder builder = new StringBuilder();    //appeding all the content of the document to single line for fetching content from pattern and matcher
					while ((sCurrentLine = br.readLine()) != null) {
						builder.append(" ");
						builder.append(sCurrentLine);
					}
					
					//System.out.println(builder.toString());
					String s3 = remove(builder);
					
					String aw = "~`^=_[ ]:;{}?,.&!'- \" ()%&@#$/\\*>";   
					StringTokenizer st = new StringTokenizer(s3, aw);
					
					while (st.hasMoreTokens()) {                           //tokenizing
						String sr = ((String) st.nextElement()).toLowerCase();
						if (!stopwordsmap.containsKey(sr)) {
							sr = port1.stripAffixes(sr);                //performing stemming and finding freq of each word in documents
							if (finalmap.containsKey(sr)) {
								HashMap<String, Integer> temp = finalmap.get(sr);
								if (!temp.containsKey(s)) {
									temp.put(s, 1);
									finalmap.put(sr, temp);
								} else {
									temp.put(s, temp.get(s) + 1);
									finalmap.put(sr, temp);
								}
							} else {
								HashMap<String, Integer> temp = new HashMap<String, Integer>();
								temp.put(s, 1);
								finalmap.put(sr, temp);
							}
						

							if (Termcount.containsKey(s)) {                            //finding the term count to tf
								HashMap<String, Integer> temp = Termcount.get(s);
								if (!temp.containsKey(sr)) {
									temp.put(sr, 1);
									Termcount.put(s, temp);
								} else {
									temp.put(sr, temp.get(sr) + 1);
									Termcount.put(s, temp);
								}

							} else {
								HashMap<String, Integer> temp = new HashMap<String, Integer>();
								temp.put(sr, 1);
								Termcount.put(s, temp);
							}

						}
					}
					br.close();

				}

				catch (Exception e) {
					//System.out.println(e);

				}

			}
		}
		for (String name : Termcount.keySet()) {        //tf caliculation dividing by max term frequency in a doc
			String key = name.toString();
			HashMap<String, Integer> temp = Termcount.get(name);
			HashMap<String, Float> temp1 = new HashMap<String, Float>();
			float max_value = Collections.max(temp.values());
			for (String x : temp.keySet()) {
				temp1.put(x, temp.get(x) / max_value);
			}
			tf.put(key, temp1);
		}
		//System.out.println("tf"+tf);
		for (String name1 : finalmap.keySet()) {        //idf caliculation 
			String key1 = name1.toString();
			HashMap<String, Integer> temp = finalmap.get(name1);
			int count = 0;
			for (String y : temp.keySet()) {
				count += temp.get(y);
			}
			float d = (float) Math.log(1400 / count);
			idf.put(key1, d);
		}
       //System.out.println("idf"+idf);
		for (String k : tf.keySet()) {  //weight caliculation
			HashMap<String, Float> temp = tf.get(k);
			HashMap<String, Float> temp1 = new HashMap<String, Float>();
			for (String p : temp.keySet()) {
				String key1 = p.toString();
				float value = temp.get(p);
				temp1.put(key1, (value * idf.get(key1)));
			}

			tfidf.put(k, temp1);
		}
//		System.out.println("term frequency in each document is" + finalmap);
//		System.out.println("\n");
//		System.out.println("tf divided by maximum tf in a document is   " + tf);
//		
//		//System.out.println("term count is " + Termcount);
//		System.out.println("\n");
//		 System.out.println("idf values are   " + idf);
//		 System.out.println("\n");
		//System.out.println("weights of the documents obtained   " + tfidf);
		return tfidf;

	}
	//function for finding similarity of query with documents 
		
	static float cosim(HashMap<String, Float> querymap, HashMap<String, Float> docmap)
	{
	        float num = 0;
	        float den =0;
	        float wd =0;
	        float totalweightQuery =0;
	        float totalweightDocument =0;
	        float value =0;
	        System.out.println(querymap.size());
	        for(String x : querymap.keySet()){
	        	totalweightQuery=totalweightQuery+(querymap.get(x)*querymap.get(x));
	        	System.out.println("Total weight query"+totalweightQuery);
	        	if(docmap.containsKey(x))
	        	{
	        	totalweightDocument=totalweightDocument+(docmap.get(x)*docmap.get(x));
	        	System.out.println("total weight document"+totalweightDocument);
	        	}
	        	else
	        		totalweightDocument=0;
	        }
	        //System.out.println("TOTAL WEIGHT"+totalweightQuery);

	        //System.out.println("TOTAL document"+totalweightDocument);
	        den = (float) (Math.sqrt(totalweightQuery)*Math.sqrt(totalweightDocument));
	        for(String x : querymap.keySet()){
	        	if(docmap.containsKey(x))
	        	{
	        		wd = docmap.get(x);
	        		
	        	}
	        	else
	        	{
	        		wd =0;
	        	}
	        	num = num +querymap.get(x)*wd;	
	        }
	        if(den!=0)
	        {
	        value = (num/den);
//	        System.out.println("num"+num);
//	        System.out.println("den"+den);
//	        System.out.println("value"+value);
	        }
	        else
	        {
	        	return 0;
	        }
	        
	        return value;
		}
	
	
	
	
	
	

public static String  remove(StringBuilder second){
	
	String s =Jsoup.parse(second.toString()).text();
	return s;
}
public static <K> void main(String[] args) throws IOException
{
	HashMap<String, Float> Querymap = new HashMap<String, Float>();
	HashMap<String,Integer> stopwords=new HashMap<String,Integer>();
	HashMap<String, HashMap<String, Float>> weight = new HashMap<String, HashMap<String, Float>>();
	//Scanner in = new Scanner(System.in);
	
	String Query =JOptionPane.showInputDialog("Enter your Query");
	//String Query = in.nextLine();//reading query from user 
	Porter port1 = new Porter();
	String sent;
	BufferedReader br1 = new BufferedReader(
			new FileReader("./stopwords.txt"));
	 while (( sent = br1.readLine()) != null) {
		stopwords.put(sent, 0);
	 }
	 
	StringTokenizer st = new StringTokenizer(Query);
	
	while(st.hasMoreTokens())
	{
		String sr = ((String) st.nextToken()).toLowerCase();
		
		sr =port1.stripAffixes(sr);
		
		if(!stopwords.containsKey(sr))
		{
			if(Querymap.containsKey(sr))
			{
				Querymap.put(sr,Querymap.get(sr)+1);
			}
			else
			{
			Querymap.put(sr, (float) 1);
			}
			}//END OF IF 
		
		}//ENd of while
	//System.out.print(Querymap);
	weight= weights();
	HashMap<String, Float> result = new HashMap<String, Float>();
	for(String y :weight.keySet())
	{
		HashMap<String, Float> docmap = weight.get(y);
		float re=cosim(Querymap,docmap);
		
		result.put(y, re);
		
	}
	


	 List<Map.Entry<String, Float>> list =
	            new LinkedList<Map.Entry<String, Float>>( result.entrySet() );
	        Collections.sort( list, new Comparator<Map.Entry<String, Float>>()
	        {
	            public int compare( Map.Entry<String, Float> o1, Map.Entry<String, Float> o2 )
	            {
	                return (o1.getValue()).compareTo( o2.getValue() );
	            }
	        } );

	        Map<String, Float> result1 = new LinkedHashMap<String, Float>();
	        for (Map.Entry<String, Float> entry : list)
	        {
	            result1.put( entry.getKey(), entry.getValue() );
	        }
	
	
	        System.out.println("Sorted Search");
            System.out.println(result1);

	        List<String> l = new ArrayList<String>(result1.keySet());
	       
	        l = l.subList(l.size()-10,l.size());
            
            int k =l.size()-1;
            HashMap<String, String> urlmap =new HashMap<String,String>();
            File f = new File("urlmap.txt");
            Scanner input = new Scanner(f);
            int index =0;
            while(input.hasNextLine()){
            	
            urlmap.put(input.nextLine(), input.nextLine());
            }// to identifty document - to Url match
            
            HashMap<Integer,String> resultprint = new HashMap<Integer,String>();
            while(k>=0)
            {
            resultprint.put(10-k,l.get(k).toString());
            
            k--;
            }
	        System.out.println(resultprint);
	        System.out.println(urlmap);
            int abhi = 1;
            List<String> zoom = new ArrayList<>();
            JPanel panel = new JPanel();
            String[] arr =new String[11];
            System.out.println("Top 10 Relevanft Websites");
            while(abhi<=10)
            {
            	zoom.add(urlmap.get(resultprint.get(abhi)));
            	arr[abhi]=urlmap.get(resultprint.get(abhi));
            	abhi++;
            }
            panel.setPreferredSize(new Dimension(1000,2000));
    		panel.add(new JScrollPane());
    		System.out.println(zoom);
    		JList<Object> message = new JList<Object>(arr);
    		JOptionPane.showMessageDialog(panel, message,"top 10 Search Results", 0);
    		String[] a = new String[5];
    		input.close();
	        // main(a);
	 }//end of main	

}//end of class

	 
