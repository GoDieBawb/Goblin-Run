/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.math.Vector2f;
import tonegod.gui.core.Screen;


/**
 *
 * @author Bob
 */
public class InteractionManager extends AbstractAppState implements ActionListener {

  private SimpleApplication app;
  private AppStateManager   stateManager;
  private InputManager      inputManager;
  private boolean           left      = false;
  private boolean           right     = false;
  private Player            currentPlayer;

  @Override
  public void initialize(AppStateManager stateManager, Application app) {
    super.initialize(stateManager, app);
    this.app           = (SimpleApplication) app;
    this.stateManager  = this.app.getStateManager();
    this.inputManager  = this.app.getInputManager();
    this.currentPlayer = this.stateManager.getState(PlayerManager.class).currentPlayer;
    inputManager.setSimulateMouse(true);
    setUpKeys();
    }

  public void setUpKeys(){
    inputManager.addMapping("Left", new KeyTrigger(KeyInput.KEY_A));
    inputManager.addMapping("Right", new KeyTrigger(KeyInput.KEY_D));
    inputManager.addMapping("Click", new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
    inputManager.addListener(this, "Left");
    inputManager.addListener(this, "Right");
    inputManager.addListener(this, "Click");
    }

  public void onAction(String binding, boolean isPressed, float tpf) {
    
    if (binding.equals("Left")){
      left = isPressed;
      if (isPressed)
      currentPlayer.turn = -10;
      else
      currentPlayer.turn = 0;
    }
      
    if (binding.equals("Right")) {
      right = isPressed;
      if (isPressed)
      currentPlayer.turn = 10;
      else
      currentPlayer.turn = 0;
      }
    
    if (binding.equals("Click")) {
      Vector2f clickSpot = inputManager.getCursorPosition();
      float xSpot = clickSpot.getX();
      float ySpot = clickSpot.getY();
      Screen screen = stateManager.getState(GUIManager.class).screen;
      
      if (xSpot > screen.getWidth()/2)
      right = isPressed;
      
      if (xSpot < screen.getWidth()/2)
      left = isPressed;
      }
    
    }
  
  @Override
  public void update(float tpf){
    if (left)
    currentPlayer.turn = -10;
    else if (right)
    currentPlayer.turn = 10;
    else
    currentPlayer.turn = 0;

    }
  
  }
