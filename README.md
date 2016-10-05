**Scans an OpenStreetMap file for nodes and areas (and relations) whose tags indicate them as POIs (points of interest) and extracts those into a comma separated file.**

The parsing of the tags is by default based on a collocation using the OSM Wiki [Map Features](http://wiki.openstreetmap.org/wiki/Map_Features) list. You can however define a custom filter file for parsing the POIs (see "Filter File" below). For areas the positions of their geometrical centre is used. When there is a node above a same tagged area there will be duplicates. The [OSMonaut](https://github.com/MorbZ/OSMonaut) framework is used to parse the OSM files. Java 7 is required to run this program.

Simple Usage
--------------
* Download the lastest **osmpois.jar** file from the [releases page](https://github.com/MorbZ/OsmPoisPbf/releases)
* Get the [planet file](http://planet.openstreetmap.org/pbf/) or any [other](http://download.geofabrik.de/) .osm.pbf file
* Put the OpenStreetMap binary file (.pbf) in same directory as the jar file
* Run Java in a terminal (make sure you have at least 9GB of free RAM for the whole planet):  
    `java -Xmx9g -jar osmpois.jar planet.osm.pbf`

Output
--------------
The program creates a CSV file (by default a pipe-seperated-file `|` as these are rarely used in location names). The first four columns are always the same. The following columns depend on which `outputTags` have been defined, in order they appear in the parameter list (see "Parameters" below). By default "name" is the only tag that's exported.

### Sample ###
Spaces added for better readability:
> 40 | W4377786 | 52.5057433 | 13.3223853 | Savignyplatz  
> 145 | W4381164 | 52.5051773 | 13.3211363 | Else-Ury-Bogen  
> 40 | W4392438 | 52.5243813 | 13.2928814 | Schlossgarten  
> 145 | W4395344 | 52.5185754 | 13.3746373 | Platz der Republik  

### Columns ###
* **1.)** Category name as defined in the filter file (by default see [poi\_types.csv](https://github.com/MorbZ/OsmPoisPbf/blob/master/doc/poi_types.csv) for the mapping of category-IDs to category-names)
* **2.)** OSM-ID of the element, first character indicates the element type (**W**ay, **N**ode or **R**elation)
* **3.)** Latitude  
* **4.)** Longitude  
* **5.)** Name of the POI  
or  
* **5.)** - **X.)** Custom tags

Parameters
--------------
Command line parameters can be used to customize the results. The usage is:  
`java -jar osmpois.jar <parameters> <PBF-file>`

| Long | Short | Description | Default |
|---|---|---|---|---|
| `--filterFile <file>` | `-ff`  | Custom filter file that is used to define POI categories | [filters.txt](https://github.com/MorbZ/OsmPoisPbf/blob/master/res/filters.txt) |
| `--outputFile <file>` | `-of` | The name of the resulting CSV file | Name of input file with .csv extension |
| `--requiredTags <list>` | `-rt` | Comma separated list of tags (keys) that an element must have in order to be considered for export. Use `,` as argument to make it empty. | `name` |
| `--undesiredTags <list>` | `-rt` | Comma separated list of tags=value combinations that should be filtered [key=value]. | |
| `--outputTags <list>` | `-ot` | Comma separated list of tags that are exported. Use `,` as argument to make it empty. | `name` |
| `--relations` | `-r` | Also parse relations. By default only ways and nodes are parsed. Requires more RAM and more time. | |
| `--noWays` | `-nw` | Don't parse ways/areas | |
| `--noNodes` | `-nn` | Don't parse nodes | |
| `--allowUnclosedWays` | `-u` | Allow ways that aren't closed. By default only closed ways (areas) are allowed. | |
| `--decimals <number>` | `-d` | Number of decimal places of coordinates | `7` (OSM default) |
| `--separator <character>` | `-s` | Character that is used to separate columns in the CSV file. Will be replaced by a space if it occurs in names. | `|` |
| `--verbose` | `-v` | Display every single found POI instead of a counter | |
| `--help` | `-h` | Print the help and exit the program | |

Filter File
--------------
The filter file defines key/value combinations of OSM-tags that select which POIs are exported and which category they belong to. The [default filter file](https://github.com/MorbZ/OsmPoisPbf/blob/master/res/filters.txt) was created to fit a specific [symbol set](https://github.com/MorbZ/OsmPoisPbf/blob/master/doc/poi_symbols/symbol_grid.png). By default the defined category names are just numbers that can be translated to type names via the [poi-types file](https://github.com/MorbZ/OsmPoisPbf/blob/master/doc/poi_types.csv).

### Format ###
It is possible to use a customized filter file by using the `--filterFile <file>` parameter. The [default filter file](https://github.com/MorbZ/OsmPoisPbf/blob/master/res/filters.txt) can be used as a reference.

Each line of the file defines a rule with the format:  
`<key>=<value> <category_name>`

- `<value>` is optional
- `<key>=<value>` only matches the exact key/value combination
- `<key>=` matches all elements that have this tag regardless of the value
- Lines can be commented by using `#` as the first character in the line  

Sub-rules can be defined by indenting a rule by 2 spaces and are only considered if its super-rule matched. Sub-rules can have sub-rules. The first rule with a category that matches the given tags is used for the category, unless it has a sub-rule that also matches. OSM elements that don't match any rule will not be exported.

- `<key>=` can be followed by the sub-rule `=<value>`. In this case if only the key matches, the category of the key is used, but if both key and value match, the category of the value is used.
- After a `=<value>` rule must come a rule that has a key
- `<category_name>` is optional except at the endpoints (last sub-rules in the chain)

Memory Usage / Performance
--------------
The program loads only necessary geographical data into the memory. The nodes and areas are then parsed without being fully loaded into memory. For the whole planet this takes at least 9GB of RAM using the default settings and about 1 hour on a EC2 m3.xlarge instance (as of September 2015). Make sure you fit the `-Xmx` parameter.

License
--------------
Copyright 2012-2015, Merten Peetz

OsmPoisPbf is free software: you can redistribute it and/or modify it under the terms of the GNU 
General Public License as published by the Free Software Foundation, either version 3 of the 
License, or (at your option) any later version.

OsmPoisPbf is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without 
even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU 
General Public License for more details.

You should have received a copy of the GNU General Public License along with OsmPoisPbj. If not, 
see http://www.gnu.org/licenses/.