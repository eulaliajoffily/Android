package br.ufpe.cin.banco.conta;

import android.content.Intent;
import android.os.Bundle;
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
public class EditarContaActivity extends AppCompatActivity {

    private ContaDAO dao;
    public static final String KEY_NUMERO_CONTA = String.valueOf(R.string.KEY_NUMERO_CONTA);
    ContaViewModel viewModel;

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
        Intent i = getIntent();
        String numeroIntent = i.getStringExtra(KEY_NUMERO_CONTA);
        viewModel.buscarPeloNumero(numeroIntent);

        Button btnAtualizar = findViewById(R.id.btnAtualizarConta);
        Button btnRemover = findViewById(R.id.btnRemover);
        EditText campoNome = findViewById(R.id.nome);
        EditText campoNumero = findViewById(R.id.numero);
        EditText campoCPF = findViewById(R.id.cpf);
        EditText campoSaldo = findViewById(R.id.saldo);
        campoNumero.setEnabled(false);


        //TODO usar o número da conta passado via Intent para recuperar informações da conta
        viewModel.contaAtual.observe(
                this,
                conta -> {
                    campoNome.setText(conta.nomeCliente);
                    campoCPF.setText(conta.cpfCliente);
                    campoSaldo.setText(String.valueOf(conta.saldo));
                    campoNumero.setText(conta.numero);
                }
        );

        btnAtualizar.setText(R.string.btn_editar);
        btnAtualizar.setOnClickListener(
                v -> {
                    String nomeCliente = campoNome.getText().toString();
                    String cpfCliente = campoCPF.getText().toString();
                    String saldoConta = campoSaldo.getText().toString();
                    String numeroConta = campoNumero.getText().toString();
                    //TODO: Incluir validações aqui, antes de criar um objeto Conta. Se todas as validações passarem, aí sim monta um objeto Conta.
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
                        Conta c = new Conta(numeroConta, Double.parseDouble(saldoConta), nomeCliente, cpfCliente);
                        //TODO: chamar o método que vai atualizar a conta no Banco de Dados
//                        Conta c = c_atualizada;
                        viewModel.atualizar(c);
                        finish();
                    }
                }
        );

        btnRemover.setOnClickListener(v -> {
            //TODO implementar remoção da conta
            viewModel.remover(viewModel.contaAtual.getValue());
            finish();
        });
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