package cm.travelpost.tp.airline.ent.service;


import cm.travelpost.tp.airline.ent.dao.AirlineDAO;
import cm.travelpost.tp.airline.ent.vo.AirlineIdVO;
import cm.travelpost.tp.airline.ent.vo.AirlineVO;
import cm.travelpost.tp.common.exception.DashboardException;
import cm.travelpost.tp.ws.requests.CommonDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class AirlineServiceImpl implements AirlineService {

    @Autowired
    protected AirlineDAO dao;

    @Override
    public AirlineVO add(String code, String description) throws DashboardException,Exception {

        return dao.add(code,description);
    }

    @Override
    public AirlineVO update(CommonDTO uar) throws DashboardException,Exception {
        return dao.update(uar);
    }

    @Override
    public AirlineVO findByCode(String code) throws DashboardException,Exception {
        return dao.findByCode(code);
    }

    @Override
    public List<AirlineVO> all() throws Exception {
        return dao.all();
    }

    @Override
    public boolean delete(AirlineIdVO id) {
        return dao.delete(id);
    }
}
