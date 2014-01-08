package com.example.androidencriptionusingaesinecbmode;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

import android.os.Bundle;
import android.app.Activity;
import android.util.Base64;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {
	private static final String EncriptionKey = "0E9826AC44B33DD5EF220FB233951642BB5FA56AC980E09A931D3870E2A716C6";

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

	public void encryptButtonClicked(View v) {
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
		// Keyimizi byte dizisine çevirelim
		byte[] keyBytes = hexStringToByteArray(EncriptionKey);
		// Byte key'i kullanabilmemiz için hazýrlanmýþ objeyi oluþturalým.
		SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");
		// String güzel ancak bize byte lazým..
		byte[] plainBytes = plainText.getBytes("UTF-8");
		// Þifreleme objemizi yaratalým..
		Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
		// ve þifreleme için hazýrlayalým..
		cipher.init(Cipher.ENCRYPT_MODE, keySpec);
		// Sonunda þifrelenmiþ byte dizimiz
		byte[] cipherBytes = cipher.doFinal(plainBytes);
		// Base64 olarak encode ederek istediðimiz gibi kullanabiliriz.
		return Base64.encodeToString(cipherBytes, Base64.DEFAULT
				| Base64.NO_WRAP);
	}

	private String decrypt(String cipherText)
			throws UnsupportedEncodingException, NoSuchAlgorithmException,
			NoSuchPaddingException, InvalidKeyException,
			IllegalBlockSizeException, BadPaddingException {
		// Keyimizi byte dizisine çevirelim
		byte[] keyBytes = hexStringToByteArray(EncriptionKey);
		// Byte key'i kullanabilmemiz için hazýrlanmýþ objeyi oluþturalým.
		SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");
		// String güzel ancak bize yine byte lazým..
		byte[] cipherBytes = Base64.decode(cipherText, Base64.DEFAULT
				| Base64.NO_WRAP);
		// Þifreleme objemizi yaratalým..
		Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
		// ve çözme için hazýrlayalým..
		cipher.init(Cipher.DECRYPT_MODE, keySpec);
		// Sonunda þifrelenmiþ byte dizimiz
		byte[] plainBytes = cipher.doFinal(cipherBytes);
		// Byte dizimizi string'e çevirip kullanabiliriz.
		return new String(plainBytes, "UTF-8");
	}

	/**
	 * Hex string'i verip byte dizisini aldýðýmýz metodumuz..
	 */
	private static byte[] hexStringToByteArray(String hex) {
		int len = hex.length();
		byte[] data = new byte[len / 2];
		for (int i = 0; i < len; i += 2) {
			data[i / 2] = (byte) ((Character.digit(hex.charAt(i), 16) << 4) + Character
					.digit(hex.charAt(i + 1), 16));
		}
		return data;
	}
}
