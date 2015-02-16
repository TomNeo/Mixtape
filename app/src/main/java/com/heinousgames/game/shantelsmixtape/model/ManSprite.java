package com.heinousgames.game.shantelsmixtape.model;

import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.entity.modifier.RotationModifier;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

/**
 * Created by shanus on 2/15/15.
 */
public class ManSprite extends Sprite {

    private boolean swingingLeft, swingingRight;

    public ManSprite(float pX, float pY, ITextureRegion pTextureRegion, VertexBufferObjectManager pVertexBufferObjectManager) {
        super(pX, pY, pTextureRegion, pVertexBufferObjectManager);

        swingingLeft = false;
        swingingRight = false;

        setRotationCenter(getWidth()/2, 0);

        setRotation(20);

        registerUpdateHandler(new IUpdateHandler() {
            @Override
            public void onUpdate(float v) {
                if (!swingingLeft && !swingingRight) {
                    swingingRight = true;
                    registerEntityModifier(new RotationModifier(3, getRotation(), -20));
                } else if (getRotation() <= -20f && swingingRight) {
                    swingingRight = false;
                    swingingLeft = true;
                    registerEntityModifier(new RotationModifier(3, getRotation(), 20));
                } else if (getRotation() >= 20 && swingingLeft) {
                    swingingLeft = false;
                    swingingRight = true;
                    registerEntityModifier(new RotationModifier(3, getRotation(), -20));
                }
            }

            @Override
            public void reset() {

            }
        });

    }


}
