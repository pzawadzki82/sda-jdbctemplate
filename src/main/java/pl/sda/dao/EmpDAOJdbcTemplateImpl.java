package pl.sda.dao;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import pl.sda.domain.Department;
import pl.sda.domain.Employee;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by pzawa on 02.02.2017.
 */
public class EmpDAOJdbcTemplateImpl implements EmpDAO {

    private static String QUERY_BY_ID  = "SELECT empno, ename, job, manager, hiredate, salary, commision, deptno FROM Emp WHERE empno = :empno";
    private static String QUERY_ALL  = "SELECT empno, ename, job, manager, hiredate, salary, commision, deptno FROM Emp";
    private static String UPDATE_STMT= "UPDATE Emp set ename = :ename, job = :job, manager = :manager, hiredate = :hiredate, salary = :salary, commision = :commision, deptno = :deptno  WHERE empno = :empno";
    private static String DELETE_STMT= "DELETE FROM Emp WHERE empno = :empno";

    private final DataSourceFactory dataSourceFactory;

    public EmpDAOJdbcTemplateImpl(DataSourceFactory dataSourceFactory){
        this.dataSourceFactory = dataSourceFactory;
    }


    private static RowMapper<Employee> rowMapper = (rs, rowNum) -> {
        int empno = rs.getInt("empno");
        String ename = rs.getString("ename");
        String job = rs.getString("job");
        int manager = rs.getInt("manager");
        Date hiredate = rs.getDate("hiredate");
        BigDecimal salary = rs.getBigDecimal("salary");
        BigDecimal commision = rs.getBigDecimal("commision");
        int deptno = rs.getInt("deptno");

        return new Employee(empno, ename, job, manager, hiredate, salary, commision, deptno);
    };

    @Override
    public Employee findById(int id) throws Exception {
        DataSource ds = dataSourceFactory.getDataSource();
        NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(ds);

        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("empno", id);

        try {
            return jdbcTemplate.queryForObject(QUERY_BY_ID, parameters, rowMapper);
        }catch(EmptyResultDataAccessException e){
            return null;
        }
    }

    @Override
    public List<Employee> findByAll() throws Exception {
        DataSource ds = dataSourceFactory.getDataSource();
        NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(ds);

        Map<String, Object> parameters = new HashMap<String, Object>();

        try {
            return jdbcTemplate.query(QUERY_ALL, parameters, rowMapper);
        }catch(EmptyResultDataAccessException e){
            return null;
        }
    }

    @Override
    public void create(Employee employee) throws Exception {
        DataSource ds = dataSourceFactory.getDataSource();
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(ds)
                .withTableName("Emp")
                .usingColumns("empno", "ename", "job", "manager", "hiredate", "salary", "commision", "deptno");

        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("empno", employee.getEmpno());
        parameters.put("ename", employee.getEname());
        parameters.put("job", employee.getJob());
        parameters.put("manager", employee.getManager());
        parameters.put("hiredate", employee.getHiredate());
        parameters.put("salary", employee.getSalary());
        parameters.put("commision", employee.getCommision());
        parameters.put("deptno", employee.getDeptno());

        simpleJdbcInsert.execute(parameters);
    }

    @Override
    public void update(Employee employee) throws Exception {
        DataSource ds = dataSourceFactory.getDataSource();
        NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(ds);

        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("empno", employee.getEmpno());
        parameters.put("ename", employee.getEname());
        parameters.put("job", employee.getJob());
        parameters.put("manager", employee.getManager());
        parameters.put("hiredate", employee.getHiredate());
        parameters.put("salary", employee.getSalary());
        parameters.put("commision", employee.getCommision());
        parameters.put("deptno", employee.getDeptno());

        int numberOfAffectedRows = jdbcTemplate.update(UPDATE_STMT, parameters);
        System.out.println("EmpDAO.update() number of affected rows: " + numberOfAffectedRows);
    }

    @Override
    public void delete(int id) throws Exception {
        DataSource ds = dataSourceFactory.getDataSource();
        NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(ds);

        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("empno", id);
        ;

        int numberOfAffectedRows = jdbcTemplate.update(DELETE_STMT, parameters);
        System.out.println("EmpDAO.delete() number of affected rows: " + numberOfAffectedRows);
    }
}
