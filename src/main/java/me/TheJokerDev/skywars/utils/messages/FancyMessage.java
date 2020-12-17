// 
// Decompiled by Procyon v0.5.36
// 

package me.TheJokerDev.skywars.utils.messages;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonWriter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.*;
import java.util.logging.Level;

public class FancyMessage implements JsonRepresentedObject, Cloneable, Iterable<MessagePart>, ConfigurationSerializable
{
    private List<MessagePart> messageParts;
    private String jsonString;
    private boolean dirty;
    private static JsonParser _stringParser;
    
    public FancyMessage clone() throws CloneNotSupportedException {
        final FancyMessage instance = (FancyMessage)super.clone();
        instance.messageParts = new ArrayList<MessagePart>(this.messageParts.size());
        for (int i = 0; i < this.messageParts.size(); ++i) {
            instance.messageParts.add(i, this.messageParts.get(i).clone());
        }
        instance.dirty = false;
        instance.jsonString = null;
        return instance;
    }
    
    public FancyMessage(final String firstPartText) {
        this(TextualComponent.rawText(firstPartText));
    }
    
    public FancyMessage(final TextualComponent firstPartText) {
        (this.messageParts = new ArrayList<MessagePart>()).add(new MessagePart(firstPartText));
        this.jsonString = null;
        this.dirty = false;
    }
    
    public FancyMessage() {
        this((TextualComponent)null);
    }
    
    public FancyMessage text(final String text) {
        final MessagePart latest = this.latest();
        latest.text = TextualComponent.rawText(text);
        this.dirty = true;
        return this;
    }
    
    public FancyMessage text(final TextualComponent text) {
        final MessagePart latest = this.latest();
        latest.text = text;
        this.dirty = true;
        return this;
    }
    
    public FancyMessage color(final ChatColor color) {
        if (!color.isColor()) {
            throw new IllegalArgumentException(color.name() + " is not a color");
        }
        this.latest().color = color;
        this.dirty = true;
        return this;
    }
    
    public FancyMessage style(final ChatColor... styles) {
        for (final ChatColor style : styles) {
            if (!style.isFormat()) {
                throw new IllegalArgumentException(style.name() + " is not a style");
            }
        }
        this.latest().styles.addAll(Arrays.asList(styles));
        this.dirty = true;
        return this;
    }
    
    public FancyMessage file(final String path) {
        this.onClick("open_file", path);
        return this;
    }
    
    public FancyMessage link(final String url) {
        this.onClick("open_url", url);
        return this;
    }
    
    public FancyMessage suggest(final String command) {
        this.onClick("suggest_command", command);
        return this;
    }
    
    public FancyMessage insert(final String command) {
        this.latest().insertionData = command;
        this.dirty = true;
        return this;
    }
    
    public FancyMessage command(final String command) {
        this.onClick("run_command", command);
        return this;
    }
    
    public FancyMessage achievementTooltip(final String name) {
        this.onHover("show_achievement", new JsonString("achievement." + name));
        return this;
    }
    
    public FancyMessage tooltip(final String text) {
        this.onHover("show_text", new JsonString(text));
        return this;
    }
    
    public FancyMessage tooltip(final Iterable<String> lines) {
        this.tooltip((String[])ArrayWrapper.toArray(lines, String.class));
        return this;
    }
    
    public FancyMessage tooltip(final String... lines) {
        final StringBuilder builder = new StringBuilder();
        for (int i = 0; i < lines.length; ++i) {
            builder.append(lines[i]);
            if (i != lines.length - 1) {
                builder.append('\n');
            }
        }
        this.tooltip(builder.toString());
        return this;
    }
    
    public FancyMessage formattedTooltip(final FancyMessage text) {
        for (final MessagePart component : text.messageParts) {
            if (component.clickActionData != null && component.clickActionName != null) {
                throw new IllegalArgumentException("The tooltip text cannot have click data.");
            }
            if (component.hoverActionData != null && component.hoverActionName != null) {
                throw new IllegalArgumentException("The tooltip text cannot have a tooltip.");
            }
        }
        this.onHover("show_text", text);
        return this;
    }
    
