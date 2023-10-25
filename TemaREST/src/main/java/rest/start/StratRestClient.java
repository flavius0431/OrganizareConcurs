package rest.start;

import domain.Proba;
import org.springframework.web.client.RestClientException;
import rest.client.ServiceException;
import rest.services.ProbaClient;

import java.util.Arrays;

public class StratRestClient {

    private final static ProbaClient probaClient=new ProbaClient();
    public static void main(String[] args) {
        //  RestTemplate restTemplate=new RestTemplate();
        Proba probaT=new Proba(2,59,"Testul Flavius11");


        try{
            probaT = probaClient.create(probaT);
            System.out.println("Result received "+probaT);
            probaT.setProba("Testul Flavius 2");
            Proba finalProbaT = probaT;
            show(()-> System.out.println(probaClient.update(finalProbaT)));
            show(()-> System.out.println(Arrays.toString(probaClient.getAll())));
            probaClient.delete(probaT.getId());
            show(()-> System.out.println(Arrays.toString(probaClient.getAll())));
        }catch(RestClientException ex){
            System.out.println("Exception ... "+ex.getMessage());
        }
        show(()-> System.out.println(probaClient.getOne(4L)));
    }



    private static void show(Runnable task) {
        try {
            task.run();
        } catch (ServiceException e) {
            System.out.println("Service exception"+ e);
        }
    }
}
