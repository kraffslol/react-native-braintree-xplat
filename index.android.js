'use strict';

import { NativeModules, processColor } from 'react-native';
var Braintree = NativeModules.Braintree;

module.exports = {
  setup(token) {
    return new Promise(function(resolve, reject) {
      Braintree.setup(token, (test) => resolve(test));
    });
  },

  showPaymentViewController() {
    return new Promise(function(resolve, reject) {
      Braintree.paymentRequest((nonce) => resolve(nonce), (error) => reject(error));
    });
  },
};
