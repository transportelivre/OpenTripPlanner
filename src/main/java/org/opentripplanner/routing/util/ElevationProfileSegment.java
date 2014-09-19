/* This program is free software: you can redistribute it and/or
 modify it under the terms of the GNU Lesser General Public License
 as published by the Free Software Foundation, either version 3 of
 the License, or (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program.  If not, see <http://www.gnu.org/licenses/>. */

package org.opentripplanner.routing.util;

import java.io.Serializable;

import org.opentripplanner.common.MavenVersion;
import org.opentripplanner.common.geometry.PackedCoordinateSequence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vividsolutions.jts.geom.Coordinate;

/**
 * This class is an helper for Edges and Vertexes to store various data about elevation profiles.
 * 
 */
public class ElevationProfileSegment implements Serializable {

    private static final long serialVersionUID = MavenVersion.VERSION.getUID();

    @SuppressWarnings("unused")
    private static final Logger LOG = LoggerFactory.getLogger(ElevationProfileSegment.class);

    private PackedCoordinateSequence elevationProfile;

    private double slopeSpeedEffectiveLength;

    private double slopeWorkCost;

    private double maxSlope;

    protected boolean slopeOverride;

    private boolean flattened;

    public ElevationProfileSegment(double length) {
        slopeSpeedEffectiveLength = length;
        slopeWorkCost = length;
    }

    public double getMaxSlope() {
        return maxSlope;
    }

    public void setSlopeOverride(boolean slopeOverride) {
        this.slopeOverride = slopeOverride;
    }

    public boolean getSlopeOverride() {
        return slopeOverride;
    }

    public void setSlopeSpeedEffectiveLength(double slopeSpeedEffectiveLength) {
        this.slopeSpeedEffectiveLength = slopeSpeedEffectiveLength;
    }

    public double getSlopeSpeedEffectiveLength() {
        return slopeSpeedEffectiveLength;
    }

    public void setSlopeWorkCost(double slopeWorkCost) {
        this.slopeWorkCost = slopeWorkCost;
    }

    public double getSlopeWorkCost() {
        return slopeWorkCost;
    }

    public PackedCoordinateSequence getElevationProfile() {
        return elevationProfile;
    }

    public PackedCoordinateSequence getElevationProfile(double start, double end) {
        return ElevationUtils.getPartialElevationProfile(elevationProfile, start, end);
    }

    public void setElevationProfile(PackedCoordinateSequence elevationProfile) {
        this.elevationProfile = elevationProfile;
    }

    public SlopeCosts setElevationProfile(PackedCoordinateSequence elev, boolean computed,
            boolean slopeLimit) {
        if (elev == null || elev.size() < 2) {
            return null;
        }

        if (slopeOverride && !computed) {
            return null;
        }

        elevationProfile = elev;

        SlopeCosts costs = ElevationUtils.getSlopeCosts(elev, slopeLimit);
        slopeSpeedEffectiveLength = costs.slopeSpeedEffectiveLength;
        maxSlope = costs.maxSlope;
        slopeWorkCost = costs.slopeWorkCost;
        flattened = costs.flattened;

        return costs;
    }

    public String toString() {
        String out = "";
        if (elevationProfile == null || elevationProfile.size() == 0) {
            return "(empty elevation profile)";
        }
        for (int i = 0; i < elevationProfile.size(); ++i) {
            Coordinate coord = elevationProfile.getCoordinate(i);
            out += "(" + coord.x + "," + coord.y + "), ";
        }
        return out.substring(0, out.length() - 2);
    }

    public boolean isFlattened() {
        return flattened;
    }
}
