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

package br.edu.ifnmg.GerenciamentoEventos.Aplicacao;

/* ****************************************************** 
  * Código Original: 
  * Autor: Allan Peron 
  * http://javafree.uol.com.br/topic-851371-Validacao-de-CPF.html
  * ****************************************************** 
  * Modificações feitas para fácil aplicação 
*/  
  
public abstract class ValidadorCPF extends Object {  
  
    private static String calcDigVerif(String num) {  
        Integer primDig, segDig;  
        int soma = 0, peso = 10;  
        for (int i = 0; i < num.length(); i++)  
                soma += Integer.parseInt(num.substring(i, i + 1)) * peso--;  
  
        if (soma % 11 == 0 | soma % 11 == 1)  
            primDig = new Integer(0);  
        else  
            primDig = new Integer(11 - (soma % 11));  
  
        soma = 0;  
        peso = 11;  
        for (int i = 0; i < num.length(); i++)  
                soma += Integer.parseInt(num.substring(i, i + 1)) * peso--;  
          
        soma += primDig.intValue() * 2;  
        if (soma % 11 == 0 | soma % 11 == 1)  
            segDig = new Integer(0);  
        else  
            segDig = new Integer(11 - (soma % 11));  
  
        return primDig.toString() + segDig.toString();  
    }  
  
    private static int calcSegDig(String cpf, int primDig) {  
        int soma = 0, peso = 11;  
        for (int i = 0; i < cpf.length(); i++)  
                soma += Integer.parseInt(cpf.substring(i, i + 1)) * peso--;  
          
        soma += primDig * 2;  
        if (soma % 11 == 0 | soma % 11 == 1)  
            return 0;  
        else  
            return 11 - (soma % 11);  
    }  
  
    public static String geraCPF() {  
        String iniciais = "";  
        Integer numero;  
        for (int i = 0; i < 9; i++) {  
            numero = new Integer((int) (Math.random() * 10));  
            iniciais += numero.toString();  
        }  
        return iniciais + calcDigVerif(iniciais);  
    }  
  
    public static boolean validaCPF(String cpf) {  
        cpf = cpf.replace(".", "").replace("-", "");
        if (cpf.length() != 11)  
            return false;  
  
        String numDig = cpf.substring(0, 9);  
        return calcDigVerif(numDig).equals(cpf.substring(9, 11));  
    }  
}  
