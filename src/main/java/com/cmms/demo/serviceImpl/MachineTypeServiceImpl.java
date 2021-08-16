package com.cmms.demo.serviceImpl;

import com.cmms.demo.domain.MachineType;
import com.cmms.demo.reponsitory.MachineTypeResponsity;
import com.cmms.demo.service.MachineTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MachineTypeServiceImpl implements MachineTypeService {

    @Autowired
    private MachineTypeResponsity machineTypeResponsity;

    @Override
    public MachineType add(MachineType machineType){
        return machineTypeResponsity.save(machineType);
    }

    @Override
    public List<MachineType> getAllMachineType(){
        return machineTypeResponsity.findAll();
    }

    @Override
    public MachineType getOne(Long id){
        return machineTypeResponsity.findById(id).orElse(null);
    }

    @Override
    public MachineType update(Long id, MachineType item){
        MachineType itemToUpdate = getOne(id);
        if( itemToUpdate !=null) {
            itemToUpdate.setType_name(item.getType_name());
            return machineTypeResponsity.save(itemToUpdate);
        }
        return null;
    }

    @Override
    public MachineType delete(Long id){
        MachineType item = getOne(id);
        if (item != null){
            machineTypeResponsity.delete(item);
            return item;
        }
        return null;
    }

}
