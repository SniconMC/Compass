package rip.snicon.compass.npc.goals;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.EntityCreature;
import net.minestom.server.entity.Player;
import net.minestom.server.entity.ai.GoalSelector;
import rip.snicon.compass.Main;

public final class LookAtPlayerGoal extends GoalSelector {
    private Entity target;
    private final double range;
    private final Pos defaultPos;

    public LookAtPlayerGoal(EntityCreature entityCreature, double range, Pos defaultPos) {
        super(entityCreature);
        this.range = range;
        this.defaultPos = defaultPos;
    }

    @Override
    public boolean shouldStart() {
        // Find the closest target within range
        target = findTarget();
        return target != null;
    }

    @Override
    public void start() {
        // No initialization needed for this goal
    }

    @Override
    public void tick(long time) {
        target = findTarget(); // Update target dynamically
        if (target == null || entityCreature.getDistanceSquared(target) > range * range ||
                entityCreature.getInstance() != target.getInstance()) {
            resetHeadRotation();
            target = null;
            return;
        }

        Pos targetHeadPosition = target.getPosition().add(0, target.getEyeHeight(), 0);
        entityCreature.lookAt(targetHeadPosition);
    }


    @Override
    public boolean shouldEnd() {
        // End if there's no valid target
        return target == null;
    }

    @Override
    public void end() {
        // Reset the rotation when this goal ends
        resetHeadRotation();
    }

    private void resetHeadRotation() {
        // Reset the creature's rotation to its default view (facing forward)
        entityCreature.refreshPosition(defaultPos);
    }

    public Entity findTarget() {
        // Find the closest player within range
        return entityCreature.getInstance().getEntities()
                .stream()
                .filter(entity -> entity instanceof Player && entityCreature.getDistanceSquared(entity) <= range * range)
                .min((e1, e2) -> Double.compare(entityCreature.getDistanceSquared(e1), entityCreature.getDistanceSquared(e2)))
                .orElse(null);
    }
}
