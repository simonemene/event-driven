package com.eventdriven.processor.service;

import com.eventdriven.processor.enums.ProcessorEnum;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class RandomAvaiableService implements IRandomAvaiableService{

    @Override
    public String isAvaiable() {
        Random random = new Random();
        return random.nextBoolean()?
                ProcessorEnum.IN_STOCK.getValue():
                ProcessorEnum.OUT_STOCK.getValue();
    }
}
