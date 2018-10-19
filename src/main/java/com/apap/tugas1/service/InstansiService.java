package com.apap.tugas1.service;

import java.util.List;

import com.apap.tugas1.model.InstansiModel;

public interface InstansiService {
	List<InstansiModel> listInstansi();
	InstansiModel getInstansiById(long id);
}
