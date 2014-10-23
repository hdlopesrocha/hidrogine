package hidrogine.math;

import hidrogine.math.api.IBoundingBox;
import hidrogine.math.api.IBoundingSphere;
import hidrogine.math.api.IVector3;

// TODO: Auto-generated Javadoc
/**
 * The Class Box.
 */
public class BoundingBox extends IBoundingBox {

    /** The max. */
    private IVector3 min, max;

    /* (non-Javadoc)
     * @see hidrogine.math.IBox#getMin()
     */
    public IVector3 getMin() {
        return min;
    }

    /* (non-Javadoc)
     * @see hidrogine.math.IBox#setMin(hidrogine.math.IVector3)
     */
    public void setMin(IVector3 min) {
        this.min = min;
    }

    /* (non-Javadoc)
     * @see hidrogine.math.IBox#getMax()
     */
    public IVector3 getMax() {
        return max;
    }

    /* (non-Javadoc)
     * @see hidrogine.math.IBox#setMax(hidrogine.math.IVector3)
     */
    public void setMax(IVector3 max) {
        this.max = max;
    }

    /**
     * Instantiates a new box.
     *
     * @param min
     *            the min
     * @param max
     *            the max
     */
    public BoundingBox(IVector3 min, IVector3 max) {
        this.min = min;
        this.max = max;
    }


    public Vector3[] getCorners() {
        Vector3[] ret = new Vector3[8];
        ret[0]=new Vector3(getMin().getX(), getMax().getY(), getMax().getZ());
        ret[1]=new Vector3(getMax().getX(), getMax().getY(), getMax().getZ());
        ret[2]=new Vector3(getMax().getX(), getMin().getY(), getMax().getZ());
        ret[3]=new Vector3(getMin().getX(), getMin().getY(), getMax().getZ());
        ret[4]=new Vector3(getMin().getX(), getMax().getY(), getMin().getZ());
        ret[5]=new Vector3(getMax().getX(), getMax().getY(), getMin().getZ());
        ret[6]=new Vector3(getMax().getX(), getMin().getY(), getMin().getZ());
        ret[7]=new Vector3(getMin().getX(), getMin().getY(), getMin().getZ());
        return ret;
    }
    
    
    
    
   public ContainmentType contains(BoundingBox box)
    {
        //test if all corner is in the same side of a face by just checking min and max
        if (box.max.getX() < min.getX()
            || box.min.getX() > max.getX()
            || box.max.getY() < min.getY()
            || box.min.getY() > max.getY()
            || box.max.getZ() < min.getZ()
            || box.min.getZ() > max.getZ())
            return ContainmentType.Disjoint;


        if (box.min.getX() >= min.getX()
            && box.max.getX() <= max.getX()
            && box.min.getY() >= min.getY()
            && box.max.getY() <= max.getY()
            && box.min.getZ() >= min.getZ()
            && box.max.getZ() <= max.getZ())
            return ContainmentType.Contains;

        return ContainmentType.Intersects;
    }


    public ContainmentType contains(BoundingFrustum frustum)
    {
        //TODO: bad done here need a fix. 
        //Because question is not frustum contain box but reverse and this is not the same
        int i;
        ContainmentType contained;
        IVector3 []corners = frustum.getCorners();

        // First we check if frustum is in box
        for (i = 0; i < corners.length; i++)
        {
            contained = contains(corners[i]);
            if (contained == ContainmentType.Disjoint)
                break;
        }

        if (i == corners.length) // This means we checked all the corners and they were all contain or instersect
            return ContainmentType.Contains;

        if (i != 0)             // if i is not equal to zero, we can fastpath and say that this box intersects
            return ContainmentType.Intersects;


        // If we get here, it means the first (and only) point we checked was actually contained in the frustum.
        // So we assume that all other points will also be contained. If one of the points is disjoint, we can
        // exit immediately saying that the result is Intersects
        i++;
        for (; i < corners.length; i++)
        {
            contained= contains(corners[i]);
            if (contained != ContainmentType.Contains)
                return ContainmentType.Intersects;

        }

        // If we get here, then we know all the points were actually contained, therefore result is Contains
        return ContainmentType.Contains;
    }

    public ContainmentType contains(IBoundingSphere sphere)
    {
        if (sphere.getPosition().getX() - min.getX() > sphere.getRadius()
            && sphere.getPosition().getY() - min.getY() > sphere.getRadius()
            && sphere.getPosition().getZ() - min.getZ() > sphere.getRadius()
            && max.getX() - sphere.getPosition().getX() > sphere.getRadius()
            && max.getY() - sphere.getPosition().getY() > sphere.getRadius()
            && max.getZ() - sphere.getPosition().getZ() > sphere.getRadius())
            return ContainmentType.Contains;

        double dmin = 0;

        if (sphere.getPosition().getX() - min.getX() <= sphere.getRadius())
            dmin += (sphere.getPosition().getX() - min.getX()) * (sphere.getPosition().getX() - min.getX());
        else if (max.getX() - sphere.getPosition().getX() <= sphere.getRadius())
            dmin += (sphere.getPosition().getX() - max.getX()) * (sphere.getPosition().getX() - max.getX());
        if (sphere.getPosition().getY() - min.getY() <= sphere.getRadius())
            dmin += (sphere.getPosition().getY() - min.getY()) * (sphere.getPosition().getY() - min.getY());
        else if (max.getY() - sphere.getPosition().getY() <= sphere.getRadius())
            dmin += (sphere.getPosition().getY() - max.getY()) * (sphere.getPosition().getY() - max.getY());
        if (sphere.getPosition().getZ() - min.getZ() <= sphere.getRadius())
            dmin += (sphere.getPosition().getZ() - min.getZ()) * (sphere.getPosition().getZ() - min.getZ());
        else if (max.getZ() - sphere.getPosition().getZ() <= sphere.getRadius())
            dmin += (sphere.getPosition().getZ() - max.getZ()) * (sphere.getPosition().getZ() - max.getZ());

        if (dmin <= sphere.getRadius() * sphere.getRadius())
            return ContainmentType.Intersects;

        return ContainmentType.Disjoint;
    }


