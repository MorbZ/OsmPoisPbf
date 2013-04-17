/*
	Copyright 2012-2013, Merten Peetz
	
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

import java.util.Collection;

import org.openstreetmap.osmosis.core.domain.v0_6.Tag;

public class CityChecker {
	//get poi object or null if no correct tags
	public static CityVO getCity(Collection<Tag> tags) {
		//has at least two tags (name and kat)
		if(tags.size() < 2) {
			return null;
		}
		
		//check tags, fill object
		String k, v;
		CityVO city = new CityVO();
		for(Tag tag : tags) {
			k = tag.getKey();
			v = tag.getValue();
			
			if(k.equals("name")) {
				city.name = v;
			} else if(k.equals("population")) {
				//parse int
				try {
					city.population = Integer.parseInt(v);
				} catch(NumberFormatException e) {
					System.out.println("E: Couldn't parse population");
				}
			} else if(k.equals("place")) {
				if(v.equals("city") || v.equals("town") || v.equals("village") ||
					v.equals("hamlet") || v.equals("suburb") || v.equals("neighbourhood")
				) {
						city.type = v;
				}
			} else if(k.equals("is_in")) {
				city.is_in = v;
			} else if(k.equals("is_in:country")) {
				city.is_in_country = v;
			} else if(k.equals("is_in:continent")) {
				city.is_in_continent = v;
			} else if(k.equals("is_in:state")) {
				city.is_in_state = v;
			}
		}
		
		//return if has at least name and type
		if(city.name != "" && city.type != "") {
			return city;
		}
		return null;
	}
}
