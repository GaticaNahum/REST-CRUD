package mx.edu.utez.controller;

import mx.edu.utez.model.Employee;
import mx.edu.utez.util.ConnectionMysql;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Path("/employee")
public class Service {

    Connection con;
    PreparedStatement pstm;
    Statement statement;
    ResultSet rs;

    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)



    public List<Employee> getEmployees(){
        List<Employee> employees = new ArrayList<>();
        try{
            con = ConnectionMysql.getConnection();
            String query = "SELECT employees.employeeNumber, employees.firstName FROM employees; ";
            statement= con.createStatement();
            rs = statement.executeQuery(query);
            while(rs.next()){
                Employee employee = new Employee();
                employee.setEmployeeNumber(rs.getInt("employeeNumber"));
                employee.setFirstName(rs.getString("firstName"));
                employees.add(employee);
            }
        }catch (SQLException ex){
            ex.printStackTrace();
        }finally {
            closeConnection();
        }
        return employees;
    }

    @DELETE
    @Path("/{employeeNumber}")
    @Produces(MediaType.APPLICATION_JSON)
    public String removerEmpleado(@PathParam("employeeNumber") int employeeNumber){
        boolean state = false;
        String view = "";

        try{
            con = ConnectionMysql.getConnection();
            String query = "DELETE FROM employees WHERE employeeNumber = ?";
            pstm = con.prepareStatement(query);
            pstm.setInt(1, employeeNumber);
            state = pstm.executeUpdate() == 1;
            if(state){
                view = "Se elimino correctamente al usuario";
            }else {
                view = "Hubo un error al eliminar";
            }
        }catch(SQLException e){
            e.printStackTrace();
        }finally{
            closeConnection();
        }
        return view;
    }

    @PUT
    @Path("/{employeeNumber}/{firstName}/{lastName}/{extension}/{email}/{officeCode}/{reportsTo}/{jobTitle}")
    @Produces(MediaType.APPLICATION_JSON)
    public String actualizarEmpleado(@PathParam("employeeNumber") int employeeNumber, @PathParam("firstName") String firstName,
                                 @PathParam("lastName") String lastName, @PathParam("extension") String extension,
                                 @PathParam("email") String email, @PathParam("officeCode") String officeCode,
                                 @PathParam("reportsTo") String reportsTo, @PathParam("jobTitle") String jobTitle){
        String view = "";
        boolean state = false;
        try{
            con = ConnectionMysql.getConnection();
            String query = "UPDATE employees SET firstName = ?, lastName = ?, extension = ?, " +
                    "email = ?, officeCode = ?, reportsTo = ?, jobTtitle = ? WHERE employeeNumber = ?)";
            pstm = con.prepareStatement(query);
            pstm.setString(2, lastName);
            pstm.setString(1, firstName);
            pstm.setString(3, extension);
            pstm.setString(4, email);
            pstm.setString(5, officeCode);
            pstm.setString(6, reportsTo);
            pstm.setString(7, jobTitle);
            pstm.setInt(8, employeeNumber);
            state = pstm.executeUpdate() == 1;
            if(state){
                view = "Se actualizo correctamente";
            }else {
                view = "Hubo un error al actualizar";
            }
        }catch(SQLException e){
            e.printStackTrace();
        }finally{
            closeConnection();
        }
        return view;
    }


    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Employee getEmployee(@PathParam("id") int employeeNumber){
        Employee employee = new Employee();
        try{
            con = ConnectionMysql.getConnection();
            String query = "SELECT employees.firstName FROM employees WHERE employees.employeeNumber = ?;";
            pstm = con.prepareStatement(query);
            pstm.setInt(1,employeeNumber);
            rs = pstm.executeQuery();
            if(rs.next()){
                employee.setFirstName(rs.getString("firstName"));
            }
        }catch (SQLException ex){
            ex.printStackTrace();

        }finally {
            closeConnection();
        }
        return employee;
    }

    @POST
    @Path("/{employeeNumber}/{lastName}/{firstName}/{extension}/{email}/{officeCode}/{reportsTo}/{jobTitle}")
    @Produces(MediaType.APPLICATION_JSON)
    public Employee crearEmpleado(@PathParam("employeeNumber") int employeeNumber,@PathParam("lastName") String lastName, @PathParam("firstName") String firstName,
                                @PathParam("extension") String extension,@PathParam("email") String email,
                                @PathParam("officeCode") String officeCode,@PathParam("reportsTo") String reportsTo,
                                @PathParam("jobTitle") String jobTitle){
        boolean state = false;
        Employee employee = new Employee();
        try{
            con = ConnectionMysql.getConnection();
            String query = "INSERT INTO employees(employeeNumber,lastName, firstName, extension, email, officeCode, reportsTo, jobTitle) " +
                    "VALUES (?,?,?,?,?,?,?,?)";
            pstm = con.prepareStatement(query);
            pstm.setInt(1,employeeNumber);
            pstm.setString(2,lastName);
            pstm.setString(3,firstName);
            pstm.setString(4,extension);
            pstm.setString(5,email);
            pstm.setString(6,officeCode);
            pstm.setString(7,reportsTo);
            pstm.setString(8,jobTitle);
            state = pstm.executeUpdate() == 1;
            if (state){
                System.out.println("Se registro correctamente al usuario");
            }else {
                System.out.println("Hubo un problema al registrar al usuario");
            }
        }catch (SQLException ex){
            ex.printStackTrace();

        }finally {
            closeConnection();
        }
        return employee;
    }

    public void closeConnection(){
        try{
            if(con != null){
                con.close();
            }
            if (pstm != null){
                pstm.close();
            }
            if (rs != null){
                rs.close();
            }
        }catch (SQLException ex){
            ex.printStackTrace();
        }
    }
}
