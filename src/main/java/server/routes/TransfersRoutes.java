package server.routes;

import com.google.gson.Gson;
import model.dto.TransferRequest;
import model.entity.Transfer;
import server.service.TransfersService;
import spark.Route;

public class TransfersRoutes {
    private final TransfersService transfersService;

    public TransfersRoutes(TransfersService transfersService) {
        this.transfersService = transfersService;
    }

    public Route getTransfers() {
        return (request, response) -> transfersService.getAllTransfers();
    }

    public Route getTransferById() {
        return (request, response) -> {
            String id = request.params(":id");
            long trId = Long.parseLong(id);

            return transfersService.getTransferById(trId);
        };
    }

    public Route performTransfer() {
        return (request, response) -> {
            Gson gson = new Gson();
            TransferRequest trReq = gson.fromJson(request.body(), TransferRequest.class);

            Transfer transfer = transfersService.transferAmount(trReq);

            response.status(201);

            return transfer;
        };
    }
}
