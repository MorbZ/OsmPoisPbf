**Extracts all POIs (Points of interest) from an OpenStreetMap file (.pbf) and outputs these in comma seperated files.** Feel free to help improving the scripts.

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
Two CSV output files are created: cities.csv and pois.csv. Actually these are pipe-seperated-files "|" as these are rarely used in location names (pipes are removed from names).

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

* **1.)** POI-Type, see [poi\_types.csv](https://github.com/MorbZ/OsmPoisPbf/blob/master/poi_types.csv) for the list of poi type names
* **2.)** Latitude  
* **3.)** Longitude  
* **4.)** Name of the POI  

### cities.csv
Sample:
> Britz|suburb|0|||||52.44833|13.44234  
> Schönow|suburb|0|Berlin,Bundesrepublik Deutschland,Europe||||52.41082|13.27071  
> Woltersdorf|village|7198|Oder-Spree,Brandenburg,Bundesrepublik Deutschland,Europe||||52.44953|13.75615  
> Berlin|city|3531201|||Germany||52.51703|13.38885  
> Neubabelsberg|suburb|0|Babelsberg,Potsdam,Brandenburg,Bundesrepublik Deutschland,Europe||||52.40209|13.11264  
> Fichtenau|hamlet|0|Schöneiche bei Berlin,Oder-Spree,Brandenburg,Bundesrepublik Deutschland,Europe||||52.46022|13.7008  
> Lankwitz|suburb|0|||||52.43369|13.34548  

Fields:

* **1.)** Name of city
* **2.)** Type (can be: city, town, village, hamlet, suburb, neighbourhood)
* **3.)** Population
* **4.)** Common location (OSM tag: is_in)
* **5.)** Kontinent
* **6.)** Country
* **7.)** State
* **8.)** Latitude
* **9.)** Longitude

POI Types
--------------

The parsing of tags is based on a collocation using the OSM Wiki [Map Features](http://wiki.openstreetmap.org/wiki/Map_Features) list. The [poi\_types.csv](https://github.com/MorbZ/OsmPoisPbf/blob/master/poi_types.csv) is a list which contains the POI Types and their description/names. I also have a .png file with symbols for every POI Type, but there are some symbols which requires licenses so I won't post it here until I did some research and replaced these.

Memory Usage / Performance
--------------

The program loads all geographical positions into the memory. The tags of nodes and areas are then parsed without beeing fully loaded into memory. For the whole planet this takes about 3.5GB of RAM (make sure you fit the -Xmx parameter and if you can give about 5-6GB RAM, as it is faster). Parsing of the whole planet takes about 1.5 hours on an AMD Athlon 64 6000+ X2 (one core used).

Geography
--------------

The positions of areas are parsed based on their geometrical centre. When there is a node above a same tagged area you get duplicates. So make sure you remove the duplicates if you have to (e.g. by running a radius check on same names and types).

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