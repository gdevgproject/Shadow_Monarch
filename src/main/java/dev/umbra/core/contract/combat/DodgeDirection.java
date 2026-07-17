package dev.umbra.core.contract.combat;

/** Compact client-selected direction; the server derives world motion from authoritative yaw. */
public enum DodgeDirection {
    FORWARD(0, 1), FORWARD_LEFT(-1, 1), LEFT(-1, 0), BACKWARD_LEFT(-1, -1),
    BACKWARD(0, -1), BACKWARD_RIGHT(1, -1), RIGHT(1, 0), FORWARD_RIGHT(1, 1);

    private final int strafe;
    private final int forward;

    DodgeDirection(int strafe, int forward) {
        this.strafe = strafe;
        this.forward = forward;
    }

    public int strafe() { return strafe; }
    public int forward() { return forward; }

    /**
     * Converts local movement axes into the compact action intent. Inputs are sign-normalized so a
     * diagonal has the same server-normalized travel distance as a cardinal dodge. No movement
     * input intentionally falls back to backward to create space instead of pushing into danger.
     */
    public static DodgeDirection fromMovementAxes(int forward, int strafe) {
        int normalizedForward = Integer.compare(forward, 0);
        int normalizedStrafe = Integer.compare(strafe, 0);
        for (DodgeDirection direction : values()) {
            if (direction.forward == normalizedForward && direction.strafe == normalizedStrafe) {
                return direction;
            }
        }
        return BACKWARD;
    }

    /**
     * Resolves the horizontal world-space X component from the player's authoritative yaw.
     * Minecraft's positive strafe input is right, which is negative X when facing south.
     */
    public double worldXForYawRadians(double yawRadians) {
        return -Math.sin(yawRadians) * forward - Math.cos(yawRadians) * strafe;
    }

    /** Resolves the horizontal world-space Z component from the player's authoritative yaw. */
    public double worldZForYawRadians(double yawRadians) {
        return Math.cos(yawRadians) * forward - Math.sin(yawRadians) * strafe;
    }

    public static DodgeDirection fromWireId(int wireId) {
        DodgeDirection[] values = values();
        return wireId >= 0 && wireId < values.length ? values[wireId] : null;
    }
}
