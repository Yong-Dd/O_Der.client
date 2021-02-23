package com.yongdd.oder_re;

import android.net.Uri;

public class MenuUri {
    Menu menu;
    Uri uri;

    public MenuUri(Menu menu, Uri uri) {
        this.menu = menu;
        this.uri = uri;
    }

    public Menu getMenu() {
        return menu;
    }

    public void setMenu(Menu menu) {
        this.menu = menu;
    }

    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }
}
