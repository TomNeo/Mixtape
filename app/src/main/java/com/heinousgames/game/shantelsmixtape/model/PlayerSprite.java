package com.heinousgames.game.shantelsmixtape.model;

import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.ui.activity.BaseGameActivity;

/**
 * Created by Ross on 2/16/2015.
 */
public class PlayerSprite extends AnimatedSprite {

    private boolean jumping = false;
    private boolean winCondition = false;
    private int green;
    private int red;

    public PlayerSprite(float pX, float pY, ITiledTextureRegion pTiledTextureRegion, VertexBufferObjectManager pVertexBufferObjectManager, BaseGameActivity activity) {
        super(pX, pY, pTiledTextureRegion, pVertexBufferObjectManager);
        this.setHeight(100);
        this.setWidth(100);
        red = 0;
        green = 0;
    }

    public boolean getJumping(){
        return jumping;
    }

    public void setJumping(boolean value){
        jumping = value;
    }

    public boolean getWinCondition(){
        return winCondition;
    }

    public void countScore(boolean score){
        if(score){
            green++;
        }else{
            red++;
        }
    }

    public void finalCount(){
        if(green >= red){
            winCondition = true;
        }
    }

}
