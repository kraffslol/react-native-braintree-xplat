# react-native-braintree-xplat
**WIP**

An effort to merge react-native-braintree and react-native-braintree-android

## IOS Usage

### IOS Setup
```js
var BTClient = require('react-native-braintree-xplat');
BTClient.setup(<token>);
```
You can find a demo client token [here](https://developers.braintreepayments.com/start/hello-client/ios/v3).

### Show Payment Screen
v.zero
```js
BTClient.showPaymentViewController(options).then(function(nonce) {
  //payment succeeded, pass nonce to server
})
.catch(function(err) {
  //error handling
});
```

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
BTClient.getCardNonce("4111111111111111", "10", "20").then(function(nonce) {
  //payment succeeded, pass nonce to server
})
.catch(function(err) {
  //error handling
});
```

## Installation IOS
You can use [`rnpm`](https://github.com/rnpm/rnpm) to add native dependencies automatically:

`$ rnpm link`

or do it manually as described below:

1. Run `npm install react-native-braintree-xplat --save`
2. Open your project in XCode, right click on `Libraries` and click `Add
   Files to "Your Project Name"` Look under `node_modules/react-native-linear-gradient` and add `RCTBraintree.xcodeproj`.
3. Add `libRCTBraintree.a` to `Build Phases -> Link Binary With Libraries`
4. Done!

## Credits

All credits go to Alan Wong for the ios module.
