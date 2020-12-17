package me.TheJokerDev.skywars.utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public enum ParticleEffect {
    EXPLOSION_NORMAL("explode", 0, -1, ParticleProperty.DIRECTIONAL),
    EXPLOSION_LARGE("largeexplode", 1, -1),
    EXPLOSION_HUGE("hugeexplosion", 2, -1),
    FIREWORKS_SPARK("fireworksSpark", 3, -1, ParticleProperty.DIRECTIONAL),
    WATER_BUBBLE("bubble", 4, -1, ParticleProperty.DIRECTIONAL, ParticleProperty.REQUIRES_WATER),
    WATER_SPLASH("splash", 5, -1, ParticleProperty.DIRECTIONAL),
    WATER_WAKE("wake", 6, 7, ParticleProperty.DIRECTIONAL),
    SUSPENDED("suspended", 7, -1, ParticleProperty.REQUIRES_WATER),
    SUSPENDED_DEPTH("depthSuspend", 8, -1, ParticleProperty.DIRECTIONAL),
    CRIT("crit", 9, -1, ParticleProperty.DIRECTIONAL),
    CRIT_MAGIC("magicCrit", 10, -1, ParticleProperty.DIRECTIONAL),
    SMOKE_NORMAL("smoke", 11, -1, ParticleProperty.DIRECTIONAL),
    SMOKE_LARGE("largesmoke", 12, -1, ParticleProperty.DIRECTIONAL),
    SPELL("spell", 13, -1),
    SPELL_INSTANT("instantSpell", 14, -1),
    SPELL_MOB("mobSpell", 15, -1, ParticleProperty.COLORABLE),
    SPELL_MOB_AMBIENT("mobSpellAmbient", 16, -1, ParticleProperty.COLORABLE),
    SPELL_WITCH("witchMagic", 17, -1),
    DRIP_WATER("dripWater", 18, -1),
    DRIP_LAVA("dripLava", 19, -1),
    VILLAGER_ANGRY("angryVillager", 20, -1),
    VILLAGER_HAPPY("happyVillager", 21, -1, ParticleProperty.DIRECTIONAL),
    TOWN_AURA("townaura", 22, -1, ParticleProperty.DIRECTIONAL),
    NOTE("note", 23, -1, ParticleProperty.COLORABLE),
    PORTAL("portal", 24, -1, ParticleProperty.DIRECTIONAL),
    ENCHANTMENT_TABLE("enchantmenttable", 25, -1, ParticleProperty.DIRECTIONAL),
    FLAME("flame", 26, -1, ParticleProperty.DIRECTIONAL),
    LAVA("lava", 27, -1),
    FOOTSTEP("footstep", 28, -1),
    CLOUD("cloud", 29, -1, ParticleProperty.DIRECTIONAL),
    REDSTONE("reddust", 30, -1, ParticleProperty.COLORABLE),
    SNOWBALL("snowballpoof", 31, -1),
    SNOW_SHOVEL("snowshovel", 32, -1, ParticleProperty.DIRECTIONAL),
    SLIME("slime", 33, -1),
    HEART("heart", 34, -1),
    BARRIER("barrier", 35, 8),
    ITEM_CRACK("iconcrack", 36, -1, ParticleProperty.DIRECTIONAL, ParticleProperty.REQUIRES_DATA),
    BLOCK_CRACK("blockcrack", 37, -1, ParticleProperty.REQUIRES_DATA),
    BLOCK_DUST("blockdust", 38, 7, ParticleProperty.DIRECTIONAL, ParticleProperty.REQUIRES_DATA),
    WATER_DROP("droplet", 39, 8),
    ITEM_TAKE("take", 40, 8),
    MOB_APPEARANCE("mobappearance", 41, 8);

    private static final Map<String, ParticleEffect> NAME_MAP;

    private static final Map<Integer, ParticleEffect> ID_MAP;

    private final String name;

    private final int id;

    private final int requiredVersion;

    private final List<ParticleProperty> properties;

    static {
        NAME_MAP = new HashMap<>();
        ID_MAP = new HashMap<>();
        for (ParticleEffect particleEffect : values()) {
            NAME_MAP.put(particleEffect.name, particleEffect);
            ID_MAP.put(Integer.valueOf(particleEffect.id), particleEffect);
        }
    }

    ParticleEffect(String paramString1, int paramInt1, int paramInt2, ParticleProperty... paramVarArgs) {
        this.name = paramString1;
        this.id = paramInt1;
        this.requiredVersion = paramInt2;
        this.properties = Arrays.asList(paramVarArgs);
    }

    public String getName() {
        return this.name;
    }

    public int getId() {
        return this.id;
    }

    public int getRequiredVersion() {
        return this.requiredVersion;
    }

    public boolean hasProperty(ParticleProperty paramParticleProperty) {
        return this.properties.contains(paramParticleProperty);
    }

    public boolean isSupported() {
        return (this.requiredVersion == -1 || ParticlePacket.getVersion() >= this.requiredVersion);
    }

    public static ParticleEffect fromName(String paramString) {
        for (Map.Entry<String, ParticleEffect> entry : NAME_MAP.entrySet()) {
            if (!entry.getKey().equalsIgnoreCase(paramString))
                continue;
            return entry.getValue();
        }
        return null;
    }

    public static ParticleEffect fromId(int paramInt) {
        for (Map.Entry<Integer, ParticleEffect> entry : ID_MAP.entrySet()) {
            if (entry.getKey().intValue() != paramInt)
                continue;
            return entry.getValue();
        }
        return null;
    }

    private static boolean isWater(Location paramLocation) {
        Material material = paramLocation.getBlock().getType();
        return (material == Material.WATER || material == Material.STATIONARY_WATER);
    }

    private static boolean isLongDistance(Location paramLocation, List<Player> paramList) {
        String str = paramLocation.getWorld().getName();
        for (Player player : paramList) {
            Location location = player.getLocation();
            if (!str.equals(location.getWorld().getName()) || location.distanceSquared(paramLocation) < 65536.0D)
                continue;
            return true;
        }
        return false;
    }

    private static boolean isDataCorrect(ParticleEffect paramParticleEffect, ParticleData paramParticleData) {
        return (((paramParticleEffect == BLOCK_CRACK || paramParticleEffect == BLOCK_DUST) && paramParticleData instanceof BlockData) || (paramParticleEffect == ITEM_CRACK && paramParticleData instanceof ItemData));
    }

    private static boolean isColorCorrect(ParticleEffect paramParticleEffect, ParticleColor paramParticleColor) {
        return (((paramParticleEffect == SPELL_MOB || paramParticleEffect == SPELL_MOB_AMBIENT || paramParticleEffect == REDSTONE) && paramParticleColor instanceof OrdinaryColor) || (paramParticleEffect == NOTE && paramParticleColor instanceof NoteColor));
    }

    public void display(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, int paramInt, Location paramLocation, double paramDouble) {
        if (!isSupported())
            throw new ParticleVersionException("This particle effect is not supported by your server version");
        if (hasProperty(ParticleProperty.REQUIRES_DATA))
            throw new ParticleDataException("This particle effect requires additional data");
        if (hasProperty(ParticleProperty.REQUIRES_WATER) && !isWater(paramLocation))
            throw new IllegalArgumentException("There is no water at the center location");
        (new ParticlePacket(this, paramFloat1, paramFloat2, paramFloat3, paramFloat4, paramInt, (paramDouble > 256.0D), null)).sendTo(paramLocation, paramDouble);
    }

    public void display(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, int paramInt, Location paramLocation, List<Player> paramList) {
        if (!isSupported())
            throw new ParticleVersionException("This particle effect is not supported by your server version");
        if (hasProperty(ParticleProperty.REQUIRES_DATA))
            throw new ParticleDataException("This particle effect requires additional data");
        if (hasProperty(ParticleProperty.REQUIRES_WATER) && !isWater(paramLocation))
            throw new IllegalArgumentException("There is no water at the center location");
        (new ParticlePacket(this, paramFloat1, paramFloat2, paramFloat3, paramFloat4, paramInt, isLongDistance(paramLocation, paramList), null)).sendTo(paramLocation, paramList);
    }

    public void display(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, int paramInt, Location paramLocation, Player... paramVarArgs) {
        display(paramFloat1, paramFloat2, paramFloat3, paramFloat4, paramInt, paramLocation, Arrays.asList(paramVarArgs));
    }

    public void display(Vector paramVector, float paramFloat, Location paramLocation, double paramDouble) {
        if (!isSupported())
            throw new ParticleVersionException("This particle effect is not supported by your server version");
        if (hasProperty(ParticleProperty.REQUIRES_DATA))
            throw new ParticleDataException("This particle effect requires additional data");
        if (!hasProperty(ParticleProperty.DIRECTIONAL))
            throw new IllegalArgumentException("This particle effect is not directional");
        if (hasProperty(ParticleProperty.REQUIRES_WATER) && !isWater(paramLocation))
            throw new IllegalArgumentException("There is no water at the center location");
        (new ParticlePacket(this, paramVector, paramFloat, (paramDouble > 256.0D), null)).sendTo(paramLocation, paramDouble);
    }

    public void display(Vector paramVector, float paramFloat, Location paramLocation, List<Player> paramList) {
        if (!isSupported())
            throw new ParticleVersionException("This particle effect is not supported by your server version");
        if (hasProperty(ParticleProperty.REQUIRES_DATA))
            throw new ParticleDataException("This particle effect requires additional data");
        if (!hasProperty(ParticleProperty.DIRECTIONAL))
            throw new IllegalArgumentException("This particle effect is not directional");
        if (hasProperty(ParticleProperty.REQUIRES_WATER) && !isWater(paramLocation))
            throw new IllegalArgumentException("There is no water at the center location");
        (new ParticlePacket(this, paramVector, paramFloat, isLongDistance(paramLocation, paramList), null)).sendTo(paramLocation, paramList);
    }

    public void display(Vector paramVector, float paramFloat, Location paramLocation, Player... paramVarArgs) {
        display(paramVector, paramFloat, paramLocation, Arrays.asList(paramVarArgs));
    }

    public void display(ParticleColor paramParticleColor, Location paramLocation, double paramDouble) {
        if (!isSupported())
            throw new ParticleVersionException("This particle effect is not supported by your server version");
        if (!hasProperty(ParticleProperty.COLORABLE))
            throw new ParticleColorException("This particle effect is not colorable");
        if (!isColorCorrect(this, paramParticleColor))
            throw new ParticleColorException("The particle color type is incorrect");
        (new ParticlePacket(this, paramParticleColor, (paramDouble > 256.0D))).sendTo(paramLocation, paramDouble);
    }

    public void display(ParticleColor paramParticleColor, Location paramLocation, List<Player> paramList) {
        if (!isSupported())
            throw new ParticleVersionException("This particle effect is not supported by your server version");
        if (!hasProperty(ParticleProperty.COLORABLE))
            throw new ParticleColorException("This particle effect is not colorable");
        if (!isColorCorrect(this, paramParticleColor))
            throw new ParticleColorException("The particle color type is incorrect");
        (new ParticlePacket(this, paramParticleColor, isLongDistance(paramLocation, paramList))).sendTo(paramLocation, paramList);
    }

    public void display(int paramInt1, int paramInt2, int paramInt3, int paramInt4, double paramDouble, Location paramLocation1, List<Player> paramList, ParticleColor paramParticleColor, Location paramLocation2, Player... paramVarArgs) {
        display(paramParticleColor, paramLocation2, Arrays.asList(paramVarArgs));
    }

    public void display(ParticleData paramParticleData, float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, int paramInt, Location paramLocation, double paramDouble) {
        if (!isSupported())
            throw new ParticleVersionException("This particle effect is not supported by your server version");
        if (!hasProperty(ParticleProperty.REQUIRES_DATA))
            throw new ParticleDataException("This particle effect does not require additional data");
        if (!isDataCorrect(this, paramParticleData))
            throw new ParticleDataException("The particle data type is incorrect");
        (new ParticlePacket(this, paramFloat1, paramFloat2, paramFloat3, paramFloat4, paramInt, (paramDouble > 256.0D), paramParticleData)).sendTo(paramLocation, paramDouble);
    }

    public void display(ParticleData paramParticleData, float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, int paramInt, Location paramLocation, List<Player> paramList) {
        if (!isSupported())
            throw new ParticleVersionException("This particle effect is not supported by your server version");
        if (!hasProperty(ParticleProperty.REQUIRES_DATA))
            throw new ParticleDataException("This particle effect does not require additional data");
        if (!isDataCorrect(this, paramParticleData))
            throw new ParticleDataException("The particle data type is incorrect");
        (new ParticlePacket(this, paramFloat1, paramFloat2, paramFloat3, paramFloat4, paramInt, isLongDistance(paramLocation, paramList), paramParticleData)).sendTo(paramLocation, paramList);
    }

    public void display(ParticleData paramParticleData, float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, int paramInt, Location paramLocation, Player... paramVarArgs) {
        display(paramParticleData, paramFloat1, paramFloat2, paramFloat3, paramFloat4, paramInt, paramLocation, Arrays.asList(paramVarArgs));
    }

    public void display(ParticleData paramParticleData, Vector paramVector, float paramFloat, Location paramLocation, double paramDouble) {
        if (!isSupported())
            throw new ParticleVersionException("This particle effect is not supported by your server version");
        if (!hasProperty(ParticleProperty.REQUIRES_DATA))
            throw new ParticleDataException("This particle effect does not require additional data");
        if (!isDataCorrect(this, paramParticleData))
            throw new ParticleDataException("The particle data type is incorrect");
        (new ParticlePacket(this, paramVector, paramFloat, (paramDouble > 256.0D), paramParticleData)).sendTo(paramLocation, paramDouble);
    }

    public void display(ParticleData paramParticleData, Vector paramVector, float paramFloat, Location paramLocation, List<Player> paramList) {
        if (!isSupported())
            throw new ParticleVersionException("This particle effect is not supported by your server version");
        if (!hasProperty(ParticleProperty.REQUIRES_DATA))
            throw new ParticleDataException("This particle effect does not require additional data");
        if (!isDataCorrect(this, paramParticleData))
            throw new ParticleDataException("The particle data type is incorrect");
        (new ParticlePacket(this, paramVector, paramFloat, isLongDistance(paramLocation, paramList), paramParticleData)).sendTo(paramLocation, paramList);
    }

    public void display(ParticleData paramParticleData, Vector paramVector, float paramFloat, Location paramLocation, Player... paramVarArgs) {
        display(paramParticleData, paramVector, paramFloat, paramLocation, Arrays.asList(paramVarArgs));
    }

    public enum ParticleProperty {
        REQUIRES_WATER, REQUIRES_DATA, DIRECTIONAL, COLORABLE
    }

    public static abstract class ParticleData {
        private final Material material;

        private final byte data;

        private final int[] packetData;

        public ParticleData(Material param1Material, byte param1Byte) {
            this.material = param1Material;
            this.data = param1Byte;
            this.packetData = new int[] { param1Material.getId(), param1Byte };
        }

        public Material getMaterial() {
            return this.material;
        }

        public byte getData() {
            return this.data;
        }

        public int[] getPacketData() {
            return this.packetData;
        }

        public String getPacketDataString() {
            return "_" + this.packetData[0] + "_" + this.packetData[1];
        }
    }

    public static final class ItemData extends ParticleData {
        public ItemData(Material param1Material, byte param1Byte) {
            super(param1Material, param1Byte);
        }
    }

    public static final class BlockData extends ParticleData {
        public BlockData(Material param1Material, byte param1Byte) {
            super(param1Material, param1Byte);
            if (!param1Material.isBlock())
                throw new IllegalArgumentException("The material is not a block");
        }
    }

    public static abstract class ParticleColor {
        public abstract float getValueX();

        public abstract float getValueY();

        public abstract float getValueZ();
    }

    public static final class OrdinaryColor extends ParticleColor {
        private final int red;

        private final int green;

        private final int blue;

        public OrdinaryColor(int param1Int1, int param1Int2, int param1Int3) {
            if (param1Int1 < 0)
                throw new IllegalArgumentException("The red value is lower than 0");
            if (param1Int1 > 255)
                throw new IllegalArgumentException("The red value is higher than 255");
            this.red = param1Int1;
            if (param1Int2 < 0)
                throw new IllegalArgumentException("The green value is lower than 0");
            if (param1Int2 > 255)
                throw new IllegalArgumentException("The green value is higher than 255");
            this.green = param1Int2;
            if (param1Int3 < 0)
                throw new IllegalArgumentException("The blue value is lower than 0");
            if (param1Int3 > 255)
                throw new IllegalArgumentException("The blue value is higher than 255");
            this.blue = param1Int3;
        }

        public OrdinaryColor(Color param1Color) {
            this(param1Color.getRed(), param1Color.getGreen(), param1Color.getBlue());
        }

        public int getRed() {
            return this.red;
        }

        public int getGreen() {
            return this.green;
        }

        public int getBlue() {
            return this.blue;
        }

        public float getValueX() {
            return this.red / 255.0F;
        }

        public float getValueY() {
            return this.green / 255.0F;
        }

        public float getValueZ() {
            return this.blue / 255.0F;
        }
    }

    public static final class NoteColor extends ParticleColor {
        private final int note;

        public NoteColor(int param1Int) {
            if (param1Int < 0)
                throw new IllegalArgumentException("The note value is lower than 0");
            if (param1Int > 24)
                throw new IllegalArgumentException("The note value is higher than 24");
            this.note = param1Int;
        }

        public float getValueX() {
            return this.note / 24.0F;
        }

        public float getValueY() {
            return 0.0F;
        }

        public float getValueZ() {
            return 0.0F;
        }
    }

    private static final class ParticleDataException extends RuntimeException {
        private static final long serialVersionUID = 3203085387160737484L;

        public ParticleDataException(String param1String) {
            super(param1String);
        }
    }

    private static final class ParticleColorException extends RuntimeException {
        private static final long serialVersionUID = 3203085387160737484L;

        public ParticleColorException(String param1String) {
            super(param1String);
        }
    }

    private static final class ParticleVersionException extends RuntimeException {
        private static final long serialVersionUID = 3203085387160737484L;

        public ParticleVersionException(String param1String) {
            super(param1String);
        }
    }

    public static final class ParticlePacket {
        private static int version;

        private static Class<?> enumParticle;

        private static Constructor<?> packetConstructor;

        private static Method getHandle;

        private static Field playerConnection;

        private static Method sendPacket;

        private static boolean initialized;

        private final ParticleEffect effect;

        private float offsetX;

        private final float offsetY;

        private final float offsetZ;

        private final float speed;

        private final int amount;

        private final boolean longDistance;

        private final ParticleEffect.ParticleData data;

        private Object packet;

        public ParticlePacket(ParticleEffect param1ParticleEffect, float param1Float1, float param1Float2, float param1Float3, float param1Float4, int param1Int, boolean param1Boolean, ParticleEffect.ParticleData param1ParticleData) {
            initialize();
            if (param1Float4 < 0.0F)
                throw new IllegalArgumentException("The speed is lower than 0");
            if (param1Int < 0)
                throw new IllegalArgumentException("The amount is lower than 0");
            this.effect = param1ParticleEffect;
            this.offsetX = param1Float1;
            this.offsetY = param1Float2;
            this.offsetZ = param1Float3;
            this.speed = param1Float4;
            this.amount = param1Int;
            this.longDistance = param1Boolean;
            this.data = param1ParticleData;
        }

        public ParticlePacket(ParticleEffect param1ParticleEffect, Vector param1Vector, float param1Float, boolean param1Boolean, ParticleEffect.ParticleData param1ParticleData) {
            this(param1ParticleEffect, (float)param1Vector.getX(), (float)param1Vector.getY(), (float)param1Vector.getZ(), param1Float, 0, param1Boolean, param1ParticleData);
        }

        public ParticlePacket(ParticleEffect param1ParticleEffect, ParticleEffect.ParticleColor param1ParticleColor, boolean param1Boolean) {
            this(param1ParticleEffect, param1ParticleColor.getValueX(), param1ParticleColor.getValueY(), param1ParticleColor.getValueZ(), 1.0F, 0, param1Boolean, null);
            if (param1ParticleEffect == ParticleEffect.REDSTONE && param1ParticleColor instanceof ParticleEffect.OrdinaryColor && ((ParticleEffect.OrdinaryColor)param1ParticleColor).getRed() == 0)
                this.offsetX = 1.17549435E-38F;
        }

        public static void initialize() {
            if (initialized)
                return;
            try {
                version = Integer.parseInt(Character.toString(ReflectionUtils.PackageType.getServerVersion().charAt(3)));
                if (version > 7)
                    enumParticle = ReflectionUtils.PackageType.MINECRAFT_SERVER.getClass("EnumParticle");
                Class<?> clazz = ReflectionUtils.PackageType.MINECRAFT_SERVER.getClass((version < 7) ? "Packet63WorldParticles" : "PacketPlayOutWorldParticles");
                packetConstructor = ReflectionUtils.getConstructor(clazz);
                getHandle = ReflectionUtils.getMethod("CraftPlayer", ReflectionUtils.PackageType.CRAFTBUKKIT_ENTITY, "getHandle");
                playerConnection = ReflectionUtils.getField("EntityPlayer", ReflectionUtils.PackageType.MINECRAFT_SERVER, false, "playerConnection");
                sendPacket = ReflectionUtils.getMethod(playerConnection.getType(), "sendPacket", ReflectionUtils.PackageType.MINECRAFT_SERVER.getClass("Packet"));
            } catch (Exception exception) {
                throw new VersionIncompatibleException("Your current bukkit version seems to be incompatible with this library", exception);
            }
            initialized = true;
        }

        public static int getVersion() {
            if (!initialized)
                initialize();
            return version;
        }

        public static boolean isInitialized() {
            return initialized;
        }

        private void initializePacket(Location param1Location) {
            if (this.packet != null)
                return;
            try {
                this.packet = packetConstructor.newInstance();
                if (version < 8) {
                    String str = this.effect.getName();
                    if (this.data != null)
                        str = str + this.data.getPacketDataString();
                    ReflectionUtils.setValue(this.packet, true, "a", str);
                } else {
                    ReflectionUtils.setValue(this.packet, true, "a", enumParticle.getEnumConstants()[this.effect.getId()]);
                    ReflectionUtils.setValue(this.packet, true, "j", Boolean.valueOf(true));
                    if (this.data != null) {
                        int[] arrayOfInt = this.data.getPacketData();
                        (new int[1])[0] = arrayOfInt[0] | arrayOfInt[1] << 12;
                        ReflectionUtils.setValue(this.packet, true, "k", (this.effect == ParticleEffect.ITEM_CRACK) ? arrayOfInt : new int[1]);
                    }
                }
                ReflectionUtils.setValue(this.packet, true, "b", Float.valueOf((float)param1Location.getX()));
                ReflectionUtils.setValue(this.packet, true, "c", Float.valueOf((float)param1Location.getY()));
                ReflectionUtils.setValue(this.packet, true, "d", Float.valueOf((float)param1Location.getZ()));
                ReflectionUtils.setValue(this.packet, true, "e", Float.valueOf(this.offsetX));
                ReflectionUtils.setValue(this.packet, true, "f", Float.valueOf(this.offsetY));
                ReflectionUtils.setValue(this.packet, true, "g", Float.valueOf(this.offsetZ));
                ReflectionUtils.setValue(this.packet, true, "h", Float.valueOf(this.speed));
                ReflectionUtils.setValue(this.packet, true, "i", Integer.valueOf(this.amount));
            } catch (Exception exception) {
                throw new PacketInstantiationException("Packet instantiation failed", exception);
            }
        }

        public void sendTo(Location param1Location, Player param1Player) {
            initializePacket(param1Location);
            try {
                sendPacket.invoke(playerConnection.get(getHandle.invoke(param1Player)), this.packet);
            } catch (Exception exception) {
                throw new PacketSendingException("Failed to send the packet to player '" + param1Player.getName() + "'", exception);
            }
        }

        public void sendTo(Location param1Location, List<Player> param1List) {
            if (param1List.isEmpty())
                return;
            for (Player player : param1List)
                sendTo(param1Location, player);
        }

        public void sendTo(Location param1Location, double param1Double) {
            if (param1Double < 1.0D)
                throw new IllegalArgumentException("The range is lower than 1");
            String str = param1Location.getWorld().getName();
            double d = param1Double * param1Double;
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (!player.getWorld().getName().equals(str) || player.getLocation().distanceSquared(param1Location) > d)
                    continue;
                sendTo(param1Location, player);
            }
        }

        private static final class VersionIncompatibleException extends RuntimeException {
            private static final long serialVersionUID = 3203085387160737484L;

            public VersionIncompatibleException(String param2String, Throwable param2Throwable) {
                super(param2String, param2Throwable);
            }
        }

        private static final class PacketInstantiationException extends RuntimeException {
            private static final long serialVersionUID = 3203085387160737484L;

            public PacketInstantiationException(String param2String, Throwable param2Throwable) {
                super(param2String, param2Throwable);
            }
        }

        private static final class PacketSendingException extends RuntimeException {
            private static final long serialVersionUID = 3203085387160737484L;

            public PacketSendingException(String param2String, Throwable param2Throwable) {
                super(param2String, param2Throwable);
            }
        }
    }
}

