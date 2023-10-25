package server;


import model.OficiuPersoana;
import model.Participant;
import model.Proba;
import persistence.database.IOficiuPersoana;
import persistence.database.IParticipant;
import persistence.database.IProba;
import services.IObserver;
import services.IServices;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Service implements IServices {

    private IOficiuPersoana opRepo;
    private IParticipant pRepo;
    private IProba pRepo2;

    private Map<Long, IObserver> loggedOficii;

    private final int defaultThreadsNo=55556;

    public Service(IOficiuPersoana opRepo, IParticipant pRepo, IProba pRepo2) {
        this.opRepo = opRepo;
        this.pRepo = pRepo;
        this.pRepo2 = pRepo2;
        loggedOficii = new ConcurrentHashMap<>();
    }

    //public OficiuPersoana logIn(String user, String password) {
       // return this.opRepo.Login(user, password);
    //}


    public OficiuPersoana login(String username, String password, IObserver client) throws Exception {
        OficiuPersoana oficiuPersoana = opRepo.Login(username, password);
        System.out.println("ajunge aici");
        if (oficiuPersoana == null) {
            throw new Exception("Authentication failed.");
        }
        /*if (loggedOficii.get(oficiuPersoana.getId()) != null) {
            throw new Exception("User already logged in.");
        }*/
        loggedOficii.put(oficiuPersoana.getId(), client);
        System.out.println("log in: " + oficiuPersoana.getId());
        return oficiuPersoana;
    }

    public void logout(OficiuPersoana oficiuPersoana,IObserver client) {
        loggedOficii.remove(oficiuPersoana.getId());
        System.out.println("log out: " + oficiuPersoana.getId());
    }

    private void notifyOficiiUpdate() {
        ExecutorService executor= Executors.newFixedThreadPool(defaultThreadsNo);
        for(Map.Entry<Long,IObserver> entry : loggedOficii.entrySet()){
            System.out.println(entry.getKey());
            executor.execute(() -> {
                try {
                    System.out.println("Notifying oficii update  ");
                    entry.getValue().updateOficii(getAllParticipants());
                } catch (Exception e) {
                    System.err.println("Error notifying oficii update " + e);
                }
            });
        }
        executor.shutdown();
    }

    public Iterable<Participant> getAllParticipants() {
        return this.pRepo.findAll();
    }

    public Participant addParticipant(Participant participant) {
        notifyOficiiUpdate();
        return this.pRepo.save(participant);
    }

    public Participant updateParticipant(Participant participant) {
        notifyOficiiUpdate();
        return this.pRepo.update(participant);
    }

    public OficiuPersoana findOfPers(String user,String pass){
        OficiuPersoana ofi=null;
        for(OficiuPersoana of:opRepo.findAll()){
            if(Objects.equals(of.getUser(), user) && Objects.equals(of.getPassword(), pass)){
                return of;
            }
        }
        return ofi;

    }

    public Proba findProba1(Long id) {
        return this.pRepo2.findOne(id);
    }

    public Iterable<Proba> findAllProbs() {
        return this.pRepo2.findAll();
    }

    public int participantCategorieProba(int m, int M, String p) {
        return this.pRepo.participantiCategorie(m, M, p);
    }

    public List<Participant> ParticipantiProba(String proba) {
        return this.pRepo.participantlaProba(proba);
    }

    public Participant searchwithCNP(String CNP) {
        return this.pRepo.searchwithCNP(CNP);
    }

    public List<Integer> getNrCategorie(Integer varsta) {
        Integer varstamin = 0;
        Integer varstamax = 0;
        List<Integer> list = new ArrayList<>();
        if (varsta >= 6 && varsta <= 8) {
            varstamin = 6;
            varstamax = 8;
        }
        if (varsta >= 9 && varsta <= 11) {
            varstamin = 9;
            varstamax = 11;
        }
        if (varsta >= 12 && varsta <= 15) {
            varstamin = 12;
            varstamax = 14;
        }

        list.add(varstamin);
        list.add(varstamax);

        return list;
    }

    public Iterable<Participant> findProba(int varstamin, int varstamax, String proba) {
        Iterable<Participant> participants = this.pRepo.findAll();
        List<Participant> probe = new ArrayList<>();
        for (Participant p : participants) {
            if (p.getVarsta() >= varstamin && p.getVarsta() <= varstamax) {
                for (Proba prob : p.getProbe()) {
                    if (prob.getProba().equals(proba)) {
                        probe.add(p);
                    }
                }
            }
        }

        return probe;
    }
}

