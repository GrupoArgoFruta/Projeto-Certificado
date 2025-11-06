package br.com.argo.controller;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;

import br.com.sankhya.extensions.actionbutton.AcaoRotinaJava;
import br.com.sankhya.extensions.actionbutton.ContextoAcao;
import br.com.sankhya.extensions.actionbutton.Registro;
import br.com.sankhya.jape.EntityFacade;
import br.com.sankhya.jape.core.JapeSession;
import br.com.sankhya.jape.core.JapeSession.SessionHandle;
import br.com.sankhya.jape.dao.JdbcWrapper;
import br.com.sankhya.jape.sql.NativeSql;
import br.com.sankhya.jape.vo.DynamicVO;
import br.com.sankhya.jape.wrapper.JapeFactory;
import br.com.sankhya.jape.wrapper.JapeWrapper;
import br.com.sankhya.modelcore.MGEModelException;
import br.com.sankhya.modelcore.comercial.impostos.ImpostosHelpper;
import br.com.sankhya.modelcore.util.EntityFacadeFactory;

public class ControllerUpCultivar implements AcaoRotinaJava {

	@Override
	public void doAction(ContextoAcao ctx) throws Exception {
	    Registro[] linhas = ctx.getLinhas();
	    StringBuilder mensagemSucesso = new StringBuilder();

	    try {
	        // Cabeçalho HTML
	        mensagemSucesso.append("<!DOCTYPE html>")
	            .append("<html><body>")
	            .append("<div style='text-align: center;'>")
	            .append("<img src='https://argofruta.com/wp-content/uploads/2021/05/Logo-text-green.png' style='width:120px; height:90px;'>")
	            .append("</div>")
	            .append("<div style='display: flex; align-items: center; justify-content: center;'>")
	            .append("<img src='https://cdn-icons-png.flaticon.com/256/189/189677.png' style='width:23px; height:23px; margin-right:5px;'>")
	            .append("<p style='color:#274135; font-family:verdana; font-size:15px; margin:0;'><b>Atualização do cultivar</b></p>")
	            .append("</div>")
	            .append("<p style='font-family:courier; color:#274135;'>Certificados atualizados:<br><br>");

	        // 🔹 Parâmetros do botão — com tratamento de null
	        String P1_cultivar = ctx.getParam("CULTIVAR1") != null ? ((String) ctx.getParam("CULTIVAR1")).trim() : null;
	        String P2_cultivar = ctx.getParam("CULTIVAR2") != null ? ((String) ctx.getParam("CULTIVAR2")).trim() : null;
	        String P3_cultivar = ctx.getParam("CULTIVAR3") != null ? ((String) ctx.getParam("CULTIVAR3")).trim() : null;
	        for (Registro registro : linhas) {
	            BigDecimal seq = (BigDecimal) registro.getCampo("SEQUENCIA");
	            BigDecimal codparc = (BigDecimal) registro.getCampo("CODPARC");

	            // Atualiza os campos
	            atualizarCampos(codparc, seq, P1_cultivar, P2_cultivar,P3_cultivar);

	            // Mensagem de sucesso
	            mensagemSucesso.append("Parceiro: <b>")
	                .append(codparc)
	                .append("</b> | Sequência: <b>")
	                .append(seq)
	                .append("</b><br>");
	        }

	        mensagemSucesso.append("</p></body></html>");
	        ctx.setMensagemRetorno(mensagemSucesso.toString());

	    } catch (Exception e) {
	        ctx.mostraErro("Erro ao atualizar certificados: " + e.getMessage());
	    }
	}
	public void atualizarCampos(BigDecimal codparc, BigDecimal seq, String P1_cultivar, String P2_cultivar,
			String P3_cultivar) throws Exception {
		JapeSession.SessionHandle hnd = null;
		try {
			hnd = JapeSession.open();
			JapeWrapper dao = JapeFactory.dao("AD_CERTIFICADOS");

			DynamicVO vo = dao.findByPK(codparc, seq);
			if (vo != null) {
				dao.prepareToUpdate(vo)
				.set("CULTIVAR1", P1_cultivar != null ? P1_cultivar.trim() : null)
				.set("CULTIVAR2", P2_cultivar != null ? P2_cultivar.trim() : null)
				.set("CULTIVAR3", P3_cultivar != null ? P3_cultivar.trim() : null)
				.update();
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("Erro ao atualizar o campo certificados: " + e.getMessage());
		} finally {
			JapeSession.close(hnd);
		}
	}
	public void atualizaCampos(BigDecimal codparc, BigDecimal seq, String P1_cultivar, String P2_cultivar, String p3_cultivar) throws MGEModelException {
	    SessionHandle hnd = null;
	    try {
	        hnd = JapeSession.open();

	        // Também garante que os valores sejam gravados sem espaços e em maiúsculo
	        JapeFactory.dao("AD_CERTIFICADOS")
	            .prepareToUpdateByPK(codparc, seq)
	             .set("CULTIVAR1", P1_cultivar != null ? P1_cultivar.trim() : null)
                 .set("CULTIVAR2", P2_cultivar != null ? P2_cultivar.trim() : null)
                 .set("CULTIVAR3", p3_cultivar != null ? p3_cultivar.trim() : null)
	            .update();

	    } catch (Exception e) {
	        throw new MGEModelException("Erro ao atualizar o campo certificados: " + e.getMessage(), e);
	    } finally {
	        JapeSession.close(hnd);
	    }
	}

	
	

//	public void atualizarCampos1(BigDecimal codparc, BigDecimal seq, String P1_cultivar, String P2_cultivar) throws Exception {
//	    JapeSession.SessionHandle hnd = null;
//	    try {
//	        hnd = JapeSession.open();
//	        JapeWrapper dao = JapeFactory.dao("AD_CERTIFICADOS");
//
//	        // Busca pela chave composta (CODPARC, SEQUENCIA)
//	        DynamicVO vo = dao.findByPK(codparc, seq);
//	        if (vo != null) {
//	            dao.prepareToUpdate(vo)
//	               .set("CULTIVAR1", P1_cultivar)
//	               .set("CULTIVAR2", P2_cultivar)
//	               .update();
//	        }
//
//	    } catch (Exception e) {
//	        e.printStackTrace();
//	        throw new Exception("Erro ao atualizar campos do AD_CERTIFICADOS: " + e.getMessage());
//	    } finally {
//	        JapeSession.close(hnd);
//	    }
//	}
	


//	public void atualizaCultvar(BigDecimal codparc, BigDecimal seq, String P1_cultivar, String P2_cultivar) throws MGEModelException {
//	    SessionHandle hnd = null;
//	    try {
//	        hnd = JapeSession.open();
//
//	        JapeWrapper dao = JapeFactory.dao("AD_CERTIFICADOS");
//
//	        // Busca o registro
//	        DynamicVO vo = dao.findOne("CODPARC = ? AND SEQUENCIA = ?", codparc, seq);
//
//	        if (vo != null) {
//	            // Atualiza os campos desejados
//	            dao.prepareToUpdate(vo)
//	                .set("CULTIVAR1", P1_cultivar)
//	                .set("CULTIVAR2", P2_cultivar)
//	                .update();
//	        } else {
//	            throw new MGEModelException("Registro não encontrado para CODPARC=" + codparc + " e SEQUENCIA=" + seq);
//	        }
//
//	    } catch (Exception e) {
//	        throw new MGEModelException("Erro ao atualizar CULTIVAR1 e CULTIVAR2: " + e.getMessage(), e);
//	    } finally {
//	        JapeSession.close(hnd);
//	    }
//	}
//	public void Atualizacultivarex1(BigDecimal seq, String P1_cultivar, String P2_cultivar) throws Exception {
//	    SessionHandle hnd = JapeSession.open();
//	    hnd.setFindersMaxRows(-1);
//	    EntityFacade entity = EntityFacadeFactory.getDWFFacade();
//	    JdbcWrapper jdbc = entity.getJdbcWrapper();
//	    jdbc.openSession();
//
//	    try {
//	        NativeSql sql = new NativeSql(jdbc);
//	        sql.appendSql("UPDATE AD_CERTIFICADOS SET CULTIVAR1 = :CULTIVAR1, CULTIVAR2 = :CULTIVAR2 WHERE SEQUENCIA = :SEQUENCIA");
//	        sql.setNamedParameter("CULTIVAR1", P1_cultivar);
//	        sql.setNamedParameter("CULTIVAR2", P2_cultivar);
//	        sql.setNamedParameter("SEQUENCIA", seq);
//	        sql.executeUpdate();
//
//	    } catch (Exception e) {
//	        e.printStackTrace();
//	        throw new Exception("Erro ao atualizar CULTIVAR1 e CULTIVAR2: " + e.getMessage());
//	    } finally {
//	        JdbcWrapper.closeSession(jdbc);
//	        JapeSession.close(hnd);
//	    }
//	}



}

//AND (UPPER(CON.DESCRCONTROLE) LIKE UPPER('%' || :TELA  ||   '%') OR (:TELA IS NULL))
