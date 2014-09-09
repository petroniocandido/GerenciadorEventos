/*
 *   This file is part of SGEA - Sistema de Gestão de Eventos Acadêmicos - TADS IFNMG Campus Januária.
 *
 *   SGEA is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   SGEA is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with SGEA.  If not, see <http://www.gnu.org/licenses/>.
 */

package br.edu.ifnmg.GerenciamentoEventos.Infraestrutura.Dados;

import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos.ArquivoRepositorio;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;
import javax.ejb.Singleton;

/**
 *
 * @author petronio
 */
@Singleton
public class ArquivoDAO 
    extends DAOGenerico<Arquivo> 
    implements ArquivoRepositorio {
    
    public ArquivoDAO(){
        super(Arquivo.class);
    }

    @Override
    public List<Arquivo> Buscar(Arquivo filtro) {
        return IgualA("id", filtro.getId())
                .Like("nome", filtro.getNome())
                .IgualA("responsavel", filtro.getResponsavel())
                .Like("uri", filtro.getUri())
                .Buscar();
    }

    @Override
    public Arquivo Abrir(String uri) {
        return IgualA("uri", uri).Abrir();
    }

    @Override
    public Arquivo Salvar(InputStream is, String nome, String pastaBase, Pessoa criador) {
        try {
            if(is == null || nome == null || nome.length() == 0 || pastaBase == null)
                return null;
            
            String extension = nome.substring(nome.lastIndexOf("."));
            String name = java.util.UUID.randomUUID().toString() + extension;
            File file = new File(pastaBase + name);
            try (OutputStream os = new FileOutputStream(file)) {
                byte buf[] = new byte[1024];
                int len;
                while ((len = is.read(buf)) > 0) {
                    os.write(buf, 0, len);
                }
            }
            is.close();

            Arquivo arquivo = new Arquivo();
            arquivo.setNome(nome);
            arquivo.setUri(name);
            arquivo.setResponsavel(criador);
            arquivo.setCriador(criador);
            arquivo.setResponsavel(criador);
            arquivo.setDataCriacao(new Date());
            arquivo.setDataUltimaAlteracao(new Date());
            arquivo.setUltimoAlterador(criador);
            
            if(super.Salvar(arquivo))
                return super.Refresh(arquivo);
            else
                return null;

        } catch (IOException ex) {
            setErro(ex);
            return null;
        }
    }

    @Override
    public boolean Apagar(Arquivo a, String pastaBase) {
        try {
            File file = new File(pastaBase + a.getUri());
            
            if(!file.delete())
                return false;
            
            return super.Apagar(a);

        } catch (Exception ex) {
            setErro(ex);
            return false;
        }
    }
    
}
