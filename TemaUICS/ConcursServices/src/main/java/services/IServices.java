package services;

import model.OficiuPersoana;
import model.Participant;
import model.Proba;



import java.util.List;

public interface IServices {

    OficiuPersoana login(String username, String password, IObserver client) throws Exception;

    void logout(OficiuPersoana oficiuPersoana,IObserver client) throws Exception;

    Iterable<Participant> getAllParticipants() throws Exception;

    Participant addParticipant(Participant participant) throws Exception;

    Participant updateParticipant(Participant participant) throws Exception;

    Proba findProba1(Long id) throws Exception;

    Iterable<Proba> findAllProbs()throws Exception;

    int participantCategorieProba(int m, int M, String p)throws Exception;

    List<Participant> ParticipantiProba(String proba)throws Exception;

    Participant searchwithCNP(String CNP)throws Exception;
    List<Integer> getNrCategorie(Integer varsta) throws Exception;
    Iterable<Participant> findProba(int varstamin, int varstamax, String proba) throws Exception;



}
