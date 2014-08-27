package xlong.data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;

import xlong.util.MyWriter;
import xlong.util.PropertiesUtil;

public class HierachicalTree implements Comparable<HierachicalTree> {
	private String typeName;
	private Collection<String> urls;
	private int cntUrls;
	private TreeSet<HierachicalTree> sons;
	private static TreeSet<String> types;
	private static final String typeStart = "http://dbpedia.org/ontology/";
	private static final String extendName = ".txt";
	
	private HierachicalTree(String name){
		if (name.startsWith(typeStart)) {
			typeName = name.substring(typeStart.length());
		} else {
			typeName = name;
		}
		urls = new ArrayList<String>();
		sons = new TreeSet<HierachicalTree>();
	}
	
	private void addSon(HierachicalTree ht){
		sons.add(ht);
	}
	
	public void write(String filePath){
		String type = typeName;
		if (urls.size() > 0){
			MyWriter.setFile(filePath + "/" + type + extendName, false);
			for (String url:urls) {
				MyWriter.writeln(url);
			}
			MyWriter.close();
		}

		for (HierachicalTree son:sons){
			son.write(filePath + "/" + son.typeName);
		}
	}
	
	public static HierachicalTree read(String filePath, String typeName) throws IOException{
		HierachicalTree tree = new HierachicalTree(typeName);
		File dir = new File(filePath);
		for (File f:dir.listFiles()){
			if (f.isDirectory()) {
				tree.addSon(read(filePath + "/" + f.getName(), f.getName()));
			} else {
				BufferedReader in = new BufferedReader(new FileReader(f));
				tree.urls = new HashSet<String>();
				String url;
				while ((url = in.readLine()) != null) {
					if (url.length() > 0){
						tree.urls.add(url);
					}
				}
				in.close();
			}
		}
		return tree;
	}
	
	public static HierachicalTree read(String filePath) throws IOException{
		return read(filePath, "root");
	}
	
	public void loadUrls(HashMap<String, HashSet<String>> typeMap){
		if (typeMap.containsKey(typeName)){
			urls = typeMap.get(typeName);
		} else {
			urls = new HashSet<String>();
		}
		for (HierachicalTree son:sons){
			son.loadUrls(typeMap);
		}
	}
	
	public void countUrls(){
		int cnt = urls.size();
		for (HierachicalTree son:sons){
			son.countUrls();
			cnt += son.cntUrls;
		}
		cntUrls = cnt;
	}
	
	public void cut(int minimumUrl){
		for (HierachicalTree son:sons) {
			son.cut(minimumUrl);
		}
		for (Iterator<HierachicalTree> it = sons.iterator(); it.hasNext();){
			HierachicalTree son = it.next();
			if (son.cntUrls < minimumUrl) {
				it.remove();
			}
		}	
		cntUrls = urls.size();
		for (HierachicalTree son:sons) {
			cntUrls += son.cntUrls;
		}
	}
	
	public List<List<String>> splitUrls(int[] percent, Random rand) {
		int npart = percent.length;
		int n = urls.size();
		int[] ns = new int[npart];
		int s = 0;
		int sp = 0;
		for (int i = 0; i < npart; i++) {
			ns[i] = (int) Math.round(((double)n)*percent[i]/100.0);
			s += ns[i];
			sp += percent[i];
		}
		if (sp > 100) {
			return null;
		}
		if (s != (int) Math.round(((double)n)*sp/100.0)){
			int maxid = 0;
			for (int i = 1; i < npart; i++) {
				if (percent[i] > percent[maxid]) {
					maxid = i;
				}
			}
			ns[maxid] -= (s - (int) Math.round(((double)n)*sp/100.0));
		}
		
//		for (int i = 0; i < npart; i++) {
//			System.out.print(ns[i] + " ");
//		}
//		System.out.println();
		
		List<String> aUrls = new ArrayList<String> (urls);
		Collections.shuffle(aUrls, rand);
		List<List<String>> sUrls = new ArrayList<List<String>>();
		
		sUrls.add(aUrls.subList(0, ns[0]));
		int sum = ns[0];
		for (int i = 1; i < npart; i++) {
			sUrls.add(aUrls.subList(sum, sum + ns[i]));
			sum += ns[i];
		}
		return sUrls;
	}
	
