package com.apap.tugas1.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.apap.tugas1.model.JabatanModel;
import com.apap.tugas1.repository.JabatanDb;

@Service
@Transactional
public class JabatanServiceImpl implements JabatanService {
	@Autowired
	private JabatanDb jabatanDb;
	
	@Override
	public void addJabatan (JabatanModel jabatan) {
		jabatanDb.save(jabatan);
	}
	
	@Override
	public void deleteJabatanById (long id) {
		jabatanDb.deleteById(id);
	}
	
	@Override
	public JabatanModel findJabatanById (long id) {
		return jabatanDb.getOne(id);
	}
	
	@Override
	public List<JabatanModel> jabatanList () {
		return jabatanDb.findAll();
	}
}
