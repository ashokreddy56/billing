/**
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.mifosplatform.billing.inventory.serialization;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.mifosplatform.infrastructure.core.api.JsonCommand;
import org.mifosplatform.infrastructure.core.data.ApiParameterError;
import org.mifosplatform.infrastructure.core.data.DataValidatorBuilder;
import org.mifosplatform.infrastructure.core.exception.InvalidJsonException;
import org.mifosplatform.infrastructure.core.exception.PlatformApiDataValidationException;
import org.mifosplatform.infrastructure.core.serialization.FromJsonHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;

/**
 * Deserializer for code JSON to validate API request.
 */
@Component
public final class InventoryItemCommandFromApiJsonDeserializer {

    /**
     * The parameters supported for this command.
     */



	private final Set<String> supportedParameters = new HashSet<String>(Arrays.asList("grnId","itemMasterId","quality","serialNumber","provisioningSerialNumber", 
			"remarks","status","warranty","locale","officeId","clientId","inventorylisttable_length","flag"));

    
    private final FromJsonHelper fromApiJsonHelper;

    @Autowired
    public InventoryItemCommandFromApiJsonDeserializer(final FromJsonHelper fromApiJsonHelper) {
        this.fromApiJsonHelper = fromApiJsonHelper;
    }

    public void validateForCreate(final JsonCommand command) {
        if (StringUtils.isBlank(command.toString())) { throw new InvalidJsonException(); }

        final Type typeOfMap = new TypeToken<Map<String, Object>>() {}.getType();
        fromApiJsonHelper.checkForUnsupportedParameters(typeOfMap, command.json(),supportedParameters);

        final List<ApiParameterError> dataValidationErrors = new ArrayList<ApiParameterError>();
        final DataValidatorBuilder baseDataValidator = new DataValidatorBuilder(dataValidationErrors).resource("item");

        //final JsonElement element = fromApiJsonHelper.parse(command);
        //final JsonElement jsonElement = fromApiJsonHelper.parse(command.toString());

        final Integer itemMasterId = command.integerValueOfParameterNamed("itemMasterId");
        
        final Integer grnId = command.integerValueOfParameterNamed("grnId");
        
        final String remarks = command.stringValueOfParameterNamed("remarks");
        final String serialNumber = command.stringValueOfParameterNamed("serialNumber");
        final String provisioningSerialNumber  = command.stringValueOfParameterNamed("provisioningSerialNumber");
        final String status  = command.stringValueOfParameterNamed("status");
        
        
        final String quality = command.stringValueOfParameterNamed("quality");
        
		baseDataValidator.reset().parameter("itemMasterId").value(itemMasterId).notNull();
		
		baseDataValidator.reset().parameter("serialNumber").value(serialNumber).notBlank().notNull();
		baseDataValidator.reset().parameter("provisioningSerialNumber").value(provisioningSerialNumber).notBlank().notNull();
		baseDataValidator.reset().parameter("status").value(status).notNull();
		baseDataValidator.reset().parameter("quality").value(quality).notNull();
		baseDataValidator.reset().parameter("grnId").value(grnId).notBlank();
		

        throwExceptionIfValidationWarningsExist(dataValidationErrors);
    }

    public void validateForUpdate(final String json) {
        if (StringUtils.isBlank(json)) { throw new InvalidJsonException(); }

        final Type typeOfMap = new TypeToken<Map<String, Object>>() {}.getType();
        fromApiJsonHelper.checkForUnsupportedParameters(typeOfMap, json, supportedParameters);

        final List<ApiParameterError> dataValidationErrors = new ArrayList<ApiParameterError>();
        final DataValidatorBuilder baseDataValidator = new DataValidatorBuilder(dataValidationErrors).resource("code");

        final JsonElement element = fromApiJsonHelper.parse(json);
        if (fromApiJsonHelper.parameterExists("name", element)) {
            final String name = fromApiJsonHelper.extractStringNamed("name", element);
            baseDataValidator.reset().parameter("name").value(name).notBlank().notExceedingLengthOf(100);
        }

        throwExceptionIfValidationWarningsExist(dataValidationErrors);
    }

    private void throwExceptionIfValidationWarningsExist(final List<ApiParameterError> dataValidationErrors) {
        if (!dataValidationErrors.isEmpty()) { throw new PlatformApiDataValidationException("validation.msg.validation.errors.exist",
                "Validation errors exist.", dataValidationErrors); }
    }
}