/*
	Copyright 2015, Merten Peetz
	
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

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class FilterFileParser {
	private static final int MAX_INDENT = 100; // Prevent stack overflow
	private String filename;
	
	public FilterFileParser(String filename) {
		this.filename = filename;
	}
	
	public List<Filter> parse() {
		// Load filter file
		List<String> lines = null;
		try {
			lines = readFilterFile(filename);
		} catch(IOException ex) {
			System.out.println("Error: Reading filter file failed");
			return null;
		}
		
		if(lines == null) {
			System.out.println("Error: Reading filter file failed");
			return null;
		}
		
		// Parse lines
		List<Filter> filters = new ArrayList<Filter>();
		Filter[] filterLevels = new Filter[MAX_INDENT + 1];
		int lineNumber = 0;
		for(String line : lines) {
			lineNumber++;
			
			// Ignore if comment
			if(line.length() > 0 && line.charAt(0) == '#') {
				continue;
			}
			
			// Ignore if empty line
			if(line.trim().length() == 0) {
				continue;
			}
			
			// Get indentation
			// Count leading spaces
			int spaces = 0;
			for(char c : line.toCharArray()) {
				if(c != ' ') {
					break;
				}
				spaces++;
			}
			int level = spaces / 2;
			
			// Check for max indent
			if(level > MAX_INDENT) {
				showError("Max indentation is " + MAX_INDENT + " levels", lineNumber);
				return null;
			}
			
			// Get parts
			line = line.trim();
			String[] parts = line.split("=", 2);
			if(parts.length != 2) {
				showError("No \"=\" character", lineNumber);
				return null;
			}
			String key = parts[0];
			String value = parts[1];
			
			// Get category name
			String category = "";
			parts = value.split(" ", 2);
			if(parts.length == 2) {
				value = parts[0];
				category = parts[1];
			}
			
			// Make filter
			Filter filter = new Filter(key, value, category);
			
			// Add to highest level
			if(level == 0) {
				filters.add(filter);
				
				// Check for key
				if(key.isEmpty()) {
					showError("No key for top level filter", lineNumber);
					return null;
				}
				
				// Reset filter levels
				filterLevels = new Filter[MAX_INDENT + 1];
				filterLevels[0] = filter;
				continue;
			}
			
			// Get parent
			Filter parent = filterLevels[level - 1];
			if(parent == null) {
				showError("Invalid indentation", lineNumber);
				return null;
			}
			
			// Check if valid
			if(!filter.hasKey() && !filter.hasValue()) {
				showError("Filters must have at least a key or a value", lineNumber);
				return null;
			}
			
			// After a value has to come a key
			if(parent.hasValue() && !filter.hasKey()) {
				showError("No key provided after a value", lineNumber);
				return null;
			}
			
			// After a key only has to come a value only
			if((parent.hasKey() && !parent.hasValue()) && (filter.hasKey() || !filter.hasValue())) {
				showError("After a key only rule must come a value only rule", lineNumber);
				return null;
			}
			
			// Add to parent
			parent.childs.add(filter);
			filterLevels[level] = filter;
		}
		
		// Verify filters
		for(Filter filter : filters) {
			if(!isFilterValid(filter)) {
				System.out.println("Error: Parsing filter file: There is at least one filter without children that either has no category name or is not closed by a value");
				return null;
			}
		}
		return filters;
	}
	
	// Check that all end points have a category name and are closed (recursive)
	private boolean isFilterValid(Filter filter) {
		// Check endpoint (filter without childs)
		if(filter.childs.isEmpty()) {
			// Check that there is a category name
			if(!filter.hasCategory()) {
				return false;
			}
			
			// Make sure that it's not key only
			if(filter.hasKey() && !filter.hasValue()) {
				return false;
			}
		}
		
		// Iterate childs
		for(Filter child : filter.childs) {
			if(!isFilterValid(child)) {
				return false;
			}
		}
		return true;
	}
	
	// Display an error message with line number
	private void showError(String msg, int lineNumber) {
		System.out.println("Error: Parsing filter file, line " + lineNumber + ": " + msg);
	}
	
	private List<String> readFilterFile(String filename) throws IOException {
		// Use default file or customized file
		InputStreamReader reader;
		if(filename == null) {
			InputStream stream = getClass().getResourceAsStream("/filters.txt");
			if(stream == null) {
				return null;
			}
			reader = new InputStreamReader(stream);
		} else {
			reader = new FileReader(filename);
		}
		
		// Read file
		BufferedReader buffered = new BufferedReader(reader);
		List<String> lines = new ArrayList<String>();
		String line;
		while((line = buffered.readLine()) != null) {
			lines.add(line);
		}
		buffered.close();
		return lines;
	}
}
