package com.apap.tugas1.controller;

import java.sql.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.apap.tugas1.model.InstansiModel;
import com.apap.tugas1.model.JabatanModel;
import com.apap.tugas1.model.PegawaiModel;
import com.apap.tugas1.service.InstansiService;
import com.apap.tugas1.service.JabatanService;
import com.apap.tugas1.service.PegawaiService;
import com.apap.tugas1.service.ProvinsiService;


@Controller
public class PegawaiController {
	@Autowired
	private PegawaiService pegawaiService;
	
	@Autowired
	private JabatanService jabatanService;
	
	@Autowired
	private InstansiService instansiService;
	
	@Autowired
	private ProvinsiService provinsiService;
	
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
		double gajiTertinggiMuda = pegawaiService.calculateGajiPokokTerbesar(pegawaiService.getPegawaiBynip(nipMuda));
		double tunjunganMuda = pegawaiService.getPegawaiBynip(nipMuda).getInstansi().getProvinsi().getPresentaseTunjangan() * 0.01 * gajiTertinggiMuda;
		double gajiPokokMuda = gajiTertinggiMuda + tunjunganMuda;
		model.addAttribute("gajiMuda", Math.round(gajiPokokMuda));
		
		//Gaji Tertinggi Tua
		double gajiTertinggiTua = pegawaiService.calculateGajiPokokTerbesar(pegawaiService.getPegawaiBynip(nipTua));
		double tunjunganTua = pegawaiService.getPegawaiBynip(nipTua).getInstansi().getProvinsi().getPresentaseTunjangan() * 0.01 * gajiTertinggiTua;
		double gajiPokokTua = gajiTertinggiTua + tunjunganTua;
		model.addAttribute("gajiTua", Math.round(gajiPokokTua));
		
		model.addAttribute("pegawaiMuda", pegawaiService.getPegawaiBynip(nipMuda));
		model.addAttribute("jabatanMuda",pegawaiService.getPegawaiBynip(nipMuda).getJabatanList());
		model.addAttribute("pegawaiTua", pegawaiService.getPegawaiBynip(nipTua));
		model.addAttribute("jabatanTua",pegawaiService.getPegawaiBynip(nipTua).getJabatanList());
		
		return "termuda-tertua";
	}	
	
	@RequestMapping(value = "/pegawai/tambah", method = RequestMethod.GET)
	private String addPegawai(Model model) {
		PegawaiModel pegawai = new PegawaiModel();
		pegawai.setInstansi(new InstansiModel());
		
		model.addAttribute("pegawai", pegawai);
		model.addAttribute("listProvinsi", provinsiService.getListProvinsi());
		model.addAttribute("listJabatan", jabatanService.jabatanList());
		
		return "tambah-pegawai";
	}

	@RequestMapping(value = "/pegawai/tambah", method = RequestMethod.POST)
	private String addPegawaiSubmit(@ModelAttribute PegawaiModel pegawai, Model model) {
		String nip = "";
		
		nip += pegawai.getInstansi().getId();
		
		String[] tanggalLahir = pegawai.getTanggalLahir().toString().split("-");
		String tglLahirString = tanggalLahir[2] + tanggalLahir[1] + tanggalLahir[0].substring(2, 4);
		nip += tglLahirString;

		nip += pegawai.getTahunMasuk();

		int counter = 1;
		for (PegawaiModel pegawaiInstansi : pegawai.getInstansi().getPegawaiInstansi()) {
			if (pegawaiInstansi.getTahunMasuk().equals(pegawai.getTahunMasuk()) && pegawaiInstansi.getTanggalLahir().equals(pegawai.getTanggalLahir())) {
				counter ++;
			}	
		}
		nip += "0" + counter;
		pegawai.setNip(nip);
		pegawaiService.addPegawai(pegawai);
		
		model.addAttribute("pegawai", pegawai);
		return "tambah-pegawai-sukses";
	}
}
