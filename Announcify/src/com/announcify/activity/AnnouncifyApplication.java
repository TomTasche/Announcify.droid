
package com.announcify.activity;

import greendroid.app.GDApplication;

public class AnnouncifyApplication extends GDApplication {
    @Override
    public Class<?> getHomeActivityClass() {
        return AnnouncifyActivity.class;
    }
}
