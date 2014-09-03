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

import xlong.converter.StringToSparseVectorConverter;
import xlong.converter.tokenizer.Tokenizer;
import xlong.data.IO.SubClassRelationReader;
import xlong.data.IO.TypeMapIO;
import xlong.util.MyWriter;
import xlong.util.PropertiesUtil;

public class HierarchicalTree implements Comparable<HierarchicalTree> {
	private String typeName;
	private Collection<String> urls;
	private Collection<Map<Integer, Double>> vectors;
	private int cntUrls;
	private TreeSet<HierarchicalTree> sons;
	private static TreeSet<String> types;
	private static final String typeStart = "http://dbpedia.org/ontology/";
	private static final String extendName = ".txt";
	
	private HierarchicalTree(String name){
		if (name.startsWith(typeStart)) {
			typeName = name.substring(typeStart.length());
		} else {
			typeName = name;
		}
		urls = new ArrayList<String>();
		sons = new TreeSet<HierarchicalTree>();
	}
	
	private void addSon(HierarchicalTree ht){
		sons.add(ht);
	}
	
	public void urls2vectors(Tokenizer tokenizer, int wordsToKeep) {
		vectors = new StringToSparseVectorConverter(tokenizer, wordsToKeep).convert(urls);
		urls = null;
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

		for (HierarchicalTree son:sons){
			son.write(filePath + "/" + son.typeName);
		}
	}
	
	public static HierarchicalTree read(String filePath, String typeName) throws IOException{
		HierarchicalTree tree = new HierarchicalTree(typeName);
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
	
	public static HierarchicalTree read(String filePath) throws IOException{
		return read(filePath, "root");
	}
	
	public void loadUrls(HashMap<String, HashSet<String>> typeMap){
		if (typeMap.containsKey(typeName)){
			urls = typeMap.get(typeName);
		} else {
			urls = new HashSet<String>();
		}
		for (HierarchicalTree son:sons){
			son.loadUrls(typeMap);
		}
	}
	
	public void countUrls(){
		int cnt = urls.size();
		for (HierarchicalTree son:sons){
			son.countUrls();
			cnt += son.cntUrls;
		}
		cntUrls = cnt;
	}
	
	public void cut(int minimumUrl){
		for (HierarchicalTree son:sons) {
			son.cut(minimumUrl);
		}
		for (Iterator<HierarchicalTree> it = sons.iterator(); it.hasNext();){
			HierarchicalTree son = it.next();
			if (son.cntUrls < minimumUrl) {
				urls.addAll(son.urls);
				it.remove();
			}
		}	
		cntUrls = urls.size();
		for (HierarchicalTree son:sons) {
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
	
	public HierarchicalTree[] split(int[] percent, Random rand) {
		int npart = percent.length;
		HierarchicalTree[] trees = new HierarchicalTree[npart];
		List<List<String>> sUrls = splitUrls(percent, rand);
		for (int i = 0; i < npart; i++) {
			HierarchicalTree tree = new HierarchicalTree(typeName);
			tree.urls = sUrls.get(i);
			trees[i] = tree;
		}
		for (HierarchicalTree son:sons) {
			HierarchicalTree[] sSons = son.split(percent, rand);
			for (int i = 0; i < npart; i++) {
				HierarchicalTree tree = trees[i];
				tree.addSon(sSons[i]);
			}
		}		
		return trees;
	}
	
	public HierarchicalTree[] split(int[] percent) {
		Random rand = new Random();
		return split(percent, rand);
	}
	
	public HierarchicalTree[] split(int[] percent, long seed) {
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
	
	public static HierarchicalTree getTree(Map<String, HashSet<String>> subClassOfMap) {
		calTypes(subClassOfMap);
		System.out.println(types.size());
		HierarchicalTree root = new HierarchicalTree("root");
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
		Map<String, HierarchicalTree> HTMap = new HashMap<String, HierarchicalTree>();
		HTMap.put("root", root);
		
		while (true) {
			int cnt = 0;
			for (String type:types) {
				if ( (!oldAdded.contains(type)) && oldAdded.containsAll(parents.get(type))){
					cnt ++;
					HierarchicalTree son = new HierarchicalTree(type);
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
	public int compareTo(HierarchicalTree o) {
		return this.typeName.compareTo(o.typeName);
	}
	
	private String toString(int level) {
		String str = "";
		for (int i = 0; i < level; i++) {
			str += "    ";
		}
		str += typeName + "\n";
		for (HierarchicalTree son:sons){
			str += son.toString(level + 1);
		}
		return str;
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
	
	/**
	 * @return the vectors
	 */
	public Collection<Map<Integer, Double>> getVectors() {
		return vectors;
	}
	
	@Override
	public String toString(){
		return toString(0);
	}
	
	public static void main(String[] args) throws IOException {
		HashMap<String, HashSet<String>> typeMap = TypeMapIO.read("result/types");
		System.out.println(typeMap.size());
		int cnt = 0;
		for (HashSet<String> urls:typeMap.values()){
			cnt += urls.size();
		}
		System.out.println(cnt);

		try {
			PropertiesUtil.init();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		PropertiesUtil.loadProperties();
		String ontologyFile = PropertiesUtil.getProperty("DBpedia_ontology.owl");
		Map<String, HashSet<String>> subClassMap = SubClassRelationReader.getSubClassOf(ontologyFile);
		HierarchicalTree root = HierarchicalTree.getTree(subClassMap);
		
		ArrayList<String> types= new ArrayList<String>(typeMap.keySet());
		types.removeAll(HierarchicalTree.getTypes());
		for (String type:types) {
			System.out.println(type);
		}		
		
		root.loadUrls(typeMap);
		root.countUrls();
		System.out.println(root.getCntUrls());
		
		root.write("result/maintree");
		root = HierarchicalTree.read("result/maintree");
		root.countUrls();
		System.out.println(root.getCntUrls());
		
		root.cut(100);
		System.out.println(root.getCntUrls());
		
		HierarchicalTree[] trees = root.split(new int[] {10,20,30,40});
		for (HierarchicalTree tree:trees) {
			tree.countUrls();
			System.out.println(tree.getCntUrls());
		}
		
		trees[0].write("result/splittree1");
		trees[1].write("result/splittree2");

	}
}
