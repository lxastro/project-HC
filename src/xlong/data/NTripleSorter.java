package xlong.data;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;

import xlong.data.processer.SimplifyProcesser;
import xlong.data.processer.Triple2PairProcesser;
import xlong.data.processer.UrlNormalizeProcesser;
import xlong.util.PropertiesUtil;

public class NTripleSorter {
	public static void sort(ArrayList<String[]> triples) {
	    Collections.sort(triples, new Comparator<String[]>() {
			@Override
			public int compare(String[] arg0, String[] arg1) {
				return arg0[0].compareTo(arg1[0]);
			}
	      });
	}
	
	/**
	 * Testing code
	 */
	public static void main(String[] args) {
		int maxLines = 1000000000;
		try {
			PropertiesUtil.init();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		PropertiesUtil.loadProperties();
		String typeFile = PropertiesUtil.getProperty("DBpedia_instance_types.nt");
		String urlFile = PropertiesUtil.getProperty("DBpedia_external_links.nt");
	
		try {	
			NTripleReader reader = new NTripleReader(typeFile, 
					new SimplifyProcesser(new UrlNormalizeProcesser(new Triple2PairProcesser())));
			ArrayList<String[]> types = reader.readAll(maxLines);
			System.out.println("sort");
			NTripleSorter.sort(types);
			System.out.println("sorted");
			
			System.out.println("create map");
			HashMap<String, HashSet<String>> et = new HashMap<String, HashSet<String>>();
			for (String[] ss:types){
				if (!et.containsKey(ss[0])) {
					et.put(ss[0], new HashSet<String>());
				}
				et.get(ss[0]).add(ss[1]);
			}
					
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			NTripleReader reader = new NTripleReader(urlFile, 
					new SimplifyProcesser(new UrlNormalizeProcesser(new Triple2PairProcesser())));
			ArrayList<String[]> urls = reader.readAll(maxLines);
			System.out.println("sort");
			NTripleSorter.sort(urls);
			System.out.println("sorted");
		} catch (IOException e) {
			e.printStackTrace();
		}
		

		
	}

}
