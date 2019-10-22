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
import com.jme3.collision.CollisionResults;
import com.jme3.material.Material;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;
import java.util.Random;

/**
 *
 * @author Bob
 */
public class SceneManager extends AbstractAppState {

  private SimpleApplication  app;
  private AppStateManager    stateManager;
  private AssetManager       assetManager;
  private Node               sceneNode;
  private Node               floorNode;
  private Node               buildingNode;
  private Player             currentPlayer;
  public  int                floorsPassed;
  private Material           floorMat;
  private Material           buildMat1;
  private Material           buildMat2;
  private Material           buildMat3;
  private Material           buildMat4;
  private boolean            hasStarted;

  @Override
  public void initialize(AppStateManager stateManager, Application app) {
    this.app          = (SimpleApplication) app;
    this.stateManager = this.app.getStateManager();
    this.assetManager = this.app.getAssetManager();
    sceneNode         = new Node();
    buildingNode      = new Node();
    floorNode         = new Node();
    currentPlayer     = stateManager.getState(PlayerManager.class).currentPlayer;
    hasStarted        = false;
    this.app.getRootNode().attachChild(sceneNode);
    sceneNode.attachChild(buildingNode);
    sceneNode.attachChild(floorNode);
    initMaterials();
    }
  
  public void startGame() {
    Box box         = new Box(120, .2f, 120);
    Geometry floor  = new Geometry("The Floor", box);
    Box box1        = new Box(120, .2f, 120);
    Geometry floor1 = new Geometry("The Floor", box1);
    hasStarted      = true;
    
    floorNode.attachChild(floor);
    floorNode.attachChild(floor1);
    floor1.setLocalTranslation(0, 0, 60);
    floor.setMaterial(floorMat);
    floor1.setMaterial(floorMat);
    }
  
  private void initMaterials(){
    floorMat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
    floorMat.setTexture("ColorMap", assetManager.loadTexture("Textures/dirt.jpg"));
    
    buildMat1 = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
    buildMat1.setTexture("ColorMap", assetManager.loadTexture("Textures/Wall/D.png"));
     
    buildMat2 = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
    buildMat2.setTexture("ColorMap", assetManager.loadTexture("Textures/Brick/D.png"));
    
    buildMat3 = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
    buildMat3.setTexture("ColorMap", assetManager.loadTexture("Textures/CobbleWall/D.png"));
    
    buildMat4 = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
    buildMat4.setTexture("ColorMap", assetManager.loadTexture("Textures/Planks/D.png"));
    }
  
  private void createFloor(){
    Box box        = new Box(120, .2f, 120);
    Geometry floor = new Geometry("The Floor", box);
    floorNode.attachChild(floor);
    floor.setLocalTranslation(0, 0, 75f);
    floor.setMaterial(floorMat);
    }
  
  private void createBuilding() {
    System.out.println("Building Created " + buildingNode.getChildren().size());
    Node building        = new Node();
    Random rand          = new Random();
    int buildingChance   = rand.nextInt(3) + 1;
    Box box              = new Box(5, 5f, 5);
    Geometry buildingBox = new Geometry("A building", box);
    buildingBox.setMaterial(buildMat4);
    building.attachChild(buildingBox);
    
    if (buildingChance == 0)
    buildingBox.setMaterial(buildMat1);
    if (buildingChance == 1)
    buildingBox.setMaterial(buildMat2);
    if (buildingChance == 2)
    buildingBox.setMaterial(buildMat3);
    if (buildingChance == 3)
    buildingBox.setMaterial(buildMat4);

    buildingNode.attachChild(building);
    building.setLocalTranslation(rand.nextInt(150) - 75, 0, rand.nextInt(30) + 80);
    placeBuilding(building);
    }
  
  private void placeBuilding(Node building) {
    Random rand   = new Random();
    building.setLocalTranslation(rand.nextInt(150) - 75, 0, rand.nextInt(40) + 80);
    }
  
  private void lose() {
    currentPlayer.isDead = true;
    hasStarted           = false;
    floorsPassed         = 0;
    currentPlayer.animChannel.setAnim("idleA");
    buildingNode.detachAllChildren();
    floorNode.detachAllChildren();
    this.stateManager.getState(GUIManager.class).addMenu();
    }
 
  
  @Override
  public void update(float tpf) {
  if (!currentPlayer.isDead && hasStarted) {
     
    float moveSpeed = floorsPassed * 2f;
    if (moveSpeed > 80)
    moveSpeed = 80;
    if (moveSpeed < 20)
    moveSpeed = 20;
    
    Vector3f moveDir = new Vector3f(currentPlayer.turn, 0, -moveSpeed);
      
    for (int i = 0; i < buildingNode.getChildren().size(); i++ ) {
      
      Node currentBuilding = (Node) buildingNode.getChild(i);
      currentBuilding.setLocalTranslation(currentBuilding.getLocalTranslation().addLocal(moveDir.mult(tpf)));
      
      if (currentBuilding.getLocalTranslation().z < -50)
      placeBuilding(currentBuilding);
      
      }

    if (buildingNode.getChildren().size() < 8)
    createBuilding();
    
    for (int i = 0; i < floorNode.getChildren().size(); i++ ) {
      Geometry currentFloor = (Geometry) floorNode.getChild(i);
      currentFloor.setLocalTranslation(currentFloor.getLocalTranslation().addLocal(moveDir.mult(tpf)));
      
      if (currentFloor.getLocalTranslation().z < -100) {
        floorsPassed++;
        currentFloor.removeFromParent();
        createFloor();
        }
      
      }

    CollisionResults results = new CollisionResults();
    buildingNode.collideWith(currentPlayer.model.getWorldBound(), results);
    
    if(results.size() > 0)
    lose();
    
    }
  }
}
