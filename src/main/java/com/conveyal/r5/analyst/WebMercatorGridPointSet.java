package com.conveyal.r5.analyst;

import com.conveyal.r5.profile.StreetMode;
import com.conveyal.r5.streets.LinkedPointSet;
import com.conveyal.r5.streets.StreetLayer;
import com.conveyal.r5.transit.TransportNetwork;
import com.vividsolutions.jts.geom.Coordinate;
import org.mapdb.Fun;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * A linked pointset that represents a web mercator grid laid over the graph.
 */
public class WebMercatorGridPointSet extends PointSet implements Serializable {

    public static final Logger LOG = LoggerFactory.getLogger(WebMercatorGridPointSet.class);

    public static final int DEFAULT_ZOOM = 9;

    /** web mercator zoom level */
    public final int zoom;

    /** westernmost pixel */
    public final int west;

    /** northernmost pixel */
    public final int north;

    /** width */
    public final int width;

    /** height */
    public final int height;

    public WebMercatorGridPointSet(int zoom, int west, int north, int width, int height) {
        this.zoom = zoom;
        this.west = west;
        this.north = north;
        this.width = width;
        this.height = height;

    }

    public WebMercatorGridPointSet(TransportNetwork transportNetwork) {
        LOG.info("Creating web mercator pointset for transport network with extents {}", transportNetwork.streetLayer.envelope);

        this.zoom = DEFAULT_ZOOM;
        int west = lonToPixel(transportNetwork.streetLayer.envelope.getMinX());
        int east = lonToPixel(transportNetwork.streetLayer.envelope.getMaxX());
        int north = latToPixel(transportNetwork.streetLayer.envelope.getMaxY());
        int south = latToPixel(transportNetwork.streetLayer.envelope.getMinY());

        this.west = west;
        this.north = north;
        this.height = south - north;
        this.width = east - west;
    }

    @Override
    public int featureCount() {
        return (int) (height * width);
    }

    @Override
    public double getLat(int i) {
        long y = i / this.width + this.north;
        return pixelToLat(y);
    }

    @Override
    public double getLon(int i) {
        long x = i % this.width + this.west;
        return pixelToLon(x);
    }

    // http://wiki.openstreetmap.org/wiki/Slippy_map_tilenames#Mathematics

    /** convert longitude to pixel value */
    public int lonToPixel (double lon) {
        // factor of 256 is to get a pixel value not a tile number
        return Grid.lonToPixel(lon, zoom);
    }

    /** convert latitude to pixel value */
    public int latToPixel (double lat) {
        return Grid.latToPixel(lat, zoom);
    }

    public double pixelToLon (double x) {
        return Grid.pixelToLon(x, zoom);
    }

    public double pixelToLat (double y) {
        return Grid.pixelToLat(y, zoom);
    }

}
