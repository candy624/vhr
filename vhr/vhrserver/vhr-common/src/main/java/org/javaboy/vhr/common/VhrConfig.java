package org.javaboy.vhr.common;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created by candy on 2020/11/3.
 */
@Component
@ConfigurationProperties(prefix = "vhr")
public class VhrConfig {

    /** 上传路径 */
    private static String profile;

    public static String getProfile() {
        return profile;
    }

    public static void setProfile(String profile) {
        VhrConfig.profile = profile;
    }

    /**
     * 获取下载路径
     */
    public static String getDownloadPath()
    {
        return getProfile() + "/download/";
    }

    /**
     * 获取上传路径
     */
    public static String getUploadPath()
    {
        return getProfile() + "/upload";
    }
}
