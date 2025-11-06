package br.com.argo.service;

import java.math.BigDecimal;

import br.com.sankhya.jape.core.JapeSession;
import br.com.sankhya.jape.core.JapeSession.SessionHandle;
import br.com.sankhya.jape.wrapper.JapeFactory;
import br.com.sankhya.modelcore.MGEModelException;

public class ServiceAtualizarAtivo {
    public void atualizaCampos(BigDecimal codparc, BigDecimal seq, String ativoParam) throws MGEModelException {
        SessionHandle hnd = null;
        try {
            hnd = JapeSession.open();

            // Atualiza o campo ATIVO conforme o parâmetro recebido
            JapeFactory.dao("AD_CERTIFICADOS").prepareToUpdateByPK(codparc, seq)
                .set("ATIVO", ativoParam) // 'S' ou 'N'
                .update();

        } catch (Exception e) {
            throw new MGEModelException("Erro ao atualizar o campo ATIVO: " + e.getMessage(), e);
        } finally {
            JapeSession.close(hnd);
        }
    }
}
//1= 1 - Péssimo;2= 2 - Ruim;3= 3 - Regular;4= 4 - Bom;5= 5 - Ótimo