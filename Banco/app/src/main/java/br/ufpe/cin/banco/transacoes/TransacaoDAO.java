package br.ufpe.cin.banco.transacoes;

import androidx.annotation.WorkerThread;
import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

//Ver anotações TODO no código
@Dao
public interface TransacaoDAO {

    @Insert
    void adicionar(Transacao t);

    //não deve ser possível editar ou remover uma transação

    @Query("SELECT * FROM transacoes ORDER BY dataTransacao DESC")
    LiveData<List<Transacao>> transacoes();

    //TODO incluir métodos para buscar transações pelo (1) número da conta, (2) pela data, filtrando pelo tipo da transação (crédito, débito, ou todas)

    @Query("SELECT * FROM transacoes WHERE numeroConta = :numeroConta ORDER BY dataTransacao DESC")
    List<Transacao> transacoesPorNumeroConta(String numeroConta);
    @Query("SELECT * FROM transacoes WHERE numeroConta = :numeroConta AND tipoTransacao = :tipoTransacao ORDER BY dataTransacao DESC")
    List<Transacao> transacoesPorNumeroContaETipo(String numeroConta, Character tipoTransacao);

    @Query("SELECT * FROM transacoes WHERE dataTransacao = :dataTransacao ORDER BY dataTransacao DESC")
    List<Transacao> transacoesPorData(String dataTransacao);

    @Query("SELECT * FROM transacoes WHERE dataTransacao = :dataTransacao AND tipoTransacao = :tipoTransacao ORDER BY dataTransacao DESC")
    List<Transacao> transacoesPorDataETipo(String dataTransacao, Character tipoTransacao);

    @Query("SELECT * FROM transacoes WHERE tipoTransacao = :tipoTransacao ORDER BY dataTransacao DESC")
    List<Transacao> transacoesPorTipo(Character tipoTransacao);

    @Query("SELECT * FROM transacoes ORDER BY dataTransacao DESC")
    List<Transacao> todasTransacoes();

}
