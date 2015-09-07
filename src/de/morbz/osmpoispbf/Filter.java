/*
	Copyright 2015, Merten Peetz
	
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

package de.morbz.osmpoispbf;

import java.util.ArrayList;
import java.util.List;

public class Filter {
	private String key;
	private String value;
	private String category;
	
	public List<Filter> childs = new ArrayList<Filter>();
	
	public Filter(String key, String value, String category) {
		this.key = key.isEmpty() ? null : key;
		this.value = value.isEmpty() ? null : value;
		this.category = category.isEmpty() ? null : category;
	}
	
	public String getKey() {
		return key;
	}
	
	public String getValue() {
		return value;
	}
	
	public String getCategory() {
		return category;
	}
	
	public boolean hasKey() {
		return key != null;
	}
	
	public boolean hasValue() {
		return value != null;
	}
	
	public boolean hasCategory() {
		return category != null;
	}
}
