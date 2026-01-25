package com.eventdriven.processor.service;

import com.eventdriven.processor.enums.ProcessorEnum;

import java.util.Random;

public class RandomAvailableService implements IRandomAvailableService {

    @Override
    public String isAvaiable() {
        Random random = new Random();
        return random.nextBoolean()?
                ProcessorEnum.IN_STOCK.getValue():
                ProcessorEnum.OUT_STOCK.getValue();
    }
}
