package com.apap.tugas1.service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.apap.tugas1.repository.JabatanPegawaiDb;

@Service
@Transactional
public class JabatanPegawaiServiceImpl implements JabatanPegawaiService {
	@Autowired
	private JabatanPegawaiDb jabatanPegawaiDb;
	
	@Override
	public long sizeJabatanPegawai() {
		return jabatanPegawaiDb.count();
	}
}
