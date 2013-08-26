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

package de.morbz.osmpoispbf.tags;

import java.util.Collection;

import org.openstreetmap.osmosis.core.domain.v0_6.Tag;

import de.morbz.osmpoispbf.Poi;

public class TagChecker {
	//get poi object or null if no correct tags
	public static Poi getPoi(Collection<Tag> tags) {
		//has at least two tags (name and kat)
		if(tags.size() < 2) {
			return null;
		}
		
		//check kat, name
		String name = null;
		String k;
		int kat = 0;
		for(Tag tag : tags) {
			if(tag.getKey().equals("highway") && tag.getValue().equals("pedestrian")) {
				System.out.print("");
			}
			
			
			k = tag.getKey();
			//name
			if(k.equals("name")) {
				name = tag.getValue();
				if(kat > 0) {
					return createPoi(name, kat);
				} else if(kat < 0) {
					break;
				}
			} else {
				//kat
				if(kat == 0) {
					kat = getKat(k, tag.getValue());
					if(name != null) {
						if(kat > 0) {
							return createPoi(name, kat);
						} else if(kat < 0) {
							break;
						}
					}
				}
			}
		}
		
		//check for subkat
		if(name != null && kat < 0) {
			kat = getSubKat(tags, kat);
			if(kat > 0) {
				return createPoi(name, kat);
			}
		}
		return null;
	}
	
	//returns sub-kat or 0
	private static int getSubKat(Collection<Tag> tags, int superkat) {
		String k, v;
		
		//forest types
		if(superkat == Tags.SUPER_LANDUSE_FOREST || superkat == Tags.SUPER_NATURAL_WOOD) {
			for(Tag tag : tags) {
				if(tag.getKey().equals("wood")) {
					v = tag.getValue();
					if(v.equals("coniferous")) { return Tags.LANDUSE_CONIFEROUS; }
					if(v.equals("deciduous")) { return Tags.LANDUSE_DECIDUOUS; }
					return Tags.LANDUSE_CONIFEROUSDECIDUOUS;
				}
			}
			return Tags.LANDUSE_CONIFEROUSDECIDUOUS;
		}
		
		//tower types
		if(superkat == Tags.SUPER_MAN_MADE_TOWER) {
			for(Tag tag : tags) {
				if(tag.getKey().equals("tower:type")) {
					v = tag.getValue();
					if(v.equals("communication")) { return Tags.POI_TOWERCOMMUNICATION; }
					return Tags.POI_TOWERLOOKOUT;
				}
			}
			return Tags.POI_TOWERLOOKOUT;
		}
		
		//churches/religions
		if(superkat == Tags.SUPER_PLACE_OF_WORSHIP) {
			for(Tag tag : tags) {
				if(tag.getKey().equals("religion")) {
					v = tag.getValue();
					if(v.equals("bahai")) { return Tags.POW_BAHAI; }
					if(v.equals("buddhist")) { return Tags.POW_BUDDHIST; }
					if(v.equals("christian")) { return Tags.POW_CHRISTIAN; }
					if(v.equals("hindu")) { return Tags.POW_HINDU; }
					if(v.equals("jain")) { return Tags.POW_JAIN; }
					if(v.equals("jewish")) { return Tags.POW_JEWISH; }
					if(v.equals("muslim")) { return Tags.POW_ISLAMIC; }
					if(v.equals("shinto")) { return Tags.POW_SHINTO; }
					if(v.equals("sikh")) { return Tags.POW_SIKH; }
					return Tags.POW_UNKOWN;
				}
			}
			return Tags.POW_UNKOWN;
		}
		
		//stop position types
		if(superkat == Tags.SUPER_PUBLIC_TRANSPORT_STOP_POSITION) {
			for(Tag tag : tags) {
				if(tag.getValue().equals("yes")) {
					k = tag.getKey();
					if(k.equals("train")) { return Tags.TRANSPORT_STATION; }
					if(k.equals("subway")) { return Tags.TRANSPORT_SUBWAY; }
					if(k.equals("monorail")) { return Tags.TRANSPORT_STATION; }
					if(k.equals("tram")) { return Tags.TRANSPORT_TRAMSTOP; }
					if(k.equals("bus")) { return Tags.TRANSPORT_BUSSTOP; }
				}
			}
			return Tags.TRANSPORT_BUSSTOP;
		}
		
		//railway station train/subway
		if(superkat == Tags.SUPER_RAILWAY_STATION) {
			for(Tag tag : tags) {
				if(tag.getKey().equals("station")) {
					if(tag.getValue().equals("subway")) { return Tags.TRANSPORT_SUBWAY; }
					return Tags.TRANSPORT_STATION;
				}
			}
			return Tags.TRANSPORT_STATION;
		}
		
		//information offices only
		if(superkat == Tags.SUPER_TOURISM_INFORMATION) {
			for(Tag tag : tags) {
				if(tag.getKey().equals("information")) {
					v = tag.getValue();
					if(v.equals("office")) { return Tags.TOURIST_INFORMATION; }
					if(v.equals("terminal")) { return Tags.TOURIST_INFORMATION; }
					return 0;
				}
			}
			return 0;
		}
		
		return 0;
	}
	
