**Parses an OpenStreetMap file (.pbf) and outputs all POIs (points of interest) and cities in comma seperated files. Feel free to help improving the scripts.**

Usage
--------------

To run the program:
- Download the osmpois.jar file
- Put the OpenStreetMap binary file (*.pbf) in same directory as the jar file. You can get the planet file here: http://planet.openstreetmap.org/pbf/
- Run java in a terminal:
	$ java -Xmx4g -jar osmpois.jar planet
	The last parameter must be the .osm.pbf file name without file ending (e.g. "planet" for planet.osm.pbf)
  
Output
--------------

You get two CSV output files: cities_*.csv and pois_*.csv.

pois_planet.csv looks like this:
132|46.07865|12.03679|Campo Sportivo
21|46.08098|12.03941|Scuole Medie
89|39.15728|-78.16837|Lowe's
67|49.07546|8.82875|Humstermühle
6|47.54811|-2.24219|Domaine de Bodeuc
15|-34.78508|-58.65121|Unidad Carcelaria González Catán
18|-34.77608|-58.66023|Cementerio Parque Lar de Paz

Fields:
- 1: POI-Type (see poi_types.csv list)
- 2: Latitude
- 3: Longitude
- 4: Name of POI

cities_planet.csv looks like this:
Vidulini|hamlet|0|||||45.07631|13.85302
Estancia Jankho Huayo|hamlet|0|||||-15.96947|-68.55526
Auktsjaur|hamlet|0|||||65.74681|19.3973
Petha Ammapura|village|0|||||16.53224|76.66857

Fields:
- 1: Name of city
- 2: Type (can be: city, town, village, hamlet, suburb, neighbourhood)
- 3: Population (often 0)
- 4: Common location (OSM tag: is_in)
- 5: Kontinent
- 6: Country
- 7: State
- 8: Latitude
- 9: Longitude

POI Types
--------------

The parsing of tags is based on a collocation using the OSM Wiki (http://wiki.openstreetmap.org/wiki/Map_Features). The poi_types.csv is a list with the POI Types and their description/names. I also have a .png file with symbols for every POI Type, but there are some symbols which requires licenses so I won't post it here until I did some research and replaced these.

Memory Usage / Performance
--------------

This program loads all positions into the memory. The tags of nodes and areas are then parsed without fully loaded into memory. For the whole planet this takes about 3.5GB of RAM (make sure you fit the -Xmx parameter and if you have give about 5-6GB RAM, as it is faster). Parsing of the whole planet takes about 1.5 hours on an AMD Athlon 64 6000+ X2 (one core used).

Geography
--------------

Positions of areas are parsed based on their geometrical centre. The position of nodes is used directly. When there is a node above a same tagged area you get duplicates. So make sure you remove the duplicates if you have to (e.g. by running a radius check on same names and types).

Closing Words
--------------

I wrote this program for an app I made and don't need it anymore, so don't expect any support or further development. Also it's while ago I made this, so I struggled a bit writing this documentation. Sorry for that =)

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