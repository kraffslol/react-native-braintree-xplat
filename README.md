# react-native-braintree-xplat
[![npm version](https://badge.fury.io/js/react-native-braintree-xplat.svg)](https://badge.fury.io/js/react-native-braintree-xplat)

An effort to merge react-native-braintree and react-native-braintree-android

## iOS Installation
You can use the React Native CLI to add native dependencies automatically:

`$ react-native link`

or do it manually as described below:

1. Run `npm install react-native-braintree-xplat --save`
2. Open your project in XCode, right click on `Libraries` and click `Add
   Files to "Your Project Name"` Look under `node_modules/react-native-braintree-xplat` and add `RCTBraintree.xcodeproj`.
3. Add `libRCTBraintree.a` to `Build Phases -> Link Binary With Libraries`
4. Done!

## Android Installation
Run `npm install react-native-braintree-xplat --save`
### RN 0.29 and over

In `android/settings.gradle`
```gradle
...

include ':react-native-braintree-xplat'
project(':react-native-braintree-xplat').projectDir = new File(rootProject.projectDir, '../node_modules/react-native-braintree-xplat/android')
```

In `android/app/build.gradle`

```gradle
...

dependencies {
    ...

    compile project(':react-native-braintree-xplat')
}
```

Register module (in `MainApplication.java`)

```java
import com.pw.droplet.braintree.BraintreePackage; // <--- Import Package
import android.content.Intent; // <--- Import Intent

public class MainApplication extends Application implements ReactApplication {

  private final ReactNativeHost mReactNativeHost = new ReactNativeHost(this) {
    @Override
    protected boolean getUseDeveloperSupport() {
      return BuildConfig.DEBUG;
    }

    @Override
    protected List<ReactPackage> getPackages() {
      return Arrays.<ReactPackage>asList(
          new MainReactPackage(),
          new BraintreePackage() // <--- Initialize the package
      );
    }
  };

  @Override
  public ReactNativeHost getReactNativeHost() {
      return mReactNativeHost;
  }
}

```
---
### RN 0.28 and under


In `android/settings.gradle`
```gradle
...

include ':react-native-braintree-xplat'
project(':react-native-braintree-xplat').projectDir = file('../node_modules/react-native-braintree-xplat/android')
```

In `android/app/build.gradle`

```gradle
...

dependencies {
    ...

    compile project(':react-native-braintree-xplat')
}
```

Register module (in `MainActivity.java`)

```java
import com.pw.droplet.braintree.BraintreePackage; // <--- Import Package
import android.content.Intent; // <--- Import Intent

public class MainActivity extends ReactActivity {
    /**
     * Returns the name of the main component registered from JavaScript.
     * This is used to schedule rendering of the component.
     */
    @Override
    protected String getMainComponentName() {
        return "example";
    }

    /**
     * Returns whether dev mode should be enabled.
     * This enables e.g. the dev menu.
     */
    @Override
    protected boolean getUseDeveloperSupport() {
        return BuildConfig.DEBUG;
    }

    /**
     * A list of packages used by the app. If the app uses additional views
     * or modules besides the default ones, add more packages here.
     */
    @Override
    protected List<ReactPackage> getPackages() {
        return Arrays.<ReactPackage>asList(
            new MainReactPackage(),
            new BraintreePackage() // <---  Initialize the Package
        );
    }
}

```

## Usage

### Setup
```js
var BTClient = require('react-native-braintree-xplat');
BTClient.setup(<token>);
```
You can find a demo client token [here](https://developers.braintreepayments.com/start/hello-client/ios/v3).

### Show Payment Screen (Android & iOS)
v.zero
```js
BTClient.showPaymentViewController(options).then(function(nonce) {
  //payment succeeded, pass nonce to server
})
.catch(function(err) {
  //error handling
});
```
**Options (iOS only)**
- bgColor - Background color for the view.
- tintColor - Tint color for the view.
- barBgColor - Background color for the navbar.
- barTintColor - Tint color for the navbar.
- callToActionText - Text for call to action button. (Works for both Android and iOS)

Example:
```js
const options = {
  bgColor: '#FFF',
  tintColor: 'orange',
  callToActionText: 'Save'
}
```

___

PayPal only
```js
BTClient.showPayPalViewController().then(function(nonce) {
  //payment succeeded, pass nonce to server
})
.catch(function(err) {
  //error handling
});
```

## Custom Integration
If you only want to tokenize credit card information, you can use the following:
```js
BTClient.getCardNonce("4111111111111111", "10", "20", "400").then(function(nonce) {
  //payment succeeded, pass nonce to server
})
.catch(function(err) {
  //error handling
});
```

## One Touch on iOS
To take advantage of [One Touch](https://developers.braintreepayments.com/guides/one-touch/overview/ios/v3), there are additional setup required:

1. Register a URL scheme in Xcode (should always start with YOUR Bundle ID)
[More info here](https://developers.braintreepayments.com/guides/paypal/client-side/ios/v3#register-a-url-type) TL;DR


#### Add CFBundleURLTypes to Info.Plist
```js
	<key>CFBundleURLTypes</key>
	<array>
	<dict>
		<key>CFBundleTypeRole</key>
		<string>Editor</string>
		<key>CFBundleURLName</key>
		<string>your.bundle.id</string>
		<key>CFBundleURLSchemes</key>
		<array>
			<string>your.bundle.id.payments</string>
		</array>
	</dict>
	</array>
```
#### WhiteList
If your app is built using iOS 9 as its Base SDK, then you must add URLs to a whitelist in your app's info.plist
```js
   <key>LSApplicationQueriesSchemes</key>
   <array>
     <string>com.paypal.ppclient.touch.v1</string>
     <string>com.paypal.ppclient.touch.v2</string>
     <string>com.venmo.touch.v2</string>
   </array>
```

2. For iOS: Use setupWithURLScheme instead, passing the url scheme you have registered in previous step
  ```js
  var BTClient = require('react-native-braintree');
  BTClient.setupWithURLScheme(token, 'your.bundle.id.payments');
  ```
  #### For xplat, you can do something like this:
  
  ```js
  
   if (Platform.OS === 'ios') {
        BTClient.setupWithURLScheme(token, 'your.bundle.id.payments');
    } else {
        BTClient.setup(token);
    }  
  ```

3. Add this delegate method (callback) to your AppDelegate.m
  ```objc
#import "RCTBraintree.h"

- (BOOL)application:(UIApplication *)application openURL:(NSURL *)url sourceApplication:(NSString *)sourceApplication annotation:(id)annotation {
  return [[RCTBraintree sharedInstance] application:application openURL:url sourceApplication:sourceApplication annotation:annotation];
}
  ```
## Credits

Big thanks to [@alanhhwong](https://github.com/alanhhwong) and [@surialabs](https://github.com/surialabs) for the original ios & android modules.