	//returns kat or 0
	private static int getKat(String k, String v) {
		k = k.trim().toLowerCase();
		v = v.trim().toLowerCase();
		
		if(k.equals("amenity")) {
			if(v.equals("bar")) { return Tags.FOOD_BAR; }
			if(v.equals("biergarten")) { return Tags.FOOD_BIERGARTEN; }
			if(v.equals("cafe")) { return Tags.FOOD_CAFE; }
			if(v.equals("fast_food")) { return Tags.FOOD_FASTFOOD; }
			if(v.equals("food_court")) { return Tags.FOOD_FASTFOOD; }
			if(v.equals("ice_cream")) { return Tags.FOOD_ICECREAM; }
			if(v.equals("pub")) { return Tags.FOOD_PUB; }
			if(v.equals("restaurant")) { return Tags.FOOD_RESTAURANT; }
			
			if(v.equals("college")) { return Tags.EDUCATION_COLLEGE; }
			if(v.equals("kindergarten")) { return Tags.EDUCATION_NURSERY; }
			if(v.equals("school")) { return Tags.EDUCATION_SCHOOL; }
			if(v.equals("university")) { return Tags.EDUCATION_UNIVERSITY; }
			
			if(v.equals("clinic")) { return Tags.HEALTH_HOSPITAL; }
			if(v.equals("dentist")) { return Tags.HEALTH_DENTIST; }
			if(v.equals("doctors")) { return Tags.HEALTH_DOCTORS; }
			if(v.equals("hospital")) { return Tags.HEALTH_HOSPITAL; }
			if(v.equals("pharmacy")) { return Tags.HEALTH_PHARMACY; }
			if(v.equals("veterinary")) { return Tags.HEALTH_VETERINARY; }
			
			if(v.equals("arts_centre")) { return Tags.TOURIST_ART; }
			if(v.equals("cinema")) { return Tags.TOURIST_CINEMA; }
			if(v.equals("community_centre")) { return Tags.TOURIST_THEATRE; }
			if(v.equals("theatre")) { return Tags.TOURIST_THEATRE; }
			if(v.equals("nightclub")) { return Tags.TOURIST_NIGHTCLUB; }
			if(v.equals("stripclub")) { return Tags.TOURIST_NIGHTCLUB; }
			
			if(v.equals("courthouse")) { return Tags.AMENITY_COURT; }
			if(v.equals("fire_station")) { return Tags.AMENITY_FIRESTATION; }
			if(v.equals("police")) { return Tags.AMENITY_POLICE; }
			if(v.equals("post_office")) { return Tags.AMENITY_POSTOFFICE; }
			if(v.equals("prison")) { return Tags.AMENITY_PRISON; }
			if(v.equals("townhall")) { return Tags.AMENITY_TOWNHALL; }
			if(v.equals("library")) { return Tags.AMENITY_LIBRARY; }
			if(v.equals("social_centre")) { return Tags.AMENITY_PUBLICBUILDING; }
			if(v.equals("nursing_home")) { return Tags.AMENITY_PUBLICBUILDING; }
			if(v.equals("social_facility")) { return Tags.AMENITY_PUBLICBUILDING; }
			if(v.equals("public_building")) { return Tags.AMENITY_PUBLICBUILDING; }
			
			if(v.equals("grave_yard")) { return Tags.BARRIER_BLOCKS; }
			
			if(v.equals("bicycle_rental")) { return Tags.SHOP_BICYCLE; }
			if(v.equals("marketplace")) { return Tags.SHOP_MARKETPLACE; }
			
			if(v.equals("fuel")) { return Tags.TRANSPORT_FUEL; }
			if(v.equals("bus_station")) { return Tags.TRANSPORT_BUSSTOP; }
			if(v.equals("car_rental")) { return Tags.TRANSPORT_RENTALCAR; }
			if(v.equals("car_wash")) { return Tags.TRANSPORT_RENTALCAR; }
			
			if(v.equals("bank")) { return Tags.MONEY_BANK; }
			if(v.equals("bureau_de_change")) { return Tags.MONEY_EXCHANGE; }
			
			if(v.equals("place_of_worship")) { return Tags.SUPER_PLACE_OF_WORSHIP; }
			
			if(v.equals("embassy")) { return Tags.POI_EMBASSY; }
			return 0;
		}
		
		if(k.equals("geological")) {
			if(v.equals("palaeontological_site")) { return Tags.TOURIST_ARCHAELOGICAL; }
			if(v.equals("outcrop")) { return Tags.LANDUSE_HILLS; }
			return 0;
		}
		
		if(k.equals("highway")) {
			if(v.equals("rest_area")) { return Tags.TRANSPORT_FUEL; }
			if(v.equals("bus_stop")) { return Tags.TRANSPORT_BUSSTOP; }
			if(v.equals("pedestrian")) { return Tags.TOURIST_FOUNTAIN; }
			if(v.equals("services")) { return Tags.TRANSPORT_FUEL; }
			return 0;
		}
		
		if(k.equals("historic")) {
			if(v.equals("archaeological_site")) { return Tags.TOURIST_ARCHAELOGICAL; }
			if(v.equals("battlefield")) { return Tags.TOURIST_BATTLEFIELD; }
			if(v.equals("castle")) { return Tags.TOURIST_CASTLE2; }
			if(v.equals("city_gate")) { return Tags.TOURIST_CASTLE; }
			if(v.equals("fort")) { return Tags.TOURIST_CASTLE2; }
			if(v.equals("memorial")) { return Tags.TOURIST_MEMORIAL; }
			if(v.equals("monument")) { return Tags.TOURIST_MONUMENT; }
			if(v.equals("ruins")) { return Tags.TOURIST_RUINS; }
			if(v.equals("rune_stone")) { return Tags.TOURIST_MEMORIAL; }
			if(v.equals("wreck")) { return Tags.TOURIST_WRECK; }
			return 0;
		}
		
		if(k.equals("man_made")) {
			if(v.equals("tower")) { return Tags.SUPER_MAN_MADE_TOWER; }
			if(v.equals("water_tower")) { return Tags.WATER_TOWER; }
			if(v.equals("watermill")) { return Tags.TOURIST_WINDMILL; }
			if(v.equals("windmill")) { return Tags.TOURIST_WINDMILL; }
			if(v.equals("lighthouse")) { return Tags.TRANSPORT_LIGHTHOUSE; }
			if(v.equals("mineshaft")) { return Tags.POI_MINE; }
			if(v.equals("water_works")) { return Tags.WATER_WEIR; }
			return 0;
		}
		
		if(k.equals("military")) {
			if(v.equals("bunker")) { return Tags.POI_BUNKER; }
			if(v.equals("airfield")) { return Tags.TRANSPORT_AIRPORT; }
			if(v.equals("danger_area")) { return Tags.LANDUSE_MILITARY; }
			if(v.equals("naval_base")) { return Tags.TRANSPORT_MARINA; }
			if(v.equals("range")) { return Tags.LANDUSE_MILITARY; }
			return 0;
		}
		
		if(k.equals("place")) {
			if(v.equals("city")) { return Tags.POI_CITY; }
			if(v.equals("town")) { return Tags.POI_TOWN; }
			if(v.equals("village")) { return Tags.POI_VILLAGE; }
			if(v.equals("hamlet")) { return Tags.POI_HAMLET; }
			if(v.equals("isolated_dwelling")) { return Tags.POI_HAMLET; }
			if(v.equals("farm")) { return Tags.POI_HAMLET; }
			if(v.equals("suburb")) { return Tags.POI_SUBURB; }
			if(v.equals("neighbourhood")) { return Tags.POI_HAMLET; }
			return 0;
		}
		
		if(k.equals("public_transport")) {
			if(v.equals("stop_position")) { return Tags.SUPER_PUBLIC_TRANSPORT_STOP_POSITION; }
			return 0;
		}
		
		if(k.equals("railway")) {
			if(v.equals("station")) { return Tags.SUPER_RAILWAY_STATION; }
			if(v.equals("tram_stop")) { return Tags.TRANSPORT_TRAMSTOP; }
			return 0;
		}
		
		if(k.equals("shop")) {
			if(v.equals("alcohol")) { return Tags.SHOP_ALCOHOL; }
			if(v.equals("appliance")) { return Tags.SHOP_LAUNDRETTE; }
			if(v.equals("baby_goods")) { return Tags.SHOP_TOYS; }
			if(v.equals("bakery")) { return Tags.SHOP_BAKERY; }
			if(v.equals("beauty")) { return Tags.SHOP_HAIRDRESSER; }
			if(v.equals("beverages")) { return Tags.SHOP_ALCOHOL; }
			if(v.equals("bicycle")) { return Tags.SHOP_BICYCLE; }
			if(v.equals("books")) { return Tags.SHOP_BOOK; }
			if(v.equals("boutique")) { return Tags.SHOP_CLOTHES; }
			if(v.equals("butcher")) { return Tags.SHOP_BUTCHER; }
			if(v.equals("car")) { return Tags.SHOP_CAR; }
			if(v.equals("car_repair")) { return Tags.SHOP_CARREPAIR; }
			if(v.equals("car_parts")) { return Tags.SHOP_CARREPAIR; }
			if(v.equals("convenience")) { return Tags.SHOP_CONVENIENCE; }
			if(v.equals("chemist")) { return Tags.SHOP_CONVENIENCE; }
			if(v.equals("clothes")) { return Tags.SHOP_CLOTHES; }
			if(v.equals("computer")) { return Tags.SHOP_COMPUTER; }
			if(v.equals("confectionery")) { return Tags.SHOP_CONFECTIONERY; }
			if(v.equals("convenience")) { return Tags.SHOP_CONVENIENCE; }
			if(v.equals("copyshop")) { return Tags.SHOP_COPYSHOP; }
			if(v.equals("curtain")) { return Tags.SHOP_CLOTHES; }
			if(v.equals("deli")) { return Tags.SHOP_CONFECTIONERY; }
			if(v.equals("department_store")) { return Tags.SHOP_SUPERMARKET; }
			if(v.equals("dive")) { return Tags.SHOP_FISH; }
			if(v.equals("dry_cleaning")) { return Tags.SHOP_LAUNDRETTE; }
			if(v.equals("doityourself")) { return Tags.SHOP_DIY; }
			if(v.equals("drugstore")) { return Tags.SHOP_CONVENIENCE; }
			if(v.equals("electronics")) { return Tags.SHOP_HIFI; }
			if(v.equals("fabric")) { return Tags.SHOP_CLOTHES; }
			if(v.equals("farm")) { return Tags.SHOP_DIY; }
			if(v.equals("florist")) { return Tags.SHOP_FLORIST; }
			if(v.equals("garden_centre")) { return Tags.SHOP_GARDENCENTRE; }
			if(v.equals("gas")) { return Tags.SHOP_DIY; }
			if(v.equals("general")) { return Tags.SHOP_CONVENIENCE; }
			if(v.equals("gift")) { return Tags.SHOP_GIFT; }
			if(v.equals("glaziery")) { return Tags.SHOP_DIY; }
			if(v.equals("greengrocer")) { return Tags.SHOP_GREENGROCER; }
			if(v.equals("hairdresser")) { return Tags.SHOP_HAIRDRESSER; }
			if(v.equals("hardware")) { return Tags.SHOP_DIY; }
			if(v.equals("hearing_aids")) { return Tags.SHOP_HEARINGAIDS; }
			if(v.equals("herbalist")) { return Tags.SHOP_FLORIST; }
			if(v.equals("hifi")) { return Tags.SHOP_HIFI; }
			if(v.equals("hunting")) { return Tags.SHOP_DIY; }
			if(v.equals("jewelry")) { return Tags.SHOP_JEWELRY; }
			if(v.equals("kiosk")) { return Tags.SHOP_KIOSK; }
			if(v.equals("kitchen")) { return Tags.SHOP_LAUNDRETTE; }	
			if(v.equals("laundry")) { return Tags.SHOP_LAUNDRETTE; }
			if(v.equals("mall")) { return Tags.SHOP_SUPERMARKET; }
			if(v.equals("mobile_phone")) { return Tags.SHOP_PHONE; }
			if(v.equals("money_lender")) { return Tags.SHOP_VENDINGMASCHINE; }
			if(v.equals("motorcycle")) { return Tags.SHOP_MOTORCYCLE; }
			if(v.equals("musical_instrument")) { return Tags.SHOP_MUSIC; }
			if(v.equals("newsagent")) { return Tags.SHOP_NEWSPAPER; }
			if(v.equals("organic")) { return Tags.SHOP_CONVENIENCE; }
			if(v.equals("outdoor")) { return Tags.SHOP_DIY; }
			if(v.equals("pawnbroker")) { return Tags.SHOP_VENDINGMASCHINE; }
			if(v.equals("pet")) { return Tags.SHOP_PET; }
			if(v.equals("radiotechnics")) { return Tags.SHOP_HIFI; }
			if(v.equals("seafood")) { return Tags.SHOP_FISH; }
			if(v.equals("fish")) { return Tags.SHOP_FISH; }
			if(v.equals("second_hand")) { return Tags.SHOP_CLOTHES; }
			if(v.equals("shoes")) { return Tags.SHOP_SHOES; }
			if(v.equals("sports")) { return Tags.SHOP_MOTORCYCLE; }
			if(v.equals("stationery")) { return Tags.SHOP_COPYSHOP; }
			if(v.equals("supermarket")) { return Tags.SHOP_SUPERMARKET; }
			if(v.equals("ticket")) { return Tags.SHOP_KIOSK; }
			if(v.equals("tobacco")) { return Tags.SHOP_TOBACCO; }
			if(v.equals("toys")) { return Tags.SHOP_TOYS; }
			if(v.equals("trade")) { return Tags.SHOP_DIY; }
			if(v.equals("vacuum_cleaner")) { return Tags.SHOP_LAUNDRETTE; }
			if(v.equals("variety_store")) { return Tags.SHOP_CONVENIENCE; }
			if(v.equals("video")) { return Tags.SHOP_VIDEORENTAL; }
			if(v.equals("window_blind")) { return Tags.SHOP_DIY; }
			return Tags.SHOP_DEPARTMENTSTORE;
		}
		
		if(k.equals("sport")) {
			if(v.equals("9pin")) { return Tags.SPORT_BOWLING; }
			if(v.equals("10pin")) { return Tags.SPORT_BOWLING; }
			if(v.equals("american_football")) { return Tags.SPORT_FOOTBALL; }
			if(v.equals("archery")) { return Tags.SPORT_ARCHERY; }
			if(v.equals("athletics")) { return Tags.SPORT_GYMNASIUM; }
			if(v.equals("australian_football")) { return Tags.SPORT_FOOTBALL; }
			if(v.equals("badminton")) { return Tags.SPORT_TENNIS; }
			if(v.equals("baseball")) { return Tags.SPORT_BASEBALL; }
			if(v.equals("basketball")) { return Tags.SPORT_BASKETBALL; }
			if(v.equals("beachvolleyball")) { return Tags.SPORT_BASKETBALL; }
			if(v.equals("boules")) { return Tags.SPORT_SNOOKER; }
			if(v.equals("bowls")) { return Tags.SPORT_BOWLING; }
			if(v.equals("canadian_football")) { return Tags.SPORT_FOOTBALL; }
			if(v.equals("canoe")) { return Tags.SPORT_CANOE; }
			if(v.equals("climbing")) { return Tags.SPORT_CLIMBING; }
			if(v.equals("cricket")) { return Tags.SPORT_CRICKET; }
			if(v.equals("cricket_nets")) { return Tags.SPORT_CRICKET; }
			if(v.equals("croquet")) { return Tags.SPORT_CRICKET; }
			if(v.equals("diving")) { return Tags.SPORT_DIVING; }
			if(v.equals("equestrian")) { return Tags.SPORT_HORSE; }
			if(v.equals("football")) { return Tags.SPORT_FOOTBALL; }
			if(v.equals("gaelic_games")) { return Tags.SPORT_CRICKET; }
			if(v.equals("golf")) { return Tags.SPORT_GOLF; }
			if(v.equals("gymnastics")) { return Tags.SPORT_GYM; }
			if(v.equals("hockey")) { return Tags.SPORT_BASEBALL; }
			if(v.equals("horseshoes")) { return Tags.SPORT_HORSE; }
			if(v.equals("horse_racing")) { return Tags.SPORT_HORSE; }
			if(v.equals("ice_stock")) { return Tags.SPORT_SNOOKER; }
			if(v.equals("karting")) { return Tags.SPORT_MOTORRACING; }
			if(v.equals("korfball")) { return Tags.SPORT_BASKETBALL; }
			if(v.equals("motor")) { return Tags.SPORT_MOTORRACING; }
			if(v.equals("paddle_tennis")) { return Tags.SPORT_TENNIS; }
			if(v.equals("pelota")) { return Tags.SPORT_BASEBALL; }
			if(v.equals("racquet")) { return Tags.SPORT_CRICKET; }
			if(v.equals("rowing")) { return Tags.SPORT_CANOE; }
			if(v.equals("rugby_league")) { return Tags.SPORT_FOOTBALL; }
			if(v.equals("rugby_union")) { return Tags.SPORT_FOOTBALL; }
			if(v.equals("shooting")) { return Tags.SPORT_SHOOTING; }
			if(v.equals("skating")) { return Tags.SPORT_ICESKATING; }
			if(v.equals("skateboard")) { return Tags.SPORT_SKATING; }
			if(v.equals("skiing")) { return Tags.SPORT_SKIINGDOWNHILL; }
			if(v.equals("soccer")) { return Tags.SPORT_SOCCER; }
			if(v.equals("surfing")) { return Tags.SPORT_SURFING; }
			if(v.equals("swimming")) { return Tags.SPORT_SWIMMING; }
			if(v.equals("table_tennis")) { return Tags.SPORT_TENNIS; }
			if(v.equals("team_handball")) { return Tags.SPORT_BASKETBALL; }
			if(v.equals("tennis")) { return Tags.SPORT_TENNIS; }
			if(v.equals("volleyball")) { return Tags.SPORT_BASKETBALL; }
			if(v.equals("water_ski")) { return Tags.SPORT_WATERSKI; }
			return 0;
		}
		
		if(k.equals("tourism")) {
			if(v.equals("alpine_hut")) { return Tags.ACCOMMO_ALPINEHUT; }
			if(v.equals("attraction")) { return Tags.TOURIST_ATTRACTION; }
			if(v.equals("artwork")) { return Tags.TOURIST_ART; }
			if(v.equals("camp_site")) { return Tags.ACCOMMO_CAMPING; }
			if(v.equals("caravan_site")) { return Tags.ACCOMMO_CARAVAN; }
			if(v.equals("chalet")) { return Tags.ACCOMMO_CHALET; }
			if(v.equals("guest_house")) { return Tags.ACCOMMO_HOTEL; }
			if(v.equals("hostel")) { return Tags.ACCOMMO_HOSTEL; }
			if(v.equals("hotel")) { return Tags.ACCOMMO_HOTEL; }
			if(v.equals("information")) { return Tags.SUPER_TOURISM_INFORMATION; }
			if(v.equals("motel")) { return Tags.ACCOMMO_MOTEL; }
			if(v.equals("museum")) { return Tags.TOURIST_MUSEUM; }
			if(v.equals("theme_park")) { return Tags.TOURIST_THEMEPARK; }
			if(v.equals("zoo")) { return Tags.TOURIST_ZOO; }
			return 0;
		}
		
		if(k.equals("aeroway")) {
			if(v.equals("aerodrome")) { return Tags.TRANSPORT_AIRPORT; }
			if(v.equals("terminal")) { return Tags.TRANSPORT_TERMINAL; }
			return 0;
		}
		
		if(k.equals("emergency")) {
			if(v.equals("ambulance_station")) { return Tags.HEALTH_HOSPITALEMERGENCY; }
			if(v.equals("ses_station")) { return Tags.HEALTH_HOSPITALEMERGENCY; }
			return 0;
		}
		
		if(k.equals("waterway")) {
			if(v.equals("riverbank")) { return Tags.WATER_WEIR; }
			if(v.equals("dam")) { return Tags.WATER_DAM; }
			return 0;
		}
		
		if(k.equals("leisure")) {
			if(v.equals("common")) { return Tags.LANDUSE_GRASS; }
			if(v.equals("dance")) { return Tags.TOURIST_NIGHTCLUB; }
			if(v.equals("dog_park")) { return Tags.LANDUSE_GRASS; }
			if(v.equals("garden")) { return Tags.SHOP_FLORIST; }
			if(v.equals("golf_course")) { return Tags.SPORT_GOLF; }
			if(v.equals("ice_rink")) { return Tags.SPORT_ICESKATING; }
			if(v.equals("marina")) { return Tags.TRANSPORT_MARINA; }
			if(v.equals("miniature_golf")) { return Tags.SPORT_MINIATURGOLF; }
			if(v.equals("nature_reserve")) { return Tags.LANDUSE_GRASS; }
			if(v.equals("park")) { return Tags.LANDUSE_GRASS; }
			if(v.equals("pitch")) { return Tags.SPORT_STADIUM; }
			if(v.equals("playground")) { return Tags.AMENITY_PLAYGROUND; }
			if(v.equals("sports_centre")) { return Tags.SPORT_LEISURECENTER; }
			if(v.equals("stadium")) { return Tags.SPORT_STADIUM; }
			if(v.equals("water_park")) { return Tags.SPORT_SWIMMING; }
			return 0;
		}
			
		if(k.equals("building")) {
			if(v.equals("hotel")) { return Tags.ACCOMMO_HOTEL; }
			if(v.equals("school")) { return Tags.EDUCATION_SCHOOL; }
			if(v.equals("supermarket")) { return Tags.SHOP_CONVENIENCE; }
			if(v.equals("train_station")) { return Tags.TRANSPORT_STATION; }
			if(v.equals("university")) { return Tags.EDUCATION_UNIVERSITY; }
			return 0;
		}
		
		if(k.equals("landuse")) {
			if(v.equals("allotments")) { return Tags.LANDUSE_ALLOTMENTS; }
			if(v.equals("brownfield")) { return Tags.LANDUSE_QUARY; }
			if(v.equals("cemetery")) { return Tags.BARRIER_BLOCKS; }
			if(v.equals("farmland")) { return Tags.LANDUSE_GRASS; }
			if(v.equals("farm")) { return Tags.LANDUSE_GRASS; }
			if(v.equals("forest")) { return Tags.SUPER_LANDUSE_FOREST; }
			if(v.equals("grass")) { return Tags.LANDUSE_GRASS; }
			if(v.equals("greenfield")) { return Tags.LANDUSE_GRASS; }
			if(v.equals("meadow")) { return Tags.LANDUSE_GRASS; }
			if(v.equals("greenhouse_horticulture")) { return Tags.LANDUSE_GRASS; }
			if(v.equals("industrial")) { return Tags.POI_CRANE; }
			if(v.equals("landfill")) { return Tags.LANDUSE_HILLS; }
			if(v.equals("military")) { return Tags.LANDUSE_MILITARY; }
			if(v.equals("orchard")) { return Tags.LANDUSE_SCRUB; }
			if(v.equals("plant_nursery")) { return Tags.LANDUSE_GRASS; }
			if(v.equals("quarry")) { return Tags.LANDUSE_QUARY; }
			if(v.equals("recreation_ground")) { return Tags.LANDUSE_GRASS; }
			if(v.equals("village_green")) { return Tags.LANDUSE_GRASS; }
			if(v.equals("vineyard")) { return Tags.LANDUSE_GRASS; }
			if(v.equals("retail")) { return Tags.SHOP_VENDINGMASCHINE; }
			return 0;
		}
		
		if(k.equals("natural")) {
			if(v.equals("bay")) { return Tags.TOURIST_BEACH; }
			if(v.equals("beach")) { return Tags.TOURIST_BEACH; }
			if(v.equals("cave_entrance")) { return Tags.POI_CAVE; }
			if(v.equals("cliff")) { return Tags.POI_PEAK; }
			if(v.equals("glacier")) { return Tags.POI_PEAK; }
			if(v.equals("peak")) { return Tags.POI_PEAK; }
			if(v.equals("spring")) { return Tags.WATER_WEIR; }
			if(v.equals("stone")) { return Tags.BARRIER_BLOCKS; }
			if(v.equals("volcano")) { return Tags.POI_PEAK1; }
			if(v.equals("fell")) { return Tags.LANDUSE_GRASS; }
			if(v.equals("grassland")) { return Tags.LANDUSE_GRASS; }
			
			if(v.equals("heath")) { return Tags.LANDUSE_SCRUB; }
			if(v.equals("marsh")) { return Tags.LANDUSE_SWAMP; }
			if(v.equals("wetland")) { return Tags.LANDUSE_SWAMP; }
			if(v.equals("mud")) { return Tags.LANDUSE_SWAMP; }
			if(v.equals("sand")) { return Tags.TOURIST_BEACH; }
			if(v.equals("scree")) { return Tags.LANDUSE_QUARY; }
			if(v.equals("scrub")) { return Tags.LANDUSE_SCRUB; }
			if(v.equals("spring")) { return Tags.WATER_WEIR; }
			if(v.equals("wood")) { return Tags.SUPER_NATURAL_WOOD; }
			return 0;
		}
		return 0;
	}
	
	//creates poi object
	private static Poi createPoi(String name, int kat) {
		Poi poi = new Poi();
		poi.name = name;
		poi.kat = kat;
		return poi;
	}
}
