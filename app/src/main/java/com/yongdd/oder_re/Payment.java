package com.yongdd.oder_re;

public class Payment {
    String menuName;
    int menuCount;
    int menuPrice;

    public Payment(String menuName, int menuCount, int menuPrice) {
        this.menuName = menuName;
        this.menuCount = menuCount;
        this.menuPrice = menuPrice;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public int getMenuCount() {
        return menuCount;
    }

    public void setMenuCount(int menuCount) {
        this.menuCount = menuCount;
    }

    public int getMenuPrice() {
        return menuPrice;
    }

    public void setMenuPrice(int menuPrice) {
        this.menuPrice = menuPrice;
    }
}
