
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Prane
 */
public class Sql {

    Connection con;

    public Sql() throws ClassNotFoundException {

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/arm", "root", "moversonroad333");
        } catch (SQLException ex) {
            Logger.getLogger(Sql.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public static void main(String[] args) throws SQLException, ClassNotFoundException {

        Sql obj = new Sql();
        ResultSet rs = obj.getTicketDetails("1");

        if (rs.next()) {

            System.out.println(rs.getString(1) + " | " + rs.getString(2) + " | " + rs.getString(3) + " | " + rs.getInt(4) + " | " + rs.getString(5));

            while (rs.next()) {
                System.out.println(rs.getString(1) + " | " + rs.getString(2) + " | " + rs.getString(3) + " | " + rs.getInt(4) + " | " + rs.getString(5));
            }

        } else {
            System.out.println("Empty data set");
        }

    }

    public ResultSet getRouteDetails(String rootId) throws SQLException {

        String sql = "select * from route where RT_ID = ?";

        PreparedStatement pst = con.prepareStatement(sql);
        pst.setString(1, rootId);

        return pst.executeQuery();

    }

    boolean verifyCust(String id, String pass) throws SQLException {

        String sql = "select * FROM PASSENGER WHERE PS_ID = ? AND PASSWORD =?;";
        PreparedStatement pst = con.prepareStatement(sql);
        pst.setInt(1, Integer.parseInt(id));
        pst.setString(2, pass);

        ResultSet rs = pst.executeQuery();

        if (rs.next()) {
            return true;
        }
        return false;

    }

    ResultSet getTicketDetails(String id) throws SQLException {

        String sql = "select r.source as SOURCE,r.destination as DESTINATION,r.rt_id as ROUTEID,r.rt_cost as ROUTECOST,f.a_tme as ARRIVAL,"
                + "f.d_tme as DEPARTURE,p.ps_name as NAME,p.ps_age as AGE,p.ps_mail as EMAIL,"
                + "p.ps_contact AS MOBILE,p.pass_no as PASSPORT,p.ps_address as ADDRESS,"
                + "from route r,flight_schedule f,passenger p,ADMIN A,"
                + "where a.ps_id=1,"
                + "and a.ps_id=p.ps_id,"
                + "and A.rt_id=R.RT_ID,"
                + "and f.rt_id=r.rt_id;";

        String sql1 = "select r.source as SOURCE,r.destination as DESTINATION,r.rt_id as ROUTEID,r.rt_cost as ROUTECOST,f.a_tme as ARRIVAL,\n"
                + "	f.d_tme as DEPARTURE,p.ps_name as NAME,p.ps_age as AGE,p.ps_mail as EMAIL,\n"
                + "	p.ps_contact AS MOBILE,p.pass_no as PASSPORT,p.ps_address as ADDRESS\n"
                + "     from route r,flight_schedule f,passenger p,ADMIN A\n"
                + "     where a.ps_id=?\n"
                + "	and a.ps_id=p.ps_id\n"
                + "	and A.rt_id=R.RT_ID\n"
                + "     and f.rt_id=r.rt_id;";

        PreparedStatement pst = con.prepareStatement(sql1);
        pst.setInt(1, Integer.parseInt(id));

        return pst.executeQuery();

    }

    ResultSet getCities() throws SQLException {

        String sql = "select city from places;";
        PreparedStatement pst = con.prepareStatement(sql);

        return pst.executeQuery();

    }

    ResultSet getRouteDetails(String source, String dest) throws SQLException {

        String sql = "SELECT R.SOURCE,R.DESTINATION,R.RT_ID,R.RT_COST,R.AC_ID,F.D_TME,F.A_TME,F.F_ID\n"
                + "FROM ROUTE R,FLIGHT_SCHEDULE F\n"
                + "WHERE R.SOURCE= ?\n"
                + "AND R.DESTINATION= ? \n"
                + "AND R.RT_ID=F.RT_ID;";
        PreparedStatement pst = con.prepareStatement(sql);
        pst.setString(1, source);
        pst.setString(2, dest);

        return pst.executeQuery();

    }

    public int insertIntoAdmin(String routeId, String flightId, String aircraftId, String passId) throws SQLException {

        String sql = "INSERT INTO ADMIN VALUES(?,?,?,?,?);";
        PreparedStatement pst = con.prepareStatement(sql);

        pst.setString(1, aircraftId);
        pst.setString(2, routeId);
        pst.setInt(3, Integer.parseInt(passId));
        pst.setString(4, "BERA");
        pst.setString(5, flightId);

        return pst.executeUpdate();

    }

    public int insertIntoPassenger(String psId, String name, String age, String email, String phone, String passport, String address, String password) throws SQLException {

        String sql = "INSERT INTO PASSENGER(PS_CONTACT,PS_ID,PS_NAME,PS_AGE,PS_ADDRESS,password,pass_no,ps_mail) VALUES(?,?,?,?,?,?,?,?);";
        PreparedStatement pst = con.prepareStatement(sql);

        pst.setLong(1, Long.parseLong(phone));
        pst.setInt(2, Integer.parseInt(psId));
        pst.setString(3, name);
        pst.setInt(4, Integer.parseInt(age));
        pst.setString(5, address);
        pst.setString(6, password);
        pst.setString(7, passport);
        pst.setString(8, email);

        return pst.executeUpdate();

    }

    ResultSet getAllTickets() throws SQLException {

        String sql = " select r.source , r.destination ,r.rt_id as ROUTEID,r.rt_cost as ROUTECOST,f.a_tme as ARRIVAL,\n"
                + "	f.d_tme as DEPARTURE,p.ps_name as NAME,p.ps_age as AGE,p.ps_mail as EMAIL,\n"
                + "	p.ps_contact AS MOBILE,p.pass_no as PASSPORT,p.ps_address as ADDRESS\n"
                + "     from route r,flight_schedule f,passenger p,ADMIN A\n"
                + "     where a.ps_id=p.ps_id\n"
                + "     and A.rt_id=R.RT_ID\n"
                + "     and a.f_id=f.f_id;";
        
        PreparedStatement pst = con.prepareCall(sql);
        
        return pst.executeQuery();


    }

}
