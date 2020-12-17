// 
// Decompiled by Procyon v0.5.36
// 

package me.TheJokerDev.skywars.utils.messages;

import com.google.gson.stream.JsonWriter;

import java.io.IOException;

interface JsonRepresentedObject
{
    void writeJson(final JsonWriter p0) throws IOException;
}
