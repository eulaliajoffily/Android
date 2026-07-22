package br.ufpe.cin.calculadora;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

// Tutorial de como perder neurônios
// Passo 1: faça uma calculadora em java android
// Passo 2: ignore o passo 1...

// Enfim, entendo por que a apple demorou anos para implementar uma calculadora no iPad...

public class MainActivity extends AppCompatActivity {

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        final TextView text_info = findViewById(R.id.text_info);
        final TextView text_calc = findViewById(R.id.text_calc);
        final Button zero = findViewById(R.id.btn_0);
        final Button one = findViewById(R.id.btn_1);
        final Button two = findViewById(R.id.btn_2);
        final Button three = findViewById(R.id.btn_3);
        final Button four = findViewById(R.id.btn_4);
        final Button five = findViewById(R.id.btn_5);
        final Button six = findViewById(R.id.btn_6);
        final Button seven = findViewById(R.id.btn_7);
        final Button eight = findViewById(R.id.btn_8);
        final Button nine = findViewById(R.id.btn_9);
        final Button divide = findViewById(R.id.btn_Divide);
        final Button dot = findViewById(R.id.btn_Dot);
        final Button multiply = findViewById(R.id.btn_Multiply);
        final Button subtract = findViewById(R.id.btn_Subtract);
        final Button equals = findViewById(R.id.btn_Equal);
        final Button add = findViewById(R.id.btn_Add);
        final Button l_paren = findViewById(R.id.btn_LParen);
        final Button r_paren = findViewById(R.id.btn_RParen);
        final Button power = findViewById(R.id.btn_Power);
        final Button clear = findViewById(R.id.btn_Clear);

        zero.setOnClickListener(v -> text_calc.setText(text_calc.getText() + getString(R.string.number_0)));
        one.setOnClickListener(v -> text_calc.setText(text_calc.getText() + getString(R.string.number_1)));
        two.setOnClickListener(v -> text_calc.setText(text_calc.getText() + getString(R.string.number_2)));
        three.setOnClickListener(v -> text_calc.setText(text_calc.getText() + getString(R.string.number_3)));
        four.setOnClickListener(v -> text_calc.setText(text_calc.getText() + getString(R.string.number_4)));
        five.setOnClickListener(v -> text_calc.setText(text_calc.getText() + getString(R.string.number_5)));
        six.setOnClickListener(v -> text_calc.setText(text_calc.getText() + getString(R.string.number_6)));
        seven.setOnClickListener(v -> text_calc.setText(text_calc.getText() + getString(R.string.number_7)));
        eight.setOnClickListener(v -> text_calc.setText(text_calc.getText() + getString(R.string.number_8)));
        nine.setOnClickListener(v -> text_calc.setText(text_calc.getText() + getString(R.string.number_9)));
        multiply.setOnClickListener(v -> text_calc.setText(text_calc.getText() + getString(R.string.multiply)));
        subtract.setOnClickListener(v -> text_calc.setText(text_calc.getText() + getString(R.string.subtract)));
        add.setOnClickListener(v -> text_calc.setText(text_calc.getText() + getString(R.string.add)));
        divide.setOnClickListener(v -> text_calc.setText(text_calc.getText() + getString(R.string.divide)));
        power.setOnClickListener(v -> text_calc.setText(text_calc.getText() + getString(R.string.power)));
        l_paren.setOnClickListener(v -> text_calc.setText(text_calc.getText() + getString(R.string.l_paren)));
        r_paren.setOnClickListener(v -> text_calc.setText(text_calc.getText() + getString(R.string.r_paren)));

        dot.setOnClickListener(v -> {
            if (text_calc.getText().equals(getString(R.string.clear))){
                text_calc.setText(getString(R.string.number_0) + getString(R.string.dot));
            } else {
                text_calc.setText(text_calc.getText() + getString(R.string.dot));
            }
        });

        clear.setOnClickListener(v -> {
            text_calc.setText(R.string.clear);
            text_info.setText(R.string.clear);
        });

        equals.setOnClickListener(v -> {
            if (text_info.getText().length() == 0) {
                try {
                    text_info.setText(String.valueOf(eval(text_calc.getText().toString())));
                } catch (RuntimeException e) {
                    Toast.makeText(
                            MainActivity.this,
                            R.string.error,
                            Toast.LENGTH_SHORT
                    ).show();
                    text_calc.setText(R.string.clear);
                }
            } else {
                try {
                    text_info.setText(String.valueOf(eval(text_info.getText().toString() + text_calc.getText().toString())));
                    text_calc.setText(R.string.clear);
                } catch (RuntimeException e) {
                    Toast.makeText(
                            MainActivity.this,
                            R.string.error,
                            Toast.LENGTH_SHORT
                    ).show();
                }
            }
            text_calc.setText(R.string.clear);
        });
    }


    //Como usar a função:
// eval("2+2") == 4.0
// eval("2+3*4") = 14.0
// eval("(2+3)*4") = 20.0
//Fonte: https://stackoverflow.com/a/26227947
    public static double eval(final String str) {
        return new Object() {
            int pos = -1, ch;

            void nextChar() {
                ch = (++pos < str.length()) ? str.charAt(pos) : -1;
            }

            boolean eat(int charToEat) {
                while (ch == ' ') nextChar();
                if (ch == charToEat) {
                    nextChar();
                    return true;
                }
                return false;
            }

            double parse() {
                nextChar();
                double x = parseExpression();
                if (pos < str.length())
                    throw new RuntimeException("Caractere inesperado: " + (char) ch);
                return x;
            }

// Grammar:
// expression = term | expression `+` term | expression `-` term
// term = factor | term `*` factor | term `/` factor
// factor = `+` factor | `-` factor | `(` expression `)`
// | number | functionName factor | factor `^` factor

            double parseExpression() {
                double x = parseTerm();
                for (; ; ) {
                    if (eat('+')) x += parseTerm(); // adição
                    else if (eat('-')) x -= parseTerm(); // subtração
                    else return x;
                }
            }

            double parseTerm() {
                double x = parseFactor();
                for (; ; ) {
                    if (eat('*')) x *= parseFactor(); // multiplicação
                    else if (eat('/')) x /= parseFactor(); // divisão
                    else return x;
                }
            }

            double parseFactor() {
                if (eat('+')) return parseFactor(); // + unário
                if (eat('-')) return -parseFactor(); // - unário

                double x;
                int startPos = this.pos;
                if (eat('(')) { // parênteses
                    x = parseExpression();
                    eat(')');
                } else if ((ch >= '0' && ch <= '9') || ch == '.') { // números
                    while ((ch >= '0' && ch <= '9') || ch == '.') nextChar();
                    x = Double.parseDouble(str.substring(startPos, this.pos));
                } else if (ch >= 'a' && ch <= 'z') { // funções
                    while (ch >= 'a' && ch <= 'z') nextChar();
                    String func = str.substring(startPos, this.pos);
                    x = parseFactor();
                    if (func.equals("sqrt")) x = Math.sqrt(x);
                    else if (func.equals("sin")) x = Math.sin(Math.toRadians(x));
                    else if (func.equals("cos")) x = Math.cos(Math.toRadians(x));
                    else if (func.equals("tan")) x = Math.tan(Math.toRadians(x));
                    else throw new RuntimeException("Função desconhecida: " + func);
                } else {
                    throw new RuntimeException("Caractere inesperado: " + (char) ch);
                }

                if (eat('^')) x = Math.pow(x, parseFactor()); // potência

                return x;
            }
        }.parse();
    }


}