package xlong.main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import xlong.data.HierarchicalTree;
import xlong.data.SubClassRelationReader;
import xlong.data.TypeMapIO;
import xlong.util.PropertiesUtil;

public class ConvertToFeatures {
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
