package com.yongdd.oder_re;

public class Banner {
    String titleName;
    String menuName;
    String menuDesc;
    int menuId;
    String menuImgPath;

    public Banner(String titleName, String menuName, String menuDesc, int menuId, String menuImgPath) {
        this.titleName = titleName;
        this.menuName = menuName;
        this.menuDesc = menuDesc;
        this.menuId = menuId;
        this.menuImgPath = menuImgPath;
    }

    public String getTitleName() {
        return titleName;
    }

    public void setTitleName(String titleName) {
        this.titleName = titleName;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public String getMenuDesc() {
        return menuDesc;
    }

    public void setMenuDesc(String menuDesc) {
        this.menuDesc = menuDesc;
    }

    public int getMenuId() {
        return menuId;
    }

    public void setMenuId(int menuId) {
        this.menuId = menuId;
    }

    public String getMenuImgPath() {
        return menuImgPath;
    }

    public void setMenuImgPath(String menuImgPath) {
        this.menuImgPath = menuImgPath;
    }
}
