package me.TheJokerDev.skywars.config;

import java.io.File;

public interface IConfiguration {
    File getFile();

    void save();

    void addDefault(String paramString, Object paramObject, String... paramVarArgs);

    void createSection(String paramString, String... paramVarArgs);

    void setHeader(String... paramVarArgs);

    void set(String paramString, Object paramObject, String... paramVarArgs);
}
