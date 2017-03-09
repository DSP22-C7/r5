package com.conveyal.r5.publish;

import com.conveyal.r5.analyst.cluster.GenericClusterRequest;
import com.conveyal.r5.profile.ProfileRequest;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.UUID;

import static java.lang.Double.NaN;

/**
 * Configuration object for a static site, deserialized from JSON.
 */
public class StaticSiteRequest {
    /** Transport network ID to use */
    public String transportNetworkId;

    /** Worker version */
    public String workerVersion;

    /** profile request */
    public ProfileRequest request;

    /** S3 bucket for result output */
    public String bucket;

    /** Prefix for result output */
    public String prefix;

    /** Bounds; if left NaN, will be computed based on transport network */
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    public double north = NaN;
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    public double east = NaN;
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    public double south = NaN;
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    public double west = NaN;

    public final String jobId = UUID.randomUUID().toString().replace("-", "");

    public PointRequest getPointRequest (int x, int y) {
        return new PointRequest(this, x, y);
    }

    /** Represents a single point of a static site request */
    public static class PointRequest extends GenericClusterRequest {
        public final String type = "static";

        private PointRequest (StaticSiteRequest request, int x, int y) {
            this.request = request;
            this.x = x;
            this.y = y;
            this.workerVersion = request.workerVersion;
            this.graphId = request.transportNetworkId;
            this.jobId = request.jobId;
            this.id = x + "_" + y;
        }

        /** no-arg constructor for deserialization */
        public PointRequest () { /* do nothing */ }

        /** x pixel, relative to west side of request */
        public int x;

        /** y pixel, relative to north edge of request */
        public int y;

        /** StaticSiteRequest this is associated with */
        public StaticSiteRequest request;

        @Override
        public ProfileRequest extractProfileRequest() {
            return request.request;
        }

    }
}
