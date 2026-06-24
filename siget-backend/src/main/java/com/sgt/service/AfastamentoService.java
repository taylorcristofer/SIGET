package com.sgt.service;

import com.sgt.dto.request.AfastamentoRequest;
import com.sgt.dto.response.AfastamentoResponse;
import com.sgt.entity.Afastamento;
import com.sgt.entity.Funcionario;
import com.sgt.repository.AfastamentoRepository;
import com.sgt.repository.FuncionarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AfastamentoService {

    private final AfastamentoRepository afastamentoRepository;
    private final FuncionarioRepository funcionarioRepository;

    public List<AfastamentoResponse> listarPorFuncionario(Long funcionarioId) {
        return afastamentoRepository
            .findByFuncionarioIdAndAtivoTrue(funcionarioId)
            .stream()
            .map(AfastamentoResponse::from)
            .toList();
    }

    public List<AfastamentoResponse> listarPorTipo(String tipo) {
        return afastamentoRepository
            .findByTipoAndAtivoTrue(tipo.toUpperCase())
            .stream()
            .map(AfastamentoResponse::from)
            .toList();
    }

    public AfastamentoResponse cadastrar(AfastamentoRequest request) {
        Funcionario funcionario = funcionarioRepository.findById(request.funcionarioId())
            .orElseThrow(() -> new RuntimeException(
                "Funcionário não encontrado: " + request.funcionarioId()));

        validar(request);

        Afastamento afastamento = Afastamento.builder()
            .funcionario(funcionario)
            .tipo(request.tipo().toUpperCase())
            .dataInicio(request.dataInicio())
            .dataFim(request.dataFim())
            .dias(request.dias())
            .observacao(request.observacao())
            .build();

        return AfastamentoResponse.from(afastamentoRepository.save(afastamento));
    }

    public AfastamentoResponse atualizar(Long id, AfastamentoRequest request) {
        Afastamento afastamento = buscarAtivo(id);

        validar(request);

        afastamento.setTipo(request.tipo().toUpperCase());
        afastamento.setDataInicio(request.dataInicio());
        afastamento.setDataFim(request.dataFim());
        afastamento.setDias(request.dias());
        afastamento.setObservacao(request.observacao());

        return AfastamentoResponse.from(afastamentoRepository.save(afastamento));
    }

    public void desativar(Long id) {
        Afastamento afastamento = buscarAtivo(id);
        afastamento.setAtivo(false);
        afastamentoRepository.save(afastamento);
    }

    private void validar(AfastamentoRequest request) {
        if (request.dataFim().isBefore(request.dataInicio())) {
            throw new RuntimeException("Data fim não pode ser anterior à data início.");
        }
        if (!List.of("ATESTADO", "SUSPENSAO", "INSS", "FALTA_JUSTIFICADA", "LICENCA_MATERNIDADE")
        .contains(request.tipo().toUpperCase())) {
    throw new RuntimeException(
        "Tipo inválido. Use: ATESTADO, SUSPENSAO, INSS, FALTA_JUSTIFICADA ou LICENCA_MATERNIDADE.");
}
        if (request.dias() <= 0) {
            throw new RuntimeException("Dias deve ser maior que zero.");
        }
    }

    private Afastamento buscarAtivo(Long id) {
        return afastamentoRepository.findById(id)
            .filter(Afastamento::getAtivo)
            .orElseThrow(() -> new RuntimeException("Afastamento não encontrado: " + id));
    }
}