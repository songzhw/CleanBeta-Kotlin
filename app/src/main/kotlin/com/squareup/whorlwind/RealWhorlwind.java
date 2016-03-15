/*
 * Copyright (C) 2016 Square, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.squareup.whorlwind;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyPermanentlyInvalidatedException;
import android.security.keystore.KeyProperties;
import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import java.security.GeneralSecurityException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.security.spec.KeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.crypto.Cipher;
import okio.ByteString;
import rx.Observable;

import static android.Manifest.permission.USE_FINGERPRINT;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;

@TargetApi(Build.VERSION_CODES.M) //
final class RealWhorlwind extends Whorlwind {
  private final Context context;
  private final FingerprintManager fingerprintManager;
  private final Storage storage;
  private final String keyAlias;
  private final KeyStore keyStore;
  private final KeyPairGenerator keyGenerator;
  private final KeyFactory keyFactory;
  private final AtomicBoolean readerScanning;
  private final Object dataLock = new Object();

  RealWhorlwind(Context context, FingerprintManager fingerprintManager, Storage storage,
      String keyAlias, KeyStore keyStore, KeyPairGenerator keyGenerator, KeyFactory keyFactory) {
    this.context = context;
    this.fingerprintManager = fingerprintManager;
    this.storage = storage;
    this.keyAlias = keyAlias;
    this.keyStore = keyStore;
    this.keyGenerator = keyGenerator;
    this.keyFactory = keyFactory;

    readerScanning = new AtomicBoolean();
  }

  // Lint is being stupid. The permission is being checked first before accessing fingerprint APIs.
  @SuppressLint("MissingPermission") //
  @CheckResult @Override public boolean canStoreSecurely() {
    return checkSelfPermission(USE_FINGERPRINT) == PERMISSION_GRANTED
        && fingerprintManager.isHardwareDetected()
        && fingerprintManager.hasEnrolledFingerprints();
  }

  private int checkSelfPermission(String permission) {
    return context.checkPermission(permission, android.os.Process.myPid(),
        android.os.Process.myUid());
  }

  void checkCanStoreSecurely() {
    if (!canStoreSecurely()) {
      throw new IllegalStateException(
          "Can't store securely. Check canStoreSecurely() before attempting to read/write.");
    }
  }

  @Override public void write(@NonNull String name, @Nullable ByteString value) {
    checkCanStoreSecurely();

    synchronized (dataLock) {
      if (value == null) {
        storage.remove(name);
        return;
      }

      prepareKeyStore();

      try {
        Cipher cipher = createCipher();
        cipher.init(Cipher.ENCRYPT_MODE, getPublicKey());

        storage.put(name, ByteString.of(cipher.doFinal(value.toByteArray())));
      } catch (Exception e) {
        Log.w(TAG, String.format("Failed to write value for %s", name), e);
      }
    }
  }

  @CheckResult @Override public Observable<ReadResult> read(@NonNull String name) {
    checkCanStoreSecurely();

    return Observable.create(new FingerprintAuthOnSubscribe(fingerprintManager, storage, name, //
        readerScanning, dataLock, this));
  }

  /**
   * Prepares the key store and our keys for encrypting/decrypting. Keys will be generated if we
   * haven't done so yet, and keys will be re-generated if the old ones have been invalidated. In
   * both cases, our K/V store will be cleared before continuing.
   */
  void prepareKeyStore() {
    try {
      Key key = keyStore.getKey(keyAlias, null);
      Certificate certificate = keyStore.getCertificate(keyAlias);
      if (key != null && certificate != null) {
        try {
          createCipher().init(Cipher.DECRYPT_MODE, key);

          // We have a keys in the store and they're still valid.
          return;
        } catch (KeyPermanentlyInvalidatedException e) {
          Log.d(TAG, "Key invalidated.");
        }
      }

      storage.clear();

      keyGenerator.initialize(new KeyGenParameterSpec.Builder(keyAlias,
          KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT) //
          .setBlockModes(KeyProperties.BLOCK_MODE_ECB) //
          .setUserAuthenticationRequired(true) //
          .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_RSA_PKCS1) //
          .build());

      keyGenerator.generateKeyPair();
    } catch (GeneralSecurityException e) {
      throw new RuntimeException(e);
    }
  }

  static Cipher createCipher() throws GeneralSecurityException {
    return Cipher.getInstance(KeyProperties.KEY_ALGORITHM_RSA
        + "/"
        + KeyProperties.BLOCK_MODE_ECB
        + "/"
        + KeyProperties.ENCRYPTION_PADDING_RSA_PKCS1);
  }

  private PublicKey getPublicKey() throws GeneralSecurityException {
    PublicKey publicKey = keyStore.getCertificate(keyAlias).getPublicKey();

    // In contradiction to the documentation, the public key returned from the key store is only
    // unlocked after the user has authenticated with their fingerprint. This is unnecessary
    // (and broken) for encryption using asynchronous keys, so we work around this by re-creating
    // our own copy of the key. See known issues at
    // http://developer.android.com/reference/android/security/keystore/KeyGenParameterSpec.html
    KeySpec spec = new X509EncodedKeySpec(publicKey.getEncoded());
    return keyFactory.generatePublic(spec);
  }

  PrivateKey getPrivateKey() throws GeneralSecurityException {
    return (PrivateKey) keyStore.getKey(keyAlias, null);
  }
}