    public FancyMessage formattedTooltip(final FancyMessage... lines) {
        if (lines.length < 1) {
            this.onHover(null, null);
            return this;
        }
        final FancyMessage result = new FancyMessage();
        result.messageParts.clear();
        for (int i = 0; i < lines.length; ++i) {
            try {
                for (final MessagePart component : lines[i]) {
                    if (component.clickActionData != null && component.clickActionName != null) {
                        throw new IllegalArgumentException("The tooltip text cannot have click data.");
                    }
                    if (component.hoverActionData != null && component.hoverActionName != null) {
                        throw new IllegalArgumentException("The tooltip text cannot have a tooltip.");
                    }
                    if (!component.hasText()) {
                        continue;
                    }
                    result.messageParts.add(component.clone());
                }
                if (i != lines.length - 1) {
                    result.messageParts.add(new MessagePart(TextualComponent.rawText("\n")));
                }
            }
            catch (CloneNotSupportedException e) {
                Bukkit.getLogger().log(Level.WARNING, "Failed to clone object", e);
                return this;
            }
        }
        return this.formattedTooltip(result.messageParts.isEmpty() ? null : result);
    }
    
    public FancyMessage formattedTooltip(final Iterable<FancyMessage> lines) {
        return this.formattedTooltip((FancyMessage[])ArrayWrapper.toArray(lines, FancyMessage.class));
    }
    
    public FancyMessage translationReplacements(final String... replacements) {
        for (final String str : replacements) {
            this.latest().translationReplacements.add(new JsonString(str));
        }
        this.dirty = true;
        return this;
    }
    
    public FancyMessage translationReplacements(final FancyMessage... replacements) {
        for (final FancyMessage str : replacements) {
            this.latest().translationReplacements.add(str);
        }
        this.dirty = true;
        return this;
    }
    
    public FancyMessage translationReplacements(final Iterable<FancyMessage> replacements) {
        return this.translationReplacements((FancyMessage[])ArrayWrapper.toArray(replacements, FancyMessage.class));
    }
    
    public FancyMessage then(final String text) {
        return this.then(TextualComponent.rawText(text));
    }
    
    public FancyMessage then(final TextualComponent text) {
        if (!this.latest().hasText()) {
            throw new IllegalStateException("previous message part has no text");
        }
        this.messageParts.add(new MessagePart(text));
        this.dirty = true;
        return this;
    }
    
    public FancyMessage then() {
        if (!this.latest().hasText()) {
            throw new IllegalStateException("previous message part has no text");
        }
        this.messageParts.add(new MessagePart());
        this.dirty = true;
        return this;
    }
    
    @Override
    public void writeJson(final JsonWriter writer) throws IOException {
        if (this.messageParts.size() == 1) {
            this.latest().writeJson(writer);
        }
        else {
            writer.beginObject().name("text").value("").name("extra").beginArray();
            for (final MessagePart part : this) {
                part.writeJson(writer);
            }
            writer.endArray().endObject();
        }
    }
    
    public String toJSONString() {
        if (!this.dirty && this.jsonString != null) {
            return this.jsonString;
        }
        final StringWriter string = new StringWriter();
        final JsonWriter json = new JsonWriter((Writer)string);
        try {
            this.writeJson(json);
            json.close();
        }
        catch (IOException e) {
            throw new RuntimeException("invalid message");
        }
        this.jsonString = string.toString();
        this.dirty = false;
        return this.jsonString;
    }
    
    public void send(final Player player) {
        this.send((CommandSender)player, this.toJSONString());
    }
    
