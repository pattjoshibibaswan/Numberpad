package com.samplendk.zippr.samplendk;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.davidmiguel.numberkeyboard.NumberKeyboard;
import com.davidmiguel.numberkeyboard.NumberKeyboardListener;

public class MainActivity extends AppCompatActivity {

    private static final double MAX_ALLOWED_AMOUNT = 9999.99;

    private NumberKeyboard numberKeyboard;
    private TextView sample_text;
    private String text = "";
    private String amountText = "";
    private double amount;

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Example of a call to a native method
        TextView tv = (TextView) findViewById(R.id.sample_text);
        tv.setText(stringFromJNI());
        numberKeyboard = findViewById(R.id.number_keybord);
        sample_text = findViewById(R.id.sample_text);
        numberKeyboard.setListener(new NumberKeyboardListener() {
            @Override
            public void onNumberClicked(int number) {
                if (amountText.isEmpty() && number == 0) {
                    return;
                }
                updateAmount(amountText + number);
            }

            @Override
            public void onLeftAuxButtonClicked() {

            }

            @Override
            public void onRightAuxButtonClicked() {
                if (amountText.isEmpty()) {
                    return;
                }
                String newAmountText;
                if (amountText.length() <= 1) {
                    newAmountText = "";
                } else {
                    newAmountText = amountText.substring(0, amountText.length() - 1);
                    if (newAmountText.charAt(newAmountText.length() - 1) == ',') {
                        newAmountText = newAmountText.substring(0, newAmountText.length() - 1);
                    }
                    if ("0".equals(newAmountText)) {
                        newAmountText = "";
                    }
                }
                updateAmount(newAmountText);
            }
        });
    }
    private void updateAmount(String newAmountText) {
        double newAmount = newAmountText.isEmpty() ? 0.0 : Double.parseDouble(newAmountText.replaceAll(",", "."));
        if (newAmount >= 0.0 && newAmount <= MAX_ALLOWED_AMOUNT) {
            amountText = newAmountText;
            amount = newAmount;
            showAmount(amountText);
        }
    }
    private void showAmount(String amount) {
        text = amount;
        sample_text.setText((amount.isEmpty() ? "" : amount));
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();
}
