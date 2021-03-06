package com.stefanolupo.ndngame.libgdx.systems.core;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.physics.box2d.Body;
import com.stefanolupo.ndngame.libgdx.components.StateComponent;
import com.stefanolupo.ndngame.libgdx.components.enums.MotionState;
import com.stefanolupo.ndngame.libgdx.systems.HasComponentMappers;

/**
 * Sets the velocities of players (those with StateComponents) based on their state
 * The PhysicsSystem updates all of their positions using these updated velocities
 */
public class MovementSystem extends IteratingSystem implements HasComponentMappers {

    private static final Float MAX_VEL = 5f;

    public MovementSystem() {
        super(Family.all(StateComponent.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        StateComponent stateComponent = STATE_MAPPER.get(entity);
        Body body = BODY_MAPPER.get(entity).getBody();

        if (stateComponent.getHozState() == MotionState.MOVE_RIGHT) {
            lerpVelocityX(body, MAX_VEL);
        } else if (stateComponent.getHozState() == MotionState.MOVE_LEFT) {
            lerpVelocityX(body, -MAX_VEL);
        } else {
            lerpVelocityX(body, 0);
        }

        if (stateComponent.getVertState() == MotionState.MOVE_UP) {
            lerpVelocityY(body, MAX_VEL);
        } else if (stateComponent.getVertState() == MotionState.MOVE_DOWN) {
            lerpVelocityY(body, -MAX_VEL);
        } else {
            lerpVelocityY(body, 0);
        }
    }


    private void lerpVelocityX(Body body, float toValue) {
        body.setLinearVelocity(toValue, body.getLinearVelocity().y);
//        body.setLinearVelocity(MathUtils.lerp(body.getLinearVelocity().x, toValue, 0.2f), body.getLinearVelocity().y);
    }

    private void lerpVelocityY(Body body, float toValue) {
        body.setLinearVelocity(body.getLinearVelocity().x, toValue);
        //        body.setLinearVelocity(body.getLinearVelocity().x, MathUtils.lerp(body.getLinearVelocity().y, toValue, 0.2f));
    }
}
