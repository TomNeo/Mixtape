package com.heinousgames.game.shantelsmixtape.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.heinousgames.game.shantelsmixtape.model.CopSprite;
import com.heinousgames.game.shantelsmixtape.model.TreeSprite;
import com.heinousgames.game.shantelsmixtape.model.WalkingManSprite;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.MoveModifier;
import org.andengine.entity.scene.IOnAreaTouchListener;
import org.andengine.entity.scene.ITouchArea;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.scene.background.modifier.ColorBackgroundModifier;
import org.andengine.entity.sprite.Sprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.bitmap.BitmapTexture;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.TextureRegionFactory;
import org.andengine.ui.activity.SimpleBaseGameActivity;
import org.andengine.util.adt.io.in.IInputStreamOpener;
import org.andengine.util.debug.Debug;
import org.andengine.util.modifier.IModifier;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by shanus on 1/9/15.
 */
public class PoliceBrutalityActivity extends SimpleBaseGameActivity {

    private final int CAMERA_WIDTH = 480;
    private final int CAMERA_HEIGHT = 748;

    private int x, y;

    private ITextureRegion copRegion, walkManRegion, treeRegion;
    private ITexture copTexture, walkManTexture, treeTexture;

    private Camera mCamera;
    private Scene mScene;
    private SharedPreferences myPrefs;
    private CopSprite copSprite;
    private TreeSprite[] treeSprite;
    private ArrayList<WalkingManSprite> walkingMen;
    private Background mBackground;
    private MoveModifier mMoveToTreeMod;

    @Override
    protected void onCreateResources() {
        myPrefs = getSharedPreferences("levelCompletion", MODE_PRIVATE);

        try {
            this.copTexture = new BitmapTexture(this.getTextureManager(), new IInputStreamOpener() {
                @Override
                public InputStream open() throws IOException {
                    return getAssets().open("gfx/cop.png");
                }
            });
            this.copTexture.load();
            this.copRegion = TextureRegionFactory.extractFromTexture(this.copTexture);
        } catch (IOException e) {
            Debug.e(e);
        }

        try {
            this.walkManTexture = new BitmapTexture(this.getTextureManager(), new IInputStreamOpener() {
                @Override
                public InputStream open() throws IOException {
                    return getAssets().open("gfx/walking_man.png");
                }
            });
            this.walkManTexture.load();
            this.walkManRegion = TextureRegionFactory.extractFromTexture(this.walkManTexture);
        } catch (IOException e) {
            Debug.e(e);
        }

        try {
            this.treeTexture = new BitmapTexture(this.getTextureManager(), new IInputStreamOpener() {
                @Override
                public InputStream open() throws IOException {
                    return getAssets().open("gfx/tree.png");
                }
            });
            this.treeTexture.load();
            this.treeRegion = TextureRegionFactory.extractFromTexture(this.treeTexture);
        } catch (IOException e) {
            Debug.e(e);
        }

    }

