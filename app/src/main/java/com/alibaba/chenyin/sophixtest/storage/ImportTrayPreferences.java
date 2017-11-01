package com.alibaba.chenyin.sophixtest.storage;

import android.content.Context;
import android.support.annotation.NonNull;

import net.grandcentrix.tray.TrayPreferences;

/**
 * Created by chenyin on 17/10/18.
 */

public class ImportTrayPreferences extends TrayPreferences {
    private static final String TAG = ImportTrayPreferences.class.getSimpleName();

    public ImportTrayPreferences(@NonNull final Context context, @NonNull final String module, final int version) {
        super(context, module, version);
    }

    @Override
    protected void onCreate(final int initialVersion) {
        super.onCreate(initialVersion);
    }

    private void importSharedPreferences() {

    }
}
