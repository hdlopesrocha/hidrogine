package hidrogine.math.api;


// TODO: Auto-generated Javadoc
/**
 * The Class IVector3.
 */
public abstract class IVector3 {

    /**
     * Gets the z.
     *
     * @return the z
     */
    public abstract float getZ();

    /**
     * Sets the z.
     *
     * @param zz
     *            the new z
     * @return the vector3
     */
    public abstract IVector3 setZ(float zz);

    /**
     * Gets the x.
     *
     * @return the x
     */
    public abstract float getX();

    /**
     * Sets the x.
     *
     * @param xx
     *            the new x
     * @return the vector3
     */
    public abstract IVector3 setX(float xx);

    /**
     * Gets the y.
     *
     * @return the y
     */
    public abstract float getY();

    /**
     * Sets the y.
     *
     * @param yy
     *            the new y
     * @return the vector3
     */
    public abstract IVector3 setY(float yy);

    /*
     * (non-Javadoc)
     * 
     * @see hidrogine.math.IVector3#add(hidrogine.math.IVector3)
     */
    /**
     * Adds the.
     *
     * @param vec
     *            the vec
     * @return the i vector3
     */
    public final IVector3 add(final IVector3 vec) {
        setX(getX() + vec.getX());
        setY(getY() + vec.getY());
        setZ(getZ() + vec.getZ());
        return this;
    }

    /*
     * (non-Javadoc)
     * 
     * @see hidrogine.math.IVector3#addX(float)
     */
    /**
     * Adds the x.
     *
     * @param vec
     *            the vec
     * @return the i vector3
     */
    public final IVector3 addX(final float vec) {
        setX(getX() + vec);
        return this;
    }

    /*
     * (non-Javadoc)
     * 
     * @see hidrogine.math.IVector3#addY(float)
     */
    /**
     * Adds the y.
     *
     * @param vec
     *            the vec
     * @return the i vector3
     */
    public final IVector3 addY(final float vec) {
        setY(getY() + vec);
        return this;
    }

    /*
     * (non-Javadoc)
     * 
     * @see hidrogine.math.IVector3#addZ(float)
     */
    /**
     * Adds the z.
     *
     * @param vec
     *            the vec
     * @return the i vector3
     */
    public final IVector3 addZ(final float vec) {
        setZ(getZ() + vec);
        return this;
    }

    /*
     * (non-Javadoc)
     * 
     * @see hidrogine.math.IVector3#subtract(hidrogine.math.IVector3)
     */
    /**
     * Subtract.
     *
     * @param vec
     *            the vec
     * @return the i vector3
     */
    public final IVector3 subtract(final IVector3 vec) {
        setX(getX() - vec.getX());
        setY(getY() - vec.getY());
        setZ(getZ() - vec.getZ());
        return this;
    }
    


    /*
     * (non-Javadoc)
     * 
     * @see hidrogine.math.IVector3#multiply(hidrogine.math.IVector3)
     */
    /**
     * Multiply.
     *
     * @param vec
     *            the vec
     * @return the i vector3
     */
    public final IVector3 multiply(final IVector3 vec) {
        setX(getX() * vec.getX());
        setY(getY() * vec.getY());
        setZ(getZ() * vec.getZ());
        return this;
    }

    /**
     * Sets the.
     *
     * @param posX
     *            the pos x
     * @param posY
     *            the pos y
     * @param posZ
     *            the pos z
     * @return the i vector3
     */
    public IVector3 set(float posX, float posY, float posZ) {

        setX(posX);
        setY(posY);
        setZ(posZ);
        return this;
    }

    /**
     * Multiply.
     *
     * @param vec
     *            the vec
     * @return the i vector3
     */
    public final IVector3 multiply(final float vec) {
        setX(getX() * vec);
        setY(getY() * vec);
        setZ(getZ() * vec);
        return this;
    }

    /**
     * Normalize.
     *
     * @return the i vector3
     */
    public IVector3 normalize() {
        divide(length());
        return this;

    }

    /*
     * (non-Javadoc)
     * 
     * @see hidrogine.math.IVector3#divide(hidrogine.math.IVector3)
     */
    /**
     * Divide.
     *
     * @param vec
     *            the vec
     * @return the i vector3
     */
    public final IVector3 divide(final IVector3 vec) {
        setX(getX() / vec.getX());
        setY(getY() / vec.getY());
        setZ(getZ() / vec.getZ());
        return this;
    }

    /*
     * (non-Javadoc)
     * 
     * @see hidrogine.math.IVector3#divide(float)
     */
    /**
     * Divide.
     *
     * @param vec
     *            the vec
     * @return the i vector3
     */
    public final IVector3 divide(final float vec) {
        setX(getX() + vec);
        setY(getY() + vec);
        setZ(getZ() + vec);
        return this;
    }

    /**
     * Sets the.
     *
     * @param vec
     *            the vec
     * @return the i vector3
     */
    public IVector3 set(IVector3 vec) {
        setX(vec.getX());
        setY(vec.getY());
        setZ(vec.getZ());
        return this;
    }

    /**
     * Dot.
     *
     * @param v the v
     * @return the float
     */
    public final float dot(final IVector3 v) {
        return getX() * v.getX() + getY() * v.getY() + getZ()
                * v.getZ();
    }

    /**
     * Cross.
     *
     * @param v the v
     * @return the i vector3
     */
    public final IVector3 cross(final IVector3 v) {
        float x = getY() * v.getZ() - v.getY() * getZ();
        float y = v.getX() * getZ() - getX() * v.getZ();
        float z = getX() * v.getY() - v.getX() * getY();
        setX(x);
        setY(y);
        setZ(z);
        return this;
    }


    /*
     * (non-Javadoc)
     * 
     * @see hidrogine.math.IVector3#length()
     */
    /**
     * Length.
     *
     * @return the double
     */
    public float length() {
        return (float) Math.sqrt(getX() * getX() + getY() * getY() + getZ()
                * getZ());
    }

    /*
     * (non-Javadoc)
     * 
     * @see hidrogine.math.IVector3#distance(hidrogine.math.IVector3)
     */
    /**
     * Distance.
     *
     * @param vec
     *            the vec
     * @return the double
     */
    public double distance(IVector3 vec) {
        float x = getX() - vec.getX();
        float y = getY() - vec.getY();
        float z = getZ() - vec.getZ();
        return Math.sqrt(x * x + y * y + z * z);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    /*
     * (non-Javadoc)
     * 
     * @see hidrogine.math.IVector3#toString()
     */
    public String toString() {
        return "{X=" + getX() + ", Y=" + getY() + ", Z=" + getZ() + "}";
    }
   

}
