package persistence.database;
import model.*;
import persistence.Repository0;



public interface IOficiuPersoana extends Repository0<Long, OficiuPersoana> {


    OficiuPersoana Login(String user, String password);
}
