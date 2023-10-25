import persistence.database.*;
import server.Service;
import services.IServices;
import networking.utils.*;

import java.io.IOException;
import java.util.Properties;

public class StartRpcServer {
    private static int defaultPort=55555;
    public static void main(String[] args) {
        Properties serverProps=new Properties();
        try {
            serverProps.load(StartRpcServer.class.getResourceAsStream("/concursserver.properties"));
            System.out.println("Server properties set. ");
            serverProps.list(System.out);
        } catch (IOException e) {
            System.err.println("Cannot find chatserver.properties "+e);
            return;
        }
        IOficiuPersoana oficiuPersoanaRepo=new OficiuPersoanaDBRepo(serverProps);
        IProba probaRepo=new ProbaDBRepo(serverProps);
        IParticipant participantRepo=new ParticipantDBRepo(serverProps, (ProbaDBRepo) probaRepo);
        IServices concursServerImpl=new Service(oficiuPersoanaRepo, (IParticipant) participantRepo, (IProba) probaRepo);
        int chatServerPort=defaultPort;
        try {
            chatServerPort = Integer.parseInt(serverProps.getProperty("concurs.server.port"));
        }catch (NumberFormatException nef){
            System.err.println("Wrong  Port Number"+nef.getMessage());
            System.err.println("Using default port "+defaultPort);
        }
        System.out.println("Starting server on port: "+chatServerPort);
        AbstractServer server = new ChatRpcConcurrentServer(chatServerPort, concursServerImpl);
        try {
            server.start();
        } catch (ServerException e) {
            System.err.println("Error starting the server" + e.getMessage());
        }finally {
            try {
                server.stop();
            }catch(ServerException e){
                System.err.println("Error stopping server "+e.getMessage());
            }
        }
    }
}
