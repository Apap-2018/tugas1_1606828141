package com.apap.tugas1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.apap.tugas1.model.JabatanModel;
import com.apap.tugas1.model.PegawaiModel;
import com.apap.tugas1.service.PegawaiService;


@Controller
public class PegawaiController {
	@Autowired
	private PegawaiService pegawaiService;
	
	@RequestMapping("/")
	private String home() {
		return "home";
	}
	
	@RequestMapping(value = "/pegawai")
	private String view(@RequestParam(value = "nip") String nip, Model model) {
		PegawaiModel pegawai = pegawaiService.getPegawaiBynip(nip);
		model.addAttribute("pegawai", pegawai);
		model.addAttribute("jabatan", pegawai.getJabatanList());
		
		//Pilih Gaji Tertinggi
		double gajiTertinggi = 0;
		for (JabatanModel jabatan: pegawai.getJabatanList()) {
			if (jabatan.getGajiPokok() > gajiTertinggi) {
				gajiTertinggi = jabatan.getGajiPokok();
			}
		}
		
		//Hitung Gaji Pokok
		double tunjungan = pegawai.getInstansi().getProvinsi().getPresentaseTunjangan() * 0.01 * gajiTertinggi;
		double gajiPokok = gajiTertinggi + tunjungan;
		model.addAttribute("gaji", Math.round(gajiPokok));
		return "view-pegawai";
	}
}
