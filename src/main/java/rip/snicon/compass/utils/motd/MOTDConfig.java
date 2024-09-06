package rip.snicon.compass.utils.motd;

import java.util.List;

/**
 * Represents the configuration for the Message of the Day (MOTD).
 *
 * <p>This class is used to store and retrieve the configuration for MOTD messages,
 * which are divided into two parts: {@code row1} and {@code row2}. Each row consists
 * of multiple lines, allowing for dynamic generation of MOTD messages with customizable
 * content.</p>
 *
 * <p>The configuration is typically loaded from JSON files, and the class provides
 * getter methods to access the configuration data.</p>
 *
 * @see MOTD
 *
 * @author znopp
 */
public class MOTDConfig {

    /**
     * The first part of the MOTD, represented as a list of strings.
     *
     * <p>This field contains multiple lists, where each sublist represents a line
     * of text to be displayed in the first row of the MOTD.</p>
     */
    private List<List<String>> row1;

    /**
     * The second part of the MOTD, represented as a list of strings.
     *
     * <p>This field contains multiple lists, where each sublist represents a line
     * of text to be displayed in the second row of the MOTD. This allows for
     * dynamic and random selection of content from multiple options.</p>
     */
    private List<List<String>> row2;

    /**
     * Retrieves the first part of the MOTD configuration.
     *
     * <p>This method returns the configuration for the first row of the MOTD,
     * which contains a list of strings representing lines of text.</p>
     *
     * @return a list of lists of strings representing the first row of the MOTD
     */
    public List<List<String>> getRow1() {
        return row1;
    }

    /**
     * Retrieves the second part of the MOTD configuration.
     *
     * <p>This method returns the configuration for the second row of the MOTD,
     * which contains a list of strings representing lines of text. This can include
     * multiple options, allowing for random selection of content.</p>
     *
     * @return a list of lists of strings representing the second row of the MOTD
     */
    public List<List<String>> getRow2() {
        return row2;
    }
}
