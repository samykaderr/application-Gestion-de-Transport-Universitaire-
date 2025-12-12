package Service;

import DATA.BusDAO;
import Model.Bus;

import java.util.List;

public class BusService {

    private final BusDAO busDAO;

    public BusService() {
        this.busDAO = new BusDAO();
    }

    public List<Bus> getAllBuses() {
        return busDAO.getAllBuses();
    }

    public void addBus(Bus bus) {
        busDAO.addBus(bus);
    }

    public void updateBus(Bus bus) {
        busDAO.updateBus(bus);
    }

    public void deleteBus(int id) {
        busDAO.deleteBus(id);
    }
}
