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

public class Const {
	//special const for superclasses
	public static final int SUPER_PLACE_OF_WORSHIP = -1;
	public static final int SUPER_MAN_MADE_TOWER = -2;
	public static final int SUPER_RAILWAY_STATION = -3;
	public static final int SUPER_PUBLIC_TRANSPORT_STOP_POSITION = -4;
	public static final int SUPER_TOURISM_INFORMATION = -5;
	public static final int SUPER_LANDUSE_FOREST = -6;
	public static final int SUPER_NATURAL_WOOD = -7;
	
	//icon types
	public static final int ACCOMMO_ALPINEHUT = 1;
	public static final int ACCOMMO_CAMPING = 2;
	public static final int ACCOMMO_CARAVAN = 3;
	public static final int ACCOMMO_CHALET = 4;
	public static final int ACCOMMO_HOSTEL = 5;
	public static final int ACCOMMO_HOTEL = 6;
	public static final int ACCOMMO_MOTEL = 7;
	
	public static final int AMENITY_COURT = 8;
	public static final int AMENITY_FIRESTATION = 9;
	public static final int AMENITY_LIBRARY = 11;
	public static final int AMENITY_PLAYGROUND = 12;
	public static final int AMENITY_POLICE = 13;
	public static final int AMENITY_POSTOFFICE = 14;
	public static final int AMENITY_PRISON = 15;
	public static final int AMENITY_PUBLICBUILDING = 16;
	public static final int AMENITY_TOWNHALL = 17;

	public static final int BARRIER_BLOCKS = 18;
	
	public static final int EDUCATION_COLLEGE = 19;
	public static final int EDUCATION_NURSERY = 20;
	public static final int EDUCATION_SCHOOL = 21;
	public static final int EDUCATION_UNIVERSITY = 22;
	
	public static final int FOOD_BAR = 23;
	public static final int FOOD_BIERGARTEN = 24;
	public static final int FOOD_CAFE = 25;
	public static final int FOOD_FASTFOOD = 26;
	public static final int FOOD_ICECREAM = 27;
	public static final int FOOD_PUB = 28;
	public static final int FOOD_RESTAURANT = 29;

	public static final int HEALTH_DENTIST = 30;
	public static final int HEALTH_DOCTORS = 31;
	public static final int HEALTH_HOSPITALEMERGENCY = 32;
	public static final int HEALTH_HOSPITAL = 33;
	public static final int HEALTH_PHARMACY = 34;
	public static final int HEALTH_VETERINARY = 35;
	
	public static final int LANDUSE_ALLOTMENTS = 36;
	public static final int LANDUSE_CONIFEROUSDECIDUOUS = 37;
	public static final int LANDUSE_CONIFEROUS = 38;
	public static final int LANDUSE_DECIDUOUS = 39;
	public static final int LANDUSE_GRASS = 40;
	public static final int LANDUSE_HILLS = 41;
	public static final int LANDUSE_MILITARY = 42;
	public static final int LANDUSE_QUARY = 43;
	public static final int LANDUSE_SCRUB = 44;
	public static final int LANDUSE_SWAMP = 45;

	public static final int MONEY_BANK = 46;
	public static final int MONEY_EXCHANGE = 47;
	
	public static final int POW_BAHAI = 48;
	public static final int POW_BUDDHIST = 49;
	public static final int POW_CHRISTIAN = 50;
	public static final int POW_HINDU = 51;
	public static final int POW_ISLAMIC = 52;
	public static final int POW_JAIN = 53;
	public static final int POW_JEWISH = 54;
	public static final int POW_SHINTO = 55;
	public static final int POW_SIKH = 56;
	public static final int POW_UNKOWN = 58;
	
	public static final int POI_CAVE = 59;
	public static final int POI_CRANE = 60;
	public static final int POI_EMBASSY = 61;
	public static final int POI_BUNKER = 62;
	public static final int POI_MINE = 63;
	public static final int POI_PEAK1 = 64;
	public static final int POI_PEAK = 65;
	public static final int POI_CITY = 66;
	public static final int POI_HAMLET = 67;
	public static final int POI_SUBURB = 68;
	public static final int POI_TOWN = 69;
	public static final int POI_VILLAGE = 70;
	public static final int POI_TOWERCOMMUNICATION = 71;
	public static final int POI_TOWERLOOKOUT = 72;
	
