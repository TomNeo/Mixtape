package com.heinousgames.game.shantelsmixtape.activities;

import android.content.Intent;
import android.content.SharedPreferences;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.modifier.ColorModifier;
import org.andengine.entity.modifier.LoopEntityModifier;
import org.andengine.entity.modifier.MoveModifier;
import org.andengine.entity.modifier.MoveXModifier;
import org.andengine.entity.modifier.ScaleModifier;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.scene.background.modifier.ColorBackgroundModifier;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.bitmap.BitmapTexture;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.TextureRegionFactory;
import org.andengine.ui.activity.SimpleBaseGameActivity;
import org.andengine.util.adt.io.in.IInputStreamOpener;
import org.andengine.util.debug.Debug;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by shanus on 1/9/15.
 */
public class PoliceBrutalityActivity extends SimpleBaseGameActivity {

    private final int CAMERA_WIDTH = 480;
    private final int CAMERA_HEIGHT = 748;

    private int x, y;

    private ITextureRegion copRegion, swingManRegion, walkManRegion, treeRegion;
    private ITexture copTexture, swingManTexture, walkManTexture, treeTexture;

    private Camera mCamera;
    private Scene mScene;
    private SharedPreferences myPrefs;
    private Sprite copSprite, swingingManSprite;
    private Sprite[] treeSprite, walkingManSprite;
    private Background mBackground;

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
            this.swingManTexture = new BitmapTexture(this.getTextureManager(), new IInputStreamOpener() {
                @Override
                public InputStream open() throws IOException {
                    return getAssets().open("gfx/swinging_man.png");
                }
            });
            this.swingManTexture.load();
            this.swingManRegion = TextureRegionFactory.extractFromTexture(this.swingManTexture);
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

        copSprite = new Sprite(CAMERA_WIDTH/2 - copTexture.getWidth()/2, CAMERA_HEIGHT/2 - copTexture.getHeight()/2, copRegion, getVertexBufferObjectManager());

        treeSprite = new Sprite[5];
        walkingManSprite = new Sprite[5];

        for (int i = 0; i < 5; i++) {
            treeSprite[i] = new Sprite(x, y, treeRegion, getVertexBufferObjectManager());
            walkingManSprite[i] = new Sprite(0, CAMERA_HEIGHT - walkManTexture.getHeight(), walkManRegion, getVertexBufferObjectManager());
            if (i < 2) {
                x += CAMERA_WIDTH / 3;
                //walkingManSprite[i].setScale(2);
            } else if (i == 2) {
                y += treeTexture.getHeight();
                x -= treeTexture.getWidth()/2;
            } else if (i==3) {
                x -= CAMERA_WIDTH / 3;
            }

            walkingManSprite[i].setY(CAMERA_HEIGHT - walkingManSprite[i].getHeight());
            walkingManSprite[i].registerEntityModifier(new LoopEntityModifier(new MoveXModifier(i+3, 0, CAMERA_WIDTH - walkingManSprite[i].getWidth())));
            mScene.attachChild(treeSprite[i]);
            mScene.attachChild(walkingManSprite[i]);
        }

        mScene.attachChild(copSprite);

        copSprite.registerEntityModifier(new LoopEntityModifier(new ColorModifier(0.5f, 1, 0, 0, 0, 0, 1)));
        copSprite.registerEntityModifier(new MoveModifier(3, copSprite.getX(), walkingManSprite[4].getX(), copSprite.getY(), walkingManSprite[4].getY()));

//        mScene.registerUpdateHandler(new IUpdateHandler() {
//            @Override
//            public void onUpdate(float v) {
//                for (Sprite man : walkingManSprite) {
//                    if (man.getX() >= CAMERA_WIDTH - man.getWidth()) {
//                        man.setFlippedHorizontal(!man.isFlippedHorizontal());
//                        man.registerEntityModifier(new MoveXModifier(7, man.getX(), 0));
//                    } else if (man.getX() <= 0) {
//                        man.setFlippedHorizontal(!man.isFlippedHorizontal());
//                        man.registerEntityModifier(new MoveXModifier(7, man.getX(), CAMERA_WIDTH - man.getWidth()));
//                    }
//                }
//            }
//
//            @Override
//            public void reset() {
//
//            }
//        });



        //if won
//        myPrefs.edit().putBoolean("leftFinished", true).commit();
        // else
//        myPrefs.edit().putBoolean("leftFinished", false).commit();

//        Intent intent = new Intent(this, MainActivity.class);
//        startActivity(intent);
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
