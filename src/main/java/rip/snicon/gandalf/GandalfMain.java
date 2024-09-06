package rip.snicon.gandalf;

import com.github.sniconmc.utils.UtilsMain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rip.snicon.gandalf.listener.SetPlayerTab;

public class GandalfMain {

    public static final Logger logger = LoggerFactory.getLogger(UtilsMain.class);

    public static void init() {
        logger.info("Gandalf initialized");

        GandalfManager gandalfManager = new GandalfManager();

        SetPlayerTab removePlayerTab = new SetPlayerTab();
    }
}
