package hidrogine.math;

// TODO: Auto-generated Javadoc
//MIT License - Copyright (C) The Mono.Xna Team
//This file is subject to the terms and conditions defined in
//file 'LICENSE.txt', which is part of this source code package.
/**
 * The Class Ray.
 */
public class Ray {

    /** The Direction. */
    private IVector3 direction;

    /** The Position. */
    private IVector3 position;

    /**
     * Gets the direction.
     *
     * @return the direction
     */
    public IVector3 getDirection() {
        return direction;
    }

    /**
     * Sets the direction.
     *
     * @param direction
     *            the new direction
     */
    public void setDirection(Vector3 direction) {
        this.direction = direction;
    }

    /**
     * Gets the position.
     *
     * @return the position
     */
    public IVector3 getPosition() {
        return position;
    }

    /**
     * Sets the position.
     *
     * @param position
     *            the new position
     */
    public void setPosition(IVector3 position) {
        this.position = position;
    }

    /**
     * Instantiates a new ray.
     *
     * @param iVector3
     *            the position
     * @param iVector32
     *            the direction
     */
    public Ray(IVector3 iVector3, IVector3 iVector32) {
        this.position = iVector3;
        this.direction = iVector32;
    }

    /**
     * Equals.
     *
     * @param other
     *            the other
     * @return true, if successful
     */
    public boolean equals(Ray other) {
        return this.position.equals(other.position)
                && this.direction.equals(other.direction);
    }

    // adapted from
    // http://www.scratchapixel.com/lessons/3d-basic-lessons/lesson-7-intersecting-simple-shapes/ray-box-intersection/
    /**
     * Intersects.
     *
     * @param box
     *            the box
     * @return the float
     */
    public Float intersects(IBoundingBox box) {
        float Epsilon = 1e-6f;

        Float tMin = null, tMax = null;

        if (Math.abs(direction.getX()) < Epsilon) {
            if (position.getX() < box.getMin().getX()
                    || position.getX() > box.getMax().getX())
                return null;
        } else {
            tMin = (box.getMin().getX() - position.getX()) / direction.getX();
            tMax = (box.getMax().getX() - position.getX()) / direction.getX();

            if (tMin > tMax) {
                float temp = tMin;
                tMin = tMax;
                tMax = temp;
            }
        }

        if (Math.abs(direction.getY()) < Epsilon) {
            if (position.getY() < box.getMin().getY()
                    || position.getY() > box.getMax().getY())
                return null;
        } else {
            float tMinY = (box.getMin().getY() - position.getY())
                    / direction.getY();
            float tMaxY = (box.getMax().getY() - position.getY())
                    / direction.getY();

            if (tMinY > tMaxY) {
                float temp = tMinY;
                tMinY = tMaxY;
                tMaxY = temp;
            }

            if ((tMin != null && tMin > tMaxY)
                    || (tMax != null && tMinY > tMax))
                return null;

            if (tMin == null || tMinY > tMin)
                tMin = tMinY;
            if (tMax == null || tMaxY < tMax)
                tMax = tMaxY;
        }

        if (Math.abs(direction.getZ()) < Epsilon) {
            if (position.getZ() < box.getMin().getZ()
                    || position.getZ() > box.getMax().getZ())
                return null;
        } else {
            float tMinZ = (box.getMin().getZ() - position.getZ())
                    / direction.getZ();
            float tMaxZ = (box.getMax().getZ() - position.getZ())
                    / direction.getZ();

            if (tMinZ > tMaxZ) {
                float temp = tMinZ;
                tMinZ = tMaxZ;
                tMaxZ = temp;
            }

            if ((tMin != null && tMin > tMaxZ)
                    || (tMax != null && tMinZ > tMax))
                return null;

            if (tMin == null || tMinZ > tMin)
                tMin = tMinZ;
            if (tMax == null || tMaxZ < tMax)
                tMax = tMaxZ;
        }

        // having a positive tMin and a negative tMax means the ray is inside
        // the box
        // we expect the intesection distance to be 0 in that case
        if ((tMin != null && tMin < 0) && tMax > 0)
            return 0f;

        // a negative tMin means that the intersection point is behind the ray's
        // origin
        // we discard these as not hitting the AABB
        if (tMin < 0)
            return null;

        return tMin;
    }

    /*
     * public float? Intersects(BoundingFrustum frustum) { if (frustum == null)
     * { throw new ArgumentNullException("frustum"); }
     * 
     * return frustum.Intersects(this); }
     */

    /**
     * Intersects.
     *
     * @param plane
     *            the plane
     * @return the float
     */
    public Float intersects(Plane plane) {
        float den = direction.dot(plane.getNormal());
        if (Math.abs(den) < 0.00001f) {
            return null;
        }
        return -(plane.getDistance() + plane.getNormal().dot(position)) / den;
    }

    /**
     * Intersects.
     *
     * @param triangle
     *            the triangle
     * @return the i vector3
     */
    public Float intersects(final Triangle triangle, float maxDistance) {
        final Float distance = intersects(triangle.getPlane());
        if (distance != null && distance<= maxDistance) {
            IVector3 intersection = new Vector3(position).addMultiply(direction,distance);
            
            System.out.println(intersection+" VS "+ position+ " | "+ distance);
            if (triangle.contains(intersection)) {

                return distance;
            }
        }
        return null;
    }

    /**
     * Intersects.
     *
     * @param sphere
     *            the sphere
     * @return the float
     */
    public Float intersects(IBoundingSphere sphere) {
        // Find the vector between where the ray starts the the sphere's centre
        IVector3 difference = new Vector3(sphere.getCenter())
                .subtract(this.position);

        float differenceLengthSquared = difference.lengthSquared();
        float sphereRadiusSquared = sphere.getRadius() * sphere.getRadius();

        float distanceAlongRay;

        // If the distance between the ray start and the sphere's centre is less
        // than
        // the radius of the sphere, it means we've intersected. N.B. checking
        // the LengthSquared is faster.
        if (differenceLengthSquared < sphereRadiusSquared) {
            return 0.0f;
        }

        distanceAlongRay = direction.dot(difference);
        // If the ray is pointing away from the sphere then we don't ever
        // intersect
        if (distanceAlongRay < 0) {
            return null;
        }

        // Next we kinda use Pythagoras to check if we are within the bounds of
        // the sphere
        // if x = radius of sphere
        // if y = distance between ray position and sphere centre
        // if z = the distance we've travelled along the ray
        // if x^2 + z^2 - y^2 < 0, we do not intersect
        float dist = sphereRadiusSquared + distanceAlongRay * distanceAlongRay
                - differenceLengthSquared;

        return (dist < 0) ? null : distanceAlongRay - (float) Math.sqrt(dist);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    public String toString() {
        return "{{Position:" + position.toString() + " Direction:"
                + direction.toString() + "}}";
    }

}