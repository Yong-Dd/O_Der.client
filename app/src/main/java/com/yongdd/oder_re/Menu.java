package com.yongdd.oder_re;

public class Menu {
    int menuId;
    String menuName;
    int menuPrice;
    int menuCheckHotIce;
    String menuImgPath;

    public Menu(int menuId, String menuName, int menuPrice, int menuCheckHotIce, String menuImgPath) {
        this.menuId = menuId;
        this.menuName = menuName;
        this.menuPrice = menuPrice;
        this.menuCheckHotIce = menuCheckHotIce;
        this.menuImgPath = menuImgPath;
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

    public int getMenuPrice() {
        return menuPrice;
    }

    public void setMenuPrice(int menuPrice) {
        this.menuPrice = menuPrice;
    }

    public int getMenuCheckHotIce() {
        return menuCheckHotIce;
    }

    public void setMenuCheckHotIce(int menuCheckHotIce) {
        this.menuCheckHotIce = menuCheckHotIce;
    }

    public String getMenuImgPath() {
        return menuImgPath;
    }

    public void setMenuImgPath(String menuImgPath) {
        this.menuImgPath = menuImgPath;
    }
}
