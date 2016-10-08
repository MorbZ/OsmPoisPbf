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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import net.morbz.osmonaut.osm.LatLon;

public class Poi {
	private String[] values;
	private String cat;
	private LatLon coords;
	private String osmId;

	private static String format;

	public Poi(String[] values, String cat, LatLon coords, String osmId) {
		this.values = values;
		this.cat = cat;
		this.coords = coords;
		this.osmId = osmId;
	}

	public List<String> getCsvFields() {
		List<String> fields = new ArrayList<String>();

		// Add basic information
		fields.add(cat);
		fields.add(osmId);
		fields.add(round(coords.getLat()));
		fields.add(round(coords.getLon()));

		// Add output tags
		fields.addAll(Arrays.asList(values));
		return fields;
	}

	public String toString() {
		String valStr = "";
		for(int i = 0; i < values.length; i++) {
			if(values[i] != null) {
				valStr += "\"" + values[i] + "\"";
			} else {
				valStr += "\"\"";
			}
			if(values.length - 1 != i) {
				valStr += ", ";
			}
		}
		return "[Poi(tags=["+valStr+"],osm-id=\""+osmId+"\",cat="+cat+",coords="+coords.toString()+")]";
	}

	private String round(double coordinate) {
		return String.format((Locale)null, format, coordinate);
	}

	/* Setters */
	public static void setDecimals(int decimals) {
		format = "%." + decimals + "f";
	}
}
