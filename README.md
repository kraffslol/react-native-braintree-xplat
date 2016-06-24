# react-native-braintree-xplat
[![npm version](https://badge.fury.io/js/react-native-braintree-xplat.svg)](https://badge.fury.io/js/react-native-braintree-xplat)

An effort to merge react-native-braintree and react-native-braintree-android

## iOS Installation
You can use [`rnpm`](https://github.com/rnpm/rnpm) to add native dependencies automatically:

`$ rnpm link`

or do it manually as described below:

1. Run `npm install react-native-braintree-xplat --save`
2. Open your project in XCode, right click on `Libraries` and click `Add
   Files to "Your Project Name"` Look under `node_modules/react-native-braintree-xplat` and add `RCTBraintree.xcodeproj`.
3. Add `libRCTBraintree.a` to `Build Phases -> Link Binary With Libraries`
4. Done!

## Android Installation
Run `npm install react-native-braintree-xplat --save`

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
    private BraintreePackage mBraintreePackage; // <-- Add Package Variable
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
            mBraintreePackage = new BraintreePackage(this) // <---  Initialize the Package
        );
    }

    // Add onActivityResult
    @Override
    public void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        mBraintreePackage.handleActivityResult(requestCode, resultCode, data);
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

Example:
```js
const options = {
  bgColor: '#FFF',
  tintColor: 'orange'
}
```

___

PayPal only (iOS only)
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
BTClient.getCardNonce("4111111111111111", "10", "20").then(function(nonce) {
  //payment succeeded, pass nonce to server
})
.catch(function(err) {
  //error handling
});
```

## Credits

Big thanks to [@alanhhwong](https://github.com/alanhhwong) and [@surialabs](https://github.com/surialabs) for the original ios & android modules.
