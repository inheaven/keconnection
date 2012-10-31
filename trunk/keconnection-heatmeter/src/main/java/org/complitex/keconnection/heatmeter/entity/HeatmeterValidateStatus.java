package org.complitex.keconnection.heatmeter.entity;

/**
 * @author Anatoly A. Ivanov java@inheaven.ru
 *         Date: 30.10.12 12:59
 */
public enum HeatmeterValidateStatus {
    VALID,
    ERROR_PERIOD_TYPE_REQUIRED,
    ERROR_PERIOD_BEGIN_DATE_REQUIRED,
    ERROR_PERIOD_MORE_THAN_TWO_OPEN_OPERATION,
    ERROR_PERIOD_MORE_THAN_TWO_OPEN_ADJUSTMENT,
    ERROR_PERIOD_INTERSECTION,
    ERROR_PERIOD_BEGIN_DATE_AFTER_END_DATE,
    ERROR_PERIOD_OPERATION_MUST_ENCLOSES_ADJUSTMENT,

    ERROR_CONNECTION_BEGIN_DATE_REQUIRED,
    ERROR_CONNECTION_NOT_FOUND,
    ERROR_CONNECTION_INTERSECTION,

    ERROR_PAYLOAD_BEGIN_DATE_REQUIRED,
    ERROR_PAYLOAD_VALUES_REQUIRED,
    ERROR_PAYLOAD_SUM_100,
    ERROR_PAYLOAD_INTERSECTION,

    ERROR_CONSUMPTION_READOUT_DATE_REQUIRED,
    ERROR_CONSUMPTION_VALUE_REQUIRED,
    ERROR_CONSUMPTION_INTERSECTION
}
