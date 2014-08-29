/**
 * Project : Classify Urls
 */
package xlong.data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.TreeSet;

import xlong.data.filter.EntityFilter;

/**
 * Class for merging information of external links and entity types. A simple
 * entity can contain a list of Urls or a list of types The combine method can
 * merge two simple entity.
 * 
 * @author Xiang Long (longx13@mails.tsinghua.edu.cn)
 */

public class Entity {


	/** The name of a entity */
	protected String name;

	/** The list of Urls */
	protected ArrayList<String> urls;

	/** The list of types */
	protected ArrayList<String> types;


	/**
	 * Constructor
	 * 
	 * @param name
	 *            the name of the entity.
	 */
	public Entity(String name) {
		this.name = name;
		urls = null;
		types = null;
	}

	/**
	 * Returns the name of the entity.
	 * 
	 * @return the name of the entity.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Returns the list of Urls of the entity.
	 * 
	 * @return the list of Urls of the entity.
	 */
	public ArrayList<String> getUrls() {
		return urls;
	}

	/**
	 * Returns the list of types of the entity.
	 * 
	 * @return the list of types of the entity.
	 */
	public ArrayList<String> getTypes() {
		return types;
	}

	/**
	 * Add a Url into ULRs list
	 * @param url the Url need to be added.
	 */
	public void addUrl(String url) {

		if (urls == null) {
			urls = new ArrayList<String>();
		}
		urls.add(url);
	}

	/**
	 * Add a type into types list.
	 * @param typethe type need to be added.
	 */
	public void addType(String type) {
		if (types == null) {
			types = new ArrayList<String>();
		}
		types.add(type);
	}

	
	private static final String typeStart = "http://dbpedia.org/ontology/";
	/**
	 * Filter types. Exclude types don't start with
	 * 'http://dbpedia.org/ontology/'. If type A is subclass of type B, then
	 * exclude type B.
	 * 
	 * @param subClassMap
	 *            the subclass of relationship map.
	 * @return success or not.
	 */
	public int filterTypes(Map<String, HashSet<String>> subClassMap) {
		if (types == null) {
			return 0;
		}
		HashSet<String> dels = new HashSet<String>();
		if (subClassMap != null){
			for (String type : types) {
				if (subClassMap.containsKey(type)) {
					dels.addAll(subClassMap.get(type));
				}
			}
		}
		HashSet<String> newTypes = new HashSet<String>();
		for (String type : types) {
			if (!dels.contains(type)
					&& type.startsWith(typeStart)) {
				newTypes.add(type.substring(typeStart.length()));
			}
		}
		types = new ArrayList<String>(newTypes);
		return types.size();
	}
	
	public int filterUrls() {
		if (urls == null) {
			return 0;
		}
		HashSet<String> newUrls = new HashSet<String>();
		for (String url : urls) {
			newUrls.add(url);
		}
		urls = new ArrayList<String>(newUrls);
		return urls.size();
	}
	
	public static Collection<Entity> generateEntities(ArrayList<String[]> types, ArrayList<String[]> urls, Map<String, HashSet<String>> subClassMap) {
		HashMap<String, Entity> entityMap = new HashMap<String, Entity>();
		for (String[] ss:types){
			if (!entityMap.containsKey(ss[0])) {
				entityMap.put(ss[0], new Entity(ss[0]));
			}
			entityMap.get(ss[0]).addType(ss[1]);
		}

		for (String[] ss:urls){
			if (!entityMap.containsKey(ss[0])) {
				entityMap.put(ss[0], new Entity(ss[0]));
			}
			entityMap.get(ss[0]).addUrl(ss[1]);
		}
		
		for (Entity en:entityMap.values()){
			en.filterTypes(subClassMap);
			en.filterUrls();
		}
		return entityMap.values();
	}
	
	public static Collection<Entity> generateEntities(ArrayList<String[]> types, ArrayList<String[]> urls) {
		return generateEntities(types, urls, null);
	}
	
	public static ArrayList<Entity> filtEntities(Collection<Entity> entities, EntityFilter filter) {
		ArrayList<Entity> newEntities = new ArrayList<Entity>();
		for (Entity en:entities) {
			if (filter.pipeFilt(en)) {
				newEntities.add(en);
			}
		}
		return newEntities;
	}
	
	public static ArrayList<String[]> entity2UrlTypePairs(Entity entity) {
		ArrayList<String[]> pairs = new ArrayList<String[]>();
		Collection<String> urls = entity.getUrls();
		Collection<String> types = entity.getTypes();
		if (urls == null || types == null) {
			return pairs;
		}
		for (String url:urls) {
			for (String type:types) {
				pairs.add(new String[] {url, type});
			}
		}
		return pairs;
	}
	
	public static ArrayList<String[]> entities2UrlTypePairs(Collection<Entity> entities) {
		ArrayList<String[]> pairs = new ArrayList<String[]>();
		for (Entity en:entities) {
			pairs.addAll(entity2UrlTypePairs(en));
		}
		return pairs;
	}
	
	public static HashMap<String, HashSet<String>> entities2TypeMap(Collection<Entity> entities) {
		HashMap<String, HashSet<String>> typeMap = new HashMap<String, HashSet<String>>();
		for (Entity entity:entities) {
			Collection<String> urls = entity.getUrls();
			Collection<String> types = entity.getTypes();		
			if (urls == null || types == null) {
				continue;
			}
			for (String type:types) {
				if (!typeMap.containsKey(type)) {
					typeMap.put(type, new HashSet<String>());
				}	
				HashSet<String> typedUrls = typeMap.get(type);
				typedUrls.addAll(urls);
			}
		}
		return typeMap;
	}
	
	public static HashMap<String, TreeSet<String>> entities2UrlMap(Collection<Entity> entities){
		HashMap<String, TreeSet<String>> urlMap = new HashMap<String, TreeSet<String>>();
		for (Entity entity:entities) {
			Collection<String> urls = entity.getUrls();
			Collection<String> types = entity.getTypes();		
			if (urls == null || types == null) {
				continue;
			}
			for (String url:urls) {
				if (!urlMap.containsKey(url)) {
					urlMap.put(url, new TreeSet<String>());
				}	
				urlMap.get(url).addAll(types);
			}
		}	
		return urlMap;
	}

	/**
	 * Gets if this entity is equals to another entity or not.
	 * 
	 * @param b
	 *            entity to compare with.
	 * @return this entity equals to entity b or not.
	 */
	public boolean equals(Entity b) {
		if (name.equals(b.name))
			return true;
		else
			return false;
	}

	/**
	 * To string method.
	 */
	@Override
	public String toString() {
		String s = name + "\n";
		for (String url : urls) {
			s = s + url + " ";
		}
		s = s + "\n";
		for (String type : types) {
			s = s + type + " ";
		}
		return s;
	}
}
