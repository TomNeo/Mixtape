package com.heinousgames.game.shantelsmixtape.activities;

import android.content.Intent;
import android.content.SharedPreferences;

import com.heinousgames.game.shantelsmixtape.model.BlockSprite;
import com.heinousgames.game.shantelsmixtape.model.PlayerSprite;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.JumpModifier;
import org.andengine.entity.modifier.LoopEntityModifier;
import org.andengine.entity.modifier.MoveXModifier;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.bitmap.BitmapTexture;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.texture.region.TextureRegionFactory;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.ui.activity.SimpleBaseGameActivity;
import org.andengine.util.adt.io.in.IInputStreamOpener;
import org.andengine.util.debug.Debug;
import org.andengine.util.modifier.IModifier;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.util.Random;

/**
 * Created by shanus on 1/9/15.
 */
public class SelfDoubtActivity extends SimpleBaseGameActivity {

    private final int CAMERA_WIDTH = 480;
    private final int CAMERA_HEIGHT = 748;
    private final int GAME_LENGTH = 2000;

    private Camera mCamera;
    private Scene mScene;
    private Background mBackground;
    private PlayerSprite mPlayer;

    private TiledTextureRegion PlayerRegion;
    private ITexture PlayerTexture, BlockTexture;

    private float gameLength;

    SharedPreferences myPrefs;
    private ITextureRegion BlockRegion;

    public SelfDoubtActivity() {
        gameLength = GAME_LENGTH;
    }

    @Override
    protected void onCreateResources() {
        myPrefs = getSharedPreferences("levelCompletion", MODE_PRIVATE);

        try {
            this.PlayerTexture = new BitmapTexture(this.getTextureManager(), new IInputStreamOpener() {
                @Override
                public InputStream open() throws IOException {
                    return getAssets().open("gfx/PlayerRun.png");
                }
            });
            this.PlayerTexture.load();
            this.PlayerRegion = TiledTextureRegion.create(PlayerTexture,0,0,PlayerTexture.getWidth(),PlayerTexture.getHeight(),8,1);
        } catch (IOException e) {
            Debug.e(e);
        }

        try {
            this.BlockTexture = new BitmapTexture(this.getTextureManager(), new IInputStreamOpener() {
                @Override
                public InputStream open() throws IOException {
                    return getAssets().open("gfx/Block.png");
                }
            });
            this.BlockTexture.load();
            this.BlockRegion = TextureRegionFactory.extractFromTexture(this.BlockTexture);
        } catch (IOException e) {
            Debug.e(e);
        }
    }

    /**
     * Base Game Plan:
     * Simple tap screen anywhere to jump obstacle game. Obstacles can consist of pitfalls,
     * negative words, and possibly other dangers. Possibly use an entire sentence word for word
     * from that particular song to get that point across. Could spell out a sentence and at parts
     * with positive words have negative words be obstacles that you must jump over. They would
     * line up vertically with the positive words above them, this way the player must jump at
     * the right time to avoid the negative word from filling in the blank and to get the positive
     * word to fill in the blank. If can't find negative words, use spikes or pitfalls as usual.
     * Could be predetermined amount of spike hits before death. When you hit a spike, or just a
     * wall even, it should break apart and disappear (like walls from sonic?). 
     * @return
     */
    @Override
    protected Scene onCreateScene() {

        mScene = new Scene();

        mBackground = new Background(0, 0, 0, 1);

        mScene.setBackground(mBackground);

        // when game is over, save the data in the shared pref, finish the activity, and
        // start the main activity again
        mBackground = new Background(0, 0, 0, 1);

        mScene.setBackground(mBackground);

        mPlayer = new PlayerSprite((CAMERA_WIDTH/4-32),(CAMERA_HEIGHT/1.5f-32),PlayerRegion,getVertexBufferObjectManager(),this);
        mPlayer.animate(100,true);

        mScene.attachChild(mPlayer);

        final JumpModifier skippy = new JumpModifier(1,mPlayer.getX(),mPlayer.getX(),mPlayer.getY(),mPlayer.getY(),200);
        skippy.addModifierListener(new IModifier.IModifierListener<IEntity>(){
            @Override
            public void onModifierStarted(IModifier<IEntity> iEntityIModifier, IEntity iEntity) {
                mPlayer.setJumping(true);
            }

            @Override
            public void onModifierFinished(IModifier<IEntity> iEntityIModifier, IEntity iEntity) {
                mPlayer.setJumping(false);
            }
        });

        mScene.setOnSceneTouchListener(new IOnSceneTouchListener() {
            @Override
            public boolean onSceneTouchEvent(Scene scene, TouchEvent touchEvent) {
                if(!mPlayer.getJumping()){
                    skippy.reset();
                    mPlayer.registerEntityModifier(skippy);
                }
                return false;
            }
        });

        mScene.registerUpdateHandler(new IUpdateHandler() {
            int interval = 80;
            int count = 0;
            Random rand = new Random();
          @Override
           public void onUpdate(float time){
              gameLength--;
              count++;

              if(count == interval){
                  count = 0;
                  BlockSprite BlockSpriteBuffer = new BlockSprite(CAMERA_WIDTH, ((CAMERA_HEIGHT/3)+rand.nextInt(400)),BlockRegion,getVertexBufferObjectManager(),SelfDoubtActivity.this);
                  BlockSpriteBuffer.registerEntityModifier(new MoveXModifier(2,BlockSpriteBuffer.getX(),BlockSpriteBuffer.getX() - CAMERA_WIDTH));
                  mScene.attachChild(BlockSpriteBuffer);
              }

              if(gameLength < 0) {
                  if (mPlayer.getWinCondition())
                      myPrefs.edit().putBoolean("leftFinished", true).commit();
                  else
                      myPrefs.edit().putBoolean("leftFinished", false).commit();
                  gameLength = GAME_LENGTH;
                  Intent intent = new Intent(SelfDoubtActivity.this, MainActivity.class);
                  startActivity(intent);
              }
          }

            @Override
        public void reset(){}

        });

        //if won

      //  myPrefs.edit().putBoolean("leftFinished", true).commit();
        // else
     //   myPrefs.edit().putBoolean("leftFinished", false).commit();

        //Intent intent = new Intent(this, MainActivity.class);
       // startActivity(intent);

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
