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
import de.morbz.osmpoispbf.tags.TagChecker;
import de.morbz.osmpoispbf.utils.StopWatch;

public class Scanner {
	private static StopWatch stop_watch = new StopWatch();
	private static List<Filter> filters;
	
	//vars
	private static Writer writer, writer_cities;
	private static boolean parseCities = false;
	
	//const
	public static final String SEPERATOR = "|";
	private static final String VERSION = "v1.0.3";
	
	//files
	private static String input_file, output_file, output_cities_file;
	
	//init
	public static void main(String[] args) {
		stop_watch.start();
		
		// Get filter rules
		FilterFileParser parser = new FilterFileParser(null);
		filters = parser.parse();
		if(filters == null) {
			System.exit(-1);
		}
		
		// Parse command line arguments
		System.out.println("OsmPoisPbf " + VERSION + " started");
		if(args.length == 0) {
			System.out.println("E: Please provide an input file");
			System.exit(-1);
		} else if(args.length == 2) {
			if(args[1].equals("-c") || args[1].equals("-cities")) {
				parseCities = true;
			} else {
				System.out.println("E: Could not recognize the last parameter");
				System.exit(-1);
			}
		} else if(args.length > 2) {
			System.out.println("E: Too many parameters given");
			System.exit(-1);
		}
		
		//set filenames
		String pbf_name = args[0];
		input_file = pbf_name + ".osm.pbf";
		output_file = "pois_" + pbf_name + ".csv";
		output_cities_file = "cities_" + pbf_name + ".csv";
		
		//setup output
		try {
			writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(output_file),"UTF8"));
			if(parseCities) {
				writer_cities = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(output_cities_file),"UTF8"));
			}
		} catch(IOException e) {
			System.out.println("E: Output file error");
			System.exit(-1);
		}
		
		// Setup OSMonaut
		EntityFilter filter = new EntityFilter(true, true, false);
		Osmonaut naut = new Osmonaut(input_file, filter);
		
		// Start OSMonaut
		naut.scan(new IOsmonautReceiver() {
		    @Override
		    public boolean needsEntity(EntityType type, Tags tags) {
		        return TagChecker.getPoi(tags, filters) != null;
		    }

		    @Override
		    public void foundEntity(Entity entity) {
		    	// Check if closed
		    	if(entity.getEntityType() == EntityType.WAY) {
		    		if(!((Way)entity).isClosed()) {
		    			return;
		    		}
		    	}
		    	
		        // Get POI
		    	Poi poi = TagChecker.getPoi(entity.getTags(), filters);
		    	poi.coords = entity.getCenter();
		    	
		    	//add id
				String id = "";
				if(entity.getEntityType() == EntityType.WAY) {
					id = "W";
				} else if(entity.getEntityType() == EntityType.NODE) {
					id = "N";
				}
				id += entity.getId();
				poi.osmId = id;
				
				savePoi(poi);
		    }
		});
		
		quit();
	}
	
	/* POI */
	private static void savePoi(Poi poi) {
		//save
		System.out.println(poi);
		try { 
			writer.write(poi.toCsv()+"\n");
		} catch(IOException e) {
			System.out.println("E: Output file write error");
			System.exit(-1);
		}
	}
	
	//quit
	private static void quit() {
		//close writer
		try {
			writer.close();
			if(parseCities) {
				writer_cities.close();
			}
		} catch(IOException e) {
			System.out.println("E: Output file close error");
			System.exit(-1);
		}
		
		stop_watch.stop();
		System.out.println("elapsed time in milliseconds: " + stop_watch.getElapsedTime());
		System.exit(0);
	}
}
