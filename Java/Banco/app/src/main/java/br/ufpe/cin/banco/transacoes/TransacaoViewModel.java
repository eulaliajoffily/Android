package br.ufpe.cin.banco.transacoes;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.WorkerThread;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import br.ufpe.cin.banco.BancoDB;
import br.ufpe.cin.banco.R;
import br.ufpe.cin.banco.conta.Conta;

//Ver anotações TODO no código
public class TransacaoViewModel extends AndroidViewModel {

    private TransacaoRepository repository;
    public LiveData<List<Transacao>> transacoes;

    public TransacaoViewModel(@NonNull Application application) {
        super(application);
        this.repository = new TransacaoRepository(BancoDB.getDB(application).transacaoDAO());
        this.transacoes = repository.getTransacoes();
    }

    public void inserir(Transacao t) {
        new Thread(() -> repository.inserir(t)).start();
    }
    //TODO implementar métodos de busca de transações

    public List<Transacao> buscarTransacoesFiltradas(String tipoPesquisa, String termoPesquisa, Character tipoTransacao) {
        if (!termoPesquisa.isEmpty()) {
            if (tipoPesquisa.equals(String.valueOf(R.string.numero_conta))) {
                if (tipoTransacao == null) {
                    return repository.buscarTransacoesPorNumeroConta(termoPesquisa);
                } else {
                    return repository.buscarTransacoesPorNumeroETipo(termoPesquisa, tipoTransacao);
                }
            } else if (tipoPesquisa.equals(String.valueOf(R.string.data))) {
                if (tipoTransacao == null) {
                    return repository.buscarTransacoesPorData(termoPesquisa);
                } else {
                    return repository.buscarTransacoesPorDataETipo(termoPesquisa, tipoTransacao);
                }
            } else {
                if (tipoTransacao == null) {
                    return repository.buscarTransacoes();
                } else {
                    return repository.buscarTransacoesPorTipo(tipoTransacao);
                }
            }
        } else {
            if (tipoPesquisa.equals(String.valueOf(R.string.numero_conta))){
                if (tipoTransacao == null){
                    return repository.buscarTransacoes();
                } else {
                    return repository.buscarTransacoesPorTipo(tipoTransacao);
                }
            } else if (tipoPesquisa.equals(String.valueOf(R.string.data))){
                if (tipoTransacao == null){
                    return repository.buscarTransacoes();
                } else {
                    return repository.buscarTransacoesPorTipo(tipoTransacao);
                }
            } else {
                if (tipoTransacao == null){
                    return repository.buscarTransacoes();
                } else {
                    return repository.buscarTransacoesPorTipo(tipoTransacao);
                }
            }
        }
    }
}
