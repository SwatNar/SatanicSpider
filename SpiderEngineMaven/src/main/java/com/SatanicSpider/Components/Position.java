/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.SatanicSpider.Components;

import com.SatanicSpider.artemis.Component;
import com.SatanicSpider.artemis.Entity;
import com.SatanicSpider.artemis.Message;
import org.jbox2d.common.Vec2;

/**
 *
 * @author bryant
 */
public class Position implements Component {

    public void init(Entity e) {

    }

    public void cleanup(Entity e) {

    }

    public void receiveMessage(Message m) {

    }
    public Vec2 pos;
    public boolean changed = true;
    public boolean dynamic = false; //Do not obay the 'changed' item

    public Position(Vec2 pos) {
        this.pos = pos;
    }
    
    public Position(float x, float y)
    {
        pos = new Vec2(x,y);
    }
}
