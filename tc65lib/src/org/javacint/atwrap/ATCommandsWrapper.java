package org.javacint.atwrap;

import com.siemens.icm.io.ATCommand;
import org.javacint.common.Strings;
import org.javacint.logging.Logger;
import org.javacint.settings.Settings;

/**
 * AT Commands wrapper.
 *
 * Most of the "dirty" AT commands handling code is put here.
 */
public class ATCommandsWrapper {

	private static final String THIS = "ATCW";

	public static String[] getMONC(ATCommand atc) {
		try {
			synchronized (atc) {
				String ret = atc.send("AT^SMONC\r");
				String[] spl = Strings.split('\n', ret);
				spl = Strings.split(',', spl[1].substring(7).
						trim());
				return spl;
			}
		} catch (Exception ex) {
			if (Logger.BUILD_CRITICAL) {
				Logger.log(THIS + ".getMCC", ex, true);
			}
			return null;
		}
	}

	/**
	 * Get the IMEI number of the GSM chip
	 *
	 * @param atc ATCommand used to get it
	 * @return IMEI number
	 */
	public static String getImei(ATCommand atc) {
		try {
			String retour;
			synchronized (atc) {
				retour = atc.send("AT+GSN\r");
			}
			String tab[] = Strings.split('\n', retour);
			return (tab[1]).trim();
		} catch (Exception ex) {
			if (Logger.BUILD_CRITICAL) {
				Logger.log(THIS + ".getImei", ex);
			}
			return null;
		}
	}

	/**
	 * Get the IMSI of the SIM card
	 *
	 * @param atc ATCommand
	 * @return SIM card IMSI
	 */
	public static String getImsi(ATCommand atc) {
		try {
			String ret;
			synchronized (atc) {
				ret = atc.send("AT+CIMI\r");
			}
			String tab[] = Strings.split('\n', ret);
			String imsi = (tab[1]).trim();

			if (imsi.equals("ERROR")) {
				return null;
			}
			return imsi;
		} catch (Exception ex) {
			System.out.println(THIS + ".getIMSI.19 : ex : " + ex.getClass().
					toString() + " : " + ex.getMessage());
			return null;
		}
	}

	/**
	 * Display the SIM card identification number
	 *
	 * @param atc ATCommand
	 * @return SIM card identification number
	 */
	public static String getScid(ATCommand atc) {
		try {
			String ret;
			synchronized (atc) {
				ret = atc.send("AT^SCID\r");
			}
			String tab[] = Strings.split('\n', ret);
			String scid = (tab[1]).trim().substring(7);
			if (Logger.BUILD_DEBUG) {
				Logger.log("Common.getImsi: \"" + scid + "\"");
			}
			if (scid.equals("ERROR")) {
				return null;
			}
			return scid;
		} catch (Exception ex) {
			if (Logger.BUILD_CRITICAL) {
				Logger.log(THIS + ".getScid", ex, true);
			}
			return null;
		}
	}

	/**
	 * Restarts the chip
	 *
	 * @param atc ATCommand to use
	 */
	public static void restart(ATCommand atc) {
		if (Logger.BUILD_DEBUG) {
			Logger.log(THIS + ".restart();");
		}
		try {
			synchronized (atc) {
				atc.send("AT+CFUN=1,1\r");
			}
		} catch (Exception ex) {
			if (Logger.BUILD_CRITICAL) {
				Logger.log(THIS + ".restart", ex);
			}
		}
	}

	public static void update(final ATCommand atc) {
		update(atc, null, null);
	}

	public static void update(final ATCommand atc, String target) {
		update(atc, null, target);
	}

	public static void update(ATCommand atc, String apn, String target) {
		if (apn == null) {
			apn = Settings.getSetting(Settings.SETTING_APN);
		}
		if (target == null) {
			target = Settings.getSetting(Settings.SETTING_JADURL);
		}
		if (Logger.BUILD_DEBUG) {
			Logger.log("ATCommandsWrapper.update( ATCommand, \"" + apn + "\", \"" + target + "\" );", true);
		}
		try {
			// If we are on a TC65 and not a TC65i (I hope I could remove this code someday soon)
			if (true) { // getChipName is broken, I need to fix it
				String[] spl = Strings.split(",", apn);
				apn = "";
				for (int i = 0; i < spl.length && i < 4; i++) {
					if (i > 0) {
						apn += ",";
					}

					// We need to remove "" parameters, they are the one
					// that make the AT^SJOTAP command fail
					if (spl[i].compareTo("\"\"") == 0) {
						continue;
					}

					apn += spl[i];
				}
			}


			String ret1 = null, ret2 = null;
			synchronized (atc) {
				if (apn != null && target != null) {
					ret1 = atc.send("AT^SJOTAP=," + target + ",a:,,," + apn + "\r");
					if (Logger.BUILD_DEBUG) {
						Logger.log("ATCommandsWrapper.update: ret1=\"" + ret1.replace('\r', '.').
								replace('\n', '.') + "\"", true);
					}
				} else {
					if (Logger.BUILD_DEBUG) {
						Logger.log("ATCommandsWrapper.update: No APN or no target !");
					}
				}
				ret2 = atc.send("AT^SJOTAP\r");
				if (Logger.BUILD_DEBUG) {
					Logger.log("ATCommandsWrapper.update: ret2=\"" + ret2.replace('\r', '.').
							replace('\n', '.') + "\"", true);
				}
			}
		} catch (Exception ex) {
			Logger.log(THIS + ".update();", ex, true);
		}
	}
}
