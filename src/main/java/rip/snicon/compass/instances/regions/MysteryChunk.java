package rip.snicon.compass.instances.regions;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.network.packet.server.play.ParticlePacket;
import net.minestom.server.particle.Particle;
import rip.snicon.compass.Main;
import rip.snicon.compass.player.MysteryPlayer;

import java.util.List;

public class MysteryChunk {
    private final Pos min;
    private final Pos max;

    public MysteryChunk(Pos pos1, Pos pos2) {
        this.min = new Pos(
                Math.min(pos1.x(), pos2.x()),
                Math.min(pos1.y(), pos2.y()),
                Math.min(pos1.z(), pos2.z())
        );
        this.max = new Pos(
                Math.max(pos1.x(), pos2.x()),
                Math.max(pos1.y(), pos2.y()),
                Math.max(pos1.z(), pos2.z())
        );
    }

    public Pos getMin() {
        return min;
    }

    public Pos getMax() {
        return max;
    }

    public boolean isInside(Pos position) {
        return position.x() >= min.x() && position.x() <= max.x()
                && position.y() >= min.y() && position.y() <= max.y()
                && position.z() >= min.z() && position.z() <= max.z();
    }

    public boolean isBordering(MysteryChunk otherChunk) {
        // Check if the chunks are adjacent along any face
        return (this.max.x() == otherChunk.min.x() || this.min.x() == otherChunk.max.x()) &&
                (this.max.z() >= otherChunk.min.z() && this.min.z() <= otherChunk.max.z()) &&
                (this.max.y() >= otherChunk.min.y() && this.min.y() <= otherChunk.max.y()) ||

                (this.max.z() == otherChunk.min.z() || this.min.z() == otherChunk.max.z()) &&
                        (this.max.x() >= otherChunk.min.x() && this.min.x() <= otherChunk.max.x()) &&
                        (this.max.y() >= otherChunk.min.y() && this.min.y() <= otherChunk.max.y());
    }

    public List<Pos> getDirectionVectors() {
        // Calculate direction vectors from the min corner to the max corner
        Pos vectorX = new Pos(max.x() - min.x(), 0, 0); // Positive X direction
        Pos vectorY = new Pos(0, max.y() - min.y(), 0); // Positive Y direction
        Pos vectorZ = new Pos(0, 0, max.z() - min.z()); // Positive Z direction

        return List.of(vectorX, vectorY, vectorZ);
    }

    public List<Object> getClosestFace(Pos playerPosition) {
        // Get the directional vectors from the earlier method
        List<Pos> directionalVectors = getDirectionVectors();
        Pos xVector = directionalVectors.get(0); // X-direction
        Pos yVector = directionalVectors.get(1); // Y-direction
        Pos zVector = directionalVectors.get(2); // Z-direction

        // Return the plane point and the two face vectors
        return List.of();
    }


    public void displayToPlayer(MysteryPlayer player) {
        // Calculate the lengths of each side
        double xLength = max.x() - min.x();
        double zLength = max.z() - min.z();
        double yMin = min.y();
        double yMax = max.y();

        // Display particles along the edges

        // Bottom rectangle (y = min)
        for (double x = min.x(); x <= max.x(); x += 0.5) {
            // Front edge (min.z())
            player.sendPacket(new ParticlePacket(
                    Particle.CRIT, true, x, yMin, min.z(), 0f, 0f, 0f, 0.05f, 1
            ));
            // Back edge (max.z())
            player.sendPacket(new ParticlePacket(
                    Particle.CRIT, true, x, yMin, max.z(), 0f, 0f, 0f, 0.05f, 1
            ));
        }
        for (double z = min.z(); z <= max.z(); z += 0.5) {
            // Left edge (min.x())
            player.sendPacket(new ParticlePacket(
                    Particle.CRIT, true, min.x(), yMin, z, 0f, 0f, 0f, 0.05f, 1
            ));
            // Right edge (max.x())
            player.sendPacket(new ParticlePacket(
                    Particle.CRIT, true, max.x(), yMin, z, 0f, 0f, 0f, 0.05f, 1
            ));
        }

        // Top rectangle (y = max)
        for (double x = min.x(); x <= max.x(); x += 0.5) {
            // Front edge (min.z())
            player.sendPacket(new ParticlePacket(
                    Particle.CRIT, true, x, yMax, min.z(), 0f, 0f, 0f, 0.05f, 1
            ));
            // Back edge (max.z())
            player.sendPacket(new ParticlePacket(
                    Particle.CRIT, true, x, yMax, max.z(), 0f, 0f, 0f, 0.05f, 1
            ));
        }
        for (double z = min.z(); z <= max.z(); z += 0.5) {
            // Left edge (min.x())
            player.sendPacket(new ParticlePacket(
                    Particle.CRIT, true, min.x(), yMax, z, 0f, 0f, 0f, 0.05f, 1
            ));
            // Right edge (max.x())
            player.sendPacket(new ParticlePacket(
                    Particle.CRIT, true, max.x(), yMax, z, 0f, 0f, 0f, 0.05f, 1
            ));
        }

        // Vertical edges
        for (double y = yMin; y <= yMax; y += 0.5) {
            // Four corners
            player.sendPacket(new ParticlePacket(
                    Particle.CRIT, true, min.x(), y, min.z(), 0f, 0f, 0f, 0.05f, 1
            ));
            player.sendPacket(new ParticlePacket(
                    Particle.CRIT, true, min.x(), y, max.z(), 0f, 0f, 0f, 0.05f, 1
            ));
            player.sendPacket(new ParticlePacket(
                    Particle.CRIT, true, max.x(), y, min.z(), 0f, 0f, 0f, 0.05f, 1
            ));
            player.sendPacket(new ParticlePacket(
                    Particle.CRIT, true, max.x(), y, max.z(), 0f, 0f, 0f, 0.05f, 1
            ));
        }
    }



}
