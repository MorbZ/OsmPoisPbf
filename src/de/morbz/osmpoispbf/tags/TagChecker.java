/*
	Copyright 2012-2015, Merten Peetz
	
	This file is part of OsmPoisPbf.
	OsmPoisPbf is free software: you can redistribute it and/or modify it under the terms of the GNU 
	General Public License as published by the Free Software Foundation, either version 3 of the 
	License, or (at your option) any later version.
	
	OsmPoisPbf is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without 
	even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU 
	General Public License for more details.
	
	You should have received a copy of the GNU General Public License along with OsmPoisPbj. If not, 
	see http://www.gnu.org/licenses/.
*/

package de.morbz.osmpoispbf.tags;

import java.util.Collection;
import java.util.Hashtable;
import java.util.List;

import org.openstreetmap.osmosis.core.domain.v0_6.Tag;

import de.morbz.osmpoispbf.Filter;
import de.morbz.osmpoispbf.Poi;

public class TagChecker {
	public static Poi getPoi(Collection<Tag> tags, List<Filter> filters) {
		// Has at least two tags (name and tag for category)
		if(tags.size() < 2) {
			return null;
		}
		
		// Build tag table
		String name = null;
		String k, v;
		Hashtable<String, String> tags2 = new Hashtable<String, String>();
		for(Tag tag : tags) {
			k = tag.getKey();
			v = tag.getValue();
			tags2.put(k, v);
			
			// Get name
			if(k.equals("name")) {
				name = v;
			}
		}
		
		// Check name
		if(name == null) {
			return null;
		}
		
		// Check category
		String cat = null;
		for(Filter filter : filters) {
			cat = getCategory(filter, tags2, null);
			if(cat != null) {
				return createPoi(name, Integer.valueOf(cat));
			}
		}
		return null;
	}
	
	private static String getCategory(Filter filter, Hashtable<String, String> tags, String key) {
		// Use key of parent rule or current
		if(filter.hasKey()) {
			key = filter.getKey();
		}
		
		// Check for key/value
		if(tags.containsKey(key)) {
			if(filter.hasValue() && !filter.getValue().equals(tags.get(key))) {
				return null;
			}
		} else {
			return null;
		}
		
		// If childs have categories, those will be used
		for(Filter child : filter.childs) {
			String cat = getCategory(child, tags, key);
			if(cat != null) {
				return cat;
			}
		}
		return filter.getCategory();
	}
	
	// Creates a POI object
	private static Poi createPoi(String name, int cat) {
		Poi poi = new Poi();
		poi.name = name;
		poi.cat = cat;
		return poi;
	}
}
