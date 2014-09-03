package xlong.cell;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.TreeSet;
import java.util.Map.Entry;

import xlong.data.IO.SubClassRelationReader;
import xlong.util.MyWriter;
import xlong.util.PropertiesUtil;


public class OntologyTree implements Comparable<OntologyTree>  {
	private String name;
	private TreeSet<OntologyTree> sons;
	private static TreeSet<String> types;
	
	private OntologyTree(String name){
		this.setTypeName(name);
		this.sons = new TreeSet<OntologyTree>();
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

	public static OntologyTree getTree(Map<String, HashSet<String>> subClassOfMap) {
		calTypes(subClassOfMap);
		System.out.println(types.size());
		OntologyTree root = new OntologyTree("root");
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
		Map<String, OntologyTree> HTMap = new HashMap<String, OntologyTree>();
		HTMap.put("root", root);
		
		while (true) {
			int cnt = 0;
			for (String type:types) {
				if ( (!oldAdded.contains(type)) && oldAdded.containsAll(parents.get(type))){
					cnt ++;
					OntologyTree son = new OntologyTree(type);
					HTMap.put(type, son);
					ArrayList<String> dirs = new ArrayList<String>(oldEdge);
					dirs.retainAll(parents.get(type));
					if (dirs.size()>1) System.out.println(type);
					if (dirs.size()>1) System.out.print("->");
					for (String dir:dirs) {
						if (dirs.size()>1) System.out.print(dir + "          ");
						HTMap.get(dir).sons.add(son);
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

	/**
	 * @return the name
	 */
	public String getTypeName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setTypeName(String name) {
		this.name = name;
	}
	
	public TreeSet<OntologyTree> getSons() {
		return sons;
	}
	
	private String toString(int level) {
		String str = "";
		for (int i = 0; i < level; i++) {
			str += "    ";
		}
		str += name + "\n";
		for (OntologyTree son:sons){
			str += son.toString(level + 1);
		}
		return str;
	}
	
	@Override
	public String toString(){
		return toString(0);
	}
	
	@Override
	public int compareTo(OntologyTree o) {
		return this.name.compareTo(o.name);
	}
	
	public static void main(String[] args) throws IOException{
		try {
			PropertiesUtil.init();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		PropertiesUtil.loadProperties();
		String ontologyFile = PropertiesUtil.getProperty("DBpedia_ontology.owl");
		
		MyWriter.setFile("result/ontology.txt" , false);
		Map<String, HashSet<String>> subClassOfMap = SubClassRelationReader.getSubClassOf(ontologyFile);	
		OntologyTree tree = OntologyTree.getTree(subClassOfMap);	
		MyWriter.writeln(tree.toString());
		MyWriter.close();
	}


}
