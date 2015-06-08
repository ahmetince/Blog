package com.canyapan.blog.androidencriptionusingrsa;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import android.app.Activity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	private PublicKey publicKey = null;
	private PrivateKey privateKey = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public void generateKeysClicked(View v) {
		final TextView publicKeyTextView = (TextView) findViewById(R.id.textViewPublicKey);
		final TextView privateKeyTextView = (TextView) findViewById(R.id.textViewPrivateKey);
		final EditText cipherTextView = (EditText) findViewById(R.id.editTextDecrypt);
		
		try {
			KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
	        keyPairGenerator.initialize(2048);
	        
	        KeyPair keyPair = keyPairGenerator.genKeyPair();
	        publicKey = keyPair.getPublic();
	        privateKey = keyPair.getPrivate();
	        
	        // Key �ok uzun oldu�undan sadece ilk 32 karakterini g�r�nt�leyece�iz.
	        publicKeyTextView.setText(Base64.encodeToString(publicKey.getEncoded(), Base64.DEFAULT | Base64.NO_WRAP).substring(0, 32) + "...");
	        privateKeyTextView.setText(Base64.encodeToString(privateKey.getEncoded(), Base64.DEFAULT | Base64.NO_WRAP).substring(0, 32) + "...");
	        
	        cipherTextView.setText("");
		} catch (Exception e) {
			e.printStackTrace();
			
			Toast.makeText(this, "Something went wrong :( Check logcat for details..", Toast.LENGTH_LONG).show();
		}
	}

	public void encryptButtonClicked(View v) {
		if (null == publicKey || null == privateKey) {
			Toast.makeText(this, "Keys are not ready. Please click on Generate Keys First.", Toast.LENGTH_LONG).show();
			return;
		}
		
		final EditText plainTextView = (EditText) findViewById(R.id.editTextEncrypt);
		final EditText cipherTextView = (EditText) findViewById(R.id.editTextDecrypt);

		final String plainText = plainTextView.getText().toString();

		String cipherText;
		try {
			cipherText = encrypt(plainText);
			cipherTextView.setText(cipherText);
		} catch (Exception e) {
			e.printStackTrace();
			
			Toast.makeText(this, "Something went wrong :( Check logcat for details..", Toast.LENGTH_LONG).show();
		}
	}

	public void decryptButtonClicked(View v) {
		if (null == publicKey || null == privateKey) {
			Toast.makeText(this, "Keys are not ready. Please click on Generate Keys First.", Toast.LENGTH_LONG).show();
			return;
		}
		
		final EditText plainTextView = (EditText) findViewById(R.id.editTextEncrypt);
		final EditText cipherTextView = (EditText) findViewById(R.id.editTextDecrypt);

		final String cipherText = cipherTextView.getText().toString();

		String plainText;
		try {
			plainText = decrypt(cipherText);
			plainTextView.setText(plainText);
		} catch (Exception e) {
			e.printStackTrace();
			
			Toast.makeText(this, "Something went wrong :( Check logcat for details..", Toast.LENGTH_LONG).show();
		}
	}

	private String encrypt(String plainText)
			throws UnsupportedEncodingException, NoSuchAlgorithmException,
			NoSuchPaddingException, InvalidKeyException,
			IllegalBlockSizeException, BadPaddingException {
		// �ifreleme objemizi haz�rlayal�m.
		Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
		cipher.init(Cipher.ENCRYPT_MODE, publicKey);
		 
		// Metnimizi byte dizisine �evirelim.
		byte[] plainBytes = plainText.getBytes("UTF-8");
		 
		// Burada a��k metni �ifreleyip �ifrelenmi� byte dizisini elde ediyoruz.
		byte[] cipherBytes = cipher.doFinal(plainBytes);
		 
		// �ifrelenmi� veriyi saklamak ve ta��mak i�in Base64 olarak encode ediyoruz.
		return Base64.encodeToString(cipherBytes, Base64.DEFAULT);
	}

	private String decrypt(String cipherText)
			throws UnsupportedEncodingException, NoSuchAlgorithmException,
			NoSuchPaddingException, InvalidKeyException,
			IllegalBlockSizeException, BadPaddingException {
		// �ifreleme objemizi haz�rlayal�m.
		Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
		cipher.init(Cipher.DECRYPT_MODE, privateKey);
		 
		// Metnimizi byte dizisine �evirelim.
		byte[] cipherBytes = Base64.decode(cipherText, Base64.DEFAULT);
		 
		// Burada �ifreli verimizi ��z�p a��k byte dizisini elde ediyoruz.
		byte[] plainBytes = cipher.doFinal(cipherBytes);
		 
		// A��k veriyi string format�na geri �eviriyoruz.
		return new String(plainBytes, "UTF-8");
	}
}
