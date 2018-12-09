package server.service;

import db.InMemoryRepository;
import model.dto.TransferRequest;
import model.entity.Transfer;

import java.util.List;

public class TransfersService {
    private final InMemoryRepository repository;

    public TransfersService(InMemoryRepository repository) {
        this.repository = repository;
    }

    public List<Transfer> getAllTransfers() {
        return repository.getAllTransfers();
    }

    public Transfer getTransferById(long trId) {
        return repository.getTransferById(trId);
    }

    public Transfer transferAmount(TransferRequest trReq) {
        return repository.performTransfer(trReq);
    }
}