    /**
     * Basic Game Plan:
     * Guys on bottom walk back and forth. Cop comes straight down like a lever game.
     * If cop collides with one he flies it up to a tree. Player can tap cop to destroy him.
     * If cop is destroyed before getting to tree, he drops guy straight down and he walks again.
     * If cop gets guy to tree, he turns into a hangman. Make hangman sprite class with swing
     * rotation setup to a tree. Make the tree apart of the class, deploy them like the trees
     * currently. when collided with walker show the animation version. Determine how many clicks
     * it takes to kill a cop. Every time a cop is Destroyed a new one comes out of the center.
     * Determine if I want the empty icon in the middle with a new one coming out with color.
     * Determine if i want multipek cops out at once or just one at a time. If only one at a time,
     * try speeding up each cop by a bit to see how difficult it becomes. User may have to keep
     * all guys alive within a certain amount of time to win the minigame. Maybe a more artsy
     * version would be to have one cop that is bigger, like a boss, and ends up taking a lot of
     * clicks to defeat. Maybe it's apart of the final level that is unlocked after each
     * minigame is finished.
     * @return Scene GameScene
     */
    @Override
    protected Scene onCreateScene() {

        mScene = new Scene();

        x = 0;
        y = 0;

        mBackground = new Background(0, 0, 0, 1);

        mScene.setBackground(mBackground);

        mBackground.registerBackgroundModifier(new ColorBackgroundModifier(3, 0, 1, 0, 1, 0, 1));

        copSprite = new CopSprite(CAMERA_WIDTH/2 - copTexture.getWidth()/2, CAMERA_HEIGHT/2 - copTexture.getHeight()/2, copRegion, getVertexBufferObjectManager(), this);

        mScene.registerTouchArea(copSprite);

        treeSprite = new TreeSprite[5];
        walkingMen = new ArrayList<WalkingManSprite>();

        for (int i = 0; i < 5; i++) {
            treeSprite[i] = new TreeSprite(x, y, treeRegion, getVertexBufferObjectManager(), this);
            final WalkingManSprite wms = new WalkingManSprite(x, CAMERA_HEIGHT - walkManTexture.getHeight(), walkManRegion, getVertexBufferObjectManager());
            if (i < 2) {
                x += CAMERA_WIDTH / 3;
            } else if (i == 2) {
                y += treeTexture.getHeight();
                x -= treeTexture.getWidth()/2;
            } else if (i==3) {
                x -= CAMERA_WIDTH / 3;
            }

            mScene.attachChild(treeSprite[i]);
            mScene.attachChild(wms);
            walkingMen.add(wms);
        }

        mScene.attachChild(copSprite);

        mScene.setOnAreaTouchListener(new IOnAreaTouchListener() {
            @Override
            public boolean onAreaTouched(TouchEvent touchEvent, ITouchArea iTouchArea, float v, float v2) {
                if (iTouchArea.equals(copSprite) && touchEvent.isActionUp()) {
                    copSprite.setHitCount(copSprite.getHitCount() - 1);
                    Log.v("HitCount", String.valueOf(copSprite.getHitCount()));
                }
                return false;
            }
        });

        mScene.registerUpdateHandler(new IUpdateHandler() {
            @Override
            public void onUpdate(float v) {

                if (treeSprite[0].isUsed() && treeSprite[1].isUsed() && treeSprite[2].isUsed() && treeSprite[3].isUsed() && treeSprite[4].isUsed()) {
                    myPrefs.edit().putBoolean("rightFinished", false).commit();
                    Intent intent = new Intent(PoliceBrutalityActivity.this, MainActivity.class);
                    startActivity(intent);
                }

                Iterator<WalkingManSprite> menIterator = walkingMen.iterator();

                while (menIterator.hasNext()) {

                    WalkingManSprite wms = menIterator.next();

                    if (wms.collidesWith(copSprite) && !copSprite.hasPerson()) {
                        wms.clearUpdateHandlers();
                        wms.clearEntityModifiers();
                        mScene.detachChild(wms);
                        menIterator.remove();
                        copSprite.setHasPerson(true);

                        if (!treeSprite[0].isUsed())
                            mMoveToTreeMod = new MoveModifier(3, copSprite.getX(), treeSprite[0].getX() + treeSprite[0].getWidth()/2,
                                    copSprite.getY(), treeSprite[0].getY() + treeSprite[0].getHeight()/2);
                        else if (!treeSprite[1].isUsed())
                            mMoveToTreeMod = new MoveModifier(3, copSprite.getX(), treeSprite[1].getX() + treeSprite[1].getWidth()/2,
                                    copSprite.getY(), treeSprite[1].getY() + treeSprite[1].getHeight()/2);
                        else if (!treeSprite[2].isUsed())
                            mMoveToTreeMod = new MoveModifier(3, copSprite.getX(), treeSprite[2].getX() + treeSprite[2].getWidth()/2,
                                    copSprite.getY(), treeSprite[2].getY() + treeSprite[2].getHeight()/2);
                        else if (!treeSprite[3].isUsed())
                            mMoveToTreeMod = new MoveModifier(3, copSprite.getX(), treeSprite[3].getX() + treeSprite[3].getWidth()/2,
                                    copSprite.getY(), treeSprite[3].getY() + treeSprite[3].getHeight()/2);
                        else if (!treeSprite[4].isUsed())
                            mMoveToTreeMod = new MoveModifier(3, copSprite.getX(), treeSprite[4].getX() + treeSprite[4].getWidth()/2,
                                    copSprite.getY(), treeSprite[4].getY() + treeSprite[4].getHeight()/2);

                        mMoveToTreeMod.addModifierListener(new IModifier.IModifierListener<IEntity>() {
                            @Override
                            public void onModifierStarted(IModifier<IEntity> iEntityIModifier, IEntity iEntity) {}

                            @Override
                            public void onModifierFinished(IModifier<IEntity> iEntityIModifier, IEntity iEntity) {
                                copSprite.setHasPerson(false);
                                copSprite.detachChildren();
                                if (copSprite.collidesWith(treeSprite[0]))
                                    treeSprite[0].setUsed(true);
                                else if (copSprite.collidesWith(treeSprite[1]))
                                    treeSprite[1].setUsed(true);
                                else if (copSprite.collidesWith(treeSprite[2]))
                                    treeSprite[2].setUsed(true);
                                else if (copSprite.collidesWith(treeSprite[3]))
                                    treeSprite[3].setUsed(true);
                                else if (copSprite.collidesWith(treeSprite[4]))
                                    treeSprite[4].setUsed(true);
                                copSprite.setPosition(CAMERA_WIDTH/2 - copTexture.getWidth()/2, CAMERA_HEIGHT/2 - copTexture.getHeight()/2);
                                mMoveToTreeMod.removeModifierListener(this);
                            }
                        });

                        mMoveToTreeMod.reset();

                        copSprite.registerEntityModifier(mMoveToTreeMod);
                    }
                }
            }

            @Override
            public void reset() {}
        });

        return mScene;
    }

    @Override
    public EngineOptions onCreateEngineOptions() {
        mCamera = new Camera(0.0F, 0.0F, CAMERA_WIDTH, CAMERA_HEIGHT);
        EngineOptions localEngineOptions = new EngineOptions(true, ScreenOrientation.PORTRAIT_FIXED, new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), mCamera);
        localEngineOptions.getAudioOptions().setNeedsSound(true);
        localEngineOptions.getAudioOptions().setNeedsMusic(true);
        return localEngineOptions;
    }

}
