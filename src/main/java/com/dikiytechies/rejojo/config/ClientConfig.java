package com.dikiytechies.rejojo.config;

import com.dikiytechies.rejojo.AddonConfig;

public class ClientConfig {
    public static boolean isNewTSIconsEnabled() {
        return AddonConfig.CLIENT.isNewTSIconsEnabled.get();
    }
}
