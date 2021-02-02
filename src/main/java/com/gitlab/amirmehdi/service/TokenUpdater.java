package com.gitlab.amirmehdi.service;

import com.gitlab.amirmehdi.domain.BourseCode;
import com.gitlab.amirmehdi.domain.enumeration.OMS;

public interface TokenUpdater {
    OMS getOMS();

    void updateAllTokens();

    void updateToken(BourseCode bourseCode);
}
