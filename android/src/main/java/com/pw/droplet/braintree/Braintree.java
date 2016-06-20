package com.pw.droplet.braintree;

import android.content.Intent;
import android.content.Context;
import android.app.Activity;

import com.braintreepayments.api.PaymentRequest;
import com.braintreepayments.api.models.PaymentMethodNonce;
import com.braintreepayments.api.BraintreePaymentActivity;

import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;

public class Braintree extends ReactContextBaseJavaModule {
  private static final int PAYMENT_REQUEST = 1;

  private Callback successCallback;
  private Callback errorCallback;

  private Context mActivityContext;

  public Braintree(ReactApplicationContext reactContext, Context activityContext) {
    super(reactContext);
    this.mActivityContext = activityContext;
  }

  @Override
  public String getName() {
    return "Braintree";
  }

  @ReactMethod
  public void paymentRequest(final String clientToken, final Callback successCallback, final Callback errorCallback) {
    this.successCallback = successCallback;
    this.errorCallback = errorCallback;

    PaymentRequest paymentRequest = new PaymentRequest()
    .clientToken(clientToken);

    ((Activity)this.mActivityContext).startActivityForResult(
      paymentRequest.getIntent(this.mActivityContext),
      PAYMENT_REQUEST
    );
  }

  public void handleActivityResult(final int requestCode, final int resultCode, final Intent data) {
    if (requestCode == PAYMENT_REQUEST) {
      switch (resultCode) {
        case Activity.RESULT_OK:
          PaymentMethodNonce paymentMethodNonce = data.getParcelableExtra(
            BraintreePaymentActivity.EXTRA_PAYMENT_METHOD_NONCE
          );
          this.successCallback.invoke(paymentMethodNonce.getNonce());
          break;
        case BraintreePaymentActivity.BRAINTREE_RESULT_DEVELOPER_ERROR:
        case BraintreePaymentActivity.BRAINTREE_RESULT_SERVER_ERROR:
        case BraintreePaymentActivity.BRAINTREE_RESULT_SERVER_UNAVAILABLE:
          this.errorCallback.invoke(
            data.getSerializableExtra(BraintreePaymentActivity.EXTRA_ERROR_MESSAGE)
          );
          break;
        default:
          break;
      }
    }
  }
}
