package com.heinousgames.game.shantelsmixtape.model;

import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.entity.modifier.MoveXModifier;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

/**
 * Created by shanus on 2/14/15.
 */
public class WalkingManSprite extends Sprite {

    private static final int CAMERA_WIDTH = 480;
    private static final int MOVEMENT_DURATION = 5;
    private static final float DIVISOR = 100.0f;

    private boolean isWalkingLeft, isWalkingRight;
    private float speed;

    public WalkingManSprite(float pX, float pY, ITextureRegion pTextureRegion, VertexBufferObjectManager pVertexBufferObjectManager) {
        super(pX, pY, pTextureRegion, pVertexBufferObjectManager);

        isWalkingLeft = false;
        isWalkingRight = false;

        speed = (CAMERA_WIDTH - getWidth() - getX()) / DIVISOR;

        this.registerUpdateHandler(new IUpdateHandler() {
            @Override
            public void onUpdate(float v) {
                if (getX() >= CAMERA_WIDTH - getWidth() && isWalkingRight) {
                    isWalkingRight = false;
                    isWalkingLeft = true;
                    setFlippedHorizontal(!isFlippedHorizontal());
                    registerEntityModifier(new MoveXModifier(MOVEMENT_DURATION, getX(), 0));
                } else if (getX() <= 0 && isWalkingLeft) {
                    isWalkingLeft = false;
                    isWalkingRight = true;
                    setFlippedHorizontal(!isFlippedHorizontal());
                    registerEntityModifier(new MoveXModifier(MOVEMENT_DURATION, getX(), CAMERA_WIDTH - getWidth()));
                } else if (!isWalkingLeft && !isWalkingRight) {
                    isWalkingRight = true;
                    registerEntityModifier(new MoveXModifier(speed, getX(), CAMERA_WIDTH - getWidth()));
                }
            }

            @Override
            public void reset() {

            }
        });
    }
}
