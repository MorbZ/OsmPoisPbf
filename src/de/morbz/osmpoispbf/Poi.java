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
	public String name;
	public int cat;
	public LatLon coords = null;
	public String osmId = "";
	
	public String toCsv() {
		if(coords == null) {
			return "";
		}
		
		String str = "";
		str += cat + Scanner.SEPERATOR;
		str += osmId + Scanner.SEPERATOR;
		str += (double)Math.round(coords.getLat()*100000)/100000 + Scanner.SEPERATOR;
		str += (double)Math.round(coords.getLon()*100000)/100000 + Scanner.SEPERATOR;
		str += getEscapedCsvString(name);
		return str;
	}
	
	public String toString() {
		if(coords != null) {
			return "[Poi(name=\""+name+"\",osm-id=\""+osmId+"\",cat="+cat+",coords="+coords.toString()+")]";
		} else {
			return "[Poi(name=\""+name+"\",osm-id=\""+osmId+"\",cat="+cat+"]";
		}
	}
	
	private String getEscapedCsvString(String str) {
		str = str.replace(Scanner.SEPERATOR, ";");
		str = str.replace("\n", "");
		return str;
	}
}
