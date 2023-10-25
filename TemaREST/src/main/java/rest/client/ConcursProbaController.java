package rest.client;

import domain.Proba;
import persistance.ProbaDBRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import persistance.RepositoryException;

import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Properties;

@CrossOrigin
@RestController
@RequestMapping("/concurs/probe")

public class ConcursProbaController {

    private static final String template = "Hello, %s!";


    private ProbaDBRepo probaDBRepo = new ProbaDBRepo(loadProperties());

    @RequestMapping("/greeting")
    public  String greeting(@RequestParam(value="name", defaultValue="World") String name) {
        return String.format(template, name);
    }


    @RequestMapping( method=RequestMethod.GET)
    public Iterable<Proba> getAll(){
        System.out.println("Get all probs ...");
        return probaDBRepo.findAll();
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String probaError(Exception e) {
        return e.getMessage();
    }
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getById(@PathVariable Long id){
        System.out.println("Get by id "+id);
        Proba user=probaDBRepo.findOne(id);
        if (user==null)
            return new ResponseEntity<String>("User not found",HttpStatus.NOT_FOUND);
        else
            return new ResponseEntity<Proba>(user, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST)
    public Proba create(@RequestBody Proba proba){
        System.out.println("Creating proba ..." + proba);
        probaDBRepo.save(proba);
        System.out.println("S-a creat proba "+proba);
        return proba;

    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<?> update(@PathVariable Long id,@RequestBody Proba proba) {
        try{
            System.out.println("Updating proba ...");
            proba.setId(id);
            Proba proba1 = probaDBRepo.update(proba);
            return new ResponseEntity<Proba>(proba1,HttpStatus.OK);
        }
        catch (Exception ex){
            System.out.println("Ctrl Update proba exception");
            return new ResponseEntity<String>(ex.getMessage(),HttpStatus.BAD_REQUEST);
        }

    }
    // @CrossOrigin(origins = "http://localhost:3000")
    @RequestMapping(value="/{id}", method= RequestMethod.DELETE)
    public ResponseEntity<?> delete(@PathVariable Long id) throws SQLException {
        System.out.println("Deleting proba ... "+id);
        try {
            Proba proba= probaDBRepo.delete(id);
            return new ResponseEntity<Proba>(proba,HttpStatus.OK);
        } catch (Exception ex){
            System.out.println("Ctrl Delete proba exception");
            return new ResponseEntity<Long>(Long.valueOf(ex.getMessage()),HttpStatus.BAD_REQUEST);
        }

    }


/*   @RequestMapping("/{proba}/name")
    public String name(@PathVariable String user){
        Proba result=probaDBRepo.findOne(user);
        System.out.println("Result ..."+result);

        return result.getName();
    }*/

    public static Properties loadProperties(){
        Properties serverProps=new Properties();
        try {
            serverProps.load(new FileReader("server.properties"));
            System.out.println("Server properties set. ");
            serverProps.list(System.out);
        } catch (IOException e) {
            System.err.println("Cannot find server.properties "+e);
        }
        return serverProps;
    }
}
