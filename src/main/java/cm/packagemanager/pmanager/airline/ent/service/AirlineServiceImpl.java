package cm.packagemanager.pmanager.airline.ent.service;


import cm.packagemanager.pmanager.airline.ent.dao.AirlineDAO;
import cm.packagemanager.pmanager.airline.ent.vo.AirlineVO;
import cm.packagemanager.pmanager.common.exception.DashboardException;
import cm.packagemanager.pmanager.ws.requests.airplane.UpdateAirlineDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AirlineServiceImpl implements AirlineService {

    @Autowired
    protected AirlineDAO airlineDAO;

    @Override
    public AirlineVO add(String code, String description) throws DashboardException,Exception {

        return airlineDAO.add(code,description);
    }

    @Override
    public AirlineVO update(UpdateAirlineDTO uar) throws DashboardException,Exception {
        return airlineDAO.update(uar);
    }

    @Override
    public AirlineVO findByCode(String code) throws DashboardException,Exception {
        return airlineDAO.findByCode(code);
    }

    @Override
    public List<AirlineVO> all() throws Exception {
        return airlineDAO.all();
    }

    @Override
    public boolean delete(Long id) {
        return false;
    }
}
