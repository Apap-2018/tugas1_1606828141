package com.apap.tugas1.service;

import java.util.List;

import com.apap.tugas1.model.PegawaiModel;

public interface PegawaiService {
	PegawaiModel getPegawaiBynip (String nip);
	public double calculateGajiPokokTerbesar (PegawaiModel pegawai);
	List<PegawaiModel> getAllPegawai();
	void addPegawai(PegawaiModel pegawai);
}