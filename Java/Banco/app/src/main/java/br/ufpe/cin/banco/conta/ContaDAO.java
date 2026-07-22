package br.ufpe.cin.banco.conta;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

//Ver anotações TODO no código
@Dao
public interface ContaDAO {

    @Insert(entity = Conta.class, onConflict = OnConflictStrategy.REPLACE)
    void adicionar(Conta c);

    //TODO incluir métodos para atualizar conta e remover conta
    @Update
    void atualizar(Conta c);

    @Delete
    void remover(Conta c);

    @Query("SELECT * FROM contas ORDER BY numero ASC")
    LiveData<List<Conta>> contas();

    @Query("SELECT * FROM contas ORDER BY numero ASC")
    List<Conta> todasContas();

    //TODO incluir métodos para buscar pelo (1) número da conta, (2) pelo nome e (3) pelo CPF do Cliente

    @Query("SELECT * FROM contas WHERE numero = :numeroPesquisado")
    Conta buscarPeloNumero(String numeroPesquisado);

    @Query("SELECT * FROM contas WHERE nomeCliente = :nomePesquisado")
    List<Conta> buscarPeloNome(String nomePesquisado);

    @Query("SELECT * FROM contas where cpfCliente = :cpfPesquisado")
    List<Conta> buscarPeloCPF(String cpfPesquisado);

    @Query("SELECT SUM(COALESCE(saldo, 0)) FROM contas")
    LiveData<Double> saldoTotal();
}
