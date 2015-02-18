package com.heinousgames.game.shantelsmixtape.model;

import com.heinousgames.game.shantelsmixtape.activities.SelfDoubtActivity;

import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.MoveXModifier;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.ui.activity.BaseGameActivity;
import org.andengine.util.modifier.IModifier;

/**
 * Created by Ross on 2/16/2015.
 */
public class BlockSprite extends Sprite {

    private SelfDoubtActivity parentActivity;
    private BlockSprite namesake;

    public BlockSprite(float pX, float pY, ITextureRegion pTextureRegion, VertexBufferObjectManager pVertexBufferObjectManager, SelfDoubtActivity activity) {
        super(pX, pY, pTextureRegion, pVertexBufferObjectManager);
        parentActivity = activity;
        setMovements();
        namesake = this;
    }

    private void setMovements(){
        MoveXModifier ModifierBuffer = new MoveXModifier(2,this.getX(),this.getX() - parentActivity.getCAMERA_WIDTH());
        ModifierBuffer.addModifierListener(new IModifier.IModifierListener<IEntity>(){
            @Override
            public void onModifierStarted(IModifier<IEntity> iEntityIModifier, IEntity iEntity) {

            }

            @Override
            public void onModifierFinished(IModifier<IEntity> iEntityIModifier, IEntity iEntity) {
                killMe();
            }
        });
        this.registerEntityModifier(ModifierBuffer);
    }

    private void killMe(){
        parentActivity.addToList(this);
    }
}
