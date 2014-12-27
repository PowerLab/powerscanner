package bean;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import util.DBMgr;

public class powerDAO {
	
	DBMgr dbm;
	Connection con;
	PreparedStatement stmt;
	ResultSet rs;
	
	public powerDAO(){
		dbm = DBMgr.getInstance();
	}
	
	public Vector<powerDTO> getpower(){
		Vector<powerDTO> list = new Vector<powerDTO>();
		
		con = dbm.getConnection();
		
		String sql = "select * from power order by id";
		
		try {
			// ������ ���̽��� �ִ� �����͵��� �����ͼ� ����Ʈ�� �߰��� (result.jsp�� ���)
			stmt = con.prepareStatement(sql);
			rs = stmt.executeQuery();
			while (rs.next()) {
				powerDTO dto = new powerDTO();
				
				dto.setId(rs.getInt(1));
				dto.setPackagename(rs.getString(2));
				dto.setTotal(rs.getInt(3));
				dto.setLed(rs.getInt(4));
				dto.setCpu(rs.getInt(5));
				dto.setWifi(rs.getInt(6));
				dto.setThreeg(rs.getInt(7));
				dto.setGps(rs.getInt(8));
				dto.setAudio(rs.getInt(9));
				
				list.add(dto);
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			// �׻� ó���� ������ close����� ��.
			dbm.close(con, stmt, rs);
		}
		return list;
	}
	
	public boolean inputPower(powerDTO dto){
		boolean b = false;
		
		con = dbm.getConnection();
		// app_id.nextval(����Ŭ) => mysql���� AUTO_INCREMENT�� ���� ���.
		String sql = "insert into power values(app_id.nextval,?,?,?,?,?,?,?,?)";
		
		try {
			// ������ ���� �����͸� ���� ����
			stmt = con.prepareStatement(sql);
			stmt.setString(1, dto.getPackagename());
			stmt.setInt(2, dto.getTotal());
			stmt.setInt(3, dto.getLed());
			stmt.setInt(4, dto.getCpu());
			stmt.setInt(5, dto.getWifi());
			stmt.setInt(6, dto.getThreeg());
			stmt.setInt(7, dto.getGps());
			stmt.setInt(8, dto.getAudio());

			int a = stmt.executeUpdate();
			if(a > 0) b =true;
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			// �׻� ó���� ������ close����� ��.
			dbm.close(con, stmt);
		}
		return b;
	}
}
