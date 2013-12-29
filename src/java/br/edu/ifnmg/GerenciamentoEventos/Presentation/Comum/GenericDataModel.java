/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifnmg.GerenciamentoEventos.Presentation.Comum;



import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Entidade;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos.Repositorio;
import java.util.List;
import javax.faces.model.ListDataModel;
import org.primefaces.model.SelectableDataModel;

public class GenericDataModel<T> extends ListDataModel<T> implements SelectableDataModel<T> {

    Repositorio<T> dao;

    public GenericDataModel(Repositorio<T> repo) {
        dao = repo;
    }

    public GenericDataModel(List<T> data, Repositorio<T> repo) {
        super(data);
        dao = repo;
    }

    @Override
    public T getRowData(String rowKey) {
        //In a real app, a more efficient way like a query by rowKey should be implemented to deal with huge data  

        if (rowKey == null || rowKey.equals("")) {
            return null;
        }

        try {
            Long id = Long.parseLong(rowKey);

            T obj = dao.Abrir(id);

            return obj;
        } catch (Exception ex) {
            return null;
        }
    }

    @Override
    public Object getRowKey(T obj) {
        Entidade t = (Entidade)obj;
        return t.getId();
    }
}
