package hidrogine.math;

// TODO: Auto-generated Javadoc
/**
 * The Class IObject3D.
 */
public abstract class IObject3D extends IBoundingSphere {

    /** The position. */
    private IVector3 position;

    /** The model. */
    private IModel3D model;

    /** The node. */
    private SpaceNode node;

    /**
     * Gets the model.
     *
     * @return the model
     */
    public IModel3D getModel() {
        return model;
    }

    /**
     * Sets the model.
     *
     * @param model
     *            the new model
     */
    public void setModel(IModel3D model) {
        this.model = model;
    }

    /**
     * Instantiates a new i object3 d.
     *
     * @param position
     *            the position
     * @param model
     *            the model
     */
    public IObject3D(IVector3 position, IModel3D model) {
        this.position = position;
        this.model = model;
    }

    /*
     * (non-Javadoc)
     * 
     * @see hidrogine.math.api.ISphere#getRadius()
     */
    public float getRadius() {
        return model.getContainer().getRadius();
    }

    /*
     * (non-Javadoc)
     * 
     * @see hidrogine.math.api.ISphere#setRadius(float)
     */
    public void setRadius(float radius) {
        throw new RuntimeException("Readonly property!");
    }

    /*
     * (non-Javadoc)
     * 
     * @see hidrogine.math.api.ISphere#getPosition()
     */
    public IVector3 getPosition() {
        return position;
    }

    /*
     * (non-Javadoc)
     * 
     * @see hidrogine.math.api.ISphere#setPosition(hidrogine.math.api.IVector3)
     */
    public void setPosition(IVector3 position) {
        this.position = position;
    }

    /**
     * Insert.
     *
     * @param space
     *            the space
     */
    public void insert(Space space) {
        node = space.insert(this);

    }

    /**
     * Removes the.
     */
    public void remove() {
        node.remove(this);
    }

    /**
     * Gets the node.
     *
     * @return the node
     */
    protected SpaceNode getNode() {
        return node;
    }

    /**
     * Update.
     *
     * @param space
     *            the space
     */
    public void update(Space space) {
        node = space.update(this, node);
    }

}