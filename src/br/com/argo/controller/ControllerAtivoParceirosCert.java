package br.com.argo.controller;

import java.math.BigDecimal;

import br.com.argo.service.ServiceAtualizarAtivo;
import br.com.sankhya.extensions.actionbutton.AcaoRotinaJava;
import br.com.sankhya.extensions.actionbutton.ContextoAcao;
import br.com.sankhya.extensions.actionbutton.Registro;

public class ControllerAtivoParceirosCert implements AcaoRotinaJava {

	@Override
	public void doAction(ContextoAcao ctx) throws Exception {
	    Registro linhas[] = ctx.getLinhas();
	    StringBuilder mensagemSucesso = new StringBuilder();
	    ServiceAtualizarAtivo servicos = new ServiceAtualizarAtivo();

	    try {
	        // Cabeçalho da mensagem HTML
	        mensagemSucesso.append("<!DOCTYPE html>")
	            .append("<html><body>")
	            .append("<div style='text-align: center;'>")
	            .append("<img src='https://argofruta.com/wp-content/uploads/2021/05/Logo-text-green.png' style='width:120px; height:90px;'>")
	            .append("</div>")
	            .append("<div style='display: flex; align-items: center; justify-content: center;'>")
	            .append("<img src='https://cdn-icons-png.flaticon.com/256/189/189677.png' style='width:23px; height:23px; margin-right:5px;'>")
	            .append("<p style='color:#274135; font-family:verdana; font-size:15px; margin:0;'><b>Atualização de Parceiro</b></p>")
	            .append("</div>")
	            .append("<p style='font-family:courier; color:#274135;'>Certificados atualizados:<br><br>");

	        // Pega o parâmetro vindo do botão (S ou N)
	        String ativoParam = (String) ctx.getParam("ATIVO");

	        // Itera sobre as linhas selecionadas
	        for (Registro registro : linhas) {
	            BigDecimal seq = (BigDecimal) registro.getCampo("SEQUENCIA");
	            BigDecimal codparc = (BigDecimal) registro.getCampo("CODPARC");

	            // Chama o serviço que faz o UPDATE
	            servicos.atualizaCampos(codparc, seq, ativoParam);

	            // Adiciona na mensagem de sucesso
	            mensagemSucesso.append("Parceiro: ").append(codparc)
	                .append(" | Sequência: ").append(seq)
	                .append(" -> Status: ").append(ativoParam.equals("S") ? "Ativado" : "Desativado")
	                .append("<br>");
	        }

	        mensagemSucesso.append("</p></body></html>");

	        // Retorna mensagem formatada
	        ctx.setMensagemRetorno(mensagemSucesso.toString());

	    } catch (Exception e) {
	        ctx.mostraErro("Erro ao atualizar certificados: " + e.getMessage());
	    }
	}
}