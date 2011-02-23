package com.yellowbkpk.geo.shapefile;

import java.io.File;
import java.io.IOException;

import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.openstreetmap.josm.Main;
import org.openstreetmap.josm.actions.ExtensionFileFilter;
import org.openstreetmap.josm.gui.layer.Layer;
import org.openstreetmap.josm.gui.progress.ProgressMonitor;
import org.openstreetmap.josm.io.FileImporter;

import org.geotools.data.shapefile.ShapefileDataStore;

public class ShapefileImporter extends FileImporter {

	public ShapefileImporter() {
		super(new ExtensionFileFilter("shp", "shp", "ESRI Shapefiles (*.shp)"));
	}
	
    @Override
    public void importData(final File file, ProgressMonitor progressMonitor) throws IOException {
        ShapefileDataStore data = new ShapefileDataStore(file.toURI().toURL());
        
        Layer layer;
		try {
			layer = new ShapefileLayer(data);
			Main.main.addLayer(layer);
		} catch (NoSuchAuthorityCodeException e) {
			throw new IOException("Could not import shapefile.", e);
		} catch (FactoryException e) {
			throw new IOException("Could not import shapefile.", e);
		} catch (Throwable e) {
			e.printStackTrace();
		}
    }

}
