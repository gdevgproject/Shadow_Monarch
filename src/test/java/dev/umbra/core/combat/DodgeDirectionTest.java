package dev.umbra.core.combat;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import dev.umbra.core.contract.combat.DodgeDirection;
import org.junit.jupiter.api.Test;

public final class DodgeDirectionTest {
    @Test
    public void onlyKnownWireDirectionsAreAccepted() {
        assertEquals(DodgeDirection.FORWARD, DodgeDirection.fromWireId(0));
        assertEquals(DodgeDirection.FORWARD_RIGHT, DodgeDirection.fromWireId(7));
        assertNull(DodgeDirection.fromWireId(-1));
        assertNull(DodgeDirection.fromWireId(8));
    }

    @Test
    public void movementAxesCoverAllDirectionsAndDefaultToBackward() {
        assertEquals(DodgeDirection.BACKWARD, DodgeDirection.fromMovementAxes(0, 0));
        assertEquals(DodgeDirection.FORWARD, DodgeDirection.fromMovementAxes(1, 0));
        assertEquals(DodgeDirection.BACKWARD, DodgeDirection.fromMovementAxes(-1, 0));
        assertEquals(DodgeDirection.LEFT, DodgeDirection.fromMovementAxes(0, -1));
        assertEquals(DodgeDirection.RIGHT, DodgeDirection.fromMovementAxes(0, 1));
        assertEquals(DodgeDirection.FORWARD_LEFT, DodgeDirection.fromMovementAxes(1, -1));
        assertEquals(DodgeDirection.FORWARD_RIGHT, DodgeDirection.fromMovementAxes(1, 1));
        assertEquals(DodgeDirection.BACKWARD_LEFT, DodgeDirection.fromMovementAxes(-1, -1));
        assertEquals(DodgeDirection.BACKWARD_RIGHT, DodgeDirection.fromMovementAxes(-1, 1));
    }

    @Test
    public void worldDirectionMatchesMinecraftRelativeRightAndLeft() {
        // At yaw zero the player faces south (+Z), so right is west (-X).
        assertEquals(-1.0, DodgeDirection.RIGHT.worldXForYawRadians(0.0));
        assertEquals(0.0, DodgeDirection.RIGHT.worldZForYawRadians(0.0));
        assertEquals(1.0, DodgeDirection.LEFT.worldXForYawRadians(0.0));
        assertEquals(0.0, DodgeDirection.LEFT.worldZForYawRadians(0.0));
    }
}
