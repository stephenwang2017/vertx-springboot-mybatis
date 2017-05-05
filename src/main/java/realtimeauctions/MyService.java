package realtimeauctions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class MyService {

	@Autowired
	private MyDAO myDAO;
	
	public String getCurrentDataTime() {
		return myDAO.getCurrentTime();
	}
}
