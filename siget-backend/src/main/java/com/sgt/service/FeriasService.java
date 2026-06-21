package com.sgt.service;

import com.sgt.dto.request.FeriasRequest;
import com.sgt.dto.response.FeriasResponse;
import com.sgt.entity.Ferias;
import com.sgt.entity.Funcionario;
import com.sgt.repository.FeriasRepository;
import com.sgt.repository.FuncionarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FeriasService {

    private final FeriasRepository feriasRepository;
    private final FuncionarioRepository funcionarioRepository;

    public List<FeriasResponse> listarPorFuncionario(Long funcionarioId) {
        return feriasRepository
            .findByFuncionarioIdAndAtivoTrue(funcionarioId)
            .stream()
            .map(FeriasResponse::from)
            .toList();
    }

    public FeriasResponse cadastrar(FeriasRequest request) {
        Funcionario funcionario = funcionarioRepository.findById(request.funcionarioId())
            .orElseThrow(() -> new RuntimeException(
                "Funcionário não encontrado: " + request.funcionarioId()));

        validarPeriodo(request);

        Ferias ferias = Ferias.builder()
            .funcionario(funcionario)
            .dataInicio(request.dataInicio())
            .dataFim(request.dataFim())
            .diasDireito(request.diasDireito() != null ? request.diasDireito() : 30)
            .diasGozados(request.diasGozados())
            .build();

        return FeriasResponse.from(feriasRepository.save(ferias));
    }

    public FeriasResponse atualizar(Long id, FeriasRequest request) {
        Ferias ferias = buscarAtivo(id);

        validarPeriodo(request);

        ferias.setDataInicio(request.dataInicio());
        ferias.setDataFim(request.dataFim());
        ferias.setDiasDireito(request.diasDireito() != null ? request.diasDireito() : 30);
        ferias.setDiasGozados(request.diasGozados());

        return FeriasResponse.from(feriasRepository.save(ferias));
    }

    public void desativar(Long id) {
        Ferias ferias = buscarAtivo(id);
        ferias.setAtivo(false);
        feriasRepository.save(ferias);
    }

    private void validarPeriodo(FeriasRequest request) {
        if (request.dataFim().isBefore(request.dataInicio())) {
            throw new RuntimeException("Data fim não pode ser anterior à data início.");
        }
        if (request.diasGozados() > request.diasDireito()) {
            throw new RuntimeException("Dias gozados não podem ser maiores que dias de direito.");
        }
    }

    private Ferias buscarAtivo(Long id) {
        return feriasRepository.findById(id)
            .filter(Ferias::getAtivo)
            .orElseThrow(() -> new RuntimeException("Férias não encontradas: " + id));
    }
}