'use strict';

import {NativeModules, processColor} from 'react-native';
var Braintree = NativeModules.Braintree;

module.exports = {
  setup(token) {
    return new Promise(function(resolve, reject) {
      Braintree.setup(token, test => resolve(test), err => reject(err));
    });
  },

  getCardNonce(cardNumber, expirationMonth, expirationYear, cvv) {
    return new Promise(function(resolve, reject) {
      Braintree.getCardNonce(
        cardNumber,
        expirationMonth,
        expirationYear,
        cvv,
        nonce => resolve(nonce),
        err => reject(err)
      );
    });
  },

  showPaymentViewController(config = {}) {
    var options = {
      callToActionText: config.callToActionText,
      title: config.title,
      description: config.description,
      amount: config.amount,
    };
    return new Promise(function(resolve, reject) {
      Braintree.paymentRequest(
        options,
        nonce => resolve(nonce),
        error => reject(error)
      );
    });
  },

  showPayPalViewController() {
    return new Promise(function(resolve, reject) {
      Braintree.paypalRequest(nonce => resolve(nonce), error => reject(error));
    });
  },
};
