package com.heinousgames.game.shantelsmixtape.model;

import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.ui.activity.BaseGameActivity;

/**
 * Created by Ross on 2/16/2015.
 */
public class BlockSprite extends Sprite {
    public BlockSprite(float pX, float pY, ITextureRegion pTextureRegion, VertexBufferObjectManager pVertexBufferObjectManager, BaseGameActivity activity) {
        super(pX, pY, pTextureRegion, pVertexBufferObjectManager);
    }

}
