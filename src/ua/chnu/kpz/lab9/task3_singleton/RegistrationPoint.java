package ua.chnu.kpz.lab9.task3_singleton;

import ua.chnu.kpz.lab9.common.EventLog;
import ua.chnu.kpz.lab9.domain.Shipment;

import java.util.ArrayList;
import java.util.List;

/** Клієнт Singleton — місце реєстрації відправлень (відділення або онлайн-кабінет). */
public class RegistrationPoint {

    private final String name;
    private final WaybillNumberingService numbering;

    public RegistrationPoint(String name) {
        this.name = name;
        this.numbering = WaybillNumberingService.getInstance();
        EventLog.log(name, "Отримано службу нумерації, id = " + numbering.getServiceId());
    }

    public List<String> registerShipments(Shipment... shipments) {
        List<String> numbers = new ArrayList<>();
        for (Shipment s : shipments) {
            String n = numbering.nextNumber();
            s.assignWaybillNumber(n);
            numbers.add(n);
            EventLog.log(name, "Відправлення (" + s.shortInfo() + ") → накладна №" + n);
        }
        return numbers;
    }

    public String getName() {
        return name;
    }

    public WaybillNumberingService getNumbering() {
        return numbering;
    }
}
