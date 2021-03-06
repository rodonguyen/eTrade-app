package server.DataSourceClasses;

import common.dataClasses.DataCollection;
import common.dataClasses.OrganisationalUnit;
import server.DBConnection;

import java.sql.*;

/**
 * Provides needed functions to interact with "organisationalUnits" database for data
 */
public class OrganisationsDataSource extends DataSource {

    // SQL query strings
    private static final String CREATE_TABLE =
            """
                    CREATE TABLE IF NOT EXISTS          organisationalUnits (
                        organisation_id                 INTEGER PRIMARY KEY AUTOINCREMENT,
                        organisation_name               VARCHAR(16) NOT NULL,
                        credits  DECIMAL(10,2)          NOT NULL DEFAULT 0
                        );""";
    private static final String ADD_ORGANISATION = "INSERT INTO organisationalUnits(organisation_id, organisation_name, credits) VALUES (?, ?, ?);";
    private static final String DELETE_ORGANISATION = "DELETE FROM organisationalUnits WHERE organisation_id=?";
    private static final String GET_ORGANISATION = "SELECT * FROM organisationalUnits WHERE organisation_id=?";
    private static final String GET_ALL_ORGANISATION = "SELECT * FROM organisationalUnits";
    private static final String EDIT_ORGANISATION =
            "UPDATE organisationalUnits\n" +
            "SET organisation_name=?, credits=? " +
            "WHERE organisation_id=?";
    private static final String DELETE_ALL = "DELETE FROM organisationalUnits";
    protected static final String GET_MAX_ID = "SELECT organisation_id FROM organisationalUnits";


    // Prepared statements
    private PreparedStatement addOrganisation;
    private PreparedStatement deleteOrganisation;
    private PreparedStatement getOrganisation;
    private PreparedStatement editOrganisation;
    private PreparedStatement getAllOrganisation;
    private PreparedStatement deleteAll;

    /**
     * Connect to the database then create table if not exists
     */
    public OrganisationsDataSource() {
        connection = DBConnection.getInstance();
        try {
            Statement st = connection.createStatement();
            st.execute(CREATE_TABLE);
            addOrganisation = connection.prepareStatement(ADD_ORGANISATION);
            deleteOrganisation = connection.prepareStatement(DELETE_ORGANISATION);
            getOrganisation = connection.prepareStatement(GET_ORGANISATION);
            editOrganisation = connection.prepareStatement(EDIT_ORGANISATION);
            getAllOrganisation = connection.prepareStatement(GET_ALL_ORGANISATION);
            deleteAll = connection.prepareStatement(DELETE_ALL);
            getMaxId = connection.prepareStatement(GET_MAX_ID);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Delete all organisations from the database
     */
    public void deleteAll()
    {
        try {
            deleteAll.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Add new organisational unit if not exists
     * @param newOrganisationalUnit OrganisationalUnit object to input
     */
    public void addOrganisation(OrganisationalUnit newOrganisationalUnit){
        try {
            //set values into the above query
            int newOrgId = newOrganisationalUnit.getId() == null ? getNextId() : newOrganisationalUnit.getId();
            addOrganisation.setInt(1, newOrgId);
            addOrganisation.setString(2, newOrganisationalUnit.getName());
            addOrganisation.setFloat(3, newOrganisationalUnit.getBalance());
            addOrganisation.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Delete an OrganisationalUnit if exists
     * @param id of the organisation
     */
    public void deleteOrganisation(int id) throws Exception {
        if (id < 0) throw new Exception("Parameter id must not be negative");
        //set values into the above query
        deleteOrganisation.setInt(1, id);
        deleteOrganisation.executeUpdate();
    }

    /**
     * Return already existed organisational unit.
     * @param id OrganisationalUnit Object input to get values from
     * @return Object-OrganisationalUnit
     */
    public OrganisationalUnit getOrganisation(int id) {
        //Create dummy object to store data
        OrganisationalUnit dummy = null;
        ResultSet rs;
        try {
            getOrganisation.setInt(1, id);
            rs = getOrganisation.executeQuery();
            //Store data into the dummy
            dummy = new OrganisationalUnit(rs.getInt("organisation_id"),
                    rs.getString("organisation_name"),
                    rs.getFloat("credits"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dummy;
    }

    /**
     * Get all organisations from the database
     * @return an Organisation DataCollection
     */
    public DataCollection<OrganisationalUnit> getOrganisationList() throws SQLException {
        DataCollection<OrganisationalUnit> organisations = new DataCollection<>();
        ResultSet rs = getAllOrganisation.executeQuery();
        while (rs.next()) {
            Integer nextId = rs.getInt(1);
            organisations.add(getOrganisation(nextId));
        }
        return organisations;
    }
    /**
     * A method to update an Organisational Unit information on  database
     * @param organisationNewInfo an Organisational Unit class object containing new data
     */
    public void editOrganisation(OrganisationalUnit organisationNewInfo)  {
        try {
            editOrganisation.setString(1, organisationNewInfo.getName());
            editOrganisation.setFloat(2, organisationNewInfo.getBalance());
            editOrganisation.setInt(3, organisationNewInfo.getId());
            editOrganisation.executeUpdate();
        } catch (SQLException e){
            e.printStackTrace();
        }
    }
}