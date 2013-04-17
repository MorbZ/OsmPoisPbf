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

import java.awt.Point;

public class Poi {
	public String name;
	public int kat;
	public Point coords = null;
	
	public String toCsv() {
		if(coords == null) {
			return "";
		}
		return kat+Scanner.SEPERATOR+(double)coords.x/100000+Scanner.SEPERATOR+(double)coords.y/100000+Scanner.SEPERATOR+name.replace(Scanner.SEPERATOR, ";");
	}
	
	public String toString() {
		if(coords != null) {
			return "[Poi(name=\""+name+"\",kat="+kat+",coords="+coords.toString()+")]";
		} else {
			return "[Poi(name=\""+name+"\",kat="+kat+"]";
		}
	}
}
