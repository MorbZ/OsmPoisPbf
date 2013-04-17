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
import java.util.List;

public class Polygon { 
    //get centroid
    public static Point centroid(List<Point> points) {
    	//check if first equals last
    	if(!points.get(0).equals(points.get(points.size()-1))) {
    		points.add(points.get(0));
    	}
    	
    	//count
    	int n = points.size()-1;
    	
    	//process centroid
    	double area = 0.0;
    	double x = 0.0, y = 0.0;
    	double p1x, p1y, p2x, p2y;
    	double factor;
    	for(int i = 0; i < n; i++) {
    		//for polygon
    		p1x = (double)points.get(i).x;
    		p1y = (double)points.get(i).y;
    		p2x = (double)points.get(i+1).x;
    		p2y = (double)points.get(i+1).y;
    		factor = (p1x*p2y)-(p2x*p1y);
    		x += (p1x+p2x) * factor;
    		y += (p1y+p2y) * factor;
    		
    		//for area
    		area += factor;
    	}
    	
    	//make area
    	area *= 0.5;
    	if(area == 0) {
    		area = 0.00000000001;
    	}
    	
    	//centroid
    	double areafactor = 1/(6*area);
    	x *= areafactor;
    	y *= areafactor;
    	return new Point((int) x, (int) y);
    }
}
