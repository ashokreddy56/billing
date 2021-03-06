package org.mifosplatform.billing.order.handler;

import org.mifosplatform.billing.order.service.OrderWritePlatformService;
import org.mifosplatform.commands.handler.NewCommandSourceHandler;
import org.mifosplatform.infrastructure.core.api.JsonCommand;
import org.mifosplatform.infrastructure.core.data.CommandProcessingResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RetrackOsdMessageOrderCommandHandler implements NewCommandSourceHandler  {
	 private final OrderWritePlatformService writePlatformService;

	    @Autowired
	    public RetrackOsdMessageOrderCommandHandler(final OrderWritePlatformService writePlatformService) {
	        this.writePlatformService = writePlatformService;
	    }

	    @Transactional
	    @Override
	public CommandProcessingResult processCommand(JsonCommand command) {
		
		return this.writePlatformService.retrackOsdMessage(command);
	}

}
