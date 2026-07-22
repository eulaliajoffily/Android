package br.ufpe.cin.banco.conta;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import br.ufpe.cin.banco.BancoDB;

//Ver métodos anotados com TODO
public class ContaViewModel extends AndroidViewModel {

    private final ContaRepository repository;

    public LiveData<List<Conta>> contas;
    final private MutableLiveData<Conta> _contaAtual = new MutableLiveData<>();
    public LiveData<Conta> contaAtual = _contaAtual;

    public ContaViewModel(@NonNull Application application) {
        super(application);
        this.repository = new ContaRepository(BancoDB.getDB(application).contaDAO());
        this.contas = repository.getContas();
    }

    void adicionar(Conta c) {
        backgroundThread(
                () -> this.repository.adicionar(c)
        );

    }

    void atualizar(Conta c) {
        //TODO implementar
        backgroundThread(
                () -> this.repository.atualizar(c)
        );
    }

    void remover(Conta c) {
        //TODO implementar
        backgroundThread(
                () -> repository.remover(c)
        );
    }

    void buscarPeloNumero(String numeroConta) {
        //TODO implementar
        backgroundThread(
                () -> {
                    Conta c = repository.buscarPeloNumero(numeroConta);
                    _contaAtual.postValue(c);
                }
        );
    }
    private void backgroundThread(Runnable r) {new Thread(r).start();}
}

