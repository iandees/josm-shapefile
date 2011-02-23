package com.yellowbkpk.geo.shapefile;

import org.openstreetmap.josm.actions.ExtensionFileFilter;
import org.openstreetmap.josm.data.coor.EastNorth;
import org.openstreetmap.josm.data.coor.LatLon;
import org.openstreetmap.josm.data.projection.Mercator;
import org.openstreetmap.josm.io.FileImporter;
import org.openstreetmap.josm.plugins.Plugin;
import org.openstreetmap.josm.plugins.PluginInformation;

public class ShapefilePlugin extends Plugin {
	public static Mercator proj = new Mercator();

	public static EastNorth latlon2eastNorth(LatLon p) {
		return proj.latlon2eastNorth(p); 
	}
	public static LatLon eastNorth2latlon(EastNorth p) {
		return proj.eastNorth2latlon(p); 
	}

	public ShapefilePlugin(PluginInformation info) {
		super(info);
		FileImporter shapefileImporter = new ShapefileImporter();
		ExtensionFileFilter.importers.add(shapefileImporter);
		System.err.println("Shapefile importer plugin loaded.");
	}
	
}
