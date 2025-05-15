package com.gbv.webapp_gbv_l3.service;

import com.gbv.webapp_gbv_l3.entity.Worker;
import com.gbv.webapp_gbv_l3.repository.WorkerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WorkerServiceImpl implements WorkerService{

    @Autowired
    private WorkerRepository workerRepository;
    @Override
    public List<Worker> getAllWorkers() {
        return workerRepository.findAll();
    }

    @Override
    public String insertWorker(Worker worker) {
        if (workerRepository.existsByCode(worker.getCode())) {
            return "Error: Worker code already exists!";
        }
        try{
            workerRepository.save(worker);
        }catch (Exception e) {
            return "Error inserting worker: " + e.getMessage();
        }
        return "Worker successfully inserted!";

    }

    @Override
    public String updateWorker(Worker worker) {
        if (workerRepository.existsByCodeAndIdNot(worker.getCode(), worker.getId())) {
            return "Error: Worker code already exists!";
        }
        try{
            workerRepository.save(worker);
        }catch (Exception e) {
            return "Error updating worker: " + e.getMessage();
        }
        return "Worker successfully edited!";

    }

    @Override
    public Worker getById(Long id) {
        return workerRepository.getById(id);
    }

    @Override
    public void deleteById(Long id) {
        workerRepository.deleteById(id);
    }
}
