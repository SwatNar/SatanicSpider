/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.SatanicSpider.Storage.GameState;

import com.artemis.Entity;
import java.util.ArrayList;
import java.util.UUID;

/**
 *
 * @author bryant
 */
public class PlayerState
{
	public UUID player_id;
	public ArrayList<UUID> player_objects;
	public Entity player_game_entity;
	public Entity player_render_entity;
	public Entity player_physics_entity;
}
