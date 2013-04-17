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

import java.util.Collection;

import org.openstreetmap.osmosis.core.domain.v0_6.Tag;

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
		if(superkat == Const.SUPER_LANDUSE_FOREST || superkat == Const.SUPER_NATURAL_WOOD) {
			for(Tag tag : tags) {
				if(tag.getKey().equals("wood")) {
					v = tag.getValue();
					if(v.equals("coniferous")) { return Const.LANDUSE_CONIFEROUS; }
					if(v.equals("deciduous")) { return Const.LANDUSE_DECIDUOUS; }
					return Const.LANDUSE_CONIFEROUSDECIDUOUS;
				}
			}
			return Const.LANDUSE_CONIFEROUSDECIDUOUS;
		}
		
		//tower types
		if(superkat == Const.SUPER_MAN_MADE_TOWER) {
			for(Tag tag : tags) {
				if(tag.getKey().equals("tower:type")) {
					v = tag.getValue();
					if(v.equals("communication")) { return Const.POI_TOWERCOMMUNICATION; }
					return Const.POI_TOWERLOOKOUT;
				}
			}
			return Const.POI_TOWERLOOKOUT;
		}
		
		//churches/religions
		if(superkat == Const.SUPER_PLACE_OF_WORSHIP) {
			for(Tag tag : tags) {
				if(tag.getKey().equals("religion")) {
					v = tag.getValue();
					if(v.equals("bahai")) { return Const.POW_BAHAI; }
					if(v.equals("buddhist")) { return Const.POW_BUDDHIST; }
					if(v.equals("christian")) { return Const.POW_CHRISTIAN; }
					if(v.equals("hindu")) { return Const.POW_HINDU; }
					if(v.equals("jain")) { return Const.POW_JAIN; }
					if(v.equals("jewish")) { return Const.POW_JEWISH; }
					if(v.equals("muslim")) { return Const.POW_ISLAMIC; }
					if(v.equals("shinto")) { return Const.POW_SHINTO; }
					if(v.equals("sikh")) { return Const.POW_SIKH; }
					return Const.POW_UNKOWN;
				}
			}
			return Const.POW_UNKOWN;
		}
		
		//stop position types
		if(superkat == Const.SUPER_PUBLIC_TRANSPORT_STOP_POSITION) {
			for(Tag tag : tags) {
				if(tag.getValue().equals("yes")) {
					k = tag.getKey();
					if(k.equals("train")) { return Const.TRANSPORT_STATION; }
					if(k.equals("subway")) { return Const.TRANSPORT_SUBWAY; }
					if(k.equals("monorail")) { return Const.TRANSPORT_STATION; }
					if(k.equals("tram")) { return Const.TRANSPORT_TRAMSTOP; }
					if(k.equals("bus")) { return Const.TRANSPORT_BUSSTOP; }
				}
			}
			return Const.TRANSPORT_BUSSTOP;
		}
		
		//railway station train/subway
		if(superkat == Const.SUPER_RAILWAY_STATION) {
			for(Tag tag : tags) {
				if(tag.getKey().equals("station")) {
					if(tag.getValue().equals("subway")) { return Const.TRANSPORT_SUBWAY; }
					return Const.TRANSPORT_STATION;
				}
			}
			return Const.TRANSPORT_STATION;
		}
		
		//information offices only
		if(superkat == Const.SUPER_TOURISM_INFORMATION) {
			for(Tag tag : tags) {
				if(tag.getKey().equals("information")) {
					v = tag.getValue();
					if(v.equals("office")) { return Const.TOURIST_INFORMATION; }
					if(v.equals("terminal")) { return Const.TOURIST_INFORMATION; }
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
			if(v.equals("bar")) { return Const.FOOD_BAR; }
			if(v.equals("biergarten")) { return Const.FOOD_BIERGARTEN; }
			if(v.equals("cafe")) { return Const.FOOD_CAFE; }
			if(v.equals("fast_food")) { return Const.FOOD_FASTFOOD; }
			if(v.equals("food_court")) { return Const.FOOD_FASTFOOD; }
			if(v.equals("ice_cream")) { return Const.FOOD_ICECREAM; }
			if(v.equals("pub")) { return Const.FOOD_PUB; }
			if(v.equals("restaurant")) { return Const.FOOD_RESTAURANT; }
			
			if(v.equals("college")) { return Const.EDUCATION_COLLEGE; }
			if(v.equals("kindergarten")) { return Const.EDUCATION_NURSERY; }
			if(v.equals("school")) { return Const.EDUCATION_SCHOOL; }
			if(v.equals("university")) { return Const.EDUCATION_UNIVERSITY; }
			
			if(v.equals("clinic")) { return Const.HEALTH_HOSPITAL; }
			if(v.equals("dentist")) { return Const.HEALTH_DENTIST; }
			if(v.equals("doctors")) { return Const.HEALTH_DOCTORS; }
			if(v.equals("hospital")) { return Const.HEALTH_HOSPITAL; }
			if(v.equals("pharmacy")) { return Const.HEALTH_PHARMACY; }
			if(v.equals("veterinary")) { return Const.HEALTH_VETERINARY; }
			
			if(v.equals("arts_centre")) { return Const.TOURIST_ART; }
			if(v.equals("cinema")) { return Const.TOURIST_CINEMA; }
			if(v.equals("community_centre")) { return Const.TOURIST_THEATRE; }
			if(v.equals("theatre")) { return Const.TOURIST_THEATRE; }
			if(v.equals("nightclub")) { return Const.TOURIST_NIGHTCLUB; }
			if(v.equals("stripclub")) { return Const.TOURIST_NIGHTCLUB; }
			
			if(v.equals("courthouse")) { return Const.AMENITY_COURT; }
			if(v.equals("fire_station")) { return Const.AMENITY_FIRESTATION; }
			if(v.equals("police")) { return Const.AMENITY_POLICE; }
			if(v.equals("post_office")) { return Const.AMENITY_POSTOFFICE; }
			if(v.equals("prison")) { return Const.AMENITY_PRISON; }
			if(v.equals("townhall")) { return Const.AMENITY_TOWNHALL; }
			if(v.equals("library")) { return Const.AMENITY_LIBRARY; }
			if(v.equals("social_centre")) { return Const.AMENITY_PUBLICBUILDING; }
			if(v.equals("nursing_home")) { return Const.AMENITY_PUBLICBUILDING; }
			if(v.equals("social_facility")) { return Const.AMENITY_PUBLICBUILDING; }
			if(v.equals("public_building")) { return Const.AMENITY_PUBLICBUILDING; }
			
			if(v.equals("grave_yard")) { return Const.BARRIER_BLOCKS; }
			
			if(v.equals("bicycle_rental")) { return Const.SHOP_BICYCLE; }
			if(v.equals("marketplace")) { return Const.SHOP_MARKETPLACE; }
			
			if(v.equals("fuel")) { return Const.TRANSPORT_FUEL; }
			if(v.equals("bus_station")) { return Const.TRANSPORT_BUSSTOP; }
			if(v.equals("car_rental")) { return Const.TRANSPORT_RENTALCAR; }
			if(v.equals("car_wash")) { return Const.TRANSPORT_RENTALCAR; }
			
			if(v.equals("bank")) { return Const.MONEY_BANK; }
			if(v.equals("bureau_de_change")) { return Const.MONEY_EXCHANGE; }
			
			if(v.equals("place_of_worship")) { return Const.SUPER_PLACE_OF_WORSHIP; }
			
			if(v.equals("embassy")) { return Const.POI_EMBASSY; }
			return 0;
		}
		
		if(k.equals("geological")) {
			if(v.equals("palaeontological_site")) { return Const.TOURIST_ARCHAELOGICAL; }
			if(v.equals("outcrop")) { return Const.LANDUSE_HILLS; }
			return 0;
		}
		
		if(k.equals("highway")) {
			if(v.equals("rest_area")) { return Const.TRANSPORT_FUEL; }
			if(v.equals("bus_stop")) { return Const.TRANSPORT_BUSSTOP; }
			if(v.equals("pedestrian")) { return Const.TOURIST_FOUNTAIN; }
			if(v.equals("services")) { return Const.TRANSPORT_FUEL; }
			return 0;
		}
		
		if(k.equals("historic")) {
			if(v.equals("archaeological_site")) { return Const.TOURIST_ARCHAELOGICAL; }
			if(v.equals("battlefield")) { return Const.TOURIST_BATTLEFIELD; }
			if(v.equals("castle")) { return Const.TOURIST_CASTLE2; }
			if(v.equals("city_gate")) { return Const.TOURIST_CASTLE; }
			if(v.equals("fort")) { return Const.TOURIST_CASTLE2; }
			if(v.equals("memorial")) { return Const.TOURIST_MEMORIAL; }
			if(v.equals("monument")) { return Const.TOURIST_MONUMENT; }
			if(v.equals("ruins")) { return Const.TOURIST_RUINS; }
			if(v.equals("rune_stone")) { return Const.TOURIST_MEMORIAL; }
			if(v.equals("wreck")) { return Const.TOURIST_WRECK; }
			return 0;
		}
		
		if(k.equals("man_made")) {
			if(v.equals("tower")) { return Const.SUPER_MAN_MADE_TOWER; }
			if(v.equals("water_tower")) { return Const.WATER_TOWER; }
			if(v.equals("watermill")) { return Const.TOURIST_WINDMILL; }
			if(v.equals("windmill")) { return Const.TOURIST_WINDMILL; }
			if(v.equals("lighthouse")) { return Const.TRANSPORT_LIGHTHOUSE; }
			if(v.equals("mineshaft")) { return Const.POI_MINE; }
			if(v.equals("water_works")) { return Const.WATER_WEIR; }
			return 0;
		}
		
		if(k.equals("military")) {
			if(v.equals("bunker")) { return Const.POI_BUNKER; }
			if(v.equals("airfield")) { return Const.TRANSPORT_AIRPORT; }
			if(v.equals("danger_area")) { return Const.LANDUSE_MILITARY; }
			if(v.equals("naval_base")) { return Const.TRANSPORT_MARINA; }
			if(v.equals("range")) { return Const.LANDUSE_MILITARY; }
			return 0;
		}
		
		if(k.equals("place")) {
			if(v.equals("city")) { return Const.POI_CITY; }
			if(v.equals("town")) { return Const.POI_TOWN; }
			if(v.equals("village")) { return Const.POI_VILLAGE; }
			if(v.equals("hamlet")) { return Const.POI_HAMLET; }
			if(v.equals("isolated_dwelling")) { return Const.POI_HAMLET; }
			if(v.equals("farm")) { return Const.POI_HAMLET; }
			if(v.equals("suburb")) { return Const.POI_SUBURB; }
			if(v.equals("neighbourhood")) { return Const.POI_HAMLET; }
			return 0;
		}
		
		if(k.equals("public_transport")) {
			if(v.equals("stop_position")) { return Const.SUPER_PUBLIC_TRANSPORT_STOP_POSITION; }
			return 0;
		}
		
		if(k.equals("railway")) {
			if(v.equals("station")) { return Const.SUPER_RAILWAY_STATION; }
			if(v.equals("tram_stop")) { return Const.TRANSPORT_TRAMSTOP; }
			return 0;
		}
		
		if(k.equals("shop")) {
			if(v.equals("alcohol")) { return Const.SHOP_ALCOHOL; }
			if(v.equals("appliance")) { return Const.SHOP_LAUNDRETTE; }
			if(v.equals("baby_goods")) { return Const.SHOP_TOYS; }
			if(v.equals("bakery")) { return Const.SHOP_BAKERY; }
			if(v.equals("beauty")) { return Const.SHOP_HAIRDRESSER; }
			if(v.equals("beverages")) { return Const.SHOP_ALCOHOL; }
			if(v.equals("bicycle")) { return Const.SHOP_BICYCLE; }
			if(v.equals("books")) { return Const.SHOP_BOOK; }
			if(v.equals("boutique")) { return Const.SHOP_CLOTHES; }
			if(v.equals("butcher")) { return Const.SHOP_BUTCHER; }
			if(v.equals("car")) { return Const.SHOP_CAR; }
			if(v.equals("car_repair")) { return Const.SHOP_CARREPAIR; }
			if(v.equals("car_parts")) { return Const.SHOP_CARREPAIR; }
			if(v.equals("convenience")) { return Const.SHOP_CONVENIENCE; }
			if(v.equals("chemist")) { return Const.SHOP_CONVENIENCE; }
			if(v.equals("clothes")) { return Const.SHOP_CLOTHES; }
			if(v.equals("computer")) { return Const.SHOP_COMPUTER; }
			if(v.equals("confectionery")) { return Const.SHOP_CONFECTIONERY; }
			if(v.equals("convenience")) { return Const.SHOP_CONVENIENCE; }
			if(v.equals("copyshop")) { return Const.SHOP_COPYSHOP; }
			if(v.equals("curtain")) { return Const.SHOP_CLOTHES; }
			if(v.equals("deli")) { return Const.SHOP_CONFECTIONERY; }
			if(v.equals("department_store")) { return Const.SHOP_SUPERMARKET; }
			if(v.equals("dive")) { return Const.SHOP_FISH; }
			if(v.equals("dry_cleaning")) { return Const.SHOP_LAUNDRETTE; }
			if(v.equals("doityourself")) { return Const.SHOP_DIY; }
			if(v.equals("drugstore")) { return Const.SHOP_CONVENIENCE; }
			if(v.equals("electronics")) { return Const.SHOP_HIFI; }
			if(v.equals("fabric")) { return Const.SHOP_CLOTHES; }
			if(v.equals("farm")) { return Const.SHOP_DIY; }
			if(v.equals("florist")) { return Const.SHOP_FLORIST; }
			if(v.equals("garden_centre")) { return Const.SHOP_GARDENCENTRE; }
			if(v.equals("gas")) { return Const.SHOP_DIY; }
			if(v.equals("general")) { return Const.SHOP_CONVENIENCE; }
			if(v.equals("gift")) { return Const.SHOP_GIFT; }
			if(v.equals("glaziery")) { return Const.SHOP_DIY; }
			if(v.equals("greengrocer")) { return Const.SHOP_GREENGROCER; }
			if(v.equals("hairdresser")) { return Const.SHOP_HAIRDRESSER; }
			if(v.equals("hardware")) { return Const.SHOP_DIY; }
			if(v.equals("hearing_aids")) { return Const.SHOP_HEARINGAIDS; }
			if(v.equals("herbalist")) { return Const.SHOP_FLORIST; }
			if(v.equals("hifi")) { return Const.SHOP_HIFI; }
			if(v.equals("hunting")) { return Const.SHOP_DIY; }
			if(v.equals("jewelry")) { return Const.SHOP_JEWELRY; }
			if(v.equals("kiosk")) { return Const.SHOP_KIOSK; }
			if(v.equals("kitchen")) { return Const.SHOP_LAUNDRETTE; }	
			if(v.equals("laundry")) { return Const.SHOP_LAUNDRETTE; }
			if(v.equals("mall")) { return Const.SHOP_SUPERMARKET; }
			if(v.equals("mobile_phone")) { return Const.SHOP_PHONE; }
			if(v.equals("money_lender")) { return Const.SHOP_VENDINGMASCHINE; }
			if(v.equals("motorcycle")) { return Const.SHOP_MOTORCYCLE; }
			if(v.equals("musical_instrument")) { return Const.SHOP_MUSIC; }
			if(v.equals("newsagent")) { return Const.SHOP_NEWSPAPER; }
			if(v.equals("organic")) { return Const.SHOP_CONVENIENCE; }
			if(v.equals("outdoor")) { return Const.SHOP_DIY; }
			if(v.equals("pawnbroker")) { return Const.SHOP_VENDINGMASCHINE; }
			if(v.equals("pet")) { return Const.SHOP_PET; }
			if(v.equals("radiotechnics")) { return Const.SHOP_HIFI; }
			if(v.equals("seafood")) { return Const.SHOP_FISH; }
			if(v.equals("fish")) { return Const.SHOP_FISH; }
			if(v.equals("second_hand")) { return Const.SHOP_CLOTHES; }
			if(v.equals("shoes")) { return Const.SHOP_SHOES; }
			if(v.equals("sports")) { return Const.SHOP_MOTORCYCLE; }
			if(v.equals("stationery")) { return Const.SHOP_COPYSHOP; }
			if(v.equals("supermarket")) { return Const.SHOP_SUPERMARKET; }
			if(v.equals("ticket")) { return Const.SHOP_KIOSK; }
			if(v.equals("tobacco")) { return Const.SHOP_TOBACCO; }
			if(v.equals("toys")) { return Const.SHOP_TOYS; }
			if(v.equals("trade")) { return Const.SHOP_DIY; }
			if(v.equals("vacuum_cleaner")) { return Const.SHOP_LAUNDRETTE; }
			if(v.equals("variety_store")) { return Const.SHOP_CONVENIENCE; }
			if(v.equals("video")) { return Const.SHOP_VIDEORENTAL; }
			if(v.equals("window_blind")) { return Const.SHOP_DIY; }
			return Const.SHOP_DEPARTMENTSTORE;
		}
		
		if(k.equals("sport")) {
			if(v.equals("9pin")) { return Const.SPORT_BOWLING; }
			if(v.equals("10pin")) { return Const.SPORT_BOWLING; }
			if(v.equals("american_football")) { return Const.SPORT_FOOTBALL; }
			if(v.equals("archery")) { return Const.SPORT_ARCHERY; }
			if(v.equals("athletics")) { return Const.SPORT_GYMNASIUM; }
			if(v.equals("australian_football")) { return Const.SPORT_FOOTBALL; }
			if(v.equals("badminton")) { return Const.SPORT_TENNIS; }
			if(v.equals("baseball")) { return Const.SPORT_BASEBALL; }
			if(v.equals("basketball")) { return Const.SPORT_BASKETBALL; }
			if(v.equals("beachvolleyball")) { return Const.SPORT_BASKETBALL; }
			if(v.equals("boules")) { return Const.SPORT_SNOOKER; }
			if(v.equals("bowls")) { return Const.SPORT_BOWLING; }
			if(v.equals("canadian_football")) { return Const.SPORT_FOOTBALL; }
			if(v.equals("canoe")) { return Const.SPORT_CANOE; }
			if(v.equals("climbing")) { return Const.SPORT_CLIMBING; }
			if(v.equals("cricket")) { return Const.SPORT_CRICKET; }
			if(v.equals("cricket_nets")) { return Const.SPORT_CRICKET; }
			if(v.equals("croquet")) { return Const.SPORT_CRICKET; }
			if(v.equals("diving")) { return Const.SPORT_DIVING; }
			if(v.equals("equestrian")) { return Const.SPORT_HORSE; }
			if(v.equals("football")) { return Const.SPORT_FOOTBALL; }
			if(v.equals("gaelic_games")) { return Const.SPORT_CRICKET; }
			if(v.equals("golf")) { return Const.SPORT_GOLF; }
			if(v.equals("gymnastics")) { return Const.SPORT_GYM; }
			if(v.equals("hockey")) { return Const.SPORT_BASEBALL; }
			if(v.equals("horseshoes")) { return Const.SPORT_HORSE; }
			if(v.equals("horse_racing")) { return Const.SPORT_HORSE; }
			if(v.equals("ice_stock")) { return Const.SPORT_SNOOKER; }
			if(v.equals("karting")) { return Const.SPORT_MOTORRACING; }
			if(v.equals("korfball")) { return Const.SPORT_BASKETBALL; }
			if(v.equals("motor")) { return Const.SPORT_MOTORRACING; }
			if(v.equals("paddle_tennis")) { return Const.SPORT_TENNIS; }
			if(v.equals("pelota")) { return Const.SPORT_BASEBALL; }
			if(v.equals("racquet")) { return Const.SPORT_CRICKET; }
			if(v.equals("rowing")) { return Const.SPORT_CANOE; }
			if(v.equals("rugby_league")) { return Const.SPORT_FOOTBALL; }
			if(v.equals("rugby_union")) { return Const.SPORT_FOOTBALL; }
			if(v.equals("shooting")) { return Const.SPORT_SHOOTING; }
			if(v.equals("skating")) { return Const.SPORT_ICESKATING; }
			if(v.equals("skateboard")) { return Const.SPORT_SKATING; }
			if(v.equals("skiing")) { return Const.SPORT_SKIINGDOWNHILL; }
			if(v.equals("soccer")) { return Const.SPORT_SOCCER; }
			if(v.equals("surfing")) { return Const.SPORT_SURFING; }
			if(v.equals("swimming")) { return Const.SPORT_SWIMMING; }
			if(v.equals("table_tennis")) { return Const.SPORT_TENNIS; }
			if(v.equals("team_handball")) { return Const.SPORT_BASKETBALL; }
			if(v.equals("tennis")) { return Const.SPORT_TENNIS; }
			if(v.equals("volleyball")) { return Const.SPORT_BASKETBALL; }
			if(v.equals("water_ski")) { return Const.SPORT_WATERSKI; }
			return 0;
		}
		
		if(k.equals("tourism")) {
			if(v.equals("alpine_hut")) { return Const.ACCOMMO_ALPINEHUT; }
			if(v.equals("attraction")) { return Const.TOURIST_ATTRACTION; }
			if(v.equals("artwork")) { return Const.TOURIST_ART; }
			if(v.equals("camp_site")) { return Const.ACCOMMO_CAMPING; }
			if(v.equals("caravan_site")) { return Const.ACCOMMO_CARAVAN; }
			if(v.equals("chalet")) { return Const.ACCOMMO_CHALET; }
			if(v.equals("guest_house")) { return Const.ACCOMMO_HOTEL; }
			if(v.equals("hostel")) { return Const.ACCOMMO_HOSTEL; }
			if(v.equals("hotel")) { return Const.ACCOMMO_HOTEL; }
			if(v.equals("information")) { return Const.SUPER_TOURISM_INFORMATION; }
			if(v.equals("motel")) { return Const.ACCOMMO_MOTEL; }
			if(v.equals("museum")) { return Const.TOURIST_MUSEUM; }
			if(v.equals("theme_park")) { return Const.TOURIST_THEMEPARK; }
			if(v.equals("zoo")) { return Const.TOURIST_ZOO; }
			return 0;
		}
		
		if(k.equals("aeroway")) {
			if(v.equals("aerodrome")) { return Const.TRANSPORT_AIRPORT; }
			if(v.equals("terminal")) { return Const.TRANSPORT_TERMINAL; }
			return 0;
		}
		
		if(k.equals("emergency")) {
			if(v.equals("ambulance_station")) { return Const.HEALTH_HOSPITALEMERGENCY; }
			if(v.equals("ses_station")) { return Const.HEALTH_HOSPITALEMERGENCY; }
			return 0;
		}
		
		if(k.equals("waterway")) {
			if(v.equals("riverbank")) { return Const.WATER_WEIR; }
			if(v.equals("dam")) { return Const.WATER_DAM; }
			return 0;
		}
		
		if(k.equals("leisure")) {
			if(v.equals("common")) { return Const.LANDUSE_GRASS; }
			if(v.equals("dance")) { return Const.TOURIST_NIGHTCLUB; }
			if(v.equals("dog_park")) { return Const.LANDUSE_GRASS; }
			if(v.equals("garden")) { return Const.SHOP_FLORIST; }
			if(v.equals("golf_course")) { return Const.SPORT_GOLF; }
			if(v.equals("ice_rink")) { return Const.SPORT_ICESKATING; }
			if(v.equals("marina")) { return Const.TRANSPORT_MARINA; }
			if(v.equals("miniature_golf")) { return Const.SPORT_MINIATURGOLF; }
			if(v.equals("nature_reserve")) { return Const.LANDUSE_GRASS; }
			if(v.equals("park")) { return Const.LANDUSE_GRASS; }
			if(v.equals("pitch")) { return Const.SPORT_STADIUM; }
			if(v.equals("playground")) { return Const.AMENITY_PLAYGROUND; }
			if(v.equals("sports_centre")) { return Const.SPORT_LEISURECENTER; }
			if(v.equals("stadium")) { return Const.SPORT_STADIUM; }
			if(v.equals("water_park")) { return Const.SPORT_SWIMMING; }
			return 0;
		}
			
		if(k.equals("building")) {
			if(v.equals("hotel")) { return Const.ACCOMMO_HOTEL; }
			if(v.equals("school")) { return Const.EDUCATION_SCHOOL; }
			if(v.equals("supermarket")) { return Const.SHOP_CONVENIENCE; }
			if(v.equals("train_station")) { return Const.TRANSPORT_STATION; }
			if(v.equals("university")) { return Const.EDUCATION_UNIVERSITY; }
			return 0;
		}
		
		if(k.equals("landuse")) {
			if(v.equals("allotments")) { return Const.LANDUSE_ALLOTMENTS; }
			if(v.equals("brownfield")) { return Const.LANDUSE_QUARY; }
			if(v.equals("cemetery")) { return Const.BARRIER_BLOCKS; }
			if(v.equals("farmland")) { return Const.LANDUSE_GRASS; }
			if(v.equals("farm")) { return Const.LANDUSE_GRASS; }
			if(v.equals("forest")) { return Const.SUPER_LANDUSE_FOREST; }
			if(v.equals("grass")) { return Const.LANDUSE_GRASS; }
			if(v.equals("greenfield")) { return Const.LANDUSE_GRASS; }
			if(v.equals("meadow")) { return Const.LANDUSE_GRASS; }
			if(v.equals("greenhouse_horticulture")) { return Const.LANDUSE_GRASS; }
			if(v.equals("industrial")) { return Const.POI_CRANE; }
			if(v.equals("landfill")) { return Const.LANDUSE_HILLS; }
			if(v.equals("military")) { return Const.LANDUSE_MILITARY; }
			if(v.equals("orchard")) { return Const.LANDUSE_SCRUB; }
			if(v.equals("plant_nursery")) { return Const.LANDUSE_GRASS; }
			if(v.equals("quarry")) { return Const.LANDUSE_QUARY; }
			if(v.equals("recreation_ground")) { return Const.LANDUSE_GRASS; }
			if(v.equals("village_green")) { return Const.LANDUSE_GRASS; }
			if(v.equals("vineyard")) { return Const.LANDUSE_GRASS; }
			if(v.equals("retail")) { return Const.SHOP_VENDINGMASCHINE; }
			return 0;
		}
		
		if(k.equals("natural")) {
			if(v.equals("bay")) { return Const.TOURIST_BEACH; }
			if(v.equals("beach")) { return Const.TOURIST_BEACH; }
			if(v.equals("cave_entrance")) { return Const.POI_CAVE; }
			if(v.equals("cliff")) { return Const.POI_PEAK; }
			if(v.equals("glacier")) { return Const.POI_PEAK; }
			if(v.equals("peak")) { return Const.POI_PEAK; }
			if(v.equals("spring")) { return Const.WATER_WEIR; }
			if(v.equals("stone")) { return Const.BARRIER_BLOCKS; }
			if(v.equals("volcano")) { return Const.POI_PEAK1; }
			if(v.equals("fell")) { return Const.LANDUSE_GRASS; }
			if(v.equals("grassland")) { return Const.LANDUSE_GRASS; }
			
			if(v.equals("heath")) { return Const.LANDUSE_SCRUB; }
			if(v.equals("marsh")) { return Const.LANDUSE_SWAMP; }
			if(v.equals("wetland")) { return Const.LANDUSE_SWAMP; }
			if(v.equals("mud")) { return Const.LANDUSE_SWAMP; }
			if(v.equals("sand")) { return Const.TOURIST_BEACH; }
			if(v.equals("scree")) { return Const.LANDUSE_QUARY; }
			if(v.equals("scrub")) { return Const.LANDUSE_SCRUB; }
			if(v.equals("spring")) { return Const.WATER_WEIR; }
			if(v.equals("wood")) { return Const.SUPER_NATURAL_WOOD; }
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
