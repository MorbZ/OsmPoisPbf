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

package de.morbz.osmpoispbf;

import net.morbz.osmonaut.osm.LatLon;

public class Poi {
	private String name;
	private String cat;
	private LatLon coords;
	private String osmId;
	
	public Poi(String name, String cat, LatLon coords, String osmId) {
		this.name = name;
		this.cat = cat;
		this.coords = coords;
		this.osmId = osmId;
	}
	
	public String toCsv() {
		String str = "";
		str += getEscapedCsvString(cat) + Scanner.SEPERATOR;
		str += osmId + Scanner.SEPERATOR;
		str += round(coords.getLat()) + Scanner.SEPERATOR;
		str += round(coords.getLon()) + Scanner.SEPERATOR;
		str += getEscapedCsvString(name);
		return str;
	}
	
	public String toString() {
		return "[Poi(name=\""+name+"\",osm-id=\""+osmId+"\",cat="+cat+",coords="+coords.toString()+")]";
	}
	
	private String getEscapedCsvString(String str) {
		str = str.replace(Scanner.SEPERATOR, " ");
		return str;
	}
	
	private double round(double coordinate) {
		int factor = 10000000; // 7 decimals (OSM default)
		return (double)Math.round(coordinate * factor) / factor;
	}
}
