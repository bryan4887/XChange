package org.knowm.xchange.bitfinex.v1.service;

import java.math.BigInteger;

import javax.crypto.Mac;

import org.knowm.xchange.service.BaseParamsDigest;

import net.iharder.Base64;
import si.mazi.rescu.RestInvocation;

public class BitfinexHmacPostBodyDigest extends BaseParamsDigest {

  /**
   * Constructor
   * 
   * @param secretKeyBase64
   * @throws IllegalArgumentException if key is invalid (cannot be base-64-decoded or the decoded key is invalid).
   */
  private BitfinexHmacPostBodyDigest(String secretKeyBase64) {

    super(secretKeyBase64, HMAC_SHA_384);
  }

  public static BitfinexHmacPostBodyDigest createInstance(String secretKeyBase64) {

    return secretKeyBase64 == null ? null : new BitfinexHmacPostBodyDigest(secretKeyBase64);
  }

  @Override
  public String digestParams(RestInvocation restInvocation) {

    String postBody = restInvocation.getRequestBody();
    Mac mac = getMac();
    mac.update(Base64.encodeBytes(postBody.getBytes()).getBytes());

    return String.format("%096x", new BigInteger(1, mac.doFinal()));
  }
  
  public String digestParams(String postBody) {
    Mac mac = getMac();
    mac.update(postBody.getBytes());
    return String.format("%096x", new BigInteger(1, mac.doFinal()));
  }
}
