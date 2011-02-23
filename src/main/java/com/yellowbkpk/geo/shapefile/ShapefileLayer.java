package com.yellowbkpk.geo.shapefile;

import java.awt.Color;
import java.awt.Graphics2D;
import java.io.IOException;

import javax.swing.Action;
import javax.swing.Icon;

import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.map.DefaultMapContext;
import org.geotools.map.MapContext;
import org.geotools.referencing.CRS;
import org.geotools.renderer.lite.StreamingRenderer;
import org.geotools.styling.FeatureTypeStyle;
import org.geotools.styling.LineSymbolizer;
import org.geotools.styling.Rule;
import org.geotools.styling.Stroke;
import org.geotools.styling.Style;
import org.geotools.styling.StyleFactory;
import org.opengis.filter.FilterFactory;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.openstreetmap.josm.Main;
import org.openstreetmap.josm.data.Bounds;
import org.openstreetmap.josm.data.coor.LatLon;
import org.openstreetmap.josm.data.osm.visitor.BoundingXYVisitor;
import org.openstreetmap.josm.gui.MapView;
import org.openstreetmap.josm.gui.layer.Layer;
import org.openstreetmap.josm.tools.ImageProvider;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;

public class ShapefileLayer extends Layer {

	private static StyleFactory styleFactory = CommonFactoryFinder.getStyleFactory(null);
	private static FilterFactory filterFactory = CommonFactoryFinder.getFilterFactory(null);
	private StreamingRenderer renderer;
	private CoordinateReferenceSystem crs;

	public ShapefileLayer(ShapefileDataStore data) throws NoSuchAuthorityCodeException, FactoryException, IOException {
		super("Shapefile");
		renderer = new StreamingRenderer();
		crs = CRS.decode(Main.proj.toCode());
		MapContext context = new DefaultMapContext(crs);
		Style style = createLineStyle();
		context.addLayer(data.getFeatureSource(), style);
		renderer.setContext(context);
	}
	
	private Style createLineStyle() {
        Stroke stroke = styleFactory.createStroke(
                filterFactory.literal(Color.BLUE),
                filterFactory.literal(1));

        /*
         * Setting the geometryPropertyName arg to null signals that we want to
         * draw the default geomettry of features
         */
        LineSymbolizer sym = styleFactory.createLineSymbolizer(stroke, null);

        Rule rule = styleFactory.createRule();
        rule.symbolizers().add(sym);
        FeatureTypeStyle fts = styleFactory.createFeatureTypeStyle(new Rule[]{rule});
        Style style = styleFactory.createStyle();
        style.featureTypeStyles().add(fts);

        return style;
    }

	@Override
	public Icon getIcon() {
        return ImageProvider.get("cancel");
	}

	@Override
	public Object getInfoComponent() {
		return getToolTipText();
	}

	@Override
	public Action[] getMenuEntries() {
		return new Action[] {};
	}

	@Override
	public String getToolTipText() {
		return "Shapefile";
	}

	@Override
	public boolean isMergable(Layer other) {
		return false;
	}

	@Override
	public void mergeFrom(Layer from) {
		return;
	}

	@Override
	public void paint(Graphics2D g, MapView mv, Bounds box) {
		LatLon max = box.getMax();
		Coordinate northWest = new Coordinate(max.getX(), max.getY());
		LatLon min = box.getMin();
		Coordinate southEast = new Coordinate(min.getX(), min.getY());
		Envelope envelope = new Envelope(northWest, southEast);
		ReferencedEnvelope mapArea = new ReferencedEnvelope(envelope, crs);
		renderer.paint(g, mv.getBounds(), mapArea);
	}

	@Override
	public void visitBoundingBox(BoundingXYVisitor v) {
	}

	
}
