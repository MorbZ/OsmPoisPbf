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

package de.morbz.osmpoispbf.city;

import java.awt.Point;

import de.morbz.osmpoispbf.Scanner;

public class CityVO {
	public String name = "";
	public String type = "";
	public int population = 0;
	public String is_in = "";
	public String is_in_continent = "";
	public String is_in_country = "";
	public String is_in_state = "";
	public Point coords = null;
	
	public String toCsv() {
		if(coords == null) {
			return "";
		}
		
		//make string
		String csv = name + Scanner.SEPERATOR + type + Scanner.SEPERATOR + String.valueOf(population)
			+ Scanner.SEPERATOR + is_in + Scanner.SEPERATOR + is_in_continent 
			+ Scanner.SEPERATOR + is_in_country + Scanner.SEPERATOR + is_in_state 
			+ Scanner.SEPERATOR	+ (double)coords.x/100000 + Scanner.SEPERATOR 
			+ (double)coords.y/100000;
		System.out.println(csv);
		return csv;
	}
	
	public String toString() {
		/*if(coords != null) {
			return "[Poi(name=\""+name+"\",kat="+kat+",coords="+coords.toString()+")]";
		} else {
			return "[Poi(name=\""+name+"\",kat="+kat+"]";
		}*/
		return name;
	}
}
