package bean;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import util.DBMgr;

public class appDAO {
	DBMgr dbm;
	Connection con;
	PreparedStatement stmt;
	ResultSet rs;

	public appDAO() {
		dbm = DBMgr.getInstance();
	}

	public Vector<appDTO> getapp() {
		Vector<appDTO> list = new Vector<appDTO>();

		con = dbm.getConnection();

		String sql = "select * from app order by id";

		try {
			// 데이터 베이스에 있는 데이터들을 가져와서 리스트에 추가함 (result_app.jsp에 사용)
			stmt = con.prepareStatement(sql);
			rs = stmt.executeQuery();
			while (rs.next()) {
				appDTO dto = new appDTO();

				dto.setId(rs.getInt(1));
				dto.setPackagename(rs.getString(2));
				dto.setAvg_power(rs.getDouble(3));

				list.add(dto);
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			// 항상 처리가 끝나면 close해줘야 함.
			dbm.close(con, stmt, rs);
		}
		return list;
	}
}
