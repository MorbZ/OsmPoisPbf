**Scans an OpenStreetMap file for nodes and areas whose tags indicate them as POIs (Points of interest) and extracts those into a comma seperated file.**

The parsing of tags is based on a collocation using the OSM Wiki [Map Features](http://wiki.openstreetmap.org/wiki/Map_Features) list. The [poi\_types.csv](https://github.com/MorbZ/OsmPoisPbf/blob/master/poi_types.csv) contains the POI-type-numbers used by this program and their associated names. For areas the positions of their geometrical centre is used. When there is a node above a same tagged area you get duplicates. So make sure you remove the duplicates if you have to (e.g. by running a radius check on same names and types).

Simple Usage
--------------

* Download the [osmpois.jar](http://morbz.de/downloads/osmpois.jar) file
* Get the [planet file](http://planet.openstreetmap.org/pbf/) or any other .osm.pbf
* Put the OpenStreetMap binary file (.pbf) in same directory as the jar file
* Run java in a terminal (make sure you have at least 4GB of free RAM for the whole planet):  
    `java -Xmx4g -jar osmpois.jar planet`
    * The last parameter must be the .osm.pbf file name without file ending (e.g. "planet" for planet.osm.pbf)

  
Output
--------------
Two CSV output files are created: [cities.csv](https://github.com/MorbZ/OsmPoisPbf/blob/master/doc/cities_csv.md) and pois.csv. Actually these are pipe-seperated-files "|" as these are rarely used in location names (pipes are removed from names).

### pois.csv ###
Sample:
> 132|46.07865|12.03679|Campo Sportivo  
> 21|46.08098|12.03941|Scuole Medie  
> 89|39.15728|-78.16837|Lowe's  
> 67|49.07546|8.82875|Humstermühle  
> 6|47.54811|-2.24219|Domaine de Bodeuc  
> 15|-34.78508|-58.65121|Unidad Carcelaria González Catán  
> 18|-34.77608|-58.66023|Cementerio Parque Lar de Paz

Fields:

* **1.)** POI-type, see [poi\_types.csv](https://github.com/MorbZ/OsmPoisPbf/blob/master/poi_types.csv) for the list of poi type names
* **2.)** Latitude  
* **3.)** Longitude  
* **4.)** Name of the POI

Memory Usage / Performance
--------------

The program loads all geographical positions into the memory. The tags of nodes and areas are then parsed without beeing fully loaded into memory. For the whole planet this takes about 3.5GB of RAM (make sure you fit the -Xmx parameter and if you can give about 5-6GB RAM, as it is faster). Parsing of the whole planet takes about 1.5 hours on an AMD Athlon 64 6000+ X2 (one core used).

Symbols
--------------

There is a sample high-resolution symbol set for each type of POI. Please refer [to the symbol licenses](https://github.com/MorbZ/OsmPoisPbf/blob/master/poi_symbols/symbol_licenses.txt) (mostly CC0) before using this. The single symbols of the grid are arranged based on their [POI-type-id](https://github.com/MorbZ/OsmPoisPbf/blob/master/poi_types.csv) starting with ID 1 from upper left, then second from left and so on.

[![POI Symbol Grid](https://raw.github.com/MorbZ/OsmPoisPbf/master/poi_symbols/symbol_grid.png) ""](https://github.com/MorbZ/OsmPoisPbf/blob/master/poi_symbols/symbol_grid.png)

Closing Words
--------------

I wrote this program for an app I made and don't need it anymore, so don't expect any support or further development. So feel free to help improving the scripts. Also it's while ago I made this, so I struggled a bit writing this documentation. 

License
--------------

Copyright 2012-2013, Merten Peetz

OsmPoisPbf is free software: you can redistribute it and/or modify it under the terms of the GNU 
General Public License as published by the Free Software Foundation, either version 3 of the 
License, or (at your option) any later version.

OsmPoisPbf is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without 
even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU 
General Public License for more details.

You should have received a copy of the GNU General Public License along with OsmPoisPbj. If not, 
see http://www.gnu.org/licenses/.