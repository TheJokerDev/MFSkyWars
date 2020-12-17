package me.TheJokerDev.skywars.abilities;

public class AbilityLevel
{
    private int level;
    private int chance;
    private int price;
    private int value;
    private boolean hasvalue;

    public AbilityLevel(int paramInt1, int paramInt2, int paramInt3, boolean paramBoolean, int paramInt4) {
        setLevel(paramInt1);
        setChance(paramInt2);
        setPrice(paramInt3);
        setHasvalue(paramBoolean);
        setValue(paramInt4);
    }


    public int getLevel() { return this.level; }



    public void setLevel(int paramInt) { this.level = paramInt; }



    public int getChance() { return this.chance; }



    public void setChance(int paramInt) { this.chance = paramInt; }



    public int getPrice() { return this.price; }



    public void setPrice(int paramInt) { this.price = paramInt; }



    public int getValue() { return this.value; }



    public void setValue(int paramInt) { this.value = paramInt; }



    public boolean isHasvalue() { return this.hasvalue; }



    public void setHasvalue(boolean paramBoolean) { this.hasvalue = paramBoolean; }
}
