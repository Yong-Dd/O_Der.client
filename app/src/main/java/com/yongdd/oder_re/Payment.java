package com.yongdd.oder_re;

public class Payment {
    int menuId;
    String menuName;
    int menuTotalPrice;
    int menuTotalCount;
    String menuHotIce;

    public Payment(int menuId, String menuName, int menuTotalPrice, int menuTotalCount, String menuHotIce) {
        this.menuId = menuId;
        this.menuName = menuName;
        this.menuTotalPrice = menuTotalPrice;
        this.menuTotalCount = menuTotalCount;
        this.menuHotIce = menuHotIce;
    }

    public int getMenuId() {
        return menuId;
    }

    public void setMenuId(int menuId) {
        this.menuId = menuId;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public int getMenuTotalPrice() {
        return menuTotalPrice;
    }

    public void setMenuTotalPrice(int menuTotalPrice) {
        this.menuTotalPrice = menuTotalPrice;
    }

    public int getMenuTotalCount() {
        return menuTotalCount;
    }

    public void setMenuTotalCount(int menuTotalCount) {
        this.menuTotalCount = menuTotalCount;
    }

    public String getMenuHotIce() {
        return menuHotIce;
    }

    public void setMenuHotIce(String menuHotIce) {
        this.menuHotIce = menuHotIce;
    }
}
