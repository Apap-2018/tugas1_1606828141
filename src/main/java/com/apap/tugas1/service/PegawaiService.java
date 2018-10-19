package com.apap.tugas1.service;

import com.apap.tugas1.model.PegawaiModel;

public interface PegawaiService {
	PegawaiModel getPegawaiBynip (String nip);
	public double calculateGajiPokokTerbesar (PegawaiModel pegawai);
}
