package com.mozarellabytes.kroy.Entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;

public class Particle {

    /** Entity that WaterParticle is firing at */
    private final Entity target;

    /** Random colour of Rectangle */
    private Color colour;

    /** Random size of the Rectangle */
    private final float size;

    /** The position where the water particle starts from (the position
     * of the source)
     */
    private final Vector2 startPosition;

    /** The current position of the water particle */
    private Vector2 currentPosition;

    /** The end position of the water particle (the target the source
     * is attacking)
     */
    private Vector2 targetPosition;

    /**
     * Constructs a WaterParticle with
     * the following parameters
     *
     * @param source     The Entity that the
     *                  WaterParticle came from
     * @param target    The Entity that the
     *                  WaterParticle is heading
     *                  towards
     */
    public Particle(Entity source, Entity target, int colourType) {
		this.target = target;
        if (colourType == 0) {
        	Color[] colors = new Color[] {Color.CYAN, Color.NAVY, Color.BLUE, Color.PURPLE, Color.SKY, Color.TEAL};
            this.colour = colors[(int)(Math.random() * 4)];
        }
        if (colourType == 1) {
        	Color[] colors = new Color[] {Color.GOLD, Color.RED, Color.SCARLET, Color.FIREBRICK, Color.SALMON, Color.TAN};
            this.colour = colors[(int)(Math.random() * 4)];
        }
        this.size = (float)(Math.random()/5 + 0.1);
        this.startPosition = new Vector2(source.getPosition().x + 0.5f, source.getPosition().y + 0.5f);
        this.currentPosition = startPosition;
        this.targetPosition = target.getPosition();
        createTargetPosition(target);
    }

    /**
     * Creates the random coordinate within the target
     *
     * @param target the target whose target position is being created
     */
    private void createTargetPosition(Entity target) {
        float xCoord = (float)(Math.random()-0.5+target.getPosition().x);
        float yCoord = (float)(Math.random()-0.5+target.getPosition().y);
        this.targetPosition = new Vector2(xCoord, yCoord);
    }

    /**
     * Updates the position of the WaterParticle
     * using the Interpolation function
     */
    public void updatePosition() {
        this.currentPosition = this.startPosition.interpolate(this.targetPosition, 0.2f, Interpolation.circle);
    }

    /**
     * Checks if the WaterParticle has
     * reached the the Entity
     *
     * @return  <code>true</code> if WaterParticle hit Entity
     *          <code>false</code> otherwise
     */
    public boolean isHit() {
        return (((int) this.targetPosition.x == (int) this.currentPosition.x) &&
                ((int) this.targetPosition.y == (int) this.currentPosition.y));
    }

    public Entity getTarget() { return this.target; }

    public float getSize() { return this.size; }

    public Color getColour() { return this.colour; }

    public Vector2 getPosition() { return this.currentPosition; }

}