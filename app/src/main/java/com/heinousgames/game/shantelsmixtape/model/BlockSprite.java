package com.heinousgames.game.shantelsmixtape.model;

import com.heinousgames.game.shantelsmixtape.activities.SelfDoubtActivity;

import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.ColorModifier;
import org.andengine.entity.modifier.MoveXModifier;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.ui.activity.BaseGameActivity;
import org.andengine.util.modifier.IModifier;

import java.util.Random;

/**
 * Created by Ross on 2/16/2015.
 */
public class BlockSprite extends Sprite {

    private SelfDoubtActivity parentActivity;
    private boolean score;

    public BlockSprite(float pX, float pY, ITextureRegion pTextureRegion, VertexBufferObjectManager pVertexBufferObjectManager, SelfDoubtActivity activity) {
        super(pX, pY, pTextureRegion, pVertexBufferObjectManager);
        parentActivity = activity;
        setProperties();
        setMovements();
    }

    private void setProperties(){

        this.setWidth(48);
        this.setHeight(30);
        Random rand = new Random();

        if(rand.nextInt(2)==0){
            this.setColor(1,0,0,1);
            score = false;

        }else{
            this.setColor(0,1,0,1);
            score = true;
        }
    }

    public boolean getScore(){
        return score;
    }

    @Override
    protected void onManagedUpdate(float pSecondsElapsed){
        super.onManagedUpdate(pSecondsElapsed);
        if(this.collidesWith(parentActivity.getPlayer())){
            parentActivity.getPlayer().countScore(score);
            clearEntityModifiers();
            killMe();
        }
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
