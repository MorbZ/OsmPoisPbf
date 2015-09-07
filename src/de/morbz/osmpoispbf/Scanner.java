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

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.List;

import net.morbz.osmonaut.EntityFilter;
import net.morbz.osmonaut.IOsmonautReceiver;
import net.morbz.osmonaut.Osmonaut;
import net.morbz.osmonaut.osm.Entity;
import net.morbz.osmonaut.osm.EntityType;
import net.morbz.osmonaut.osm.Tags;
import net.morbz.osmonaut.osm.Way;
import de.morbz.osmpoispbf.utils.StopWatch;

public class Scanner {
	// Const
	public static final String SEPERATOR = "|";
	private static final String VERSION = "v1.0.3";
	
	// Vars
	private static Writer writer;
	private static List<Filter> filters;
	
	public static void main(String[] args) {
		// Start watch
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		
		// Get filter rules
		FilterFileParser parser = new FilterFileParser(null);
		filters = parser.parse();
		if(filters == null) {
			System.exit(-1);
		}
		
		// Parse command line arguments
		System.out.println("OsmPoisPbf " + VERSION + " started");
		if(args.length == 0) {
			System.out.println("Error: Please provide an input file");
			System.exit(-1);
		} else if(args.length > 1) {
			System.out.println("Error: Too many parameters given");
			System.exit(-1);
		}
		
		// Make filenames
		String pbfName = args[0];
		String inputFile = pbfName + ".osm.pbf";
		String outputFile = "pois_" + pbfName + ".csv";
		
		// Setup CSV output
		try {
			writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFile),"UTF8"));
		} catch(IOException e) {
			System.out.println("Error: Output file error");
			System.exit(-1);
		}
		
		// Setup OSMonaut
		EntityFilter filter = new EntityFilter(true, true, false);
		Osmonaut naut = new Osmonaut(inputFile, filter);
		
		// Start OSMonaut
		naut.scan(new IOsmonautReceiver() {
		    @Override
		    public boolean needsEntity(EntityType type, Tags tags) {
		        return getCategory(tags, filters) != null;
		    }

		    @Override
		    public void foundEntity(Entity entity) {
		    	// Check if way is closed
		    	if(entity.getEntityType() == EntityType.WAY) {
		    		if(!((Way)entity).isClosed()) {
		    			return;
		    		}
		    	}
		    	
		    	// Get name
		    	Tags tags = entity.getTags();
		    	String name = tags.get("name");
				if(name == null) {
					return;
				}
				
				// Get category
				String cat = getCategory(tags, filters);
				if(cat == null) {
					return;
				}
				
				// Make OSM-ID
				String id = "";
				if(entity.getEntityType() == EntityType.WAY) {
					id = "W";
				} else if(entity.getEntityType() == EntityType.NODE) {
					id = "N";
				}
				id += entity.getId();
		    	
		        // Make POI
				Poi poi = new Poi(name, cat, entity.getCenter(), id);
				
				// Output
				System.out.println(poi);
				
				// Write to file
				try { 
					writer.write(poi.toCsv()+"\n");
				} catch(IOException e) {
					System.out.println("Error: Output file write error");
					System.exit(-1);
				}
		    }
		});
		
		// Close writer
		try {
			writer.close();
		} catch(IOException e) {
			System.out.println("Error: Output file close error");
			System.exit(-1);
		}
		
		// Show elapsed time
		stopWatch.stop();
		System.out.println("Elapsed time in milliseconds: " + stopWatch.getElapsedTime());
		
		// Quit
		System.exit(0);
	}
	
	/* Categories */
	public static String getCategory(Tags tags, List<Filter> filters) {
		// Has at least two tags (name and tag for category)
		if(tags.size() < 2) {
			return null;
		}
		
		// Check category
		String cat = null;
		for(Filter filter : filters) {
			cat = getCategoryRecursive(filter, tags, null);
			if(cat != null) {
				return cat;
			}
		}
		return null;
	}
	
	private static String getCategoryRecursive(Filter filter, Tags tags, String key) {
		// Use key of parent rule or current
		if(filter.hasKey()) {
			key = filter.getKey();
		}
		
		// Check for key/value
		if(tags.hasKey(key)) {
			if(filter.hasValue() && !filter.getValue().equals(tags.get(key))) {
				return null;
			}
		} else {
			return null;
		}
		
		// If childs have categories, those will be used
		for(Filter child : filter.childs) {
			String cat = getCategoryRecursive(child, tags, key);
			if(cat != null) {
				return cat;
			}
		}
		return filter.getCategory();
	}
}
