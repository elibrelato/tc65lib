package org.javacint.watchdog;

//#if sdkns == "siemens"
import com.siemens.icm.misc.Watchdog;
//#elif sdkns == "cinterion"
//# //import com.cinterion.icm.misc.Watchdog;
//#endif

/**
 * Embedded watchdog management class for the TC65 v3 / TC65i chips.
 */
public class WatchdogEmbedded implements WatchdogActor {

    /**
     * Default constructor
     */
    public WatchdogEmbedded() {
        this(300);
    }

    /**
     * Constructor with specified timeout time
     *
     * @param time Watchdog timeout (in seconds [default: 300])
     */
    public WatchdogEmbedded(int time) {
        init(time);
    }

    /**
     * Initialization method
     *
     * @param timeout Watchdog timeout (in seconds)
     */
    private void init(int timeout) {
        Watchdog.start(timeout);
    }

    /**
     * Launch a signal to the watchdog
     *
     * @return always true
     */
    public boolean kick() {
        Watchdog.kick();
        return true;
    }
}