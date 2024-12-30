package rip.snicon.compass.other;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import net.minestom.server.network.packet.server.play.ParticlePacket;
import net.minestom.server.particle.Particle;

import java.util.ArrayList;
import java.util.List;

public class LevelUp {

    private static final List<List<Pos>> HARDCODED_LETTERS = List.of(
            // "L"
            List.of(
                    new Pos(0, 2, 0), new Pos(0, 1, 0), new Pos(0, 0, 0), // Vertical line of "L"
                    new Pos(0, 0, 0), new Pos(1, 0, 0)                  // Horizontal line of "L"
            ),
            // "E"
            List.of(
                    new Pos(2, 0, 0), new Pos(2, 2, 0), // Vertical line of "E"
                    new Pos(3, 2, 0), new Pos(2, 2, 0),                  // Top horizontal line of "E"
                    new Pos(2, 1, 0), new Pos(2.5, 1, 0),                // Middle horizontal line of "E"
                    new Pos(2, 1, 0), new Pos(2, 0, 0), new Pos(3, 0, 0) // Bottom horizontal line of "E"
            ),
            // "V"
            List.of(
                    new Pos(4, 2, 0), new Pos(4.5, 0, 0),                // Left diagonal of "V"
                    new Pos(4.5, 0, 0), new Pos(5, 2, 0)                 // Right diagonal of "V"
            ),
            // "E"
            List.of(
                    new Pos(6, 0, 0), new Pos(6, 2, 0), // Vertical line of "E"
                    new Pos(7, 2, 0), new Pos(6, 2, 0),                  // Top horizontal line of "E"
                    new Pos(6, 1, 0), new Pos(6.5, 1, 0),                // Middle horizontal line of "E"
                    new Pos(6, 1, 0), new Pos(6, 0, 0), new Pos(7, 0, 0) // Bottom horizontal line of "E"
            ),
            // "L"
            List.of(
                    new Pos(8, 2, 0), new Pos(8, 1, 0), new Pos(8, 0, 0), // Vertical line of "L"
                    new Pos(8, 0, 0), new Pos(9, 0, 0)                  // Horizontal line of "L"
            ),
            // "E"
            List.of(
                    new Pos(10, 0, 0), new Pos(10, 2, 0), // Vertical line of "E"
                    new Pos(11, 2, 0), new Pos(10, 2, 0),                  // Top horizontal line of "E"
                    new Pos(10, 1, 0), new Pos(10.5, 1, 0),                // Middle horizontal line of "E"
                    new Pos(10, 1, 0), new Pos(10, 0, 0), new Pos(11, 0, 0) // Bottom horizontal line of "E"
            ),
            // "D"
            List.of(
                    new Pos(12, 2, 0), new Pos(12, 1, 0), new Pos(12, 0, 0), // Vertical line of "D"
                    new Pos(12, 2, 0), new Pos(13, 1, 0), new Pos(12, 0, 0)  // Curved diagonal to simulate a "D" shape
            ),
            // "U"
            List.of(
                    new Pos(5, -1, 0), new Pos(5, -2, 0), new Pos(5, -3, 0), // Left vertical line of "U"
                    new Pos(5, -3, 0), new Pos(6, -3, 0),                   // Bottom horizontal line of "U"
                    new Pos(6, -3, 0), new Pos(6, -2, 0), new Pos(6, -1, 0) // Right vertical line of "U"
            ),
            // "P"
            List.of(
                    new Pos(7, -1, 0), new Pos(7, -2, 0), new Pos(7, -3, 0), // Vertical line of "P"
                    new Pos(7, -1, 0), new Pos(8, -1.5, 0), new Pos(7, -2, 0) // Curved diagonal to simulate the loop of "P"
            )
    );