    public ContainmentType contains(Vector3 point)
    {
        //first we get if point is out of box
        if (point.getX() < min.getX()
            || point.getX() > max.getX()
            || point.getY() < min.getY()
            || point.getY() > max.getY()
            || point.getZ() < min.getZ()
            || point.getZ() > max.getZ())
        {
            return ContainmentType.Disjoint;
        }//or if point is on box because coordonate of point is lesser or equal
        else if (point.getX() == min.getX()
            || point.getX() == max.getX()
            || point.getY() == min.getY()
            || point.getY() == max.getY()
            || point.getZ() == min.getZ()
            || point.getZ() == max.getZ())
            return ContainmentType.Intersects;
        else
            return ContainmentType.Contains;
    }
/*
    BoundingBox BoundingBox::CreateFromPoints(vector<Vector3> points)
    {
        // TODO: Just check that Count > 0
        bool empty = true;
        Vector3 vector2 = Vector3(float.MaxValue);
        Vector3 vector1 = Vector3(float.MinValue);
        
        foreach (Vector3 vector3 in points)
        {
            vector2 = Vector3.Min(vector2, vector3);
            vector1 = Vector3.Max(vector1, vector3);
            empty = false;
        }
        if (empty)
            throw "ArgumentException";

        return BoundingBox(vector2, vector1);
    }
*/
    public BoundingBox createFromSphere(BoundingSphere sphere)
    {
        IVector3 vector1 =new Vector3(sphere.getRadius());
        return new BoundingBox(new Vector3(sphere.getPosition()).subtract(vector1), new Vector3(sphere.getPosition()).add(vector1));
    }

    public BoundingBox createMerged(BoundingBox original, BoundingBox additional)
    {
        return new BoundingBox(
            new Vector3(original.min).min(additional.min), new Vector3(original.max).max(additional.max));
    }


    public boolean equals(BoundingBox other)
    {
        return (min.equals(other.min)) && (max.equals(other.max));
    }

    public boolean intersects(BoundingBox box)
    {
        if ((max.getX() >= box.min.getX()) && (min.getX() <= box.max.getX()))
        {
            if ((max.getY() < box.min.getY()) || (min.getY() > box.max.getY()))
                return false;
            return (max.getZ() >= box.min.getZ()) && (min.getZ() <= box.max.getZ());
        }
        return false;
    }


    public boolean intersects(BoundingFrustum frustum)
    {
        return frustum.intersects(this);
    }

    public boolean intersects(BoundingSphere sphere)
    {
        if (sphere.getPosition().getX() - min.getX() > sphere.getRadius()
            && sphere.getPosition().getY() - min.getY() > sphere.getRadius()
            && sphere.getPosition().getZ() - min.getZ() > sphere.getRadius()
            && max.getX() - sphere.getPosition().getX() > sphere.getRadius()
            && max.getY() - sphere.getPosition().getY() > sphere.getRadius()
            && max.getZ() - sphere.getPosition().getZ() > sphere.getRadius())
            return true;

        double dmin = 0;

        if (sphere.getPosition().getX() - min.getX() <= sphere.getRadius())
            dmin += (sphere.getPosition().getX() - min.getX()) * (sphere.getPosition().getX() - min.getX());
        else if (max.getX() - sphere.getPosition().getX() <= sphere.getRadius())
            dmin += (sphere.getPosition().getX() - max.getX()) * (sphere.getPosition().getX() - max.getX());

        if (sphere.getPosition().getY() - min.getY() <= sphere.getRadius())
            dmin += (sphere.getPosition().getY() - min.getY()) * (sphere.getPosition().getY() - min.getY());
        else if (max.getY() - sphere.getPosition().getY() <= sphere.getRadius())
            dmin += (sphere.getPosition().getY() - max.getY()) * (sphere.getPosition().getY() - max.getY());

        if (sphere.getPosition().getZ() - min.getZ() <= sphere.getRadius())
            dmin += (sphere.getPosition().getZ() - min.getZ()) * (sphere.getPosition().getZ() - min.getZ());
        else if (max.getZ() - sphere.getPosition().getZ() <= sphere.getRadius())
            dmin += (sphere.getPosition().getZ() - max.getZ()) * (sphere.getPosition().getZ() - max.getZ());

        if (dmin <= sphere.getRadius() * sphere.getRadius())
            return true;

        return false;
    }

    public PlaneIntersectionType intersects(Plane plane)
    {
        //check all corner side of plane
        Vector3[] corners = getCorners();
        float lastdistance = plane.Normal.dot(corners[0]) + plane.D;

        for (int i = 1; i < corners.length; i++)
        {
            float distance = plane.Normal.dot(corners[i]) + plane.D;
            if ((distance <= 0.0 && lastdistance > 0.0) || (distance >= 0.0 && lastdistance < 0.0))
                return PlaneIntersectionType.Intersecting;
            lastdistance = distance;
        }

        if (lastdistance > 0.0)
            return PlaneIntersectionType.Front;

        return PlaneIntersectionType.Back;

    }



    public String toString(){
       return "{{Min:" + min.toString() + " Max:" + max.toString() + "}}";
    }

}
