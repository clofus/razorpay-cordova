package com.razorpay.cordova;

import com.razorpay.CheckoutActivity;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultWithDataListener;
import com.razorpay.PaymentData;
import org.json.JSONObject;
import org.apache.cordova.*;
import org.json.JSONArray;
import org.json.JSONException;
import android.widget.Toast;
import android.os.Bundle;
import android.content.Intent;
import android.app.Activity;


public class Main extends CordovaPlugin  implements PaymentResultWithDataListener {
  public static final String MAP_KEY_ERROR_CODE = "code";
  public static final String MAP_KEY_ERROR_DESC = "description";
  public static final String RAZORPAY_PAYMENT_ID = "razorpay_payment_id";
  public static final String RAZORPAY_SIGNATURE= "razorpay_signature";
  public static final String RAZORPAY_ORDER_ID = "razorpay_order_id";
  public CallbackContext cc;

  @Override
  public boolean execute(String action, JSONArray data, CallbackContext callbackContext) throws JSONException {
    this.cc = callbackContext;
    try{
      Intent intent = new Intent(this.cordova.getActivity(), CheckoutActivity.class);
      intent.putExtra("OPTIONS", data.getString(0));
      intent.putExtra("FRAMEWORK", "cordova");
      this.cordova.startActivityForResult((CordovaPlugin)this, intent, Checkout.RZP_REQUEST_CODE);
    } catch (Exception e){
      Toast.makeText(this.cordova.getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
    }
    return true;
  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent intent) {
    Checkout.handleActivityResult(this.cordova.getActivity(), requestCode, resultCode, intent, this);    
  }

  @Override
  public void onPaymentSuccess(String razorpayPaymentId, PaymentData paymentData) {
  
   try {
        JSONObject paymentsuccess = new JSONObject();
	    paymentsuccess.put(RAZORPAY_PAYMENT_ID, razorpayPaymentId);
	    paymentsuccess.put(RAZORPAY_SIGNATURE, paymentData.getSignature());
	    paymentsuccess.put(RAZORPAY_ORDER_ID, paymentData.getOrderId());
	    cc.success(paymentsuccess);
    } catch(Exception e){}
  } 

  @Override
  public void onPaymentError(int code, String description, PaymentData paymentData) {
    try {
        JSONObject error = new JSONObject();
        error.put(MAP_KEY_ERROR_CODE, code);
        error.put(MAP_KEY_ERROR_DESC, description);
        cc.error(error);
    } catch(Exception e){}
  }

}