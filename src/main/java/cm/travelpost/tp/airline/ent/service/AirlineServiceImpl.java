package cm.travelpost.tp.airline.ent.service;


import cm.travelpost.tp.airline.ent.dao.AirlineDAO;
import cm.travelpost.tp.airline.ent.vo.AirlineVO;
import cm.travelpost.tp.common.exception.DashboardException;
import cm.travelpost.tp.ws.requests.CommonDTO;
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
    public AirlineVO update(CommonDTO uar) throws DashboardException,Exception {
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
