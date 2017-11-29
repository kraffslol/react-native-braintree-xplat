package com.pw.droplet.braintree;

import java.util.Map;
import java.util.HashMap;

import com.braintreepayments.api.interfaces.BraintreeCancelListener;
import com.google.gson.Gson;

import android.content.Intent;
import android.content.Context;
import android.app.Activity;

import com.braintreepayments.api.PaymentRequest;
import com.braintreepayments.api.models.PaymentMethodNonce;
import com.braintreepayments.api.BraintreePaymentActivity;
import com.braintreepayments.api.BraintreeFragment;
import com.braintreepayments.api.exceptions.InvalidArgumentException;
import com.braintreepayments.api.exceptions.BraintreeError;
import com.braintreepayments.api.exceptions.ErrorWithResponse;
import com.braintreepayments.api.models.CardBuilder;
import com.braintreepayments.api.Card;
import com.braintreepayments.api.PayPal;
import com.braintreepayments.api.interfaces.PaymentMethodNonceCreatedListener;
import com.braintreepayments.api.interfaces.BraintreeErrorListener;

import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ActivityEventListener;
import com.facebook.react.bridge.ReadableMap;

public class Braintree extends ReactContextBaseJavaModule implements ActivityEventListener {
  private static final int PAYMENT_REQUEST = 1706816330;
  private String token;

  private Callback successCallback;
  private Callback errorCallback;

  private Context mActivityContext;

  private BraintreeFragment mBraintreeFragment;

  public Braintree(ReactApplicationContext reactContext) {
    super(reactContext);
    reactContext.addActivityEventListener(this);
  }

  @Override
  public String getName() {
    return "Braintree";
  }

  public String getToken() {
    return this.token;
  }

  public void setToken(String token) {
    this.token = token;
  }

  @ReactMethod
  public void setup(final String token, final Callback successCallback, final Callback errorCallback) {
    try {
      this.mBraintreeFragment = BraintreeFragment.newInstance(getCurrentActivity(), token);
      this.mBraintreeFragment.addListener(new BraintreeCancelListener() {
        @Override
        public void onCancel(int requestCode) {
          nonceErrorCallback("USER_CANCELLATION");
        }
      });
      this.mBraintreeFragment.addListener(new PaymentMethodNonceCreatedListener() {
        @Override
        public void onPaymentMethodNonceCreated(PaymentMethodNonce paymentMethodNonce) {
          nonceCallback(paymentMethodNonce.getNonce());
        }
      });
      this.mBraintreeFragment.addListener(new BraintreeErrorListener() {
        @Override
        public void onError(Exception error) {
          if (error instanceof ErrorWithResponse) {
            ErrorWithResponse errorWithResponse = (ErrorWithResponse) error;
            BraintreeError cardErrors = errorWithResponse.errorFor("creditCard");
            if (cardErrors != null) {
              Gson gson = new Gson();
              final Map<String, String> errors = new HashMap<>();
              BraintreeError numberError = cardErrors.errorFor("number");
              BraintreeError cvvError = cardErrors.errorFor("cvv");
              BraintreeError expirationDateError = cardErrors.errorFor("expirationDate");
              BraintreeError postalCode = cardErrors.errorFor("postalCode");

              if (numberError != null) {
                errors.put("card_number", numberError.getMessage());
              }

              if (cvvError != null) {
                errors.put("cvv", cvvError.getMessage());
              }

              if (expirationDateError != null) {
                errors.put("expiration_date", expirationDateError.getMessage());
              }

              // TODO add more fields
              if (postalCode != null) {
                errors.put("postal_code", postalCode.getMessage());
              }

              nonceErrorCallback(gson.toJson(errors));
            } else {
              nonceErrorCallback(errorWithResponse.getErrorResponse());
            }
          }
        }
      });
      this.setToken(token);
      successCallback.invoke(this.getToken());
    } catch (InvalidArgumentException e) {
      errorCallback.invoke(e.getMessage());
    }
  }

