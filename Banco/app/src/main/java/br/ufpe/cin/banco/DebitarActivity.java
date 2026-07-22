package br.ufpe.cin.banco;

import static android.widget.Toast.LENGTH_SHORT;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import br.ufpe.cin.banco.conta.Conta;
import br.ufpe.cin.banco.transacoes.Transacao;
import br.ufpe.cin.banco.transacoes.TransacaoViewModel;

//Ver anotações TODO no código

public class DebitarActivity extends AppCompatActivity {
    BancoViewModel viewModel;
    TransacaoViewModel transacaoViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_operacoes);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        viewModel = new ViewModelProvider(this).get(BancoViewModel.class);
        transacaoViewModel = new ViewModelProvider(this).get(TransacaoViewModel.class);

        TextView tipoOperacao = findViewById(R.id.tipoOperacao);
        EditText numeroContaOrigem = findViewById(R.id.numeroContaOrigem);
        TextView labelContaDestino = findViewById(R.id.labelContaDestino);
        EditText numeroContaDestino = findViewById(R.id.numeroContaDestino);
        EditText valorOperacao = findViewById(R.id.valor);
        Button btnOperacao = findViewById(R.id.btnOperacao);

        labelContaDestino.setVisibility(View.GONE);
        numeroContaDestino.setVisibility(View.GONE);

        valorOperacao.setHint(valorOperacao.getHint() + " " + getString(R.string.debitado));
        tipoOperacao.setText(R.string.transacao_debitar);
        btnOperacao.setText(R.string.btn_creditar);

        btnOperacao.setOnClickListener(v -> {
            //TODO lembrar de implementar validação do número da conta e do valor da operação, antes de efetuar a operação de débito.
            if (!numeroContaOrigem.getText().toString().isEmpty()) {
                String numOrigem = numeroContaOrigem.getText().toString();
                if (isNumber(numOrigem)) {
                    try {
                        backgroundThread(() -> {
                            Conta conta = viewModel.buscarContaPeloNumero(numOrigem);
                            double saldo = conta.saldo;
                            if (!valorOperacao.getText().toString().isEmpty()) {
                                if (isNumber(valorOperacao.getText().toString())) {
                                    double valor = Double.parseDouble(valorOperacao.getText().toString());
                                    if (valor > saldo) {
                                        runOnUiThread(() -> Toast.makeText(this, "Erro, saldo insuficiente", LENGTH_SHORT).show());
                                        finish();
                                    } else if (valor > 0) {
                                        viewModel.debitar(numOrigem, valor);
                                        runOnUiThread(() -> Toast.makeText(this, R.string.debito_sucesso, LENGTH_SHORT).show());
                                        Date dataAtual = new Date();
                                        SimpleDateFormat formato = new SimpleDateFormat(getString(R.string.date_format), Locale.getDefault());
                                        String dataTransacao = formato.format(dataAtual);
                                        Transacao t = new Transacao(0, 'D', numOrigem, valor, dataTransacao);
                                        transacaoViewModel.inserir(t);
                                        runOnUiThread(() -> Toast.makeText(this, R.string.transacao_inserida, LENGTH_SHORT).show());
                                        finish();
                                    } else {
                                        runOnUiThread(() -> Toast.makeText(this, R.string.erro_valor_negativo, LENGTH_SHORT).show());
                                    }
                                } else {
                                    runOnUiThread(() -> Toast.makeText(this, R.string.erro_notNumber, LENGTH_SHORT).show());
                                }
                            } else {
                                runOnUiThread(() -> Toast.makeText(this, R.string.erro_valor_nao_informado, LENGTH_SHORT).show());
                            }
                        });
                    } catch (Exception e) {
                        Log.d("ErroConta", String.valueOf(R.string.erro_conta_nao_existe));
                        Toast.makeText(this, R.string.erro_conta_nao_existe, LENGTH_SHORT).show();
                        finish();
                    }
                } else {
                    Toast.makeText(this, R.string.erro_notNumber, LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, R.string.erro_conta_nao_informada, LENGTH_SHORT).show();
            }
        });
    }

    private boolean isNumber(String s) {
        try {
            Double.parseDouble(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private void backgroundThread(Runnable r) {new Thread(r).start();}
}

