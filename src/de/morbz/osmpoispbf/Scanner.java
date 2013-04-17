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
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.openstreetmap.osmosis.core.container.v0_6.EntityContainer;
import org.openstreetmap.osmosis.core.domain.v0_6.Entity;
import org.openstreetmap.osmosis.core.domain.v0_6.Node;
import org.openstreetmap.osmosis.core.domain.v0_6.Tag;
import org.openstreetmap.osmosis.core.domain.v0_6.Way;
import org.openstreetmap.osmosis.core.domain.v0_6.WayNode;
import org.openstreetmap.osmosis.core.task.v0_6.RunnableSource;
import org.openstreetmap.osmosis.core.task.v0_6.Sink;

public class Scanner {
	private static StopWatch stop_watch = new StopWatch();
	
	//vars
	private static Map<Long, Point> nodes;
	private static HashSet<Long> neededNodes;
	private static int scanStep;
	private static Writer writer, writer_cities;
	
	//const
	private static final int STEP_WAY_NODES = 1;
	private static final int STEP_NODES = 2;
	private static final int STEP_WAYS = 3;
	public static final String SEPERATOR = "|";
	
	//files
	private static final String PBF_NAME = "berlin";
	private static String input_file, output_file, output_cities_file;
	
	//init
	public static void main(String[] args) {
		stop_watch.start();
		
		//Make file names
		if(args.length == 0) {
			System.out.println("E: Please provide an input file");
			System.exit(-1);
		}
		input_file = args[0] + ".osm.pbf";
		output_file = "pois_" + PBF_NAME + ".csv";
		output_cities_file = "cities_" + PBF_NAME + ".csv";
		
		//setup output
		try {
			writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(output_file),"UTF8"));
			writer_cities = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(output_cities_file),"UTF8"));
		} catch(IOException e) {
			System.out.println("E: Output file error");
			System.exit(-1);
		}
		
		//setup tables
		nodes = new HashMap<Long, Point>();
		neededNodes = new HashSet<Long>();
		
		//first scan ways nodes
		System.out.println("Step: STEP_WAY_NODES");
		scanStep = STEP_WAY_NODES;
		scanFile();
		
		//then scan nodes
		System.out.println("Step: STEP_NODES");
		scanStep = STEP_NODES;
		scanFile();
		
		quit();
	}
	
	//quit
	private static void quit() {
		//close writer
		try {
			writer.close();
			writer_cities.close();
		} catch(IOException e) {
			System.out.println("E: Output file close error");
			System.exit(-1);
		}
		
		stop_watch.stop();
		System.out.println("elapsed time in milliseconds: " + stop_watch.getElapsedTime());
		System.exit(0);
	}
	
	/* File */
	//scan file
	private static void scanFile() {
		File file = new File(input_file);
		
		//get entities
		Sink sinkImplementation = new Sink() {
		    public void process(EntityContainer entityContainer) {
		        Entity entity = entityContainer.getEntity();
		        if (entity instanceof Node) {
		        	if(scanStep == STEP_NODES) {
		        		//node
		        		handleNode((Node) entity);
		        	}
		        } else if (entity instanceof Way) {
		        	if(scanStep == STEP_NODES) { 
		        		System.out.println("Step: STEP_WAYS");
		        		scanStep = STEP_WAYS;
		        	}
		        	if(scanStep == STEP_WAY_NODES) {
			        	//way nodes
			            handleWayNodes((Way) entity);
		        	} else if(scanStep == STEP_WAYS) {
		        		//way
			            handleWay((Way) entity);
		        	}
		        } /*else if (entity instanceof Relation) {
		        }*/
		    }
		    public void release() { }
		    public void complete() { }
		};
		
		//start reader
		RunnableSource reader;
		try {
			reader = new crosby.binary.osmosis.OsmosisReader(
		            new FileInputStream(file));
			reader.setSink(sinkImplementation);
			Thread readerThread = new Thread(reader);
			readerThread.start();
			
			while (readerThread.isAlive()) {
			    try {
			        readerThread.join();
			    } catch (InterruptedException e) {
			        /* do nothing */
			    }
			}
		} catch(FileNotFoundException e) {
			System.out.println("E: Input file not found");
			System.exit(-1);
		}
	}
	
	/* Entities */
	//handle way nodes
	private static void handleWayNodes(Way way) {
		//check if has correct tags
		if(TagChecker.getPoi(way.getTags()) == null) {
			return;
		}
		
		//go through way nodes and request them
		List<WayNode> waynodes = way.getWayNodes();
		for(WayNode waynode : waynodes) {
			neededNodes.add(waynode.getNodeId());
		}
	}
	
	//handle node
	private static void handleNode(Node node) {
		Point point = null;
		
		//is this nodes requested?
		long node_id = node.getId();
		if(neededNodes.contains(node_id)) {
			//create point and add
			point = new Point(
					DegreeToInt(node.getLatitude()),
					DegreeToInt(node.getLongitude())
			);
			nodes.put(node_id, point);
			neededNodes.remove(node_id);
		}
		
		//vars
		Collection<Tag> tags = node.getTags();
		
		/* City */
		//check city
		CityVO city = CityChecker.getCity(tags);
		if(city != null) {
			//add coords
			if(point == null) {
				point = new Point(
						DegreeToInt(node.getLatitude()),
						DegreeToInt(node.getLongitude())
				);
			}
			city.coords = point;
			
			//save
			saveCity(city);
		}

		/* POI */
		//has correct tag?
		Poi poi = TagChecker.getPoi(tags);
		if(poi == null) {
			return;
		}
		
		//Found Poi!
		//add coords
		if(point == null) {
			point = new Point(
					DegreeToInt(node.getLatitude()),
					DegreeToInt(node.getLongitude())
			);
		}
		poi.coords = point;
		
		//save
		savePoi(poi);
	}

	//handle way
	private static void handleWay(Way way) {
		//check if has correct tags
		Poi poi;
		if((poi = TagChecker.getPoi(way.getTags())) == null) {
			return;
		}
		
		//has enough nodes?
		List<WayNode> waynodes = way.getWayNodes();
		int waynodes_length = waynodes.size();
		if(waynodes_length < 3) {
			return;
		}
		
		//Found Poi
		//build point list
		List<Point> points = new ArrayList<Point>();
		Point point;
		for(int i = 0; i < waynodes_length; i++) {
			//does node exist?
			point = nodes.get(waynodes.get(i).getNodeId());
			if(point == null) {
				System.out.println("Warning: Skipped way Poi: "+poi);
				return;
			}
			
			//add to array
			points.add(point);
		}
		
		//get centroid
		poi.coords = Polygon.centroid(points);
		
		//save
		savePoi(poi);
	}
	
	/* POI */
	private static void savePoi(Poi poi) {
		System.out.println(poi);
		try { 
			writer.write(poi.toCsv()+"\n");
		} catch(IOException e) {
			System.out.println("E: Output file write error");
			System.exit(-1);
		}
	}
	
	/* City */
	private static void saveCity(CityVO city) {
		System.out.println(city);
		try { 
			writer_cities.write(city.toCsv()+"\n");
		} catch(IOException e) {
			System.out.println("E: Output file write error");
			System.exit(-1);
		}
	}

	/* Library */
	private static int DegreeToInt(double degree) {
		int degree2 = (int)(degree * 100000);
		return degree2;
	}
}
