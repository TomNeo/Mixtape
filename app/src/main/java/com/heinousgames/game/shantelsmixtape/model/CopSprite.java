package com.heinousgames.game.shantelsmixtape.model;

import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.heinousgames.game.shantelsmixtape.activities.MainActivity;

import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.entity.modifier.ColorModifier;
import org.andengine.entity.modifier.LoopEntityModifier;
import org.andengine.entity.modifier.MoveYModifier;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.bitmap.BitmapTexture;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.TextureRegionFactory;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.ui.activity.BaseGameActivity;
import org.andengine.util.adt.io.in.IInputStreamOpener;
import org.andengine.util.debug.Debug;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by shanus on 2/14/15.
 */
public class CopSprite extends Sprite {

    private boolean isGoingDown;
    private boolean isGoingUp;
    private boolean hasPerson;

    private ITextureRegion manRegion;
    private ITexture manTexture;
    private Sprite manSprite;

    private SharedPreferences myPrefs;

    private int mHitCount;

    private static final int CAMERA_HEIGHT = 748;
    private static final int MOVEMENT_DURATION = 3;

    private BaseGameActivity mActivity;

    public CopSprite(float pX, float pY, ITextureRegion pTextureRegion, VertexBufferObjectManager pVertexBufferObjectManager, BaseGameActivity activity) {
        super(pX, pY, pTextureRegion, pVertexBufferObjectManager);

        mActivity = activity;

        myPrefs = mActivity.getSharedPreferences("levelCompletion", mActivity.MODE_PRIVATE);

        try {
            this.manTexture = new BitmapTexture(mActivity.getTextureManager(), new IInputStreamOpener() {
                @Override
                public InputStream open() throws IOException {
                    return mActivity.getAssets().open("gfx/walking_man.png");
                }
            });
            this.manTexture.load();
            this.manRegion = TextureRegionFactory.extractFromTexture(this.manTexture);
        } catch (IOException e) {
            Debug.e(e);
        }

        manSprite = new Sprite(20, 20, manRegion, mActivity.getVertexBufferObjectManager());

        isGoingDown = false;
        isGoingUp = false;
        hasPerson = false;

        mHitCount = 5;

        registerEntityModifier(new LoopEntityModifier(new ColorModifier(0.5f, 1, 0, 0, 0, 0, 1)));

        this.registerUpdateHandler(new IUpdateHandler() {
            @Override
            public void onUpdate(float v) {

                if (mHitCount <= 0) {
                    clearUpdateHandlers();
                    clearEntityModifiers();
                    detachSelf();
                    myPrefs.edit().putBoolean("rightFinished", true).commit();
                    Intent intent = new Intent(mActivity, MainActivity.class);
                    mActivity.startActivity(intent);
                }

                if (getY() >= CAMERA_HEIGHT - getHeight()/2 && isGoingDown) {
                    isGoingDown = false;
                    isGoingUp = true;
                    registerEntityModifier(new MoveYModifier(MOVEMENT_DURATION, getY(), CAMERA_HEIGHT/2 - getHeight()/2));
                } else if (getY() <= CAMERA_HEIGHT/2 - getHeight()/2 && isGoingUp) {
                    isGoingUp = false;
                    isGoingDown = true;
                    registerEntityModifier(new MoveYModifier(MOVEMENT_DURATION, getY(), CAMERA_HEIGHT - getHeight()/2));
                } else if (!isGoingDown && !isGoingUp) {
                    isGoingDown = true;
                    registerEntityModifier(new MoveYModifier(MOVEMENT_DURATION, getY(), CAMERA_HEIGHT - getHeight()/2));
                }
            }

            @Override
            public void reset() {}
        });

    }

    public boolean hasPerson() {
        return hasPerson;
    }

    public void setHasPerson(boolean hasPerson) {
        this.hasPerson = hasPerson;

        if (hasPerson)
            attachChild(manSprite);
        else
            detachChild(manSprite);
    }

    public int getHitCount() {
        return mHitCount;
    }

    public void setHitCount(int mHitCount) {
        this.mHitCount = mHitCount;
    }

}
