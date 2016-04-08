/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.edu.ifnmg.GerenciamentoEventos.Infraestrutura;

import br.edu.ifnmg.DomainModel.AreaConhecimento;
import br.edu.ifnmg.DomainModel.Perfil;
import br.edu.ifnmg.DomainModel.Pessoa;
import br.edu.ifnmg.DomainModel.Services.AreaConhecimentoRepositorio;
import br.edu.ifnmg.DomainModel.Services.ConfiguracaoService;
import br.edu.ifnmg.DomainModel.Services.LogService;
import br.edu.ifnmg.DomainModel.Services.MailService;
import br.edu.ifnmg.DomainModel.Services.PerfilRepositorio;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Evento;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos.DistribuicaoTrabalhosAvaliadoresService;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos.PessoaRepositorioLocal;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos.SubmissaoRepositorio;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Submissao;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.SubmissaoStatus;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author petronio
 */
@Stateless
public class DistribuicaoTrabalhosAvaliadoresServiceImpl implements DistribuicaoTrabalhosAvaliadoresService {
    @EJB
    SubmissaoRepositorio daoSub;
    
    @EJB
    AreaConhecimentoRepositorio daoArea;
    
    @EJB
    PessoaRepositorioLocal daoPes;
    
    @EJB
    PerfilRepositorio daoPerfil;
    
    @EJB
    ConfiguracaoService configuracao;
    
    @EJB 
    LogService log;
    
    @EJB
    MailService mail; 
    
    @Override
    public void Distribuir(Evento e){
        List<AreaConhecimento> areas = daoArea.Areas();
        
        String textoMensagem = configuracao.get("MENSAGEMDISTRIBUICAOAVALIADOR");
        String codperfil = configuracao.get("PERFILSELECAOAVALIADOR");
        
        Perfil perfil = daoPerfil.Abrir(Long.parseLong(codperfil));
        
        for(AreaConhecimento area : areas){
            List<Submissao> submissoes = daoSub.Buscar(SubmissaoStatus.Pendente, e, null, area);
            List<Pessoa> avaliadores = daoPes.BuscarAvaliadores(perfil, area);
            
            int tcount = 0;
            int tfalha = 0;
            int count = 0;
            int numaval = avaliadores.size();
            
            if(numaval == 0 || submissoes.isEmpty())
                continue;
            
            for(Submissao s : submissoes){
                
                Pessoa avaliador = null;
                
                boolean check = true;  
                int cloop = 0;
                while(check && cloop < 10){
                    avaliador = avaliadores.get(count % numaval);
                    if(s.getAutor1().toUpperCase().contains(avaliador.getNome().toUpperCase())
                            || s.getAutor2().toUpperCase().contains(avaliador.getNome().toUpperCase())
                            || s.getAutor3().toUpperCase().contains(avaliador.getNome().toUpperCase())
                            || s.getAutor4().toUpperCase().contains(avaliador.getNome().toUpperCase())
                            || s.getAutor5().toUpperCase().contains(avaliador.getNome().toUpperCase())){
                        check = true;
                        count = count + 1;
                        cloop = cloop + 1;
                    } else {
                        check = false;
                    }
                }
                
                count = count + 1;
                
                if(check)
                    continue;
                
                s.add(avaliador);
                s.setStatus(SubmissaoStatus.Atribuido);
                
                if(daoSub.Salvar(s)) {
                    String tmp = textoMensagem.replace("###TITULO###", s.getTitulo());
                    mail.enviar(avaliador.getEmail(), "[" + e.getNome() + "] Trabalho disponível para avaliação",tmp);
                    log.Append("Falha ao atribuir a submissão " + s.toString() + " ao avaliador " + avaliador.toString() );
                    tcount++;
                } else {
                    log.Append("Falha ao atribuir a submissão " + s.toString() + " ao avaliador " + avaliador.toString() + ". " + daoSub.getErro().getMessage());
                    tfalha++;
                }
            }
            
            log.Append("Atribuídas submissões para " + area.getNome() + ": Sucesso - " + tcount + " - Falha " + tfalha);
            
        }
        
    }
}
