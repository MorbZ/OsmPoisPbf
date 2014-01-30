**Scans an OpenStreetMap file for nodes and areas whose tags indicate them as POIs (Points of interest) and extracts those into a comma seperated file.**

The parsing of tags is based on a collocation using the OSM Wiki [Map Features](http://wiki.openstreetmap.org/wiki/Map_Features) list. The [poi\_types.csv](https://github.com/MorbZ/OsmPoisPbf/blob/master/poi_types.csv) contains the POI-type-numbers used by this program and their associated names. For areas the positions of their geometrical centre is used. When there is a node above a same tagged area you get duplicates. So make sure you remove the duplicates if you have to (e.g. by running a radius check on same names and types).

[![Flattr me](http://api.flattr.com/button/flattr-badge-large.png)](https://flattr.com/thing/2481051/MorbZOsmPoisPbf-on-GitHub)

Simple Usage
--------------

* Download the lastest **osmpois.jar** file from the [releases page](https://github.com/MorbZ/OsmPoisPbf/releases)
* Get the [planet file](http://planet.openstreetmap.org/pbf/) or any other .osm.pbf
* Put the OpenStreetMap binary file (.pbf) in same directory as the jar file
* Run java in a terminal (make sure you have at least 4GB of free RAM for the whole planet):  
    `java -Xmx4g -jar osmpois.jar planet`
    * The last parameter must be the .osm.pbf file name without file ending (e.g. "planet" for planet.osm.pbf)
    * If you add the `-cities` (or `-c`) parameter after the file name, an additional [cities.csv](https://github.com/MorbZ/OsmPoisPbf/blob/master/doc/cities_csv.md) is created that contains all cities.

  
Output
--------------
A CSV output file called pois.csv is created. Actually it's a pipe-seperated-file "|" as these are rarely used in location names (pipes are removed from names).

### pois.csv ###
Sample (spaces added for better readability):
> 28 | N2272819038 | 52.46276 | 13.37742 | Bose-Eck  
> 28 | N2272820300 | 52.46667 | 13.35347 | Zum Bunker  
> 40 | W4377786 | 52.50574 | 13.32238 | Savignyplatz  
> 145 | W4381164 | 52.50517 | 13.32113 | Else-Ury-Bogen  
> 40 | W4392438 | 52.52438 | 13.29288 | Schlossgarten  
> 145 | W4395344 | 52.51857 | 13.37463 | Platz der Republik  
> 18 | W4401982 | 52.48562 | 13.31099 | Friedhof Wilmersdorf  

Fields:

* **1.)** POI-type, see [poi\_types.csv](https://github.com/MorbZ/OsmPoisPbf/blob/master/poi_types.csv) for the list of poi type names
* **2.)** OSM-ID of the element, first character indicates the element type (**W**ay or **N**ode)
* **3.)** Latitude  
* **4.)** Longitude  
* **5.)** Name of the POI

Memory Usage / Performance
--------------

The program loads only necessary geographical positions into the memory. The tags of nodes and areas are then parsed without beeing fully loaded into memory. For the whole planet this takes about 3.5GB of RAM (make sure you fit the -Xmx parameter and if you can give about 5-6GB RAM, as it is faster). Parsing of the whole planet takes about 1.5 hours on an AMD Athlon 64 6000+ X2 (one core used).

Symbols
--------------

There is a sample high-resolution symbol set for each type of POI. Please refer [to the symbol licenses](https://github.com/MorbZ/OsmPoisPbf/blob/master/poi_symbols/symbol_licenses.txt) (mostly CC0) before using this. The single symbols of the grid are arranged based on their [POI-type-id](https://github.com/MorbZ/OsmPoisPbf/blob/master/poi_types.csv) starting with ID 1 from upper left, then second from left and so on.

[![POI Symbol Grid](https://raw.github.com/MorbZ/OsmPoisPbf/master/poi_symbols/symbol_grid.png) ](https://github.com/MorbZ/OsmPoisPbf/blob/master/poi_symbols/symbol_grid.png)

License
--------------

Copyright 2012-2014, Merten Peetz

OsmPoisPbf is free software: you can redistribute it and/or modify it under the terms of the GNU 
General Public License as published by the Free Software Foundation, either version 3 of the 
License, or (at your option) any later version.

OsmPoisPbf is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without 
even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU 
General Public License for more details.

You should have received a copy of the GNU General Public License along with OsmPoisPbj. If not, 
see http://www.gnu.org/licenses/.