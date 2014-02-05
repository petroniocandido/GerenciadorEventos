/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifnmg.GerenciamentoEventos.DataAccess;

import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Entidade;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos.Repositorio;
import java.util.HashMap;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.FlushModeType;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author petronio
 * @param <T>
 */
public class DAOGenerico<T extends Entidade> implements Repositorio<T> {

    @PersistenceContext(name = "GerenciamentoEventosPU")
    private EntityManager manager;
    
    private Class tipo;
    private Exception erro;
    private String logicalConnector = " and ";
    private StringBuilder where = new StringBuilder();
    private StringBuilder update = new StringBuilder();
    private StringBuilder order = new StringBuilder();
    private HashMap<String,String> join = new HashMap<String,String>();
    private HashMap<Integer,Object> params = new HashMap<Integer, Object>();
    
     public DAOGenerico(Class t) {
        tipo = t;     
    }

    public EntityManager getManager() {
        return manager;
    }

    public void setManager(EntityManager manager) {
        this.manager = manager;
    }
     
     
    
    /**
     * Cria um filtro para consulta de igualdade  
     * @param campo Atributo da classe que se deseja filtrar 
     * @param valor Valor do filtro
     * @author petronio
     * @return 
     */
     
     @PostConstruct
     public void configure() {
         manager.setFlushMode(FlushModeType.COMMIT);
     }
     
    protected DAOGenerico<T> IgualA(String campo, Object valor) {
        addOp(campo,"=",valor);
        return this;
    }

    protected DAOGenerico<T> DiferenteDe(String campo, Object valor) {
        addOp(campo,"<>",valor);
        return this;
    }

    protected DAOGenerico<T> MaiorQue(String campo, Object valor) {
        addOp(campo,">",valor);
        return this;
    }

    protected DAOGenerico<T> MaiorOuIgualA(String campo, Object valor) {
        addOp(campo,">=",valor);
        return this;
    }

    protected DAOGenerico<T> MenorQue(String campo, Object valor) {
        addOp(campo,"<",valor);
        return this;
    }

    protected DAOGenerico<T> MenorOuIgualA(String campo, Object valor) {
        addOp(campo,"<=",valor);
        return this;
    }
    
    private void addSpecialOp(String campo, String op){
        if(where.length() > 0) {
            where.append(logicalConnector);
        }

        if(!campo.contains(".")) {
            where.append("o.");
        }

        where.append(campo).append(" ").append(op);        
    }

    protected DAOGenerico<T> Like(String campo, String valor) {
        if(valor == null || valor.toString().length() == 0)
            return this;
        addSpecialOp(campo,"like '%"+valor+"%'");
        return this;
    }
    
    
    protected DAOGenerico<T> ENulo(String campo) {
        addSpecialOp(campo, "is null");
        return this;
    }

    
    protected DAOGenerico<T> NaoENulo(String campo) {
        addSpecialOp(campo, "is null");
        return this;
    }
    
    protected DAOGenerico<T> Ordenar(String campo, String sentido) {
        if(order.length() > 0)
            order.append(",");
        order.append("o.").append(campo).append(" ").append(sentido);
        return this;
    }

    protected DAOGenerico<T> Setar(String campo, Object valor) {
        int key = params.size();

        if(update.length() > 0) {
            update.append(", ");
        }

        update.append("o.").append(campo).append(" = ").append(" :p").append(Integer.toString(key));
        params.put(key, valor);
        
        
        return this;
    }

    protected boolean Atualiza() {
        try {
            StringBuilder sql = new StringBuilder("update ").append(tipo.getSimpleName()).append(" o set ").append(update.toString());

            if(where.length() > 0) {
                sql.append(" where ").append(where.toString());
            }
            
            Query query = manager.createQuery(sql.toString(),tipo); 

            for(Integer key : params.keySet()){
                query.setParameter("p"+key.toString(), params.get(key));
            }

            query.executeUpdate();
            
            return true;
        }
        catch(Exception ex){
            setErro(ex);
            return false;
        }
        finally{
            join.clear();
            params.clear();
            where = new StringBuilder();
            order = new StringBuilder();
            update = new StringBuilder();
        }
    }

