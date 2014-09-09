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

package br.edu.ifnmg.GerenciamentoEventos.DomainModel;

import java.util.Date;

/**
 *
 * @author petronio
 */

public interface Entidade  {

    public void setId(Long id);
    public Long getId();
    public Pessoa getCriador();
    public void setCriador(Pessoa obj);
    public Date getDataCriacao();
    public void setDataCriacao(Date obj);
    public Pessoa getUltimoAlterador();
    public void setUltimoAlterador(Pessoa obj);
    public Date getDataUltimaAlteracao();
    public void setDataUltimaAlteracao(Date obj);
    public Long getVersao();
    //public boolean isAtivo();
    //public void setAtivo(boolean value);
}
