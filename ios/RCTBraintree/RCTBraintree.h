//
//  RCTBraintree.h
//  RCTBraintree
//
//  Created by Rickard Ekman on 18/06/16.
//  Copyright © 2016 Rickard Ekman. All rights reserved.
//

#import <React/RCTBridgeModule.h>
#import <React/RCTUtils.h>
#import <React/RCTConvert.h>

#import "BraintreeCore.h"
#import "BraintreePayPal.h"
#import "BraintreeApplePay.h"
#import "BraintreeCard.h"
#import "BraintreeUI.h"
#import "Braintree3DSecure.h"
#import "BTDataCollector.h"
#import "PPDataCollector.h"

@import PassKit;

@interface RCTBraintree : UIViewController <RCTBridgeModule, BTDropInViewControllerDelegate, BTViewControllerPresentingDelegate, PKPaymentAuthorizationViewControllerDelegate>

@property (nonatomic, strong) BTAPIClient *braintreeClient;
@property (nonatomic, strong, readwrite) BTThreeDSecureDriver *threeDSecure;
@property (nonatomic, strong) UIViewController *reactRoot;
@property (nonatomic, strong) BTDataCollector *dataCollector;

// ApplePay
@property (nonatomic, strong) PKPaymentRequest *paymentRequest;
@property (nonatomic, strong) NSString *applePayNonce;

@property (nonatomic, strong) RCTResponseSenderBlock callback;
@property (nonatomic, strong) NSDictionary *threeDSecureOptions;

+ (instancetype)sharedInstance;
- (BOOL)application:(UIApplication *)application openURL:(NSURL *)url sourceApplication:(NSString *)sourceApplication annotation:(id)annotation;

@end
