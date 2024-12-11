package com.maderarasto;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.json.JSONArray;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;

public class CordovaPluginExt extends CordovaPlugin {
    private ArrayList<Method> m_CordovaMethods;

    protected void pluginInitialize() {
        resolveJsMethods();
    }

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) {
        Stream<Method> foundMethodsStream = m_CordovaMethods.stream().filter(method -> {
            return method.getName().equals(action);
        });

        List<Method> foundMethods = foundMethodsStream.collect(Collectors.toList());

        if (foundMethods.isEmpty()) {
            return false;
        }

        try {
            return (Boolean) foundMethods.get(0).invoke(this, args, callbackContext);
        } catch (IllegalAccessException ex) {
            ex.printStackTrace();
        } catch (InvocationTargetException ex) {
            ex.printStackTrace();
        }
        
        return false;
    }


    private void resolveJsMethods() {
        Method[] methods = this.getClass().getMethods();
        ArrayList<Method> methodList = new ArrayList<>(Arrays.asList(methods));
        
        // Filter methods that have @JsMethod annotation.
        Stream<Method> methodsStream = methodList.stream().filter(method -> {
            return method.isAnnotationPresent(CordovaMethod.class);
        });

        m_CordovaMethods = (ArrayList<Method>) methodsStream.collect(Collectors.toList());
    }
}
