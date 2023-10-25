package persistance;

import domain.Proba;
import persistence.Repository0;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.sql.Statement;

public class ProbaDBRepo implements IProba {

    private JdbcUtils dbUtils;



    public ProbaDBRepo(Properties props){

        dbUtils = new JdbcUtils(props);
    }
    @Override
    public Proba findOne(Long aLong) {
        Connection connection = dbUtils.getConnection();
        Proba proba = null;
        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Proba WHERE id=(?)")) {
            preparedStatement.setLong(1,aLong);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    int varstamin = resultSet.getInt("varstamin");
                    int varstamax = resultSet.getInt("varstamax");
                    String proba1 = resultSet.getString("proba");
                    proba = new Proba(varstamin, varstamax, proba1);
                    proba.setId(aLong);
                    return proba;
                }

            }

        } catch (SQLException e) {

            System.err.println("Error DB" + e);
        }

        return proba;
    }

    @Override
    public Iterable<Proba> findAll() {

        Connection connection = dbUtils.getConnection();
        List<Proba> probs = new ArrayList<>();
        try(PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Proba")){
            try(ResultSet resultSet = preparedStatement.executeQuery()){
                while(resultSet.next()){
                    Long id =resultSet.getLong("id");
                    int varstamin = resultSet.getInt("varstamin");
                    int varstamax = resultSet.getInt("varstamax");
                    String proba = resultSet.getString("proba");

                    Proba proba1= new Proba(varstamin,varstamax,proba);

                    proba1.setId(id);
                    probs.add(proba1);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error DB"+e);
        }
        return probs;
    }


    @Override
    public Proba save(Proba entity) {
        Connection connection = dbUtils.getConnection();
        try(PreparedStatement preparedStratement = connection.prepareStatement("INSERT INTO Proba(varstamin,varstamax,proba) VALUES (?,?,?)", Statement.RETURN_GENERATED_KEYS)){
            preparedStratement.setInt(1,entity.getVarstamin());
            preparedStratement.setInt(2,entity.getVarstamax());
            preparedStratement.setString(3,entity.getProba());
            int result = preparedStratement.executeUpdate();
            if(result==0)
                throw new RuntimeException("Nu s-a adaugat proba");
            try(ResultSet generatedKeys = preparedStratement.getGeneratedKeys()){
                if(generatedKeys.next()){
                    Long id = generatedKeys.getLong(1);
                    entity.setId(id);
                    return entity;
                }
                else{
                    throw new RuntimeException("Nu s-a adaugat proba");
                }
            }
            /*PreparedStatement pr = connection.prepareStatement("SELECT * from Proba WHERE varstamin=(?) AND varstamax=(?) AND proba=(?)");
            pr.setInt(1,entity.getVarstamin());
            pr.setInt(2,entity.getVarstamax());
            pr.setString(3,entity.getProba());
            ResultSet rs = pr.executeQuery();
            if(rs.next()){
                Long id = rs.getLong("id");
                entity.setId(id);
                return entity;
            }*/
        } catch (SQLException e) {

            System.err.println("Error DB"+e);
        }
        return null;
        /*Proba proba = new Proba(entity.getVarstamin(),entity.getVarstamax(),entity.getProba());
        proba.setId(entity.getId());
        return proba;*/
    }

    @Override
    public Proba delete(Long aLong) throws Exception {
        Connection connection = dbUtils.getConnection();
        Proba proba = findOne(aLong);
        System.out.println(proba);
        if(proba==null){
            throw new Exception("Proba inexistenta");
        }
        try (PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM Proba WHERE id=(?)")) {
            preparedStatement.setLong(1, aLong);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {

            System.err.println("Error DB" + e);
        }
        return proba;
    }

    @Override
    public Proba update(Proba entity) {
        Connection connection = dbUtils.getConnection();
        Proba proba =findOne(entity.getId());
        try(PreparedStatement preparedStatement = connection.prepareStatement("UPDATE Proba SET varstamin=(?),varstamax=(?),proba=(?) WHERE id=(?)")){
            preparedStatement.setInt(1,entity.getVarstamin());
            preparedStatement.setInt(2,entity.getVarstamax());
            preparedStatement.setString(3,entity.getProba());
            preparedStatement.setLong(4,entity.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {

            System.err.println("Error DB"+e);
        }
        /*Proba proba =findOne(entity.getId());
        return proba;*/
        return entity;
    }


}
