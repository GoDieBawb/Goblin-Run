/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.animation.AnimChannel;
import com.jme3.animation.AnimControl;
import com.jme3.scene.Node;

/**
 *
 * @author Bob
 */
public class Player extends Node {
  public Node        model;
  public boolean     isDead;
  public int         turn;
  public AnimControl animControl;
  public AnimChannel animChannel;
  }
