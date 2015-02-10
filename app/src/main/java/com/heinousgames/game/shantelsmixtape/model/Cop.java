package com.heinousgames.game.shantelsmixtape.model;

import android.app.Activity;
import android.content.Context;

import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.bitmap.BitmapTexture;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.TextureRegionFactory;
import org.andengine.ui.activity.BaseGameActivity;
import org.andengine.util.adt.io.in.IInputStreamOpener;
import org.andengine.util.debug.Debug;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by shanus on 2/8/15.
 */
public class Cop {

    private ITextureRegion copRegion;
    private ITexture copTexture;

    public Sprite getCopSprite() {
        return copSprite;
    }

//    public void setCopSprite(Sprite copSprite) {
//        this.copSprite = copSprite;
//    }

    public Sprite copSprite;

    public Cop(final BaseGameActivity baseGameActivity, float x, float y) {

        try {
            this.copTexture = new BitmapTexture(baseGameActivity.getTextureManager(), new IInputStreamOpener() {
                @Override
                public InputStream open() throws IOException {
                    return baseGameActivity.getAssets().open("gfx/cop.png");
                }
            });
            this.copTexture.load();
            this.copRegion = TextureRegionFactory.extractFromTexture(this.copTexture);
        } catch (IOException e) {
            Debug.e(e);
        }

        copSprite = new Sprite(768/2 - copTexture.getWidth()/2, 1024/2 - copTexture.getHeight()/2, copRegion, baseGameActivity.getVertexBufferObjectManager());

    }

//    public Cop(float x, float y) {
//        super();
//
//
//
//    }
//
//    public Cop(float x, float y, float width, float height) {
//
//    }
}
