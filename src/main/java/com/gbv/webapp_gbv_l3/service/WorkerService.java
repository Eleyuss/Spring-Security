package com.gbv.webapp_gbv_l3.service;

import com.gbv.webapp_gbv_l3.entity.Department;
import com.gbv.webapp_gbv_l3.entity.Worker;

import java.util.List;

public interface WorkerService {
    List<Worker> getAllWorkers();
    String insertWorker(Worker worker);
    String updateWorker(Worker worker);
    Worker getById(Long id);
    void deleteById(Long id);
}
