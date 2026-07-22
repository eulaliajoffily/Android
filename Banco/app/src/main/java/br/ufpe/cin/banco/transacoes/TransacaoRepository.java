package br.ufpe.cin.banco.transacoes;

import androidx.annotation.WorkerThread;
import androidx.lifecycle.LiveData;

import java.util.List;

//Ver anotações TODO no código
public class TransacaoRepository {
    private TransacaoDAO dao;
    private LiveData<List<Transacao>> transacoes;

    public TransacaoRepository(TransacaoDAO dao) {
        this.dao = dao;
        this.transacoes = dao.transacoes();
    }

    @WorkerThread
    public LiveData<List<Transacao>> getTransacoes() {
        return this.transacoes;
    }

    @WorkerThread
    public void inserir(Transacao t) {
        dao.adicionar(t);
    }

    //TODO implementar métodos de busca de transações
    @WorkerThread
    public List<Transacao> buscarTransacoesPorNumeroConta(String numeroConta) {
        return dao.transacoesPorNumeroConta(numeroConta);
    }

    @WorkerThread
    public List<Transacao> buscarTransacoesPorNumeroETipo(String numeroConta, Character tipoTransacao) {
        return dao.transacoesPorNumeroContaETipo(numeroConta, tipoTransacao);
    }

    @WorkerThread
    public List<Transacao> buscarTransacoesPorData(String dataTransacao) {
        return dao.transacoesPorData(dataTransacao);
    }

    @WorkerThread
    public List<Transacao> buscarTransacoesPorDataETipo(String dataTransacao, Character tipoTransacao) {
        return dao.transacoesPorDataETipo(dataTransacao, tipoTransacao);
    }

    @WorkerThread
    public List<Transacao> buscarTransacoesPorTipo(char tipoTransacao) {
        return dao.transacoesPorTipo(tipoTransacao);
    }

    public List<Transacao> buscarTransacoes() {
        return dao.transacoes().getValue();
    }
}