	public static final int SHOP_ALCOHOL = 73;
	public static final int SHOP_BAKERY = 74;
	public static final int SHOP_BICYCLE = 75;
	public static final int SHOP_BOOK = 76;
	public static final int SHOP_BUTCHER = 77;
	public static final int SHOP_CARREPAIR = 78;
	public static final int SHOP_CAR = 79;
	public static final int SHOP_CLOTHES = 80;
	public static final int SHOP_COMPUTER = 81;
	public static final int SHOP_CONFECTIONERY = 82;
	public static final int SHOP_CONVENIENCE = 83;
	public static final int SHOP_COPYSHOP = 84;
	public static final int SHOP_DEPARTMENTSTORE = 85;
	public static final int SHOP_DIY = 86;
	public static final int SHOP_FISH = 87;
	public static final int SHOP_FLORIST = 88;
	public static final int SHOP_GARDENCENTRE = 89;
	public static final int SHOP_GIFT = 90;
	public static final int SHOP_GREENGROCER = 91;
	public static final int SHOP_HAIRDRESSER = 92;
	public static final int SHOP_HEARINGAIDS = 93;
	public static final int SHOP_HIFI = 94;
	public static final int SHOP_JEWELRY = 95;
	public static final int SHOP_KIOSK = 96;
	public static final int SHOP_LAUNDRETTE = 97;
	public static final int SHOP_MARKETPLACE = 98;
	public static final int SHOP_PHONE = 99;
	public static final int SHOP_MOTORCYCLE = 100;
	public static final int SHOP_MUSIC = 101;
	public static final int SHOP_NEWSPAPER = 102;
	public static final int SHOP_PET = 103;
	public static final int SHOP_SHOES = 104;
	public static final int SHOP_SUPERMARKET = 105;
	public static final int SHOP_TOBACCO = 106;
	public static final int SHOP_TOYS = 107;
	public static final int SHOP_VENDINGMASCHINE = 108;
	public static final int SHOP_VIDEORENTAL = 109;
	
	public static final int SPORT_ARCHERY = 110;
	public static final int SPORT_BASEBALL = 111;
	public static final int SPORT_BASKETBALL = 112;
	public static final int SPORT_BOWLING = 113;
	public static final int SPORT_CANOE = 114;
	public static final int SPORT_CRICKET = 115;
	public static final int SPORT_DIVING = 116;
	public static final int SPORT_FOOTBALL = 117;
	public static final int SPORT_GOLF = 118;
	public static final int SPORT_GYM = 119;
	public static final int SPORT_GYMNASIUM = 120;
	public static final int SPORT_CLIMBING = 121;
	public static final int SPORT_HORSE = 122;
	public static final int SPORT_ICESKATING = 123;
	public static final int SPORT_LEISURECENTER = 124;
	public static final int SPORT_MINIATURGOLF = 125;
	public static final int SPORT_MOTORRACING = 126;
	public static final int SPORT_SHOOTING = 127;
	public static final int SPORT_SKATING = 128;
	public static final int SPORT_SKIINGDOWNHILL = 129;
	public static final int SPORT_SNOOKER = 130;
	public static final int SPORT_SOCCER = 131;
	public static final int SPORT_STADIUM = 132;
	public static final int SPORT_SWIMMING = 133;
	public static final int SPORT_TENNIS = 134;
	public static final int SPORT_WATERSKI = 135;
	public static final int SPORT_SURFING = 136;
	
	public static final int TOURIST_ARCHAELOGICAL = 137;
	public static final int TOURIST_ART = 138;
	public static final int TOURIST_ATTRACTION = 139;
	public static final int TOURIST_BATTLEFIELD = 140;
	public static final int TOURIST_BEACH = 141;
	public static final int TOURIST_CASTLE = 142;
	public static final int TOURIST_CASTLE2 = 143;
	public static final int TOURIST_CINEMA = 144;
	public static final int TOURIST_FOUNTAIN = 145;
	public static final int TOURIST_INFORMATION = 146;
	public static final int TOURIST_MEMORIAL = 147;
	public static final int TOURIST_MONUMENT = 148;
	public static final int TOURIST_MUSEUM = 149;
	public static final int TOURIST_NIGHTCLUB = 150;
	public static final int TOURIST_RUINS = 151;
	public static final int TOURIST_THEATRE = 152;
	public static final int TOURIST_THEMEPARK = 153;
	public static final int TOURIST_WINDMILL = 156;
	public static final int TOURIST_WRECK = 157;
	public static final int TOURIST_ZOO = 158;

	public static final int TRANSPORT_TERMINAL = 159;
	public static final int TRANSPORT_AIRPORT = 160;
	public static final int TRANSPORT_BUSSTOP = 161;
	public static final int TRANSPORT_FUEL = 162;
	public static final int TRANSPORT_LIGHTHOUSE = 163;
	public static final int TRANSPORT_MARINA = 164;
	public static final int TRANSPORT_RENTALCAR = 165;
	public static final int TRANSPORT_SUBWAY = 166;
	public static final int TRANSPORT_STATION = 167;
	public static final int TRANSPORT_TRAMSTOP = 168;

	public static final int WATER_DAM = 169;
	public static final int WATER_TOWER = 170;
	public static final int WATER_WEIR = 171;
}
