package com.heinousgames.game.shantelsmixtape.model;

import com.heinousgames.game.shantelsmixtape.activities.SelfDoubtActivity;

import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.MoveXModifier;
import org.andengine.entity.text.Text;
import org.andengine.opengl.font.IFont;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.modifier.IModifier;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Ross on 2/18/2015.
 */
public class WordText extends Text {

    SelfDoubtActivity parentActivity;
    private boolean score;

    public WordText(float pX, float pY, IFont pFont, CharSequence pText, VertexBufferObjectManager pVertexBufferObjectManager, SelfDoubtActivity activity){
        super(pX,pY,pFont,pText,pVertexBufferObjectManager);
        parentActivity = activity;
       // setProperties();
        setMovements();
    }

    private void setProperties(){

        Random rand = new Random();

        if(rand.nextInt(2)==0){
 //           this.setText(BadWords[rand.nextInt(BadWords.length-1)]);
            this.setColor(1,0,0,1);
            score = false;

        }else{
//            this.setText(GoodWords[rand.nextInt(GoodWords.length-1)]);
            this.setColor(0,1,0,1);
            score = true;
        }
    }

    public boolean getScore(){
        return score;
    }

    public void setScore(boolean value){
        score = value;
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