    private void send(final CommandSender sender, final String jsonString) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(this.toOldMessageFormat());
            return;
        }
        final Player player = (Player)sender;
        Bukkit.dispatchCommand((CommandSender)Bukkit.getConsoleSender(), "tellraw " + player.getName() + " " + jsonString);
    }
    
    public void send(final CommandSender sender) {
        this.send(sender, this.toJSONString());
    }
    
    public void send(final Iterable<? extends CommandSender> senders) {
        final String string = this.toJSONString();
        for (final CommandSender sender : senders) {
            this.send(sender, string);
        }
    }
    
    public String toOldMessageFormat() {
        final StringBuilder result = new StringBuilder();
        for (final MessagePart part : this) {
            result.append((part.color == null) ? "" : part.color);
            for (final ChatColor formatSpecifier : part.styles) {
                result.append(formatSpecifier);
            }
            result.append(part.text);
        }
        return result.toString();
    }
    
    private MessagePart latest() {
        return this.messageParts.get(this.messageParts.size() - 1);
    }
    
    private void onClick(final String name, final String data) {
        final MessagePart latest = this.latest();
        latest.clickActionName = name;
        latest.clickActionData = data;
        this.dirty = true;
    }
    
    private void onHover(final String name, final JsonRepresentedObject data) {
        final MessagePart latest = this.latest();
        latest.hoverActionName = name;
        latest.hoverActionData = data;
        this.dirty = true;
    }
    
    public Map<String, Object> serialize() {
        final HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("messageParts", this.messageParts);
        return map;
    }
    
    public static FancyMessage deserialize(final Map<String, Object> serialized) {
        final FancyMessage msg = new FancyMessage();
        msg.messageParts = (List<MessagePart>)serialized.get("messageParts");
        msg.jsonString = (serialized.containsKey("JSON") ? serialized.get("JSON").toString() : null);
        msg.dirty = !serialized.containsKey("JSON");
        return msg;
    }
    
    @Override
    public Iterator<MessagePart> iterator() {
        return this.messageParts.iterator();
    }
    
    public static FancyMessage deserialize(final String json) {
        final JsonObject serialized = FancyMessage._stringParser.parse(json).getAsJsonObject();
        final JsonArray extra = serialized.getAsJsonArray("extra");
        final FancyMessage returnVal = new FancyMessage();
        returnVal.messageParts.clear();
        for (final JsonElement mPrt : extra) {
            final MessagePart component = new MessagePart();
            final JsonObject messagePart = mPrt.getAsJsonObject();
            for (final Map.Entry<String, JsonElement> entry : messagePart.entrySet()) {
                if (TextualComponent.isTextKey(entry.getKey())) {
                    final Map<String, Object> serializedMapForm = new HashMap<String, Object>();
                    serializedMapForm.put("key", entry.getKey());
                    if (entry.getValue().isJsonPrimitive()) {
                        serializedMapForm.put("value", entry.getValue().getAsString());
                    }
                    else {
                        for (final Map.Entry<String, JsonElement> compositeNestedElement : entry.getValue().getAsJsonObject().entrySet()) {
                            serializedMapForm.put("value." + compositeNestedElement.getKey(), compositeNestedElement.getValue().getAsString());
                        }
                    }
                    component.text = TextualComponent.deserialize(serializedMapForm);
                }
                else if (MessagePart.stylesToNames.inverse().containsKey((Object)entry.getKey())) {
                    if (!entry.getValue().getAsBoolean()) {
                        continue;
                    }
                    component.styles.add((ChatColor)MessagePart.stylesToNames.inverse().get((Object)entry.getKey()));
                }
                else if (entry.getKey().equals("color")) {
                    component.color = ChatColor.valueOf(entry.getValue().getAsString().toUpperCase());
                }
                else if (entry.getKey().equals("clickEvent")) {
                    final JsonObject object = entry.getValue().getAsJsonObject();
                    component.clickActionName = object.get("action").getAsString();
                    component.clickActionData = object.get("value").getAsString();
                }
                else if (entry.getKey().equals("hoverEvent")) {
                    final JsonObject object = entry.getValue().getAsJsonObject();
                    component.hoverActionName = object.get("action").getAsString();
                    if (object.get("value").isJsonPrimitive()) {
                        component.hoverActionData = new JsonString(object.get("value").getAsString());
                    }
                    else {
                        component.hoverActionData = deserialize(object.get("value").toString());
                    }
                }
                else if (entry.getKey().equals("insertion")) {
                    component.insertionData = entry.getValue().getAsString();
                }
                else {
                    if (!entry.getKey().equals("with")) {
                        continue;
                    }
                    for (final JsonElement object2 : entry.getValue().getAsJsonArray()) {
                        if (object2.isJsonPrimitive()) {
                            component.translationReplacements.add(new JsonString(object2.getAsString()));
                        }
                        else {
                            component.translationReplacements.add(deserialize(object2.toString()));
                        }
                    }
                }
            }
            returnVal.messageParts.add(component);
        }
        return returnVal;
    }
    
    static {
        ConfigurationSerialization.registerClass((Class)FancyMessage.class);
        FancyMessage._stringParser = new JsonParser();
    }
}
