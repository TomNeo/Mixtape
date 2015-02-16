package com.heinousgames.game.shantelsmixtape.model;

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
 * Created by shanus on 1/18/15.
 */
public class TreeSprite extends Sprite {

    private boolean isUsed;

    private ITextureRegion manRegion;
    private ITexture manTexture;
    private ManSprite man;
    private BaseGameActivity mActivity;

    public TreeSprite(float pX, float pY, ITextureRegion pTextureRegion, VertexBufferObjectManager pVertexBufferObjectManager, BaseGameActivity activity) {
        super(pX, pY, pTextureRegion, pVertexBufferObjectManager);

        mActivity = activity;

        isUsed = false;

        try {
            this.manTexture = new BitmapTexture(mActivity.getTextureManager(), new IInputStreamOpener() {
                @Override
                public InputStream open() throws IOException {
                    return mActivity.getAssets().open("gfx/swinging_man.png");
                }
            });
            this.manTexture.load();
            this.manRegion = TextureRegionFactory.extractFromTexture(this.manTexture);
        } catch (IOException e) {
            Debug.e(e);
        }

    }

    public boolean isUsed() {
        return isUsed;
    }

    public void setUsed(boolean isUsed) {
        this.isUsed = isUsed;

        if (isUsed) {
            man = new ManSprite(20, 20, manRegion, mActivity.getVertexBufferObjectManager());

            attachChild(man);
        }
    }

}
