package com.apap.tugas1.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.apap.tugas1.model.JabatanModel;
import com.apap.tugas1.service.JabatanService;	

@Controller
public class JabatanController {
	@Autowired
	private JabatanService jabatanService;
	
	@RequestMapping(value = "/jabatan/tambah", method = RequestMethod.GET)
	private String addJabatan(Model model) {
		model.addAttribute("jabatan", new JabatanModel());
		return "tambah-jabatan";
	}
	
	@RequestMapping(value = "/jabatan/tambah", method = RequestMethod.POST)
	private String saveJabatan(@ModelAttribute JabatanModel jabatan) {
		jabatanService.addJabatan(jabatan);
		return "add";
	}
	
	@RequestMapping(value = "/jabatan/view", method = RequestMethod.GET)
	private String viewJabatan(@RequestParam(value = "id") long id, Model model) {
		model.addAttribute("jabatan", jabatanService.findJabatanById(id));
		return "view-jabatan";
	}
	
	@RequestMapping(value = "/jabatan/ubah", method = RequestMethod.GET)
	private String ubahJabatan(@RequestParam(value = "idJabatan") long idJabatan, Model model) {
		JabatanModel modelJabatan = jabatanService.findJabatanById(idJabatan);
		model.addAttribute("modelJabatan", modelJabatan);
		return "ubah-jabatan";
	}
	
	@RequestMapping(value = "/jabatan/ubah", method = RequestMethod.POST)
	private String ubahJabatanFinal(@ModelAttribute JabatanModel jabatan, Model model) {
		jabatanService.addJabatan(jabatan);
		return "add";
	}
	
	@RequestMapping(value = "/jabatan/hapus", method = RequestMethod.GET)
    private String hapus(@RequestParam(value = "idJabatan") long id, Model model) {
		jabatanService.deleteJabatanById(id);
        return "hapus-jabatan";
    }
	
	@RequestMapping(value = "/jabatan/viewall", method = RequestMethod.GET)
	public String lihatSemuaJabatan(Model model) {
		List<JabatanModel> listJabatan = jabatanService.jabatanList();
		for (JabatanModel jabatan:listJabatan) {
			jabatan.setSizePegawai(jabatan.jabatanSize());
		}
		model.addAttribute("listJabatan", listJabatan);
		return "semua-jabatan";
	}
	
}
