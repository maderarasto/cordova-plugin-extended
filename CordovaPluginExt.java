package com.maderarasto;

import org.apache.cordova.CordovaPlugin;
import android.widget.Toast;

public class CordovaPluginExt extends CordovaPlugin {
    protected void pluginInitialize() {
        Toast.makeText(cordova.getActivity(), "Initialize", Toast.LENGTH_LONG).show();
    }
}
