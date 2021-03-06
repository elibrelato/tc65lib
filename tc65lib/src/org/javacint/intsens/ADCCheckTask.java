package org.javacint.intsens;

//#if sdkns == "siemens"
import com.siemens.icm.io.*;
//#elif sdkns == "cinterion"
//# import com.cinterion.io.*;
//#endif
import java.util.TimerTask;
import org.javacint.logging.Logger;

public abstract class ADCCheckTask extends TimerTask {

    private final int adcNb;
    private final int diff;
    private int lastValue = -1000;
    private ADC adc;

    public ADCCheckTask(int adcNb, int diff) {
        this.adcNb = adcNb;
        this.diff = diff;
    }

    public void run() {
        try {
            if (adc == null) {
                adc = new ADC(adcNb, 0);
                if (Logger.BUILD_DEBUG) {
                    Logger.log("Setting up ADC " + adcNb);
                }
            }

            int value;
            synchronized (ADC.class) {
                Thread.sleep(300);
                value = adc.getValue();
                if (Logger.BUILD_DEBUG) {
                    Logger.log("ADC" + adcNb + ", got value " + value);
                }
            }

            considerValue(value);
        } catch (Exception ex) {
            if (Logger.BUILD_CRITICAL) {
                Logger.log(this + ".run", ex, true);
            }

            // If we fail, it's best that we don't run anymore
            cancel();
        }
    }

    public String toString() {
        return "ADCCheckTask";
    }

    protected void considerValue(int value) {
        if (Math.abs(value - lastValue) >= diff) {
            lastValue = value;
            changed(value);
        }
    }

    public abstract void changed(int temp);
}