	public HierachicalTree[] split(int[] percent, Random rand) {
		int npart = percent.length;
		HierachicalTree[] trees = new HierachicalTree[npart];
		List<List<String>> sUrls = splitUrls(percent, rand);
		for (int i = 0; i < npart; i++) {
			HierachicalTree tree = new HierachicalTree(typeName);
			tree.urls = sUrls.get(i);
			trees[i] = tree;
		}
		for (HierachicalTree son:sons) {
			HierachicalTree[] sSons = son.split(percent, rand);
			for (int i = 0; i < npart; i++) {
				HierachicalTree tree = trees[i];
				tree.addSon(sSons[i]);
			}
		}		
		return trees;
	}
	
	public HierachicalTree[] split(int[] percent) {
		Random rand = new Random();
		return split(percent, rand);
	}
	
	public HierachicalTree[] split(int[] percent, long seed) {
		Random rand = new Random(seed);
		return split(percent, rand);
	}
	
	public static Set<String> getTypes() {
		HashSet<String> tps = new HashSet<String>();
		for (String type:types){
			tps.add(type.substring(typeStart.length()));
		}
		return tps;
	}
	
	public static void calTypes(Map<String, HashSet<String>> subClassOfMap) {
		types = new TreeSet<String>();
		for (Entry<String, HashSet<String>> en : subClassOfMap.entrySet()) {
			types.add(en.getKey());
			for (String type:en.getValue()) {
				types.add(type);
			}
		}
	}
	
	public static HierachicalTree getTree(Map<String, HashSet<String>> subClassOfMap) {
		calTypes(subClassOfMap);
		System.out.println(types.size());
		HierachicalTree root = new HierachicalTree("root");
		Map<String, HashSet<String>> parents = new HashMap<String, HashSet<String>>();
		for (String type:types) {
			parents.put(type, new HashSet<String>());
			parents.get(type).add("root");
		}
		for (Entry<String, HashSet<String>> en : subClassOfMap.entrySet()) {
			parents.get(en.getKey()).addAll(en.getValue());
		}
		HashSet<String> oldAdded = new HashSet<String>();
		oldAdded.add("root");
		HashSet<String> newAdded = new HashSet<String>();
		newAdded.add("root");		
		HashSet<String> oldEdge = new HashSet<String>();
		oldEdge.add("root");
		HashSet<String> newEdge = new HashSet<String>();
		Map<String, HierachicalTree> HTMap = new HashMap<String, HierachicalTree>();
		HTMap.put("root", root);
		
		while (true) {
			int cnt = 0;
			for (String type:types) {
				if ( (!oldAdded.contains(type)) && oldAdded.containsAll(parents.get(type))){
					cnt ++;
					HierachicalTree son = new HierachicalTree(type);
					HTMap.put(type, son);
					ArrayList<String> dirs = new ArrayList<String>(oldEdge);
					dirs.retainAll(parents.get(type));
					if (dirs.size()>1) System.out.println(type);
					if (dirs.size()>1) System.out.print("->");
					for (String dir:dirs) {
						if (dirs.size()>1) System.out.print(dir + "          ");
						HTMap.get(dir).addSon(son);
					}
					if (dirs.size()>1) System.out.println();
					newAdded.add(type);
					newEdge.add(type);
				}
			}
			if (cnt == 0) {
				break;
			}
			oldEdge = newEdge;
			newEdge = new HashSet<String>();
			oldAdded = newAdded;
			newAdded = new HashSet<String>(oldAdded);
		}
		
		return root;
	}
	

	@Override
	public int compareTo(HierachicalTree o) {
		return this.typeName.compareTo(o.typeName);
	}
	
	private String toString(int level) {
		String str = "";
		for (int i = 0; i < level; i++) {
			str += "    ";
		}
		str += typeName + "\n";
		for (HierachicalTree son:sons){
			str += son.toString(level + 1);
		}
		return str;
	}
	
	@Override
	public String toString(){
		return toString(0);
	}
	
	public static void main(String[] args) throws IOException {
		try {
			PropertiesUtil.init();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		PropertiesUtil.loadProperties();
		String ontologyFile = PropertiesUtil.getProperty("DBpedia_ontology.owl");
		MyWriter.setFile("result/tree" , false);
		Map<String, HashSet<String>> subClassMap = SubClassRelationReader.getSubClassOf(ontologyFile);
		HierachicalTree root = HierachicalTree.getTree(subClassMap);
		MyWriter.write(root.toString());
		MyWriter.close();
	}

	/**
	 * @return the urls
	 */
	public Collection<String> getUrls() {
		return urls;
	}
	
	public int getCntUrls(){
		return cntUrls;
	}
}
