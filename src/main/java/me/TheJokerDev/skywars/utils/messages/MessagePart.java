// 
// Decompiled by Procyon v0.5.36
// 

package me.TheJokerDev.skywars.utils.messages;

import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;
import com.google.gson.stream.JsonWriter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.ConfigurationSerialization;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

final class MessagePart implements JsonRepresentedObject, ConfigurationSerializable, Cloneable
{
    ChatColor color;
    ArrayList<ChatColor> styles;
    String clickActionName;
    String clickActionData;
    String hoverActionName;
    JsonRepresentedObject hoverActionData;
    TextualComponent text;
    String insertionData;
    ArrayList<JsonRepresentedObject> translationReplacements;
    static final BiMap<ChatColor, String> stylesToNames;
    
    MessagePart(final TextualComponent text) {
        this.color = ChatColor.WHITE;
        this.styles = new ArrayList<ChatColor>();
        this.clickActionName = null;
        this.clickActionData = null;
        this.hoverActionName = null;
        this.hoverActionData = null;
        this.text = null;
        this.insertionData = null;
        this.translationReplacements = new ArrayList<JsonRepresentedObject>();
        this.text = text;
    }
    
    MessagePart() {
        this.color = ChatColor.WHITE;
        this.styles = new ArrayList<ChatColor>();
        this.clickActionName = null;
        this.clickActionData = null;
        this.hoverActionName = null;
        this.hoverActionData = null;
        this.text = null;
        this.insertionData = null;
        this.translationReplacements = new ArrayList<JsonRepresentedObject>();
        this.text = null;
    }
    
    boolean hasText() {
        return this.text != null;
    }
    
    public MessagePart clone() throws CloneNotSupportedException {
        final MessagePart obj = (MessagePart)super.clone();
        obj.styles = (ArrayList<ChatColor>)this.styles.clone();
        if (this.hoverActionData instanceof JsonString) {
            obj.hoverActionData = new JsonString(((JsonString)this.hoverActionData).getValue());
        }
        else if (this.hoverActionData instanceof FancyMessage) {
            obj.hoverActionData = ((FancyMessage)this.hoverActionData).clone();
        }
        obj.translationReplacements = (ArrayList<JsonRepresentedObject>)this.translationReplacements.clone();
        return obj;
    }
    
    @Override
    public void writeJson(final JsonWriter json) {
        try {
            json.beginObject();
            this.text.writeJson(json);
            json.name("color").value(this.color.name().toLowerCase());
            for (final ChatColor style : this.styles) {
                json.name((String)MessagePart.stylesToNames.get((Object)style)).value(true);
            }
            if (this.clickActionName != null && this.clickActionData != null) {
                json.name("clickEvent").beginObject().name("action").value(this.clickActionName).name("value").value(this.clickActionData).endObject();
            }
            if (this.hoverActionName != null && this.hoverActionData != null) {
                json.name("hoverEvent").beginObject().name("action").value(this.hoverActionName).name("value");
                this.hoverActionData.writeJson(json);
                json.endObject();
            }
            if (this.insertionData != null) {
                json.name("insertion").value(this.insertionData);
            }
            if (this.translationReplacements.size() > 0 && this.text != null && TextualComponent.isTranslatableText(this.text)) {
                json.name("with").beginArray();
                for (final JsonRepresentedObject obj : this.translationReplacements) {
                    obj.writeJson(json);
                }
                json.endArray();
            }
            json.endObject();
        }
        catch (IOException e) {
            Bukkit.getLogger().log(Level.WARNING, "A problem occured during writing of JSON string", e);
        }
    }
    
    public Map<String, Object> serialize() {
        final HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("text", this.text);
        map.put("styles", this.styles);
        map.put("color", this.color.getChar());
        map.put("hoverActionName", this.hoverActionName);
        map.put("hoverActionData", this.hoverActionData);
        map.put("clickActionName", this.clickActionName);
        map.put("clickActionData", this.clickActionData);
        map.put("insertion", this.insertionData);
        map.put("translationReplacements", this.translationReplacements);
        return map;
    }
    
    public static MessagePart deserialize(final Map<String, Object> serialized) {
        final MessagePart part = new MessagePart((TextualComponent) serialized.get("text"));
        part.styles = (ArrayList<ChatColor>)serialized.get("styles");
        part.color = ChatColor.getByChar(serialized.get("color").toString());
        part.hoverActionName = (String) serialized.get("hoverActionName");
        part.hoverActionData = (JsonRepresentedObject)serialized.get("hoverActionData");
        part.clickActionName = (String) serialized.get("clickActionName");
        part.clickActionData = (String) serialized.get("clickActionData");
        part.insertionData = (String) serialized.get("insertion");
        part.translationReplacements = (ArrayList<JsonRepresentedObject>) serialized.get("translationReplacements");
        return part;
    }
    
    static {
        final ImmutableBiMap.Builder<ChatColor, String> builder = ImmutableBiMap.builder();
        for (final ChatColor style : ChatColor.values()) {
            if (style.isFormat()) {
                String styleName = null;
                switch (style) {
                    case MAGIC: {
                        styleName = "obfuscated";
                        break;
                    }
                    case UNDERLINE: {
                        styleName = "underlined";
                        break;
                    }
                    default: {
                        styleName = style.name().toLowerCase();
                        break;
                    }
                }
                builder.put(style, styleName);
            }
        }
        stylesToNames = (BiMap)builder.build();
        ConfigurationSerialization.registerClass((Class)MessagePart.class);
    }
}
