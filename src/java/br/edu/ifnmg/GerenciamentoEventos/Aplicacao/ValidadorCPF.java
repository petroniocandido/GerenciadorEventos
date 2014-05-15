/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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