    /**
     * Triggers the level-up effect.
     *
     * @param levelingPlayer The player who leveled up.
     * @param scale  The scale factor for the letters.
     */
    public static void trigger(Player levelingPlayer, double scale) {
        Pos levelingPlayerPosition = levelingPlayer.getPosition().add(0,3,0);

        // Calculate the maximum x-coordinate across all points
        double maxX = HARDCODED_LETTERS.stream()
                .flatMap(List::stream)
                .mapToDouble(Pos::x)
                .max()
                .orElse(0);

        // Calculate the centering offset
        double centerOffset = (maxX * scale) / 2;

        // Adjust the points relative to the centered base position
        Pos basePosition = new Pos(levelingPlayerPosition.x() - centerOffset, levelingPlayerPosition.y() + 2, levelingPlayerPosition.z());

        // For each player within 48 blocks
        for (Player viewer : levelingPlayer.getInstance().getPlayers()) {
            if (viewer.getPosition().distance(levelingPlayerPosition) <= 48) {
                // Rotate the text to face the viewer
                List<List<Pos>> adjustedAndRotatedLetters = adjustAndRotateLettersToViewer(
                        HARDCODED_LETTERS, basePosition, scale, levelingPlayerPosition, viewer.getPosition());

                // Show particles to this viewer
                spawnParticles(adjustedAndRotatedLetters, viewer, 0.1);
            }
        }
    }


    /**
     * Adjusts and rotates letters to a viewer.
     *
     * @param letters           The points for each letter.
     * @param basePosition      The base position of the text.
     * @param scale             The scale factor.
     * @param levelingPlayerPos The position of the leveling player.
     * @param viewerPos         The position of the viewer.
     * @return A list of adjusted and rotated letters.
     */
    private static List<List<Pos>> adjustAndRotateLettersToViewer(List<List<Pos>> letters, Pos basePosition, double scale, Pos levelingPlayerPos, Pos viewerPos) {
        List<List<Pos>> adjustedAndRotatedLetters = new ArrayList<>();
        double dx = viewerPos.x() - levelingPlayerPos.x();
        double dz = viewerPos.z() - levelingPlayerPos.z();
        double yaw = Math.atan2(dz, dx) - Math.PI/2; // Yaw is the angle to the viewer

        for (List<Pos> letter : letters) {
            List<Pos> adjustedAndRotatedLetter = new ArrayList<>();
            for (Pos point : letter) {
                // Adjust and scale
                double adjustedX = basePosition.x() + point.x() * scale;
                double adjustedY = basePosition.y() + point.y() * scale;
                double adjustedZ = basePosition.z() + point.z() * scale;

                // Rotate around the Y-axis to face the viewer
                double relativeX = adjustedX - levelingPlayerPos.x();
                double relativeZ = adjustedZ - levelingPlayerPos.z();
                double rotatedX = relativeX * Math.cos(yaw) - relativeZ * Math.sin(yaw);
                double rotatedZ = relativeX * Math.sin(yaw) + relativeZ * Math.cos(yaw);

                // Add the rotated and adjusted position back
                adjustedAndRotatedLetter.add(new Pos(
                        rotatedX + levelingPlayerPos.x(),
                        adjustedY,
                        rotatedZ + levelingPlayerPos.z()
                ));
            }
            adjustedAndRotatedLetters.add(adjustedAndRotatedLetter);
        }
        return adjustedAndRotatedLetters;
    }


    /**
     * Spawns particles to connect points within each letter for the viewer.
     *
     * @param letters The points for each letter.
     * @param viewer  The player who sees the particles.
     * @param step    The distance between each particle along the line.
     */
    private static void spawnParticles(List<List<Pos>> letters, Player viewer, double step) {
        for (List<Pos> letter : letters) {
            for (int i = 0; i < letter.size() - 1; i++) {
                Pos start = letter.get(i);
                Pos end = letter.get(i + 1);

                // Calculate the direction vector and distance
                double dx = end.x() - start.x();
                double dy = end.y() - start.y();
                double dz = end.z() - start.z();
                double distance = Math.sqrt(dx * dx + dy * dy + dz * dz);

                // Normalize the direction vector
                double unitX = dx / distance;
                double unitY = dy / distance;
                double unitZ = dz / distance;

                // Spawn particles along the line at intervals of 'step'
                for (double d = 0; d <= distance; d += step) {
                    double x = start.x() + unitX * d;
                    double y = start.y() + unitY * d;
                    double z = start.z() + unitZ * d;

                    // Create and send the particle packet
                    ParticlePacket packet = new ParticlePacket(
                            Particle.END_ROD, // Particle type
                            true,          // Long distance
                            (float) x,
                            (float) y,
                            (float) z,
                            0f,            // Offset X
                            0f,            // Offset Y
                            0f,            // Offset Z
                            0f,            // Speed (static particle)
                            1              // Particle count
                    );

                    viewer.sendPacket(packet);
                }
            }
        }
    }
}

