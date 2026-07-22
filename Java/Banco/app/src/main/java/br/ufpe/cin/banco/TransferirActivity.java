package br.ufpe.cin.banco;

import static android.widget.Toast.LENGTH_SHORT;

import android.os.Bundle;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import br.ufpe.cin.banco.conta.Conta;
import br.ufpe.cin.banco.transacoes.Transacao;
import br.ufpe.cin.banco.transacoes.TransacaoViewModel;

//Ver anotações TODO no código
public class TransferirActivity extends AppCompatActivity {

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

        valorOperacao.setHint(valorOperacao.getHint() + " " +getString(R.string.transferido));
        tipoOperacao.setText(R.string.transacao_transferir);
        btnOperacao.setText(R.string.btn_transferir);

        btnOperacao.setOnClickListener(
                v -> {
                    //TODO lembrar de implementar validação dos números das contas e do valor da operação, antes de efetuar a operação de transferência.
                    if (!numeroContaOrigem.getText().toString().isEmpty() || !numeroContaDestino.getText().toString().isEmpty() || !valorOperacao.getText().toString().isEmpty()) {
                        if (isNumber(numeroContaOrigem.getText().toString()) || isNumber(numeroContaDestino.getText().toString()) || isNumber(valorOperacao.getText().toString())) {
                            backgroundThread(() -> {
                                try {
                                    Conta contaOrigem = viewModel.buscarContaPeloNumero(numeroContaOrigem.getText().toString());
                                    Conta contaDestino = viewModel.buscarContaPeloNumero(numeroContaDestino.getText().toString());
                                    if (contaOrigem != null || contaDestino != null) {
                                        double valor = Double.parseDouble(valorOperacao.getText().toString());
                                        if (contaOrigem.saldo >= valor) {
                                            viewModel.transferir(numeroContaOrigem.getText().toString(), numeroContaDestino.getText().toString(), valor);
                                            runOnUiThread(() -> Toast.makeText(this, R.string.transferencia_sucesso, LENGTH_SHORT).show());
                                            Date dataAtual = new Date();
                                            SimpleDateFormat formato = new SimpleDateFormat(getString(R.string.date_format), Locale.getDefault());
                                            String dataTransacao = formato.format(dataAtual);
                                            Transacao t_debitoOrigem = new Transacao(0, 'D', numeroContaOrigem.getText().toString(), valor, dataTransacao);
                                            Transacao t_creditoDestino = new Transacao(0, 'C', numeroContaDestino.getText().toString(), valor, dataTransacao);
                                            transacaoViewModel.inserir(t_debitoOrigem);
                                            transacaoViewModel.inserir(t_creditoDestino);
                                            finish();
                                        } else {
                                            runOnUiThread(() -> Toast.makeText(this, R.string.erro_saldo_insuficiente, LENGTH_SHORT).show());
                                        }
                                    }
                                } catch (Exception e) {
                                    runOnUiThread(() -> Toast.makeText(this, R.string.erro_conta_nao_existe, LENGTH_SHORT).show());
                                }
                            });
                        } else {
                            Toast.makeText(this, R.string.erro_notNumber, LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(this, R.string.erro_campo_vazio, LENGTH_SHORT).show();
                    }
                }
        );

    }

    private boolean isNumber(String s) {
        try {
            Double.parseDouble(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private void backgroundThread(Runnable r) {
        new Thread(r).start();
    }
}