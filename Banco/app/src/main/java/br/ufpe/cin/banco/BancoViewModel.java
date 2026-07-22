package br.ufpe.cin.banco;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.annotation.WorkerThread;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import br.ufpe.cin.banco.conta.Conta;
import br.ufpe.cin.banco.conta.ContaRepository;
import br.ufpe.cin.banco.transacoes.Transacao;
import br.ufpe.cin.banco.transacoes.TransacaoRepository;

//Ver anotações TODO no código
public class BancoViewModel extends AndroidViewModel {
    private ContaRepository contaRepository;
    private TransacaoRepository transacaoRepository;

    public BancoViewModel(@NonNull Application application) {
        super(application);
        this.contaRepository = new ContaRepository(BancoDB.getDB(application).contaDAO());
        this.transacaoRepository = new TransacaoRepository(BancoDB.getDB(application).transacaoDAO());
    }

    void transferir(String numeroContaOrigem, String numeroContaDestino, double valor) {
        //TODO implementar transferência entre contas (lembrar de salvar no BD os objetos Conta modificados)

        //Debitar valor da conta origem
        debitar(numeroContaOrigem, valor);

        //Creditar valor na conta destino
        creditar(numeroContaDestino, valor);

        //As respectivas atualizações já ocorrem nos métodos debitar e creditar

    }

    void creditar(String numeroConta, double valor) {
        //TODO implementar creditar em conta (lembrar de salvar no BD o objeto Conta modificado)
        //Pegar a conta pelo número
        Conta contaCredito = buscarContaPeloNumero(numeroConta);

        //Creditar valor ao saldo
        contaCredito.saldo = contaCredito.saldo + valor;

        //Atualizar conta
        contaRepository.atualizar(contaCredito);
    }

    void debitar(String numeroConta, double valor) {
        //TODO implementar debitar em conta (lembrar de salvar no BD o objeto Conta modificado)

        //Pegar a conta pelo número
        Conta contaDebito = buscarContaPeloNumero(numeroConta);

        //Debitar valor do saldo
        contaDebito.saldo = contaDebito.saldo - valor;

        //Atualizar conta
        contaRepository.atualizar(contaDebito);
    }

    @WorkerThread
    List<Conta> buscarContasPeloNome(String nomeCliente) {
        //TODO implementar busca pelo nome do Cliente
        List<Conta> contas = contaRepository.buscarPeloNome(nomeCliente);
        return contas;
    }

    @WorkerThread
    List<Conta> buscarContasPeloCPF(String cpfCliente) {
        //TODO implementar busca pelo CPF do Cliente
        List<Conta> contas = contaRepository.buscarPeloCPF(cpfCliente);
        return contas;
    }

    @WorkerThread
    Conta buscarContaPeloNumero(String numeroConta) {
        //TODO implementar busca pelo número da Conta
        Conta conta = contaRepository.buscarPeloNumero(numeroConta);
        return conta;
    }


    LiveData<Double> saldoTotal() {
        return contaRepository.saldoTotal();
    }
    @WorkerThread
    void buscarTransacoesPeloNumero(String numeroConta) {
        //TODO implementar
        transacaoRepository.buscarTransacoesPorNumeroConta(numeroConta);
    }

    @WorkerThread
    void buscarTransacoesPeloTipo(char tipoTransacao) {
        //TODO implementar
        transacaoRepository.buscarTransacoesPorTipo(tipoTransacao);
    }

    @WorkerThread
    void buscarTransacoesPelaData(String dataTransacao) {
        //TODO implementar
        transacaoRepository.buscarTransacoesPorData(dataTransacao);
    }

}
