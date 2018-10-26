package com.westar.core.dao;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.westar.base.model.BandMAC;

@Repository
public class BandMACDao extends BaseDao {

	/**
	 * 通过Mac
	 * @param macStr
	 * @return
	 */
	public BandMAC queryValiDateByMac(String macStr) {
		StringBuffer sql = new StringBuffer();
		List<Object> args = new ArrayList<Object>();
		sql.append("\n select * from bandMAC a where 1=1");
		sql.append("\n and macName=?");
		args.add(macStr);
		return (BandMAC) this.objectQuery(sql.toString(), args.toArray(), BandMAC.class);
	}

	

}
