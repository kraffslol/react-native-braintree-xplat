'use strict';

import { NativeModules, processColor } from 'react-native';
var RCTBraintree = NativeModules.Braintree;

var Braintree = {

	setupWithURLScheme(token, urlscheme) {
		return new Promise(function(resolve, reject) {
			RCTBraintree.setupWithURLScheme(token, urlscheme, function(success) {
				success == true ? resolve(true) : reject("Invalid Token");
			});
		});
	},

	setup(token) {
		return new Promise(function(resolve, reject) {
			RCTBraintree.setup(token, function(success) {
				success == true ? resolve(true) : reject("Invalid Token");
			});
		});
	},

	showPaymentViewController(config = {}) {
    var options = {
      tintColor: processColor(config.tintColor),
      bgColor: processColor(config.bgColor),
      barBgColor: processColor(config.barBgColor),
      barTintColor: processColor(config.barTintColor),
      callToActionText: config.callToActionText,
    };
		return new Promise(function(resolve, reject) {
			RCTBraintree.showPaymentViewController(options, function(err, nonce) {
				nonce != null ? resolve(nonce) : reject(err);
			});
		});
	},

	showPayPalViewController() {
		return new Promise(function(resolve, reject) {
			RCTBraintree.showPayPalViewController(function(err, nonce) {
				nonce != null ? resolve(nonce) : reject(err);
			});
		});
	},

    getCardNonce(cardNumber, expirationMonth, expirationYear) {
    	return new Promise(function(resolve, reject) {
    		RCTBraintree.getCardNonce(cardNumber, expirationMonth, expirationYear, function(err, nonce) {
    			nonce != null ? resolve(nonce) : reject(err);
    		});
    	});
    }

};

module.exports = Braintree;
