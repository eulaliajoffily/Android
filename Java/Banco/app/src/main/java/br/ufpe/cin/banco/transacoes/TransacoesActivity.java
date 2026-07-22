package br.ufpe.cin.banco.transacoes;

import static android.widget.Toast.LENGTH_SHORT;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import br.ufpe.cin.banco.BancoViewModel;
import br.ufpe.cin.banco.R;

//Ver anotações TODO no código
public class TransacoesActivity extends AppCompatActivity {
    BancoViewModel bancoViewModel;
    TransacaoViewModel transacaoViewModel;
    TransacaoAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_transacoes);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        bancoViewModel = new ViewModelProvider(this).get(BancoViewModel.class);
        transacaoViewModel = new ViewModelProvider(this).get(TransacaoViewModel.class);
        EditText aPesquisar = findViewById(R.id.pesquisa);
        Button btnPesquisar = findViewById(R.id.btn_Pesquisar);
        RadioGroup tipoTransacao = findViewById(R.id.tipoTransacao);
        RadioGroup tipoPesquisa = findViewById(R.id.tipoPesquisa);
        RecyclerView rvResultado = findViewById(R.id.rvResultado);
        adapter = new TransacaoAdapter(getLayoutInflater());
        rvResultado.setLayoutManager(new LinearLayoutManager(this));
        rvResultado.setAdapter(adapter);

        btnPesquisar.setOnClickListener(
                v -> {
                    String oQueFoiDigitado = aPesquisar.getText().toString();
                    //TODO implementar o filtro de transações com o tipo de busca escolhido pelo usuário
                    int checkTipo = tipoTransacao.getCheckedRadioButtonId();
                    int checkId = tipoPesquisa.getCheckedRadioButtonId();
                    try {
                        new Thread(() -> realizarBusca(checkId, checkTipo, oQueFoiDigitado)).start();
                    } catch (Exception e) {
                        Toast.makeText(
                                this,
                                R.string.erro_ao_realizar_busca,
                                LENGTH_SHORT
                        ).show();
                        throw new RuntimeException(e);
                    }
                }
        );

        //TODO atualizar o RecyclerView com resultados da busca na medida que encontrar
        // inicialmente deve exibir a lista de todas as transações
        transacaoViewModel.transacoes.observe(this, transacoes ->
                adapter.submitList(transacoes)
        );
    }

    void realizarBusca(int checkedId, int checkedTipo, String termoPesquisa) {
        List<Transacao> resultadoDaBusca;
        String tipoPesquisa;
        if (checkedId == R.id.peloNumeroConta) {
            tipoPesquisa = String.valueOf(R.string.numero_conta);
        } else if (checkedId == R.id.pelaData) {
            tipoPesquisa = String.valueOf((R.string.data));
        } else {
            tipoPesquisa = "";
        }
        Character tipoTransacao = null;
        if (checkedTipo == R.id.peloTipoCredito) {
            tipoTransacao = 'C';
        } else if (checkedTipo == R.id.peloTipoDebito) {
            tipoTransacao = 'D';
        } else if (checkedTipo == R.id.peloTipoTodos) {
            tipoTransacao = null;
        }
        if (!tipoPesquisa.isBlank() && termoPesquisa.isEmpty()) {
            resultadoDaBusca = transacaoViewModel.buscarTransacoesFiltradas(tipoPesquisa, termoPesquisa, tipoTransacao);
            runOnUiThread(
                    () -> {
                        if (resultadoDaBusca == null || resultadoDaBusca.isEmpty()) {
                            Toast.makeText(this, R.string.nenhuma_transacao_encontrada, LENGTH_SHORT).show();
                            adapter.submitList(new ArrayList<>());
                        } else {
                            adapter.submitList(resultadoDaBusca);
                        }
                    }
            );
        } else if (tipoPesquisa.isBlank() && !termoPesquisa.isEmpty()) {
            resultadoDaBusca = transacaoViewModel.buscarTransacoesFiltradas(tipoPesquisa, termoPesquisa, tipoTransacao);
            runOnUiThread(
                    () -> {
                        if (resultadoDaBusca == null || resultadoDaBusca.isEmpty()) {
                            Toast.makeText(this, R.string.nenhuma_transacao_encontrada, LENGTH_SHORT).show();
                            adapter.submitList(new ArrayList<>());
                        } else {
                            adapter.submitList(resultadoDaBusca);
                        }
                    }
            );
        } else {
            if (tipoTransacao != null) {
                resultadoDaBusca = transacaoViewModel.buscarTransacoesFiltradas(tipoPesquisa, termoPesquisa, tipoTransacao);
            } else {
                resultadoDaBusca = transacaoViewModel.transacoes.getValue();
            }
            runOnUiThread(
                    () -> {
                        if (resultadoDaBusca == null || resultadoDaBusca.isEmpty()) {
                            Toast.makeText(this, R.string.nenhuma_transacao_encontrada, LENGTH_SHORT).show();
                            adapter.submitList(new ArrayList<>());
                        } else {
                            adapter.submitList(resultadoDaBusca);
                        }
                    }
            );
        }
    }
}
