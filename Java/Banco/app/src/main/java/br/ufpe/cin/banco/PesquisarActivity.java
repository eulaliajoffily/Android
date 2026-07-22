package br.ufpe.cin.banco;

import static android.widget.Toast.LENGTH_SHORT;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import br.ufpe.cin.banco.conta.Conta;
import br.ufpe.cin.banco.conta.ContaAdapter;

//Ver anotações TODO no código
public class PesquisarActivity extends AppCompatActivity {
    BancoViewModel viewModel;
    ContaAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_pesquisar);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        viewModel = new ViewModelProvider(this).get(BancoViewModel.class);
        EditText aPesquisar = findViewById(R.id.pesquisa);
        Button btnPesquisar = findViewById(R.id.btn_Pesquisar);
        RadioGroup tipoPesquisa = findViewById(R.id.tipoPesquisa);
        RecyclerView rvResultado = findViewById(R.id.rvResultado);
        adapter = new ContaAdapter(getLayoutInflater());
        rvResultado.setLayoutManager(new LinearLayoutManager(this));
        rvResultado.setAdapter(adapter);

        btnPesquisar.setOnClickListener(v -> {
            if (!aPesquisar.getText().toString().isEmpty()) {
                String oQueFoiDigitado = aPesquisar.getText().toString();
                int idSelecionado = tipoPesquisa.getCheckedRadioButtonId();
                if (idSelecionado == -1) {
                    runOnUiThread(
                            () -> Toast.makeText(this, R.string.erro_busca_invalida, LENGTH_SHORT).show()
                    );
                } else {
                    new Thread(
                            () -> realizarBusca(idSelecionado, oQueFoiDigitado)
                    ).start();
                }
            } else {
                runOnUiThread(
                        () -> Toast.makeText(this, R.string.erro_campo_vazio, LENGTH_SHORT).show()
                );
            }
        });


    }

    //TODO implementar a busca de acordo com o tipo de busca escolhido pelo usuário
    private void realizarBusca(int checkedId, String termoPesquisa) {
        //Cria uma lista que vai ser preenchida de acordo com o ID do RadioButton selecionado
        List<Conta> resultadosDaBusca = new ArrayList<>();
        //Verifica qual RadioButton foi selecionado
        if (checkedId == R.id.peloNomeCliente) {
            List<Conta> c_nome = viewModel.buscarContasPeloNome(termoPesquisa);
            if (c_nome != null) {
                resultadosDaBusca.addAll(c_nome);
            } else {
                runOnUiThread(
                        () -> Toast.makeText(this, R.string.erro_conta_nao_encontrada, LENGTH_SHORT).show()
                );
            }
        } else if (checkedId == R.id.peloNumeroConta) {
            Conta c_numero = viewModel.buscarContaPeloNumero(termoPesquisa);
            if (c_numero != null) {
                resultadosDaBusca.add(c_numero);
            } else {
                runOnUiThread(
                        () -> Toast.makeText(this, R.string.erro_conta_nao_encontrada, LENGTH_SHORT).show()
                );
            }
        } else if (checkedId == R.id.peloCPFcliente) {
            List<Conta> c_cpf = viewModel.buscarContasPeloCPF(termoPesquisa);
            if (c_cpf != null) {
                resultadosDaBusca.addAll(c_cpf);
            } else {
                runOnUiThread(
                        () -> Toast.makeText(this, R.string.erro_conta_nao_encontrada, LENGTH_SHORT).show()
                );
            }
        }
        //TODO atualizar o RecyclerView com resultados da busca na medida que encontrar
        runOnUiThread(
                () -> adapter.submitList(resultadosDaBusca)
        );


    }
}