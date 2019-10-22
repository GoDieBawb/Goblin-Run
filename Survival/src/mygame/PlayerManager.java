/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.animation.AnimControl;
import com.jme3.animation.LoopMode;
import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.asset.TextureKey;
import com.jme3.material.Material;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.texture.Texture;

/**
 *
 * @author Bob
 */
public class PlayerManager extends AbstractAppState{
    
  private SimpleApplication  app;
  private AssetManager       assetManager;
  private Material           mat;
  public  Player             currentPlayer;
    
  @Override
  public void initialize(AppStateManager stateManager, Application app) {
    this.app          = (SimpleApplication) app;
    this.assetManager = this.app.getAssetManager();
    mat               = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
    TextureKey key    = new TextureKey("Textures/Goblin/D.png", false);
    Texture tex       = assetManager.loadTexture(key);
    mat.setTexture("ColorMap", tex);
    createPlayer();
    }
  
  private void createPlayer() {
    currentPlayer             = new Player();
    currentPlayer.model       = (Node) assetManager.loadModel("Models/Goblin.j3o");
    currentPlayer.animControl = currentPlayer.model.getControl(AnimControl.class);
    currentPlayer.animChannel = currentPlayer.animControl.createChannel();
    currentPlayer.model.setMaterial(mat);
    currentPlayer.attachChild(currentPlayer.model);
    }
  
  public void startGame(){
    this.app.getRootNode().attachChild(currentPlayer);
    currentPlayer.turn   = 0;
    currentPlayer.isDead = false;
    currentPlayer.animChannel.setAnim("run");
    currentPlayer.animChannel.setLoopMode(LoopMode.Loop);
    currentPlayer.setLocalTranslation(0, 0, -30);
    this.app.getCamera().setLocation(currentPlayer.getLocalTranslation().add(0, 1.5f, -2));
    this.app.getCamera().lookAt(new Vector3f(0, 1, 0), new Vector3f(0, 1, 0));
    }
  
}
