/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.unice.vicc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import org.cloudbus.cloudsim.Host;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.VmAllocationPolicy;

/**
 *
 * @author Malou
 */
public class MyAntiAffinityPolicy extends VmAllocationPolicy{
    private Map<String, Host> vmTable;
    public MyAntiAffinityPolicy(List<? extends Host> list) {
        super(list);
        vmTable=new HashMap<>();
        
    }

    @Override
    public boolean allocateHostForVm(Vm vm) {
        
        int hostTotal = getHostList().size();
        int id = vm.getId();
       int index = id%100;
        int alt=hostTotal/100;
        for(int i=0;i<alt;i++){
             Host host = getHostList().get(index +100*i);
            if(host.vmCreate(vm)){
                vmTable.put(vm.getUid(), host);
                return true;
            }
       }
        return false;
    }

    @Override
    public boolean allocateHostForVm(Vm vm, Host host) {
            if(host.vmCreate(vm)){
                vmTable.put(vm.getUid(), host);
                return true;
            }
            return false;
    }

    @Override
    public List<Map<String, Object>> optimizeAllocation(List<? extends Vm> list) {
        return null;
    }
    

    @Override
    public void deallocateHostForVm(Vm vm) {
        vmTable.get(vm.getUid()).vmDestroy(vm);
    }

    @Override
    public Host getHost(Vm vm) {
        return vmTable.get(vm.getUid());
    }

    @Override
    public Host getHost(int i, int i1) {
        return vmTable.get(Vm.getUid(i1, i));
    }
    
}