    protected boolean Apaga() {
        try {
            StringBuilder sql = new StringBuilder("delete from ").append(tipo.getSimpleName()).append(" o ");

            if(join.size() > 0){
                for(String key : join.keySet()) {
                    sql.append(" join o.").append(key).append(" ").append(join.get(key));
                }
            }
            
            if(where.length() > 0) {
                sql.append(" where ").append(where.toString());
            }

            Query query = manager.createQuery(sql.toString(),tipo); 

            for(Integer key : params.keySet()){
                query.setParameter("p"+key.toString(), params.get(key));
            }
            
            query.executeUpdate();
            
            return true;
        }
        catch(Exception ex){
            setErro(ex);
            return false;
        }
        finally{
            join.clear();
            params.clear();
            where = new StringBuilder();
            order = new StringBuilder();
        }
    }
    
    private Query processQuery() {
        try {
            StringBuilder sql = new StringBuilder("select o from ").append(tipo.getSimpleName()).append(" o ");

            if(join.size() > 0){
                for(String key : join.keySet()) {
                    sql.append(" join o.").append(key).append(" ").append(join.get(key));
                }
            }
            if(where.length() > 0) {
                sql.append(" where ").append(where.toString());
            }
            if(order.length() > 0) {
                sql.append(" order by ").append(order.toString());
            }
            
            String tmp = sql.toString();

            Query query = manager.createQuery(tmp,tipo); 

            for(Integer key : params.keySet()){
                query.setParameter("p"+key.toString(), params.get(key));
            }

            return query;
        }
        catch(Exception ex){
            setErro(ex);
            return null;
        }
        finally{
            join.clear();
            params.clear();
            where = new StringBuilder();
            order = new StringBuilder();
        }
    }
    
    protected T Abrir() {
        try {
            return (T)processQuery().getSingleResult();
        }
        catch(Exception ex){
            setErro(ex);
            return null;
        }
    }
    
    public List<T> Buscar() {
        try {
            return processQuery().getResultList();
        }
        catch(Exception ex){
            setErro(ex);
            return null;
        }
    }
    
    private void addOp(String field, String op,  Object value){
        if(value == null ) {
            return;
        }
        if(value.toString().equals("")){
            return;
        }
        
        if(value.toString().equals("0")){
            return;
        }
        
        int key = params.size();

        if(where.length() > 0) {
            where.append(logicalConnector);
        }

        if(!field.contains(".")) {
            where.append("o.");
        }

        where.append(field).append(" ").append(op).append(" :p").append(Integer.toString(key));
        params.put(key, value);
    }

    @Override
    public boolean Salvar(T obj) {
        try {
            
            if(obj.getId() != null && obj.getId() > 0)
               obj =manager.merge(obj);
            else
               manager.persist(obj);
            
            manager.flush(); 
            
            return true;
        } catch (Exception ex) {
            setErro(ex);            
            return false;
        }
    }

    @Override
    public boolean Apagar(T obj) {
        try {
            // Persiste o objeto
            manager.remove(manager.merge(obj));
            return true;
        } catch (Exception ex) {
            setErro(ex);
            return false;
        }
    }

    @Override
    public T Abrir(Long id) {
       try {

            // Persiste o objeto
            T obj = (T) manager.find(tipo, id);

            return obj;

        } catch (Exception ex) {
            setErro(ex);
            return null;
        }
    }
    
    public void setErro(Exception erro) {
        System.out.println(erro.getMessage());
        this.erro = erro;
    }

    @Override
    public Exception getErro() {
        return erro;
    }

    @Override
    public  List<T> Buscar(T filtro) {
        return Buscar();
    }
}
