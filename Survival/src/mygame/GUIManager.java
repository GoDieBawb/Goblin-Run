/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.input.event.MouseButtonEvent;
import com.jme3.math.Vector2f;
import tonegod.gui.controls.buttons.ButtonAdapter;
import tonegod.gui.core.Screen;

/**
 *
 * @author Bob
 */
public class GUIManager extends AbstractAppState {
  
  private SimpleApplication  app;
  private AppStateManager    stateManager;
  private AssetManager       assetManager;
  private ButtonAdapter      startButton;
  private BitmapText         scoreText;
  private BitmapFont         scoreFont;
  private boolean            isAttached;
  public  Screen             screen;

  @Override
  public void initialize(AppStateManager stateManager, Application app) {
    this.app          = (SimpleApplication) app;
    this.stateManager = this.app.getStateManager();
    this.assetManager = this.app.getAssetManager();
    screen            = new Screen(app, "tonegod/gui/style/atlasdef/style_map.gui.xml");
    scoreFont         = assetManager.loadFont("Interface/Fonts/Impact.fnt");
    scoreText         = new BitmapText(scoreFont, false);
    screen.setUseTextureAtlas(true,"tonegod/gui/style/atlasdef/atlas.png");
    screen.setUseMultiTouch(true);
    this.app.getGuiNode().addControl(screen);
    createMenu();
    addMenu();
    }
  
  private void createMenu(){
    startButton = new ButtonAdapter(screen, "LongRun", new Vector2f(15, 15) ) {
    @Override
      public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean toggled) {
        startGame();
        }
      };
    }
   
  public void addMenu() {
    isAttached = true;
    screen.addElement(startButton);
    startButton.setDimensions(screen.getWidth()/5, screen.getHeight()/5);
    startButton.setPosition(screen.getWidth()/2 - startButton.getWidth()/2, screen.getHeight()/2 - startButton.getHeight()/2);
    startButton.setFont("Interface/Fonts/Impact.fnt");
    startButton.setText("Start Running");
    this.app.getGuiNode().detachChild(scoreText);
    }
  
  private void clearScreen() {
    isAttached = false;
    screen.removeElement(startButton);
    }
  
  private void startGame(){
    if (isAttached) {
      clearScreen();
      this.app.getGuiNode().attachChild(scoreText);
      scoreText.setText(String.valueOf(stateManager.getState(SceneManager.class).floorsPassed));
      this.stateManager.getState(PlayerManager.class).startGame();
      this.stateManager.getState(SceneManager.class).startGame();
      }
    }
  
  private void updateScore() {
    scoreText.setText(String.valueOf(stateManager.getState(SceneManager.class).floorsPassed));
    scoreText.setLocalTranslation(screen.getWidth()/1.1f - scoreText.getLineWidth()/2f, screen.getWidth()/1.9f, 0);
    }
  
  @Override
  public void update(float tpf){
    updateScore();
    }
  }
