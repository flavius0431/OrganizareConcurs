package services;
import model.OficiuPersoana;
import model.Participant;
import model.Proba;

public interface IObserver {


    void updateOficii( Iterable<Participant> participants) throws Exception;
}
