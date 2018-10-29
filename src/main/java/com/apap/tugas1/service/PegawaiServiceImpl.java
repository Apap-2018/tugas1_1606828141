package com.apap.tugas1.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.apap.tugas1.model.JabatanModel;
import com.apap.tugas1.model.PegawaiModel;
import com.apap.tugas1.repository.PegawaiDb;


@Service
@Transactional
public class PegawaiServiceImpl implements PegawaiService {
	@Autowired
	private PegawaiDb pegawaiDb;
	
	@Override
	public PegawaiModel getPegawaiBynip (String nip) {
		return pegawaiDb.findBynip(nip);
	}
	
	@Override
	public double calculateGajiPokokTerbesar (PegawaiModel pegawai) {
		double gajiTertinggi = 0;
		for (JabatanModel jabatan: pegawai.getJabatanList()) {
			if (jabatan.getGajiPokok() > gajiTertinggi) {
				gajiTertinggi = jabatan.getGajiPokok();
			}
		}
		return gajiTertinggi;
	}
	
	@Override
	public void addPegawai(PegawaiModel pegawai) {
		pegawaiDb.save(pegawai);
	}

	@Override
	public List<PegawaiModel> getAllPegawai() {
		return pegawaiDb.findAll();
	}

	
}
