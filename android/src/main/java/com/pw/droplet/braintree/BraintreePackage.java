package com.pw.droplet.braintree;

import android.content.Intent;
import android.content.Context;

import com.facebook.react.ReactPackage;
import com.facebook.react.bridge.JavaScriptModule;
import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.uimanager.ViewManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BraintreePackage implements ReactPackage {
  private Context mContext;
  private Braintree mModuleInstance;

  public BraintreePackage(Context activityContext) {
    mContext = activityContext;
  }

  @Override
  public List<NativeModule> createNativeModules(ReactApplicationContext reactContext) {
    List<NativeModule> modules = new ArrayList<>();
    mModuleInstance = new Braintree(reactContext, mContext);

    modules.add(mModuleInstance);
    return modules;
  }

  @Override
  public List<Class<? extends JavaScriptModule>> createJSModules() {
    return Collections.emptyList();
  }

  @Override
  public List<ViewManager> createViewManagers(ReactApplicationContext reactContext) {
    return Collections.emptyList();
  }

  public void handleActivityResult(final int requestCode, final int resultCode, final Intent data) {
    mModuleInstance.handleActivityResult(requestCode, resultCode, data);
  }
}
