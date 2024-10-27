package com.dikiytechies.rejojo.config;

import com.dikiytechies.rejojo.AddonConfig;

public class GlobalConfig {
    public static boolean enableTSResolve(boolean clientSide) {
        return AddonConfig.getCommonConfigInstance(clientSide).enableTSResolve.get();
    }
}
