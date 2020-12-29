package com.gitlab.amirmehdi.domain.enumeration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.gitlab.amirmehdi.domain.enumeration.OMS.*;

/**
 * The Broker enumeration.
 */
public enum Broker {
    REFAH(TADBIR, "https://silver.refahbroker.ir/Account/Login"),
    AGAH(ASA, "https://online.agah.com"),
    FIROOZE_ASIA(SAHRA, "https://firouzex.ephoenix.ir"),
    HAFEZ(SAHRA, "https://hafez.ephoenix.ir"),
    GANJINE(SAHRA, "https://gs.ephoenix.ir");
    private static final Map<OMS, List<Broker>> BY_OMS = new HashMap<>();

    static {
        for (Broker e : values()) {
            if (!BY_OMS.containsKey(e.oms)) {
                BY_OMS.put(e.oms, new ArrayList<>());
            }
            BY_OMS.get(e.oms).add(e);
        }
    }

    public final OMS oms;
    public final String url;

    Broker(OMS oms, String url) {
        this.oms = oms;
        this.url = url;
    }

    public static List<Broker> byOms(OMS oms) {
        return BY_OMS.get(oms);
    }
}
