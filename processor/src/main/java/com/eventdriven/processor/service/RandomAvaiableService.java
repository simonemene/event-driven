package com.eventdriven.processor.service;

import com.eventdriven.processor.enums.ProcessorEnum;

import java.util.Random;

public class RandomAvaiableService implements IRandomAvaiableService{

    @Override
    public String isAvaiable() {
        Random random = new Random();
        return random.nextBoolean()?
                ProcessorEnum.IN_STOCK.getValue():
                ProcessorEnum.OUT_STOCK.getValue();
    }
}
