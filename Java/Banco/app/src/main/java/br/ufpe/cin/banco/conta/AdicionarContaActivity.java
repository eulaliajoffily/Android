package br.ufpe.cin.banco.conta;

import static android.util.Log.i;

import static br.ufpe.cin.banco.conta.EditarContaActivity.KEY_NUMERO_CONTA;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import br.ufpe.cin.banco.R;

//Ver anotações TODO no código
public class AdicionarContaActivity extends AppCompatActivity {

    ContaViewModel viewModel;

    public static final String KEY_CONTA = "Conta_key";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_adicionar_conta);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        viewModel = new ViewModelProvider(this).get(ContaViewModel.class);

        Button btnAtualizarConta = findViewById(R.id.btnAtualizarConta);
        Button btnRemover = findViewById(R.id.btnRemover);
        EditText campoNome = findViewById(R.id.nome);
        EditText campoNumero = findViewById(R.id.numero);
        EditText campoCPF = findViewById(R.id.cpf);
        EditText campoSaldo = findViewById(R.id.saldo);

        btnAtualizarConta.setText(R.string.inserir);
        btnRemover.setVisibility(View.GONE);

        btnAtualizarConta.setOnClickListener(
                v -> {
                    String nomeCliente = campoNome.getText().toString();
                    String cpfCliente = campoCPF.getText().toString();
                    String numeroConta = campoNumero.getText().toString();
                    String saldoConta = campoSaldo.getText().toString();
                    Intent i = new Intent(
                            this,
                            ContasActivity.class
                    );
                    //TODO: Incluir validações aqui, antes de criar um objeto Conta (por exemplo, verificar que digitou um nome com pelo menos 5 caracteres, que o campo de saldo tem de fato um número, assim por diante). Se todas as validações passarem, aí sim cria a Conta conforme linha abaixo.
                    if (nomeCliente.length() < 5) {
                        Toast.makeText(
                                this,
                                R.string.toast_erro_nome,
                                Toast.LENGTH_SHORT
                        ).show();
                    } else if (!isNumber(saldoConta)) {
                        Toast.makeText(
                                this,
                                R.string.toast_erro_saldo,
                                Toast.LENGTH_SHORT
                        ).show();
                    } else if (!cpfCliente.isEmpty() && !isNumber(cpfCliente)){
                        Toast.makeText(
                                this,
                                R.string.toast_erro_cpf,
                                Toast.LENGTH_SHORT
                        ).show();
                    } else {
                        Conta c = new Conta(numeroConta, Double.valueOf(saldoConta), nomeCliente, cpfCliente);
                        //TODO: chamar o método que vai salvar a conta no Banco de Dados
                        viewModel.adicionar(c);
                        i.putExtra(KEY_NUMERO_CONTA, numeroConta);
                        finish();
                    }

                }
        );

    }

    // verificar se a string é um número
    private boolean isNumber(String s) {
        try {
            Double.parseDouble(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}