package me.jehn;

public class User {
    private String name;
    private int asset;
    private int liability;

    public User(String name, int asset, int liability) {
        this.name = name;
        this.asset = asset;
        this.liability = liability;
    }
    public User(){}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAsset() {
        return asset;
    }

    public void setAsset(int asset) {
        this.asset = asset;
    }

    public int getLiability() {
        return liability;
    }

    public void setLiability(int liability) {
        this.liability = liability;
    }
}