  @ReactMethod
  public void getCardNonce(final ReadableMap parameters, final Callback successCallback, final Callback errorCallback) {
    this.successCallback = successCallback;
    this.errorCallback = errorCallback;

    CardBuilder cardBuilder = new CardBuilder()
      .validate(true);

    if (parameters.hasKey("number"))
      cardBuilder.cardNumber(parameters.getString("number"));

    if (parameters.hasKey("cvv"))
      cardBuilder.cvv(parameters.getString("cvv"));

    // In order to keep compatibility with iOS implementation, do not accept expirationMonth and exporationYear,
    // accept rather expirationDate (which is combination of expirationMonth/expirationYear)
    if (parameters.hasKey("expirationDate"))
      cardBuilder.expirationDate(parameters.getString("expirationDate"));

    if (parameters.hasKey("cardholderName"))
      cardBuilder.cardholderName(parameters.getString("cardholderName"));

    if (parameters.hasKey("firstName"))
      cardBuilder.firstName(parameters.getString("firstName"));

    if (parameters.hasKey("lastName"))
      cardBuilder.lastName(parameters.getString("lastName"));

    if (parameters.hasKey("company"))
      cardBuilder.company(parameters.getString("company"));

    if (parameters.hasKey("countryName"))
      cardBuilder.countryName(parameters.getString("countryName"));

    if (parameters.hasKey("countryCodeAlpha2"))
      cardBuilder.countryCodeAlpha2(parameters.getString("countryCodeAlpha2"));

    if (parameters.hasKey("countryCodeAlpha3"))
      cardBuilder.countryCodeAlpha3(parameters.getString("countryCodeAlpha3"));

    if (parameters.hasKey("countryCodeNumeric"))
      cardBuilder.countryCodeNumeric(parameters.getString("countryCodeNumeric"));

    if (parameters.hasKey("locality"))
      cardBuilder.locality(parameters.getString("locality"));

    if (parameters.hasKey("postalCode"))
      cardBuilder.postalCode(parameters.getString("postalCode"));

    if (parameters.hasKey("region"))
      cardBuilder.region(parameters.getString("region"));

    if (parameters.hasKey("streetAddress"))
      cardBuilder.streetAddress(parameters.getString("streetAddress"));

    if (parameters.hasKey("extendedAddress"))
      cardBuilder.extendedAddress(parameters.getString("extendedAddress"));

    Card.tokenize(this.mBraintreeFragment, cardBuilder);
  }

  public void nonceCallback(String nonce) {
    this.successCallback.invoke(nonce);
  }

  public void nonceErrorCallback(String error) {
    this.errorCallback.invoke(error);
  }

  @ReactMethod
  public void paymentRequest(final ReadableMap options, final Callback successCallback, final Callback errorCallback) {
    this.successCallback = successCallback;
    this.errorCallback = errorCallback;
    PaymentRequest paymentRequest = null;

    String callToActionText = null;
    String title = null;
    String description = null;
    String amount = null;

    if (options.hasKey("callToActionText")) {
      callToActionText = options.getString("callToActionText");
    }

    if (options.hasKey("title")) {
      title = options.getString("title");
    }

    if (options.hasKey("description")) {
      description = options.getString("description");
    }

    if (options.hasKey("amount")) {
      amount = options.getString("amount");
    }

    paymentRequest = new PaymentRequest()
      .submitButtonText(callToActionText)
      .primaryDescription(title)
      .secondaryDescription(description)
      .amount(amount)
      .clientToken(this.getToken());

    (getCurrentActivity()).startActivityForResult(
      paymentRequest.getIntent(getCurrentActivity()),
      PAYMENT_REQUEST
    );
  }

  @ReactMethod
  public void paypalRequest(final Callback successCallback, final Callback errorCallback) {
    this.successCallback = successCallback;
    this.errorCallback = errorCallback;
    PayPal.authorizeAccount(this.mBraintreeFragment);
  }

  @Override
  public void onActivityResult(Activity activity, final int requestCode, final int resultCode, final Intent data) {
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
        case Activity.RESULT_CANCELED:
          this.errorCallback.invoke("USER_CANCELLATION");
          break;
        default:
          break;
      }
    }
  }

  public void onNewIntent(Intent intent){}
}
