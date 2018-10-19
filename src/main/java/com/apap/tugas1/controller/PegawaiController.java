package com.apap.tugas1.controller;

import java.sql.Date;
import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.apap.tugas1.model.InstansiModel;
import com.apap.tugas1.model.JabatanModel;
import com.apap.tugas1.model.PegawaiModel;
import com.apap.tugas1.service.InstansiService;
import com.apap.tugas1.service.JabatanService;
import com.apap.tugas1.service.PegawaiService;


@Controller
public class PegawaiController {
	@Autowired
	private PegawaiService pegawaiService;
	
	@Autowired
	private JabatanService jabatanService;
	
	@Autowired
	private InstansiService instansiService;
	
	@RequestMapping("/")
	private String home(Model model) {
		List<JabatanModel> listJabatan = jabatanService.jabatanList();
		model.addAttribute("listJabatan", listJabatan);
		
		List<InstansiModel> listInstansi = instansiService.listInstansi();
		model.addAttribute("listInstansi", listInstansi);
		return "home";
	}
	
	@RequestMapping(value = "/pegawai")
	private String view(@RequestParam(value = "nip") String nip, Model model) {
		PegawaiModel pegawai = pegawaiService.getPegawaiBynip(nip);
		model.addAttribute("pegawai", pegawai);
		model.addAttribute("jabatan", pegawai.getJabatanList());
		
		//Hitung Gaji Pokok
		double gajiTertinggi = pegawaiService.calculateGajiPokokTerbesar(pegawai);
		double tunjungan = pegawai.getInstansi().getProvinsi().getPresentaseTunjangan() * 0.01 * gajiTertinggi;
		double gajiPokok = gajiTertinggi + tunjungan;
		model.addAttribute("gaji", Math.round(gajiPokok));
		return "view-pegawai";
	}
	
	@RequestMapping(value = "/pegawai/termuda-tertua", method = RequestMethod.GET)
	private String termudaTertua(@RequestParam(value = "idInstansi") long idJabatan, Model model) {
		InstansiModel contoh = instansiService.getInstansiById(idJabatan);
		PegawaiModel dummy = pegawaiService.getPegawaiBynip("5201111161198701");
		PegawaiModel dummyDua = pegawaiService.getPegawaiBynip("7204020393201401");
		Date tanggalDummyMuda = dummy.getTanggalLahir();
		Date tanggalDummyTua = dummyDua.getTanggalLahir();
		Date tanggalTemp = null;
		String nipMuda = null;
		String nipTua = null;
		
		for (PegawaiModel pegawaiCounter : contoh.getPegawaiInstansi()) {
			tanggalTemp = pegawaiCounter.getTanggalLahir();
			
			//Pegawai Muda
			if (tanggalDummyMuda.before(tanggalTemp)) {
				tanggalDummyMuda = tanggalTemp;
				nipMuda = pegawaiCounter.getNip();
			}
			
			//Pegawai Tua
			if (tanggalDummyTua.after(tanggalTemp)) {
				tanggalDummyTua = tanggalTemp;
				nipTua = pegawaiCounter.getNip();
			}
		}
		
		//Gaji Tertinggi Muda
		System.out.println(nipMuda);
		System.out.println(nipTua);
		
		double gajiTertinggiMuda = pegawaiService.calculateGajiPokokTerbesar(pegawaiService.getPegawaiBynip(nipMuda));
		
		double tunjunganMuda = pegawaiService.getPegawaiBynip(nipMuda).getInstansi().getProvinsi().getPresentaseTunjangan() * 0.01 * gajiTertinggiMuda;
		double gajiPokokMuda = gajiTertinggiMuda + tunjunganMuda;
		model.addAttribute("gajiMuda", Math.round(gajiPokokMuda));
		
		//Gaji Tertinggi Tua
		double gajiTertinggiTua = pegawaiService.calculateGajiPokokTerbesar(pegawaiService.getPegawaiBynip(nipTua));
		double tunjungan = pegawaiService.getPegawaiBynip(nipTua).getInstansi().getProvinsi().getPresentaseTunjangan() * 0.01 * gajiTertinggiTua;
		double gajiPokokTua = gajiTertinggiTua + tunjungan;
		model.addAttribute("gajiTua", Math.round(gajiPokokTua));
		
		model.addAttribute("pegawaiMuda", pegawaiService.getPegawaiBynip(nipMuda));
		model.addAttribute("jabatanMuda",pegawaiService.getPegawaiBynip(nipMuda).getJabatanList());
		model.addAttribute("pegawaiTua", pegawaiService.getPegawaiBynip(nipTua));
		model.addAttribute("jabatanTua",pegawaiService.getPegawaiBynip(nipTua).getJabatanList());
		
		return "termuda-tertua";
	}	
}
