'use strict';

import { NativeModules, processColor } from 'react-native';
var Braintree = NativeModules.Braintree;

module.exports = {
  paymentRequest(clientToken) {
    return new Promise(function(resolve, reject) {
      Braintree.paymentRequest(clientToken, (nonce) => resolve(nonce), (error) => reject(error));
    });
  },
};
